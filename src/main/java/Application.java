import java.util.Scanner;

public class Application {
    public static void main(String[] args) {
        System.out.println("hello world");
        Scanner scanner = new Scanner(System.in);

        int i = scanner.nextInt();
        double f = scanner.nextDouble();
        String t = scanner.nextLine();

        scanner.close();

        // Write your code here.
        System.out.println(i);
        System.out.println(f);
        System.out.println(t);

        System.out.print(String.format("%-10s  %03d", "hello", 10));
    }
}
