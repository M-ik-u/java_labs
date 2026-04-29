package bsu.rfe.java.group10.lab2.turchak;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Formula extends JFrame {

    private static final int WIDTH = 900;
    private static final int HEIGHT = 420;
    public Double mem1 = Double.valueOf(0);
    public Double mem2 = Double.valueOf(0);
    public Double mem3 = Double.valueOf(0);
    public Double result;


    private JTextField textFieldX;
    private JTextField textFieldY;
    private JTextField textFieldZ;

    private JTextField textFieldResult;
    private ButtonGroup radioButtons = new ButtonGroup();
    private ButtonGroup variableButtons = new ButtonGroup();
    private Box hboxFormulaType = Box.createHorizontalBox();
    private Box hboxVariableType = Box.createHorizontalBox();
    private int formulaId = 1;
    private int variableId = 1;

    public Formula(){
        super("Вычесление формулы");
        setSize(WIDTH,HEIGHT);
        Toolkit kit = Toolkit.getDefaultToolkit();

        setLocation((kit.getScreenSize().width - WIDTH)/2,
                (kit.getScreenSize().height - HEIGHT)/2);


        hboxFormulaType.add(Box.createHorizontalGlue());
        addRadioButton("Формула 1", 1);
        addRadioButton("Формула 2", 2);
        radioButtons.setSelected(radioButtons.getElements().nextElement().getModel(), true);

        hboxFormulaType.add(Box.createHorizontalGlue());
        hboxFormulaType.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        hboxVariableType.add(Box.createHorizontalGlue());
        addVariableButton("Переменная 1", 1);
        addVariableButton("Переменная 2", 2);
        addVariableButton("Переменная 3", 3);
        variableButtons.setSelected(variableButtons.getElements().nextElement().getModel(),true);

        hboxVariableType.add(Box.createHorizontalGlue());
        hboxVariableType.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JLabel labelForX = new JLabel("X:");
        textFieldX = new JTextField("0", 10);
        textFieldX.setMaximumSize(textFieldX.getPreferredSize());

        JLabel labelForY  = new JLabel("Y:");
        textFieldY = new JTextField("0", 10);
        textFieldY.setMaximumSize(textFieldY.getPreferredSize());

        JLabel labelForZ  = new JLabel("Z:");
        textFieldZ = new JTextField("0", 10);
        textFieldZ.setMaximumSize(textFieldZ.getPreferredSize());



        Box hboxVariables = Box.createHorizontalBox();
        hboxVariables.setBorder(BorderFactory.createLineBorder(Color.RED));
        hboxVariables.add(Box.createHorizontalGlue());

        hboxVariables.add(labelForX);
        hboxVariables.add(Box.createHorizontalStrut(10));
        hboxVariables.add(textFieldX);
        hboxVariables.add(Box.createHorizontalStrut(100));

        hboxVariables.add(labelForY);
        hboxVariables.add(Box.createHorizontalStrut(10));
        hboxVariables.add(textFieldY);
        hboxVariables.add(Box.createHorizontalStrut(100));

        hboxVariables.add(labelForZ);
        hboxVariables.add(Box.createHorizontalStrut(10));
        hboxVariables.add(textFieldZ);
        hboxVariables.add(Box.createHorizontalStrut(100));


        JLabel labelForResult = new JLabel("Резульат:");

        textFieldResult = new JTextField("0", 10);

        Box hboxResult = Box.createHorizontalBox();

        hboxResult.add(Box.createHorizontalGlue());
        hboxResult.add(labelForResult);
        hboxResult.add(Box.createHorizontalStrut(10));
        hboxResult.add(textFieldResult);
        hboxResult.add(Box.createHorizontalGlue());
        hboxResult.setBorder(BorderFactory.createLineBorder(Color.BLUE));


        JButton buttonCalc = new JButton("Вычислить");

        buttonCalc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ev) {
                try{
                    Double x = Double.parseDouble(textFieldX.getText());
                    Double y = Double.parseDouble(textFieldY.getText());
                    Double z = Double.parseDouble(textFieldZ.getText());
                    if(formulaId == 1){
                        result = calculate1(x,y,z);
                    }
                    else {
                        result = calculate2(x,y,z);
                    }
                    textFieldResult.setText(result.toString());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(Formula.this, "Ошибка в формате записи числа с плавающей точкой", "Ошибочный формат числа",JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        JButton buttonReset = new JButton("Очистить поля");
        buttonReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ev) {
                textFieldX.setText("0");
                textFieldY.setText("0");
                textFieldZ.setText("0");
                textFieldResult.setText("0");
            }
        });

        JButton buttonMC = new JButton("MC");
        buttonMC.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(variableId == 1){
                    mem1 = Double.valueOf(0);
                    textFieldResult.setText("mem1 cleared: " + mem1.toString());
                } else if (variableId == 2) {
                    mem2 = Double.valueOf(0);
                    textFieldResult.setText("mem2 cleared: " + mem2.toString());
                }
                else{
                    mem3 = Double.valueOf(0);
                    textFieldResult.setText("mem3 cleared: " + mem3.toString());
                }
            }
        });

        JButton buttonMp = new JButton("M+");
        buttonMp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(variableId == 1){
                    mem1 = Double.valueOf(mem1.doubleValue() + result.doubleValue());
                    textFieldResult.setText(mem1.toString());
                } else if (variableId == 2) {
                    mem2 = Double.valueOf(mem2.doubleValue() + result.doubleValue());
                    textFieldResult.setText(mem2.toString());
                }
                else{
                    mem3 = Double.valueOf(mem3.doubleValue() + result.doubleValue());
                    textFieldResult.setText(mem3.toString());
                }
            }
        });


        Box hboxButtons = Box.createHorizontalBox();
        hboxButtons.add(Box.createHorizontalGlue());
        hboxButtons.add(buttonCalc);
        hboxButtons.add(Box.createHorizontalStrut(30));
        hboxButtons.add(buttonReset);
        hboxButtons.add(Box.createHorizontalGlue());
        hboxButtons.add(Box.createHorizontalStrut(30));
        hboxButtons.add(buttonMp);
        hboxButtons.add(Box.createHorizontalStrut(30));
        hboxButtons.add(buttonMC);
        hboxButtons.add(Box.createHorizontalGlue());
        hboxButtons.setBorder(BorderFactory.createLineBorder(Color.GREEN));

        Box contentBox = Box.createVerticalBox();

        contentBox.add(Box.createHorizontalGlue());
        contentBox.add(hboxFormulaType);
        contentBox.add(Box.createHorizontalGlue());

        contentBox.add(hboxVariableType);
        contentBox.add(Box.createHorizontalGlue());

        contentBox.add(hboxVariables);
        contentBox.add(Box.createHorizontalGlue());

        contentBox.add(hboxResult);
        contentBox.add(hboxButtons);
        contentBox.add(Box.createHorizontalGlue());
        getContentPane().add(contentBox,BorderLayout.CENTER);


    }

    public static double calculate1(double x, double y, double z) {
        double part1 = Math.sin(Math.sin(y) + Math.exp(Math.cos(y)) + z * z);

        double sinXY2 = Math.sin(x * y * y);
        double lnX2 = Math.log(x * x);
        double part2 = Math.pow(sinXY2 + lnX2, 0.25);

        return part1 * part2;
    }

    public static double calculate2(double x, double y, double z) {
        double up = Math.pow(x, x);
        double down = Math.sqrt(Math.pow(y, 3) + 1) + Math.log(z);

        return up / down;
    }

    private void addRadioButton(String buttonName, final int formulaId){
        JRadioButton button = new JRadioButton(buttonName);
        button.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ev) {
                Formula.this.formulaId = formulaId;
            }
        });

        radioButtons.add(button);
        hboxFormulaType.add(button);
    }

    private void addVariableButton(String buttonName, final int variableId){
        JRadioButton button = new JRadioButton(buttonName);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Formula.this.variableId = variableId;
            }
        });
        variableButtons.add(button);
        hboxVariableType.add(button);
    }


    public static void main(String[] args){
        Formula frame = new Formula();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

}
