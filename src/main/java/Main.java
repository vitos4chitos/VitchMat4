import java.util.Scanner;
import io.Input;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ввод будет производиться с консоли?(y/n)");
        boolean console;
        Input input;
        while (true){
            String a = "";
            a = scanner.nextLine();
            if(a.equals("y")) {
                console = true;
                input = new Input(console);
                input.input();
                break;
            }
            else{
                if(a.equals("n")){
                    console = false;
                    input = new Input(console);
                    input.input();
                    break;
                }
                else{
                    System.out.println("Вы ввели что-то не то, попробуйте ещё раз");
                }
            }
        }

    }
}
