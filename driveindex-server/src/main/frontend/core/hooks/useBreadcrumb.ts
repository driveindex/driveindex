import {BreadcrumbDataItem} from "@hi-ui/breadcrumb";
import {translate, key} from "@vaadin/hilla-react-i18n";

export type BreadcrumbItem = BreadcrumbDataItem & { path: string }

const useBreadcrumb = (pathStr?: string) => {
    const data: BreadcrumbItem[] = [{
        title: translate(key`home.file.root`),
        path: "/",
    }]
    if (pathStr == undefined) {
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
