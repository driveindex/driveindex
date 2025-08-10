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
import {useQueryLocation} from "Frontend/core/util/_Router";
import {key, translate} from "@vaadin/hilla-react-i18n";
import {Button, HorizontalLayout, Icon, VerticalLayout} from "@vaadin/react-components";
import CreateDirDialog from "Frontend/views/_component/home/CreateDirDialog";

export const config: ViewConfig = {
    loginRequired: true,
};

interface MainViewQueryParams {
    path?: string;
    pageIndex?: number;
    pageSize?: number;
}

const MainView = () => {
    const queryLocation = useQueryLocation<MainViewQueryParams>()
    const pageIndex = queryLocation.pageIndex ?? 0
    const pageSize = queryLocation.pageSize ?? 15

    const [ fileList, setFileList ] = useState<FileListRespDto | undefined>(undefined)
    const updateFileList = (path?: string) => {
        if (path === undefined) {
            path = queryLocation.path
        }
        FileController.listFile({
            path: path!,
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
        if (queryLocation.path == undefined) {
            queryLocation.set({
                path: "/"
            })
        } else {
            updateFileList(queryLocation.path)
        }
    }, [queryLocation.path])

    const isMdUp = useBreakpointUp("md")
    const showAsMobile = useBreakpointDown("sm")
    const contentWidth = isMdUp ? 740 : "100%"

    const breadcrumbData = useBreadcrumb(queryLocation.path)

    const breadcrumb = (
        <Breadcrumb
            data={breadcrumbData}
            onClick={(e, i, index) => {
                const targetPath = breadcrumbData[index].path
                if (targetPath == queryLocation.path) {
                    return
                }
                queryLocation.set({
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
                        }}>
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
                            <VerticalLayout
                                style={{
                                    borderRadius: isMdUp ? 10 : 0,
                                    backgroundVerticalLayoutor: "#FFFFFF",
                                    paddingTop: 6,
                                    paddingBottom: 6,
                                    marginTop: 20
                                }}>
                                {breadcrumb}
                                {
                                    isMdUp && (
                                        <div style={{width: 40}} />
                                    )
                                }
                            </VerticalLayout>
                        )
                    }
                    <FileList
                        isMdUp={isMdUp}
                        showAsMobile={showAsMobile}
                        data={fileList}
                        pageIndex={pageIndex}
                        pageSize={pageSize}
                        onCurrentPageChanged={(pageIndex: number) => queryLocation.set({
                            pageIndex: pageIndex - 1
                        })}
                        onPageSizeChanged={(pageSize: number) => queryLocation.set({
                            pageSize: pageSize
                        })}/>
                </VerticalLayout>
            </VerticalLayout>
            {
                queryLocation.path && (
                    <>
                        <CreateDirDialog
                            visible={createDirShow}
                            currentPath={queryLocation.path}
                            onClose={(created: boolean) => {
                                showCreateDir(false)
                                if (created) {
                                    updateFileList()
                                }
                            }} />
                    </>
                )
            }
        </>
    )
}

export default MainView
