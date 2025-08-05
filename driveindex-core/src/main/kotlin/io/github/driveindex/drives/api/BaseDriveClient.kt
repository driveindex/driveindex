package io.github.driveindex.drives.api

import io.github.driveindex.drives.attributes.AccountAttribute
import io.github.driveindex.drives.attributes.ClientAttribute
import io.github.driveindex.utils.StateMachine
import io.github.driveindex.utils.polymorphic
import kotlinx.serialization.KSerializer
import kotlinx.serialization.modules.SerializersModuleBuilder
import kotlin.concurrent.atomics.ExperimentalAtomicApi

sealed interface DriveClient {
    val name: String

    fun create()
    fun enable()
    fun disable()
    fun destroy()

    fun registerAttrJsonModule(module: SerializersModuleBuilder)
}

enum class DriveClientState {
    CREATED,
    ENABLED,
    DISABLED,
    DESTROYED,
}

@OptIn(ExperimentalAtomicApi::class)
abstract class BaseDriveClient: DriveClient {
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
        .newFlow(DriveClientState.DISABLED, to = DriveClientState.DESTROYED) {
            onDestroyed()
        }
        .build()

    override fun create() {
        stateMachine.init()
    }
    protected abstract fun onCreated()

    override fun enable() {
        stateMachine.moveTo(DriveClientState.ENABLED)
    }
    protected open fun onEnabled() { }

    override fun disable() {
        stateMachine.moveTo(DriveClientState.DISABLED)
    }
    protected open fun onDisabled() { }

    override fun destroy() {
        stateMachine.moveTo(DriveClientState.DESTROYED)
    }
    protected open fun onDestroyed() { }


    inline fun <reified T: ClientAttribute> SerializersModuleBuilder.polymorphicClientAttr(serializer: KSerializer<T>) {
        polymorphic(ClientAttribute::class, serializer)
    }
    inline fun <reified T: AccountAttribute> SerializersModuleBuilder.polymorphicAccountAttr(serializer: KSerializer<T>) {
        polymorphic(AccountAttribute::class, serializer)
    }
}
