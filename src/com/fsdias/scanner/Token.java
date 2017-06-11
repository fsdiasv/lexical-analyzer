package com.fsdias.scanner;

/**
 * Created by fsdias on 03/06/17.
 */
public class Token {
    private String type; // tipo do token
    private String value; // valor do token
    private int line; // linha no código onde se encontra o token
    private int column; // coluna no código onde se encontra o token
    private boolean flagPrintMode; // caso esta 'flag == true' o método de impressão de tokens imprimirá apenas o tipo do token <TIPO>, e não <TIPO, VALOR>

    /**
     * Construtor padrão para classe Token
     * @param type
     * @param value
     * @param line
     * @param column
     */
    public Token(String type, String value, int line, int column) {
        this.type = type;
        this.value = value;
        this.line = line;
        this.column = column;
        this.flagPrintMode = false;
    }

    /**
     * Método responsável por identificar o nome correto do tipo do token
     * @param lexem
     * @return
     */
    public static String tokenName(String lexem) {

        switch (lexem) {
            case "=": return "OP_ASSIGN";
            case "+=": return "OP_SUMASSIGN";
            case "&&": return "OP_COND";
            case "==": return "OP_EQUAL";
            case ">": return "OP_GREATER";
            case "<=": return "OP_LESSEQUAL";
            case "+": return "OP_ADD";
            case "-": return "OP_MINUS";
            case "*": return "OP_MULT";
            case "++": return "OP_INCREMENT";
            case "!": return "OP_NOT";
            case "--": return "OP_DECREMENT";
            case ",": return "SP_COMMA";
            case ".": return "SP_DOT";
            case ";": return "SP_SEMICOLON";
            case "(": return "SP_OPENPARENTHESES";
            case "{": return "SP_OPENBRACES";
            case "[": return "SP_OPENBRACKETS";
            case ")": return "SP_CLOSEPARENTHESES";
            case "}": return "SP_CLOSEBRACES";
            case "]": return "SP_CLOSEBRACKETS";
        }
        return "error";
    }

    public String getType() {
        return type;
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

    public void setFlagPrintMode(boolean flagPrintMode) {
        this.flagPrintMode = flagPrintMode;
    }

    public boolean isPrintMode() {
        return flagPrintMode;
    }
}
