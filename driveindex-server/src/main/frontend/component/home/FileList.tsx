import React, {useEffect} from "react";
import RespLayoutProps from "Frontend/core/props/RespLayoutProps";
import {translate, key} from "@vaadin/hilla-react-i18n";
import {EmptyState} from "@hi-ui/empty-state"
import {Loading} from "@hi-ui/loading"
import {
    Grid,
    GridColumn,
    GridSortColumn,
    GridSortColumnDirectionChangedEvent,
    HorizontalLayout,
    VerticalLayout
} from "@vaadin/react-components";
import FileListRespDto from "Frontend/generated/io/github/driveindex/dto/resp/FileListRespDto";
import type {GridSorterDirection} from "@vaadin/grid/src/vaadin-grid-sorter";
import GetDirReqSort from "Frontend/generated/io/github/driveindex/dto/req/user/GetDirReqSort";

export interface FileListProps  {
    fileList?: FileListRespDto
    pageIndex: number
    pageSize: number
    onCurrentPageChanged: (pageIndex: number) => void
    onPageSizeChanged: (pageSize: number) => void

    loading?: boolean

    sortBy?: GetDirReqSort
    asc?: boolean
    onChangeSortBy?: (sortBy?: GetDirReqSort, asc?: boolean) => void
}

export const FileList = (props: FileListProps & RespLayoutProps) => {
    const useDirection = (current: GetDirReqSort): [GridSorterDirection, (event: GridSortColumnDirectionChangedEvent) => void] => {
        let currentDirection: GridSorterDirection = null
        if (current === props.sortBy) {
            currentDirection = (props.asc ?? true) ? 'asc' : 'desc'
        }
        return [currentDirection, (event: GridSortColumnDirectionChangedEvent) => {
            const sort = event.detail.value
            if (sort === null || sort === undefined) {
                props.onChangeSortBy?.()
            } else {
                props.onChangeSortBy?.(current, sort !== 'desc')
            }
        }]
    }
    const [nameSort, setNameSort] = useDirection(GetDirReqSort.NAME)
    const [modTimeSort, setModTimeSort] = useDirection(GetDirReqSort.MODIFIED_TIME)

    return (
        <VerticalLayout style={{
            borderRadius: props.isMdUp ? 10 : 0,
            overflow: "hidden",
            backgroundColor: "white",
            marginBottom: 20,
            width: "100%",
        }}>
            <Grid items={props.fileList?.content} theme={"no-row-borders"} style={{border: "none"}}>
                <GridSortColumn path={"name"} header={translate(key`home.file.listHead.name`)}
                                autoWidth={true}
                                direction={nameSort} onDirectionChanged={setNameSort} />
                <GridSortColumn path={"modifyAt"} header={translate(key`home.file.listHead.modify`)}
                                direction={modTimeSort} onDirectionChanged={setModTimeSort} />
                <GridColumn path={"size"} header={translate(key`home.file.listHead.size`)} />
                <div slot={"empty-state"}>
                    <EmptyState style={{paddingTop: 30, paddingBottom: 30}} size={"lg"} />
                    {
                        (props.loading === true) && (
                            <Loading style={{height: 200}} content={translate(key`common.loading`)} />
                        )
                    }
                </div>
            </Grid>
            {
                (props.fileList?.content !== undefined) && <GridPaginationControls
                    totalItemCount={props.fileList.contentSize}
                    currentPage={props.pageIndex + 1}
                    pageSize={Math.ceil(props.fileList.contentSize / props.pageSize)}
                    onCurrentPageChanged={props.onCurrentPageChanged}
                    onPageSizeChanged={props.onPageSizeChanged}
                />
            }
        </VerticalLayout>
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
    return (
        <HorizontalLayout>

        </HorizontalLayout>
    )
}
