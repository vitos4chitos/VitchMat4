package io;

import approximation.Approximation;
import javafx.util.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Input {
    private Scanner scanner;
    private boolean console;
    private int n;
    private Double x, y;
    private ArrayList<Pair<Double, Double>> listOfValues;
    private Approximation approximation;
    public Input(boolean console){
        this.console = console;
        listOfValues = new ArrayList<>();
        scanner = new Scanner(System.in);
    }

    public void input(){
        if(console){
            inputForConsole();
        }
        else{
            inputForFile();
        }
    }

    private void inputForConsole(){
        System.out.println("Ввыедите число значений(не менее 12)");
        while(true){
            try{
                n = Integer.parseInt(scanner.nextLine());
                if(n < 0){
                    System.out.println("Число значений меньше 12. Попробуйте ещё раз.");
                }
                else break;
            }
            catch (Exception e){
                System.out.println("Некорректный ввод");
            }
        }
        System.out.println("Введите " + n + " пар значений X Y построчно(Сначала X, потом Y)");
        while (true){
            try {
                for(int i = 0; i < n; i++) {
                    x = Double.valueOf(scanner.nextLine());
                    y = Double.valueOf(scanner.nextLine());
                    listOfValues.add(new Pair<>(x, y));
                }
                break;
            }
            catch (Exception e){
                System.out.println("Некорректный ввод");
                listOfValues.clear();
            }
        }
        System.out.println("Вывод будет производится в консоль? (y/n)");
        String nn;
        String path;
        while(true){
            try {
                nn = scanner.nextLine();
                if(nn.equals("y")) {
                    approximation = new Approximation(listOfValues, new Output(true));
                    approximation.solve();
                    break;
                }
                else if(nn.equals("n")){
                    System.out.println("Пожалуйста, введите путь до файла");
                    path = scanner.nextLine();
                    File file = new File(path);
                    approximation = new Approximation(listOfValues, new Output(false, file));
                    approximation.solve();
                    break;
                }
                else{
                    System.out.println("Вы ввели что-то не то, попробуйте ещё раз");
                }
            }
            catch (Exception e){
                System.out.println("Вы ввели что-то не то, попробуйте ещё раз");
            }

        }

    }

    private void inputForFile(){
        System.out.println("Введите путь до файла: ");
        Scanner scanner = new Scanner(System.in);
        String path = scanner.nextLine();
        try {
                BufferedReader br = new BufferedReader(new FileReader(path));
                Output output;
                int n = Integer.parseInt(br.readLine());
                double x, y;
                for (int i = 0; i < n; i++){
                    x = Double.parseDouble(br.readLine());
                    y = Double.parseDouble(br.readLine());
                    listOfValues.add(new Pair<>(x, y));
                }
                String console = br.readLine();
                String file;
                if(console.equals("n")){
                    file = br.readLine();
                    output = new Output(false, new File(file));
                }
                else{
                    output = new Output(true);
                }
                approximation = new Approximation(listOfValues, output);
                approximation.solve();
            } catch (IOException e) {
                e.printStackTrace();
            }

    }
}