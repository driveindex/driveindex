import React, {useMemo, useState} from "react";
import {Breadcrumb} from "@hi-ui/breadcrumb";
import {FileList} from "Frontend/views/_component/home/FileList";
import useBreadcrumb from "Frontend/core/hooks/useBreadcrumb";
import {useBreakpointDown, useBreakpointUp} from "Frontend/core/hooks/useViewport";
import {CommonHeader} from "Frontend/views/_component/home/CommonHeader";
import {ViewConfig} from "@vaadin/hilla-file-router/types.js";
import "./@index.css"
import {FileController} from "Frontend/generated/endpoints";
import GetDirReqSort from "Frontend/generated/io/github/driveindex/dto/req/user/GetDirReqSort";
import FileListRespDto from "Frontend/generated/io/github/driveindex/dto/resp/FileListRespDto";
import {useUrlArgs} from "Frontend/core/utils/_Router";
import {key, translate} from "@vaadin/hilla-react-i18n";
import {Button, HorizontalLayout, Icon, VerticalLayout} from "@vaadin/react-components";
import CreateDirDialog from "Frontend/views/_component/home/CreateDirDialog";

export const config: ViewConfig = {
    loginRequired: true,
};

interface MainViewQueryParams {
    path: string;
    pageIndex: number;
    pageSize: number;
}

const MainView = () => {
    const urlArgs = useUrlArgs<MainViewQueryParams>()
    const pageIndex = urlArgs.query.pageIndex ?? 0
    const pageSize = urlArgs.query.pageSize ?? 15

    const [ fileList, setFileList ] = useState<FileListRespDto | undefined>(undefined)
    const updateFileList = (path: string) => {
        FileController.listFile({
            path: path,
            sortBy: GetDirReqSort.NAME,
            asc: false,
            pageIndex: pageIndex,
            pageSize: pageSize,
        }).then((result) => {
            if (result.code === 200) {
                setFileList(result.data)
                return
            }
        })
    }
    useMemo(() => {
        if (urlArgs.query.path == undefined) {
            urlArgs.setQuery({
                path: "/"
            })
        } else {
            updateFileList(urlArgs.query.path)
        }
    }, [urlArgs.query.path])

    const isMdUp = useBreakpointUp("md")
    const showAsMobile = useBreakpointDown("sm")
    const contentWidth = isMdUp ? 740 : "100%"

    const breadcrumbData = useBreadcrumb(urlArgs.query.path)

    const breadcrumb = (
        <Breadcrumb
            data={breadcrumbData}
            onClick={(e, i, index) => {
                const targetPath = breadcrumbData[index].path
                if (targetPath == urlArgs.query.path) {
                    return
                }
                urlArgs.setQuery({
                    path: targetPath
                })
            }}
            style={{
                margin: "0 20px",
            }} />
    )

    const [ createDirShow, showCreateDir ] = useState(false)
    const [ createLinkShow, showCreateLink ] = useState(false)
    const [ createDirLoading, setCreateLoading ] = useState(false)

    return (
        <>
            <CommonHeader isShowInProfile={false} />
            <VerticalLayout
                style={{
                    display: "flex",
                    alignItems: "center",
                    flexDirection: "column",
                }}>
                <VerticalLayout style={{width: contentWidth}}>
                    <HorizontalLayout
                        style={{
                            padding: "0 " + (isMdUp ? 0 : 20) + "px",
                            width: "100%",
                        }}
                        theme={"spacing"}>
                        <Button
                            theme={"primary large"}
                            onClick={() => showCreateDir(true)}>
                            <Icon icon={"vaadin:plus"} />
                            {translate(key`home.file.create.dir`)}
                        </Button>
                        <Button
                            theme={"secondary large"}
                            onClick={() => showCreateLink(true)}>
                            <Icon icon={"vaadin:link"} />
                            {translate(key`home.file.create.mount`)}
                        </Button>
                    </HorizontalLayout>
                    {
                        showAsMobile ? (
                            breadcrumb
                        ) : (
                            <div
                                style={{
                                    borderRadius: isMdUp ? 10 : 0,
                                    backgroundColor: "#FFFFFF",
                                    paddingTop: 6,
                                    paddingBottom: 6,
                                    marginTop: 20,
                                    width: "100%",
                                }}>
                                {breadcrumb}
                            </div>
                        )
                    }
                    {
                        isMdUp && (
                            <div style={{width: 40}} />
                        )
                    }
                    <FileList
                        isMdUp={isMdUp}
                        showAsMobile={showAsMobile}
                        data={fileList}
                        pageIndex={pageIndex}
                        pageSize={pageSize}
                        onCurrentPageChanged={(pageIndex: number) => urlArgs.setQuery({
                            pageIndex: pageIndex - 1
                        })}
                        onPageSizeChanged={(pageSize: number) => urlArgs.setQuery({
                            pageSize: pageSize
                        })}/>
                </VerticalLayout>
            </VerticalLayout>
            {
                urlArgs.query.path && (
                    <>
                        <CreateDirDialog
                            visible={createDirShow}
                            currentPath={urlArgs.query.path}
                            onClose={(created: boolean) => {
                                showCreateDir(false)
                                if (created) {
                                    updateFileList(urlArgs.query.path!)
                                }
                            }} />
                    </>
                )
            }
        </>
    )
}

export default MainView
