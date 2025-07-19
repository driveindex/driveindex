import React, {FC} from "react";
import {useTranslation} from "react-i18next";
import {Col, EmptyState, Loading, Row} from "@hi-ui/hiui";
import RespLayoutProps from "Frontend/core/props/RespLayoutProps";
import FileItem from "Frontend/generated/io/github/driveindex/dto/resp/FileListRespDto/FileItem";

export enum FileListSortBy {
    NAME, SIZE, CREATE_TIME, MODIFIED_TIME
}

export interface FileListProps  {
    list?: Array<FileItem>
}

export const FileList: FC<FileListProps & FileListHeaderProps & RespLayoutProps> = (props) => {
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

export const BreadcrumbContainer: FC<BreadcrumbContainerProps & RespLayoutProps> = (props) => {
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

const RealList: FC<RespLayoutProps & FileListProps> = (props) => {
    const { t } = useTranslation()
    let listContent: JSX.Element[] | JSX.Element | undefined
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
                        content={t("loading")} />
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

const FileListHeader: FC<FileListHeaderProps & RespLayoutProps> = (props) => {
    const { t } = useTranslation()
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
                                <div>{t("home_file_list_head_name")}</div>
                            </Col>
                            <Col span={2} style={{ display: props.isMdUp ? "block" : "none" }}>
                                <div>{t("home_file_list_head_modify")}</div>
                            </Col>
                            <Col span={1} style={{ display: props.isMdUp ? "block" : "none" }}>
                                <div>{t("home_file_list_head_size")}</div>
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

export const FileListItem: FC<FileListItemProps & RespLayoutProps> = (props) => {
    const { t } = useTranslation()
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
                    <div>{t("home_file_list_head_name")}</div>
                </Col>
                <Col span={2} style={{ display: props.showAsMobile ? "none" : "block" }}>
                    <div>{t("home_file_list_head_modify")}</div>
                </Col>
                <Col span={1} style={{ display: props.isMdUp ? "block" : "none" }}>
                    <div>{t("home_file_list_head_size")}</div>
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
