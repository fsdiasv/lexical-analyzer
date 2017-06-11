package com.fsdias.scanner;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fsdias on 05/06/17.
 */
public class LScanner {

    private String fileName; // nome do arquivo de entrada
    private int lineNum = 1; // número atual da linha que estará sendo analisada
    List<Token> tokens = new ArrayList<Token>(); // lista com todos os tokens analisados
    List<String> symbolTable = new ArrayList<String>(); // tabela de símbolos (índice e lexema)
    List<Error> errors = new ArrayList<Error>(); // lista com os erros que foram identificados

    /**
     * Construtor padrão para a classe, recebe apenas o nome do arquivo a ser lido
     * @param fileName
     */
    public LScanner(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Verifica se um lexema já está contido na tabela de símbolos.
     * Caso esteja, retorna o índice onde se encontra o lexema na tabela;
     * Caso não esteja, adiciona o lexema à tabela de símbolos e retorna o índice no qual foi inserido.
     * @param lexem
     * @return index
     */
    public String refSymbolTable(String lexem) {
        if (symbolTable.contains(lexem)) {
            return String.valueOf(symbolTable.indexOf(lexem));
        } else {
            symbolTable.add(lexem);
            return String.valueOf(symbolTable.size());
        }
    }

    /**
     * Método responsável por executar a análise do código. Recebe uma lista de possíveis lexemas (prelexem) da linha atual.
     * Verifica se o lexema é válido, caso seja, adiciona na lista de tokens e referencia o valor com a tabela de símbolos,
     * se o lexema for um identificador;
     * Caso o possível lexema seja inválido, quebrará a "prelexem" em uma nova lista de possíveis lexemas e tentará
     * verificar novamente se o lexema em questão é válido ou não. Dessa vez, caso não seja válido, retornará como erro,
     * caso seja válido, será atribuido a lista de tokens.
     * @param prelexems
     */
    public void exe(String[] prelexems) {
        int column = 0; // coluna atual
        boolean comment = false; // caso a linha seja um comentário
        boolean strltr = false; // caso tenha reconhecido um padrão literal string ou char
        String stringliteral = ""; // auxiliar para montar a string, caso seja identificada (enquanto 'strltr == true')
        LanguageScan scan = new LanguageScan(); // classe auxiliar para verificar se o lexema é válido, seguindo alguns padrões

        for (String prelexem : prelexems) {

            if (!prelexem.equals("") && !comment) { // Não entra aqui caso seja comentário (com exceção da primeira iteração de identificação) ou caso seja espaço em branco.

                /*if(strltr) {
                    stringliteral += prelexem;
                } else {

                }*/

                // Se a palavra for comentário
                if (prelexem.charAt(0) == '/' && prelexem.charAt(1) == '/') {
                    comment = true;
                    Token t = new Token("COMMENT", "COMMENT", lineNum, ++column); // constrói um novo token do tipo comentário
                    tokens.add(t); // adiciona o token a lista de tokens
                    t.setFlagPrintMode(true);
                }
                // Se for uma palavra reservada
                else if (scan.isReservedWord(prelexem) ) {
                    if (strltr) {
                        stringliteral += prelexem;
                    } else {
                        Token t = new Token(prelexem.toUpperCase(), prelexem, lineNum, ++column);
                        tokens.add(t);
                        t.setFlagPrintMode(true);
                    }
                }
                // Se for um operador
                else if (scan.isOperator(prelexem)) {
                    if (strltr) {
                        stringliteral += prelexem;
                    } else {
                        Token t = new Token(Token.tokenName(prelexem), prelexem, lineNum, ++column);
                        tokens.add(t);
                        t.setFlagPrintMode(true);
                    }
                }
                // Se for um separador
                else if (scan.isSeparator(prelexem)) {
                    if (strltr) {
                        stringliteral += prelexem;
                    } else {
                        Token t = new Token(Token.tokenName(prelexem), prelexem, lineNum, ++column);
                        tokens.add(t);
                        t.setFlagPrintMode(true);
                    }
                }
                // Se for um inteiro literal
                else if (scan.isIntLiteral(prelexem)) {
                    if (strltr) {
                        stringliteral += prelexem;
                    } else {
                        Token t = new Token("INT_LIT", prelexem, lineNum, ++column);
                        tokens.add(t);
                    }
                }
                // Se for uma string literal
                else if (scan.isStringLiteral(prelexem) ) {

                    strltr = !strltr;

                    stringliteral += prelexem;
                    if(!strltr) {
                        Token t = new Token("STR_LIT", stringliteral, lineNum, ++column);
                        tokens.add(t);
                    }
                }
                // Se for um char literal
                else if (scan.isCharLiteral(prelexem) ) {

                    strltr = !strltr;

                    stringliteral += prelexem;
                    if(!strltr) {
                        Token t = new Token("CHAR_LIT", stringliteral, lineNum, ++column);
                        tokens.add(t);
                    }
                }
                // Se for um identificador
                else if (scan.isIdentifier(prelexem)) {
                    if (strltr) {
                        stringliteral += " " + prelexem;
                    } else {
                        Token t = new Token("ID", refSymbolTable(prelexem), lineNum, ++column);
                        tokens.add(t);
                    }
                }
                // Se não for um lexema válido, basicamente repete o processo acima
                else {
                    List<String> newlexems = scan.breakWord(prelexem); // Quebrará a palavra atual em uma nova lista de possíveis lexemas

                    for (String nl : newlexems) {
                        if (!nl.equals("") && !comment) { // Não entra aqui caso seja comentário (com exceção da primeira iteração de identificação) ou caso seja espaço em branco.

                            // Se for um comentário
                            if (prelexem.charAt(0) == '/' && prelexem.charAt(1) == '/') {
                                comment = true;
                                Token t = new Token("COMMENT", "COMMENT", lineNum, ++column);
                                tokens.add(t);
                                t.setFlagPrintMode(true);
                            }
                            // Se for uma palavra reservada
                            else if (scan.isReservedWord(nl)) {
                                if (strltr) {
                                    stringliteral += nl;
                                } else {
                                    Token t = new Token(nl.toUpperCase(), nl, lineNum, ++column);
                                    t.setFlagPrintMode(true);
                                    tokens.add(t);
                                }
                            }
                            // Se for um operador
                            else if (scan.isOperator(nl)) {
                                if (strltr) {
                                    stringliteral += nl;
                                } else {
                                    Token t = new Token(Token.tokenName(nl), nl, lineNum, ++column);
                                    tokens.add(t);
                                    t.setFlagPrintMode(true);
                                }
                            }
                            // Se for um separador
                            else if (scan.isSeparator(nl) ) {
                                if (strltr) {
                                    stringliteral += nl;
                                } else {
                                    Token t = new Token(Token.tokenName(nl), nl, lineNum, ++column);
                                    tokens.add(t);
                                    t.setFlagPrintMode(true);
                                }
                            }
                            // Se for um inteiro literal
                            else if (scan.isIntLiteral(nl)) {
                                if (strltr) {
                                    stringliteral += nl;
                                } else {
                                    Token t = new Token("INT_LIT", nl, lineNum, ++column);
                                    tokens.add(t);
                                }
                            }
                            // Se for uma string literal
                            else if (scan.isStringLiteral(nl)) {

                                strltr = !strltr;

                                stringliteral += nl;
                                if(!strltr) {
                                    Token t = new Token("STR_LIT", stringliteral, lineNum, ++column);
                                    tokens.add(t);
                                }
                            }
                            // Se for um char literal
                            else if (scan.isCharLiteral(prelexem) ) {

                                strltr = !strltr;

                                stringliteral += prelexem;
                                if(!strltr) {
                                    Token t = new Token("CHAR_LIT", stringliteral, lineNum, ++column);
                                    tokens.add(t);
                                }
                            }
                            // Se for um identificador
                            else if (scan.isIdentifier(nl)) {
                                if (strltr) {
                                    stringliteral += " " + nl;
                                } else {
                                    Token t = new Token("ID", refSymbolTable(nl), lineNum, ++column);
                                    tokens.add(t);
                                }
                            }
                            // Caso não seja um lexema válido, retornará como erro.
                            else {
                                Error e = new Error(nl, lineNum, ++column); // Constrói um novo erro
                                errors.add(e); // Adiciona a lista de errors
                            }

                        } // fim - if (!nl.equals("") && !comment)
                    } // fim - for (String nl : newlexems)
                } // fim - else (segunda iteração, depois da quebra da palavra
            } // fim - if (!prelexem.equals("") && !comment)
        } // fim - for (String prelexem : prelexems)
    }

    /**
     * Método de entrada do programa.
     * Lê linha por linha do arquivo fonte informado e chama o método de extração de tokens.
     */
    public void scanCode() {
        try {
            FileReader file = new FileReader(this.fileName); // Lê o arquivo com o nome informado
            BufferedReader rf = new BufferedReader(file);
            String line = rf.readLine();

            while (line != null) {
                String[] prelexems = line.split("\\s+"); // Quebra a linha atual removendo os espaços

                exe(prelexems); // Chama método para identificar os tokens, passando a lista de palavras iniciais a serem analisadas.

                lineNum++; // Incrementa o número da linha atual
                line = rf.readLine(); // Lê a próxima linha
            }

        } catch (FileNotFoundException e) {
            System.err.println("Erro ao abrir arquivo: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Imprime todos os tokens contidos no arquivo fonte
     * @return void
     */
    public void printTokens() {
        for (int i = 0; i < tokens.size(); i++) {

            if (tokens.get(i).getColumn() == 1) System.out.print("Line #" + tokens.get(i).getLine() + ": ");
            if ( i < tokens.size()-1 && (tokens.get(i).getLine() != tokens.get(i+1).getLine()) )  {
                if(tokens.get(i).isPrintMode()) {
                    System.out.print("<" + tokens.get(i).getType() + ">#" + tokens.get(i).getColumn() + "  ");
                } else {
                    System.out.print("<" + tokens.get(i).getType() + ", " + tokens.get(i).getValue() + ">#" + tokens.get(i).getColumn() + "  ");
                }
                System.out.println();
            } else if (i < tokens.size()){
                if( (tokens.get(i).isPrintMode())) {
                    System.out.print("<" + tokens.get(i).getType() + ">#" + tokens.get(i).getColumn() + "  ");
                } else {
                    System.out.print("<" + tokens.get(i).getType() + ", " + tokens.get(i).getValue() + ">#" + tokens.get(i).getColumn() + "  ");
                }
            }
        }
    }

    /**
     * Imprime todos os erros encontrados no arquivo
     * @return void
     */
    public void printErrors() {
        System.out.println("\n-------------------------------\nERRORS:");

        if (errors.size() == 0) {
            System.out.println("No errors!");
        } else {
            for(Error e : errors) {
                System.out.println(e.getValue() + "  (Line #" + e.getLine() + ", Column #" + e.getColumn() + ")");
            }
        }
    }

    /**
     * Imprime a tabela de símbolos com todas as entradas
     * @return void
     */
    public void printSymbolTable() {
        System.out.println("\n-------------------------------\nSYMBOL TABLE:");
        for(int i = 0; i < symbolTable.size(); i++) {
            System.out.println("#" + (i+1) + " : " + symbolTable.get(i));
        }
    }
}
