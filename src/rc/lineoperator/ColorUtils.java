package rc.lineoperator;

class ColorUtils {

    static int construct(int r, int g, int b) {
        return 65536 * r + 256 * g + b;
    }

    static int[] separate(int rgb) {
        return new int[]{(rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF};
    }

    static int getRed(int rgb) {
        return (rgb >> 16) & 0xFF;
    }

    static int getGreen(int rgb) {
        return (rgb >> 8) & 0xFF;
    }

    static int getBlue(int rgb) {
        return rgb & 0xFF;
    }

    static int getRGBAverage(int rgb) {
        int red = (rgb >> 16) & 0xFF;
        int green = (rgb >> 8) & 0xFF;
        int blue = rgb & 0xFF;

        return (red + green + blue) / 3;
    }

    static int getRGBSum(int rgb) {
        int red = (rgb >> 16) & 0xFF;
        int green = (rgb >> 8) & 0xFF;
        int blue = rgb & 0xFF;

        return red + green + blue;
    }
}
