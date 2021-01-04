/*
 * Copyright (C) 2021 Jeffrey Orazulike
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.jeffreyorazulike.simpletron;

import com.jeffreyorazulike.simpletron.cpu.CPU;
import com.jeffreyorazulike.simpletron.cpu.CPU.OperationCode;
import com.jeffreyorazulike.simpletron.display.Display;
import com.jeffreyorazulike.simpletron.display.Display.Decorator;
import com.jeffreyorazulike.simpletron.display.Display.Messages;
import com.jeffreyorazulike.simpletron.input.Input;
import com.jeffreyorazulike.simpletron.memory.Memory;
import java.io.IOException;
import java.lang.ref.Cleaner.Cleanable;
import java.util.Arrays;

/**
 *
 * @author Jeffrey Orazulike
 * @apiNote The system that runs commands written in simpletron machine code
 */
public class Simpletron implements Cleanable {

    /**
     * <p>
     * Indicates input for a segment has ended
     * </p>
     */
    public static final int END_SEGMENT = -99999;
    private final Display display;
    private final CPU cpu;
    private final Input userInput;
    private final Memory memory;
    private final int MEMORY_SIZE;
    private final int MIN_WORD;
    private final int MAX_WORD;

    /**
     *
     * @param display   The interface used to display messages for the
     *                  simpletron pc
     * @param userInput The interface used to receive input for the user
     * @param cpu       The CPU
     * @param memory    The memory
     */
    public Simpletron(Display display, Input userInput, CPU cpu, Memory memory) {
        this.display = display;
        this.userInput = userInput;
        this.cpu = cpu;
        this.memory = memory;
        MEMORY_SIZE = memory.getConstant(Memory.Constant.MEMORY_SIZE);
        MIN_WORD = memory.getConstant(Memory.Constant.MIN_WORD);
        MAX_WORD = memory.getConstant(Memory.Constant.MAX_WORD);
    }

    @Override
    public void clean() {
        cpu.clean();
        memory.clean();
    }

    /**
     *
     * Launches Simpletron
     *
     * @param commandsInput The interface used to receive input for SML code
     *
     * @throws java.io.IOException if an i/o exception occurs when running the
     *                             commands
     *
     */
    protected void runSystem(Input commandsInput) throws IOException {
        clean();
        display.splashScreen();

        int index = 0;
        int inputValue;

        // enter the constants to be used by the program
        int memoryLocation;
        while (true) {
            String[] values = commandsInput.nextLine().split("\\D+");
            if (values.length <= 1)
                break;
            System.err.println(Arrays.toString(values));
            memoryLocation = Integer.parseInt(values[0].trim());
            inputValue = Integer.parseInt(values[1].trim());

            if (memoryLocation >= MEMORY_SIZE || memoryLocation < 1 || inputValue > MAX_WORD || inputValue < MIN_WORD)
                continue;
            memory.set(memoryLocation, inputValue);
        }

        commandsInput.returnEOL(false);
        display.line("%s %s %s", Display.Decorator.MESSAGE, Display.Messages.CONSTANT_END, Display.Decorator.MESSAGE);

        // enter the programs instructions
        do {
            if (commandsInput.onDemand()) {
                inputValue = Integer.parseInt(commandsInput.next());
                if (commandsInput.isEOF())
                    break;
                display.line("%02d %s %d", index, Decorator.INSTRUCTION, inputValue);
            } else {
                display.show("%02d %s ", index, Decorator.INSTRUCTION);
                inputValue = Integer.parseInt(commandsInput.next());
            }

            if (inputValue == END_SEGMENT)
                break;
            if (inputValue > MAX_WORD || inputValue < MIN_WORD)
                continue;
            if (index >= MEMORY_SIZE)
                showError("");
            memory.set(index++, inputValue);
        } while (true);

        Arrays.stream(Display.Messages.DONE_LOAD.split(Display.NEW_LINE))
                .forEach(line -> display.line("%s %-25s %s", Decorator.MESSAGE, line, Decorator.MESSAGE));
        display.show(Display.NEW_LINE);

        while (true)
            cpu.execute(memory).ifPresent(this::handleOperation);
    }

    public void runCommand(int instructionCounter, int operationCode) {
        runCommands(instructionCounter, new int[]{operationCode});
    }

    public void runCommands(int instructionCounter, int[] operationCodes) {
        cpu.setRegisterValue(CPU.Register.INSTRUCTION_COUNTER, instructionCounter);
        for (int counter = 0; counter < operationCodes.length; ++counter) {
            memory.set(instructionCounter + counter, operationCodes[counter]);
            cpu.execute(memory).ifPresent(this::handleOperation);
        }
    }

    public Memory getMemory() {
        return memory;
    }

    public CPU getCPU() {
        return cpu;
    }

    /**
     * <p>
     * Handles the current operation
     * </p>
     *
     * @param operation The operation to execute
     */
    private int handleOperation(OperationCode operation) {
        int memoryLocation = cpu.getRegisterValue(CPU.Register.OPERAND);
        final int memoryLocationValue = memory.get(memoryLocation);
        final CPU.Register accumulator = CPU.Register.ACCUMULATOR;
        final CPU.Register instructionCounter = CPU.Register.INSTRUCTION_COUNTER;
        int accumulatorValue = cpu.getRegisterValue(accumulator);
        switch (operation) {
            case NEW_LINE:
                display.show(Display.NEW_LINE);
                break;
            case READ:
                display.show("%s %s ", Messages.READ, Decorator.INPUT);
                int value = Integer.parseInt(userInput.next());
                checkOverflow(value);
                memory.set(memoryLocation, value);
                break;
            case WRITE:
                display.show(String.valueOf(memoryLocationValue) + Display.NEW_LINE);
                break;
            case READ_STRING:
                display.show("%s %s ", Messages.READ_STRING, Decorator.INPUT);
                String input = userInput.next();
                memory.set(memoryLocation, input.length());

                for (int i = 0; i < input.length(); ++i)
                    memory.set(--memoryLocation, input.charAt(i));
                break;
            case WRITE_STRING:
                StringBuilder output = new StringBuilder(memoryLocationValue);
                for (int i = 0; i < memoryLocationValue; ++i)
                    output.append((char) memory.get(--memoryLocation));

                display.show(output.toString() + Display.NEW_LINE);
                break;
            case LOAD:
                cpu.setRegisterValue(accumulator, memoryLocationValue);
                break;
            case STORE:
                memory.set(memoryLocation, accumulatorValue);
                break;
            case ADD:
                cpu.setRegisterValue(accumulator, accumulatorValue += memoryLocationValue);
                checkOverflow(accumulatorValue);
                break;
            case SUBTRACT:
                cpu.setRegisterValue(accumulator, accumulatorValue -= memoryLocationValue);
                checkOverflow(accumulatorValue);
                break;
            case DIVIDE:
                if (memoryLocationValue == 0)
                    showError(Display.Messages.DIVIDE_BY_ZERO);

                cpu.setRegisterValue(accumulator, accumulatorValue / memoryLocationValue);
                break;
            case MULTIPLY:
                cpu.setRegisterValue(accumulator, accumulatorValue * memoryLocationValue);
                checkOverflow(accumulatorValue);
                break;
            case REMAINDER:
                cpu.setRegisterValue(accumulator, accumulatorValue % memoryLocationValue);
                break;
            case EXPONENT:
                cpu.setRegisterValue(accumulator, (int) Math.pow(accumulatorValue, memoryLocationValue));
                checkOverflow(accumulatorValue);
                break;
            case BRANCH:
                cpu.setRegisterValue(instructionCounter, memoryLocation);
                break;
            case BRANCHNEG:
                if (accumulatorValue < 0)
                    cpu.setRegisterValue(instructionCounter, memoryLocation);
                break;
            case BRANCHZERO:
                if (accumulatorValue == 0)
                    cpu.setRegisterValue(instructionCounter, memoryLocation);
                break;
            case HALT:
                displayMessage(Display.Messages.HALT);
                dump();
                return 0;
            default:
                throw new AssertionError(operation.name());
        }
        return memoryLocationValue / 10 + 1;
    }

    private void checkOverflow(int value) {
        if (value > MAX_WORD || value < MIN_WORD)
            showError(Display.Messages.MEMORY_OVERFLOW);
    }

    private void displayMessage(String message) {
        display.line("%s %s %s", Decorator.MESSAGE, message, Decorator.MESSAGE);
    }

    public void showError(String message) {
        displayMessage(Display.Messages.FATAL_ERROR);
        displayMessage(message);
        dump();
        System.exit(1);
    }

    public void dump() {
        // print the registers
        display.show(Display.NEW_LINE + "REGISTERS:");
        Arrays.stream(cpu.toString().split("\n")).forEach(line -> display.show(Display.NEW_LINE + "\t" + line));

        // print the memory
        final int DIVIDER = 15;
        display.show("%sMEMORY:%s\t%3s", Display.NEW_LINE, Display.NEW_LINE, " ");
        for (int i = 0; i < DIVIDER; i++)
            display.show("%7d", i);

        for (int i = 0; i < MEMORY_SIZE; ++i) {
            if (i % DIVIDER == 0)
                display.show("%s\t%3d", Display.NEW_LINE, i);
            display.show("%2s%05d", (memory.get(i) >= 0) ? " +" : " -", Math.abs(memory.get(i)));
        }
    }
}
