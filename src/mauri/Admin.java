package mauri;

import java.util.ArrayList;

public class Admin extends User{

    /**
     * @param userGroup Stores all the users with the same groupKey value as Admin (ID)
     */

    protected ArrayList<Regular> userGroup;


    public Admin(String name, String lastName, String email, int superKey, int regDate) {
        super(name, lastName, email, superKey, regDate, UserType.ADMIN);
        userGroup = new ArrayList<>();
    }



    public Admin() {}

    /*
    * Getters and Setters
     */


    /**
     * @return The full list of users
     */

    public ArrayList<Regular> getUserGroup(){
        return userGroup;
    }


    /**
     *
     * @param user Takes a user from Regular class and adds to list
     */

    public void addRegular(Regular user){
        userGroup.add(user);
    }


    @Override
    public String toString() {
        return "Admin{" +
                "name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", superKey=" + superKey +
                '}';
    }
}
