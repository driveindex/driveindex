package io.github.driveindex.client.onedrive

import io.github.driveindex.configuration.lazyFeignClientOf
import kotlin.getValue

/**
 * @see <a href="https://learn.microsoft.com/zh-cn/graph/delta-query-overview#national-clouds">使用增量查询跟踪 Microsoft Graph 数据更改 - 国家云</a>
 */
enum class OneDriveEndpoint(
    val LoginHosts: String,
    portal: String,
    graph: String,
    val supportDelta: Boolean = false,
) {
    Global(
        LoginHosts = "https://login.microsoftonline.com",
        portal = "https://portal.azure.com",
        graph = "https://graph.microsoft.com",
        supportDelta = true,
    ),
    US_L4(
        LoginHosts = "https://login.microsoftonline.us",
        portal = "https://portal.azure.us",
        graph = "https://graph.microsoft.us",
    ),
    US_L5(
        LoginHosts = "https://login.microsoftonline.us",
        portal = "https://portal.azure.us",
        graph = "https://dod-graph.microsoft.us",
    ),
    CN(
        LoginHosts = "https://login.chinacloudapi.cn",
        portal = "https://portal.azure.cn",
        graph = "https://microsoftgraph.chinacloudapi.cn",
        supportDelta = true,
    );

    val Auth: AzureAuthClient by lazyFeignClientOf(LoginHosts)

    val Portal: AzureAuthClient by lazyFeignClientOf(portal)

    val Graph: AzureGraphClient by lazyFeignClientOf(graph)
}