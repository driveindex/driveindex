package io.github.driveindex.core.util

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
inline fun <T: Any> T?.runIfNotNull(block: (T) -> Unit) {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
    }
    if (this != null) {
        block(this)
    }
}

@OptIn(ExperimentalContracts::class)
inline fun <reified CastT: Any, OutT: Any> Any?.castOrNull(block: (CastT) -> OutT): OutT? {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
    }
    if (this !is CastT) {
        return null
    }
    return block(this)
}
