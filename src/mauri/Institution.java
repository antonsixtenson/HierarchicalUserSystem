package mauri;

import java.util.ArrayList;

public class Institution {


    protected String name;
    protected String region;
    protected String mail;
    protected int superKey;
    protected int regDate;
    protected UserType type;
    protected ArrayList<Regular> regUsers;
    protected ArrayList<Admin> admins;

    /**
     *
     * @param name Name of Institution
     * @param region Geographic place of Institution
     */


    public Institution(String name, String region, String mail, int regDate){
        this.name = name;
        this.region = region;
        this.mail = mail;
        this.regDate = regDate;
        this.type = UserType.INSTITUTION;
        regUsers = new ArrayList<>();
        admins = new ArrayList<>();
    }

    /*
     * Getters and Setters
     */

    public void setName(String name){
        this.name = name;
    }

    public void setRegion(String region){
        this.region = region;
    }

    public void setMail(String mail) { this.mail = mail; }

    public void setAdmins(ArrayList<Admin> al){
        this.admins = al;
    }

    public void setRegulars(ArrayList<Regular> rl){
        this.regUsers = rl;
    }

    public void setSuperKey(int superKey){
        this.superKey = superKey;
    }

    public String getName(){
        return name;
    }

    public String getRegion(){
        return region;
    }

    public String getMail() { return mail; }

    public int getSuperKey(){
        return superKey;
    }

    public int getRegDate() { return regDate; }

    /**
     *
     * @param user Add regular user to list
     */

    public void addRegular(Regular user){
        regUsers.add(user);
    }

    /**
     *
     * @param admin Add admin user to list
     */

    public void addAdmin(Admin admin){
        admins.add(admin);
    }

    /**
     *
     * @return The full list of Regular users linked (by group key) to Institution
     */

    public ArrayList<Regular> getRegUsers(){
        return regUsers;
    }

    /**
     *
     * @return The full list of Admins linked (by group key) to Institution
     */

    public ArrayList<Admin> getAdmins(){
        return admins;
    }


    @Override
    public String toString() {
        return "Institution{" +
                "name='" + name + '\'' +
                ", region='" + region + '\'' +
                ", mail='" + mail + '\'' +
                ", superKey=" + superKey +
                ", type=" + type +
                ", regUsers=" + regUsers +
                ", admins=" + admins +
                '}';
    }
}
