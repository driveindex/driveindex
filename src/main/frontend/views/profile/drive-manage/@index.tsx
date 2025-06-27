import React, {FC, useEffect, useState} from "react";
import {useTranslation} from "react-i18next";
import {
    Avatar,
    Breadcrumb,
    Button,
    Card, Col,
    List,
    ListDataItem,
    ListItem, Menu,
    message,
    Popover,
    Row,
    Scrollbar
} from "@hi-ui/hiui";
import {LoadingCover, useLoading} from "Frontend/driveindex/hooks/useLoading";
import {BreadcrumbDataItem} from "@hi-ui/breadcrumb/lib/types/types";
import {DriveIndexAPI, DriveType} from "Frontend/driveindex/axios";
import {AxiosResponse} from "axios";
import {checkLoginStatus, useLoginExpiredDialog} from "Frontend/driveindex/hooks/useLoginExpiredDialog";
import {useLocation, useNavigate, useParams} from "react-router-dom";
import "./@index.css"
import {BarsOutlined, PlusOutlined} from "@hi-ui/icons"
import Ic_OneDrive from "Frontend/static/drawable/drive/onedrive.svg"
import Ic_Unknown from "Frontend/static/drawable/drive/unknown.svg"
import RespLayoutProps from "Frontend/driveindex/props/RespLayoutProps";
import {useBreakpointDown, useBreakpointUp} from "Frontend/driveindex/hooks/useViewport";
import {asInitials} from "Frontend/driveindex/util/_String";
import ClientCreationDialog from "Frontend/components/profile/drive/ClientCreationDialog";


type ClientBreadcrumbDataItem = BreadcrumbDataItem & {
    targetClient?: string
}

const Index: FC = () => {
    const { t } = useTranslation()

    const { state } = useLocation()

    const isMdUp = useBreakpointUp("md")
    const showAsMobile = useBreakpointDown("sm")

    return (
        <div
            className={"dirveindex-profile-drive"}
            style={{
                display: "flex",
                flexDirection: "column",
                height: "100%"
            }}>
            <h2>{t("profile_drive_title")}</h2>
            <p>{t("profile_drive_text")}</p>
            <LoadingCover>
                <DriveList client={state} isMdUp={isMdUp} showAsMobile={showAsMobile} />
            </LoadingCover>
        </div>
    )
}

interface ClientState {
    name: string,
    type: DriveType,
}

interface DriveListProps {
    client: ClientState | null
}
const DriveList: FC<DriveListProps & RespLayoutProps> = (props) => {
    const { t } = useTranslation()

    const [ clientCreating, showClientCreating ] = useState<boolean>(false)
    const [ clientTarget, setClientTarget ] = useState<any | undefined>()
    const [ clientList, setClientList ] = useState<any[]>([])

    const [ accountCreating, showAccountCreating ] = useState(false)
    const [ accountList, setAccountList ] = useState<any[]>([])

    const { clientId } = useParams()

    const navigate = useNavigate()
    const { setLoading } = useLoading()
    const showLoginExpiredDialog = useLoginExpiredDialog()

    const [
        breadcrumbData,
        setBreadcrumbData
    ] = useState<ClientBreadcrumbDataItem[]>()
    const refreshContent = () => {
        setLoading(true)
        setTimeout(() => {
            let request: Promise<AxiosResponse<any, any>>
            if (clientId === undefined) {
                request = DriveIndexAPI.get("/api/user/client")
            } else {
                request = DriveIndexAPI.get("/api/user/account", {
                    params: {
                        client_id: clientId
                    }
                })
            }
            request.then((resp) => {
                if (!checkLoginStatus(resp, showLoginExpiredDialog)) {
                    return
                }
                if (resp.data["code"] !== 200) {
                    message.open({
                        title: t("profile_drive_failed") + resp.data["message"],
                        type: "error",
                    })
                    return
                }
                if (clientId === undefined) {
                    setClientList(resp.data["data"])
                    setAccountList([])
                } else {
                    setAccountList(resp.data["data"])
                }
            }).finally(() => {
                setLoading(false)
            })
        }, 200)
    }
    const creatingAccount = () => {
        showAccountCreating(true)
        const type = props.client!!.type
        console.log("type: ", type)
        if (type === DriveType.OneDrive) {
            DriveIndexAPI.get("/api/user/login/url/onedrive", {
                params: {
                    client_id: clientId,
                    redirect_uri: "http://localhost:8009/redirect/drive/onedrive",
                }
            }).then((resp) => {
                if (!checkLoginStatus(resp, showLoginExpiredDialog)) {
                    return
                }
                if (resp.data["code"] !== 200) {
                    message.open({
                        title: t("profile_drive_account_failed") + resp.data["message"],
                        type: "error",
                    })
                    return
                }
                window.open(resp.data["data"]["redirect_url"], "_self")
            })
        } else {
            showAccountCreating(false)
            message.open({
                title: t("internal_error") + t("profile_drive_account_internal_unknown_type"),
                type: "error",
            })
        }
    }
    useEffect(() => {
        showAccountCreating(false)
        const base: ClientBreadcrumbDataItem[] = [
            {
                title: t("profile_drive_breadcrumb"),
            }
        ]
        if (clientId === undefined) {
            setBreadcrumbData(base)
            refreshContent()
            return
        }
        if (props.client !== null) {
            base.push({
                title: props.client.name
            })
            setBreadcrumbData(base)
            refreshContent()
            return
        }
        setLoading(true)
        DriveIndexAPI.get("/api/user/client").then((resp) => {
            if (!checkLoginStatus(resp, showLoginExpiredDialog)) {
                return
            }
            if (resp.data["code"] !== 200) {
                message.open({
                    title: t("profile_drive_failed") + resp.data["message"],
                    type: "error",
                })
                setLoading(false)
                return
            }
            const data = resp.data["data"]
            for (const index in data) {
                if (data[index]["id"] !== clientId) {
                    continue
                }
                setLoading(false)
                navigate("/profile/drive/"+clientId, {
                    state: {
                        name: data[index]["name"],
                        type: data[index]["type"],
                    }
                })
                return
            }
            setLoading(false)
            navigate("/profile/drive", {
                state: null
            })
        })
    }, [clientId, props.client])

    return (
        <>
            <Card
                title={
                    <Row style={{padding: "0 6px"}}>
                        <Breadcrumb
                            data={breadcrumbData}
                            onClick={(e, i, index) => {
                                if (index === 0) {
                                    navigate("/profile/drive", {
                                        state: null
                                    })
                                }
                            }}
                            size={"md"}
                            style={{
                                marginBottom: 0,
                                display: "flex",
                                flex: 1,
                            }}/>
                        {
                            props.client === null ?
                                <Button
                                    type={"primary"}
                                    icon={<PlusOutlined />}
                                    onClick={() => {
                                        showClientCreating(true)
                                    }}>{t("profile_drive_add_client")}</Button> :
                                <Button
                                    type={"primary"}
                                    icon={<PlusOutlined />}
                                    loading={accountCreating}
                                    disabled={accountCreating}
                                    onClick={() => {
                                        creatingAccount()
                                    }}>{t("profile_drive_add_account")}</Button>
                        }
                    </Row>
                }
                style={{
                    height: "100%",
                }}
                showHeaderDivider={true}>
                <Scrollbar>
                    <List
                        style={{
                            padding: "0 16px",
                        }}
                        bordered={false}
                        data={(props.client === null ? clientList : accountList).map((item) => {
                            const options: Intl.DateTimeFormatOptions = {
                                year: "numeric",
                                month: "2-digit",
                                day: "2-digit",
                                hour: "2-digit",
                                minute: "2-digit",
                                second: "2-digit"
                            }

                            if (props.client === null) {
                                let icon: string
                                const type = item["type"]
                                if (type === "OneDrive") {
                                    icon = Ic_OneDrive
                                } else {
                                    icon = Ic_Unknown
                                }
                                return {
                                    title: item["name"],
                                    description: t("profile_drive_last_modify") + Intl.DateTimeFormat(
                                        navigator.language, options
                                    ).format(item["modify_at"]),
                                    avatar: icon,
                                    onClick: () => {
                                        navigate("/profile/drive/"+item["id"], {
                                            state: item as ClientState
                                        })
                                    },
                                    onEdit: () => {
                                        showClientCreating(true)
                                        setClientTarget(item)
                                    },
                                    onDelete: () => {

                                    },
                                } as ListDataItem
                            } else {
                                return {
                                    title: item["display_name"] + " (" + item["user_principal_name"] + ")",
                                    description: t("profile_drive_last_modify") + Intl.DateTimeFormat(
                                        navigator.language, options
                                    ).format(item["modify_at"]),
                                    avatar: asInitials(item["display_name"]),
                                    onEdit: () => {

                                    },
                                    onDelete: () => {

                                    },
                                }
                            }
                        })}
                        render={(dataItem: ListDataItem & {
                            onClick?: () => void,
                            onDelete?: () => void,
                            onEdit?: () => void,
                        }) => {
                            return props.client === null ? (
                                <ClientItem {...dataItem} />
                            ) : (
                                <AccountItem {...dataItem} />
                            )
                        }}
                        emptyContent={t("not_found")}
                    />
                </Scrollbar>
            </Card>
            <ClientCreationDialog
                visible={clientCreating}
                requestClose={() => {
                    showClientCreating(false)
                    setClientTarget(undefined)
                }}
                initValue={clientTarget}/>
        </>
    )
}

const ClientItem: FC<ListDataItem & {
    onClick?: () => void
    onDelete?: () => void,
    onEdit?: () => void,
}> = (props) => {
    const [ isShowDesktopAction, setShowDesktopAction ] = useState(false)
    useEffect(() => {
        console.log("isShowDesktopAction: {}", isShowDesktopAction)
    }, [isShowDesktopAction]);
    return (
        <div
            style={{
                display: "flex",
                alignItems: "center",
                flexDirection: "row",
            }}>
            <div style={{
                flex: 1
            }} onClick={() => {
                console.log("client item clicked!")
                props.onClick!()
            }}>
                <List.Item {...props}/>
            </div>
            <ItemMenu
                onDelete={props.onDelete}
                onEdit={props.onEdit}/>
        </div>
    )
}

const AccountItem: FC<ListDataItem & {
    onClick?: () => void
    onDelete?: () => void,
    onEdit?: () => void,
}> = (props) => {
    return (
        <div
            className={"dirveindex-profile-drive-account"}
            style={{
                display: "flex",
                alignItems: "center",
                flexDirection: "row",
            }}>
            <Avatar
                size={54}
                style={{
                    marginRight: 16,
                }}
                initials={asInitials(props.avatar)} />
            <ListItem
                title={props.title}
                description={props.description}
                action={props.action}/>
            <ItemMenu
                onDelete={props.onDelete}
                onEdit={props.onEdit}/>
        </div>
    )
}

const ItemMenu: FC<{
    onDelete?: () => void,
    onEdit?: () => void,
}> = (props) => {
    const { t } = useTranslation()
    const [ show, setShow ] = useState(false)
    return (
        <Popover placement={"left"} content={
            <div>
                {
                    props.onEdit !== undefined && (
                        <Button
                            type="default" appearance="link"
                            style={{ width: 80 }}
                            onClick={() => {
                                setShow(false)
                                props.onEdit!()
                            }}>{
                            t("profile_drive_creation_detail")
                        }</Button>
                    )
                }
                {
                    props.onDelete !== undefined && (
                        <Button
                            type="danger" appearance="link"
                            style={{ width: 80 }}
                            onClick={() => {
                                setShow(false)
                                props.onDelete!()
                            }}>{
                            t("delete")
                        }</Button>
                    )
                }
            </div>
        } visible={show}>
            <BarsOutlined
                size={22}
                style={{ padding: 6 }}
                onClick={() => setShow(true)}/>
        </Popover>
    )
}

export default Index