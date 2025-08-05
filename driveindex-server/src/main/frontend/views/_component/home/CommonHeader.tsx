import {Avatar, Button, Popover} from "@hi-ui/hiui";
import React from "react";
import {NavigateFunction} from "react-router";
import {UserPref} from "Frontend/core/prefs/UserPref";
import {useNavigate} from "react-router-dom";
import {MoveOutlined} from "@hi-ui/icons"
import RespLayoutProps from "Frontend/core/props/RespLayoutProps";
import {asInitials} from "Frontend/core/util/_String";
import Logo from "Frontend/views/_component/Logo";
import {key, translate} from "@vaadin/hilla-react-i18n";

export interface CommonHeaderProps {
    isShowInProfile: boolean
    showAvatar?: boolean
    switchShowDrawer?: () => void
}

export const CommonHeader = (props: CommonHeaderProps & RespLayoutProps) => {
    return (
        <div style={{
            display: "flex",
            alignItems: "center",
            padding: "15px 24px",
        }}>
            {
                (props.isShowInProfile && props.showAsMobile) && (
                    <Button
                        style={{
                            marginRight: 20,
                        }}
                        appearance={"link"}
                        onClick={props.switchShowDrawer}>
                        <MoveOutlined size={20} />
                    </Button>
                )
            }
            <Logo />
            <div style={{ display: "flex", justifyContent: "flex-end", width: "100%" }}>
                {
                    (props.showAvatar === undefined || props.showAvatar) && (
                        <Popover placement={"bottom-end"} content={<UserMenu {...props} />}>
                            <Avatar
                                initials={asInitials(UserPref.Username)}
                                name={UserPref.Username}/>
                        </Popover>
                    )
                }
            </div>
        </div>
    )
}

export const UserMenu = (props: CommonHeaderProps) => {
    const navigate = useNavigate()

    return (
        <div>
            {
                props.isShowInProfile || (
                    <>
                        <Button
                            type="default" appearance="link"
                            style={{ width: 80 }}
                            onClick={() => goToProfile(navigate)}>{
                            translate(key`home.profile`)
                        }</Button>
                        <div style={{ height: 1, width: "100%", backgroundColor: "lightgray", marginTop: 10, marginBottom: 10 }} />
                    </>
                )
            }
            <Button
                type="danger" appearance="link"
                style={{ width: 80 }}
                onClick={() => doLogout(navigate)}>{
                translate(key`home.logout`)
            }</Button>
        </div>
    )
}

function doLogout(navigate: NavigateFunction) {
    UserPref.Role = ""
    UserPref.Nickname = ""
    UserPref.Login = false
    navigate("/login")
}

function goToProfile(navigate: NavigateFunction) {
    navigate("/profile")
}