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
package com.jeffreyorazulike.simpletron.display;

import java.util.Arrays;

/**
 *
 * @author Jeffrey Orazulike
 * @apiNote Defines the way a message is displayed on the simpletron system
 */
public interface Display {

    /**
     * <p>
     * The new line character supported by simpletron
     * </p>
     * b
     */
    public static final String NEW_LINE = "\n";

    /**
     * <p>
     * Defines how a display decorates each line
     * </p>
     */
    public enum Decorator {
        /**
         * <p>
         * {@code ***}The default string shown around messages
         * </p>
         */
        MESSAGE("***"),
        /**
         * <p>
         * {@code ?}The default string shown after a memory location to input an
         * instruction
         * </p>
         */
        INSTRUCTION("?"),
        /**
         * <p>
         * {@code >>}The default string shown during the execution of a
         * simpletron program when requesting for input
         * </p>
         */
        INPUT(">>"),
        /**
         * <p>
         * {@code }An empty string
         * </p>
         */
        NONE("");

        private final String decorator;

        private Decorator(String decorator) {
            this.decorator = decorator;
        }

        @Override
        public String toString() {
            return decorator;
        }
    }

    /**
     * <p>
     * Display the welcome screen
     * </p>
     */
    default void splashScreen() {
        Arrays.stream(Messages.WELCOME_MESSAGE.split("\n"))
                .forEach(line -> line("%s %-47s %s", Decorator.MESSAGE, line, Decorator.MESSAGE));
    }

    /**
     * <p>
     * Displays a message with formatting, works just like the
     * {@code String::format method}
     * </p>
     *
     * @param message The message to display
     * @param args    The strings to insert into the format specifers
     */
    public void show(String message, Object... args);

    /**
     * <p>
     * Displays a message with formatting, and ends it with a new line
     * </p>
     *
     * @param message The message to display
     * @param args    The strings to insert into the format specifers
     */
    default void line(String message, Object... args) {
        show(message + NEW_LINE, args);
    }

    /**
     * <p>
     * Close the display if it's using IO
     * </p>
     */
    default void close() {
    }

    public static class Messages {

        /**
         * <p>
         * The message that greets the user when they load simpletron
         * </p>
         */
        public static final String WELCOME_MESSAGE = "Welcome to Simpletron!" + NEW_LINE
                + "Please enter your program one instruction" + NEW_LINE + "(or data word) at a time. I will display"
                + NEW_LINE + "the location number and a question mark (?)" + NEW_LINE
                + "You then type the word for that location" + NEW_LINE + "but first, you will have to enter the"
                + NEW_LINE + "constants used by your program. Enter a" + NEW_LINE
                + "number referencing the memory location" + NEW_LINE + "followed by a non digit and the value to"
                + NEW_LINE + "be placed in the location. Type a single" + NEW_LINE
                + "digit/character to stop entering your contants." + NEW_LINE
                + "Type -99999 to stop entering your program.";

        public static final String CONSTANT_END = "Done entering constant.";
        public static final String DONE_LOAD = "Program Loadiing Complete" + NEW_LINE + "Program Execution Begins";

        public static final String READ = "Enter an integer";
        public static final String READ_STRING = "Enter an string";
        public static final String HALT = "Simpletron execution terminated";

        public static final String MEMORY_OVERFLOW = "Memory Overflow";
        public static final String DIVIDE_BY_ZERO = "Attempt to divide by zero";
        public static final String FATAL_ERROR = "Simpletron execution abnormally terminated";
    }
}
