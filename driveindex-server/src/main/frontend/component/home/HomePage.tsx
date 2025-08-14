import {FileListProps, FileList} from "Frontend/component/home/FileList";
import {Breadcrumb} from "@hi-ui/breadcrumb";
import {BreadcrumbItem} from "Frontend/core/hooks/useBreadcrumb";
import {CommonHeader} from "Frontend/component/home/CommonHeader";
import {HorizontalLayout, VerticalLayout} from "@vaadin/react-components";
import {useBreakpointUp, useBreakpointDown } from "Frontend/core/hooks/useViewport";
import React from "react";

interface HomePageProps {
    breadcrumbData?: BreadcrumbItem[]
    onBreadcrumbClick?: (index: number) => void

    children?: React.ReactNode[]
}

const HomePage = (props: HomePageProps & FileListProps) => {
    const isMdUp = useBreakpointUp("md")
    const showAsMobile = useBreakpointUp("sm")
    const contentWidth = isMdUp ? 740 : "100%"

    const fileListProps: Pick<FileListProps, keyof FileListProps> = props

    const breadcrumb = (
        <Breadcrumb
            data={props.breadcrumbData}
            onClick={(e, i, index: number) => {
                props.onBreadcrumbClick?.(index)
            }}
            style={{
                margin: "0 20px",
            }} />
    )

    return (
        <>
            <CommonHeader isShowInProfile={false} showAvatar={props.children !== undefined} />
            <VerticalLayout
                style={{
                    display: "flex",
                    alignItems: "center",
                    flexDirection: "column",
                }}>
                <VerticalLayout style={{width: contentWidth}}>
                    {
                        props.children && (
                            <HorizontalLayout
                                style={{
                                    padding: "0 " + (isMdUp ? 0 : 20) + "px",
                                    width: "100%",
                                }}
                                theme={"spacing"}>
                                {props.children}
                            </HorizontalLayout>
                        )
                    }
                    <div
                        style={{
                            borderRadius: isMdUp ? 10 : 0,
                            backgroundColor: "white",
                            overflow: "hidden",
                            paddingTop: 6,
                            paddingBottom: 6,
                            marginTop: 20,
                            width: "100%",
                        }}>
                        {breadcrumb}
                    </div>
                    {
                        isMdUp && (
                            <div style={{height: 12}} />
                        )
                    }
                    <FileList isMdUp={isMdUp} showAsMobile={showAsMobile} {...fileListProps}/>
                </VerticalLayout>
            </VerticalLayout>
        </>
    )
}

export default HomePage
