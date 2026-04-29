import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Field extends JPanel {

    private ArrayList<BouncingBall> balls = new ArrayList<BouncingBall>(10);

    private boolean paused;

    private double speedCoef = 1.0;

    public Field(){
        setBackground(Color.WHITE);

        Timer repaintTimer = new Timer(10, _ -> repaint());
        repaintTimer.start();
    }

    public void addBall(){
        balls.add(new BouncingBall(this, speedCoef));
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D canvas = (Graphics2D) g;

        for (BouncingBall ball : balls){
            ball.paint(canvas);
        }
    }

    public synchronized void pause(){
        paused = true;
    }

    public synchronized void canMove(BouncingBall ball)
            throws InterruptedException{
        if (paused){
            wait();
        }
    }

    public synchronized void resume(){
        paused = false;
        notifyAll();
    }

    public synchronized void changeSpeed(double coef){
        this.speedCoef *= coef;

        if (this.speedCoef < 0.0) this.speedCoef = 0.0;
        if ( this.speedCoef > 5.0) this.speedCoef = 5.0;

        for (BouncingBall ball : balls){
            ball.setSpeedCoef(this.speedCoef);
        }
    }

    public synchronized void resetSpeed(){
        this.speedCoef = 1.0;
        for(BouncingBall ball : balls){
            ball.resetSpeed();
        }
    }

    public synchronized void getInfo(){
        for(BouncingBall ball : balls){
            System.out.println(ball.getThread().getState());
        }
    }
}
