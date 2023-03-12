package org.komissarov.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.Date;

@Setter
@Getter
public class Medicine {
    private String name;
    private Date startDate;
    private int dosagePerOneTake;
    private int takesPerDay;
    private LocalTime time;
    private int durationDays;
    private int availableQuantity;
}