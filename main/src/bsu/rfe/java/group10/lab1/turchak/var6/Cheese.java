package bsu.rfe.java.group10.lab1.turchak.var6;

public class Cheese extends Food {
    public Cheese(){
        super("Сыр");
        this.cal = 402;
    }

    @Override
    public String toString() {
        return super.toString() + "(" + cal + " ккал)";
    }

    @Override
    public boolean equals(Object arg0) {
        if(super.equals(arg0)){
            if(!(arg0 instanceof Cheese)) return false;
            return cal.equals(((Cheese)arg0).cal);
        }
        else{
            return false;
        }
    }

    @Override
    public void consume() {
        System.out.println(this + " съеден");
    }

    @Override
    public Integer calculateCalories() {
        return cal;
    }
}
