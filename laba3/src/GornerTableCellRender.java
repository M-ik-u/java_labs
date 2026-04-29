import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

public class GornerTableCellRender implements TableCellRenderer {
    private String needle = null;
    private JPanel panel = new JPanel();
    private JLabel label = new JLabel();

    private DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance();


    public GornerTableCellRender(){
        // для того, чтобы эффективно отображать данные
        panel.add(label);
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));

        // настройка форматтера: ерунда из 12 строки
        formatter.setMaximumFractionDigits(5);
        formatter.setGroupingUsed(false);
        DecimalFormatSymbols dottedDouble = formatter.getDecimalFormatSymbols();
        dottedDouble.setDecimalSeparator('.');
        formatter.setDecimalFormatSymbols(dottedDouble);

    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                                                   int row, int col){
        String formattedDouble = formatter.format(value);
        label.setText(formattedDouble);
        if (col == 1 && needle != null && needle.equals(formattedDouble)){
            panel.setBackground(Color.RED);
            return panel;
        }
        if(value instanceof Double){
            int intPart = (int) Math.floor((Double)value);

            if (intPart % 2 == 0){
                panel.setBackground(Color.GREEN);
            }
            else{
                panel.setBackground(Color.PINK);
            }
        }
        else{
            panel.setBackground(Color.WHITE);
        }
        return panel;
    }

    public void setNeedle(String needle){
        this.needle = needle;
    }

}
