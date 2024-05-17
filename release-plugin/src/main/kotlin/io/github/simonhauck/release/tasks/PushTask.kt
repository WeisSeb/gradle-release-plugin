package io.github.simonhauck.release.tasks

import java.time.Duration
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.logging.Logging
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction

abstract class PushTask : BaseReleaseTask(), GitTask {
    private val log = Logging.getLogger(PushTask::class.java)

    @get:InputFile @get:Optional abstract val sshKeyFile: RegularFileProperty

    @get:Input @get:Optional abstract val disablePush: Property<Boolean>
    @get:Input @get:Optional abstract val delayBeforePushInMs: Property<Duration>

    // TODO Simon.Hauck 2024-05-17 - test this task
    @TaskAction
    fun push() {
        if (disablePush.get()) {
            println("Push is disabled by configuration.")
            gitCommandHistoryApi.get().flushCommands()
            return
        }

        val delay: Duration = delayBeforePushInMs.getOrElse(Duration.ZERO)
        if (!delay.isZero)
            log.lifecycle("Delay before push is set to ${delay.seconds}s. Waiting...")

        Thread.sleep(delay.seconds)

        gitCommandApi().pullRebase()

        log.lifecycle("Pushing changes to remote repository")
        gitCommandApi()
            .push(sshKeyFile.orNull?.asFile)
            .onRight { gitCommandHistoryApi.get().flushCommands() }
            .onLeft { gitCommandHistoryApi.get().revertAllCommands() }
    }
}