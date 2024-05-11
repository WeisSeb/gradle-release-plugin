package io.github.simonhauck.release.plugin

import io.github.simonhauck.release.git.internal.commands.GitCommandHistoryService
import io.github.simonhauck.release.tasks.*
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.RegularFile
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.TaskProvider

class ReleasePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension = project.registerExtension()

        project.configureGitTasks(extension)

        val releaseVersionStore =
            project.layout.buildDirectory.file("release/tmpVersion.properties")

        val calculateReleaseVersionTask =
            project.registerCalculateReleaseVersionTask(releaseVersionStore, extension)

        val writeReleaseVersionTask =
            project.tasks.register("writeReleaseVersion", WriteVersionTask::class.java) {
                it.dependsOn(calculateReleaseVersionTask)
                it.versionType.set(VersionType.RELEASE)
                it.releaseVersionStore.set(releaseVersionStore)
                it.versionFile.set(extension.versionPropertyFile)
            }

        // TODO Simon.Hauck 2024-05-12 - WIP: Continue to add support for tags
        val commitReleaseVersion =
            project.tasks.register("commitReleaseVersion", CommitAndTagTask::class.java) {
                it.dependsOn(writeReleaseVersionTask)
                it.commitMessage.set(
                    project.provider {
                        println("Resolved")
                        "Release version ${extension.versionPropertyFile.get().asFile.readText()}"
                    }
                )
            }

        project.provider { commitReleaseVersion.get().commitMessage }

        val writeNextDevVersionTask =
            project.tasks.register("writeNextDevVersion", WriteVersionTask::class.java) {
                // TODO Simon.Hauck 2024-05-10 - remove this temporary dependency to the commit task
                it.dependsOn(
                    calculateReleaseVersionTask,
                    writeReleaseVersionTask,
                    commitReleaseVersion
                )
                it.versionType.set(VersionType.NEXT_DEV)
                it.releaseVersionStore.set(releaseVersionStore)
                it.versionFile.set(extension.versionPropertyFile)
            }

        project.tasks.register("release", BaseReleaseTask::class.java) {
            it.description = "Release the current version"
            it.dependsOn(writeNextDevVersionTask)
        }
    }

    private fun Project.registerCalculateReleaseVersionTask(
        releaseVersionStore: Provider<RegularFile>,
        extension: ReleaseExtension
    ): TaskProvider<CalculateReleaseVersionTask> =
        tasks.register("calculateReleaseVersion", CalculateReleaseVersionTask::class.java) {
            val stringMap = properties.map { (key, value) -> key to value.toString() }.toMap()
            it.commandLineParameters.set(stringMap)
            it.releaseVersionStorePath.set(releaseVersionStore.get().asFile)
            it.versionPropertyFile.set(extension.versionPropertyFile)
            it.releaseVersionStore.set(releaseVersionStore)
        }

    private fun Project.configureGitTasks(
        extension: ReleaseExtension,
    ) {
        val commandHistoryService = project.registerGitHistoryService()

        tasks.withType(GitTask::class.java) {
            it.gitRootDirectory.set(extension.rootGitDirectory.asFile)
            it.gitCommandHistoryApi.set(commandHistoryService)
        }
    }

    private fun Project.registerExtension(): ReleaseExtension {
        val extension = extensions.create("release", ReleaseExtension::class.java)

        return extension
    }

    private fun Project.registerGitHistoryService(): Provider<GitCommandHistoryService> =
        gradle.sharedServices.registerIfAbsent(
            "commandHistory",
            GitCommandHistoryService::class.java
        )
}
