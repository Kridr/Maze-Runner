package maze;
import java.io.*;
import java.util.*;

public class Maze {
    Set<Pair<Integer, Integer>> maze;
    Set<Pair<Integer, Integer>> way;
    boolean isWay = false;

    int width;
    int height;

    int entrance;
    int exit;

    public Maze() {
        maze = new HashSet<>();
    }

    public Maze(int w, int h) {
        width = (w - 1) / 2;
        height = (h - 1) / 2;

        var fullGraph = fullGridGraph();
        createEntrance();
        minSpanTree(fullGraph);
    }

    private Set<Pair<Pair<Integer, Integer>, Integer>> fullGridGraph() {
        Random random = new Random();
        Set<Pair<Pair<Integer, Integer>, Integer>> fullGraph = new HashSet<>();

        int non = width * height;

        //main part of the grid
        for (int i = 0; i < height - 1; i++) {
            for (int j = 0; j < width - 1; j++) {
                int currentNode = Pair.getNumber(j, i, width);
                int rightNode = Pair.getNumber(j + 1, i, width);
                int bottomNode = Pair.getNumber(j, i + 1, width);

                fullGraph.add(new Pair<>(new Pair<>(currentNode, rightNode), random.nextInt(non)));
                fullGraph.add(new Pair<>(new Pair<>(currentNode, bottomNode), random.nextInt(non)));
            }
        }

        //right part
        for (int i = 0; i < height - 1; i++) {
            int currentNode = Pair.getNumber(width - 1, i, width);
            int bottomNode = Pair.getNumber(width - 1, i + 1, width);

            fullGraph.add(new Pair<>(new Pair<>(currentNode, bottomNode), random.nextInt(non)));
        }

        //bottom
        for (int i = 0; i < width - 1; i++) {
            int currentNode = Pair.getNumber(i, height - 1, width);
            int rightNode = Pair.getNumber(i + 1, height - 1, width);

            fullGraph.add(new Pair<>(new Pair<>(currentNode, rightNode), random.nextInt(non)));
        }

        return fullGraph;
    }

    private void minSpanTree(Set<Pair<Pair<Integer, Integer>, Integer>> fullGraph) {
        Set<Integer> availableNodes = new HashSet<>();
        availableNodes.add(0);
        maze = new HashSet<>();


        while (availableNodes.size() != width * height) {
            int maxVal = 0;
            Pair<Integer, Integer> edge = null;
            for (var e : fullGraph) {
                if (e.getSecond() >= maxVal &&
                        (
                                (availableNodes.contains(e.getFirst().getFirst()))
                                ^
                                (availableNodes.contains(e.getFirst().getSecond()))
                        )) {
                    maxVal = e.getSecond();
                    edge = new Pair<>(e.getFirst().getFirst(), e.getFirst().getSecond());
                }
            }

            assert edge != null;
            availableNodes.add(edge.getFirst());
            availableNodes.add(edge.getSecond());

            maze.add(edge);

            fullGraph.remove(edge);
        }
    }

    @Override
    public String toString() {
        int nWidth = width + (width - 1) + 2;
        int nHeight = height + (height - 1) + 2;
        String[][] mazeGrid = new String[nHeight][nWidth];

        //vertices
        for (int i = 0; i < nHeight; i++) {
            for (int j = 0; j < nWidth; j++) {
                if (i % 2 == 1 && j % 2 == 1) {
                    mazeGrid[i][j] = MazeBlock.PASS.getBlock();
                } else {
                    mazeGrid[i][j] = MazeBlock.WALL.getBlock();
                }
            }
        }

        //edges
        for (var e : maze) {
            int realIF = e.getFirst() % width;
            int realJF = e.getFirst() / width;
            int iF = realIF * 2 + 1;
            int jF = realJF * 2 + 1;

            int realIS = e.getSecond() % width;
            int realJS = e.getSecond() / width;
            int iS = realIS * 2 + 1;
            int jS = realJS * 2 + 1;

            int i = (iF + iS) / 2;
            int j = (jF + jS) / 2;

            mazeGrid[j][i] = MazeBlock.PASS.getBlock();
        }

        if (isWay) {
            for (var e : way) {
                int realIF = e.getFirst() % width;
                int realJF = e.getFirst() / width;
                int iF = realIF * 2 + 1;
                int jF = realJF * 2 + 1;

                int realIS = e.getSecond() % width;
                int realJS = e.getSecond() / width;
                int iS = realIS * 2 + 1;
                int jS = realJS * 2 + 1;

                int i = (iF + iS) / 2;
                int j = (jF + jS) / 2;

                mazeGrid[jF][iF] = MazeBlock.WAY.getBlock();
                mazeGrid[jS][iS] = MazeBlock.WAY.getBlock();
                mazeGrid[j][i] = MazeBlock.WAY.getBlock();
            }
        }

        var en_ex = isWay ? MazeBlock.WAY.getBlock() : MazeBlock.PASS.getBlock();

        mazeGrid[0][(entrance % width) * 2 + 1] = en_ex;
        mazeGrid[height + (height - 1) + 1][(exit % width) * 2 + 1] = en_ex;

        StringBuilder s = new StringBuilder();

        for (int i = 0; i < nHeight; i++) {
            for (int j = 0; j < nWidth; j++) {
                s.append(mazeGrid[i][j]);
            }
            s.append("\n");
        }

        return s.toString();
    }

    public int load(String filename) {
        try(Scanner scanner = new Scanner(new File(filename))) {
            Set<Pair<Integer, Integer>> tempMaze = new HashSet<>(maze);
            maze.clear();

            try {

                width = scanner.nextInt();
                height = scanner.nextInt();

                entrance = scanner.nextInt();
                exit = scanner.nextInt();

                while (scanner.hasNext()) {
                    int v1 = scanner.nextInt();
                    int v2 = scanner.nextInt();

                    maze.add(new Pair<>(v1, v2));
                }
                return 0;
            } catch (NoSuchElementException e) {
                maze = new HashSet<>(tempMaze);
                return 2;
            }
        } catch (FileNotFoundException e) {
            return 1;
        }
    }

    public void save(String filename) {
        try (PrintWriter printWriter = new PrintWriter(new File(filename))) {
            printWriter.println(width + " " + height);
            printWriter.println(entrance + " " + exit);
            for (var e : maze) {
                printWriter.println(e.getFirst() + " " + e.getSecond());
            }

        } catch (IOException ignored) { }
    }

    private void createEntrance() {
        Random random = new Random();

        entrance = random.nextInt(width);
        exit = height * width - entrance - 1;
    }

    //Wave algo
    public void solve() {
        var set = solveLabeling();
        solveLabelParse(set);
    }

    private Set<Pair<Integer, Integer>> solveLabeling() {
        Queue<Pair<Integer, Integer>> queue = new ArrayDeque<>();
        Set<Pair<Integer, Integer>> set = new HashSet<>();
        Set<Integer> visited = new HashSet<>();

        Pair<Integer, Integer> v = new Pair<>(entrance, 0);

        while (!v.getFirst().equals(exit)) {
            visited.add(v.getFirst());
            set.add(v);
            for (var edge : maze) {
                if (edge.getFirst().equals(v.getFirst()) && !visited.contains(edge.getSecond())) {
                    var a = new Pair<>(edge.getSecond(), v.getSecond() + 1);
                    queue.add(a);
                } else if (edge.getSecond().equals(v.getFirst()) && !visited.contains(edge.getFirst())) {
                    var a = new Pair<>(edge.getFirst(), v.getSecond() + 1);
                    queue.add(a);
                }
            }
            v = queue.poll();
        }
        set.add(v);
        return set;
    }

    private void solveLabelParse(Set<Pair<Integer, Integer>> set) {
        way = new HashSet<>();

        var curN = new Pair<>(exit, -1);
        for (var e : set) {
            if (e.getFirst().equals(exit)) {
                curN = e;
            }
        }

        while (!curN.getFirst().equals(entrance)) {
            for (var me : maze) {
                boolean eq = false;
                var posNextN = -1;
                if (me.getFirst().equals(curN.getFirst()) ) {
                    eq = true;
                    posNextN = me.getSecond();
                } else if (me.getSecond().equals(curN.getFirst())) {
                    eq = true;
                    posNextN = me.getFirst();
                }

                if (eq) {
                    for (var label : set) {
                        if (label.getFirst() == posNextN && label.getSecond() == curN.getSecond() - 1) {
                            way.add(new Pair<>(posNextN, curN.getFirst()));
                            curN = label;
                            break;
                        }
                    }
                }
            }
        }

        isWay = true;
    }
}