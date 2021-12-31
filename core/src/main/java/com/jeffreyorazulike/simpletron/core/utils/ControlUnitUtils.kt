package com.jeffreyorazulike.simpletron.core.utils

import com.jeffreyorazulike.simpletron.core.components.CPU

/**
 * @param value the number to check for overflow
 * @param action the action to perform if no overflow occurred
 *
 * Checks to see if the given value overflowed the memory
 * */
inline fun CPU.ControlUnit.overflow(
    value: Number,
    action: (Number) -> Unit = {}
): Boolean {
    return if (value.toFloat() !in memory.minWord..memory.maxWord) {
        display.show("Memory Overflow\n")
        true
    } else {
        action(value)
        false
    }
}