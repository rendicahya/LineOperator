package rc.lineoperator;

import java.awt.Point;

class TemplateFactory {

    private TemplateFactory() {
    }

    static Point[][] getLineTemplate(int lineSize) {
        Point[][] template = new Point[12][];
        Point[] endPoints = getEndPoints(lineSize);

        for (int i = 0; i < template.length; i++) {
            template[i] = Bresenham.getPixels(endPoints[i].x, endPoints[i].y, endPoints[i + 12].x, endPoints[i + 12].y);
        }

        return template;
    }

    private static Point[] getEndPoints(int templateSize) {
        int mid = templateSize / 2;
        int oneSixth = templateSize / 6;
        int twoSixth = oneSixth * 2;

        Point[] endPoints = {
            new Point(0, -mid),
            new Point(oneSixth, -mid),
            new Point(twoSixth, -mid),
            new Point(mid, -mid),
            new Point(mid, -twoSixth),
            new Point(mid, -oneSixth),
            new Point(mid, 0),
            new Point(mid, oneSixth),
            new Point(mid, twoSixth),
            new Point(mid, mid),
            new Point(twoSixth, mid),
            new Point(oneSixth, mid),
            new Point(0, mid),
            new Point(-oneSixth, mid),
            new Point(-twoSixth, mid),
            new Point(-mid, mid),
            new Point(-mid, twoSixth),
            new Point(-mid, oneSixth),
            new Point(-mid, 0),
            new Point(-mid, -oneSixth),
            new Point(-mid, -twoSixth),
            new Point(-mid, -mid),
            new Point(-twoSixth, -mid),
            new Point(-oneSixth, -mid)
        };

        return endPoints;
    }
}