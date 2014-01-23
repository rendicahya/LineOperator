package rc.lineoperator;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.NewImage;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import org.apache.commons.math3.util.FastMath;

public class LineOperator {

    private LineOperator() {
    }

    public static void draw(String path, int windowSize, int lineSize) {
        Point[][] template = TemplateFactory.getLineTemplate(lineSize);
        int i = 1;
        FilenameFilter filenameFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.startsWith("m") && name.endsWith(".bmp");
            }
        };

        for (File file : new File(path).listFiles(filenameFilter)) {
            ImagePlus image = IJ.openImage(file.getAbsolutePath());
            ImagePlus lineStrength = getImage(image, windowSize, template);

            IJ.saveAs(lineStrength, "bmp", file.getParent() + "/j" + i + ".bmp");
//            file.delete();
            i++;
        }
    }

    private static ImagePlus getImage(ImagePlus imagePlus, int windowSize, Point[][] template) {
        long start = System.currentTimeMillis();

        if (windowSize % 2 == 0) {
            windowSize++;
        }

        BufferedImage inputImage = imagePlus.getBufferedImage();
        int w = inputImage.getWidth();
        int h = inputImage.getHeight();

        int[][] grayscale = new int[w][h];
        double[][] lineStrengths = new double[w][h];
        double[][] windowAvg = new double[w][h];

        BufferedImage lineStrengthImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        ImagePlus lineStrengthImagePlus = NewImage.createByteImage(null, w, h, 1, NewImage.FILL_BLACK);

//        cache the image
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                grayscale[x][y] = ColorUtils.getRed(inputImage.getRGB(x, y));
            }
        }

        double maxLineStrength = Integer.MIN_VALUE;
        double minLineStrength = Integer.MAX_VALUE;

        int templateLength = template.length;
        int half = windowSize / 2;
        int imageWidthMin1 = w - 1;
        int imageHeightMin1 = h - 1;

        for (int y = 0, convStartY = y - half, convEndY = y + half + 1; y < h; y++, convStartY++, convEndY++) {
            for (int x = 0, convStartX = x - half, convEndX = x + half + 1; x < w; x++, convStartX++, convEndX++) {
                int windowSum = 0;
                int windowCount = 0;

                for (int convX = convStartX; convX < convEndX; convX++) {
                    for (int convY = convStartY; convY < convEndY; convY++) {
                        if (convX < 0 || convX > imageWidthMin1 || convY < 0 || convY > imageHeightMin1) {
                            continue;
                        }

                        windowSum += grayscale[convX][convY];
                        windowCount++;
                    }
                }

                windowAvg[x][y] = (double) windowSum / windowCount;
                double maxLineAvg = 0;

                for (int angle = 0; angle < templateLength; angle++) {
                    int lineSum = 0;
                    int lineCount = 0;

                    for (Point pixel : template[angle]) {
                        int lineX = x + pixel.x;
                        int lineY = y + pixel.y;

                        if (lineX < 0 || lineX > imageWidthMin1
                                || lineY < 0 || lineY > imageHeightMin1) {
                            continue;
                        }

                        lineSum += grayscale[lineX][lineY];
                        lineCount++;
                    }

                    double lineAvg = (double) lineSum / lineCount;
                    maxLineAvg = FastMath.max(lineAvg, maxLineAvg);
                }

                lineStrengths[x][y] = maxLineAvg - windowAvg[x][y];
                maxLineStrength = FastMath.max(lineStrengths[x][y], maxLineStrength);
                minLineStrength = FastMath.min(lineStrengths[x][y], minLineStrength);
            }
        }

        double range = maxLineStrength - minLineStrength;

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int gray = (int) (((lineStrengths[x][y] - minLineStrength) / range) * 255);

                lineStrengthImage.setRGB(x, y, ColorUtils.construct(gray, gray, gray));
            }
        }

        lineStrengthImagePlus.setImage(lineStrengthImage);
        System.out.println("Line detector: " + (System.currentTimeMillis() - start) + " ms");

        return lineStrengthImagePlus;
    }
}
