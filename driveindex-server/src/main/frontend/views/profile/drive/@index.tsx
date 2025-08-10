import React, {useEffect, useState} from "react";
import {LoadingCover, useLoading} from "Frontend/core/hooks/useLoading";
import {DriveIndexAPI, DriveType} from "Frontend/core/axios";
import {AxiosResponse} from "axios";
import {useLocation, useNavigate, useParams} from "react-router-dom";
import "./@index.css"
import RespLayoutProps from "Frontend/core/props/RespLayoutProps";
import {useBreakpointDown, useBreakpointUp} from "Frontend/core/hooks/useViewport";
import {Breadcrumb, BreadcrumbDataItem} from "@hi-ui/breadcrumb";
import {key, translate} from "@vaadin/hilla-react-i18n";
import {Notification} from "@vaadin/notification";
import {Button, Card, HorizontalLayout, Icon, Scroller, VerticalLayout} from "@vaadin/react-components";


type ClientBreadcrumbDataItem = BreadcrumbDataItem & {
    targetClient?: string
}

const ProfileDrive = () => {
    const { state } = useLocation()

    const isMdUp = useBreakpointUp("md")
    const showAsMobile = useBreakpointDown("sm")

    return (
        <VerticalLayout theme={"spacing padding"}>
            <h2>{translate(key`profile.drive.title`)}</h2>
            <p>{translate(key`profile.drive.desc`)}</p>
            <LoadingCover>
                <DriveList client={state} isMdUp={isMdUp} showAsMobile={showAsMobile} />
            </LoadingCover>
        </VerticalLayout>
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
                    Notification.show(translate(key`profile.drive.add.client.error.create`, {
                        message: resp.data["message"]
                    }), {
                        position: "top-end",
                        theme: "error",
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
                    Notification.show(translate(key`profile.drive.add.account.error.create`, {
                        message: resp.data["message"]
                    }), {
                        position: "top-end",
                        theme: "error",
                    })
                    return
                }
                window.open(resp.data["data"]["redirect_url"], "_self")
            })
        } else {
            showAccountCreating(false)
            Notification.show(translate(key`common.error.internalError`, {
                message: translate(key`profile.drive.add.account.internalUnknownType`)
            }), {
                position: "top-end",
                theme: "error",
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
                Notification.show(translate(key`profile.drive.add.client.error.create`, {
                    message: resp.data["message"]
                }), {
                    position: "top-end",
                    theme: "error",
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
            <Card>
                <HorizontalLayout slot={"title"} style={{padding: "0 6px"}}>
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
                                theme={"primary"}
                                onClick={() => {
                                    showClientCreating(true)
                                }}>
                                <Icon icon={"vaadin:plus"} />
                                {translate(key`profile.drive.add.client`)}
                            </Button> :
                            <Button
                                theme={"primary"}
                                disabled={accountCreating}
                                onClick={() => {
                                    creatingAccount()
                                }}>
                                <Icon icon={"vaadin:plus"} />
                                {translate(key`profile.drive.add.account`)}
                            </Button>
                    }
                </HorizontalLayout>
            </Card>
        </>
    )
}

interface ItemProps {
    onClick?: () => void
    onDelete?: () => void
    onEdit?: () => void
}

export default ProfileDrive