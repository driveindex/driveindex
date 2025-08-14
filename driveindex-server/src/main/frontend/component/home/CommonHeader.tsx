import React, {HTMLAttributes} from "react";
import {NavigateFunction} from "react-router";
import {UserPref} from "Frontend/core/prefs/UserPref";
import {useNavigate} from "react-router-dom";
import RespLayoutProps from "Frontend/core/props/RespLayoutProps";
import Logo from "Frontend/component/Logo";
import {key, translate} from "@vaadin/hilla-react-i18n";
import {
    Avatar,
    Button,
    DrawerToggle,
    HorizontalLayout,
    HorizontalLayoutElement,
    Icon,
    Popover
} from "@vaadin/react-components";
import ThemeSwitcher from "Frontend/component/home/ThemeSwitcher";

export interface CommonHeaderProps {
    isShowInProfile: boolean
    showAvatar?: boolean
    switchShowDrawer?: () => void
}

export const CommonHeader = (props: CommonHeaderProps & RespLayoutProps & HTMLAttributes<HorizontalLayoutElement>) => {
    return (
        <HorizontalLayout theme={"spacing padding"} style={{width: "100%"}} {...props}>
            {
                (props.isShowInProfile && props.showAsMobile) && (
                    <DrawerToggle />
                )
            }
            <Logo />
            {
                (props.showAvatar === undefined || props.showAvatar) && (
                    <Avatar slot={"end"} id={"home-avatar"} />
                )
            }
            <ThemeSwitcher />
            <Popover for={"home-avatar"} position={"bottom-end"}>
                <UserMenu {...props} />
            </Popover>
        </HorizontalLayout>
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
                            theme={"tertiary"}
                            style={{ width: 80 }}
                            onClick={() => goToProfile(navigate)}>{
                            translate(key`home.profile`)
                        }</Button>
                        <div style={{ height: 1, width: "100%", backgroundColor: "lightgray", marginTop: 10, marginBottom: 10 }} />
                    </>
                )
            }
            <Button
                theme={"tertiary error"}
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