package main.model;

public enum SelectedVertex {
    TOP_LEFT(0),
    TOP_RIGHT(1),
    BOTTOM_LEFT(2),
    BOTTOM_RIGHT(3),
    CENTER(4),
    NONE(-1);
    private int idx;
    SelectedVertex(int i) {
        this.idx = i;
    }
    public int getIdx() {
        return idx;
    }
}