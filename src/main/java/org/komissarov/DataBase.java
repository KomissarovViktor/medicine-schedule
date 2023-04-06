package org.komissarov;

import org.komissarov.models.Medicine;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;

public class DataBase {

    private String jdbcURL = "jdbc:sqlite:medicineschedule.db";
    private Connection connection;

    public DataBase() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(jdbcURL);
            System.out.println("Connected");
            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS medicines (medicineID INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(50), startDate DATE, endDate DATE, dosagePerOneTake INTEGER, takesPerDay INTEGER, time TIME, availableQuantity INTEGER, state INTEGER);");
            statement.execute("CREATE TABLE IF NOT EXISTS schedules (id INT PRIMARY KEY, date DATE, medicineID INT, FOREIGN KEY (medicineID) REFERENCES medicines(medicineID) ON UPDATE CASCADE);");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void execute(String sql) {
        try {
            Statement statement = connection.createStatement();
            statement.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertNewMedicine(Medicine medicine) {
        try {
            Statement statement = connection.createStatement();
            statement.execute("INSERT INTO medicines ('name','startDate','endDate','dosagePerOneTake','takesPerDay','time','availableQuantity','state') VALUES ('" + medicine.getName() + "','" +
                    medicine.getStartDate() + "','" +
                    medicine.getEndDate() + "','" +
                    medicine.getDosagePerOneTake() + "','" +
                    medicine.getTakesPerDay() + "','" +
                    medicine.getTime() + "','" +
                    medicine.getAvailableQuantity() + "','" +
                    medicine.getState() + "')");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public ResultSet getMedicineByID(int id){
        ResultSet rs = null;
        try {
            Statement statement = connection.createStatement();
            rs = statement.executeQuery("SELECT * from medicines WHERE medicineID='"+id+"'");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }
    public ResultSet getALLMedicines() {
        ResultSet rs = null;
        try {
            Statement statement = connection.createStatement();
            rs = statement.executeQuery("SELECT * from medicines");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }
    public void deleteMedicineById(int id){
        try {
            Statement statement = connection.createStatement();
            statement.execute("DELETE FROM medicines WHERE medicineID='"+id+"'");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void updateMedicineName(int id, String name){
        try {
            Statement statement = connection.createStatement();
            statement.execute("UPDATE medicines SET name='"+name+"' WHERE medicineID='"+id+"';");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void updateMedicineDosage(int id, int dosage){
        try {
            Statement statement = connection.createStatement();
            statement.execute("UPDATE medicines SET dosagePerOneTake='"+dosage+"' WHERE medicineID='"+id+"';");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void updateMedicineTakeTime(int id, LocalTime localTime){
        try {
            Statement statement = connection.createStatement();
            statement.execute("UPDATE medicines SET time='"+localTime+"' WHERE medicineID='"+id+"';");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void updateMedicineAvailableQuantity(int id, int quantity){
        try {
            Statement statement = connection.createStatement();
            statement.execute("UPDATE medicines SET availableQuantity='"+quantity+"' WHERE medicineID='"+id+"';");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void updateMedicineState(int id, ConsumeState state){
        try {
            Statement statement = connection.createStatement();
            statement.execute("UPDATE medicines SET state='"+state+"' WHERE medicineID='"+id+"';");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void insertSchedule(LocalDate localDate, int medicineId){
        try {
            Statement statement = connection.createStatement();
            statement.execute("INSERT INTO schedules ('date','medicineID') VALUES ('"+localDate+"','"+medicineId+"')");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteScheduleByMedicineId(int id){
        try {
            Statement statement = connection.createStatement();
            statement.execute("DELETE FROM schedules WHERE medicineID='"+id+"'");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void deleteScheduleByDateAndByMedicineId(LocalDate localDate, int medicineID){
        try {
            Statement statement = connection.createStatement();
            statement.execute("DELETE FROM schedules WHERE date='"+localDate+"' AND medicineID='"+medicineID+"'");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ResultSet getAllSchedules(){
        ResultSet rs = null;
        try {
            Statement statement = connection.createStatement();
            rs = statement.executeQuery("SELECT * from schedules");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }

    public ResultSet getScheduleOnDate(LocalDate localDate){
        ResultSet rs = null;
        try {
            Statement statement = connection.createStatement();
            rs = statement.executeQuery("SELECT * FROM schedules INNER JOIN medicines ON schedules.medicineID = medicines.medicineID WHERE schedules.date = '"+ localDate +"';");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }
    public void deleteScheduleOnDate(LocalDate localDate){
        try {
            Statement statement = connection.createStatement();
            statement.execute("DELETE FROM schedules WHERE date = '"+localDate+"';");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public int getLastInsertID(){
        ResultSet rs=null;
        int id=1;
        try {
            Statement statement = connection.createStatement();
            rs=statement.executeQuery("SELECT MAX(medicineID) from medicines;");
            if (rs.next())
            {
                id=rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }
}