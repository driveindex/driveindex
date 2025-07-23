import React, {useMemo, useState} from "react";
import {Breadcrumb, Button, Col, Form, FormItem, FormSubmit, Input, Modal, Row, Scrollbar} from "@hi-ui/hiui";
import {PlusOutlined, LinkOutlined} from "@hi-ui/icons"
import {BreadcrumbContainer, FileList} from "Frontend/views/_component/home/FileList";
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

    const [ fileList, setFileList ] = useState<FileListRespDto | undefined>(undefined)
    const updateFileList = (path: string) => {
        FileController.listFile({
            path: path,
            sortBy: GetDirReqSort.NAME,
            asc: false,
            pageIndex: queryLocation.pageIndex ?? 0,
            pageSize: queryLocation.pageSize ?? 15,
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
            <Col
                style={{
                    display: "flex",
                    alignItems: "center",
                    flexDirection: "column",
                }}>
                <Col style={{width: contentWidth}}>
                    <Row
                        style={{
                            padding: "0 " + (isMdUp ? 0 : 20) + "px",
                        }}>
                        <Button
                            type={"primary"}
                            icon={<PlusOutlined />}
                            size={"lg"}
                            onClick={() => showCreateDir(true)}>
                            {translate(key`home.file.create.dir`)}
                        </Button>
                        <Button
                            type={"secondary"}
                            icon={<LinkOutlined />}
                            size={"lg"}
                            onClick={() => showCreateLink(true)}>
                            {translate(key`home.file.create.mount`)}
                        </Button>
                    </Row>
                    {
                        showAsMobile || (
                            <BreadcrumbContainer marginTop={20} isMdUp={isMdUp}>
                                {breadcrumb}
                            </BreadcrumbContainer>
                        )
                    }
                    <Scrollbar
                        style={{
                            marginTop: 20,
                            display: "flex",
                            height: "calc(100vh - 194px)"
                        }}>
                        <FileList
                            isMdUp={isMdUp}
                            showAsMobile={showAsMobile}
                            breadcrumb={breadcrumb}
                            list={fileList?.content}/>
                    </Scrollbar>
                </Col>
            </Col>
            <Modal
                visible={createDirShow}
                title={translate(key`home.file.create.dir`)}
                onClose={() => showCreateDir(false)}
                onConfirm={() => {
                    setCreateLoading(true)
                }}
                confirmText={null}
                cancelText={null}
                disabledPortal={createDirLoading}>
                <Form
                    initialValues={{ name: "" }}
                    rules={{
                        name: [
                            {
                                required: true,
                                type: "string",
                                message: translate(key`home.file.create.name.error.empty`),
                            },
                        ]
                    }}>
                    <FormItem
                        field={"name"}
                        labelPlacement={"top"}
                        label={translate(key`home.file.create.name`)}>
                        <Input />
                    </FormItem>
                    <FormSubmit
                        onClick={(value, _) => {
                            if (value === null) {
                                return
                            }
                        }}>
                        {translate(key`home.file.create.action`)}
                    </FormSubmit>
                </Form>
            </Modal>
        </>
    )
}

export default MainView
