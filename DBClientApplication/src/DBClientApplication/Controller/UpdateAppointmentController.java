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
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static DBClientApplication.Model.Appointment.modifiedAppointment;

public class UpdateAppointmentController implements Initializable {

    @FXML
    private TextField IdTxt;

    @FXML
    private TextField TitleTxt;

    @FXML
    private TextField DescriptionTxt;

    @FXML
    private TextField LocationTxt;

    @FXML
    private TextField TypeTxt;

    @FXML
    public DatePicker startDP;

    @FXML
    private DatePicker endDP;

    @FXML
    private TextField startTxt;

    @FXML
    private TextField endTxt;

    @FXML
    private TextField CustomerIdTxt;

    @FXML
    private TextField UserIdTxt;

    @FXML
    public ComboBox<String> ContactCB;

    /***
     * onActionCancel takes the user from the current screen to the 'Appointment' screen.
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
     * initialize sets the textfields and datepicker with the LOCAL times and information.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime localTime = LocalTime.parse(modifiedAppointment.getStart().toLocalDateTime().toLocalTime().toString());
        LocalTime elocalTime = LocalTime.parse(modifiedAppointment.getEnd().toLocalDateTime().toLocalTime().toString());
        LocalDate localDate = LocalDate.parse(modifiedAppointment.getStart().toLocalDateTime().toLocalDate().format(dateFormat));
        LocalDate elocalDate = LocalDate.parse(modifiedAppointment.getEnd().toLocalDateTime().toLocalDate().format(dateFormat));
        LocalDateTime ldt = LocalDateTime.of(localDate, localTime);
        LocalDateTime eldt = LocalDateTime.of(elocalDate, elocalTime);
        ZoneId utcZoneId = ZoneId.of("UTC");
        ZonedDateTime mZDT = ZonedDateTime.of(ldt, utcZoneId);
        ZonedDateTime eMZDT = ZonedDateTime.of(eldt, utcZoneId);

        ZoneId myZoneId = ZoneId.systemDefault();
        ZonedDateTime localZDT = ZonedDateTime.ofInstant(mZDT.toInstant(), myZoneId);// this line converts UTC to local
        ZonedDateTime endLocalZDT = ZonedDateTime.ofInstant(eMZDT.toInstant(), myZoneId);

        IdTxt.setText((String.valueOf(modifiedAppointment.getId())));
        TitleTxt.setText((String.valueOf(modifiedAppointment.getTitle())));
        DescriptionTxt.setText((String.valueOf(modifiedAppointment.getDescription())));
        LocationTxt.setText(String.valueOf(modifiedAppointment.getLocation()));
        TypeTxt.setText(String.valueOf(modifiedAppointment.getType()));
        startTxt.setText(String.valueOf(localZDT.toLocalTime().toString()));
        endTxt.setText(String.valueOf(endLocalZDT.toLocalTime().toString()));
        CustomerIdTxt.setText(String.valueOf(modifiedAppointment.getCustomerId()));
        UserIdTxt.setText(String.valueOf(modifiedAppointment.getUserId()));

        startDP.setValue(modifiedAppointment.getStart().toLocalDateTime().toLocalDate());
        ContactCB.setItems(FXCollections.observableArrayList(getData()));

    }

    /***
     * getData creates an object list the hold contact names for the contact combobox.
     * @return
     */
    public static List<String> getData() {

        List<String> options = new ArrayList<>();

        try {

            Connection connection = JDBC.getConnection();

            ResultSet rs = connection.createStatement().executeQuery("SELECT Contact_Name from client_schedule.contacts;");

            while (rs.next()) {
                options.add(rs.getString("Contact_Name"));
            }

            rs.close();
            return options;

        } catch (SQLException ex) {
            return null;
        }
    }

    /***
     * onActionSubmit checks for business hours and customer overlap, then updates the appointment information in the
     * database, before taking the user to 'Appointment' screen.
     * @param submit
     * @throws SQLException
     * @throws IOException
     */
    public void onActionSubmit(ActionEvent submit) throws SQLException, IOException {

        if (ContactCB.getSelectionModel().isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Unselected Error");
            alert.setContentText("Please select a contact before updating");
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

        else {

        PreparedStatement ps2 = JDBC.connection.prepareStatement("SELECT * FROM appointments WHERE Appointment_ID = ?");
        ps2.setInt(1, modifiedAppointment.getId());
        int appointmentId = 0;
        ResultSet rs2 = ps2.executeQuery();

        while (rs2.next()) {
            appointmentId = rs2.getInt("Appointment_ID");
        }

        PreparedStatement ps = JDBC.connection.prepareStatement("SELECT * FROM contacts WHERE Contact_Name = ?");
        ps.setString(1, String.valueOf(ContactCB.getValue()));
        int contactID = 0;
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            contactID = rs.getInt("Contact_ID");
        }

            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm");

            LocalTime localTime = LocalTime.parse(startTxt.getText());
            LocalTime elocalTime = LocalTime.parse(endTxt.getText());
            LocalDate localDate = LocalDate.parse(startDP.getValue().format(dateFormat));

            LocalDateTime ldt = LocalDateTime.of(localDate, localTime);
            LocalDateTime eldt = LocalDateTime.of(localDate, elocalTime);

            ZoneId utcZoneId = ZoneId.of("UTC");
            ZoneId myZoneId = ZoneId.systemDefault();

            ZonedDateTime mZDT = ZonedDateTime.of(ldt, myZoneId);
            ZonedDateTime eMZDT = ZonedDateTime.of(eldt, myZoneId);

            ZonedDateTime lZDT = ZonedDateTime.ofInstant(mZDT.toInstant(), utcZoneId);
            ZonedDateTime eLZDT = ZonedDateTime.ofInstant(eMZDT.toInstant(), utcZoneId);

        int id = appointmentId;
        String title = TitleTxt.getText();
        String description = DescriptionTxt.getText();
        String location = LocationTxt.getText();
        String contactName = String.valueOf(ContactCB.getValue());
        String type = TypeTxt.getText();
        LocalDateTime start = LocalDateTime.from(lZDT);
        LocalDateTime end = LocalDateTime.from(eLZDT);
        int customerId = Integer.parseInt(CustomerIdTxt.getText());
        int userId = Integer.parseInt(UserIdTxt.getText());
        int contactId = contactID;

        Appointment appointment = new Appointment(id, title, description, location, contactName, type, start, end, customerId, userId, contactId);

        appointment.setId(id);
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

        AppointmentQuery.update(appointment);

        Parent exit = FXMLLoader.load(getClass().getResource("/DBClientApplication/View/Appointment.fxml"));
        Scene cancelScene = new Scene(exit);
        Stage cancelStage = (Stage) ((Node) submit.getSource()).getScene().getWindow();
        cancelStage.setScene(cancelScene);
        cancelStage.show();
    }
    }

    /***
     * NoCustomerOverlap is a method that compares a new appointment start and end time to existing start and end times
     * to check for overlap.
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
     * businessHours is a method that compares an appointment start and end time with the companies business hours (EST).
     * @return
     */
    private boolean businessHours() {

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

