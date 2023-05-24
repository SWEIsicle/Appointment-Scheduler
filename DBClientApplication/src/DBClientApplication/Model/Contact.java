package DBClientApplication.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/***
 * Contact class that holds the contact data.
 */
public class Contact {

    private static int Id;

    private String contactName;

    private String email;

    public static ObservableList<Contact> ContactOList = FXCollections.observableArrayList();

    /***
     * contact default constructor.
     * @param Id
     * @param contactName
     * @param email
     */
    public Contact(int Id, String contactName, String email) {

        this.Id = Id;
        this.contactName = contactName;
        this.email = email;
    }

    /***
     * get contact id.
     * @return
     */
    public static int getId() {

        return Id;
    }

    /***
     * set contact id.
     * @param Id
     */
    public void setId(int Id) {

        Id = Id;
    }

    /***
     * get contact name.
     * @return
     */
    public String getContactName() {

        return contactName;
    }

    /***
     * set contact name.
     * @param contactName
     */
    public void setContactName(String contactName) {

        this.contactName = contactName;
    }

    /***
     * get contact email.
     * @return
     */
    public String getEmail() {

        return email;
    }

    /***
     * set contact email.
     * @param email
     */
    public void setEmail(String email) {

        this.email = email;
    }
}
