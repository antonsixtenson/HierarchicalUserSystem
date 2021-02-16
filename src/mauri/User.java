package mauri;

public abstract class User {


    protected int uid;
    protected String name;
    protected String lastName;
    protected String email;
    protected int superKey;
    protected int regDate;
    protected UserType type;


    /**
     *
     * @param name  Users name
     * @param lastName Users lastname
     * @param email  Users email
     * @param superKey References Institution ID
     * @param t Usertype
     */

    /*
     * Constructor
     */

    public User(String name, String lastName, String email, int superKey,
                int regDate, UserType t){
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.superKey = superKey;
        this.regDate = regDate;
        this.type = t;
    }

    /*
     * Default Constructor
     */

    public User(){ }



    /*
     * Getters and setters
     */

    public void setUid(int uid) {this.uid = uid;}

    public void setName(String name){
        this.name = name;
    }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public void setEmail(String email){
        this.email = email;
    }

    public void setSuperKey(int superKey){
        this.superKey = superKey;
    }

    public void setUserType(UserType t){this.type = t;}

    public String getName(){
        return name;
    }

    public String getLastName() { return lastName; }

    public String getEmail(){
        return email;
    }

    public int getSuperKey(){
        return superKey;
    }

    public int getUid() { return uid; }

    public int getRegDate() { return regDate; }

    public UserType getUserType(){return type;}


    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", superKey=" + superKey +
                ", type=" + type +
                '}';
    }
}
