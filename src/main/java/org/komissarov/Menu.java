package org.komissarov;
import org.komissarov.models.Medicine;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Scanner;
public class Menu {
    Schedule schedule = new Schedule();
    Scanner scanner = new Scanner(System.in);

    public boolean countTime(){
        List<Medicine> tempList = schedule.getScheduleOnDate(LocalDate.now());
        boolean isTimeToTake=false;
        if (tempList!=null){
            for (Medicine medicine:tempList) {
                LocalTime currentTime = LocalTime.now();
                LocalTime startPeriod = medicine.getTime().minus(10,ChronoUnit.MINUTES);
                LocalTime endPeriod = medicine.getTime().plus(10,ChronoUnit.MINUTES);
                if (medicine.getState()==ConsumeState.PLANNED) {
                    if (currentTime.isAfter(startPeriod)&&currentTime.isBefore(endPeriod)) {
                        System.out.println("Назва: "+medicine.getName());
                        System.out.println("Таблеток: "+medicine.getDosagePerOneTake());
                        System.out.println("Час: "+medicine.getTime());
                        System.out.println();
                        isTimeToTake=true;
                    }
                }
            }
        }
        return isTimeToTake;
    }
    public void startMenu(){
        showMainMenu();
    }
    public void showMainMenu() {
        int choice;
        do {
            System.out.println("Препарати, які вам потрібно прийняти зараз:");
            boolean isTimeToTake=countTime();

            System.out.println("1. Вивести графік прийому ліків");
            System.out.println("2. Добавити препарат до графіку");
            System.out.println("3. Вивести список всіх ваших препаратів");
            System.out.println("0. Вихід");
            if (isTimeToTake){
                System.out.println("Введіть 111, щоб підтвердити прийняття: ");
            }
            System.out.print("Виберіть пункт меню: ");
            choice = Validator.readInt(scanner);
            switch (choice) {
                case 1:
                    // Обробка вибору пункту 1
                    showScheduleOnDate();
                    break;
                case 2:
                    // Обробка вибору пункту 2
                    addMedicinesToSchedule();
                    break;
                case 3:
                    // Обробка вибору пункту 3
                    showAllMedicines();
                    break;
                case 111:
                    // Обробка вибору 111
                    schedule.confirmTaking();
                    break;
                case 0:
                    // Вихід з циклу
                    System.out.println("До побачення!");
                    break;
                default:
                    // Невірний вибір
                    System.out.println("Невірний вибір. Спробуйте ще раз.");
                    break;
            }
        } while (choice != 0);
        scanner.close();
    }

    public void showScheduleOnDate() {
        System.out.println("\nВведіть дату початку терапії: \nДень: ");
        List<Medicine> medicinesList;
        int day = Validator.readInt(scanner);
        System.out.println("Місяць: ");
        int month = Validator.readInt(scanner);
        try{
            medicinesList = schedule.getScheduleOnDate(LocalDate.of(2023, month, day));
        }
        catch (Exception exception) {
            System.out.println("Ви ввели не правильну дату");
            return;
        }
        if (medicinesList==null)
        {
            System.out.println("Немає графіку на цю дату");
            return;
        }
        System.out.println("\nГрафік на " + day + "." + month + "." + 2023 + ":\n");
        int i = 1;
        for (Medicine item : medicinesList) {
            System.out.println("Препарат " + i + ":");
            System.out.println("Назва:" + item.getName());
            System.out.println("Стан:" + item.getState());
            System.out.println("Кількість таблеток: " + item.getDosagePerOneTake());
            System.out.println("Час прийому: " + item.getTime());
            System.out.println("Кількість наявних таблеток: " + item.getAvailableQuantity() + "\n");
            ++i;
        }
        int choice;
        System.out.println("Щоб редагувати препарат введіть 1.");
        System.out.println("Щоб витерти графік введіть 2.");
        System.out.println("Інші варіанти - вихід");
        System.out.print("Виберіть пункт: ");
        choice = Validator.readInt(scanner);
        switch (choice)
        {
            case 1:
                showAdditionalMenuForSchedule(medicinesList,LocalDate.of(2023, month, day));
                break;
            case 2:
                schedule.deleteScheduleByDate(LocalDate.of(2023, month, day));
                break;
            default:
                break;
        }
    }

    public void addMedicinesToSchedule()
    {
        System.out.println("\nВведіть дані:\nНазва препарату: ");
        String name = scanner.next();

        System.out.println("Доза в таблетках за один прийом: ");
        int dosagePerOneTake = Validator.readInt(scanner);
        System.out.println("Кількість прийомів: ");
        int takesPerDay = Validator.readInt(scanner);
        LocalTime[] times= new LocalTime[takesPerDay];
        for (int i=0; i < takesPerDay; i++)
        {
            System.out.println("Введіть час для "+(i+1)+"-го прийому:");
            int hour,minute;
            do{
                System.out.println("Година: ");
                hour = Validator.readInt(scanner);
                System.out.println("Хвилина: ");
                minute = Validator.readInt(scanner);
                if (!Validator.isTimeValid(hour,minute)){
                    System.out.println("\nВи ввели не правильний час. Спробуйте ще раз.\n");
                }
                else{
                    times[i]=LocalTime.of(hour,minute);
                }
            }while (!Validator.isTimeValid(hour,minute));
        }

        LocalDate startDate=null,endDate=null;
        int day,month;

        do{
            System.out.println("\nВведіть дату початку терапії: \nДень: ");
            day = Validator.readInt(scanner);
            System.out.println("Місяць: ");
            month = Validator.readInt(scanner);
            if (!Validator.isDateValid(2023,month,day)){
                System.out.println("\nВи ввели неправильну дату. Спробуйте ще раз.\n");
            }
        }while (!Validator.isDateValid(2023,month,day));

        startDate  = LocalDate.of(2023,month,day);

        boolean isBefore=false;
        do
        {
            System.out.println("\nВведіть дату кінця терапії: \nДень: ");
            day = Validator.readInt(scanner);
            System.out.println("Місяць: ");
            month = Validator.readInt(scanner);
            if (!Validator.isDateValid(2023,month,day))
            {
                System.out.println("\nВи ввели неправильний формат дати кінця терапії. Введіть ще раз.");
            }
            else{
                endDate = LocalDate.of(2023,month,day);
                if (endDate.isBefore(startDate)){
                    isBefore=true;
                    System.out.println("\nДата кінця терапії передує даті початку. Введіть ще раз.");
                }
                else{
                    isBefore=false;
                }
            }
        }while(!Validator.isDateValid(2023,month,day) || isBefore);

        System.out.println("Введіть кількість наявного препарату: ");

        int quantity = Validator.readInt(scanner);

        for (int i=0; i < takesPerDay; i++)
        {
            schedule.addMedicineToSchedule(new Medicine(name, startDate, dosagePerOneTake, takesPerDay, times[i], endDate, quantity));
        }
    }
    public void showAdditionalMenuForSchedule(List<Medicine> medicinesList,LocalDate localDate)
    {
        System.out.println("Введіть номер препарату, який ви хочете редагувати: ");
        int index = Validator.readInt(scanner);
        int choice;
        do {
            System.out.println("1. Редагувати назву");
            System.out.println("2. Редугувати дозу");
            System.out.println("3. Редагувати час прийому");
            System.out.println("4. Редагувати кількість наявного препарату");
            System.out.println("5. Видалити препарат з графіку");
            System.out.println("0. Повернутися до головного меню");
            System.out.print("Виберіть пункт меню: ");
            choice = Validator.readInt(scanner);
            Medicine tempMedicine = medicinesList.get(index - 1);
            switch (choice) {
                case 1:
                    System.out.println("Введіть назву: ");
                    tempMedicine.setName(scanner.next());
                    schedule.updateMedicineName(tempMedicine.getId(),scanner.next());
                    break;
                case 2:
                    System.out.println("Введіть дозу: ");
                    schedule.updateMedicineDosage(tempMedicine.getId(),Validator.readInt(scanner));
                    break;
                case 3:
                    System.out.println("Введіть час:\n");
                    int hour,minute;
                    do{
                        System.out.println("Година: ");
                        hour = Validator.readInt(scanner);
                        System.out.println("Хвилина: ");
                        minute = Validator.readInt(scanner);
                        if (!Validator.isTimeValid(hour,minute)){
                            System.out.println("\nВи ввели не правильний час. Спробуйте ще раз.\n");
                        }
                    }while (!Validator.isTimeValid(hour,minute));
                    schedule.updateMedicineTakeTime(tempMedicine.getId(),LocalTime.of(hour, minute));
                    break;
                case 4:
                    System.out.println("Введіть кількість наявного препарату:");
                    schedule.updateMedicineAvailableQuantity(tempMedicine.getId(),Validator.readInt(scanner));
                    break;
                case 5:
                    schedule.deleteScheduleByMedicineId(medicinesList.get(index-1).getId());
                    if (medicinesList.isEmpty())
                    {
                        schedule.deleteScheduleByDate(localDate);
                    }
                    schedule.deleteMedicineFromSchedule(medicinesList.get(index-1).getId());
                    break;
                default:
                    System.out.println("Невірний вибір. Спробуйте ще раз.");
                    break;
            }
        }while (choice != 0 && choice != 5);
    }
    public void showAllMedicines()
    {
        List<Medicine> medicineList = schedule.getAllMedicines();
        System.out.println("Ваші препарати:\n");
        int i = 1;
        for (Medicine item : medicineList) {

            System.out.println("Препарат " + i + ":");
            System.out.println("Назва:" + item.getName());
            System.out.println("Кількість наявних таблеток: " + item.getAvailableQuantity() + "\n");
            ++i;
        }
    }
}
