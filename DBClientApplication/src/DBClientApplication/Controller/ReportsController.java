package DBClientApplication.Controller;

import DBClientApplication.Helper.AppointmentQuery;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class ReportsController implements Initializable {

    @FXML
    private TextArea typeArea;

    @FXML
    private TextArea scheduleArea;

    @FXML
    private TextArea totalArea;

    /***
     * initialize contains All three of the lambda expressions.
     * The 1.1 lambda expression creates an interface through functional programming that populates a report on the report screen with appointments sorted by type and month.
     * The 1.2 lambda expression creates an interface through functional programming that populates a report on the report screen with a schedule for each customer sorted by earliest time.
     * The 1.3 lambda expression creates an interface through functional programming that populates a report on the report screen with the total amount of appointments, to insert, have been entered.
     * the 2 lambda expression can be found at the bottom of AppointmentController.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Lambda 1.1
        reportsInterface reportByType = t -> typeArea.appendText(t);
        try {
            reportByType.reportBytype("Report F.A:\n" + "Appointment type, and count; " + AppointmentQuery.sortByType() + "\n" + "Total appointments within a month; " + AppointmentQuery.sortByMonth());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        //Lambda 1.2
        scheduleInterface customerSchedule = s -> scheduleArea.appendText(s);
        try {
            customerSchedule.reportSchedule("Report F.B:\n" + "Customer Schedules;\n" + AppointmentQuery.customerSchedule() + "\n");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        //Lambda 1.3
        totalAppointmentsSheduledInterface totalAppointments = n -> totalArea.appendText(n);
        try {
            totalAppointments.reportTotal("Report F.C:\n" + "Total appointments entered;\n" + AppointmentQuery.totalInserts());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /***
     * onActionAppointments takes the user from the current screen to the 'Appointment' screen.
     * @param appointments
     * @throws IOException
     */
    public void onActionAppointments(ActionEvent appointments) throws IOException {

        Parent exit = FXMLLoader.load(getClass().getResource("/DBClientApplication/View/Appointment.fxml"));
        Scene cancelScene = new Scene(exit);
        Stage cancelStage = (Stage) ((Node) appointments.getSource()).getScene().getWindow();
        cancelStage.setScene(cancelScene);
        cancelStage.show();
    }

    /***
     * a public interface for report F.C: It passes string data and returns a void.
     */
    public interface totalAppointmentsSheduledInterface {
        //TODO: create a report for; F.C 'my own report'

        void reportTotal(String n) throws SQLException;
    }

    /***
     * a public interface for report F.B: It passes string data and returns a void.
     */
    public interface scheduleInterface{
        //TODO: --DONE-- create a report for; F.B 'a schedule for each customer in your organization that includes appointment ID, title, type and description, start date and time, end date and time, and customer ID'

        void reportSchedule(String s) throws SQLException;
    }

    /***
     * a public interface for report F.A: It passes string data and returns a void.
     */
    public interface reportsInterface {
        //TODO:--DONE-- create a report for; F.A 'the total number of customer appointments by type and month'

        void reportBytype(String t) throws SQLException;
    }

    /***
     * onActionSignOut takes the user from the current screen to the 'LogIn' screen.
     * @param event
     * @throws IOException
     */
    @FXML
    void onActionSignOut(ActionEvent event) throws IOException {

        Parent exit = FXMLLoader.load(getClass().getResource("/DBClientApplication/View/LogIn.fxml"));
        Scene cancelScene = new Scene(exit);
        Stage cancelStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        cancelStage.setScene(cancelScene);
        cancelStage.show();
    }
}
