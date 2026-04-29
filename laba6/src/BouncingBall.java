import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.concurrent.atomic.AtomicInteger;

public class BouncingBall implements Runnable {

    private static final int MAX_RADIUS = 40;
    private static final int MIN_RADIUS = 3;
    private static final int MAX_SPEED = 15;

    private Field field;
    private int radius;
    private Color color;

    private Thread thisThread;

    private double x;
    private double y;

    private int speed;
    private double speedX;
    private double speedY;

    private int baseSpeed;
    private double speedCoef = 1.0;

    private static AtomicInteger ballCnt = new AtomicInteger(0);


    public BouncingBall(Field field, double newCoef) {
        this.field = field;
        this.speedCoef = newCoef;

        radius = Double.valueOf(Math.random() * (MAX_RADIUS - MIN_RADIUS)).intValue() + MIN_RADIUS;
        baseSpeed = Double.valueOf(Math.round(5 * MAX_SPEED / radius)).intValue();

        if (baseSpeed > MAX_SPEED){
            baseSpeed = MAX_SPEED;
        }

        updateSpeed();

        double angle = Math.random() * Math.PI;

        speedX = speed *Math.cos(angle);
        speedY = speed *Math.sin(angle);

        color = new Color((float)Math.random(),(float)Math.random(),
                (float)Math.random());

        x = Math.random()*(field.getSize().getWidth() - 2*radius) + radius;
        y = Math.random()*(field.getSize().getHeight() - 2*radius) + radius;

        thisThread = new Thread(this);
        thisThread.setName("Ball-" + ballCnt.incrementAndGet());
        thisThread.start();
    }

    public Thread getThread(){
        return thisThread;
    }

    private void updateSpeed(){
        speed = (int)(baseSpeed * speedCoef);
        if (speed < 0) speed = 0;
        if (speed > MAX_SPEED * 2) speed = MAX_SPEED * 2;
    }

    public void setSpeedCoef(double coef){
        this.speedCoef = coef;
        updateSpeed();

        double currentAngle = Math.atan2(speedY,speedX);
        speedX = speed * Math.cos(currentAngle);
        speedY = speed * Math.sin(currentAngle);
    }

    public void resetSpeed() {
        setSpeedCoef(1.0);
    }

    public void paint(Graphics2D canvas){
        canvas.setColor(color);
        canvas.setPaint(color);
        Ellipse2D.Double ball = new Ellipse2D.Double(x - radius, y - radius,
                2 * radius, 2 *radius);

        canvas.draw(ball);
        canvas.fill(ball);
    }

    public void run() {
        while (true) {

            try {
                field.canMove(this);
                if (x + speedX <= radius) {
                    speedX = -speedX;
                    x = radius;
                }
                else if (x + speedX >= field.getWidth() - radius) {
                    speedX = -speedX;
                    x = Double.valueOf(field.getWidth() - radius).intValue();
                }
                else {
                    x += speedX;
                }
                if (y + speedY <= radius) {
                    speedY = -speedY;
                    y = radius;
                }
                else if (y + speedY >= field.getHeight() - radius) {
                    speedY = -speedY;
                    y = Double.valueOf(field.getHeight() - radius).intValue();
                } else {
                    y += speedY;
                }

                int sleepTime = 16 - speed;
                if(sleepTime < 1) sleepTime = 1;

                Thread.sleep(sleepTime);

            } catch (InterruptedException ex) {
                break;
            }
        }
    }
}
