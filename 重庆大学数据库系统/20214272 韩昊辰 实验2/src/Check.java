import java.util.InputMismatchException;
import java.util.Scanner;

public class Check {
    public static String iocheck(int low, int high) {
        Scanner scanner = new Scanner(System.in);
        try {
            int i;
            while (true) {
                i = scanner.nextInt();
                if ((i <= high) && (i >= low)) {
                    break;
                }
                System.out.println("错误的输入,请重新输入");
            }
            return String.valueOf(i);
        }
        catch (IllegalStateException | InputMismatchException e) {
            System.out.println("""
                    ————非法的输入————
                    ————请重新输入选择————""");
            return iocheck(low, high);
        }
    }
}
