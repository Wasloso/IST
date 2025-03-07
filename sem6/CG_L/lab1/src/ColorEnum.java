public enum ColorEnum {
    black, white, undefined;

    static ColorEnum fromColor(int color) {
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;
        return fromRGB(r, g, b);
    }

    private static ColorEnum fromRGB(int r, int g, int b) {
        if (r == 255 && g == 255 && b == 255) {
            return ColorEnum.white;
        } else if (r == 0 && g == 0 && b == 0) {
            return ColorEnum.black;
        }
        return ColorEnum.undefined;
    }

}
