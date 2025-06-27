import {TFunction} from "i18next";
import {BreadcrumbDataItem} from "@hi-ui/breadcrumb/lib/types/types";

const useBreadcrumb = (t: TFunction<"translation", undefined>, pathStr: string | null) => {
    const data: (BreadcrumbDataItem & { link: string })[] = [{
        title: t("home_file_root"),
        link: "/home?path=%2F",
    }]
    if (pathStr == null) {
        return data
    }
    const names = getAbsolutePath(pathStr)
    let currentPath = "/"
    for (const name of names) {
        if (currentPath !== "/") {
            currentPath += "/"
        }
        currentPath += name
        data.push({
            title: name,
            link: "/home?path=" + encodeURIComponent(currentPath),
        })
    }
    return data
}

export default useBreadcrumb

function getAbsolutePath(path: string): string[] {
    const names: string[] = []
    for (const item of path.split("/")) {
        if (item === "..") {
            names.pop()
        } else if (item === ".") {
        } else if (!/^\s*$/.test(item)) {
            names.push(item)
        }
    }
    return names
}
