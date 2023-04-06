package org.komissarov;

import org.komissarov.models.Medicine;

import javax.xml.transform.Result;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.sql.Date;
import java.util.stream.Collectors;

public class Schedule {
    private Map<LocalDate, List<Medicine>> schedule = new HashMap<LocalDate, List<Medicine>>();
    private List<Medicine> medicinesList = new ArrayList<>();
    private DataBase db= new DataBase();

    public Schedule() {
        regenerateSchedules();
    }
    public void regenerateSchedules(){
        medicinesList.clear();
        medicinesList = getAllMedicines();
        schedule.clear();
        ResultSet rs = db.getAllSchedules();
        if (rs!=null) {
            try {
                List<Medicine> tempList;
                while(rs.next())
                {
                    LocalDate date=LocalDate.parse(rs.getString(2));
                    int medicineId = rs.getInt(3);
                    tempList=schedule.get(date);
                    if (tempList == null){tempList = new ArrayList<Medicine>();}
                    Comparator<Medicine> byTime = Comparator.comparing(Medicine::getTime);
                    tempList.add(getMedicineById(medicineId));
                    tempList.sort(byTime);
                    schedule.put(date,tempList);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void addMedicineToSchedule(Medicine medicine)
    {
        db.insertNewMedicine(medicine);
        LocalDate tempDate = medicine.getStartDate();
        int medicineID=db.getLastInsertID();
        while (tempDate.isBefore(medicine.getEndDate()) || tempDate.isEqual(medicine.getEndDate()))
        {
            db.insertSchedule(tempDate,medicineID);
            tempDate=tempDate.plusDays(1);
        }
        regenerateSchedules();
    }
    public List<Medicine> getScheduleOnDate(LocalDate date)
    {
        return schedule.get(date);
    }
    public void deleteScheduleByDate(LocalDate date)
    {
        db.deleteScheduleOnDate(date);
        regenerateSchedules();
    }
    public void deleteScheduleByMedicineId(int id){
        db.deleteScheduleByMedicineId(id);
        regenerateSchedules();
    }
    public void deleteMedicineFromSchedule(int id)
    {
        db.deleteMedicineById(id);
        regenerateSchedules();
    }
    public List<Medicine> getAllMedicines(){
        medicinesList.clear();
        ResultSet result = db.getALLMedicines();
        try{
            while(result.next())
            {
                medicinesList.add(new Medicine(
                        result.getInt(1),
                        result.getString(2),
                        LocalDate.parse(result.getString(3)),
                        LocalDate.parse(result.getString(4)),
                        result.getInt(5),
                        result.getInt(6),
                        LocalTime.parse(result.getString(7)),
                        result.getInt(8),
                        ConsumeState.fromInteger((Integer)result.getInt(9))));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return medicinesList;
    }
    public Medicine getMedicineById(int id) {
        Medicine medicine=null;
        try {
            ResultSet result = db.getMedicineByID(id);
            if (result.next()){
                medicine = new Medicine(
                        result.getInt(1),
                        result.getString(2),
                        LocalDate.parse(result.getString(3)),
                        LocalDate.parse(result.getString(4)),
                        result.getInt(5),
                        result.getInt(6),
                        LocalTime.parse(result.getString(7)),
                        result.getInt(8),
                        ConsumeState.fromInteger((Integer)result.getInt(9)));
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
        return medicine;
    }
    public void updateMedicineName(int id, String name){
        db.updateMedicineName(id,name);
        regenerateSchedules();
    }
    public void updateMedicineDosage(int id, int dosage){
        db.updateMedicineDosage(id,dosage);
        regenerateSchedules();
    }
    public void updateMedicineTakeTime(int id, LocalTime localTime){
        db.updateMedicineTakeTime(id,localTime);
        regenerateSchedules();
    }
    public void updateMedicineAvailableQuantity(int id, int quantity){
        db.updateMedicineAvailableQuantity(id,quantity);
        regenerateSchedules();
    }
    public void updateMedicineState(int id, ConsumeState state){
        db.updateMedicineState(id,state);
        regenerateSchedules();
    }
    public void confirmTaking(){
        List<Medicine> tempList = getScheduleOnDate(LocalDate.now());
        if (tempList!=null){
            for (Medicine medicine:tempList) {
                LocalTime currentTime = LocalTime.now();
                LocalTime startPeriod = medicine.getTime().minus(10, ChronoUnit.MINUTES);
                LocalTime endPeriod = medicine.getTime().plus(10,ChronoUnit.MINUTES);
                if (medicine.getState()==ConsumeState.PLANNED) {
                    if (currentTime.isAfter(startPeriod)&&currentTime.isBefore(endPeriod)) {
                        db.deleteScheduleByDateAndByMedicineId(LocalDate.now(),medicine.getId());
                        regenerateSchedules();
                    }
                }
            }
        }
    }
}
