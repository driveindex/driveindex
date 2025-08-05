import React from "react";
import {Col, EmptyState, Loading, Row} from "@hi-ui/hiui";
import RespLayoutProps from "Frontend/core/props/RespLayoutProps";
import FileItem from "Frontend/generated/io/github/driveindex/dto/resp/FileListRespDto/FileItem";
import {translate, key} from "@vaadin/hilla-react-i18n";

export enum FileListSortBy {
    NAME, SIZE, CREATE_TIME, MODIFIED_TIME
}

export interface FileListProps  {
    list?: Array<FileItem>
}

export const FileList = (props: FileListProps & FileListHeaderProps & RespLayoutProps) => {
    const realList = <RealList {...props} />
    return (
        <div
            style={{
                borderRadius: props.isMdUp ? 10 : 0,
                backgroundColor: "#FFFFFF",
                paddingTop: 16,
                paddingBottom: 16,
                marginBottom: 20,
            }}>{
            realList
        }</div>
    )
}

export interface BreadcrumbContainerProps {
    children?: React.ReactNode
    marginTop?: number
}

export const BreadcrumbContainer = (props: BreadcrumbContainerProps & RespLayoutProps) => {
    return <div
        style={{
            borderRadius: props.isMdUp ? 10 : 0,
            backgroundColor: "#FFFFFF",
            paddingTop: 6,
            paddingBottom: 6,
            marginTop: props.marginTop ?? 0
        }}>{
        props.children
    }</div>
}

const RealList = (props: RespLayoutProps & FileListProps) => {
    let listContent: React.JSX.Element[] | React.JSX.Element | undefined
    if (props.list === undefined) {
        listContent = undefined
    } else if (props.list.length > 0) {
        listContent = props.list.map(() =>
            <FileListItem
                isMdUp={props.isMdUp} />
        )
    } else {
        listContent = <EmptyState
            style={{
                paddingTop: 30,
                paddingBottom: 30,
            }}
            size={"lg"} />
    }
    return (
        <div>
            <FileListHeader {...props} />
            {
                listContent !== undefined
                    ? listContent
                    : <Loading
                        style={{
                            height: 200,
                        }}
                        content={translate(key`common.loading`)} />
            }
        </div>
    )
}

interface FileListHeaderProps {
    sortBy?: FileListSortBy
    asc?: boolean
    onChangeSortBy?: (sortBy: FileListSortBy) => void
    onChangeAsc?: (asc: boolean) => void

    breadcrumb?: React.ReactNode
}

const FileListHeader = (props: FileListHeaderProps & RespLayoutProps) => {
    return (
        <>
            {
                props.showAsMobile && (
                    props.breadcrumb
                )
            }
            {
                props.showAsMobile || (
                    <div
                        style={{
                            display: "flex",
                            alignItems: "center",
                        }}>
                        {
                            props.isMdUp && (
                                <div style={{width: 40}} />
                            )
                        }
                        <Row
                            style={{
                                width: "100%",
                                paddingLeft: 20,
                                paddingRight: 20,
                            }}
                            columns={props.isMdUp ? 7 : 4}>
                            <Col span={4}>
                                <div>{translate(key`home.file.listHead.name`)}</div>
                            </Col>
                            <Col span={2} style={{ display: props.isMdUp ? "block" : "none" }}>
                                <div>{translate(key`home.file.listHead.modify`)}</div>
                            </Col>
                            <Col span={1} style={{ display: props.isMdUp ? "block" : "none" }}>
                                <div>{translate(key`home.file.listHead.size`)}</div>
                            </Col>
                        </Row>
                        {
                            props.isMdUp || (
                                <div style={{width: 40}} />
                            )
                        }
                    </div>
                )
            }
        </>
    )
}

interface FileListItemProps {

}

export const FileListItem = (props: FileListItemProps & RespLayoutProps) => {
    return (
        <div
            style={{
                display: "flex",
                alignItems: "center",
            }}>
            {
                props.isMdUp && (
                    <div style={{width: 40}} />
                )
            }
            <Row
                style={{
                    width: "100%",
                    paddingLeft: 20,
                    paddingRight: 20,
                }}
                columns={props.showAsMobile ? 4 : props.isMdUp ? 7 : 6}>
                <Col span={4}>
                    <div>{translate(key`home.file.listHead.name`)}</div>
                </Col>
                <Col span={2} style={{ display: props.showAsMobile ? "none" : "block" }}>
                    <div>{translate(key`home.file.listHead.modify`)}</div>
                </Col>
                <Col span={1} style={{ display: props.isMdUp ? "block" : "none" }}>
                    <div>{translate(key`home.file.listHead.size`)}</div>
                </Col>
            </Row>
            {
                props.isMdUp || (
                    <div style={{width: 40}} />
                )
            }
        </div>
    )
}
