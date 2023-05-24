package DBClientApplication.Controller;

import DBClientApplication.Helper.AppointmentQuery;
import DBClientApplication.Helper.JDBC;
import DBClientApplication.Model.Appointment;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.ChronoLocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import static DBClientApplication.Controller.UpdateAppointmentController.getData;

public class AddAppointmentController implements Initializable {

    @FXML
    private TextField IdTxt;

    @FXML
    public TextField TitleTxt;

    @FXML
    public  TextField DescriptionTxt;

    @FXML
    public  TextField LocationTxt;

    @FXML
    public  TextField TypeTxt;

    @FXML
    private DatePicker startDP;

    @FXML
    private DatePicker endDP;

    @FXML
    private TextField endTxt;

    @FXML
    private TextField startTxt;

    @FXML
    public  TextField CustomerIdTxt;

    @FXML
    public  TextField UserIdTxt;

    @FXML
    public  ComboBox ContactCB;

    /***
     * onActionCancel takes the user from the
     * @param cancel
     * @throws IOException
     */
    public void onActionCancel(ActionEvent cancel) throws IOException {

        Parent exit = FXMLLoader.load(getClass().getResource("/DBClientApplication/View/Appointment.fxml"));
        Scene cancelScene = new Scene(exit);
        Stage cancelStage = (Stage) ((Node) cancel.getSource()).getScene().getWindow();
        cancelStage.setScene(cancelScene);
        cancelStage.show();
    }

    /***
     * onActionAddAppoint checks for business hours and overlapping hours, and if appropriate adds a new appointment.
     * @param addAppointment
     * @throws IOException
     * @throws SQLException
     */
    public void onActionAddAppoint(ActionEvent addAppointment) throws IOException, SQLException {

        if (startTxt.getText().isEmpty() || endTxt.getText().isEmpty() || startDP.getValue() == null || TitleTxt.getText().isEmpty()
                || DescriptionTxt.getText().isEmpty() || LocationTxt.getText().isEmpty() || TypeTxt.getText().isEmpty()){

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Field(s) Unfilled");
            alert.setContentText("Please fill out all time fields, as well as pick a date for the appointment before updating");
            alert.showAndWait();
        }
        if (!businessHours()) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Scheduling Conflict");
            alert.setHeaderText("Appointment can not be outside of business hours");
            alert.setContentText("Business hours are 8am - 10pm EST");
            alert.showAndWait();
        }

        else if (customerOverlap()) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Scheduling Conflict");
            alert.setHeaderText("Customer Overlap");
            alert.setContentText("A customer already has an appointment at that time.");
            alert.showAndWait();
        }

        else{

        PreparedStatement ps = JDBC.connection.prepareStatement("SELECT * FROM contacts WHERE Contact_Name = ?");

        ps.setString(1, String.valueOf(ContactCB.getValue()));
        int contactID = 0;
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            contactID = rs.getInt("Contact_ID");
        }

            try {
                DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd" + " " + "HH:mm");

                LocalTime myLT = LocalTime.parse(startTxt.getText());
                LocalTime emyLT = LocalTime.parse(endTxt.getText());
                LocalDate myLD = startDP.getValue();//confirmed in time webinar

                LocalDateTime myLDT = LocalDateTime.of(myLD, myLT);
                LocalDateTime endLDT = LocalDateTime.of(myLD, emyLT);

                ZoneId myZoneId = ZoneId.systemDefault();
                ZoneId utcZoneId = ZoneId.of("UTC");

                ZonedDateTime myZDT = ZonedDateTime.of(myLDT, myZoneId);
                ZonedDateTime endZDT = ZonedDateTime.of(endLDT, myZoneId);

                ZonedDateTime utcZDT = ZonedDateTime.ofInstant(myZDT.toInstant(), utcZoneId);// this line converts local to UTC
                ZonedDateTime endUtcZDT = ZonedDateTime.ofInstant(endZDT.toInstant(), utcZoneId);// this line converts local to UTC

                String title = TitleTxt.getText();
                String description = DescriptionTxt.getText();
                String location = LocationTxt.getText();
                String contactName = String.valueOf(ContactCB.getValue());
                String type = TypeTxt.getText();
                LocalDateTime start = LocalDateTime.from(utcZDT);
                LocalDateTime end = LocalDateTime.from(endUtcZDT);
                int customerId = Integer.parseInt(CustomerIdTxt.getText());
                int userId = Integer.parseInt(UserIdTxt.getText());
                int contactId = contactID;

                Appointment appointment = new Appointment(title, description, location, contactName, type, start, end, customerId, userId, contactId);

                appointment.setTitle(title);
                appointment.setDescription(description);
                appointment.setLocation(location);
                appointment.setContact(contactName);
                appointment.setType(type);
                appointment.setStart(Timestamp.valueOf(start));
                appointment.setEnd(Timestamp.valueOf(end));
                appointment.setCustomerId(customerId);
                appointment.setUserId(userId);
                appointment.setContactId(contactId);

                AppointmentController.AppointmentOList.add(appointment);

                AppointmentQuery.insert(appointment);

            } catch (NumberFormatException | SQLException e) {
                e.printStackTrace();
            }
            Parent exit = FXMLLoader.load(getClass().getResource("/DBClientApplication/View/Appointment.fxml"));
            Scene cancelScene = new Scene(exit);
            Stage cancelStage = (Stage) ((Node) addAppointment.getSource()).getScene().getWindow();
            cancelStage.setScene(cancelScene);
            cancelStage.show();
        }

    }

    /***
     * noCustomerOverlap compares new appointments times with appointment times in the database to eliminate overlap
     */
    boolean Overlap = false;
    private boolean customerOverlap() throws SQLException {

        PreparedStatement ps = JDBC.connection.prepareStatement("SELECT * FROM appointments WHERE Customer_ID = ?");
        ps.setInt(1, Integer.parseInt(CustomerIdTxt.getText()));
        ResultSet rs = ps.executeQuery();

        Timestamp start;
        Timestamp end;
        while (rs.next()) {
            start = rs.getTimestamp("Start");
            end = rs.getTimestamp("End");
            //old times
            LocalTime LT = LocalTime.from(start.toLocalDateTime().toLocalTime());
            LocalTime eLT = LocalTime.from(end.toLocalDateTime().toLocalTime());
            LocalDate LD = start.toLocalDateTime().toLocalDate();

            LocalDateTime startDT = LocalDateTime.of(LD, LT);
            LocalDateTime endDT = LocalDateTime.of(LD, eLT);

            ZoneId utcZoneId = ZoneId.of("UTC");
            ZonedDateTime mZDT = ZonedDateTime.of(startDT, utcZoneId);
            ZonedDateTime eMZDT = ZonedDateTime.of(endDT, utcZoneId);

            ZoneId myZoneId = ZoneId.systemDefault();
            ZonedDateTime oldStart = ZonedDateTime.ofInstant(mZDT.toInstant(), myZoneId);// this line converts UTC to local
            ZonedDateTime oldEnd = ZonedDateTime.ofInstant(eMZDT.toInstant(), myZoneId);
            //new times
            LocalDate newLD = startDP.getValue();
            LocalDateTime ns = LocalDateTime.of(newLD, LocalTime.parse(startTxt.getText()));
            LocalDateTime ne = LocalDateTime.of(newLD, LocalTime.parse(endTxt.getText()));

            ZonedDateTime newStart = ZonedDateTime.of(ns, ZoneId.systemDefault());
            ZonedDateTime newEnd = ZonedDateTime.of(ne, ZoneId.systemDefault());

            //Good block, prevents matching
            if (oldStart.isEqual(newStart) || oldEnd.isEqual(newEnd)
                    || newStart.isEqual(oldStart) || newEnd.isEqual(oldEnd)) {
                System.out.println("Your date and time matches " + oldStart.toLocalDateTime() + " or " + oldEnd.toLocalDateTime());
                Overlap = true;
                break;
            }

            else if (newStart.isAfter(oldStart) && newStart.isBefore(oldEnd)) {
                System.out.println(newStart.toLocalDateTime() + " is between " + oldStart.toLocalDateTime() + " and " + oldEnd.toLocalDateTime());
                Overlap = true;
                break;
            }

            else if (oldStart.isAfter(newStart) && oldStart.isBefore(newEnd)) {
                System.out.println(newStart.toLocalDateTime() + " is between " + oldStart.toLocalDateTime() + " and " + oldEnd.toLocalDateTime());
                Overlap = true;
                break;
            }

            else if (newEnd.isAfter(oldStart) && newEnd.isBefore(oldEnd)) {
                System.out.println(newStart.toLocalDateTime() + " is between " + oldStart.toLocalDateTime() + " and " + oldEnd.toLocalDateTime());
                Overlap = true;
                break;
            }

            else {
                Overlap = false;
            }

        }
        return Overlap;
    }

    /***
     * When the addAppointment screen is called, this method initializes a combobox with contact information.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ContactCB.setItems(FXCollections.observableArrayList(getData()));
    }

    /***
     * businessHours is a method that compares an appointment start and end time with the companies business hours (EST).
     * @return
     * @throws SQLException
     */
    private boolean businessHours() throws SQLException {

        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd" + " " + "HH:mm");
        LocalDate myLD = startDP.getValue();//confirmed in time webinar
        LocalTime myLT = LocalTime.parse(startTxt.getText());
        LocalDateTime myLDT = LocalDateTime.of(myLD, myLT);
        ZoneId myZoneId = ZoneId.systemDefault();
        ZonedDateTime myZDT = ZonedDateTime.of(myLDT, myZoneId);

        LocalTime endLT = LocalTime.parse(endTxt.getText());
        LocalDateTime endLDT = LocalDateTime.of(myLD, endLT);
        ZonedDateTime endZDT = ZonedDateTime.of(endLDT, myZoneId);

        ZonedDateTime startBusinessHours = ZonedDateTime.of(myLD,LocalTime.of(8,0),
                ZoneId.of("America/New_York"));
        ZonedDateTime endBusinessHours = ZonedDateTime.of(myLD, LocalTime.of(22, 0),
                ZoneId.of("America/New_York"));

        return !(myZDT.isBefore(startBusinessHours) | myZDT.isAfter(endBusinessHours) |
                endZDT.isBefore(startBusinessHours) | endZDT.isAfter(endBusinessHours) |
                myZDT.isAfter(endZDT));
    }
}

