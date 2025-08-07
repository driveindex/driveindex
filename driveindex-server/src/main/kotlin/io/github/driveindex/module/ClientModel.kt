package io.github.driveindex.module

import io.github.driveindex.utils.log
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Component
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Component
class ClientModel {
    private val executor: ExecutorService = Executors.newSingleThreadExecutor()

    @PostConstruct
    private fun onSetup() {
        executor.execute {
            Thread.sleep(10_000)
            realSetup()
        }
    }

    private fun realSetup() {
        log.trace("变更追踪开始！")
//        for (client in client.listIfSupportDelta()) {
//            for (accountId in account.selectIdByClient(client.clientId)) {
//                try {
//                    if (client.type.needDelta(accountId)) {
//                        client.type.delta(accountId)
//                    }
//                } catch (e: Exception) {
//                    log.error("delta track for account $accountId failed", e)
//                }
//            }
//        }
        log.trace("变更追踪结束，静默 1 分钟……")
        Thread.sleep(60 * 1000)
        onSetup()
    }
}