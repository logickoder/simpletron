package dev.logickoder.simpletron.core.input

sealed interface InputType {
    object CommandLine : InputType
    data class File(internal val path: String) : InputType
}