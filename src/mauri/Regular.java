package mauri;

public class Regular extends User {

    protected int age;
    protected int groupKey;
    protected String sex;

    public Regular(String name, String lastName, String email, int superKey,
                   int regDate, int age, String sex) {
        super(name, lastName, email, superKey, regDate, UserType.REGULAR);
        this.age = age;
        this.sex = sex;
    }

    public Regular(){}

    /*
     * Getters and Setters
     */

    public void setGroupKey(int groupKey) { this.groupKey = groupKey; }

    public void setAge(int age){
        this.age = age;
    }

    public void setSex(String sex){
        this.sex = sex;
    }

    public int getAge(){
        return age;
    }

    public String getSex(){
        return sex;
    }

    public int getGroupKey() { return groupKey; }

    @Override
    public String toString() {
        return "Regular{" +
                "age=" + age +
                ", sex='" + sex + '\'' +
                ", groupKey=" + groupKey +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", superKey=" + superKey +
                ", type=" + type +
                '}';
    }
}
