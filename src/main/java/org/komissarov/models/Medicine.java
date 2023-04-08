package org.komissarov.models;

import lombok.Getter;
import lombok.Setter;
import org.komissarov.ConsumeState;

import java.time.LocalDate;
import java.time.LocalTime;


@Setter
@Getter
public class Medicine {
    private int id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private int dosagePerOneTake;
    private int takesPerDay;
    private LocalTime time;
    private int availableQuantity;
    private ConsumeState state = ConsumeState.PLANNED;

    public Medicine(String name, LocalDate startDate, int dosagePerOneTake, int takesPerDay, LocalTime time, LocalDate endDate, int availableQuantity) {
        this.name = name;
        this.startDate = startDate;
        this.dosagePerOneTake = dosagePerOneTake;
        this.takesPerDay = takesPerDay;
        this.time = time;
        this.endDate = endDate;
        this.availableQuantity = availableQuantity;
    }

    public Medicine(int id, String name, LocalDate startDate, LocalDate endDate, int dosagePerOneTake, int takesPerDay, LocalTime time, int availableQuantity, ConsumeState state) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.dosagePerOneTake = dosagePerOneTake;
        this.takesPerDay = takesPerDay;
        this.time = time;
        this.availableQuantity = availableQuantity;
        this.state = state;
    }
}