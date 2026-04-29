package bsu.rfe.java.group10.lab1.turchak.var6;

import java.util.Objects;

public class Apple extends Food{
    private String size;
    private Integer cal;

    public Apple(String size){
        super("Яблоко");
        this.size = size;
        switch (size){
            case "малое":
                this.cal = 52;
                break;
            case "среднее":
                this.cal = 104;
                break;
            case "большое":
                this.cal = 156;
                break;
        }
    }

    @Override
    public String toString() {
        return super.toString() + " размера '" + size + "' (" + cal + " ккал)";
    }

    @Override
    public boolean equals(Object arg0) {
        if(super.equals(arg0)){
            if(!(arg0 instanceof Apple)) return false;
            return size.equals(((Apple)arg0).size) && cal.equals(((Apple)arg0).cal);
        }
        else{
            return false;
        }
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @Override
    public void consume() {
        System.out.println(this + " съедено");
    }

    @Override
    public Integer calculateCalories() {
        return cal;
    }
}
