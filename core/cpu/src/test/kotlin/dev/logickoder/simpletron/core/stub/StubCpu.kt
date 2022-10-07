package dev.logickoder.simpletron.core.stub

import dev.logickoder.simpletron.core.common.Component
import dev.logickoder.simpletron.core.common.classInstances
import dev.logickoder.simpletron.core.common.classes
import dev.logickoder.simpletron.core.cpu.*
import dev.logickoder.simpletron.core.cpu.CPUTest.Companion.TEST_VALUE
import dev.logickoder.simpletron.core.cpu.CPUTest.Companion.mockInput
import dev.logickoder.simpletron.core.display.Display
import dev.logickoder.simpletron.core.input.Input
import dev.logickoder.simpletron.core.memory.Memory
import org.mockito.kotlin.mock

internal class StubRegister : Register<Int>() {
    override var value = TEST_VALUE
    override val name = "Stub"
}

internal class StubOp : Instruction() {
    override val code = TEST_VALUE
    override fun execute(cpu: CPU) {}
}

internal class StubCpu : CPU {
    override val display: Display = mock { }
    override val input: Input = mockInput()
    override val memory: Memory = Memory()

    override val registers: List<Register<*>> = classes<Register<*>>(
        setOf(Accumulator::class.java, StubCpu::class.java)
    ).classInstances()

    override val instructions: List<Instruction> = classes<Instruction>(
        setOf(Read::class.java, StubCpu::class.java)
    ).classInstances()

    override fun <T : Component> swap(component: T) {
    }

    override fun execute(): Instruction? = null
}