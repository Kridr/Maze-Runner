package maze;

public enum MazeBlock {
    PASS("  "),
    WALL("\u2588\u2588"),
    WAY("\u2592\u2592");

    private final String block;

    MazeBlock(String block) {
        this.block = block;
    }

    public String getBlock() {
        return block;
    }

}
