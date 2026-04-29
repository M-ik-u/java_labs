package bsu.rfe.java.group10.lab1.turchak.var6;

import static java.lang.Math.abs;

public class Coffee extends Food {
    private String aroma;
    private Integer cal;
    public Coffee(String aroma){
        super("Кофе");
        this.aroma = aroma;
        switch (aroma){
            case "насыщенный":
                this.cal = 270;
                break;
            case "горький":
                this.cal = 63;
                break;
            case "восточный":
                this.cal = 225;
                break;
        }
    }

    @Override
    public String toString() {
        return super.toString() + " аромата '" + aroma + "' (" + cal + " ккал)";
    }

    @Override
    public boolean equals(Object arg0) {
        if(super.equals(arg0)){
            if(!(arg0 instanceof Coffee)) return false;
            return aroma.equals(((Coffee)arg0).aroma) && cal.equals(((Coffee)arg0).cal);
        }
        else{
            return false;
        }
    }

    public String getAroma(){
        return aroma;
    }

    public void setAroma(String aroma){
        this.aroma = aroma;
    }

    @Override
    public void consume() {
        System.out.println(this + " выпито");
    }

    @Override
    public Integer calculateCalories() {
        return cal;
    }
}
