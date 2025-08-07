package io.github.driveindex.drives.onedrive.core

/**
 * @see <a href="https://learn.microsoft.com/zh-cn/graph/delta-query-overview#national-clouds">使用增量查询跟踪 Microsoft Graph 数据更改 - 国家云</a>
 */
enum class OneDriveEndpoint(
    val loginHosts: String,
    val portal: String,
    val graph: String,
    val supportDelta: Boolean = false,
) {
    Global(
        loginHosts = "https://login.microsoftonline.com",
        portal = "https://portal.azure.com",
        graph = "https://graph.microsoft.com",
        supportDelta = true,
    ),
    US_L4(
        loginHosts = "https://login.microsoftonline.us",
        portal = "https://portal.azure.us",
        graph = "https://graph.microsoft.us",
    ),
    US_L5(
        loginHosts = "https://login.microsoftonline.us",
        portal = "https://portal.azure.us",
        graph = "https://dod-graph.microsoft.us",
    ),
    CN(
        loginHosts = "https://login.chinacloudapi.cn",
        portal = "https://portal.azure.cn",
        graph = "https://microsoftgraph.chinacloudapi.cn",
        supportDelta = true,
    )
}
