package com.jeffreyorazulike.simpletron.translator;

import com.jeffreyorazulike.simpletron.translator.SymbolTable.TableEntry.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Stack;

/**
 * <p>
 * Used to store constants, variable and their locations being used by the
 * {@link com.jeffreyorazulike.simpletron.translator.Translator}
 * </p>
 */
public final class SymbolTable {

    /**
     * the next location to store value of a constant or a variable
     */
    int dataCounter;
    static final int NOT_PRESENT = -1;

    private final Map<TableEntry, Integer> symbolTable = new HashMap<>();
    private final Stack<ForEntry> forEntry = new Stack<>();
    private final Map<Integer, Integer> constants = new HashMap<>();

    SymbolTable(int dataCounter) {
        this.dataCounter = dataCounter;
    }

    /**
     * <p>
     * Gets the location of the specified symbol if it exists or
     * {@link #NOT_PRESENT} if not
     * </p>
     *
     * @param symbol the symbol to check
     * @param type   the type the symbol represents
     *
     * @return the location it references in {@code Simpletron} memory or
     *         {@link #NOT_PRESENT} if it doesn't exist
     */
    int getLocation(int symbol, Type type) {
        return symbolTable.getOrDefault(new TableEntry(symbol, type), NOT_PRESENT);
    }

    /**
     * <p>
     * Gets the location of the specified symbol if it exists or else adds the
     * symbol and type to the table and returns the location
     * </p>
     *
     * @param symbol the symbol to check
     * @param type   the type the symbol represents
     *
     * @return the location it references in {@code Simpletron} memory
     */
    int addSymbol(int symbol, Type type) {
        return symbolTable.computeIfAbsent(new TableEntry(symbol, type), key -> --dataCounter);
    }

    /**
     * <p>
     * Changes the value at a specified location
     * </p>
     *
     * @param symbol   the symbol to add
     * @param location the location
     *
     */
    void addSymbolAt(int location, int symbol) {
        constants.put(location, symbol);
    }

    /**
     * <p>
     * Gets the location of the specified symbol if it exists or else adds the
     * line to the table
     * </p>
     *
     * @param line     the line to add
     * @param location the location that the symbol should be set at if it
     *                 doesn't exist
     *
     * @return the location it references in {@code Simpletron} memory
     */
    int addLine(int line, int location) {
        return symbolTable.computeIfAbsent(new TableEntry(line, Type.L), key -> location);
    }

    void addForEntry(ForEntry forEntry) {
        this.forEntry.push(forEntry);
    }

    Optional<ForEntry> popForEntry() {
        return forEntry.isEmpty() ? Optional.empty() : Optional.of(forEntry.pop());
    }

    Optional<ForEntry> peekForEntry() {
        return forEntry.isEmpty() ? Optional.empty() : Optional.of(forEntry.peek());
    }

    Map<Integer, Integer> getLocationAndSymbol(final Type type) {
        Map<Integer, Integer> finalMap = new HashMap<>();
        symbolTable.entrySet().stream().filter(entry -> entry.getKey().type == type)
                .forEach(map -> finalMap.put(map.getValue(), map.getKey().symbol));

        if (type == Type.C)
            finalMap.putAll(constants);

        return finalMap;
    }

    /**
     * T {@code TableEntry} represents an entry in the symbol table
     */
    static final class TableEntry {

        /**
         * <p>
         * Represents the type of entries
         * </p>
         */
        enum Type {
            /**
             * <p>
             * Represents {@code constants}
             * </p>
             */
            C,
            /**
             * <p>
             * Represents {@code line number}
             * </p>
             */
            L,
            /**
             * <p>
             * Represents {@code a character}
             * </p>
             */
            S,
            /**
             * <p>
             * Represents {@code variables}
             * </p>
             */
            V
        }

        /**
         * <p>
         * {@code symbol} contains the unicode representation of a variable,
         * line number or constant
         * </p>
         */
        int symbol;
        /**
         * <p>
         * {@code type} is the type of symbol being represented by this entry
         * </p>
         */
        Type type;

        /**
         * @param symbol
         * @param type
         */
        public TableEntry(int symbol, Type type) {
            this.symbol = symbol;
            this.type = type;
        }

        @Override
        public int hashCode() {
            return Objects.hash(symbol, type);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof TableEntry) {
                TableEntry tableEntry = (TableEntry) obj;
                if (symbol == tableEntry.symbol && type == tableEntry.type)
                    return true;
            }

            return false;
        }

        @Override
        public String toString() {
            return String.format("%d %s ", symbol, type.name());
        }
    }

    /**
     * ForEntry
     */
    static final class ForEntry {

        final char variable;
        final String end;
        final String step;

        /**
         * <p>
         * The line to jump back to if the conditional statement still holds
         * true
         * </p>
         */
        int returnTo;

        /**
         * @param variable the control variable
         * @param end      the location for the max\min value the control
         *                 variable can reach
         * @param step     the step for each iteration
         */
        public ForEntry(char variable, String end, String step) {
            this.end = end;
            this.step = step;
            this.variable = variable;
        }

    }
}
