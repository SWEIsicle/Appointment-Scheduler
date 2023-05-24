package DBClientApplication.Controller;

import DBClientApplication.Helper.AppointmentQuery;
import DBClientApplication.Helper.JDBC;
import DBClientApplication.Model.Appointment;
import DBClientApplication.Model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddCustomerController implements Initializable {

    @FXML
    private TextField IdTxt;

    @FXML
    private TextField NameTxt;

    @FXML
    private TextField AddressTxt;

    @FXML
    private TextField PostalCodeTxt;

    @FXML
    private TextField PhoneNumTxt;

    @FXML
    public ComboBox fldCB;

    @FXML
    public ComboBox countryCB;

    /***
     * onActionAddCustomer is a method that adds a new customer to the list and database, then returns the user to 'Records'.
     * @param submit
     * @throws IOException
     * @throws SQLException
     */
    @FXML
    void onActionAddCustomer(ActionEvent submit) throws IOException, SQLException {

        PreparedStatement ps = JDBC.connection.prepareStatement("SELECT * FROM first_level_divisions WHERE Division = ?");

        ps.setString(1, String.valueOf(fldCB.getValue()));
        int divisionID = 0;
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            divisionID = rs.getInt("Division_ID");
        }

        String name = NameTxt.getText();
        String address = AddressTxt.getText();
        String postalCode = PostalCodeTxt.getText();
        String phone = PhoneNumTxt.getText();
        String divisionName = String.valueOf(fldCB.getValue());
        String country = String.valueOf(countryCB.getValue());
        int divisionId = divisionID;

        Customer customer = new Customer(name, address, postalCode, phone, divisionName, country);

        customer.setName(name);
        customer.setAddress(address);
        customer.setPostalCode(postalCode);
        customer.setPhone(phone);
        customer.setDivision(divisionName);
        customer.setCountry(country);
        customer.setDivisionId(divisionId);

        RecordsController.RecordsOList.add(customer);

        AppointmentQuery.insertCustomer(customer);

        Parent exit = FXMLLoader.load(getClass().getResource("/DBClientApplication/View/Records.fxml"));
        Scene cancelScene = new Scene(exit);
        Stage cancelStage = (Stage) ((Node) submit.getSource()).getScene().getWindow();
        cancelStage.setScene(cancelScene);
        cancelStage.show();
    }

    /***
     * onActionCancel take the user from the 'addCustomer' screen to the 'Records' screen.
     * @param cancel
     * @throws IOException
     */
    @FXML
    public void onActionCancel(javafx.event.ActionEvent cancel) throws IOException {

        Parent exit = FXMLLoader.load(getClass().getResource("/DBClientApplication/View/Records.fxml"));
        Scene cancelScene = new Scene(exit);
        Stage cancelStage = (Stage) ((Node) cancel.getSource()).getScene().getWindow();
        cancelStage.setScene(cancelScene);
        cancelStage.show();
    }

    /***
     * getCustomerDivisionData creates an object list that holds the data for 'Division' and is returned when 'options' is called.
     * @return
     */
    public List<String> getCustomerDivisionData() {

        List<String> options = new ArrayList<>();

        try {

            PreparedStatement ps = JDBC.connection.prepareStatement("SELECT * FROM first_level_divisions as a LEFT OUTER JOIN countries as c ON a.Country_ID = c.Country_ID where Country = ?");
            ps.setString(1, String.valueOf(countryCB.getSelectionModel().getSelectedItem()));
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                options.add(rs.getString("Division"));
            }

            rs.close();
            return options;

        } catch (SQLException ex) {
            return null;
        }
    }

    /***
     * getCustomerCountryData creates an object list that holds the data for 'Country' and is returned when 'options' is called.
     * @return
     */
    public static List<String> getCustomerCountryData() {

        List<String> options = new ArrayList<>();

        try {

            Connection connection = JDBC.getConnection();

            ResultSet rs = connection.createStatement().executeQuery("SELECT Country from client_schedule.countries;");

            while (rs.next()) {
                options.add(rs.getString("Country"));
            }

            rs.close();
            return options;

        } catch (SQLException ex) {
            return null;
        }
    }

    /***
     * initialize, upon switching to addCustomerController, will set two comboboxes with data from countries and divisions.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        countryCB.setItems(FXCollections.observableArrayList(getCustomerCountryData()));
        fldCB.setItems(FXCollections.observableArrayList(getCustomerDivisionData()));

    }

    /***
     * method for connecting country decision to division list.
     * @param actionEvent
     */
    public void onActonCountry(ActionEvent actionEvent) {

        fldCB.setItems(FXCollections.observableArrayList(getCustomerDivisionData()));
    }
}
