package DBClientApplication.Controller;

import DBClientApplication.Helper.AppointmentQuery;
import DBClientApplication.Helper.JDBC;
import DBClientApplication.Model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.ResourceBundle;

import static DBClientApplication.Model.Appointment.modifiedAppointment;

public class AppointmentController extends UpdateAppointmentController implements Initializable {

    public static ObservableList<Appointment> AppointmentOList = FXCollections.observableArrayList();

    public static ObservableList<Appointment> AppointmentWeeklyOList = FXCollections.observableArrayList();

    @FXML
    public TableView<Appointment> Calendar;

    @FXML
    private TableColumn<Appointment, String> AppointmentId;

    @FXML
    private TableColumn<Appointment, String> Title;

    @FXML
    private TableColumn<Appointment, String> Description;

    @FXML
    private TableColumn<Appointment, String> Location;

    @FXML
    private TableColumn<Appointment, String> Contact;

    @FXML
    private TableColumn<Appointment, String> Type;

    @FXML
    private TableColumn<Appointment, LocalDateTime> StartDateTime;

    @FXML
    private TableColumn<Appointment, LocalDateTime> EndDateTime;

    @FXML
    private TableColumn<Appointment, String> CustomerId;

    @FXML
    private TableColumn<Appointment, String> UserId;

    @FXML
    private ToggleGroup timeStyle;

    @FXML
    private RadioButton MonthlyRBtn;

    @FXML
    private RadioButton WeeklyRBtn;

    @FXML
    private RadioButton ViewAllRBtn;

    /***
     * onActionAddAppointment takes the user from the 'Appointment' screen to the 'AddAppointment" screen.
     * @param addAppointment
     * @throws IOException
     */
    @FXML
    public void onActionAddAppointment(ActionEvent addAppointment) throws IOException {

        Parent addAppt = FXMLLoader.load(getClass().getResource("/DBClientApplication/View/AddAppointment.fxml"));
        Scene cancelScene = new Scene(addAppt);
        Stage cancelStage = (Stage) ((Node) addAppointment.getSource()).getScene().getWindow();
        cancelStage.setScene(cancelScene);
        cancelStage.show();
    }

    /***
     * onActionSignOut takes the user from the current screen and takes them to the 'LogIn' form.
     * @param signOut
     * @throws IOException
     */
    @FXML
    public void onActionSignOut(javafx.event.ActionEvent signOut) throws IOException {

        Parent exit = FXMLLoader.load(getClass().getResource("/DBClientApplication/View/LogIn.fxml"));
        Scene cancelScene = new Scene(exit);
        Stage cancelStage = (Stage) ((Node) signOut.getSource()).getScene().getWindow();
        cancelStage.setScene(cancelScene);
        cancelStage.show();
    }

    /***
     * onActionUpdateAppointment defines a selected appointment in the calendar and takes the user to 'UpdateAppointment' form.
     * @param updateAppointment
     * @throws IOException
     */
    @FXML
    public void onActionUpdateAppointment(ActionEvent updateAppointment) throws IOException {

        modifiedAppointment = Calendar.getSelectionModel().getSelectedItem();

        if(modifiedAppointment != null) {

            Parent updateAppt = FXMLLoader.load(getClass().getResource("/DBClientApplication/View/UpdateAppointment.fxml"));
            Scene cancelScene = new Scene(updateAppt);
            Stage cancelStage = (Stage) ((Node) updateAppointment.getSource()).getScene().getWindow();
            cancelStage.setScene(cancelScene);
            cancelStage.show();
        }
        else{

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(unselected.message(""));
            alert.setContentText(unselectedDescription.message(""));
            alert.showAndWait();
        }

    }

    /***
     * onActionMonthly sets the calendar to appointments within the next month, when the 'Monthly' radio button is selected.
     * @param monthly
     * @throws SQLException
     */
    @FXML
    void onActionMonthly(ActionEvent monthly) throws SQLException {

        MonthlyRBtn.isSelected();
        if (MonthlyRBtn.isSelected()) {
            try {
                Connection connection = JDBC.getConnection();
                ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM appointments as a LEFT OUTER JOIN contacts as c ON a.Contact_ID = c.Contact_ID");
                AppointmentOList.clear();

                ObservableList<Appointment> byMonth = FXCollections.observableArrayList();

                LocalDateTime monthStart = LocalDateTime.now();
                LocalDateTime monthEnd = LocalDateTime.now().plusMonths(1);

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
                    AppointmentOList.add(appointment);
                }



                for (Appointment appointment:AppointmentOList)
                    if (appointment.getEnd().after(Timestamp.valueOf(monthStart)) && appointment.getEnd().before(Timestamp.valueOf(monthEnd))) {
                        byMonth.add(appointment);
                    }

                Calendar.setItems(byMonth);

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    /***
     * onActionWeekly sets the calendar to appointments within the next week, when the 'Weekly' radio button is selected.
     * @param event
     */
    @FXML
    void onActionWeekly(ActionEvent event) {

        WeeklyRBtn.isSelected();
        if (WeeklyRBtn.isSelected()) {
            try {
                Connection connection = JDBC.getConnection();
                ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM appointments as a LEFT OUTER JOIN contacts as c ON a.Contact_ID = c.Contact_ID");
                AppointmentOList.clear();

                ObservableList<Appointment> byWeek = FXCollections.observableArrayList();

                LocalDateTime weekStart = LocalDateTime.now();
                LocalDateTime weekEnd = LocalDateTime.now().plusWeeks(1);

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
                    AppointmentOList.add(appointment);
                }



                for (Appointment appointment:AppointmentOList)
                    if (appointment.getEnd().after(Timestamp.valueOf(weekStart)) && appointment.getEnd().before(Timestamp.valueOf(weekEnd))) {
                        byWeek.add(appointment);
                    }

                Calendar.setItems(byWeek);

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

    }

    /***
     * initialize sets the appointment screen's calendar to the required columns and data grouped by 'ALL'.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        populateAppointments();

        AppointmentId.setCellValueFactory(new PropertyValueFactory<>("id"));
        Title.setCellValueFactory(new PropertyValueFactory<>("title"));
        Description.setCellValueFactory(new PropertyValueFactory<>("description"));
        Location.setCellValueFactory(new PropertyValueFactory<>("location"));
        Contact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        Type.setCellValueFactory(new PropertyValueFactory<>("type"));
        StartDateTime.setCellValueFactory(new PropertyValueFactory<>("start"));
        EndDateTime.setCellValueFactory(new PropertyValueFactory<>("end"));
        CustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        UserId.setCellValueFactory(new PropertyValueFactory<>("userId"));

    }

    /***
     * populateAppointments is a method that provides the data for all appointments currently in the database.
     */
    public void populateAppointments() {
        try {
            Connection connection = JDBC.getConnection();
            ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM appointments as a LEFT OUTER JOIN contacts as c ON a.Contact_ID = c.Contact_ID");
            AppointmentOList.clear();

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
                AppointmentOList.add(appointment);


            }
            Calendar.setItems(AppointmentOList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /***
     * onActionRecords takes the user from the current screen to the 'Records' screen.
     * @param records
     * @throws IOException
     */
    public void onActionRecords (ActionEvent records) throws IOException {

            Parent exit = FXMLLoader.load(getClass().getResource("/DBClientApplication/View/Records.fxml"));
            Scene cancelScene = new Scene(exit);
            Stage cancelStage = (Stage) ((Node) records.getSource()).getScene().getWindow();
            cancelStage.setScene(cancelScene);
            cancelStage.show();
        }
    /***
     * onActionDelete checks for a selected appointment and asks for confirmation with appointment ID before deleting.
     * @param actionEvent
     * @throws SQLException
     * @throws IOException
     */
    public void onActionDelete (ActionEvent actionEvent) throws SQLException, IOException {


            modifiedAppointment = Calendar.getSelectionModel().getSelectedItem();
            if(modifiedAppointment != null) {

                Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
                confirmation.setTitle("Confirmation!");
                confirmation.setHeaderText("Deleting Appointment with ID: " + modifiedAppointment.getId() + " of type: " + modifiedAppointment.getType());
                confirmation.setContentText("Are you sure you want to delete this appointment?");

                Optional<ButtonType> result = confirmation.showAndWait();

                if(result.get() == ButtonType.OK) {
                    AppointmentQuery.delete(modifiedAppointment);

                //To refresh the page
                Parent appointment = FXMLLoader.load(getClass().getResource("/DBClientApplication/View/Appointment.fxml"));
                Scene cancelScene = new Scene(appointment);
                Stage cancelStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                cancelStage.setScene(cancelScene);
                cancelStage.show();
            }}
            else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText(unselected.message(""));
                alert.setContentText(unselectedDescription.message(""));
                alert.showAndWait();

            }

    }

    /***
     * onActionReports takes the user from the current screen to the 'Reports' screen.
     * @param reports
     * @throws IOException
     */
    public void onActionReports(ActionEvent reports) throws IOException {

        Parent exit = FXMLLoader.load(getClass().getResource("/DBClientApplication/View/Reports.fxml"));
        Scene cancelScene = new Scene(exit);
        Stage cancelStage = (Stage) ((Node) reports.getSource()).getScene().getWindow();
        cancelStage.setScene(cancelScene);
        cancelStage.show();
    }

    /***
     * onActionViewAll provides a third toggle button in the toggle group for the default display of all appointments.
     * @param actionEvent
     */
    public void onActionViewAll(ActionEvent actionEvent) {

        ViewAllRBtn.isSelected();
        if (ViewAllRBtn.isSelected()){
            populateAppointments();
        }
    }

    /***
     * Lambda expression 2, used to easily fill current and future error text.
     */
    public interface messageInterface {
        String message(String m);
    }
    messageInterface unselected = (m) -> {m = "Unselected Error"; return m;};
    messageInterface unselectedDescription = (m) -> {m = "Please highlight a selection before submitting."; return m;};

}


