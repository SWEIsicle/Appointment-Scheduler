package DBClientApplication.Controller;

import DBClientApplication.Helper.AppointmentQuery;
import DBClientApplication.Helper.JDBC;
import DBClientApplication.Model.Appointment;
import DBClientApplication.Model.Customer;
import DBClientApplication.Model.FirstLevelDivision;
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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static DBClientApplication.Model.Customer.modifiedRecord;

public class UpdateCustomerController implements Initializable {

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
    public ComboBox<String> fldCB;

    @FXML
    public ComboBox <String> countryCB;

    @FXML
    private Button SubmitBtn;

    @FXML
    private Button ExitBtn;

    @FXML
    private Label zoneId;

    /***
     * onActionCancel takes the user from the current screen to the 'Records' screen.
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
     * initialize sets the text fields and comboboxes with the data from the database.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        IdTxt.setText(String.valueOf(modifiedRecord.getId()));
        NameTxt.setText((String.valueOf(modifiedRecord.getName())));
        AddressTxt.setText((String.valueOf(modifiedRecord.getAddress())));
        PostalCodeTxt.setText(String.valueOf(modifiedRecord.getPostalCode()));
        PhoneNumTxt.setText(String.valueOf(modifiedRecord.getPhone()));

        countryCB.setItems(FXCollections.observableArrayList(AddCustomerController.getCustomerCountryData()));
        fldCB.setItems(FXCollections.observableArrayList(getCustomerDivisionData()));


        fldCB.setPromptText(modifiedRecord.getDivision());
        countryCB.setPromptText(modifiedRecord.getCountry());
    }

    /***
     * getCustomerDivisionData creates an object list that holds the data for 'Division' and is returned when 'options' is called.
     * @return
     */
    public List<String> getCustomerDivisionData() {

        List<String> options = new ArrayList<>();

        try {

            PreparedStatement ps = JDBC.connection.prepareStatement("SELECT * FROM first_level_divisions as a LEFT OUTER JOIN countries as c ON a.Country_ID = c.Country_ID where Country = ?");
            ps.setString(1, countryCB.getSelectionModel().getSelectedItem());
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
     * onActionUpdateRecord is a method that checks for empty inputs and takes the user to 'Records' after updating the database.
     * @param update
     * @throws IOException
     * @throws SQLException
     */
    public void onActionUpdateRecord(ActionEvent update) throws IOException, SQLException {

        if (fldCB.getSelectionModel().isEmpty() || countryCB.getSelectionModel().isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Unselected Error");
            alert.setContentText("Please select both a first level division, as well as a country before updating");
            alert.showAndWait();

        }
        else {

        PreparedStatement ps2 = JDBC.connection.prepareStatement("SELECT * FROM customers WHERE Customer_ID = ?");
        ps2.setInt(1, Customer.modifiedRecord.getId());
        int customerId = 0;
        ResultSet rs2 = ps2.executeQuery();

        while (rs2.next()) {
            customerId = rs2.getInt("Customer_ID");
        }

        PreparedStatement ps = JDBC.connection.prepareStatement("SELECT * FROM first_level_divisions WHERE Division = ?");
        ps.setString(1, String.valueOf(fldCB.getValue()));
        int divisionID = 0;
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            divisionID = rs.getInt("Division_ID");
        }

            int Id = customerId;
            String name = NameTxt.getText();
            String address = AddressTxt.getText();
            String postalCode = PostalCodeTxt.getText();
            String phone = PhoneNumTxt.getText();
            String division = String.valueOf(fldCB.getValue());
            String country = String.valueOf(countryCB.getValue());
            int divisionId = divisionID;

        Customer customer = new Customer(Id, name, address, postalCode, phone, division, country, divisionId);

            customer.setId(Id);
            customer.setName(name);
            customer.setAddress(address);
            customer.setPostalCode(postalCode);
            customer.setPhone(phone);
            customer.setDivision(division);
            customer.setCountry(country);
            customer.setDivisionId(divisionId);

            RecordsController.RecordsOList.add(customer);

            AppointmentQuery.updateRecord(customer);

        Parent exit = FXMLLoader.load(getClass().getResource("/DBClientApplication/View/Records.fxml"));
        Scene cancelScene = new Scene(exit);
        Stage cancelStage = (Stage) ((Node) update.getSource()).getScene().getWindow();
        cancelStage.setScene(cancelScene);
        cancelStage.show();
    }}

    /***
     * method for connecting country decision to division list.
     * @param actionEvent
     */
    public void onActionCountry(ActionEvent actionEvent) {

        fldCB.setItems(FXCollections.observableArrayList(getCustomerDivisionData()));
    }
}
