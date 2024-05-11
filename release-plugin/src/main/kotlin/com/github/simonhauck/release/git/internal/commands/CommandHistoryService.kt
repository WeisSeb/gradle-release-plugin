package com.github.simonhauck.release.git.internal.commands

import com.github.simonhauck.release.git.api.CommandHistoryApi
import com.github.simonhauck.release.git.api.RevertCommand
import java.util.*
import org.gradle.api.services.BuildService
import org.gradle.api.services.BuildServiceParameters

internal abstract class CommandHistoryService :
    BuildService<BuildServiceParameters.None>, CommandHistoryApi {
    private val commandStack = Stack<RevertCommand>()

    override fun registerRevertCommand(command: RevertCommand) {
        commandStack.push(command)
    }

    override fun flushCommands() {
        commandStack.clear()
    }

    override fun revertAllCommands() {
        println("Reverting changes... (size=${commandStack.size})")
        while (commandStack.isNotEmpty()) {
            val lastCommand = commandStack.pop()

            print("${lastCommand.message}...")
            val status = lastCommand.revertCommand()
            val resultMessage = if (status.isLeft()) "FAILED" else "OK"
            println(" $resultMessage")
        }
        println("Changes reverted.")
    }
}