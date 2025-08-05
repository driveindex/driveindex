package io.github.driveindex.utils

import kotlin.concurrent.atomics.AtomicReference
import kotlin.concurrent.atomics.ExperimentalAtomicApi

typealias StateCallback = () -> Unit

@OptIn(ExperimentalAtomicApi::class)
class StateMachine<T: Enum<T>> internal constructor(
    private val initState: Pair<T, StateCallback>,
    private val stateMachine: Map<T, Map<T, StateCallback>>,
) {
    private val currentState: AtomicReference<T?> = AtomicReference(null)

    fun current(): T {
        return currentState.load()
            ?: throw IllegalStateException("State machine is not initialized!")
    }

    fun init(): Boolean {
        if (!currentState.compareAndSet(null, initState.first)) {
            return false
        }
        initState.second()
        return true
    }

    fun moveTo(to: T): Boolean {
        val current = current()
        val nextStatesCallback = stateMachine[current]?.get(to)
            ?: throw IllegalStateException("State $current cannot move to state $to!")
        if (!currentState.compareAndSet(current, to)) {
            return false
        }
        nextStatesCallback()
        return true
    }

    companion object {
        fun <T: Enum<T>> builder(): StateMachineBuilder<T> {
            return StateMachineBuilder()
        }
    }
}

class StateMachineBuilder<T: Enum<T>> internal constructor() {
    private var initState: Pair<T, StateCallback>? = null
    private val stateMachine: MutableMap<T, MutableMap<T, StateCallback>> = HashMap()

    fun init(initState: T, callback: () -> Unit): StateMachineBuilder<T> {
        this.initState = initState to callback
        return this
    }

    fun newFlow(vararg from: T, to: T, callback: () -> Unit): StateMachineBuilder<T> {
        for (fromItem in from) {
            val flow = stateMachine.getOrDefault(fromItem, hashMapOf())
            flow[to] = callback
        }
        return this
    }

    fun build(): StateMachine<T> {
        if (initState == null) {
            throw IllegalStateException("You must create an initial state!")
        }
        return StateMachine(initState!!, stateMachine)
    }
}
