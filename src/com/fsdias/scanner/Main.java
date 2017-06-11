package com.fsdias.scanner;

/**
 * Created by fsdias on 03/06/17.
 */
public class Main {
    public static void main(String args[]) {

        // Teste com erro na linguagem
        System.out.println("------------------------------------------- PROGRAMA TESTE SEM ERRO: ------------------------------------------- ");
        LScanner test1 = new LScanner("teste.txt");
        test1.scanCode();

        test1.printTokens();
        test1.printSymbolTable();
        test1.printErrors();


        // Teste sem erro na linguagem
        System.out.println("\n\n ------------------------------------------- PROGRAMA TESTE COM ERRO: ------------------------------------------- ");
        LScanner test2 = new LScanner("teste2.txt");
        test2.scanCode();

        test2.printTokens();
        test2.printSymbolTable();
        test2.printErrors();
    }
}
