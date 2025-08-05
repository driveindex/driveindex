import React, {useEffect, useState} from "react";
import {
    Avatar,
    Breadcrumb,
    Button,
    Card,
    Popover,
    Scrollbar
} from "@hi-ui/hiui";
import {Row} from "@hi-ui/grid";
import List, {ListDataItem} from "@hi-ui/list";
import {LoadingCover, useLoading} from "Frontend/core/hooks/useLoading";
import {DriveIndexAPI, DriveType} from "Frontend/core/axios";
import {AxiosResponse} from "axios";
import {useLocation, useNavigate, useParams} from "react-router-dom";
import "./@index.css"
import {BarsOutlined, PlusOutlined} from "@hi-ui/icons"
import Ic_OneDrive from "../../../../resources/META-INF/resources/drawable/drive/onedrive.svg"
import Ic_Unknown from "../../../../resources/META-INF/resources/drawable/drive/unknown.svg"
import RespLayoutProps from "Frontend/core/props/RespLayoutProps";
import {useBreakpointDown, useBreakpointUp} from "Frontend/core/hooks/useViewport";
import {asInitials} from "Frontend/core/util/_String";
import ClientCreationDialog from "Frontend/views/_component/profile/drive/ClientCreationDialog";
import {BreadcrumbDataItem} from "@hi-ui/breadcrumb";
import message from "@hi-ui/message";
import {key, translate} from "@vaadin/hilla-react-i18n";


type ClientBreadcrumbDataItem = BreadcrumbDataItem & {
    targetClient?: string
}

const ProfileDrive = () => {
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
            <h2>{translate(key`profile.drive.title`)}</h2>
            <p>{translate(key`profile.drive.desc`)}</p>
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
const DriveList = (props: DriveListProps & RespLayoutProps) => {
    const [ clientCreating, showClientCreating ] = useState<boolean>(false)
    const [ clientTarget, setClientTarget ] = useState<any | undefined>()
    const [ clientList, setClientList ] = useState<any[]>([])

    const [ accountCreating, showAccountCreating ] = useState(false)
    const [ accountList, setAccountList ] = useState<any[]>([])

    const { clientId } = useParams()

    const navigate = useNavigate()
    const { setLoading } = useLoading()

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
                if (resp.data["code"] !== 200) {
                    message.open({
                        title: translate(key`profile.drive.add.client.error.create`) + resp.data["message"],
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
                if (resp.data["code"] !== 200) {
                    message.open({
                        title: translate(key`profile.drive.add.account.error`) + resp.data["message"],
                        type: "error",
                    })
                    return
                }
                window.open(resp.data["data"]["redirect_url"], "_self")
            })
        } else {
            showAccountCreating(false)
            message.open({
                title: translate(key`common.error.internalError`) + translate(key`profile.drive.add.account.internalUnknownType`),
                type: "error",
            })
        }
    }
    useEffect(() => {
        showAccountCreating(false)
        const base: ClientBreadcrumbDataItem[] = [
            {
                title: translate(key`profile.drive.breadcrumb`),
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
            if (resp.data["code"] !== 200) {
                message.open({
                    title: translate(key`profile.drive.add.client.error.create`) + resp.data["message"],
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
                                    }}>{translate(key`profile.drive.add.client`)}</Button> :
                                <Button
                                    type={"primary"}
                                    icon={<PlusOutlined />}
                                    loading={accountCreating}
                                    disabled={accountCreating}
                                    onClick={() => {
                                        creatingAccount()
                                    }}>{translate(key`profile.drive.add.account`)}</Button>
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
                                    description: translate(key`profile.drive.lastModify`, {
                                        "datetime": item["modify_at"]
                                    }),
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
                                    description: translate(key`profile.drive.lastModify`, {
                                        "datetime": item["modify_at"]
                                    }),
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
                        emptyContent={translate(key`common.error.notFound`)}
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

interface ItemProps {
    onClick?: () => void
    onDelete?: () => void
    onEdit?: () => void
}

const ClientItem = (props: ListDataItem & ItemProps) => {
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

const AccountItem = (props: ListDataItem & ItemProps) => {
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
            <List.Item
                title={props.title}
                description={props.description}
                action={props.action}/>
            <ItemMenu
                onDelete={props.onDelete}
                onEdit={props.onEdit}/>
        </div>
    )
}

const ItemMenu = (props: ItemProps) => {
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
                            translate(key`profile.drive.add.client.detail`)
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
                            translate(key`common.delete`)
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

export default ProfileDrive