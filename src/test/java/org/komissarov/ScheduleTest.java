package org.komissarov;

import org.junit.jupiter.api.*;
import org.komissarov.models.Medicine;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class ScheduleTest {

    private List<Medicine> medicinesList = new ArrayList<>();
    static Schedule schedule;
    @BeforeAll
    static void setUp() {
        schedule = new Schedule();
        schedule.addMedicineToSchedule(new Medicine(1,"aspirin",LocalDate.of(2023,4,6),LocalDate.of(2023,4,6),1,1, LocalTime.of(9,0), 100,ConsumeState.PLANNED));
    }
    @AfterAll
    static void deleteRows(){
        schedule.deleteAllSchedules();
    }
    @BeforeEach
    void beforeEach(){
        medicinesList.add(new Medicine(schedule.getLastInsertId(), "aspirin",LocalDate.of(2023,4,6),LocalDate.of(2023,4,6),1,1, LocalTime.of(9,0), 100,ConsumeState.PLANNED));
    }


    @Test
    void TestGetAllMedicines() {
        assertEquals(medicinesList.size(),schedule.getAllMedicines().size());
    }
    @Test
    void TestGetMedicineById() {
        assertEquals(medicinesList.get(0).getName(),schedule.getMedicineById(schedule.getLastInsertId()).getName());
    }
    @Test
    void TestAddMedicineToSchedule() {
        Medicine medicine = schedule.getMedicineById(schedule.getLastInsertId());
        assertEquals("aspirin",medicine.getName());
    }
    @Test
    void TestGetScheduleOnDateIsExist() {
        LocalDate date= LocalDate.of(2023,4,6);
        List<Medicine> TempList = schedule.getScheduleOnDate(date);
        boolean isTrue = true;
        for (Medicine m : TempList) {
            if((m.getStartDate().isBefore(date) || m.getStartDate().isEqual(date)) &&
                    (m.getEndDate().isAfter(date) || m.getEndDate().isEqual(date))) {
            } else if (m.getStartDate().isEqual(date) && m.getEndDate().isEqual(date)){}
                    else isTrue = false;
        }
        assertEquals(true,isTrue);
    }
    @Test
    void TestGetScheduleOnDateIsNotExist() {
        List<Medicine> TempList = schedule.getScheduleOnDate(LocalDate.of(1999,4,25));
        assertEquals(null, TempList);
    }

    @Test
    void TestDeleteScheduleByDate() {
        LocalDate date= LocalDate.of(2023,4,6);
        schedule.deleteScheduleByDate(date);
        assertEquals(null, schedule.getScheduleOnDate(date));
    }
    @Test
    void TestDeleteScheduleByMedicineId() {

        LocalDate date= LocalDate.of(2023,4,6);
        Medicine medicine = schedule.getMedicineById(schedule.getLastInsertId());
        boolean containsID = false;
        int lastId = schedule.getLastInsertId();
        if (schedule.getScheduleOnDate(date).size()<=1){
            schedule.deleteScheduleByMedicineId(schedule.getLastInsertId());
            assertEquals(null, schedule.getScheduleOnDate(date));
        }
        else{
            for (Medicine m : schedule.getScheduleOnDate(date)){
                if (m.getId()==lastId) containsID=true;
            }
            assertEquals(false, containsID);
        }
    }
    @Test
    void TestDeleteMedicineFromSchedule() {
        int lastId = schedule.getLastInsertId();
        schedule.deleteMedicineFromSchedule(lastId);
        assertEquals(null,schedule.getMedicineById(lastId));
    }
    @Test
    void TestUpdateMedicineName() {
        int lastId = schedule.getLastInsertId();
        schedule.updateMedicineName(lastId,"Analhin");
        assertEquals("Analhin",schedule.getMedicineById(lastId).getName());
    }

}