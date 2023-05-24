package DBClientApplication.Model;

/***
 * User class to hold user data.
 */
public class User {

    private static int Id;

    private String UserName;

    private String Password;

    /***
     * user default constructor.
     * @param Id
     * @param UserName
     * @param Password
     */
    public User(int Id, String UserName, String Password){

        this.Id = Id;
        this.UserName = UserName;
        this.Password = Password;
    }

    /***
     * user constructor.
     */
    public User() {

    }

    /***
     * set user id.
     * @param Id
     */
    public void setId(int Id){

        this.Id = Id;
    }

    /***
     * get user id.
     * @return
     */
    public static int getId(){

        return Id;
    }

    /***
     * set username.
     * @param userName
     */
    public void setUserName(String userName){

        this.UserName = UserName;
    }

    /***
     * get username.
     * @return
     */
    public String getUserName(){

        return UserName;
    }

    /***
     * set password.
     * @param password
     */
    public void setPassword(String password){

        this.Password = Password;
    }

    /***
     * get password.
     * @return
     */
    public String getPassword(){

        return Password;
    }
}
