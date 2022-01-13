package com.logickoder.simpletron.core.impl.component.cpu.instructions

import com.jeffreyorazulike.simpletron.core.impl.component.cpu.registers.Accumulator
import com.jeffreyorazulike.simpletron.core.impl.component.cpu.registers.InstructionCounter
import com.jeffreyorazulike.simpletron.core.impl.component.cpu.registers.Operand
import com.jeffreyorazulike.simpletron.core.impl.utils.*
import kotlin.math.pow

/**
 * Outputs a new line on Simpletron
 */
class NewLine : com.logickoder.simpletron.core.component.Instruction() {
    override val code = 1

    override fun execute(cpu: com.logickoder.simpletron.core.component.CPU) {
        cpu.controlUnit.display.show(newline())
    }
}

/**
 * Read a word from the keyboard into a specific location in memory.
 */
class Read : com.logickoder.simpletron.core.component.Instruction() {
    override val code = 10

    override fun execute(cpu: com.logickoder.simpletron.core.component.CPU) = with(cpu.controlUnit) {
        display.show("Enter an integer ? ")
        val value = input.read()
        val operand: Operand = cpu.register()
        memory[operand.value] = if (value.isBlank()) 0f else value.toFloat()
    }
}

/**
 * Write a word from a specific location in memory to the screen.
 */
class Write : com.logickoder.simpletron.core.component.Instruction() {
    override val code = 11

    override fun execute(cpu: com.logickoder.simpletron.core.component.CPU) = with(cpu.controlUnit) {
        val operand: Operand = cpu.register()
        display.show(memory[operand.value].toString())
    }
}

/**
 * Read a string from the keyboard and store it in memory.
 */
class ReadString : com.logickoder.simpletron.core.component.Instruction() {
    override val code = 12

    override fun execute(cpu: com.logickoder.simpletron.core.component.CPU) = with(cpu.controlUnit) {
        display.show("Enter a string ? ")
        val input = input.read()
        var address = cpu.register<Operand>().value
        memory[address] = input.length
        for (i in input.indices) {
            memory[++address] = input[i].code
        }
    }
}

/**
 * Write a string from a specific location in memory to the screen.
 */
class WriteString : com.logickoder.simpletron.core.component.Instruction() {
    override val code = 13

    override fun execute(cpu: com.logickoder.simpletron.core.component.CPU) = with(cpu.controlUnit) {
        var address = cpu.register<Operand>().value
        val length = memory[address].toInt()
        val output = StringBuilder(length)
        for (i in 0 until length) {
            output.append(memory[++address].toInt().toChar())
        }
        display.show("$output")
    }
}

/**
 * Load a word from a specific location in memory into the accumulator.
 */
class Load : com.logickoder.simpletron.core.component.Instruction() {
    override val code = 20

    override fun execute(cpu: com.logickoder.simpletron.core.component.CPU) {
        val operand = cpu.register<Operand>()
        val accumulator = cpu.register<Accumulator>()

        accumulator.value = cpu.controlUnit.memory[operand.value]
    }
}

/**
 * Store a word from the accumulator into a specific location in memory.
 */
class Store : com.logickoder.simpletron.core.component.Instruction() {
    override val code = 21

    override fun execute(cpu: com.logickoder.simpletron.core.component.CPU) {
        val operand = cpu.register<Operand>()
        val accumulator = cpu.register<Accumulator>()

        cpu.controlUnit.memory[operand.value] = accumulator.value
    }
}

/**
 * Adds a word from a specific location in memory to the word in the
 * accumulator
 */
class Add : com.logickoder.simpletron.core.component.Instruction() {
    override val code = 30

    override fun execute(cpu: com.logickoder.simpletron.core.component.CPU): Unit = with(cpu) {
        val operand = cpu.register<Operand>()
        val accumulator = cpu.register<Accumulator>()

        overflow(accumulator.value + controlUnit.memory[operand.value]) {
            accumulator.value = it.toFloat()
        }
    }
}

/**
 * Subtract a word from a specific location in memory from the word in the
 * accumulator
 */
class Subtract : com.logickoder.simpletron.core.component.Instruction() {
    override val code = 31

    override fun execute(cpu: com.logickoder.simpletron.core.component.CPU): Unit = with(cpu) {
        val operand = cpu.register<Operand>()
        val accumulator = cpu.register<Accumulator>()

        overflow(accumulator.value - controlUnit.memory[operand.value]) {
            accumulator.value = it.toFloat()
        }
    }
}

/**
 * Divide a word from a specific location in memory from the word in the
 * accumulator
 */
class Divide : com.logickoder.simpletron.core.component.Instruction() {
    override val code = 32

    override fun execute(cpu: com.logickoder.simpletron.core.component.CPU): Unit = with(cpu) {
        val operand = cpu.register<Operand>()
        val accumulator = cpu.register<Accumulator>()

        if (controlUnit.memory[operand.value] == 0f)
            error("Attempt to divide by zero${newline()}")
        else overflow(accumulator.value / controlUnit.memory[operand.value]) {
            accumulator.value = it.toFloat()
        }
    }
}

/**
 * Multiply a word from a specific location in memory by the word in the
 * accumulator
 */
class Multiply : com.logickoder.simpletron.core.component.Instruction() {
    override val code = 33

    override fun execute(cpu: com.logickoder.simpletron.core.component.CPU): Unit = with(cpu) {
        val operand = cpu.register<Operand>()
        val accumulator = cpu.register<Accumulator>()

        overflow(accumulator.value * controlUnit.memory[operand.value]) {
            accumulator.value = it.toFloat()
        }
    }
}

/**
 * Finds the remainder when dividing the value in the accumulator by a word
 */
class Remainder : com.logickoder.simpletron.core.component.Instruction() {
    override val code = 34

    override fun execute(cpu: com.logickoder.simpletron.core.component.CPU): Unit = with(cpu) {
        val operand = cpu.register<Operand>()
        val accumulator = cpu.register<Accumulator>()

        if (controlUnit.memory[operand.value] == 0f)
            error("Attempt to divide by zero${newline()}")
        else overflow(accumulator.value % controlUnit.memory[operand.value]) {
            accumulator.value = it.toFloat()
        }
    }
}

/**
 * Finds the accumulator raised to the power of the word in the specific
 * location
 */
class Exponent : com.logickoder.simpletron.core.component.Instruction() {
    override val code = 35

    override fun execute(cpu: com.logickoder.simpletron.core.component.CPU): Unit = with(cpu) {
        val operand = cpu.register<Operand>()
        val accumulator = cpu.register<Accumulator>()

        overflow(controlUnit.memory[operand.value].toDouble().pow(accumulator.value.toInt())) {
            accumulator.value = it.toFloat()
        }
    }
}

/**
 * Branch to a specific location in memory
 */
class Branch : com.logickoder.simpletron.core.component.Instruction() {
    override val code = 40

    override fun execute(cpu: com.logickoder.simpletron.core.component.CPU) {
        val operand = cpu.register<Operand>()
        val instructionCounter = cpu.register<InstructionCounter>()

        instructionCounter.value = operand.value
    }
}

/**
 * Branch to a specific location in memory if the accumulator is negative
 */
class BranchNeg : com.logickoder.simpletron.core.component.Instruction() {
    override val code = 41

    override fun execute(cpu: com.logickoder.simpletron.core.component.CPU) {
        val operand = cpu.register<Operand>()
        val accumulator = cpu.register<Accumulator>()
        val instructionCounter = cpu.register<InstructionCounter>()

        if (accumulator.value < 0) instructionCounter.value = operand.value
    }
}

/**
 * Branch to a specific location in memory if the accumulator is zero
 */
class BranchZero : com.logickoder.simpletron.core.component.Instruction() {
    override val code = 42

    override fun execute(cpu: com.logickoder.simpletron.core.component.CPU) {
        val operand = cpu.register<Operand>()
        val accumulator = cpu.register<Accumulator>()
        val instructionCounter = cpu.register<InstructionCounter>()

        if (accumulator.value == 0f) instructionCounter.value = operand.value
    }
}

/**
 * Halt. The program has completed its task
 */
class Halt : com.logickoder.simpletron.core.component.Instruction() {
    override val code = 43

    override fun execute(cpu: com.logickoder.simpletron.core.component.CPU) = with(cpu.controlUnit) {
        display.show("${newline()}Simpletron execution terminated${newline()}${newline()}")
        cpu.dump()
        memory.dump(display)
    }
}