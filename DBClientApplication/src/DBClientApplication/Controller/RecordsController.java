package DBClientApplication.Controller;

import DBClientApplication.Helper.AppointmentQuery;
import DBClientApplication.Helper.JDBC;
import DBClientApplication.Model.Customer;
import com.mysql.cj.x.protobuf.Mysqlx;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;



public class RecordsController implements Initializable {

    public static ObservableList<Customer> RecordsOList = FXCollections.observableArrayList();

    @FXML
    public TableView<Customer> Records;

    @FXML
    private TableColumn<Customer, String> customerIdCol;

    @FXML
    private TableColumn<Customer, String> customerNameCol;

    @FXML
    private TableColumn<Customer, String> addressCol;

    @FXML
    private TableColumn<Customer, String> postalCodeCol;

    @FXML
    private TableColumn<Customer, String> phoneCol;

    @FXML
    private TableColumn<Customer, String> fldCol;

    @FXML
    private TableColumn<Customer, String> countryCol;

    /***
     * onActionAppointments takes the user from the current screen to the 'Appointment' screen.
     * @param appointments
     * @throws IOException
     */
    @FXML
    void onActionAppointments(ActionEvent appointments) throws IOException {

        Parent exit = FXMLLoader.load(getClass().getResource("/DBClientApplication/View/Appointment.fxml"));
        Scene cancelScene = new Scene(exit);
        Stage cancelStage = (Stage) ((Node) appointments.getSource()).getScene().getWindow();
        cancelStage.setScene(cancelScene);
        cancelStage.show();
    }

    /***
     * onActionAddCustomer takes the user from the current screen to the 'AddCustomer' screen.
     * @param addCustomer
     * @throws IOException
     */
    @FXML
    public void onActionAddCustomer(ActionEvent addCustomer) throws IOException {

        Parent addCust = FXMLLoader.load(getClass().getResource("/DBClientApplication/View/AddCustomer.fxml"));
        Scene cancelScene = new Scene(addCust);
        Stage cancelStage = (Stage) ((Node) addCustomer.getSource()).getScene().getWindow();
        cancelStage.setScene(cancelScene);
        cancelStage.show();
    }

    /***
     * onActionUpdateCustomer checks and passes for a selected record, then takes the user to the' UpdateCustomer' screen.
     * @param updateAppointment
     * @throws IOException
     */
    @FXML
    public void onActionUpdateCustomer(ActionEvent updateAppointment) throws IOException {

        Customer.modifiedRecord = Records.getSelectionModel().getSelectedItem();

        if(Customer.modifiedRecord != null) {

            Parent updateAppt = FXMLLoader.load(getClass().getResource("/DBClientApplication/View/UpdateCustomer.fxml"));
            Scene cancelScene = new Scene(updateAppt);
            Stage cancelStage = (Stage) ((Node) updateAppointment.getSource()).getScene().getWindow();
            cancelStage.setScene(cancelScene);
            cancelStage.show();
        }
        else{

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Unselected Error");
            alert.setContentText("Please select a record before updating");
            alert.showAndWait();
        }
    }

    /***
     * initialize pulls customer records from the database, then sets them to the records observable list.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            Connection connection = JDBC.getConnection();

            ResultSet rs2 = connection.createStatement().executeQuery("SELECT Customer_ID, Customer_Name, Address, Postal_Code, Phone, Division, Country \n" +
                    "FROM customers\n" +
                    "JOIN first_level_divisions\n" +
                    "\tON first_level_divisions.Division_ID = customers.Division_ID\n" +
                    "JOIN countries\n" +
                    "\tON countries.Country_ID = first_level_divisions.Country_ID;");
            RecordsOList.clear();
            while (rs2.next()) {

                        int Id = rs2.getInt("Customer_ID");
                        String name = rs2.getString("Customer_Name");
                        String address = rs2.getString("Address");
                        String postalCode = rs2.getString("Postal_Code");
                        String phone = rs2.getString("Phone");
                        String division = rs2.getString("Division");
                        String country = rs2.getString("Country");

                        Customer customer = new Customer(
                            Id, name, address, postalCode, phone, division, country);
                        RecordsOList.add(customer);
            }

            customerIdCol.setCellValueFactory(new PropertyValueFactory<>("Id"));
            customerNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
            addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
            postalCodeCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
            phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
            fldCol.setCellValueFactory(new PropertyValueFactory<>("division"));
            countryCol.setCellValueFactory(new PropertyValueFactory<>("country"));

            Records.setItems(RecordsOList);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /***
     * onActionDelete checks for a selected record, gets confirmation, then deletes the record before refreshing the page.
     * @param actionEvent
     * @throws SQLException
     * @throws IOException
     */
    public void onActionDelete (ActionEvent actionEvent) throws SQLException, IOException {

        Customer modifiedRecord = Records.getSelectionModel().getSelectedItem();
        if(modifiedRecord != null) {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Warning!");
            alert.setHeaderText("Deleting a customer record will delete all associated appointments.");
            alert.setContentText("Are you sure you want to delete this record?");

            Optional<ButtonType> result = alert.showAndWait();

            if(result.get() == ButtonType.OK) {
                AppointmentQuery.deleteRecord(modifiedRecord);

                //To refresh the page. Needs to be verified once another insert succeeds
                Parent appointment = FXMLLoader.load(getClass().getResource("/DBClientApplication/View/Records.fxml"));
                Scene cancelScene = new Scene(appointment);
                Stage cancelStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                cancelStage.setScene(cancelScene);
                cancelStage.show();
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Unselected Error");
            alert.setContentText("Please select a customer record before deleting");
            alert.showAndWait();
        }
    }

    /***
     * onActionExit takes the user from the current screen to the 'LogIn' form.
     * @param exit
     * @throws IOException
     */
    public void onActionExit(ActionEvent exit) throws IOException {

        Parent addCust = FXMLLoader.load(getClass().getResource("/DBClientApplication/View/LogIn.fxml"));
        Scene cancelScene = new Scene(addCust);
        Stage cancelStage = (Stage) ((Node) exit.getSource()).getScene().getWindow();
        cancelStage.setScene(cancelScene);
        cancelStage.show();
    }
}
