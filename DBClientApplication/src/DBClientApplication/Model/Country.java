package DBClientApplication.Model;

/***
 * Country class that holds the country data.
 */
public class Country {

    private int Id;

    private String country;

    /***
     * country default constructor.
     * @param Id
     * @param country
     */
    public Country(int Id, String country){

        this.Id = Id;
        this.country = country;
    }

    /***
     * set country id.
     * @param Id
     */
    public void setId(int Id){

        this.Id = Id;
    }

    /***
     * get country id.
     * @return
     */
    public int getId(){

        return Id;
    }

    /***
     * set country.
     * @param country
     */
    public void setCountry(String country){

        this.country = country;
    }

    /***
     * get country.
     * @return
     */
    public String getCountry(){

        return country;
    }
}
