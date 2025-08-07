package io.github.driveindex.drives.onedrive.endpoints

import feign.slf4j.Slf4jLogger
import io.github.driveindex.drives.onedrive.core.AzureErrorDecoder
import io.github.driveindex.drives.onedrive.core.OneDriveEndpoint
import io.github.driveindex.drives.onedrive.feign.AzureAuthClient
import io.github.driveindex.drives.onedrive.feign.AzureGraphClient
import io.github.driveindex.utils.RemoteHttpClientFactory
import java.util.StringJoiner

abstract class BaseEndpoint(
    val endpoint: OneDriveEndpoint,
    private val errorDecoder: AzureErrorDecoder,
    private val feignFactory: RemoteHttpClientFactory,
) {
    val authApi: AzureAuthClient by lazyClient(endpoint.loginHosts)
    val graphApi: AzureGraphClient by lazyClient(endpoint.graph)

    private inline fun <reified T> lazyClient(url: String) = lazy {
        feignFactory.feignBuilder()
            .errorDecoder(errorDecoder)
            .logger(Slf4jLogger(T::class.java))
            .target(T::class.java, url)
    }


    fun loginUrl(path: String, vararg args: Pair<String, Any>): String {
        val url = StringBuilder()
        url.append(endpoint.loginHosts)
        url.append(path)
        if (args.isNotEmpty()) {
            url.append('?')
            val argsStr = StringJoiner("&")
            for ((key, value) in args) {
                argsStr.add("$key=$value")
            }
            url.append(argsStr)
        }
        return url.toString()
    }
}