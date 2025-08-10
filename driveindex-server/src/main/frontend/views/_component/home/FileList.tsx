import React, {useEffect} from "react";
import RespLayoutProps from "Frontend/core/props/RespLayoutProps";
import {translate, key} from "@vaadin/hilla-react-i18n";
import {EmptyState} from "@hi-ui/empty-state"
import {Loading} from "@hi-ui/loading"
import {Grid, GridColumn, GridSortColumn, HorizontalLayout, VerticalLayout} from "@vaadin/react-components";
import FileListRespDto from "Frontend/generated/io/github/driveindex/dto/resp/FileListRespDto";

export enum FileListSortBy {
    NAME, SIZE, CREATE_TIME, MODIFIED_TIME
}

export interface FileListProps  {
    data?: FileListRespDto
    pageIndex: number
    pageSize: number
    onCurrentPageChanged: (pageIndex: number) => void
    onPageSizeChanged: (pageSize: number) => void

    sortBy?: FileListSortBy
    asc?: boolean
    onChangeSortBy?: (sortBy: FileListSortBy) => void
    onChangeAsc?: (asc: boolean) => void
}

export const FileList = (props: FileListProps & RespLayoutProps) => {
    return (
        <div
            style={{
                borderRadius: props.isMdUp ? 10 : 0,
                backgroundVerticalLayoutor: "#FFFFFF",
                paddingTop: 16,
                paddingBottom: 16,
                marginBottom: 20,
            }}>
            <VerticalLayout>
                <Grid items={props.data?.content} theme={"no-row-borders no-borders"}>
                    <GridSortColumn path={"name"} header={translate(key`home.file.listHead.name`)} />
                    <GridSortColumn path={"modifyAt"} header={translate(key`home.file.listHead.modify`)} />
                    <GridColumn path={"size"} header={translate(key`home.file.listHead.size`)} />

                    <EmptyState slot={"empty-state"} style={{paddingTop: 30, paddingBottom: 30,}} size={"lg"} />
                </Grid>
                {
                    (props.data?.content === undefined) && <Loading
                        style={{
                            height: 200,
                        }}
                        content={translate(key`common.loading`)} />
                }
                {
                    (props.data?.content !== undefined) && <GridPaginationControls
                        totalItemCount={props.data.contentSize}
                        currentPage={props.pageIndex + 1}
                        pageSize={Math.ceil(props.data.contentSize / props.pageSize)}
                        onCurrentPageChanged={props.onCurrentPageChanged}
                        onPageSizeChanged={props.onPageSizeChanged}
                    />
                }
                {
                    props.isMdUp || (
                        <div style={{width: 40}} />
                    )
                }
            </VerticalLayout>
        </div>
    )
}



interface GridPaginationControlsProps {
    totalItemCount: number
    currentPage: number
    pageSize: number
    onCurrentPageChanged: (pageIndex: number) => void
    onPageSizeChanged: (pageSize: number) => void
}

const GridPaginationControls = (props: GridPaginationControlsProps) => {
    useEffect(() => {
        if (props.currentPage > props.pageSize) {
            props.onPageSizeChanged(props.pageSize)
        }
    }, [props.currentPage])

    return (
        <HorizontalLayout>

        </HorizontalLayout>
    )
}
