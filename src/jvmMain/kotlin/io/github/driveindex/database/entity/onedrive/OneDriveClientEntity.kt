package io.github.driveindex.database.entity.onedrive

import io.github.driveindex.configuration.lazyFeignClientOf
import io.github.driveindex.feigh.onedrive.AzureAuthClient
import io.github.driveindex.feigh.onedrive.AzureGraphClient
import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.util.*

@Entity
@Table(name = "client_onedrive")
data class OneDriveClientEntity(
    @Id
    @Column(name = "client_id")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    val id: UUID,

    @Column(name = "azure_client_id")
    val clientId: String,

    @Column(name = "azure_client_secret")
    var clientSecret: String,

    @Column(name = "azure_client_tenant")
    val tenantId: String = "common",

    @Column(name = "azure_client_endpoint")
    @Enumerated(EnumType.STRING)
    val endPoint: EndPoint = EndPoint.Global,
) {
    enum class EndPoint(
        val LoginHosts: String,
        portal: String,
        graph: String,
    ) {
        Global(
            "https://login.microsoftonline.com",
            "https://portal.azure.com",
            "https://graph.microsoft.com",
        ),
        US_L4(
            "https://login.microsoftonline.us",
            "https://portal.azure.us",
            "https://graph.microsoft.us",
        ),
        US_L5(
            "https://login.microsoftonline.us",
            "https://portal.azure.us",
            "https://dod-graph.microsoft.us",
        ),
        CN(
            "https://login.chinacloudapi.cn",
            "https://portal.azure.cn",
            "https://microsoftgraph.chinacloudapi.cn",
        );

        val Auth: AzureAuthClient by lazyFeignClientOf(LoginHosts)

        val Portal: AzureAuthClient by lazyFeignClientOf(portal)

        val Graph: AzureGraphClient by lazyFeignClientOf(graph)

        /**
         * @see <a href="https://learn.microsoft.com/zh-cn/graph/delta-query-overview#national-clouds">使用增量查询跟踪 Microsoft Graph 数据更改 - 国家云</a>
         */
        val supportDelta: Boolean get() = this == CN || this == Global
    }
}