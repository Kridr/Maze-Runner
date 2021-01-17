import java.util.Scanner;

public class Main {

    public static long power(int n, int m) {
        if (m == 0) {
            return 1;
        } else if (m % 2 == 1) {
            return power(n, m - 1) * n;
        } else {
            long b = power(n, m / 2);
            return b * b;
        }
    }

    /* Do not change code below */
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        final int n = scanner.nextInt();
        final int m = scanner.nextInt();
        System.out.println(power(n, m));
    }
}