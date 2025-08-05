package io.github.driveindex.utils

import java.nio.charset.Charset
import java.security.MessageDigest
import java.util.*
import java.util.regex.Pattern

private val pattern = Pattern.compile("[\\u4E00-\\u9FA5]+")
private val GB2312 = Charset.forName("GB2312")

fun String.isChinese(): Boolean {
    return pattern.matcher(this).find()
}

fun String.isSimplifyChinese(): Boolean {
    return isChinese() && String(toByteArray(GB2312)) == this
}

val ByteArray.BASE64: String get() {
    return Base64.getEncoder().encodeToString(this)
}
val String.BASE64: String get() {
    return toByteArray(Charsets.UTF_8).BASE64
}

val String.ORIGIN_BASE64: String get() {
    return Base64.getDecoder().decode(this).toString(Charsets.UTF_8)
}

private val md5: MessageDigest get() = MessageDigest.getInstance("MD5")

/**
 * 16 位 MD5
 */
val String.MD5: String get() {
    return MD5_FULL.substring(5, 24)
}
val String.MD5_UPPER: String get() {
    return MD5.uppercase()
}

/**
 * 32 位 MD5
 */
val String.MD5_FULL: String get() {
    val digest = md5.digest(toByteArray())
    return StringBuffer().run {
        for (b in digest) {
            val i :Int = b.toInt() and 0xff
            var hexString = Integer.toHexString(i)
            if (hexString.length < 2) {
                hexString = "0$hexString"
            }
            append(hexString)
        }
        toString()
    }
}
val String.MD5_FULL_UPPER: String get() {
    return MD5_FULL.uppercase()
}

/**
 * SHA256
 */
private val sha256: MessageDigest get() = MessageDigest.getInstance("SHA-256")
val String.SHA_256: String get() {
    return sha256.digest(toByteArray())
        .fold("", { str, it -> str + "%02x".format(it) })
}
val String.SHA_256_UPPER: String get() {
    return SHA_256.uppercase()
}


fun Map<String, Any>.joinToString(
    separator: CharSequence = ", ", prefix: CharSequence = "",
    postfix: CharSequence = "", limit: Int = -1,
    truncated: CharSequence = "...",
    transform: ((Map.Entry<String, Any>) -> String)? = null
): String {
    return entries.joinToString(
        separator, prefix, postfix, limit, truncated
    ) transform@{
        return@transform transform?.invoke(it) ?: "${it.key}=${it.value}"
    }
}

fun Map<String, Any>.joinToSortedString(
    separator: CharSequence = ", ", prefix: CharSequence = "",
    postfix: CharSequence = "", limit: Int = -1,
    truncated: CharSequence = "...",
    transform: ((Map.Entry<String, Any>) -> String)? = null
): String {
    return entries.sortedBy {
        return@sortedBy it.key
    }.joinToString(
        separator, prefix, postfix, limit, truncated
    ) transform@{
        return@transform transform?.invoke(it) ?: "${it.key}=${it.value}"
    }
}