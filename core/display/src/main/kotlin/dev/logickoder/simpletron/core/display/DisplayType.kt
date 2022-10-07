package dev.logickoder.simpletron.core.display

sealed interface DisplayType {
    data class CommandLine(internal val stopValue: Int) : DisplayType
    data class File(internal val path: String) : DisplayType
}