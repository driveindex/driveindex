package io.github.driveindex.drivers.api

import io.github.driveindex.drivers.attributes.AccountAttribute
import io.github.driveindex.drivers.attributes.ClientAttribute
import io.github.driveindex.utils.StateMachine
import io.github.driveindex.utils.polymorphic
import kotlinx.serialization.KSerializer
import kotlinx.serialization.modules.SerializersModuleBuilder
import kotlin.concurrent.atomics.ExperimentalAtomicApi

sealed interface NetDiskContext {
    fun create()
    fun enable()
    fun disable()

    fun registerAttrJsonModule(module: SerializersModuleBuilder)
}

enum class DriveClientState {
    CREATED,
    ENABLED,
    DISABLED,
}

@OptIn(ExperimentalAtomicApi::class)
abstract class BaseNetDiskDriver: NetDiskContext {
    private val stateMachine = StateMachine.builder<DriveClientState>()
        .init(DriveClientState.CREATED) {
            onCreated()
        }
        .newFlow(DriveClientState.CREATED, DriveClientState.DISABLED, to = DriveClientState.ENABLED) {
            onEnabled()
        }
        .newFlow(DriveClientState.ENABLED, DriveClientState.CREATED, to = DriveClientState.DISABLED) {
            onDisabled()
        }
        .build()

    final override fun create() {
        stateMachine.init()
    }
    protected open fun onCreated() { }

    final override fun enable() {
        stateMachine.moveTo(DriveClientState.ENABLED)
    }
    protected open fun onEnabled() { }

    final override fun disable() {
        stateMachine.moveTo(DriveClientState.DISABLED)
    }
    protected open fun onDisabled() { }

    inline fun <reified T: ClientAttribute> SerializersModuleBuilder.polymorphicClientAttr(serializer: KSerializer<T>) {
        polymorphic(ClientAttribute::class, serializer)
    }
    inline fun <reified T: AccountAttribute> SerializersModuleBuilder.polymorphicAccountAttr(serializer: KSerializer<T>) {
        polymorphic(AccountAttribute::class, serializer)
    }
}
