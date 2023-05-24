package DBClientApplication.Helper;


import DBClientApplication.Model.Appointment;
import DBClientApplication.Model.Customer;
import DBClientApplication.Model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

import static DBClientApplication.Model.Appointment.modifiedAppointment;

/***
 * AppointmentQuery is a helper class designed after the 'fruits query' webinars that allow for database interactions within methods.
 */
public abstract class AppointmentQuery {


    /***
     * AppointmentQuery constructor
     * @throws SQLException
     */
    protected AppointmentQuery() throws SQLException {
    }

    /***
     * logIn is a method that validates a username and password from the database.
     * @param Username
     * @param Password
     * @return
     */
    public static Boolean logIn(String Username, String Password) {
        try {

            String sql = "SELECT * FROM users WHERE User_Name = '" + Username + "' AND Password = '" + Password + "'";
            ResultSet rs = JDBC.connection.createStatement().executeQuery(sql);
            if (rs.next()) {
                User currentUser = new User();
                currentUser.setUserName(rs.getString("User_Name"));
                currentUser.setId(rs.getInt("User_ID"));

                return Boolean.TRUE;

            } else {
                return Boolean.FALSE;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /***
     * insert is a method that passes data from appointment parameters into a new appointment stored in the database.
     * @param appointment
     * @return
     * @throws SQLException
     */
    public static int insert(Appointment appointment) throws SQLException {

        String sql = "INSERT INTO APPOINTMENTS (Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        ps.setString(1, appointment.getTitle());
        ps.setString(2, appointment.getDescription());
        ps.setString(3, appointment.getLocation());
        ps.setString(4, appointment.getType());
        ps.setTimestamp(5, appointment.getStart());
        ps.setTimestamp(6, appointment.getEnd());
        ps.setInt(7, appointment.getCustomerId());
        ps.setInt(8, appointment.getUserId());
        ps.setInt(9, appointment.getContactId());

        int rowsAffected = ps.executeUpdate();

        return rowsAffected;

    }

    /***
     * insertCustomer is a method that passes data from the customer class into a new customer stored in the database.
     * @param customer
     * @return
     * @throws SQLException
     */
    public static int insertCustomer(Customer customer) throws SQLException {

        String sql = "INSERT INTO CUSTOMERS (Customer_Name, Address, Postal_Code, Phone, Division_ID) VALUES(?, ?, ?, ?, ?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        ps.setString(1, customer.getName());
        ps.setString(2, customer.getAddress());
        ps.setString(3, customer.getPostalCode());
        ps.setString(4, customer.getPhone());
        ps.setInt(5, customer.getDivisionId());

        int rowsAffected = ps.executeUpdate();

        return rowsAffected;
    }

    /***
     * update is a method that passes data from the user interface, and updates an existing appointment in the database.
     * @param appointment
     * @return
     * @throws SQLException
     */
    public static int update(Appointment appointment) throws SQLException {

        String sql = "UPDATE APPOINTMENTS SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        ps.setString(1, appointment.getTitle());
        ps.setString(2, appointment.getDescription());
        ps.setString(3, appointment.getLocation());
        ps.setString(4, appointment.getType());
        ps.setTimestamp(5,appointment.getStart());
        ps.setTimestamp(6, appointment.getEnd());
        ps.setInt(7, appointment.getCustomerId());
        ps.setInt(8, appointment.getUserId());
        ps.setInt(9, appointment.getContactId());
        ps.setInt(10, appointment.getId());

        int rowsAffected = ps.executeUpdate();

        return rowsAffected;
    }

    /***
     * updateRecord is a method that passes data from the user interface, and updates an existing record in the database.
     * @param customer
     * @return
     * @throws SQLException
     */
    public static int updateRecord(Customer customer) throws SQLException {

        String sql = "UPDATE CUSTOMERS SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Division_ID = ? WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        ps.setString(1, customer.getName());
        ps.setString(2, customer.getAddress());
        ps.setString(3, customer.getPostalCode());
        ps.setString(4, customer.getPhone());
        ps.setInt(5, customer.getDivisionId());
        ps.setInt(6, customer.getId());

        int rowsAffected = ps.executeUpdate();

        return rowsAffected;
    }

    /***
     * delete is a method that deletes an appointment in the database based on a given appointment id.
     * @param appointment
     * @return
     * @throws SQLException
     */
    public static int delete(Appointment appointment) throws SQLException {

        String sql = "DELETE FROM APPOINTMENTS WHERE Appointment_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, appointment.getId());

        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    /***
     * deleteRecord is a method that deletes a record in the database based on a given customer id.
     * @param customer
     * @return
     * @throws SQLException
     */
    public static int deleteRecord(Customer customer) throws SQLException {

        String sql = "DELETE FROM CUSTOMERS WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, customer.getId());

        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    /***
     * appointmentAlert is a method that alerts the user, at time of log in, whether there is an appointment within 15
     * minutes or not.
     * @return
     * @throws SQLException
     */
    public static ObservableList<Appointment> appointmentAlert() throws SQLException {

        ObservableList<Appointment> AOL = FXCollections.observableArrayList();

        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd" + " " + "HH:mm");

        LocalDateTime ldt = LocalDateTime.now();
        ZonedDateTime ltz = ldt.atZone(ZoneId.systemDefault());
        ZonedDateTime utc = ltz.withZoneSameInstant(ZoneOffset.UTC);
        ZonedDateTime in15 = utc.plusMinutes(15);


        String START = utc.format(format);
        String END = in15.format(format);
        int USERID = DBClientApplication.Model.User.getId();

        String sql = "SELECT * FROM appointments as a LEFT OUTER JOIN contacts as c ON a.Contact_ID = c.Contact_ID WHERE Start BETWEEN ? AND ? AND User_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        //AppointmentController.AppointmentWeeklyOList.clear();
        ps.setString(1, START);
        ps.setString(2, END);
        ps.setInt(3, USERID);

        ResultSet rs = ps.executeQuery();
            while (rs.next()) {

                int id = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String contact = rs.getString("Contact_Name");
                String type = rs.getString("Type");
                Timestamp start = rs.getTimestamp("Start");
                Timestamp end = rs.getTimestamp("End");
                int customerId = rs.getInt("Customer_ID");
                int userId = rs.getInt("User_ID");

                Appointment appointment = new Appointment(
                        id, title, description, location, contact, type, start, end, customerId, userId);
                AOL.add(appointment);


            }
        return AOL;
    }

    /***
     * sortByType is a method that sorts data from the database by type.
     * @return
     * @throws SQLException
     */
    public static ObservableList sortByType() throws SQLException {

        String sql = "SELECT Type, COUNT(Type) as Total FROM appointments GROUP BY Type";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        String sql2 = "SELECT MONTHNAME(Start) as Month, COUNT(MONTH(Start)) as Total from appointments GROUP BY Month";
        PreparedStatement ps2 = JDBC.connection.prepareStatement(sql2);
        ResultSet rs2 = ps2.executeQuery();

        ObservableList sort = FXCollections.observableArrayList();

        while (rs.next()){

            String type = rs.getString("Type");
            int total = rs.getInt("Total");
            sort.add(type);
            sort.add(total);
        }

        while (rs2.next()){

            String month = rs2.getString("Month");
            int total = rs2.getInt("Total");
            sort.add(month);
            sort.add(total);
        }

        return sort;
    }

    /***
     * sortByMonth is a method that sorts data from the database by month.
     * @return
     * @throws SQLException
     */
    public static ObservableList sortByMonth() throws SQLException {

        String sql = "SELECT MONTHNAME(Start) as Month, COUNT(MONTH(Start)) as Total from appointments GROUP BY Month";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        ObservableList sort = FXCollections.observableArrayList();

        while (rs.next()){

            String month = rs.getString("Month");
            int total = rs.getInt("Total");
            sort.add(month);
            sort.add(total);
        }
        return sort;
    }

    /***
     * customerSchedule is a method that passes an observable list of customer data and returns it ordered by start.
     * @return
     * @throws SQLException
     */
    public static ObservableList customerSchedule() throws SQLException {
        //TODO: create a report for; F.B 'a schedule for each customer in your organization that includes --appointment ID, --title, --type and --description, --start date and time, --end date and time, and --customer ID'
        String sql = "SELECT * FROM appointments as a LEFT OUTER JOIN customers as c ON a.Customer_ID = c.Customer_ID order by Start";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        ObservableList schedule = FXCollections.observableArrayList();

        while (rs.next()){

            String name = rs.getString("Customer_Name");
            int id = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String type = rs.getString("Type");
            String description = rs.getString("Description");
            Timestamp start = rs.getTimestamp("Start");
            Timestamp end = rs.getTimestamp("End");
            int customerId = rs.getInt("Customer_ID");

            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime localTime = start.toLocalDateTime().toLocalTime();
            LocalTime elocalTime = end.toLocalDateTime().toLocalTime();
            LocalDate localDate = start.toLocalDateTime().toLocalDate();
            LocalDate elocalDate = end.toLocalDateTime().toLocalDate();
            LocalDateTime ldt = LocalDateTime.of(localDate, localTime);
            LocalDateTime eldt = LocalDateTime.of(elocalDate, elocalTime);
            ZoneId utcZoneId = ZoneId.of("UTC");
            ZonedDateTime mZDT = ZonedDateTime.of(ldt, utcZoneId);
            ZonedDateTime eMZDT = ZonedDateTime.of(eldt, utcZoneId);

            ZoneId myZoneId = ZoneId.systemDefault();
            ZonedDateTime localZDT = ZonedDateTime.ofInstant(mZDT.toInstant(), myZoneId);// this line converts UTC to local
            ZonedDateTime endLocalZDT = ZonedDateTime.ofInstant(eMZDT.toInstant(), myZoneId);

            schedule.add(name);
            schedule.add(id);
            schedule.add(title);
            schedule.add(type);
            schedule.add(description);
            schedule.add(localZDT.toLocalTime());
            schedule.add(endLocalZDT.toLocalTime());
            schedule.add(customerId);
            schedule.listIterator();
        }
        return schedule;
    }

    /***
     * totalInserts is a method that retrieves the total amount of appointments inserted, using the sql counter index.
     * @return
     * @throws SQLException
     */
    public static ObservableList totalInserts() throws SQLException {

        String sql = "select max(Appointment_ID) from appointments";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        ObservableList totalAppointments = FXCollections.observableArrayList();

        while (rs.next()){

            int total = rs.getInt("max(Appointment_ID)");

            totalAppointments.add(total);
        }
        return totalAppointments;
    }
}
