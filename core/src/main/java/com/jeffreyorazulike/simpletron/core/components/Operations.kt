package com.jeffreyorazulike.simpletron.core.components

import com.jeffreyorazulike.simpletron.core.components.Registers.*
import kotlin.math.pow


private fun Memory.overflow(value: Int) = value < minWord || value > maxWord
private fun Display.overflow() = show("Memory Overflow\n")

/**
 * Outputs a new line on Simpletron
 */
class NewLine : Operation() {
    override val code = 1

    override fun execute(params: ComponentParam) {
        params.display.show("\n")
    }
}

/**
 * Read a word from the keyboard into a specific location in memory.
 */
class Read : Operation() {
    override val code = 10

    override fun execute(params: ComponentParam) = with(params) {
        display.show("Enter an integer ? ")
        memory[OperationCode.value] = input.read().toInt()
    }
}

/**
 * Write a word from a specific location in memory to the screen.
 */
class Write : Operation() {
    override val code = 11

    override fun execute(params: ComponentParam) = with(params) {
        display.show(memory[Operand.value].toString())
    }
}

/**
 * Read a string from the keyboard and store it in memory.
 */
class ReadString : Operation() {
    override val code = 12

    override fun execute(params: ComponentParam) = with(params) {
        display.show("Enter a string ? ")
        val input = input.read()
        var address = Operand.value
        memory[address] = input.length
        for (i in input.indices) {
            memory[--address] = input[i].code
        }
    }
}

/**
 * Write a string from a specific location in memory to the screen.
 */
class WriteString : Operation() {
    override val code = 13

    override fun execute(params: ComponentParam) = with(params) {
        var address = Operand.value
        val addressValue = memory[address]
        val output = StringBuilder(addressValue)
        for (i in 0 until addressValue) {
            output.append(memory[--address].toChar())
        }
        display.show("$output\n")
    }
}

/**
 * Load a word from a specific location in memory into the accumulator.
 */
class Load : Operation() {
    override val code = 20

    override fun execute(params: ComponentParam) {
        Accumulator.value = params.memory[Operand.value]
    }
}

/**
 * Store a word from the accumulator into a specific location in memory.
 */
class Store : Operation() {
    override val code = 21

    override fun execute(params: ComponentParam) {
        params.memory[Operand.value] = Accumulator.value
    }
}

/**
 * Adds a word from a specific location in memory to the word in the
 * accumulator
 */
class Add : Operation() {
    override val code = 30

    override fun execute(params: ComponentParam) = with(params) {
        val result = Accumulator.value + memory[Operand.value]

        if(memory.overflow(result))
            display.overflow()
        else
            Accumulator.value = result
    }
}

/**
 * Subtract a word from a specific location in memory from the word in the
 * accumulator
 */
class Subtract : Operation() {
    override val code = 31

    override fun execute(params: ComponentParam) = with(params) {
        val result = Accumulator.value - memory[Operand.value]

        if(memory.overflow(result))
            display.overflow()
        else
            Accumulator.value = result
    }
}

/**
 * Divide a word from a specific location in memory from the word in the
 * accumulator
 */
class Divide : Operation() {
    override val code = 32

    override fun execute(params: ComponentParam)  = with(params) {
        if(memory[Operand.value] == 0)
            display.show("Attempt to divide by zero\n")
        else {
            val result = Accumulator.value / memory[Operand.value]

            if(memory.overflow(result))
                display.overflow()
            else
                Accumulator.value = result
        }
    }
}

/**
 * Multiply a word from a specific location in memory by the word in the
 * accumulator
 */
class Multiply : Operation() {
    override val code = 33

    override fun execute(params: ComponentParam) = with(params) {
        val result = Accumulator.value * memory[Operand.value]

        if(memory.overflow(result))
            display.overflow()
        else
            Accumulator.value = result
    }
}

/**
 * Finds the remainder when dividing the value in the accumulator by a word
 */
class Remainder : Operation() {
    override val code = 34

    override fun execute(params: ComponentParam) = with(params) {
        if(memory[Operand.value] == 0)
            display.show("Attempt to divide by zero\n")
        else {
            val result = Accumulator.value % memory[Operand.value]

            if(memory.overflow(result))
                display.overflow()
            else
                Accumulator.value = result
        }
    }
}

/**
 * Finds the accumulator raised to the power of the word in the specific
 * location
 */
class Exponent : Operation() {
    override val code = 35

    override fun execute(params: ComponentParam) = with(params) {
        val result = memory[Operand.value].toDouble().pow(Accumulator.value).toInt()

        if(memory.overflow(result))
            display.overflow()
        else
            Accumulator.value = result
    }
}

/**
 * Branch to a specific location in memory
 */
class Branch : Operation() {
    override val code = 40

    override fun execute(params: ComponentParam) {
        InstructionCounter.value = Operand.value
    }
}

/**
 * Branch to a specific location in memory if the accumulator is negative
 */
class BranchNeg : Operation() {
    override val code = 41

    override fun execute(params: ComponentParam) {
        if(Accumulator.value < 0) InstructionCounter.value = Operand.value
    }
}

/**
 * Branch to a specific location in memory if the accumulator is zero
 */
class BranchZero : Operation() {
    override val code = 42

    override fun execute(params: ComponentParam) {
        if(Accumulator.value == 0) InstructionCounter.value = Operand.value
    }
}

/**
 * Halt. The program has completed its task
 */
class Halt : Operation() {
    override val code = 43

    override fun execute(params: ComponentParam) {
        params.display.show("Simpletron execution terminated\n")
        // TODO: Display dump
    }
}