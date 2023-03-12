package org.komissarov;
import org.komissarov.models.Medicine;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;
public class Menu {
    Schedule schedule = new Schedule();
    Scanner scanner = new Scanner(System.in);

    public void showMainMenu() {
        int choice;
        do {
            System.out.println("1. Вивести графік прийому ліків");
            System.out.println("2. Добавити препарат до графіку");
            System.out.println("3. ......");
            System.out.println("0. Вихід");
            System.out.print("Виберіть пункт меню: ");
            choice = scanner.nextInt();

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
                    System.out.println("Ви вибрали пункт 3");
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
        int day = scanner.nextInt();
        System.out.println("Місяць: ");
        int month = scanner.nextInt();
        List<Medicine> medicinesList = schedule.getScheduleOnDate(LocalDate.of(2023, month, day));
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
        choice = scanner.nextInt();
        switch (choice)
        {
            case 1:
                showAdditionalMenuForSchedule(medicinesList);
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
        int dosagePerOneTake = scanner.nextInt();
        System.out.println("Кількість прийомів: ");
        int takesPerDay = scanner.nextInt();
        LocalTime[] times= new LocalTime[takesPerDay];
        for (int i=0; i < takesPerDay; i++)
        {
            System.out.println("Введіть час для "+(i+1)+"-го прийому:\nГодина: ");
            int hour = scanner.nextInt();
            System.out.println("Хвилина: ");
            int minute = scanner.nextInt();
            times[i]=LocalTime.of(hour,minute);
        }

        System.out.println("\nВведіть дату початку терапії: \nДень: ");
        int day = scanner.nextInt();
        System.out.println("Місяць: ");
        int month = scanner.nextInt();

        LocalDate startDate = LocalDate.of(2023,month,day);
        LocalDate endDate;
        do
        {
            System.out.println("\nВведіть дату кінця терапії: \nДень: ");
            day = scanner.nextInt();
            System.out.println("Місяць: ");
            month = scanner.nextInt();

            endDate = LocalDate.of(2023,month,day);
            if (endDate.isBefore(startDate))
            {
                System.out.println("\nВи ввели неправильну дату кінця терапії, введіть ще раз.");
            }
        }while(endDate.isBefore(startDate));

        System.out.println("Введіть кількість наявного препарату: ");

        int quantity = scanner.nextInt();

        for (int i=0; i < takesPerDay; i++)
        {
            schedule.addMedicineToSchedule(new Medicine(name, startDate, dosagePerOneTake, takesPerDay, times[i], endDate, quantity));
        }
    }
    public void showAdditionalMenuForSchedule(List<Medicine> medicinesList)
    {
        System.out.println("Введіть номер препарату, який ви хочете редагувати: ");
        int index = scanner.nextInt();
        int choice;
        do {
            System.out.println("1. Редагувати назву");
            System.out.println("2. Редугувати дозу");
            System.out.println("3. Редагувати час прийому");
            System.out.println("4. Редагувати кількість наявного препарату");
            System.out.println("5. Видалити препарат з графіку");
            System.out.println("0. Повернутися до головного меню");
            System.out.print("Виберіть пункт меню: ");
            choice = scanner.nextInt();
            Medicine tempMedicine = medicinesList.get(index - 1);
            switch (choice) {
                case 1:
                    System.out.println("Введіть назву: ");
                    tempMedicine.setName(scanner.next());
                    break;
                case 2:
                    System.out.println("Введіть дозу: ");
                    tempMedicine.setDosagePerOneTake(scanner.nextInt());
                    break;
                case 3:
                    System.out.println("Введіть час:\n");
                    System.out.println("Година: ");
                    int hour = scanner.nextInt();
                    System.out.println("Хвилина: ");
                    int minute = scanner.nextInt();
                    tempMedicine.setTime(LocalTime.of(hour, minute));
                    break;
                case 4:
                    System.out.println("Введіть кількість наявного препарату:");
                    tempMedicine.setAvailableQuantity(scanner.nextInt());
                    break;
                case 5:
                    medicinesList.remove(index - 1);
                    break;
                default:
                    System.out.println("Невірний вибір. Спробуйте ще раз.");
                    break;
            }
        }while (choice != 0 || choice != 5);
    }
}
