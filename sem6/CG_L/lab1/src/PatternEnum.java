
public enum PatternEnum {
    grid,
    rings,
    chessboard;

    static PatternEnum fromString(String s) {
        return switch (s) {
            case "grid" -> PatternEnum.grid;
            case "rings" -> PatternEnum.rings;
            case "chessboard" -> PatternEnum.chessboard;
            default -> throw new IllegalArgumentException(
                    "Invalid pattern: " + s + "\nUse either grid, rings or chessboard");
        };
    }
}