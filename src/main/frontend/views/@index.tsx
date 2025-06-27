import React, {Dispatch, FC, SetStateAction, useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import {UserPref} from "Frontend/driveindex/prefs/UserPref";
import {useTranslation} from "react-i18next";
import {Breadcrumb, Button, Col, Form, FormItem, FormSubmit, Input, message, Modal, Row, Scrollbar} from "@hi-ui/hiui";
import {PlusOutlined, LinkOutlined} from "@hi-ui/icons"
import {BreadcrumbContainer, FileList} from "Frontend/components/home/FileList";
import {useQuery} from "Frontend/driveindex/hooks/useQuery";
import useBreadcrumb from "Frontend/driveindex/hooks/useBreadcrumb";
import {useBreakpointDown, useBreakpointUp} from "Frontend/driveindex/hooks/useViewport";
import {DriveIndexAPI} from "Frontend/driveindex/axios";
import {checkLoginStatus, useLoginExpiredDialog} from "Frontend/driveindex/hooks/useLoginExpiredDialog";
import {TFunction} from "i18next";
import {CommonHeader} from "Frontend/components/home/CommonHeader";

const Index = () => {
    const navigate = useNavigate()
    useEffect(() => {
        if (!UserPref.Login) {
            navigate("/login")
        }
    }, [navigate]);
    const { t } = useTranslation()

    const query = useQuery()
    const path = query.get("path")

    const showLoginExpiredDialog = useLoginExpiredDialog()
    const [ fileList, setFileList ] = useState<{}[] | undefined>(undefined)
    useEffect(() => {
        if (path == null) {
            navigate("/?path=%2F")
        } else {
            getFileListByPath(path, setFileList, showLoginExpiredDialog, t)
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
                navigate(breadcrumbData[index].link)
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
                            list={fileList}/>
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

function getFileListByPath(
    path: string,
    setFileList: Dispatch<SetStateAction<{}[] | undefined>>,
    showLoginExpiredDialog: () => void,
    t: TFunction<"translation", undefined>,
) {
    setFileList(undefined)
    DriveIndexAPI.get("/api/user/file/list", {
        params: {
            path: path
        },
        headers: {
            Authorization: true
        },
    }).then((resp) => {
        if (!checkLoginStatus(resp, showLoginExpiredDialog)) {
            return
        }
        if (resp.data["code"] !== 200) {
            message.open({
                title: t("home_list_error") + resp.data["message"],
                type: "error",
            })
        } else {
            setFileList(resp.data["data"]["content"])
        }
    })
}

function createDir(
    path: string,
    showLoginExpiredDialog: () => void,
    t: TFunction<"translation", undefined>,
) {

}

export default Index