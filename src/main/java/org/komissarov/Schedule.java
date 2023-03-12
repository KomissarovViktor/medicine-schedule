package org.komissarov;

import org.komissarov.models.Medicine;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Schedule {
    private Map<LocalDate, List<Medicine>> schedule = new HashMap<LocalDate, List<Medicine>>();

    public void addMedicineToSchedule(Medicine medicine)
    {
        LocalDate tempDate = medicine.getStartDate();

        while (tempDate.isBefore(medicine.getEndDate()) || tempDate.isEqual(medicine.getEndDate()))
        {
            List<Medicine> tempList = schedule.get(tempDate);
            if (tempList == null){tempList = new ArrayList<Medicine>();}
            tempList.add(medicine);
            Comparator<Medicine> byTime = Comparator.comparing(Medicine::getTime);
            tempList.sort(byTime);
            schedule.put(tempDate,tempList);
            tempDate=tempDate.plusDays(1);

        }
    }
    public List<Medicine> getScheduleOnDate(LocalDate date)
    {
        return schedule.get(date);
    }
    public void deleteScheduleByDate(LocalDate date)
    {
        schedule.remove(date);
    }
    public void deleteMedicineFromSchedule(String name)
    {
        schedule.forEach((key, medicinesList) -> {
            medicinesList.removeIf(medicine -> medicine.getName()==name);
        });
    }
}