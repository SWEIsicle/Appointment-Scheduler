package DBClientApplication.Model;



import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

/***
 * appointment class that holds appointment data.
 */
public class Appointment {

    private int id;

    private String title;

    private String description;

    private String location;

    private String type;

    private Timestamp start;

    private Timestamp end;

    private int customerId;

    private int userId;

    private String contact;

    private int contactId;


    public static Appointment modifiedAppointment;

    /***
     * Appointment default constructor.
     * @param id
     * @param title
     * @param description
     * @param location
     * @param contact
     * @param type
     * @param start
     * @param end
     * @param customerId
     * @param userId
     */
    public Appointment(int id, String title, String description, String location, String contact, String type, Timestamp start, Timestamp end, int customerId, int userId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contact = contact;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customerId = customerId;
        this.userId = userId;
    }

    /***
     * appointment constructor to pass data on the 'AddAppointment' screen.
     * @param title
     * @param description
     * @param location
     * @param contactName
     * @param type
     * @param start
     * @param end
     * @param customerId
     * @param userId
     * @param contactId
     */
    public Appointment(String title, String description, String location, String contactName, String type, LocalDateTime start, LocalDateTime end, int customerId, int userId, int contactId) {
    }

    /***
     * appointment constructor to pass data on the 'UpdateAppointment' screen.
     * @param id
     * @param title
     * @param description
     * @param location
     * @param contactName
     * @param type
     * @param start
     * @param end
     * @param customerId
     * @param userId
     * @param contactId
     */
    public Appointment(int id, String title, String description, String location, String contactName, String type, LocalDateTime start, LocalDateTime end, int customerId, int userId, int contactId) {
    }

    /***
     * sets appointment id.
     * @param id
     */
    public void setId(int  id){

        this.id = id;
    }

    /***
     * gets appointment id.
     * @return
     */
    public int getId(){

        return id;
    }

    /***
     * sets appointment title.
     * @param Title
     */
    public void setTitle(String Title){

        this.title = Title;
    }

    /***
     * gets appointment title.
     * @return
     */
    public String getTitle(){

        return title;
    }

    /***
     * sets appointment location.
     * @param Location
     */
    public void setLocation(String Location){

        this.location = Location;
    }

    /***
     * gets appointment location.
     * @return
     */
    public String getLocation(){

        return location;
    }

    /***
     * sets appointment description.
     * @param Description
     */
    public void setDescription(String Description){

        this.description = Description;
    }

    /***
     * gets appointment description.
     * @return
     */
    public String getDescription(){

        return description;
    }

    /***
     * sets appointment type.
     * @param Type
     */
    public void setType(String Type){

        this.type = Type;
    }

    /***
     * gets appointment type.
     * @return
     */
    public String getType(){

        return type;
    }

    /***
     * sets customer id.
     * @param CustomerId
     */
    public void setCustomerId(int CustomerId){

        this.customerId = CustomerId;
    }

    /***
     * gets customer id.
     * @return
     */
    public int getCustomerId(){

        return customerId;
    }

    /***
     * sets user id.
     * @param UserId
     */
    public void setUserId(int UserId){

        this.userId = UserId;
    }

    /***
     * gets user id.
     * @return
     */
    public int getUserId(){

        return userId;
    }

    /***
     * set appointment start.
     * @param start
     */
    public void setStart(Timestamp start){

        this.start = start;
    }

    /***
     * get appointment start.
     * @return
     */
    public Timestamp getStart() {

        return start;
    }

    /***
     * set appointment end.
     * @param end
     */
    public void setEnd(Timestamp end){

        this.end = end;
    }

    /***
     * get appointments end.
     * @return
     */
    public Timestamp getEnd() {

        return end;
    }

    /***
     * get contact.
     * @return
     */
    public String getContact() {

        return contact;
    }

    /***
     * set contact.
     * @param contact
     */
    public void setContact(String contact) {

        this.contact = contact;
    }

    /***
     * get contact id.
     * @return
     */
    public int getContactId() {

        return contactId;
    }

    /***
     * set contact id.
     * @param contactId
     */
    public void setContactId(int contactId) {

        this.contactId = contactId;
    }
}
