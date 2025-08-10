import React, {HTMLAttributes, useEffect, useState} from "react";
import {CommonHeader} from "Frontend/views/_component/home/CommonHeader";
import {useBreakpointDown} from "Frontend/core/hooks/useViewport";
import {Outlet, useLocation, useNavigate} from "react-router-dom"
import "./@layout.css"
import {translate, key} from "@vaadin/hilla-react-i18n";
import {AppLayout, DrawerToggle, SideNav, SideNavElement, SideNavItem} from "@vaadin/react-components";

const ProfilePage = () => {
    const showAsMobile = useBreakpointDown("sm")

    const [ drawer, openDrawer ] = useState(false)

    const location = useLocation();
    const navigate = useNavigate();
    useEffect(() => {
        if (location.pathname === "/profile") {
            navigate("/profile/common");
        }
    }, [location]);

    return (
        <AppLayout>
            <CommonHeader
                slot={"navbar"}
                isShowInProfile={true}
                showAsMobile={showAsMobile}
                switchShowDrawer={() => {
                    openDrawer(!drawer)
                }}/>
            <ProfileDrawer slot="drawer" />
            <Outlet />
        </AppLayout>
    )
}

const ProfileDrawer = (props: HTMLAttributes<SideNavElement>) => {
    return (
        <SideNav {...props}>
            <SideNavItem path={"/profile/common"}>
                {translate(key`profile.common`)}
            </SideNavItem>
            <SideNavItem path={"/profile/drive"} matchNested={true}>
                {translate(key`profile.drive`)}
            </SideNavItem>
            <SideNavItem path={"/profile/password"} matchNested={true}>
                {translate(key`profile.password`)}
            </SideNavItem>
        </SideNav>
    )
}

export default ProfilePage