package com.github.simonhauck.git.process

import java.util.concurrent.TimeUnit
import org.gradle.nativeplatform.platform.internal.DefaultNativePlatform
import org.zeroturnaround.exec.ProcessExecutor
import org.zeroturnaround.exec.stream.LogOutputStream

internal class ProcessWrapper {

    fun runCommand(command: List<String>, config: ProcessConfig = ProcessConfig()): ProcessResult {
        return runCatching {
                ProcessExecutor()
                    .directory(config.workingDir)
                    .environment(config.environment)
                    .addOsSpecificCommands(*command.toTypedArray())
                    .addConsolePrinter()
                    .destroyWithDescendants()
                    .timeout(40, TimeUnit.SECONDS)
                    .execute()
            }
            .map { ProcessResult.fromExitCode(it.exitValue) }
            .getOrElse { ProcessResult.Error(null, "Process failed with an exception", it) }
    }

    private fun ProcessExecutor.addOsSpecificCommands(vararg command: String): ProcessExecutor {
        val linuxCommand = listOf(*command)
        val windowsCommand = listOf("cmd.exe", "/c", *command)

        val commandToExecute =
            if (DefaultNativePlatform.getCurrentOperatingSystem().isWindows) windowsCommand
            else linuxCommand

        return this.command(commandToExecute)
    }

    private fun ProcessExecutor.addConsolePrinter(): ProcessExecutor {
        return this.redirectOutput(ProcessLogger(false)).redirectError(ProcessLogger(true))
    }

    private fun ProcessExecutor.destroyWithDescendants(): ProcessExecutor {
        return this.destroyOnExit().stopper { it.destroyWithDescendants() }
    }

    private fun Process.destroyWithDescendants() {
        println("Killing process children...")
        this.descendants().forEach { childProcess -> childProcess.destroy() }
        println("Killing process...")
        this.destroy()
        println("Waiting for shutdown (30 sec max)...")
        this.waitFor(30, TimeUnit.SECONDS)
        println("Process is terminated")
    }

    private class ProcessLogger(private val isError: Boolean) : LogOutputStream() {
        override fun processLine(line: String) {
            if (isError) System.err.println(line) else println(line)
        }
    }
}
