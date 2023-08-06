package io.github.driveindex.tasks

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.ResetCommand
import org.eclipse.jgit.errors.RepositoryNotFoundException
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.property
import org.slf4j.LoggerFactory

/**
 * @author sgpublic
 * @Date 2023/8/6 16:10
 */
open class CheckoutTask: DefaultTask() {
    @get:Internal
    val root: DirectoryProperty = project.objects.directoryProperty()

    @get:Input
    val url: Property<String> = project.objects.property()

    @get:Optional
    @get:Input
    val username: Property<String> = project.objects.property()

    @get:Optional
    @get:Input
    val token: Property<String> = project.objects.property()

    @get:Optional
    @get:Input
    val branch: Property<String> = project.objects.property()

    private val log = LoggerFactory.getLogger(CheckoutTask::class.java)

    @TaskAction
    fun checkout() {
        val target = root.asFile.get()
        val branch: String? = this.branch.orNull
        val url: String = this.url.get()

        val username: String? = this.username.orNull
        val token: String? = this.token.orNull

        try {
            Git.open(target).also { git ->
                val remote = git.repository.config.getString("remote", "origin", "url")
                if (remote != url) {
                    git.close()
                    throw IllegalStateException("此目录不是目标仓库，而是：$remote")
                }
                if (branch != null) {
                    git.checkout()
                        .setName(branch)
                        .setStartPoint("origin/$branch")
                        .call()
                }
                git.fetch().call()
                git.reset().setRef("HEAD").setMode(ResetCommand.ResetType.HARD).call()
            }
        } catch (e: Exception) {
            when (e) {
                is RepositoryNotFoundException -> {
                    log.debug("仓库 $url 不存在，重新 clone")
                }
                is IllegalStateException -> {
                    log.info("目标仓库不匹配，重新 clone")
                }
                else -> {
                    log.info("仓库 $url 检查失败，重新 clone")
                    log.debug("错误信息：${e.message}", e)
                }
            }
            target.deleteRecursively()
            target.mkdirs()
            Git.cloneRepository()
                .setURI(url)
                .setDirectory(target)
                .also {
                    if (branch != null) {
                        it.setBranch(branch)
                    }
                    if (username != null && token != null) {
                        it.setCredentialsProvider(
                            UsernamePasswordCredentialsProvider(username, token)
                        )
                    }
                }
                .call()
        }
    }
}