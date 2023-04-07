package org.komissarov;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

public class Validator {
    public static int readInt(Scanner sc) {
        int value;
        while (!sc.hasNextInt()) {
            System.out.println("Це не число!");
            System.out.println("Введіть ще раз:\n");
            sc.next();
        }
        value = sc.nextInt();
        return value;
    }
    public static double readDouble(Scanner sc) {
        double value;
        while (!sc.hasNextDouble()) {
            System.out.println("Це не число!");
            System.out.println("Введіть ще раз:");
            sc.next();
        }
        value = sc.nextDouble();
        return value;
    }
    public static boolean isTimeValid(int hour, int minute){
        boolean isValid = false;
        try{
            LocalTime.of(hour,minute);
            isValid=true;
        }catch (Exception e){

        }
        return isValid;
    }
    public static boolean isDateValid(int year, int month, int day){
        boolean isValid = false;
        try{
            LocalDate.of(year,month,day);
            isValid=true;
        }catch (Exception e){

        }
        return isValid;
    }
}
