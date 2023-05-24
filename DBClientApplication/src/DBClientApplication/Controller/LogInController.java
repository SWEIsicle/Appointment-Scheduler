package DBClientApplication.Controller;

import DBClientApplication.Helper.AppointmentQuery;
import DBClientApplication.Helper.JDBC;
import DBClientApplication.Model.Appointment;
import DBClientApplication.Model.User;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LogInController<Nat> implements Initializable {

    @FXML
    private Label userName;

    @FXML
    private Label password;

    @FXML
    private PasswordField passwordTxt;

    @FXML
    private PasswordField userNameTxt;

    @FXML
    private Button logIn;

    @FXML
    private Button exit;

    @FXML
    private Label zoneId;

    private ResourceBundle Nat;

    /***
     * LogInController is a constructor that also establishes a language resource bundle.
     */
    public LogInController() {

        Nat = ResourceBundle.getBundle("DBClientApplication/Nat", Locale.getDefault());
    }

    /***
     * onActionLogIn is a method that validates a users username and password, checks for upcoming appointments withing 15 minutes,
     * and assigns the user and ID, before taking the user to the 'Appointments' screen.
     * @param logIn
     * @throws IOException
     * @throws SQLException
     */
    @FXML
    public void onActionLogIn(ActionEvent logIn) throws IOException, SQLException {

        String un = userNameTxt.getText();
        String pw = passwordTxt.getText();
        boolean validUser = AppointmentQuery.logIn(un, pw);

        if (validUser) {
                System.out.println("a match...");

                String fileName = "login_activity.txt";

            ObservableList<Appointment> in15 = AppointmentQuery.appointmentAlert();

                if (!in15.isEmpty()){

                    for (Appointment upcoming : in15 ) {

                        String message = "You have an appointment within 15 minutes.\n" + "AppointmentID: " + upcoming.getId() + "\n" + "Start Date and Time in UTC : " +
                                upcoming.getStart().toString();
                        ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                        Alert invalidInput = new Alert(Alert.AlertType.WARNING, message, clickOkay);
                        invalidInput.showAndWait();
                    }
                }
                else {
                    ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                    Alert invalidInput = new Alert(Alert.AlertType.CONFIRMATION, "No appointments within 15 minutes", clickOkay);
                    invalidInput.showAndWait();
                }

                try {

                    //The following four lines write the log text to a file. Otherwise it will print only to the console.
                    BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true));
                    bw.append("Time of sign on: " + ZonedDateTime.now(ZoneOffset.UTC).toString() + " User: " + un + " has logged in successfully. " + "\n");
                    bw.newLine();
                    bw.flush();
                    bw.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                Parent variable = FXMLLoader.load(getClass().getResource("/DBClientApplication/View/Appointment.fxml"));
                Scene cancelScene = new Scene(variable);
                Stage cancelStage = (Stage) ((Node) logIn.getSource()).getScene().getWindow();
                cancelStage.setScene(cancelScene);
                cancelStage.show();

        } else if (userNameTxt.getText().isEmpty() || !un.matches("test")) {

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText(Nat.getString("error"));
                alert.setContentText(Nat.getString("incorrectUserName"));
                alert.showAndWait();

            try {
                String fileName = "login_activity.txt";
                //The following four lines write the log text to a file. Otherwise it will print only to the console.
                BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true));
                bw.append("Time of sign on: " + ZonedDateTime.now(ZoneOffset.UTC).toString() + " User: " + un + " has failed to login. " + "\n");
                bw.newLine();
                bw.flush();
                bw.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (passwordTxt.getText().isEmpty() || !pw.matches("test")) {

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText(Nat.getString("error"));
                alert.setContentText(Nat.getString("incorrectPassword"));
                alert.showAndWait();

            try {
                String fileName = "login_activity.txt";
                //The following four lines write the log text to a file. Otherwise it will print only to the console.
                BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true));
                bw.append("Time of sign on: " + ZonedDateTime.now(ZoneOffset.UTC).toString() + " User: " + un + " has failed to login. " + "\n");
                bw.newLine();
                bw.flush();
                bw.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /***
     * onActionExit takes the users button input and exits the application.
     * @param exit
     * @throws IOException
     */
    @FXML
    public void onActionExit(ActionEvent exit) throws IOException {

        JDBC.closeConnection();
        System.exit(0);
    }

    /***
     * initialize sets the zone label to the users local zone, as well as grabs the french translations from keywords.
     * @param url
     * @param Nat
     */
    @Override
    public void initialize(URL url, ResourceBundle Nat) {

        zoneId.setText(String.valueOf(ZoneId.systemDefault()));

        //This is the code for loading the language on the login screen
        try {
            Nat = ResourceBundle.getBundle("DBClientApplication/Nat", Locale.getDefault());
            //if (Locale.getDefault().getLanguage().equals("fr")) {

                logIn.setText(Nat.getString("logIn"));
                exit.setText(Nat.getString("cancel"));
                userName.setText(Nat.getString("userName"));
                password.setText(Nat.getString("password"));

            //}
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
