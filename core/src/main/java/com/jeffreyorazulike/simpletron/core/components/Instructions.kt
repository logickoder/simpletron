package com.jeffreyorazulike.simpletron.core.components

import kotlin.math.pow


private fun Memory.overflow(value: Int) = value < minWord || value > maxWord
private fun Display.overflow() = show("Memory Overflow\n")

/**
 * Outputs a new line on Simpletron
 */
class NewLine : Instruction() {
    override val code = 1

    override fun execute(controlUnit: CPU.ControlUnit) {
        controlUnit.display.show("\n")
    }
}

/**
 * Read a word from the keyboard into a specific location in memory.
 */
class Read : Instruction() {
    override val code = 10

    override fun execute(controlUnit: CPU.ControlUnit) = with(controlUnit) {
        display.show("Enter an integer ? ")
        val value = input.read()
        memory[Operand.value] = if (value.isBlank()) 0 else value.toInt()
    }
}

/**
 * Write a word from a specific location in memory to the screen.
 */
class Write : Instruction() {
    override val code = 11

    override fun execute(controlUnit: CPU.ControlUnit) = with(controlUnit) {
        display.show(memory[Operand.value].toString())
    }
}

/**
 * Read a string from the keyboard and store it in memory.
 */
class ReadString : Instruction() {
    override val code = 12

    override fun execute(controlUnit: CPU.ControlUnit) = with(controlUnit) {
        display.show("Enter a string ? ")
        val input = input.read()
        var address = Operand.value
        memory[address] = input.length
        for (i in input.indices) {
            memory[++address] = input[i].code
        }
    }
}

/**
 * Write a string from a specific location in memory to the screen.
 */
class WriteString : Instruction() {
    override val code = 13

    override fun execute(controlUnit: CPU.ControlUnit) = with(controlUnit) {
        var address = Operand.value
        val addressValue = memory[address]
        val output = StringBuilder(addressValue)
        for (i in 0 until addressValue) {
            output.append(memory[++address].toChar())
        }
        display.show("$output")
    }
}

/**
 * Load a word from a specific location in memory into the accumulator.
 */
class Load : Instruction() {
    override val code = 20

    override fun execute(controlUnit: CPU.ControlUnit) {
        Accumulator.value = controlUnit.memory[Operand.value]
    }
}

/**
 * Store a word from the accumulator into a specific location in memory.
 */
class Store : Instruction() {
    override val code = 21

    override fun execute(controlUnit: CPU.ControlUnit) {
        controlUnit.memory[Operand.value] = Accumulator.value
    }
}

/**
 * Adds a word from a specific location in memory to the word in the
 * accumulator
 */
class Add : Instruction() {
    override val code = 30

    override fun execute(controlUnit: CPU.ControlUnit) = with(controlUnit) {
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
class Subtract : Instruction() {
    override val code = 31

    override fun execute(controlUnit: CPU.ControlUnit) = with(controlUnit) {
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
class Divide : Instruction() {
    override val code = 32

    override fun execute(controlUnit: CPU.ControlUnit)  = with(controlUnit) {
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
class Multiply : Instruction() {
    override val code = 33

    override fun execute(controlUnit: CPU.ControlUnit) = with(controlUnit) {
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
class Remainder : Instruction() {
    override val code = 34

    override fun execute(controlUnit: CPU.ControlUnit) = with(controlUnit) {
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
class Exponent : Instruction() {
    override val code = 35

    override fun execute(controlUnit: CPU.ControlUnit) = with(controlUnit) {
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
class Branch : Instruction() {
    override val code = 40

    override fun execute(controlUnit: CPU.ControlUnit) {
        InstructionCounter.value = Operand.value
    }
}

/**
 * Branch to a specific location in memory if the accumulator is negative
 */
class BranchNeg : Instruction() {
    override val code = 41

    override fun execute(controlUnit: CPU.ControlUnit) {
        if(Accumulator.value < 0) InstructionCounter.value = Operand.value
    }
}

/**
 * Branch to a specific location in memory if the accumulator is zero
 */
class BranchZero : Instruction() {
    override val code = 42

    override fun execute(controlUnit: CPU.ControlUnit) {
        if(Accumulator.value == 0) InstructionCounter.value = Operand.value
    }
}

/**
 * Halt. The program has completed its task
 */
class Halt : Instruction() {
    override val code = 43

    override fun execute(controlUnit: CPU.ControlUnit) {
        controlUnit.display.show("Simpletron execution terminated\n")
        // TODO: Display dump
    }
}