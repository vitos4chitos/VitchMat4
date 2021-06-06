package io;


import helpers.Functions;
import javafx.util.Pair;

import java.io.*;
import java.util.ArrayList;


public class Output {

    private boolean console;
    private File file;
    private PrintStream printStream;
    private String temp;


    public Output(boolean console){
        this.console = console;
    }

    public Output(boolean console, File file) throws FileNotFoundException {
        this.console = console;
        this.file = file;
        printStream = new PrintStream(new FileOutputStream(file));
    }

    public void print(String function, double sko, double r, double coef, String type){
        if(console){
            System.out.println(type);
            System.out.println(function);
            System.out.println("СКО: " + sko);
            System.out.println("R^2: " + r);
            if(!(Double.isNaN(coef) || Double.isInfinite(coef))){
                System.out.println("Коэффициент корреляции: " + coef);
            }
            System.out.println();
        }
        else{
            printStream.println(type);
            printStream.println(function);
            printStream.println("СКО: " + sko);
            printStream.println("R^2: " + r);
            if(!(Double.isNaN(coef) || Double.isInfinite(coef))){
                printStream.println("Коэффициент корреляции: " + coef);
            }
            printStream.println();
        }
    }

    public void printBest(Functions function, ArrayList<Pair<Double, Double>> listOfValue, int n){
        System.out.println("X   Y   Fi(x)");
        for(int i = 0; i < listOfValue.size(); i++){
            System.out.println(listOfValue.get(i).getKey() + " " + listOfValue.get(i).getValue() + " " + function.getValue(n, listOfValue.get(i).getKey()));
        }
    }
}