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
package com.jeffreyorazulike.simpletron.cpu;

import com.jeffreyorazulike.simpletron.memory.Memory;
import java.lang.ref.Cleaner.Cleanable;
import java.util.Arrays;
import java.util.Optional;

/**
 *
 * @author Jeffrey Orazulike
 * @apiNote An interface defining the way the CPU for
 * {@link com.jeffreyorazulike.simpletron.Simpletron} system should be designed
 */
public interface CPU extends Cleanable {

    /**
     * Defines a register in the CPU
     */
    enum Register {
        /**
         * <p>
         * Holds the calculation the CPU is currently processing
         * </p>
         */
        ACCUMULATOR(true),
        /**
         * <p>
         * Holds the operation the CPU is to perform
         * </p>
         */
        OPERATION_CODE(false),
        /**
         * <p>
         * Holds the value the CPU is to use the Operation Code and work on
         * </p>
         */
        OPERAND(false),
        /**
         * <p>
         * Holds the current memory location the CPU should get what to work on
         * </p>
         */
        INSTRUCTION_COUNTER(false),
        /**
         * <p>
         * Holds the value of the memory location gotten from the Instruction
         * Counter to prevent unnecessary reading from the memory
         * </p>
         */
        INSTRUCTION_REGISTER(true);

        protected final boolean isMemory;

        private Register(boolean isMemory) {
            this.isMemory = isMemory;
        }
    }

    /**
     * Defines operations that can be executed by the CPU
     */
    enum OperationCode {
        // Input/output operations:
        /*
         * <p>Outputs a new line on Simpletron</p>
         */
        NEW_LINE(1),
        /**
         * <p>
         * Read a word from the keyboard into a specific location in memory.
         * </p>
         */
        READ(10),
        /**
         * <p>
         * Write a word from a specific location in memory to the screen.
         * </p>
         */
        WRITE(11),
        /**
         * <p>
         * Read a string from the keyboard and store it in memory.
         * </p>
         */
        READ_STRING(12),
        /**
         * <p>
         * Write a string from a specific location in memory to the screen.
         * </p>
         */
        WRITE_STRING(13),
        // Load/store operations:
        /**
         * <p>
         * Load a word from a specific location in memory into the accumulator.
         * </p>
         */
        LOAD(20),
        /**
         * <p>
         * Store a word from the accumulator into a specific location in memory.
         * </p>
         */
        STORE(21),
        // Arithmetic operations: These operations leave their result in the accumulator
        /**
         * <p>
         * Add a word from a specific location in memory to the word in the
         * accumulator
         * </p>
         */
        ADD(30),
        /**
         * <p>
         * Subtract a word from a specific location in memory from the word in
         * the accumulator
         * </p>
         */
        SUBTRACT(31),
        /**
         * <p>
         * Divide a word from a specific location in memory from the word in the
         * accumulator
         * </p>
         */
        DIVIDE(32),
        /**
         * <p>
         * Multiply a word from a specific location in memory by the word in the
         * accumulator
         * </p>
         */
        MULTIPLY(33),
        /**
         * <p>
         * Finds the remainder when dividing the value in the accumulator by a
         * word
         * </p>
         */
        REMAINDER(34),
        /**
         * <p>
         * Finds the accumulator raised to the power of the word in the specific
         * location
         * </p>
         */
        EXPONENT(35),
        // Transfer-of-control operations:
        /**
         * <p>
         * Branch to a specific location in memory
         * </p>
         */
        BRANCH(40),
        /**
         * <p>
         * Branch to a specific location in memory if the accumulator is
         * negative
         * </p>
         */
        BRANCHNEG(41),
        /**
         * <p>
         * Branch to a specific location in memory if the accumulator is zero
         * </p>
         */
        BRANCHZERO(42),
        /**
         * <p>
         * Halt. The program has completed its task
         * </p>
         */
        HALT(43);

        private final int value;

        private OperationCode(int value) {
            this.value = value;
        }

        public int getCode() {
            return value;
        }

        @Override
        public String toString() {
            return name().toLowerCase().concat(" " + getCode());
        }

    }

    /**
     *
     * @param register the register to get
     *
     * @return the value of the register
     */
    int getRegisterValue(Register register);

    /**
     *
     * @param register the register to change
     * @param value    the new value for the register
     */
    void setRegisterValue(Register register, int value);

    /**
     * Updates the CPU Registers and updates the instruction counter to the next
     * memory location to be read
     * <br>
     * An optional with a value is returned if the value in the operation code
     * register is valid, if not a empty optional is returned
     *
     * @param memory A reference to the memory
     *
     * @return An optional containing the operation code
     */
    Optional<OperationCode> execute(Memory memory);

    @Override
    default void clean() {
        Arrays.stream(Register.values()).forEach(register -> setRegisterValue(register, 0));
    }

    /**
     *
     * @param register the register to print
     *
     * @return the string representation of the register
     */
    String toString(Register register);
}
