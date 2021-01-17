import java.util.Scanner;

public class Main {
    public static long prodTree(long l, long r) {
        if (l > r) {
            return 1;
        } else if (l == r) {
            return l;
        } else if (r - l == 1) {
            return l * r;
        }
        long m = (l + r) / 2;
        return prodTree(l, m) * prodTree(m + 1, r);
    }

    public static long factorial(long n) {
        if (n < 0) {
            return 0;
        } else if (n == 0) {
            return 1;
        } else if (n == 1 || n == 2) {
            return n;
        }
        return prodTree(2, n);
    }

    /* Do not change code below */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        long n = Integer.parseInt(scanner.nextLine().trim());
        System.out.println(factorial(n));
    }
}