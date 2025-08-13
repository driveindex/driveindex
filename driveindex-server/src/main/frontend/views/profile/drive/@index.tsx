import React, {useEffect, useState} from "react";
import {LoadingCover, useLoading} from "Frontend/core/hooks/useLoading";
import "./@index.css"
import RespLayoutProps from "Frontend/core/props/RespLayoutProps";
import {useBreakpointDown, useBreakpointUp} from "Frontend/core/hooks/useViewport";
import {key, translate} from "@vaadin/hilla-react-i18n";
import {Card, HorizontalLayout, VerticalLayout} from "@vaadin/react-components";
import {useUrlArgs} from "Frontend/core/utils/_Router";


const ProfileDrive = () => {
    const isMdUp = useBreakpointUp("md")
    const showAsMobile = useBreakpointDown("sm")

    return (
        <VerticalLayout theme={"spacing padding"}>

        </VerticalLayout>
    )
}

const DriveList = (props: RespLayoutProps) => {
    const urlArgs = useUrlArgs()
    return (
        <>
            <Card>
                <HorizontalLayout slot={"title"} style={{padding: "0 6px"}}>

                </HorizontalLayout>
            </Card>
        </>
    )
}

export default ProfileDrive