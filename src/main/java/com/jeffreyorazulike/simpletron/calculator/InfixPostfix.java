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
package com.jeffreyorazulike.simpletron.calculator;

import java.util.Stack;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 *
 * @author Jeffrey Orazulike
 * @apiNote {@code InfixPostfix} helps the
 * {@link com.jeffreyorazulike.simpletron.translator.Translator} translate a
 * mathematical expression to a simpletron machine readable code
 */
public final class InfixPostfix {

    public static final char SPACE = ' ';
    public static final char PLUS = '+';
    public static final char MINUS = '-';
    public static final char DIVISION = '/';
    public static final char REMAINDER = '%';
    public static final char EXPONENTATION = '^';
    public static final char MULTIPLICATION = '*';
    public static final char O_PARENTHESIS = '(';
    public static final char C_PARENTHESIS = ')';

    @FunctionalInterface
    public static interface CalculateInterface {

        int calculate(Stack<Integer> stack, char operator, int count);
    }

    public static final class ReturnedNumber {

        public int number;
        public int i;

        public ReturnedNumber(int number, int i) {
            this.number = number;
            this.i = i;
        }
    }

    public static final CalculateInterface defaultCalculate = (stack, operator, count) -> {
        int y = stack.pop();
        int x = stack.pop();

        Supplier<Integer> calculate = () -> {
            switch (operator) {
                case PLUS:
                    return x + y;
                case MINUS:
                    return x - y;
                case MULTIPLICATION:
                    return x * y;
                case DIVISION:
                    return x / y;
                case REMAINDER:
                    return x % y;
                case EXPONENTATION:
                    return (int) Math.pow(x, y);

                default:
                    return 0;
            }
        };

        stack.push(calculate.get());
        return ++count;
    };

    /**
     *
     * @param start  the pos tion to start looking for a number
     * @param string the string to search
     *
     * @return the number and the current index
     */
    public static ReturnedNumber getNumbers(int start, String string) {
        StringBuilder number = new StringBuilder(string.length());
        char c;
        for (; start < string.length(); ++start) {
            c = string.charAt(start);
            if (Character.isDigit(c))
                number.append(c);
            else
                break;
        }
        return new ReturnedNumber(Integer.parseInt(number.toString()), start == string.length() ? start : start - 1);
    }

    /**
     *
     * @param operator The operator to check
     *
     * @return A number signifying precedence
     */
    private static int precedence(char operator) {
        switch (operator) {
            case PLUS:
            case MINUS:
                return 1;
            case DIVISION:
            case REMAINDER:
            case MULTIPLICATION:
                return 2;
            case EXPONENTATION:
                return 3;

            default:
                return 0;
        }
    }

    public static boolean isOperator(char c) {
        switch (c) {
            case PLUS:
            case MINUS:
            case DIVISION:
            case REMAINDER:
            case EXPONENTATION:
            case MULTIPLICATION:
                return true;
        }

        return false;
    }

    public static boolean isSpecial(char c) {
        if (isOperator(c))
            return true;
        switch (c) {
            case O_PARENTHESIS:
            case C_PARENTHESIS:
                return true;
        }
        return false;
    }

    public static String infixToPostfix(String infix) {
        StringBuilder postfix = new StringBuilder(infix.length());
        Stack<Character> stack = new Stack<>();
        stack.push(O_PARENTHESIS);
        infix = infix.concat(String.valueOf(C_PARENTHESIS));

        char c;
        for (int i = 0; i < infix.length(); ++i) {

            c = infix.charAt(i);

            if (Character.isWhitespace(c))
                continue;

            if (Character.isDigit(c)) {
                ReturnedNumber returnedNumber = getNumbers(i, infix);
                i = returnedNumber.i;
                postfix.append(returnedNumber.number).append(SPACE);
            } else if (Character.isAlphabetic(c))
                postfix.append(c).append(SPACE);
            else if (c == O_PARENTHESIS)
                stack.push(c);
            else if (isOperator(c)) {
                while (!stack.isEmpty() && (precedence(stack.peek()) >= precedence(c)))
                    postfix.append(stack.pop()).append(SPACE);
                stack.push(c);
            } else if (c == C_PARENTHESIS) {
                while (stack.peek() != O_PARENTHESIS)
                    postfix.append(stack.pop()).append(SPACE);
                stack.pop();
            }
        }

        return postfix.toString();
    }

    public static int postfixEvaluator(String postfix, CalculateInterface calculate, Function<Integer, Integer> digitTransform, Function<Character, Integer> variableTransform) {
        if (postfix.isBlank())
            return 0;

        final Stack<Integer> stack = new Stack<>();
        final String[] variables = postfix.concat(" ").concat(String.valueOf(C_PARENTHESIS)).split("\\s+");
        int count = 0;

        char c;
        for (String variable : variables) {
            c = variable.charAt(0);

            if (Character.isDigit(c))
                stack.push(digitTransform.apply(Integer.parseInt(variable)));
            else if (Character.isAlphabetic(c))
                stack.push(variableTransform.apply(c));
            else if (isOperator(c))
                count = calculate.calculate(stack, c, count);
            else if (c == C_PARENTHESIS && !stack.isEmpty())
                return stack.pop();
        }

        return 0;
    }

}
