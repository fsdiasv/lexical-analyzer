package com.fsdias.scanner;

import java.util.*;

/**
 * Created by fsdias on 06/06/17.
 */
public class LanguageScan {
    private Set<String> operators; // armazena todos os operadores válidos
    private Set<String> separators; // armazena todos os separadores válidos
    private Set<String> reservedWords; // armazena todas as palavras reservadas válidas

    /**
     * Construtor padrão da classe que basicamente atribui os operadores, separadores e palavras reservadas válidas da linguagem.
     */
    public LanguageScan() {
        String[] op = new String[] {"=", "+=", "&&", "==", ">", "<=", "+", "-", "*", "++", "!", "--"};

        String[] sp = new String[] {",", ".", "[", "{", "(", ")", "}", "]", ";"};

        String[] rw = new String[] {"abstract", "boolean", "char", "class", "else", "extends", "false",
                "import", "if", "instanceof", "int", "new", "null", "package",
                "private", "protected", "public", "return", "static", "super",
                "this", "true", "void", "while"};

        this.operators = new HashSet<String>(Arrays.asList(op));
        this.separators = new HashSet<String>(Arrays.asList(sp));
        this.reservedWords = new HashSet<String>(Arrays.asList(rw));
    }

    /**
     * Verifica se o lexema é um operador válido
     * @param word
     * @return true or false
     */
    public boolean isOperator(String word) {
        return (this.operators.contains(word));
    }

    /**
     * Verifica se o lexema é um separador válido
     * @param word
     * @return true or false
     */
    public boolean isSeparator(String word) {
        return (this.separators.contains(word));
    }

    /**
     * Verifica se o lexema é uma palavra reservada válida
     * @param word
     * @return true or false
     */
    public boolean isReservedWord(String word) {
        return (this.reservedWords.contains(word));
    }


    /**
     * Verifica se o lexema é um Identificador válido
     * @param word
     * @return true or false
     */
    public boolean isIdentifier(String word) {
        if (Character.isLetter(word.charAt(0)) || word.charAt(0) == '$' || word.charAt(0) == '_') {
            for(int i = 1; i < word.length(); i++) {
                if( !(Character.isLetter(word.charAt(i)) || Character.isDigit(word.charAt(i)) || word.charAt(i) == '$' || word.charAt(i) == '_') ) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Verifica se o lexema é um Inteiro literal válido
     * @param word
     * @return true or false
     */
    public boolean isIntLiteral(String word) {
        for(int i = 0; i < word.length(); i++) {
            if( !(Character.isDigit(word.charAt(i))) || ( word.length() > 1 && word.charAt(0) == '0' ) ) {
                return false;
            }
        }
        return true;
    }

    /**
     * Verifica se o lexema é um Char literal válido
     * @param word
     * @return true or false
     */
    public boolean isCharLiteral(String word) {
        if (word.charAt(0) == '\'' || word.charAt(word.length()-1) == '\'') {
            return true;
        }
        return false;
    }

    /**
     * Verifica se o lexema é uma String literal válida
     * @param word
     * @return true or false
     */
    public boolean isStringLiteral(String word) {
        if (word.charAt(0) == '"' || word.charAt(word.length()-1) == '"') {
            return true;
        }
        return false;
    }

    /**
     * Quebra a palavra em uma nova lista de lexemas.
     * @param word
     * @return List<String>
     */
    public List<String> breakWord(String word) {
        List<String> nw = new ArrayList<String>();
        String n = "";

        for(int i = 0; i < word.length(); i++) {
            char nextCh = (i < word.length()-1) ? word.charAt(i+1) : ' ';

            if(isSeparator(String.valueOf(word.charAt(i))) || isOperator(String.valueOf(word.charAt(i)))) {
                nw.add(String.valueOf(word.charAt(i)));
            } else {
                n += word.charAt(i);

                if ( isSeparator(String.valueOf(nextCh)) || isOperator(String.valueOf(nextCh))
                        || String.valueOf(nextCh).equals(" ")  ) {
                    nw.add(n);
                    n = "";
                }
            }
        }

        return nw;
    }
}
