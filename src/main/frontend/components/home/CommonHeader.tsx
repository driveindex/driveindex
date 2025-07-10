import {Avatar, Button, Popover} from "@hi-ui/hiui";
import React, {FC} from "react";
import {NavigateFunction} from "react-router";
import {UserPref} from "Frontend/core/prefs/UserPref";
import {useTranslation} from "react-i18next";
import {useNavigate} from "react-router-dom";
import logo from "../../static/drawable/logo.svg";
import {MoveOutlined} from "@hi-ui/icons"
import RespLayoutProps from "Frontend/core/props/RespLayoutProps";
import {asInitials} from "Frontend/core/util/_String";

export interface CommonHeaderProps {
    isShowInProfile: boolean
    showAvatar?: boolean
    switchShowDrawer?: () => void
}

export const CommonHeader: FC<CommonHeaderProps & RespLayoutProps> = (props) => {
    const { t } = useTranslation()
    const navigate = useNavigate()

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
            <img style={{ width: 34, height: 34 }} src={logo} alt={"logo"} onClick={() => navigate("/")}/>
            <div style={{ marginLeft: 10, color: "#1f2733" }}><strong>{t("title")}</strong></div>
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

export const UserMenu: FC<CommonHeaderProps> = (props) => {
    const { t } = useTranslation()
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
                            t("home_go_to_profile")
                        }</Button>
                        <div style={{ height: 1, width: "100%", backgroundColor: "lightgray", marginTop: 10, marginBottom: 10 }} />
                    </>
                )
            }
            <Button
                type="danger" appearance="link"
                style={{ width: 80 }}
                onClick={() => doLogout(navigate)}>{
                t("home_logout")
            }</Button>
        </div>
    )
}

function doLogout(navigate: NavigateFunction) {
    UserPref.Role = ""
    UserPref.AccessToken = ""
    UserPref.Nick = ""
    UserPref.Login = false
    navigate("/login")
}

function goToProfile(navigate: NavigateFunction) {
    navigate("/profile")
}