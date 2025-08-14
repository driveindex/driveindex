import React, {useEffect, useMemo, useState} from "react";
import useBreadcrumb from "Frontend/core/hooks/useBreadcrumb";
import {ViewConfig} from "@vaadin/hilla-file-router/types.js";
import "./@index.css"
import {FileController} from "Frontend/generated/endpoints";
import GetDirReqSort from "Frontend/generated/io/github/driveindex/dto/req/user/GetDirReqSort";
import FileListRespDto from "Frontend/generated/io/github/driveindex/dto/resp/FileListRespDto";
import {useUrlArgs} from "Frontend/core/utils/_Router";
import {key, translate} from "@vaadin/hilla-react-i18n";
import {Button, Icon} from "@vaadin/react-components";
import HomePage from "Frontend/component/home/HomePage";

export const config: ViewConfig = {
    loginRequired: true,
};

interface MainViewQueryParams {
    path: string;
    pageIndex: number;
    pageSize: number;
}

interface SortParams {
    sortBy: GetDirReqSort
    asc: boolean
}

const MainView = () => {
    const urlArgs = useUrlArgs<MainViewQueryParams>()
    const pageIndex = urlArgs.query.pageIndex
    const pageSize = urlArgs.query.pageSize
    const [sort, setSort] = useState<SortParams>()

    const [fileList, setFileList] = useState<FileListRespDto>()
    const [loading, setLoading] = useState<boolean>()
    const updateFileList = (path: string) => {
        setLoading(true)
        FileController.listFile({
            path: path,
            sortBy: sort?.sortBy ?? GetDirReqSort.NAME,
            asc: sort?.asc ?? true,
            pageIndex: pageIndex ?? 0,
            pageSize: pageSize ?? 15,
        }).then((result) => {
            if (result.code === 200) {
                setFileList(result.data)
            }
        }).finally(() => {
            setLoading(false)
        })
    }
    useEffect(() => {
        if (urlArgs.query.path === undefined) {
            urlArgs.setQuery({path: "/"}, {replace: true})
        } else {
            updateFileList(urlArgs.query.path)
        }
    }, [urlArgs.query.path, sort, pageIndex, pageSize])

    const breadcrumbData = useBreadcrumb(urlArgs.query.path)

    const [ createDirShow, showCreateDir ] = useState(false)
    const [ createLinkShow, showCreateLink ] = useState(false)
    const [ createDirLoading, setCreateLoading ] = useState(false)

    return (
        <>
            <HomePage
                breadcrumbData={breadcrumbData}
                onBreadcrumbClick={(index) => {
                    const targetPath = breadcrumbData[index].path
                    if (targetPath === urlArgs.query.path) {
                        return
                    }
                    urlArgs.setQuery({
                        path: targetPath
                    })
                }}
                loading={loading}
                fileList={fileList}
                pageIndex={pageIndex ?? 0} pageSize={pageSize ?? 20}
                onCurrentPageChanged={(pageIndex) => urlArgs.setQuery({pageIndex: pageIndex})}
                onPageSizeChanged={(pageSize) => urlArgs.setQuery({pageSize: pageSize})}
                {...sort}
                onChangeSortBy={(sortBy, asc) => {
                    if (sortBy === undefined || asc == undefined) {
                        setSort(undefined)
                    } else {
                        setSort({sortBy: sortBy, asc: asc})
                    }
                }}>
                <Button
                    theme={"primary large"}
                    onClick={() => showCreateDir(true)}>
                    <Icon icon={"vaadin:plus"} />
                    {translate(key`home.file.create.dir`)}
                </Button>
                <Button
                    theme={"secondary large"}
                    onClick={() => showCreateLink(true)}>
                    <Icon icon={"vaadin:link"} />
                    {translate(key`home.file.create.mount`)}
                </Button>
            </HomePage>
        </>
    )
}

export default MainView
