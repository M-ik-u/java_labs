import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class MainFrame extends JFrame {
    private static final int WIDTH = 700;
    private static final int HEIGHT = 500;

    private JMenuItem pauseMenuItem;
    private JMenuItem resumeMenuItem;

    private Field field = new Field();

    public MainFrame() {
        super("Программирование и синхронизация потоков");
        setSize(WIDTH, HEIGHT);
        Toolkit kit = Toolkit.getDefaultToolkit();

        setLocation((kit.getScreenSize().width - WIDTH)/2,
                (kit.getScreenSize().height - HEIGHT)/2);

        setExtendedState(MAXIMIZED_BOTH);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu ballMenu = new JMenu("Мячи");

        Action addBallAction = new AbstractAction("Добавить мяч") {
            @Override
            public void actionPerformed(ActionEvent e) {
                field.addBall();
                if (!pauseMenuItem.isEnabled() &&
                !resumeMenuItem.isEnabled()){
                    pauseMenuItem.setEnabled(true);
                }
            }
        };

        menuBar.add(ballMenu);
        ballMenu.add(addBallAction);

        JMenu controlMenu = new JMenu("Управление");
        menuBar.add(controlMenu);
        Action pauseAction = new AbstractAction("Приостановить движение"){
            public void actionPerformed(ActionEvent event) {
                field.pause();
                pauseMenuItem.setEnabled(false);
                resumeMenuItem.setEnabled(true);
            }
        };
        pauseMenuItem = controlMenu.add(pauseAction);
        pauseMenuItem.setEnabled(false);
        Action resumeAction = new AbstractAction("Возобновить движение") {
            public void actionPerformed(ActionEvent event) {
                field.resume();
                pauseMenuItem.setEnabled(true);
                resumeMenuItem.setEnabled(false);
            }
        };

        resumeMenuItem = controlMenu.add(resumeAction);
        resumeMenuItem.setEnabled(false);

        getContentPane().add(field, BorderLayout.CENTER);

        JPanel speedControl = new JPanel();

        JButton speedUp = new JButton("+ speed");
        JButton speedDown = new JButton("- speed");
        JButton speedReset = new JButton("reset");
        JButton getInfo = new JButton("info");


        speedUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                field.changeSpeed(1.2);
            }
        });

        speedDown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                field.changeSpeed(0.8);
            }
        });

        speedReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                field.resetSpeed();
            }
        });

        getInfo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                field.getInfo();
            }
        });

        speedControl.add(speedDown);
        speedControl.add(speedUp);
        speedControl.add(speedReset);
        speedControl.add(getInfo);

        getContentPane().add(speedControl, BorderLayout.SOUTH);
    }

    public static void main(String[] args){
        MainFrame frame = new MainFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

}
