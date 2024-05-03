package com.github.simonhauck.git.process

sealed interface ProcessResult {

    data class OK(val exitCode: Int) : ProcessResult

    data class Error(val exitCode: Int?, val message: String, val throwable: Throwable?) :
        ProcessResult

    companion object {
        fun fromExitCode(exitCode: Int): ProcessResult {
            if (exitCode == 0) return OK(exitCode)

            return Error(exitCode, "Process failed with exit code $exitCode", null)
        }
    }
}