package com.jeffreyorazulike.simpletron.translator;

/**
 * SyntaxError
 */
public class SyntaxError extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 7838378171908019726L;

    public SyntaxError() {
        this("Your code isn't well written");
    }

    public SyntaxError(String message) {
        super(message);
    }

    public SyntaxError(int line, String message) {
        this(String.format("Syntax Error on line %02d, %s.", line, message));
    }
}