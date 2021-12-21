/*
 * Copyright (C) 2020 Jeffrey Orazulike
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Jeffrey Orazulike
 * @apiNote Runs a program immediately without compiling it, it's slower than a
 * compiled program but it is useful for testing
 */
public class Interpreter extends Translator {

    private int lineIndex;
    private List<String> lines;
    private Map<Integer, String> numberTolines;
    private Map<Integer, Integer> numberToIndex;

    public Interpreter(Simpletron simpletron) {
        super(simpletron);
    }

    @Override
    protected int addConstant(int constant) {
        int location = super.addConstant(constant);
        simpletron.getMemory().set(location, constant);
        return location;
    }

    @Override
    protected void addConstantAt(int location, int constant) {
        simpletron.getMemory().set(location, constant);
        super.addConstantAt(location, constant);
    }

    @Override
    public void run(String file, Optional<Display> output) throws IOException {
        simpletron.clean();
        Input input = new FileInput(file);
        instructionCounter = 0;
        lines = new ArrayList<>(30);

        numberToIndex = new HashMap<>();
        numberTolines = new HashMap<>();
        symbolTable = new SymbolTable(sml.length);
        for (int i = 0; i < sml.length; ++i)
            sml[i] = 0;

        do
            lines.add(input.nextLine().trim());
        while (!(input.isEOF() || input.isEOF()));

        int size = lines.size();
        Pattern pattern = Pattern.compile("\\w+");

        for (int i = 0; i < size; ++i) {
            Matcher matcher = pattern.matcher(lines.get(i));
            matcher.find();
            int number = Integer.parseInt(matcher.group());
            numberTolines.put(number, lines.get(i).substring(matcher.end()));
            numberToIndex.put(number, i);
        }

        for (; lineIndex < size; ++lineIndex) {
            instructionCounter = 0;
            handleLine(lines.get(lineIndex));
        }
    }

    @Override
    void handleIf(String line, int lineNumber) {
        int ic = instructionCounter;
        handleIf(line, lineIndex, (operator, gotoLocation) -> {
            simpletron.runCommands(ic, Arrays.copyOfRange(sml, ic, instructionCounter));
            int accumulator = simpletron.getCPU().getRegisterValue(CPU.Register.ACCUMULATOR);

            switch (operator) {
                case "==":
                    if (accumulator == 0)
                        handleGoto("goto " + gotoLocation, lineNumber);
                    break;
                case ">=":
                case "<=":
                    if (accumulator == 0)
                        handleGoto("goto " + gotoLocation, lineNumber);
                case ">":
                case "<":
                    if (accumulator < 0)
                        handleGoto("goto " + gotoLocation, lineNumber);
            }
        });
    }

    @Override
    void handleGoto(String line, int lineNumber) {
        handleGoto(line, lineIndex, tokens -> lineIndex = numberToIndex.get(Integer.parseInt(tokens[1])) - 1);
    }

    @Override
    void handlePrint(String line, int lineNumber) {
        int ic = instructionCounter;
        super.handlePrint(line, lineNumber);
        simpletron.runCommands(ic, Arrays.copyOfRange(sml, ic, instructionCounter));
    }

    @Override
    void handleInput(String line, int lineNumber) {
        int ic = instructionCounter;
        super.handleInput(line, lineNumber);
        simpletron.runCommands(ic, Arrays.copyOfRange(sml, ic, instructionCounter));
    }

    @Override
    void handleLet(String line, int lineNumber) {
        int ic = instructionCounter;
        super.handleLet(line, lineNumber);
        simpletron.runCommands(ic, Arrays.copyOfRange(sml, ic, instructionCounter));
    }

    @Override
    void handleEnd() {
    }
}
