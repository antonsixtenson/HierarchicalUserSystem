package mauri;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private TextField email_field, pwd_field;
    @FXML
    private ComboBox utype_dropdown;
    @FXML
    private Label error_label;


    private boolean utypeGen = false;


    /**
     * Checks so that Institution is registered.
     * Checks if password matches and if so
     * Logs in Institution
     */


    public void loginInst() {
        App a = new App();
        try {
            int inst_id = a.fetchIdByEmail(UserType.INSTITUTION, email_field.getText());
            if(inst_id != -1){
                byte [] salt = a.fetchSaltById(UserType.INSTITUTION, inst_id);
                String password = a.fetchPwdById(UserType.INSTITUTION, inst_id);
                if(Password.genPassword(salt, pwd_field.getText()).equals(password)){
                    try {
                        Institution  inst = a.fetchInstById(inst_id);
                        inst.setSuperKey(inst_id);
                        Stage mainWindow = Main.getWindow();
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("institution.fxml"));
                        mainWindow.setScene(new Scene(loader.load()));
                        InstitutionController ic = loader.getController();
                        ic.init(inst);
                        mainWindow.show();
                    } catch (IOException e){
                        System.out.println(e.getMessage());
                    }
                } else {
                    error_label.setText("Incorrect Password");
                }
            } else {
                error_label.setText("Email not registered");
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
            error_label.setText("An error occurred, please contact system administrator");
        }
    }


    public void loginAdmin(){
        App a = new App();
        try {
            int admin_id = a.fetchIdByEmail(UserType.ADMIN, email_field.getText());
            if(admin_id != -1){
                byte [] salt = a.fetchSaltById(UserType.ADMIN, admin_id);
                String password = a.fetchPwdById(UserType.ADMIN, admin_id);
                if(Password.genPassword(salt, pwd_field.getText()).equals(password)){
                    try {
                        Admin admin = a.fetchAdminById(admin_id);
                        Stage mainWindow = Main.getWindow();
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("admin.fxml"));
                        mainWindow.setScene(new Scene(loader.load()));
                        AdminController ac = loader.getController();
                        ac.init(admin);
                        mainWindow.show();
                    } catch (IOException e){
                        System.out.println(e.getMessage());
                    }
                } else {
                    error_label.setText("Incorrect Password");
                }
            } else {
                error_label.setText("Email not registered");
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
            error_label.setText("An error occurred, please contact system administrator");
        }
    }

    /**
     * Handles login request depending on UserType.
     *
     */

    public void handleLoginRequest(){
        App a = new App();
        try {
            if (a.searchForEmail(utype_dropdown.getValue().toString(), email_field.getText())) {
                if (utype_dropdown.getValue().equals(UserType.INSTITUTION)) {
                    error_label.setText("");
                    loginInst();
                } else if (utype_dropdown.getValue().equals(UserType.ADMIN) && checkRegisterStatus()) {
                    error_label.setText("");
                    loginAdmin();
                } else if (utype_dropdown.getValue().equals(UserType.REGULAR) && checkRegisterStatus()) {
                    error_label.setText("");
                    System.out.println("Regular Success");
                }
            } else {
                error_label.setText("Email is not registered");
            }
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
            error_label.setText("Please choose the correct UserType");
        }
    }

    /**
     * Checks if Users email is linked to a pending registration requests.
     * If so, login is denied until Institution accepts register request.
     * @return Boolean
     */

    public boolean checkRegisterStatus(){
        boolean clear = false;
        App a = new App();
        if(utype_dropdown.getValue().equals(UserType.ADMIN)){
            try {
                int uid = a.fetchIdByEmail(UserType.ADMIN, email_field.getText());
                if(a.searchForRequest(UserType.ADMIN.toString(), uid) != -1){
                    error_label.setText("Your registration request have not yet been handled");
                } else {
                    clear = true;
                }
            } catch (SQLException e) {
                error_label.setText("An error occurred, contact system Administrator");
                System.out.println(e.getMessage());
            }
        } else if(utype_dropdown.getValue().equals(UserType.REGULAR)){
            try {
                int uid = a.fetchIdByEmail(UserType.REGULAR, email_field.getText());
                if(a.searchForRequest(UserType.REGULAR.toString(), uid) == -1){
                    error_label.setText("Your registration request have not yet been handled");
                } else {
                    clear = true;
                }
            } catch (SQLException e) {
                error_label.setText("An error occurred, contact system Administrator");
                System.out.println(e.getMessage());
            }
        }
        return clear;
    }

    /**
     * Sets Scene of main Window to register.fxml
     */

    public void register() throws IOException{
        Stage mainWindow = Main.getWindow();
        Parent register = FXMLLoader.load(getClass().getResource("register.fxml"));
        mainWindow.setScene(new Scene(register, 900, 700));
        mainWindow.show();
    }

    /**
     * Fills UserType Dropdown and sets
     * utypeGen to true, to prevent doublets.
     */

    public void setUtype_dropdown(){
        UserType [] types = UserType.values();
        if(!utypeGen) {
            for (UserType type : types) {
                utype_dropdown.getItems().add(type);
                utypeGen = true;
            }
        }
    }
}
