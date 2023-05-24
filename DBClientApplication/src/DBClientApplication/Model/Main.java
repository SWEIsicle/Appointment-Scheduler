package DBClientApplication.Model;

import DBClientApplication.Controller.AppointmentController;
import DBClientApplication.Controller.LogInController;
import DBClientApplication.Helper.AppointmentQuery;
import DBClientApplication.Helper.JDBC;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.ZoneId;

/***
 * Main class that loads up the 'LogIn' form and opens connection, launches args, and finally closes connection on exit.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("/DBClientApplication/View/LogIn.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) throws SQLException {


        JDBC.openConnection(); //to open the connection to the Java DataBase

        launch(args); //Should take place between opening and closing DataBase

        JDBC.closeConnection(); //to close the connection to the Java DataBase

    }

}

