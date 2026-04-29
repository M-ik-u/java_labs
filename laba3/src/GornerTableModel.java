import javax.swing.table.AbstractTableModel;

public class GornerTableModel extends AbstractTableModel {
    private Double[] coefficients;
    private Double from;
    private Double to;
    private Double step;

    public GornerTableModel(Double from, Double to, Double step, Double[] coefficients){
        this.from = from;
        this.to = to;
        this.step = step;
        this.coefficients = coefficients;
    }

    public Double getFrom(){
        return from;
    }

    public Double getTo(){
        return to;
    }

    public Double getStep(){
        return step;
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public int getRowCount() {
        return Double.valueOf(Math.ceil((to - from)/ step)).intValue() + 1;
    }

    @Override
    public Object getValueAt(int row, int col) {
        double x = from + step*row;
        if (col == 0){
            return x;
        }
        double result = coefficients[0];
        for (int i = 1; i < coefficients.length; i++) {
            result = result * x + coefficients[i];
        }
        if(col == 1)return result;

        return hasTwoPairs(result);
    }

    private boolean hasTwoPairs(double value){
        String s = String.valueOf(Math.abs(value));

        int pairs = 0;
        for (int i = 0; i < s.length() - 1; i++){
            if(Character.isDigit(s.charAt(i)) && s.charAt(i) == s.charAt(i + 1)){
                pairs++;
                i++;
            }
        }
        return pairs >= 2;
    }

    @Override
    public Class<?> getColumnClass(int agr0){
        if (agr0 == 2) return Boolean.class;
        return Double.class;
    }

    @Override
    public String getColumnName(int col){
        switch (col){
            case 0:
                return "Значение X";
            case 1:
                return "Значение многочлена";
            default:
                return "Две пары";
        }
    }
}
