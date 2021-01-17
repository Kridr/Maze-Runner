package maze;

import java.util.Scanner;

public class Wicked {
    private final static String menuHeadline = "=== Menu ===\n";
    private final static String firstOption = "1. Generate a new maze\n";
    private final static String secondOption = "2. Load a maze\n";
    private final static String thirdOption = "3. Save the maze\n";
    private final static String fourthOption = "4. Display the maze\n";
    private final static String fifthOption = "5. Solve the maze\n";
    private final static String zerothOption = "0. Exit";

    private final static String preMenu =
            menuHeadline +
                    firstOption +
                    secondOption +
                    zerothOption;

    private final static String fullMenu =
            menuHeadline +
                    firstOption +
                    secondOption +
                    thirdOption +
                    fourthOption +
                    fifthOption +
                    zerothOption;

    private final static String incorrectOption = "Incorrect option. Please try again";

    private Maze maze;

    public void menu() {
        Scanner scanner = new Scanner(System.in);
        int option;

        do {
            if (maze == null) {
                System.out.println(preMenu);
            } else {
                System.out.println(fullMenu);
            }

            option = scanner.nextInt();
            switch (option) {
                case 1:
                    generate();
                    break;
                case 2:
                    load();
                    break;
                case 3:
                    save();
                    break;
                case 4:
                    output();
                    break;
                case 5:
                    solve();
                    break;
                case 0:
                    exit();
                    break;
                default:
                    System.out.println(incorrectOption);
            }
        } while (option != 0);
    }

    private void generate() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please, enter the size of a maze");
        int w = scanner.nextInt();
        int h = scanner.nextInt();

        if (w % 2 == 0 || h % 2 == 0) {
            System.out.println("Dimensions cannot be even");
        } else {
            maze = new Maze(w, h);
            System.out.println(maze);
        }
    }

    private void load() {
        Scanner scanner = new Scanner(System.in);
        String filename = scanner.nextLine();
        maze = new Maze();
        int res = maze.load(filename);
        if (res == 1) {
            System.out.println("The file " + filename + " does not exist");
        } else if (res == 2) {
            System.out.println("Cannot load the maze. It has an invalid format");
        }
    }

    private void save() {
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        maze.save(name);
    }

    private void output() {
        System.out.println(maze);
    }

    private void solve() {
        maze.solve();
        System.out.println(maze);
    }

    private void exit() {
        System.out.println("Bye!");
    }
}
