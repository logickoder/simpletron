package dev.logickoder.simpletron.translator.symbol

/**
 * Represents data types supported by the translator
 */
sealed interface DataType {
    /**
     * Represents a number value
     */
    data class Number(val value: kotlin.Number) : DataType

    /**
     * Represents a string text
     */
    data class String(val value: kotlin.String) : DataType

    /**
     * Represents an array of data types
     */
    data class Array(val value: List<DataType>) : DataType
}