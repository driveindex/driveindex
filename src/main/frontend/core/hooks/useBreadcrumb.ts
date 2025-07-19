import {TFunction} from "i18next";
import {BreadcrumbDataItem} from "@hi-ui/breadcrumb";

const useBreadcrumb = (t: TFunction<"translation", undefined>, pathStr: string | null) => {
    const data: (BreadcrumbDataItem & { path: string })[] = [{
        title: t("home_file_root"),
        path: "/",
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
            path: currentPath,
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
