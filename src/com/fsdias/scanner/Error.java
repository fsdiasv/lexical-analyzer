package com.fsdias.scanner;

/**
 * Created by fsdias on 07/06/17.
 */
public class Error {
    private String value;
    private int line;
    private int column;

    /**
     * Construtor padrão da classe Error
     * @param value
     * @param line
     * @param column
     */
    public Error(String value, int line, int column) {
        this.value = value;
        this.line = line;
        this.column = column;
    }

    public String getValue() {
        return value;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }
}
