package org.komissarov.models;

import lombok.Getter;
import lombok.Setter;
import org.komissarov.ConsumeState;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Setter
@Getter
public class Medicine {
    private String name;
    private LocalDate startDate;
    private int dosagePerOneTake;
    private int takesPerDay;
    private LocalTime time;
    private LocalDate endDate;
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
}