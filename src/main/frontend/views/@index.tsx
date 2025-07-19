import React, {useEffect, useState} from "react";
import {useTranslation} from "react-i18next";
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

export const config: ViewConfig = {
    loginRequired: true,
};

interface MainViewQueryParams {
    path?: string;
    pageIndex?: number;
    pageSize?: number;
}

export default function MainView() {
    const queryLocation = useQueryLocation<MainViewQueryParams>()
    const { t } = useTranslation()

    const path = queryLocation.path
    const pageIndex = Number(queryLocation.pageIndex ?? 0)
    const pageSize = Number(queryLocation.pageSize ?? 15)

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
    useEffect(() => {
        if (path == undefined) {
            queryLocation.set({
                path: "/"
            })
        } else {
            updateFileList(path)
        }
    }, [path])

    const isMdUp = useBreakpointUp("md")
    const showAsMobile = useBreakpointDown("sm")
    const contentWidth = isMdUp ? 740 : "100%"

    const breadcrumbData = useBreadcrumb(t, path)

    const breadcrumb = (
        <Breadcrumb
            data={breadcrumbData}
            onClick={(e, i, index) => {
                const targetPath = breadcrumbData[index].path
                if (targetPath == path) {
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
                            {t("home_file_create_dir")}
                        </Button>
                        <Button
                            type={"secondary"}
                            icon={<LinkOutlined />}
                            size={"lg"}
                            onClick={() => showCreateLink(true)}>
                            {t("home_file_create_link")}
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
                title={t("home_file_create_dir")}
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
                                message: t("home_file_create_empty"),
                            },
                        ]
                    }}>
                    <FormItem
                        field={"name"}
                        labelPlacement={"top"}
                        label={t("home_file_create_name")}>
                        <Input />
                    </FormItem>
                    <FormSubmit
                        onClick={(value, _) => {
                            if (value === null) {
                                return
                            }
                        }}>
                        {t("home_file_create_action")}
                    </FormSubmit>
                </Form>
            </Modal>
        </>
    )
}
