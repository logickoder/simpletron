package dev.logickoder.simpletron.core.cpu

import kotlin.math.pow

/**
 * Read a word from the keyboard into a specific location in memory.
 */
object Read : Instruction(10) {

    override fun execute(cpu: CPU) = with(cpu) {
        display.show("Enter an integer ? ")
        val value = input.read()
        val operand: Operand = register()
        memory[operand.value] = if (value.isBlank()) 0f else value.toFloat()
    }
}

/**
 * Write a word from a specific location in memory to the screen.
 */
object Write : Instruction(11) {

    override fun execute(cpu: CPU) = with(cpu) {
        val operand: Operand = register()
        display.show(memory[operand.value].toString())
    }
}

/**
 * Read a string from the keyboard and store it in memory.
 */
object ReadString : Instruction(12) {

    override fun execute(cpu: CPU) = with(cpu) {
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
object WriteString : Instruction(13) {

    override fun execute(cpu: CPU) = with(cpu) {
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
 * Outputs a new line on Simpletron
 */
object NewLine : Instruction(14) {

    override fun execute(cpu: CPU) = with(cpu.display) {
        show(newline)
    }
}

/**
 * Load a word from a specific location in memory into the accumulator.
 */
object Load : Instruction(20) {

    override fun execute(cpu: CPU) {
        val operand = cpu.register<Operand>()
        val accumulator = cpu.register<Accumulator>()

        accumulator.value = cpu.memory[operand.value]
    }
}

/**
 * Store a word from the accumulator into a specific location in memory.
 */
object Store : Instruction(21) {

    override fun execute(cpu: CPU) {
        val operand = cpu.register<Operand>()
        val accumulator = cpu.register<Accumulator>()

        cpu.memory[operand.value] = accumulator.value
    }
}

/**
 * Adds a word from a specific location in memory to the word in the
 * accumulator
 */
object Add : Instruction(30) {

    override fun execute(cpu: CPU): Unit = with(cpu) {
        val operand = cpu.register<Operand>()
        val accumulator = cpu.register<Accumulator>()

        overflow(accumulator.value + memory[operand.value]) {
            accumulator.value = it.toFloat()
        }
    }
}

/**
 * Subtract a word from a specific location in memory from the word in the
 * accumulator
 */
object Subtract : Instruction(31) {

    override fun execute(cpu: CPU): Unit = with(cpu) {
        val operand = cpu.register<Operand>()
        val accumulator = cpu.register<Accumulator>()

        overflow(accumulator.value - memory[operand.value]) {
            accumulator.value = it.toFloat()
        }
    }
}

/**
 * Divide a word from a specific location in memory from the word in the
 * accumulator
 */
object Divide : Instruction(32) {

    override fun execute(cpu: CPU): Unit = with(cpu) {
        val operand = cpu.register<Operand>()
        val accumulator = cpu.register<Accumulator>()

        if (memory[operand.value] == 0f)
            error("Attempt to divide by zero${display.newline}")
        else overflow(accumulator.value / memory[operand.value]) {
            accumulator.value = it.toFloat()
        }
    }
}

/**
 * Multiply a word from a specific location in memory by the word in the
 * accumulator
 */
object Multiply : Instruction(33) {

    override fun execute(cpu: CPU): Unit = with(cpu) {
        val operand = cpu.register<Operand>()
        val accumulator = cpu.register<Accumulator>()

        overflow(accumulator.value * memory[operand.value]) {
            accumulator.value = it.toFloat()
        }
    }
}

/**
 * Finds the remainder when dividing the value in the accumulator by a word
 */
object Remainder : Instruction(34) {

    override fun execute(cpu: CPU): Unit = with(cpu) {
        val operand = cpu.register<Operand>()
        val accumulator = cpu.register<Accumulator>()

        if (memory[operand.value] == 0f)
            error("Attempt to divide by zero${display.newline}")
        else overflow(accumulator.value % memory[operand.value]) {
            accumulator.value = it.toFloat()
        }
    }
}

/**
 * Finds the accumulator raised to the power of the word in the specific
 * location
 */
object Exponent : Instruction(35) {

    override fun execute(cpu: CPU): Unit = with(cpu) {
        val operand = cpu.register<Operand>()
        val accumulator = cpu.register<Accumulator>()

        overflow(memory[operand.value].toDouble().pow(accumulator.value.toInt())) {
            accumulator.value = it.toFloat()
        }
    }
}

/**
 * Branch to a specific location in memory
 */
object Branch : Instruction(40) {

    override fun execute(cpu: CPU) {
        val operand = cpu.register<Operand>()
        val instructionCounter = cpu.register<InstructionCounter>()

        instructionCounter.value = operand.value
    }
}

/**
 * Branch to a specific location in memory if the accumulator is negative
 */
object BranchNeg : Instruction(41) {

    override fun execute(cpu: CPU) {
        val operand = cpu.register<Operand>()
        val accumulator = cpu.register<Accumulator>()
        val instructionCounter = cpu.register<InstructionCounter>()

        if (accumulator.value < 0) instructionCounter.value = operand.value
    }
}

/**
 * Branch to a specific location in memory if the accumulator is zero
 */
object BranchZero : Instruction(42) {

    override fun execute(cpu: CPU) {
        val operand = cpu.register<Operand>()
        val accumulator = cpu.register<Accumulator>()
        val instructionCounter = cpu.register<InstructionCounter>()

        if (accumulator.value == 0f) instructionCounter.value = operand.value
    }
}

/**
 * Halt. The program has completed its task
 */
object Halt : Instruction(43) {

    override fun execute(cpu: CPU) = with(cpu) {
        display.show("${display.newline}Simpletron execution terminated${display.newline}${display.newline}")
        cpu.dump()
        memory.dump(display)
    }
}