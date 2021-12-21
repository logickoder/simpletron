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
import com.jeffreyorazulike.simpletron.calculator.InfixPostfix;
import com.jeffreyorazulike.simpletron.cpu.CPU;
import com.jeffreyorazulike.simpletron.display.Display;
import com.jeffreyorazulike.simpletron.memory.Memory;
import com.jeffreyorazulike.simpletron.translator.SymbolTable.ForEntry;
import com.jeffreyorazulike.simpletron.translator.SymbolTable.TableEntry.Type;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.Stack;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Jeffrey Orazulike
 * @apiNote The translator converts a program to a language understood by the
 * computer so the computer can be able to run it
 */
public abstract class Translator {

    /**
     * @apiNote The list of commands supported by the translator
     */
    enum Commands {
        /**
         * Any text following the command {@link #REM} is for documentation
         * purposes only and is ignored by the
         * compiler.<br>{@code 50 rem this is a remark}
         */
        REM,
        /**
         * Display a question mark to prompt the user to enter an integer. Read
         * that integer from the keyboard and store the integer in x and store
         * the next input from the user in v.<br>{@code 30 input x,v}
         */
        INPUT,
        /**
         * Assign u the value of 4 * (j - 56). Note that an arbitrarily complex
         * expression can appear to the right of the equal
         * sign.<br>{@code 80 let u = 4 * (j - 56)}
         */
        LET,
        /**
         * Transfer program control to line 45.<br>{@code 70 goto 45}
         */
        GOTO,
        /**
         * Display the value of w and v.<br>{@code 10 print w,v}
         */
        PRINT,
        /**
         * Compare i and z for equality and transfer program control to line 80
         * if the condition is true; otherwise, continue execution with the next
         * statement.<br>{@code 35 if i == z goto 80}
         */
        IF,
        /**
         * Run the commands within the for and next statement for a certain
         * amount of steps<br>{@code 40 for x = 2 to 10 step 2}
         */
        FOR,
        /**
         * Jumps to the next iteration of a for loop.<br>{@code 70 next}
         */
        NEXT,
        /**
         * Terminate program execution.<br> {@code 99 end}
         */
        END
    }

    /**
     * The list of commands to be run
     */
    protected final int[] sml;
    /**
     * The amount of blocks the computer's memory has
     */
    protected final int MEMORY_LENGTH;
    /**
     * The next place to place the next command in the sml array
     */
    protected int instructionCounter;
    /**
     * Holds a bunch of symbols used by both translator during the translating
     * of the program
     */
    protected SymbolTable symbolTable;
    /**
     * A bunch of commands to execute before translating the next line
     */
    protected Stack<Consumer<Integer>> performLater;
    /**
     * A direct link to the computer
     */
    protected Simpletron simpletron;

    /**
     *
     * @param simpletron Creates a translator instance with a direct link to the
     *                   simpletron system
     */
    public Translator(Simpletron simpletron) {
        this.simpletron = simpletron;
        sml = new int[simpletron.getMemory().getConstant(Memory.Constant.MEMORY_SIZE)];
        MEMORY_LENGTH = (int) Math.log10(sml.length) - 1;
        performLater = new Stack<>();
    }

    protected final int getInstruction(CPU.OperationCode operationCode, int location) {
        return operationCode.getCode() * sml.length + location;
    }

    /**
     *
     * @param file   the file to compile
     * @param output the place to output the translated commands, it will only
     *               be useful if the commands are to be compiled
     *
     *
     * @throws IOException if an error occurred
     */
    public abstract void run(String file, Optional<Display> output) throws IOException;

    /**
     *
     * @param constant the constant to add
     *
     * @return the location the constant was placed
     */
    protected int addConstant(int constant) {
        return symbolTable.addSymbol(constant, Type.C);
    }

    /**
     *
     * @param location the location in the memory the constant should be placed
     *                 during the program's execution
     * @param constant the constant to place
     */
    protected void addConstantAt(int location, int constant) {
        symbolTable.addSymbolAt(location, constant);
    }

    /**
     *
     * @param line the line to handle
     */
    protected void handleLine(String line) {
        final String[] tokens = line.split("\\s+");
        final int lineNumber = Integer.parseInt(tokens[0]);

        while (!performLater.isEmpty()) // executes all the actions to be performed
            performLater.pop().accept(lineNumber);

        symbolTable.addLine(lineNumber, instructionCounter); // adds the line number to the symbol table

        Arrays.stream(Commands.values()).filter(command -> command.name().toLowerCase().equalsIgnoreCase(tokens[1]))
                .findAny().ifPresentOrElse(command -> {
                    switch (command) {
                        case END:
                            handleEnd();
                            return;
                        case GOTO:
                            handleGoto(line, lineNumber);
                            return;
                        case IF:
                            handleIf(line, lineNumber);
                            return;
                        case INPUT:
                            handleInput(line, lineNumber);
                            return;
                        case LET:
                            handleLet(line, lineNumber);
                            return;
                        case PRINT:
                            handlePrint(line, lineNumber);
                            return;
                        case REM:
                            return;
                        case FOR:
                            handleFor(line, lineNumber);
                            break;
                        case NEXT:
                            handleNext(lineNumber);
                            break;
                        default:
                            break;
                    }
                    // adds a let to the front of a statement if it doesn't have any command
                }, () -> handleLet("let " + line.substring(tokens[0].length()), lineNumber));
    }

    abstract void handleIf(String line, int lineNumber);

    void handleIf(String line, int lineNumber, BiConsumer<String, Integer> consumer) {
        // get the operator and store it
        Matcher operatorMatcher = Pattern.compile("==|<=|>=|<|>").matcher(line);
        Matcher matcher = Pattern.compile("if\\s+([a-z]|[0-9]+)\\s*(" + operatorMatcher.pattern().pattern()
                + ")\\s*([a-z]|[0-9]+)\\s+goto\\s+\\d+", Pattern.CASE_INSENSITIVE).matcher(line);
        throwSyntaxError("if", "35 if i == z goto 80", lineNumber, !matcher.find());
        operatorMatcher.find();

        String operator = operatorMatcher.group();

        // remove the operator from the main string
        String tokens[] = matcher.group().toLowerCase().replaceAll(operatorMatcher.pattern().pattern(), " ")
                .split("\\s+");
        int i = 0;

        char c = tokens[++i].charAt(0);
        int locationX = Character.isDigit(c) ? addConstant(Integer.parseInt(tokens[i])) : symbolTable.addSymbol(c, Type.V);

        c = tokens[++i].charAt(0);
        int locationY = Character.isDigit(c) ? addConstant(Integer.parseInt(tokens[i])) : symbolTable.addSymbol(c, Type.V);

        BiConsumer<Integer, Integer> subtract = (x, y) -> {
            sml[instructionCounter++] = getInstruction(CPU.OperationCode.LOAD, x);
            sml[instructionCounter++] = getInstruction(CPU.OperationCode.SUBTRACT, y);
        };

        switch (operator) {
            case "==":
            case "<":
            case "<=":
                subtract.accept(locationX, locationY);
                break;
            case ">":
            case ">=":
                subtract.accept(locationY, locationX);
        }
        consumer.accept(operator, Integer.parseInt(tokens[i + 2]));
    }

    abstract void handleGoto(String line, int lineNumber);

    void handleGoto(String line, int lineNumber, Consumer<String[]> consumer) {
        Pattern pattern = Pattern.compile("goto\\s+\\d+", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(line);
        throwSyntaxError("goto", "70 goto 45", lineNumber, !matcher.find());
        consumer.accept(matcher.group().split("\\s+"));
    }

    void handlePrint(String line, int lineNumber) {
        Pattern pattern = Pattern.compile("print\\s+(\\d+|(\\${0,1}[a-z]{1}))(\\s*,\\s*(\\d+|(\\${0,1}[a-z]{1}))\\s*)*",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(line);
        throwSyntaxError("print", "10 print 10,$w,v", lineNumber, !matcher.find());

        String variables[] = matcher.group().toLowerCase().split("[,\\s+]");
        char c;
        for (int i = 1; i < variables.length; ++i) {
            if (variables[i].isBlank())
                continue;
            c = variables[i].charAt(0);

            if (c == '$')
                sml[instructionCounter++] = getInstruction(CPU.OperationCode.WRITE_STRING,
                        symbolTable.addSymbol(variables[i].charAt(1), Type.V));
            else if (Character.isAlphabetic(c))
                sml[instructionCounter++] = getInstruction(CPU.OperationCode.WRITE, symbolTable.addSymbol(c, Type.V));
            else if (Character.isDigit(c))
                sml[instructionCounter++] = getInstruction(CPU.OperationCode.WRITE,
                        addConstant(Integer.parseInt(variables[i])));
        }
    }

    void handleInput(String line, int lineNumber) {
        Pattern pattern = Pattern.compile("input\\s+(\\${0,1}[a-z]{1})(\\s*,\\s*(\\${0,1}[a-z]{1})\\s*)*",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(line);
        throwSyntaxError("input", "30 input $x,v", lineNumber, !matcher.find());

        String variables[] = matcher.group().toLowerCase().split("[,\\s+]");
        char c;
        for (int i = 1; i < variables.length; ++i) {
            if (variables[i].isBlank())
                continue;

            c = variables[i].charAt(0);
            if (c == '$')
                sml[instructionCounter++] = getInstruction(CPU.OperationCode.READ_STRING,
                        symbolTable.addSymbol(variables[i].charAt(1), Type.V));
            else
                sml[instructionCounter++] = getInstruction(CPU.OperationCode.READ, symbolTable.addSymbol(c, Type.V));
        }

    }

    void handleLet(String line, int lineNumber) {
        Pattern pattern = Pattern.compile("let\\s+\\${0,1}[a-z]\\s*=\\s*", Pattern.CASE_INSENSITIVE);

        Matcher matcher = pattern.matcher(line);
        throwSyntaxError("let", "80 let u = 4 * (j - 56)", lineNumber, !matcher.find());
        StringBuilder calculation = new StringBuilder(line.substring(matcher.end(), line.length()).trim());

        char variable = matcher.group().split("\\s+")[1].charAt(0);
        if (variable == '$') {
            variable = matcher.group().split("\\s+")[1].charAt(1);

            throwSyntaxError("let", "80 let $u = \"Hello World\"", lineNumber,
                    !(calculation.toString().startsWith("\"") && calculation.toString().endsWith("\"")));

            int length = calculation.length() - 1;
            addConstantAt(symbolTable.addSymbol(variable, Type.V), length - 1);

            for (int i = 1; i < length; ++i)
                addConstantAt(--symbolTable.dataCounter, calculation.charAt(i));

            return;
        }

        calculation = new StringBuilder(calculation.toString().toLowerCase());

        String[] variables = calculation.toString().split("\\W+");
        calculation.append(variables.length == 1 ? " + 0" : "");
        calculation.insert(0, calculation.toString().startsWith("-") ? "0 " : "");

        final int ONE = addConstant(1);
        final int ZERO = addConstant(0);

        char c;
        for (int i = 0; i < calculation.length(); ++i) {
            c = calculation.charAt(i);
            if (InfixPostfix.isOperator(c)) {
                int value = (c == InfixPostfix.MULTIPLICATION || c == InfixPostfix.DIVISION) ? ONE : ZERO;
                for (; i < calculation.length(); ++i)
                    if (InfixPostfix.isOperator(calculation.charAt(++i)))
                        calculation.insert(i, String.format("%d", value));
                    else
                        break;
                --i;
            }
        }

        InfixPostfix.CalculateInterface translatorCalculate = (stack, operator, count) -> {
            // load y
            int y = stack.pop();
            if (count++ == 0)
                sml[instructionCounter++] = getInstruction(CPU.OperationCode.LOAD, stack.pop());
            switch (operator) {
                case InfixPostfix.PLUS:
                    sml[instructionCounter++] = getInstruction(CPU.OperationCode.ADD, y);
                    break;
                case InfixPostfix.MINUS:
                    sml[instructionCounter++] = getInstruction(CPU.OperationCode.SUBTRACT, y);
                    break;
                case InfixPostfix.MULTIPLICATION:
                    sml[instructionCounter++] = getInstruction(CPU.OperationCode.MULTIPLY, y);
                    break;
                case InfixPostfix.DIVISION:
                    sml[instructionCounter++] = getInstruction(CPU.OperationCode.DIVIDE, y);
                    break;
                case InfixPostfix.REMAINDER:
                    sml[instructionCounter++] = getInstruction(CPU.OperationCode.REMAINDER, y);
                    break;
                case InfixPostfix.EXPONENTATION:
                    sml[instructionCounter++] = getInstruction(CPU.OperationCode.EXPONENT, y);
                    break;
            }
            return count;
        };

        InfixPostfix.postfixEvaluator(InfixPostfix.infixToPostfix(calculation.toString()), translatorCalculate,
                number -> addConstant(number), v -> symbolTable.addSymbol(v, Type.V));

        // store it in the variable in the left hand side of the equation
        sml[instructionCounter++] = getInstruction(CPU.OperationCode.STORE, symbolTable.addSymbol(variable, Type.V));
    }

    void handleFor(String line, int lineNumber) {
        // match the step
        Matcher stepMatcher = Pattern.compile("step\\s+(\\d+|[a-z])").matcher(line);
        // adds a default step of 1 if no step was found
        line = stepMatcher.find() ? line : line.concat(" step 1");

        // matches the complete for statement
        Matcher matcher = Pattern.compile(
                "for\\s+[a-z]\\s*=\\s*(\\d+|[a-z])\\s+to\\s+(\\d+|[a-z])\\s+".concat(stepMatcher.pattern().pattern())).matcher(line);
        throwSyntaxError("for", "40 for x = 2 to 10 step 2", lineNumber, !matcher.find());

        // splits the for statement to get the useful information
        String tokens[] = matcher.group().replace("=", " ").split("\\s+");
        char variable = tokens[1].charAt(0);

        handleLet(String.format("let %s = %s", variable, tokens[2]), lineNumber);
        // stores the data to be used by the corresponding {@code next} statement
        ForEntry forEntry = new ForEntry(variable, tokens[4], tokens[6]);
        symbolTable.addForEntry(forEntry);
        performLater.push(lineNo -> forEntry.returnTo = lineNo);
    }

    void handleNext(int lineNumber) {
        symbolTable.peekForEntry().ifPresent(forEntry -> {
            handleLet(String.format("let %c = %c + %s", forEntry.variable, forEntry.variable, forEntry.step),
                    lineNumber);
            handleIf(String.format("if %c %s %s goto %d", forEntry.variable, "<=", forEntry.end, forEntry.returnTo),
                    lineNumber);
        });
        performLater.push(lineNo -> {
            if (lineNo > lineNumber)
                symbolTable.popForEntry();
        });
    }

    abstract void handleEnd();

    private void throwSyntaxError(String statement, String message, int lineNumber, boolean test) {
        throwSyntaxError(String.format("Your %s statement is incorrect, it should look like - %s", statement, message),
                lineNumber, test);
    }

    private void throwSyntaxError(String message, int lineNumber, boolean test) {
        if (test)
            throw new SyntaxError(lineNumber, message);
    }
}
