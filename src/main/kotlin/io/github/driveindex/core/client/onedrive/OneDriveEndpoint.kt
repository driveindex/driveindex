package io.github.driveindex.core.client.onedrive

import feign.Feign
import feign.slf4j.Slf4jLogger
import io.github.driveindex.Application.Companion.Bean
import io.github.driveindex.core.configuration.FeignClientConfig
import kotlin.getValue

/**
 * @see <a href="https://learn.microsoft.com/zh-cn/graph/delta-query-overview#national-clouds">使用增量查询跟踪 Microsoft Graph 数据更改 - 国家云</a>
 */
enum class OneDriveEndpoint(
    private val loginHosts: String,
    portal: String,
    graph: String,
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
    );

    val Auth: AzureAuthClient by lazyOneDriveFeignClientOf(loginHosts)

    val Portal: AzureAuthClient by lazyOneDriveFeignClientOf(portal)

    val Graph: AzureGraphClient by lazyOneDriveFeignClientOf(graph)

    fun loginUrl(path: String, vararg args: Pair<String, Any>): String {
        return "${loginHosts}${path}${if (args.isEmpty()) "" else args.joinToString("&", "?") { "${it.second}=${it.second}" }} "
    }
}

inline fun <reified T> lazyOneDriveFeignClientOf(
    url: String,
    clazz: Class<T> = T::class.java,
    crossinline block: Feign.Builder.() -> Unit = { }
): Lazy<T> {
    return lazy {
        val errorDecoder = AzureErrorDecoder::class.Bean
        FeignClientConfig.newFeignBuilder()
            .errorDecoder(errorDecoder)
            .logger(Slf4jLogger(clazz))
            .apply(block)
            .target(clazz, url)
    }
}