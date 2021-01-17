package maze;

public class Pair<V1, V2> {
    //number of first and second nodes
    private final V1 first;
    private final V2 second;

    public Pair(V1 first, V2 second) {
        this.first = first;
        this.second = second;
    }

    public static int getNumber(int i, int j, int w) {
        return i + j * w;
    }

    public V1 getFirst() {
        return first;
    }

    public V2 getSecond() {
        return second;
    }
}
