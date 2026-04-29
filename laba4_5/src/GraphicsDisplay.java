import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.*;
import java.io.FileOutputStream;
import java.util.Arrays;

public class GraphicsDisplay extends JPanel {
    private Double[][] graphicsData;
    private static final int MARGIN = 40;   // отступ от краёв
    private boolean showGrid = false;

    private boolean showAxis = true;
    private boolean showMarkers = true;

    //для 5 лабы
    private Double[] hoveredPoint = null;

    private boolean zoomed = false;

    private boolean selecting = false;
    private Point selectionStart = null;
    private Point selectionEnd = null;

    private double originalMinX, originalMaxX;
    private double originalMinY, originalMaxY;

    //----
    private double minX;
    private double maxX;
    private double minY;
    private double maxY;

    private double scale;

    private BasicStroke graphicsStroke;
    private BasicStroke axisStroke;
    private BasicStroke markerStroke;

    private Font axisFont;

    public GraphicsDisplay(){
        setBackground(Color.WHITE);

        graphicsStroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_ROUND, 10.0f,null,0.0f);

        axisStroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 10.0f,null, 0.0f);

        markerStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER,10.0f, null, 0.0f);

        axisFont = new Font("Serif", Font.BOLD, 36);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // ЛКМ → начало выделения области
                if (e.getButton() == MouseEvent.BUTTON1) {
                    selecting = true;
                    selectionStart = e.getPoint();
                    selectionEnd = e.getPoint();
                }

                // ПКМ → восстановление исходного масштаба
                if (e.getButton() == MouseEvent.BUTTON3) {
                    zoomed = false;
                    minX = originalMinX;
                    maxX = originalMaxX;
                    minY = originalMinY;
                    maxY = originalMaxY;
                    repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (selecting && e.getButton() == MouseEvent.BUTTON1) {
                    selecting = false;

                    // Выстроить новые границы X,Y
                    makeZoomArea();

                    selectionStart = null;
                    selectionEnd = null;

                    repaint();
                }
            }
        });

        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseMoved(MouseEvent e) {
                hoveredPoint = findHoveredPoint(e.getPoint());
                repaint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (selecting) {
                    selectionEnd = e.getPoint();
                    repaint();
                }
            }
        });

    }
    // поиск точки под курсором
    private Double[] findHoveredPoint(Point p) {
        if (graphicsData == null) return null;

        int r = 8; // радиус зоны захвата

        for (Double[] pt : graphicsData) {
            Point2D.Double xy = xyToPoint(pt[0], pt[1]);
            if (p.distance(xy.x, xy.y) <= r)
                return pt;
        }
        return null;
    }



    //обрезание графика на область
    private void makeZoomArea() {
        if (selectionStart == null || selectionEnd == null) return;

        int x1 = Math.min(selectionStart.x, selectionEnd.x);
        int x2 = Math.max(selectionStart.x, selectionEnd.x);
        int y1 = Math.min(selectionStart.y, selectionEnd.y);
        int y2 = Math.max(selectionStart.y, selectionEnd.y);

        Point2D.Double pMin = pixelToXY(x1, y2); // нижняя левая
        Point2D.Double pMax = pixelToXY(x2, y1); // верхняя правая

        minX = pMin.x;
        maxX = pMax.x;
        minY = pMin.y;
        maxY = pMax.y;

        zoomed = true;
    }




    //метод показа нового графика
    public void showGraphics(Double [][] graphicsData){
        this.graphicsData = graphicsData;
        Arrays.sort(this.graphicsData,(a, b) -> Double.compare(a[0], b[0]));

        /*
        * Arrays.stream(Double[][]) -> Stream<Double[]> поток где каждый p имеет тип Double[]
        * mapToDouble  превращает в поток DubleStream ( для примитивного double)
        * лямбда p -> p[0] беер каждый элемент типа Double[] и возращает первый эллемент массива
        * .min -> min
        * .getAsDouble() извлекает значение получиное min (OptionalDouble empty or double)
        *
        * */
        originalMinX = Arrays.stream(graphicsData).mapToDouble(p -> p[0]).min().getAsDouble();
        originalMaxX = Arrays.stream(graphicsData).mapToDouble(p -> p[0]).max().getAsDouble();
        originalMinY = Arrays.stream(graphicsData).mapToDouble(p -> p[1]).min().getAsDouble();
        originalMaxY = Arrays.stream(graphicsData).mapToDouble(p -> p[1]).max().getAsDouble();

        zoomed = false;

        repaint();
    }

    //методы-модиф. параметров отобр графиков
    public void setShowAxis(boolean showAxis){
        this.showAxis = showAxis;
        repaint();
    }

    public void setShowMarkers(boolean showMarkers){
        this.showMarkers = showMarkers;
        repaint();
    }

    public void setShowGrid(boolean showGrid) {
        this.showGrid = showGrid;
        repaint();
    }

    // пиксели в координаты
    protected Point2D.Double pixelToXY(int px, int py) {
        double x = minX + (px - MARGIN) / scale;
        double y = maxY - (py - MARGIN) / scale;
        return new Point2D.Double(x, y);
    }




    //методы-помощники
    protected Point2D.Double xyToPoint(double x, double y){
        double deltaX = x - minX;
        double deltaY = y - minY;
        return new Point2D.Double(MARGIN+deltaX*scale, getHeight() -MARGIN - deltaY*scale);
    }

    protected Point2D.Double shiftPoint(Point2D.Double src, double deltaX, double deltaY){
        Point2D.Double dest = new Point2D.Double();

        dest.setLocation(src.getX() + deltaX, src.getY() + deltaY);
        return dest;
    }

    //метод отображения линии графкиа
    protected void paintGraphics(Graphics2D canvas){
        canvas.setStroke(graphicsStroke);

        canvas.setColor(Color.RED);

        GeneralPath graphics = new GeneralPath();

        Point2D.Double start = xyToPoint(graphicsData[0][0], graphicsData[0][1]);
        graphics.moveTo(start.x, start.y);

        for (int i = 1; i < graphicsData.length; i++) {
            Point2D.Double p = xyToPoint(graphicsData[i][0], graphicsData[i][1]);
            graphics.lineTo(p.x, p.y);
        }

        canvas.draw(graphics);
    }

    //метод отображения оси координат
    protected void paintAxis(Graphics2D canvas) {
        canvas.setStroke(axisStroke);
        canvas.setColor(Color.BLACK);
        canvas.setPaint(Color.BLACK);
        canvas.setFont(axisFont);

        FontRenderContext context = canvas.getFontRenderContext();
        // Y
        if (minX <= 0 && maxX >= 0) {
            Point2D.Double p1 = xyToPoint(0, minY);
            Point2D.Double p2 = xyToPoint(0, maxY);
            canvas.draw(new Line2D.Double(p1, p2));

            // Стрелка
            GeneralPath arrow = new GeneralPath();
            arrow.moveTo(p2.x, p2.y);
            arrow.lineTo(p2.x + 10, p2.y + 20);
            arrow.lineTo(p2.x - 10, p2.y + 20);
            arrow.closePath();
            canvas.fill(arrow);

            // Метка "y"
            Rectangle2D bounds = axisFont.getStringBounds("y", context);
            canvas.drawString("y", (float)(p2.x + 15), (float)(p2.y - bounds.getY()));
        }
        //X
        if (minY <= 0 && maxY >= 0) {
            Point2D.Double p1 = xyToPoint(minX, 0);
            Point2D.Double p2 = xyToPoint(maxX, 0);
            canvas.draw(new Line2D.Double(p1, p2));

            // Стрелка
            GeneralPath arrow = new GeneralPath();
            arrow.moveTo(p2.x, p2.y);
            arrow.lineTo(p2.x - 20, p2.y - 10);
            arrow.lineTo(p2.x - 20, p2.y + 10);
            arrow.closePath();
            canvas.fill(arrow);

            // Метка "x"
            Rectangle2D bounds = axisFont.getStringBounds("x", context);
            canvas.drawString("x", (float)(p2.x - bounds.getWidth() - 5),
                    (float)(p2.y + bounds.getHeight()));
        }
    }

    //метод отображения маркеров точек графика
    protected void paintMarkers(Graphics2D canvas) {
        canvas.setStroke(markerStroke);

        for (Double[] point : graphicsData) {
            Point2D.Double center = xyToPoint(point[0], point[1]);

            int size = 11; // размер маркера
            int r = size / 2;

            GeneralPath star = new GeneralPath();

            star.moveTo(center.getX() - r, center.getY());
            star.lineTo(center.getX() + r, center.getY());
            star.moveTo(center.getX(), center.getY() - r);
            star.lineTo(center.getX(), center.getY() + r);
            star.moveTo(center.getX() - r, center.getY() - r);
            star.lineTo(center.getX() + r, center.getY() + r);
            star.moveTo(center.getX() - r, center.getY() + r);
            star.lineTo(center.getX() + r, center.getY() - r);

            if (isIncreasingDigits(point[1])) {
                canvas.setPaint(Color.BLUE); // выделение подходящей точки
            } else {
                canvas.setPaint(Color.RED);
            }

            canvas.draw(star);
        }
    }

    //провека возрастания цифр
    private boolean isIncreasingDigits(double value) {
        String s = String.format("%.10f", value).replace(",", ".");
        String digits = s.substring(s.indexOf('.') + 1);

        digits = digits.replaceAll("0+$", "");

        for (int i = 0; i < digits.length() - 1; i++) {
            if (digits.charAt(i) >= digits.charAt(i + 1))
                return false;
        }
        return digits.length() > 1;
    }


    //метод перерисовки компонента
    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        if (graphicsData == null || graphicsData.length == 0)
            return;

        /*
        // Поиск min/max по X и Y
        minX = maxX = graphicsData[0][0];
        minY = maxY = graphicsData[0][1];

        for (Double[] p : graphicsData) {
            minX = Math.min(minX, p[0]);
            maxX = Math.max(maxX, p[0]);
            minY = Math.min(minY, p[1]);
            maxY = Math.max(maxY, p[1]);
        }
        */
        if (!zoomed) {
            minX = originalMinX;
            maxX = originalMaxX;
            minY = originalMinY;
            maxY = originalMaxY;
        }


        // Чтобы оси были видны, добавим 0 в диапазон
        minX = Math.min(minX, 0);
        maxX = Math.max(maxX, 0);
        minY = Math.min(minY, 0);
        maxY = Math.max(maxY, 0);

        // Вычисляем масштаб
        double drawableWidth  = getWidth() - 2 * MARGIN;
        double drawableHeight = getHeight() - 2 * MARGIN;

        double scaleX = drawableWidth  / (maxX - minX);
        double scaleY = drawableHeight / (maxY - minY);

        scale = Math.min(scaleX, scaleY);

// центрирование графика
        /*
        double extraX = drawableWidth  - (maxX - minX) * scale;
        double extraY = drawableHeight - (maxY - minY) * scale;

        minX = minX - extraX / (2 * scale);
        minY = minY - extraY / (2 * scale);
*/
        Graphics2D canvas = (Graphics2D) g;

        //подсказка координат над точкой
        if (hoveredPoint != null) {
            Point2D.Double p = xyToPoint(hoveredPoint[0], hoveredPoint[1]);
            String text = String.format("X=%.4f, Y=%.4f", hoveredPoint[0], hoveredPoint[1]);

            //canvas.setColor(Color.BLACK);
            //canvas.fillRect((int)p.x + 10, (int)p.y - 10, text.length()*7 + 2, 6);

            canvas.setColor(Color.BLACK);
            canvas.drawString(text, (int)p.x + 13, (int)p.y - 5);
        }
        if (showGrid) paintGrid(canvas);
        if (showAxis) paintAxis(canvas);
        paintGraphics(canvas);
        if (showMarkers) paintMarkers(canvas);

        // Рисуем
        if (showAxis) paintAxis(canvas);
        paintGraphics(canvas);
        if (showMarkers) paintMarkers(canvas);


        //пунктирная рамка выделения
        if (selecting && selectionStart != null && selectionEnd != null) {
            canvas.setColor(Color.GRAY);

            float dash[] = {6f};
            canvas.setStroke(new BasicStroke(
                    1.5f,
                    BasicStroke.CAP_ROUND,
                    BasicStroke.JOIN_ROUND,
                    0f,
                    dash,
                    0f
            ));

            int x = Math.min(selectionStart.x, selectionEnd.x);
            int y = Math.min(selectionStart.y, selectionEnd.y);
            int w = Math.abs(selectionStart.x - selectionEnd.x);
            int h = Math.abs(selectionStart.y - selectionEnd.y);

            canvas.drawRect(x, y, w, h);
        }
    }

    protected void paintGrid(Graphics2D canvas) {
        canvas.setStroke(new BasicStroke(1f));
        canvas.setPaint(new Color(200, 200, 200)); // светло-серая сетка

        double stepX = (maxX - minX) / 10;
        double stepY = (maxY - minY) / 10;

        // Вертикальные линии
        for (double x = minX; x <= maxX; x += stepX) {
            Point2D.Double p1 = xyToPoint(x, minY);
            Point2D.Double p2 = xyToPoint(x, maxY);
            canvas.draw(new Line2D.Double(p1, p2));
        }

        // Горизонтальные линии
        for (double y = minY; y <= maxY; y += stepY) {
            Point2D.Double p1 = xyToPoint(minX, y);
            Point2D.Double p2 = xyToPoint(maxX, y);
            canvas.draw(new Line2D.Double(p1, p2));
        }

    }

}
