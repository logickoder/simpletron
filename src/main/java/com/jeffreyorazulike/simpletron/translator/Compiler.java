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
package com.jeffreyorazulike.simpletron.translator;

import com.jeffreyorazulike.simpletron.Simpletron;
import com.jeffreyorazulike.simpletron.cpu.CPU;
import com.jeffreyorazulike.simpletron.display.Display;
import com.jeffreyorazulike.simpletron.input.FileInput;
import com.jeffreyorazulike.simpletron.input.Input;
import com.jeffreyorazulike.simpletron.translator.SymbolTable.TableEntry.Type;
import java.io.IOException;
import java.util.Optional;

/**
 *
 * @author Jeffrey Orazulike
 * @apiNote The compiler translates a {@code Simpletron Basic Language} program
 * to the {@code Simpletron Machine Language}
 */
public class Compiler extends Translator {

    private final int[] flags;

    public Compiler(Simpletron simpletron) {
        super(simpletron);
        flags = new int[sml.length];
    }

    @Override
    public void run(String file, Optional<Display> output)
            throws IOException, SyntaxError {
        Input input = new FileInput(file);
        instructionCounter = 0;

        symbolTable = new SymbolTable(sml.length);
        for (int i = 0; i < sml.length; ++i) {
            sml[i] = 0;
            flags[i] = SymbolTable.NOT_PRESENT;
        }

        while (true) {
            StringBuilder line = new StringBuilder();
            do {
                input.next();
                if (input.isEOL() || input.isEOF())
                    break;
                line.append(input.last()).append(" ");
            } while (true);

            handleLine(line.toString().trim());

            if (input.isEOF()) {
                sml[instructionCounter++] = Simpletron.END_SEGMENT;
                break;
            }
        }

        for (int i = 0; i < flags.length; ++i)
            if (flags[i] != SymbolTable.NOT_PRESENT)
                sml[i] += symbolTable.getLocation(flags[i], Type.L);

        output.ifPresent(out -> {
            symbolTable.getLocationAndSymbol(Type.C)
                    .forEach((key, value) -> out.line(key + Display.Decorator.INSTRUCTION.toString() + value));
            out.line(Display.Decorator.INSTRUCTION.toString());
            for (int i : sml) {
                if (i == 0)
                    break;

                out.line(String.valueOf(i));
            }
        });
    }

    @Override
    protected void handleGoto(String line, int lineIndex) throws SyntaxError {
        handleGoto(line, lineIndex, tokens -> {
            // get the location to go to
            int gotoLocation = symbolTable.getLocation(Integer.parseInt(tokens[1]), Type.L);

            // find out if the location is absent
            boolean isLocationAbsent = gotoLocation == SymbolTable.NOT_PRESENT;

            int location = (isLocationAbsent) ? 0 : gotoLocation;
            sml[instructionCounter] = getInstruction(CPU.OperationCode.BRANCH, location);
            flags[instructionCounter] = (isLocationAbsent) ? gotoLocation : flags[instructionCounter];
            ++instructionCounter;
        });
    }

    @Override
    protected void handleIf(String line, int lineIndex) throws SyntaxError {
        handleIf(line, lineIndex, (operator, gotoLocation) -> {

            if (gotoLocation == null)
                return;

            int location = symbolTable.getLocation(gotoLocation, Type.L);
            boolean isLocationAbsent = location == SymbolTable.NOT_PRESENT;
            location = (isLocationAbsent) ? 0 : location;

            switch (operator) {
                case "==":
                    sml[instructionCounter] = getInstruction(CPU.OperationCode.BRANCHZERO, location);
                    flags[instructionCounter] = (isLocationAbsent) ? gotoLocation : flags[instructionCounter];
                    break;
                case ">=":
                case "<=":
                    sml[instructionCounter] = getInstruction(CPU.OperationCode.BRANCHZERO, location);
                    flags[instructionCounter] = (isLocationAbsent) ? gotoLocation : flags[instructionCounter];
                    ++instructionCounter;
                case ">":
                case "<":
                    sml[instructionCounter] = getInstruction(CPU.OperationCode.BRANCHNEG, location);
                    flags[instructionCounter] = (isLocationAbsent) ? gotoLocation : flags[instructionCounter];
            }
            ++instructionCounter;
        });

    }

    @Override
    protected void handleEnd() {
        sml[instructionCounter++] = getInstruction(CPU.OperationCode.HALT, 0);
    }
}
