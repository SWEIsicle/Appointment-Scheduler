package DBClientApplication.Model;

/***
 * First level division class to hold data.
 */
public class FirstLevelDivision {

    private int countryId;

    private int divisionId;

    private String division;

    /***
     * first level division default constructor.
     * @param countryId
     * @param divisionId
     * @param division
     */
    public FirstLevelDivision(int countryId, int divisionId, String division){

        this.countryId = countryId;
        this.divisionId = divisionId;
        this.division = division;
    }

    /***
     * set country id.
     * @param countryId
     */
    public void setCountryId(int countryId){

        this.countryId = countryId;
    }

    /***
     * get country id.
     * @return
     */
    public int getCountryId(){

        return countryId;
    }

    /***
     * set division id.
     * @param divisionId
     */
    public void setDivisionId(int divisionId){

        this.divisionId = divisionId;
    }

    /***
     * get division id.
     * @return
     */
    public int getDivisionId(){

        return divisionId;
    }

    /***
     * set division.
     * @param division
     */
    public void setDivision(String division){

        this.division = division;
    }

    /***
     * get division.
     * @return
     */
    public String getDivision(){

        return division;
    }
}
