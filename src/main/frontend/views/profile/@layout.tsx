import React, {FC, useState} from "react";
import {CommonHeader} from "Frontend/views/_component/home/CommonHeader";
import {useBreakpointDown} from "Frontend/core/hooks/useViewport";
import {Outlet, useLocation, useNavigate} from "react-router-dom"
import Drawer from 'react-modern-drawer'
import 'react-modern-drawer/dist/index.css'
import {Menu, MenuDataItem} from "@hi-ui/hiui";
import {useTranslation} from "react-i18next";
import "./@layout.css"

const ProfilePage: FC = () => {
    const showAsMobile = useBreakpointDown("sm")

    const [ drawer, openDrawer ] = useState(false)

    return (
        <div
            style={{
                backgroundColor: "white",
                height: "100%",
            }}
            className={"dirveindex-profile"}>
            <div
                style={{
                    boxShadow: "0 4px 8px rgba(0,0,0,.05), inset 0 -1px 0 #ebedf0"
                }}>
                <CommonHeader
                    isShowInProfile={true}
                    showAsMobile={showAsMobile}
                    switchShowDrawer={() => {
                        openDrawer(!drawer)
                    }}/>
            </div>
            <div
                style={{
                    display: "flex",
                    alignItems: "center",
                    width: "100%",
                    height: "calc(100vh - 70px)"
                }}>
                {
                    showAsMobile ? (
                        <Drawer
                            open={drawer}
                            direction={"left"}
                            size={300}
                            duration={300}
                            onClose={() => openDrawer(false)}>
                            <ProfileDrawer onItemClicked={() => openDrawer(false)} />
                        </Drawer>
                    ) : (
                        <>
                            <div style={{
                                width: 300,
                                height: "100%",
                            }}>
                                <ProfileDrawer onItemClicked={() => openDrawer(false)} />
                            </div>
                        </>
                    )
                }
                <div style={{
                    padding: 20,
                    width: "100%",
                    height: "calc(100vh - 110px)"
                }}>
                    <Outlet />
                </div>
            </div>
        </div>
    )
}

const ProfileDrawer: FC<{
    onItemClicked?: () => void
}> = (props) => {
    const { t } = useTranslation()
    const navigate = useNavigate()
    const data: MenuDataItem[] = [
        {
            title: t("profile_common"),
            id: "/profile/common",
        },
        {
            title: t("profile_account"),
            id: "/profile/account",
        },
        {
            title: t("profile_drive"),
            id: "/profile/drive",
        },
        {
            title: t("profile_password"),
            id: "/profile/password",
        },
    ]
    return (
        <Menu
            data={data}
            placement={"vertical"}
            style={{
                width: "100%",
                height: "100%",
                paddingTop: 20,
                backgroundColor: "transparent",
            }}
            activeId={useLocation().pathname}
            onClick={(id, item) => {
                navigate(id as string)
                props.onItemClicked && props.onItemClicked()
            }}/>
    )
}

export default ProfilePage