package io.github.driveindex.core.util

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.springframework.context.annotation.Configuration
import org.springframework.format.FormatterRegistry
import org.springframework.format.Formatter
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.io.File
import java.security.MessageDigest
import java.util.*

/**
 * 一个用于解析标准路径的工具类
 * @author sgpublic
 * @Date 2022/8/15 17:15
 */
@JsonSerialize(using = CanonicalPathSerializer::class)
@JsonDeserialize(using = CanonicalPathDeserializer::class)
class CanonicalPath : Cloneable, Iterable<String> {
    /** CanonicalPath 不可变，同 String  */
    private val pathStack: Stack<String>
    val length: Int get() = pathStack.size

    /**
     * 获取当前规范路径文本
     * @return 规范路径文本
     */
    val path: String get() = mPath
    private val mPath: String
    val pathSha256: String by lazy {
        MessageDigest.getInstance("SHA3-256")
            .digest(path.toByteArray()).BASE64
    }

    val file: File get() = File(mPath)

    private constructor(path: String) {
        pathStack = Stack()
        val files = path.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (file in files) {
            if ("" == file || "." == file) continue
            if (".." == file) {
                if (!pathStack.empty()) pathStack.pop()
                continue
            }
            pathStack.push(file)
        }
        mPath = generatePath()
    }

    /**
     * 直接传入目录栈创建 CanonicalPath 对象
     * @param pathStack 目录栈
     * @see CanonicalPath.clone
     */
    private constructor(pathStack: Stack<String>) {
        this.pathStack = pathStack
        mPath = generatePath()
    }

    private fun generatePath(): String {
        if (pathStack.empty()) return ROOT_PATH
        val ans = StringJoiner("/", "/", "")
        for (s in pathStack) ans.add(s)
        return ans.toString()
    }

    /**
     * 获取父级文件对象
     * @return 父级文件对象，若当前目录已经为根目录，则返回 ROOT_PATH
     */
    val parentPath: CanonicalPath
        get() {
            val path = clonePath()
            if (path.isEmpty()) return of(ROOT_PATH) else path.pop()
            return CanonicalPath(path)
        }

    /**
     * 是否存在父级目录
     * @return 是否存在父级目录
     */
    fun hasParent(): Boolean {
        return pathStack.isNotEmpty()
    }

    /**
     * 将新文件添加到当前路径后面
     * @param newFile 要添加的文件名
     * @return 添加后的新路径
     */
    fun append(newFile: String): CanonicalPath {
        val path = clonePath()
        path.add(newFile)
        return CanonicalPath(path)
    }

    /**
     * 将指定 path 添加到当前路径后面
     * @param newPath 要添加的路径
     * @return 添加后的新路径
     */
    fun append(newPath: CanonicalPath): CanonicalPath {
        val pathStack = clonePath()
        pathStack.addAll(newPath.pathStack)
        return CanonicalPath(pathStack)
    }

    /**
     * 将新文件添加到当前路径前面
     * @param newFile 要添加的文件名
     * @return 添加后的新路径
     */
    fun push(newFile: String): CanonicalPath {
        val path = Stack<String>()
        path.add(newFile)
        path.addAll(pathStack)
        return CanonicalPath(path)
    }

    /**
     * 将指定 path 添加到当前路径前面
     * @param newPath 要添加的路径
     * @return 添加后的新路径
     */
    fun push(newPath: CanonicalPath): CanonicalPath {
        val pathStack = newPath.clonePath()
        pathStack.addAll(this.pathStack)
        return CanonicalPath(pathStack)
    }

    /**
     * 获取当前文件名
     * @return 文件名，若当前路径为根目录则返回 null
     */
    val currentFile: String?
        get() = if (pathStack.isEmpty()) null else pathStack.peek()

    /**
     * 返回当前规范路径文本
     * @see CanonicalPath.mPath
     * @return 规范路径文本
     */
    override fun toString(): String {
        return mPath
    }

    public override fun clone(): CanonicalPath {
        return CanonicalPath(clonePath())
    }

    private fun clonePath(): Stack<String> {
        val newStack = Stack<String>()
        newStack.addAll(pathStack)
        return newStack
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return if (other !is CanonicalPath) false
        else pathStack == other.pathStack
    }

    override fun hashCode(): Int {
        return pathStack.hashCode()
    }

    fun subPath(start: Int, end: Int = pathStack.size): CanonicalPath {
        return CanonicalPath(Stack<String>().also {
            it.addAll(pathStack.subList(start, end))
        })
    }

    override fun iterator(): Iterator<String> {
        return pathStack.iterator()
    }

    companion object {
        const val ROOT_PATH = "/"
        val ROOT = of(ROOT_PATH)

        /**
         * 利用路径文本创建 CanonicalPath 对象
         * @param path 路径文本
         * @return CanonicalPath 对象
         */
        fun of(path: String): CanonicalPath {
            return CanonicalPath(path.replace('\\', '/')
                .replace(":".toRegex(), ""))
        }
    }

    object CanonicalPathFormatter: Formatter<CanonicalPath> {
        override fun print(target: CanonicalPath, locale: Locale): String {
            return target.path
        }

        override fun parse(text: String, locale: Locale): CanonicalPath {
            return of(text)
        }
    }

    @Configuration
    class CanonicalPathConfiguration: WebMvcConfigurer {
        override fun addFormatters(registry: FormatterRegistry) {
            registry.addFormatter(CanonicalPathFormatter)
        }
    }
}

object CanonicalPathSerializer: JsonSerializer<CanonicalPath>() {
    override fun serialize(value: CanonicalPath, gen: JsonGenerator, serializers: SerializerProvider) {
        gen.writeString(value.path)
    }
}
object CanonicalPathDeserializer: JsonDeserializer<CanonicalPath>() {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): CanonicalPath {
        return CanonicalPath.of(p.text)
    }
}

fun String.asPath(): CanonicalPath {
    return CanonicalPath.of(this)
}
