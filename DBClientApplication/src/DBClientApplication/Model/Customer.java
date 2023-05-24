package DBClientApplication.Model;

/***
 * Customer class to hold customer data.
 */
public class Customer {

    private int Id;

    private String name;

    private String address;

    private String postalCode;

    private String phone;

    private String division;

    private int divisionId;

    private String country;

    private int countryId;


    public static Customer modifiedRecord;

    /***
     * Customer default constructor.
     * @param Id
     * @param name
     * @param address
     * @param postalCode
     * @param phone
     * @param division
     * @param country
     */
    public Customer(int Id, String name, String address, String postalCode, String phone, String division, String country){

        this.Id = Id;
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.division = division;
        this.country = country;
    }

    /***
     * customer 'add' constructor.
     * @param name
     * @param address
     * @param postalCode
     * @param phone
     * @param division
     * @param country
     */
    public Customer(String name, String address, String postalCode, String phone, String division, String country) {
    }

    /***
     * customer 'update' constructor.
     * @param id
     * @param name
     * @param address
     * @param postalCode
     * @param phone
     * @param division
     * @param country
     * @param divisionId
     */
    public Customer(int id, String name, String address, String postalCode, String phone, String division, String country, int divisionId) {
    }

    /***
     * set customer id.
     * @param Id
     */
    public void setId(int Id){

        this.Id = Id;
    }

    /***
     * get customer id.
     * @return
     */
    public int getId(){

        return Id;
    }

    /***
     * set customer name.
     * @param name
     */
    public void setName(String name){

        this.name = name;
    }

    /***
     * get customer name.
     * @return
     */
    public String getName(){

        return name;
    }

    /***
     * set customer address.
     * @param address
     */
    public void setAddress(String address){

        this.address = address;
    }

    /***
     * get customer address.
     * @return
     */
    public String getAddress(){

        return address;
    }

    /***
     * set customer postalCode.
     * @param postalCode
     */
    public void setPostalCode(String postalCode){

        this.postalCode = postalCode;
    }

    /***
     * get customer postalCode.
     * @return
     */
    public String getPostalCode(){

        return postalCode;
    }

    /***
     * set customer phone.
     * @param phone
     */
    public void setPhone(String phone){

        this.phone = phone;
    }

    /***
     * get customer phone.
     * @return
     */
    public String getPhone(){

        return phone;
    }

    /***
     * set customer division.
     * @param division
     */
    public void setDivision(String  division){

        this.division = division;
    }

    /***
     * get customer division.
     * @return
     */
    public String getDivision(){

        return division;
    }

    /***
     * get division id.
     * @return
     */
    public int getDivisionId() {

        return divisionId;
    }

    /***
     * set division id.
     * @param divisionId
     */
    public void setDivisionId(int divisionId) {

        this.divisionId = divisionId;
    }

    /***
     * get country.
     * @return
     */
    public String getCountry() {

        return country;
    }

    /***
     * set country.
     * @param country
     */
    public void setCountry(String country) {

        this.country = country;
    }

    /***
     * set country id.
     * @param countryId
     */
    public void setCountryId(int countryId) {

        this.countryId = countryId;
    }
}
