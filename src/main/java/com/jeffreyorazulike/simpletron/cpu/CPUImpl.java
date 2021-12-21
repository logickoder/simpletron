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
import java.util.Arrays;
import java.util.Optional;

/**
 *
 * @author Jeffrey Orazulike
 */
public class CPUImpl implements CPU {

    private int operand;
    private int accumulator;
    private int operationCode;
    private int instructionCounter;
    private int instructionRegister;

    @Override
    public Optional<OperationCode> execute(Memory memory) {
        int seperator = memory.getConstant(Memory.Constant.MEMORY_SIZE);
        instructionRegister = memory.get(instructionCounter++);
        operationCode = instructionRegister / seperator;
        operand = instructionRegister % seperator;
        return Arrays.stream(OperationCode.values()).filter(oc -> oc.getCode() == operationCode).findAny();
    }

    @Override
    public int getRegisterValue(Register register) {
        switch (register) {
            case ACCUMULATOR:
                return accumulator;
            case OPERATION_CODE:
                return operationCode;
            case OPERAND:
                return operand;
            case INSTRUCTION_COUNTER:
                return instructionCounter;
            case INSTRUCTION_REGISTER:
                return instructionRegister;
            default:
                throw new AssertionError(register.name());
        }
    }

    @Override
    public void setRegisterValue(Register register, int value) {
        switch (register) {
            case ACCUMULATOR:
                accumulator = value;
                break;
            case OPERATION_CODE:
                operationCode = value;
                break;
            case OPERAND:
                operand = value;
                break;
            case INSTRUCTION_COUNTER:
                instructionCounter = value;
                break;
            case INSTRUCTION_REGISTER:
                instructionRegister = value;
                break;
            default:
                throw new AssertionError(register.name());
        }
    }

    @Override
    public String toString(Register register) {
        int value = getRegisterValue(register);
        return String.format("%-20s%8s", register.name().toLowerCase(),
                (register.isMemory) ? String.format("%s%05d", (value >= 0) ? "+" : "-", Math.abs(value))
                        : String.format("%03d", value));
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        Arrays.stream(Register.values()).forEach(register -> builder.append(toString(register)).append("\n"));
        return builder.toString();
    }
}
