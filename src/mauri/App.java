package mauri;

import java.sql.*;
import java.util.ArrayList;
import java.util.Locale;

public class App {


    /**
     *
     * @return Connection to db test.db
     */

    private Connection connect(){
        String url = "jdbc:sqlite:test.db";
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return conn;
    }

    /*
     * Creates table for Regulars
     */

    private void createRegularsTable() {
        String url = "jdbc:sqlite:test.db";
        String sql = "CREATE TABLE IF NOT EXISTS regulars(" +
                "id INTEGER PRIMARY KEY," +
                "name TEXT," +
                "lastname TEXT," +
                "email TEXT," +
                "age INTEGER," +
                "sex TEXT," +
                "group_key INTEGER," +
                "super_key INTEGER," +
                "usertype INTEGER," +
                "reg_date INTEGER," +
                "salt VARCHAR(16)," +
                "password VARCHAR," +
                "FOREIGN KEY(group_key) REFERENCES admins(id)" +
                "FOREIGN KEY(super_key) REFERENCES institutions(id))";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stm = conn.createStatement()) {
            stm.execute(sql);
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    /*
     * Creates table for Admins
     */

    private void createAdminsTable() {
        String url = "jdbc:sqlite:test.db";
        String sql = "CREATE TABLE IF NOT EXISTS admins(" +
                "id INTEGER PRIMARY KEY," +
                "name TEXT," +
                "lastname TEXT," +
                "email TEXT," +
                "super_key INTEGER," +
                "usertype INTEGER," +
                "reg_date INTEGER," +
                "salt VARCHAR(16)," +
                "password VARCHAR," +
                "FOREIGN KEY(super_key) REFERENCES institutions(id))";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stm = conn.createStatement()) {
            stm.execute(sql);
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    /*
     * Creates table for Institutions
     */

    private void createInstitutionsTable() {
        String url = "jdbc:sqlite:test.db";
        String sql = "CREATE TABLE IF NOT EXISTS institutions(" +
                "id INTEGER PRIMARY KEY," +
                "name TEXT," +
                "region TEXT," +
                "email TEXT," +
                "usertype INTEGER," +
                "reg_date INTEGER," +
                "salt VARCHAR(16)," +
                "password VARCHAR)";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stm = conn.createStatement()) {
            stm.execute(sql);
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Table for admins register requests
     */

    private void createAdminRegisterRequestTable() {
        String url = "jdbc:sqlite:test.db";
        String sql = "CREATE TABLE IF NOT EXISTS adminRegisterRequests(" +
                "id INTEGER PRIMARY KEY," +
                "admin_id INTEGER," +
                "super_key INTEGER," +
                "date INTEGER," +
                "FOREIGN KEY(admin_id) REFERENCES admins(id)" +
                "FOREIGN KEY(super_key) REFERENCES institutions(id))";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stm = conn.createStatement()) {
            stm.execute(sql);
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Table for regulars register requests
     */

    private void createRegularRegisterRequestTable() {
        String url = "jdbc:sqlite:test.db";
        String sql = "CREATE TABLE IF NOT EXISTS regularRegisterRequests(" +
                "id INTEGER PRIMARY KEY," +
                "regular_id INTEGER," +
                "super_key INTEGER," +
                "date INTEGER," +
                "FOREIGN KEY(regular_id) REFERENCES regulars(id)" +
                "FOREIGN KEY(super_key) REFERENCES institutions(id))";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stm = conn.createStatement()) {
            stm.execute(sql);
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }


    /**
     *
     * @param regular Takes user of subclass regular and adds to Database
     */

    public void addRegular(Regular regular, byte[] salt, String password) {
        String sql = "INSERT INTO regulars(name, lastname, email, age, sex, group_key, super_key, usertype, reg_date, salt, password)" +
                     "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = this.connect();
             PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, regular.getName());
            ps.setString(2, regular.getLastName());
            ps.setString(3, regular.getEmail());
            ps.setInt(4, regular.getAge());
            ps.setString(5, regular.getSex());
            ps.setInt(7, -1);
            ps.setInt(7, regular.getSuperKey());
            ps.setInt(8, 1);
            ps.setInt(9, regular.getRegDate());
            ps.setBytes(10, salt);
            ps.setString(11, password);
            ps.executeUpdate();
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     *
     * @param admin Takes user of subclass admin and adds to database
     */

    public void addAdmin(Admin admin, byte[] salt, String password) {
        String sql = "INSERT INTO admins(name, lastname, email, super_key, usertype, reg_date, salt, password)" +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = this.connect();
             PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, admin.getName());
            ps.setString(2, admin.getLastName());
            ps.setString(3, admin.getEmail());
            ps.setInt(4, admin.getSuperKey());
            ps.setInt(5, 2);
            ps.setInt(6, admin.getRegDate());
            ps.setBytes(7, salt);
            ps.setString(8, password);
            ps.executeUpdate();
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     *
     * @param inst Takes institution and adds to database
     */

    public void addInstitution(Institution inst, byte[] salt, String password) {
        String sql = "INSERT INTO institutions(name, region, email, usertype, reg_date, salt, password)" +
                "VALUES(?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = this.connect();
             PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, inst.getName());
            ps.setString(2, inst.getRegion());
            ps.setString(3, inst.getMail());
            ps.setInt(4, 3);
            ps.setInt(5, inst.getRegDate());
            ps.setBytes(6, salt);
            ps.setString(7, password);
            ps.executeUpdate();
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Removes all from table depending on Which usertype
     *
     */

    public void dropAllInTable(UserType type){
        String table = type.toString().toLowerCase(Locale.ROOT)+"s";
        String sql = "DELETE FROM "+table;

        try(Connection conn = this.connect();
            PreparedStatement ps = connect().prepareStatement(sql)){
            ps.executeUpdate();
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Fetch User(Admin or Regular) or Institution ID by Searching for Email
     * in corresponding Table
     * @param type UserType
     * @param email EmailAdress
     * @return Id if found, else -1
     */

    public int fetchIdByEmail(UserType type, String email) throws SQLException{
        String table = type.toString().toLowerCase(Locale.ROOT)+"s";
        try {
            Connection conn = this.connect();
            Statement sta = conn.createStatement();
            String sql = "SELECT id FROM " + table + " WHERE email='" + email + "'";
            ResultSet rs = sta.executeQuery(sql);
            int val = rs.getInt("id");
            conn.close();
            return val;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

    /**
     * Returns full Regular Object if found, else null.
     * @param uid Regular ID
     * @return Regular Object
     */

    public Regular fetchRegularById(int uid) {
        try {
            Connection conn = this.connect();
            Statement sta = conn.createStatement();
            String sql = "SELECT *  FROM regulars WHERE id=" + uid;
            ResultSet rs = sta.executeQuery(sql);
            Regular reg = new Regular(rs.getString("name"), rs.getString("lastname"),
                    rs.getString("email"),
                    rs.getInt("super_key"), rs.getInt("reg_date"),
                    rs.getInt("age"), rs.getString("sex"));
            int group_key = rs.getInt("group_key");
            conn.close();
            reg.setGroupKey(group_key);
            reg.setUid(uid);
            return reg;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Returns full Admin Object if found, else null.
     * @param uid Admin ID
     * @return Admin Object
     */

    public Admin fetchAdminById(int uid) {
        try {
            Connection conn = this.connect();
            Statement sta = conn.createStatement();
            String sql = "SELECT *  FROM admins WHERE id=" + uid;
            ResultSet rs = sta.executeQuery(sql);
            Admin admin = new Admin(rs.getString("name"), rs.getString("lastname"),
                    rs.getString("email"), rs.getInt("super_key"),
                    rs.getInt("reg_date"));
            conn.close();
            admin.setUid(uid);
            return admin;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Returns full Institution Object if found, else null.
     * @param uid Institution ID
     * @return Institution Object
     */

    public Institution fetchInstById(int uid) {
        try {
            Connection conn = this.connect();
            Statement sta = conn.createStatement();
            String sql = "SELECT *  FROM institutions WHERE id=" + uid;
            ResultSet rs = sta.executeQuery(sql);
            Institution inst = new Institution(rs.getString("name"), rs.getString("region"),
                    rs.getString("email"),
                    rs.getInt("reg_date"));
            inst.setSuperKey(rs.getInt("id"));
            conn.close();
            return inst;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Fetches all regulars registered at Institution
     * @param superKey Institution ID
     * @return Arraylist of all regulars (With SuperKey matching Institution ID)
     */

    public ArrayList<Regular> fetchAllRegularsBySuper(int superKey) {
        ArrayList<Regular> regulars = new ArrayList<>();
        try {
            Connection conn = this.connect();
            Statement sta = conn.createStatement();
            String sql = "SELECT id FROM regulars WHERE super_key=" + superKey;
            ResultSet rs = sta.executeQuery(sql);
            while (rs.next()) {
                regulars.add(fetchRegularById(rs.getInt("id")));
            }
            conn.close();
            return regulars;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Returns arraylist of all Regulars based on Admin ID (Group_key)
     * @param groupKey Admin ID
     * @return Arraylist of all Regulars
     */

    public ArrayList<Regular> fetchAllRegularsByGroup(int groupKey) {
        ArrayList<Regular> regulars = new ArrayList<>();
        try {
            Connection conn = this.connect();
            Statement sta = conn.createStatement();
            String sql = "SELECT id FROM regulars WHERE group_key=" + groupKey;
            ResultSet rs = sta.executeQuery(sql);
            while (rs.next()) {
                regulars.add(fetchRegularById(rs.getInt("id")));
            }
            conn.close();
            return regulars;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }


    /**
     * Returns arraylist of all admins based on Institution ID (Super_key)
     * @param superKey Institution ID
     * @return Arraylist of all Admins
     */

    public ArrayList<Admin> fetchAllAdminsBySuper(int superKey) {
        ArrayList<Admin> admins = new ArrayList<>();
        try {
            Connection conn = this.connect();
            Statement sta = conn.createStatement();
            String sql = "SELECT id FROM admins WHERE super_key=" + superKey;
            ResultSet rs = sta.executeQuery(sql);
            while (rs.next()) {
                admins.add(fetchAdminById(rs.getInt("id")));
            }
            conn.close();
            return admins;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }


    /**
     *
     * @return Arraylist of all Institution names
     */

    public ArrayList<String> fetchAllInstitutionNames() {
        ArrayList<String> institutions = new ArrayList<>();
        try {
            Connection conn = this.connect();
            Statement sta = conn.createStatement();
            String sql = "SELECT name FROM institutions";
            ResultSet rs = sta.executeQuery(sql);
            while (rs.next()) {
                institutions.add(rs.getString("name"));
            }
            conn.close();
            return institutions;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     *
     * @param name Name of Institution
     * @return Institution ID
     */

    public int fetchInstIdByName(String name) {
        int value = -1;
        try {
            Connection conn = this.connect();
            Statement sta = conn.createStatement();
            String sql = "SELECT id FROM institutions WHERE name='" + name + "'";
            ResultSet rs = sta.executeQuery(sql);
            value = rs.getInt("id");
            conn.close();
            return value;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return value;
    }

    /**
     *
     * @param type UserType
     * @param uid User ID
     * @return String containing SHA256 encrypted password
     */

    public String fetchPwdById(UserType type, int uid) {
        String table = type.toString().toLowerCase(Locale.ROOT)+"s";
        String pwd = "";
        try {
            Connection conn = this.connect();
            Statement sta = conn.createStatement();
            String sql = "SELECT password FROM " + table + " WHERE id="+uid;
            ResultSet rs = sta.executeQuery(sql);
            pwd = rs.getString("password");
            conn.close();
            return pwd;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     *
     * @param type UserType
     * @param uid User ID
     * @return byte array containing salt used to SHA256 encrypt password
     */

    public byte [] fetchSaltById(UserType type, int uid) {
        String table = type.toString().toLowerCase(Locale.ROOT)+"s";
        byte [] salt;
        try {
            Connection conn = this.connect();
            Statement sta = conn.createStatement();
            String sql = "SELECT salt FROM " + table + " WHERE id="+uid;
            ResultSet rs = sta.executeQuery(sql);
            salt = rs.getBytes("salt");
            conn.close();
            return salt;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Adds Admin to register request table
     * @param admin Admin User
     *
     */

    public void addAdminRegisterRequest(Admin admin) throws SQLException{
        String sql = "INSERT INTO adminRegisterRequests(admin_id, super_key, date)" +
                "VALUES(?, ?, ?)";
        Connection conn = this.connect();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, admin.getUid());
        ps.setInt(2, admin.getSuperKey());
        ps.setInt(3, admin.getRegDate());
        ps.executeUpdate();
    }

    /**
     * Adds regular user to register request table
     * @param regular Regular User
     *
     */

    public void addRegularRegisterRequest(Regular regular) throws SQLException{
        String sql = "INSERT INTO regularRegisterRequests(regular_id, super_key, date)" +
                "VALUES(?, ?, ?)";

        Connection conn = this.connect();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, regular.getUid());
        ps.setInt(2, regular.getSuperKey());
        ps.setInt(3, regular.getRegDate());
        ps.executeUpdate();
    }


    /**
     * Removes User from corresponding table
     * @param type UserType
     * @param uid User ID
     */

    public void dropUser(String type, int uid){
        type = type.toLowerCase(Locale.ROOT)+"s";
        try {
            Connection conn = this.connect();
            Statement sta = conn.createStatement();
            String sql = "DELETE FROM "+type+" WHERE id="+uid;
            sta.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Removes table
     * @param table Table to remove
     */

    public void dropTable(String table){
        table = table.toLowerCase(Locale.ROOT);
        try {
            Connection conn = this.connect();
            Statement sta = conn.createStatement();
            String sql = "DROP TABLE "+table;
            sta.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Searches for user in corresponding table.
     * @param type UserType
     * @param email Email
     * @return True if found, else false
     */

    public boolean searchForEmail(String type, String email){
        type = type.toLowerCase(Locale.ROOT)+"s";
        String str = "";
        try {
            Connection conn = this.connect();
            Statement sta = conn.createStatement();
            String sql = "SELECT email FROM "+type+" WHERE email='"+email+"'";
            ResultSet rs = sta.executeQuery(sql);
            str = rs.getString("email");
            conn.close();
            return !str.isBlank();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return !str.isBlank();
    }

    /**
     * Searches if the user have a pending register request in request table
     * @param type UserType
     * @param uid User ID
     * @return User ID if found, else -1
     */

    public int searchForRequest(String type, int uid){
        type = type.toLowerCase(Locale.ROOT);
        try {
            Connection conn = this.connect();
            Statement sta = conn.createStatement();
            String sql = "SELECT id FROM "+type+"RegisterRequests WHERE "+type+"_id="+uid;
            ResultSet rs = sta.executeQuery(sql);
            int id = rs.getInt("id");
            conn.close();
            return id;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }


    /**
     * Fetches all Admins that have requested to register at Institution
     * @param superKey Institution ID
     * @return Arraylist of all Admins
     */

    public ArrayList<Admin> fetchAllAdminRequests(int superKey){
        ArrayList<Admin> reqs = new ArrayList<>();
        try {
            Connection conn = this.connect();
            Statement sta = conn.createStatement();
            String sql = "SELECT * FROM adminRegisterRequests WHERE super_key="+superKey;
            ResultSet rs = sta.executeQuery(sql);
            while (rs.next()) {
                reqs.add(fetchAdminById(rs.getInt("admin_id")));
            }
            return reqs;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return reqs;
    }

    /**
     * Fetches all Regular users that have requested to register at Institution
     * @param superKey Institution ID
     * @return Arraylist of all users
     */

    public ArrayList<Regular> fetchAllRegularRequests(int superKey){
        ArrayList<Regular> reqs = new ArrayList<>();
        try {
            Connection conn = this.connect();
            Statement sta = conn.createStatement();
            String sql = "SELECT * FROM regularRegisterRequests WHERE super_key="+superKey;
            ResultSet rs = sta.executeQuery(sql);
            while (rs.next()) {
                reqs.add(fetchRegularById(rs.getInt("regular_id")));
            }
            return reqs;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return reqs;
    }

    /**
     * Updates name, lastname and email for Admin
     * @param admin User of type Admin
     */

    public void updateAdminBase(Admin admin){
        String sql = "UPDATE admins SET name = ?, lastname = ?, email = ? WHERE id="+admin.getUid();
        try {
            Connection conn = this.connect();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, admin.getName());
            ps.setString(2, admin.getLastName());
            ps.setString(3, admin.getEmail());
            ps.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    /**
     * Updates name, lastname, email, age, sex and groupkey for regular
     * @param regular User of type Regular
     */

    public void updateRegularBase(Regular regular){
        String sql = "UPDATE regulars SET name = ?, lastname = ?, email = ?, "+
                        "age = ?, sex = ?, group_key = ? WHERE id="+regular.getUid();
        try {
            Connection conn = this.connect();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, regular.getName());
            ps.setString(2, regular.getLastName());
            ps.setString(3, regular.getEmail());
            ps.setInt(4, regular.getAge());
            ps.setString(5, regular.getSex());
            ps.setInt(6, regular.getGroupKey());
            ps.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    /**
     * Removes the user from corresponding request table
     * @param type UserType
     * @param uid User ID.
     */

    public void dropRequest(String type, int uid){
        type = type.toLowerCase(Locale.ROOT);
        try {
            Connection conn = this.connect();
            Statement sta = conn.createStatement();
            String sql = "DELETE FROM "+type+"RegisterRequests WHERE "+type+"_id="+uid;
            sta.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    /**
     *
     * @param superKey Institution ID
     * @param groupKey Admin ID
     * @return Arraylist of all matching regulars
     */

    public ArrayList<Regular> fetchRegularsByGroup(int superKey, int groupKey){
        ArrayList<Regular> rl = new ArrayList<>();
        try {
            Connection conn = this.connect();
            Statement sta = conn.createStatement();
            String sql = "SELECT id FROM regulars WHERE super_key="+superKey + " AND group_key="+groupKey;
            ResultSet rs = sta.executeQuery(sql);
            while (rs.next()) {
                rl.add(fetchRegularById(rs.getInt("id")));
            }
            return rl;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return rl;
    }


    // TODO Clean up App.java, methods can merge and be reused.


    public static void main(String[] args) throws SQLException {
        /*
        // Use to create new database and its tables:

        App a = new App();
        a.createInstitutionsTable();
        a.createAdminsTable();
        a.createRegularsTable();
        a.createAdminRegisterRequestTable();
        a.createRegularRegisterRequestTable();
        */


    }
}
