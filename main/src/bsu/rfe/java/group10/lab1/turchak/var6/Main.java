package bsu.rfe.java.group10.lab1.turchak.var6;

import java.util.Arrays;

public class Main {
    static Food[] breakfast = new Food[20];
    static int items = 0;
    public static void main(String[] args){
        for(String arg: args){
            String[] parts = arg.split("/");
            switch (parts[0]){
                case "Сыр":
                    breakfast[items] = new Cheese();
                    items++;
                    break;
                case "Яблоко":
                    breakfast[items] = new Apple(parts[1]);
                    items++;
                    break;
                case "Кофе":
                    breakfast[items] = new Coffee(parts[1]);
                    items++;
                    break;
                case "-calories":
                    System.out.println("Рассчитываю калорийность завтрака: ");
                    int callories = 0;
                    for (Food i : breakfast){
                        if (i != null){
                            System.out.println(i.name + " содержит " + String.valueOf(i.calculateCalories()) + " ккал");
                            callories += i.calculateCalories();
                        }
                        else{
                            System.out.println("Общая каллорийность завтрака: " + String.valueOf(callories));
                            break;
                        }
                    }
                    break;
                case "-sort":
                    System.out.println("СОРИТРУЮ!!!!!!!" +
                            "\nБыло: ");
                    for(Food i : breakfast){
                        if(i != null) {
                            System.out.print(i.name + " ");
                        }
                        else{
                            break;
                        }
                    }

                    Arrays.sort(breakfast,new FoodComparator());

                    System.out.println("\nСтало: ");
                    for(Food i : breakfast){
                        if(i!= null){
                            System.out.print(i.name + " ");
                        }
                        else{
                            break;
                        }
                    }
                    System.out.println("\nФьюх... ОТСОРТИРОВАЛ");
            }
        }

        for(Food i : breakfast){
            if (i != null){
                i.consume();
            }
            else{
                break;
            }
        }
        System.out.println("Завтрак состоит из " + items + " продуктов");
        System.out.println("Покушали и попили с кайфом!");
    }
}