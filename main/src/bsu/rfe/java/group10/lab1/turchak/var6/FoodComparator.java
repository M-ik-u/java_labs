package bsu.rfe.java.group10.lab1.turchak.var6;

import java.util.Comparator;

public class FoodComparator implements Comparator<Food>{
    @Override
    public int compare(Food arg0, Food arg1) {
        if(arg0 == null) return 1;
        if(arg1 == null) return -1;
        return arg0.calculateCalories().compareTo(arg1.calculateCalories());
    }
}
