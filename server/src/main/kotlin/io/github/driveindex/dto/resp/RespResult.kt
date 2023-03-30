package io.github.driveindex.dto.resp

import java.io.Serializable

/**
 * @author sgpublic
 * @Date 2023/2/7 16:07
 */
open class RespResult<T: Any> internal constructor(
    val code: Int = 200,
    val message: String = "success.",
    val data: T? = null
): Serializable {

}

object SampleResult: RespResult<Unit>()

fun <T: Any> T.resp(): RespResult<T> {
    return RespResult(data = this)
}