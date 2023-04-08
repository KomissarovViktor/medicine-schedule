package org.komissarov;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.time.LocalTime;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorTest {
    Scanner sc;
    @Test
    void readIntNumber123() {
       sc = new Scanner("123\n");
       int result = Validator.readInt(sc);
       // Assert
       assertEquals(123, result);
       sc.close();
    }
    @Test
    void readNotIntNumber() {
        sc = new Scanner("12fsdfs3\n");
        try {
            int result = Validator.readInt(sc);
        }catch (Exception e){
            assertNotEquals(123,"12fsdfs3");
        }
        sc.close();
    }

    @Test
    void readDoubleNumber(){
        sc = new Scanner("1,5\n");
        double result = Validator.readDouble(sc);
        // Assert
        assertEquals(1.5, result);
        sc.close();
    }
    @Test
    void readNotDoubleNumber() {
        sc = new Scanner("12fsdfs3\n");
        try {
            double result = Validator.readDouble(sc);
        }catch (Exception e){
            assertNotEquals(1.5,"12fsdfs3");
        }
        sc.close();
    }
    @Test
    void TestValidTime() {
        assertEquals(true ,Validator.isTimeValid(12,30));
        assertEquals(true ,Validator.isTimeValid(0,0));
        assertEquals(true ,Validator.isTimeValid(23,59));
    }
    @Test
    void TestNotValidTime() {
        assertEquals(false ,Validator.isTimeValid(124,30));
        assertEquals(false ,Validator.isTimeValid(2,443));
        assertEquals(false ,Validator.isTimeValid(232,591));
        assertEquals(false ,Validator.isTimeValid(24,0));
        assertEquals(false ,Validator.isTimeValid(23,60));
    }
    @Test
    void TestValidDate() {
        assertEquals(true ,Validator.isDateValid(2020,1,30));
        assertEquals(true ,Validator.isDateValid(1900,12,31));
        assertEquals(true ,Validator.isDateValid(2023,1,1));
    }
    @Test
    void TestNotValidDate() {
        assertEquals(false ,Validator.isDateValid(2020,15,30));
        assertEquals(false ,Validator.isDateValid(2020,1,45));
        assertEquals(false ,Validator.isDateValid(2023,2,29));
    }
}