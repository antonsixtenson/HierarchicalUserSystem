package mauri;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;

public class RegisterController {

    @FXML
    ComboBox utype_dropdown, sex_dropdown, network_dropdown;
    @FXML
    TextField name_field, lastname_field, email_field, pwd_field, pwd_conf_field;
    @FXML
    Spinner age_spinner;
    @FXML
    Label error_label;


    private boolean utypeGen = false;
    private boolean sexGen = false;
    private boolean instGen = false;

    /**
     * Sets values for utype_dropdown ComboBox.
     * When values are added, utypeGen is set to true,
     * to not fill will same values every time it is opened.
     */

    public void setUtype_dropdown(){
        UserType [] types = UserType.values();
        if(!utypeGen) {
            for (UserType type : types) {
                if(!type.equals(UserType.INSTITUTION)) {
                    utype_dropdown.getItems().add(type);
                    utypeGen = true;
                }
            }
        }
    }

    /**
     * Sets scene of main window to login.fxml
     */

    public void backToLogin() {
        try {
            Stage mainWindow = Main.getWindow();
            Parent institution = FXMLLoader.load(getClass().getResource("login.fxml"));
            mainWindow.setScene(new Scene(institution, 900, 700));
            mainWindow.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Disables fields depending on what usertype wants to register
     */

    public void utypeListener() {
        if(utype_dropdown.getValue().equals(UserType.ADMIN)){
            sex_dropdown.setDisable(true);
            age_spinner.setDisable(true);
        } else {
            sex_dropdown.setDisable(false);
            age_spinner.setDisable(false);
        }
    }

    /**
     * Called when register button is clicked
     */

    public void register() {
        if(utype_dropdown.getValue().equals(UserType.ADMIN)) {
            registerAdmin();
        } else if(utype_dropdown.getValue().equals(UserType.REGULAR)) {
            registerRegular();
        }
    }

    /**
     * Handles admin registration.
     * Creates a new Admin Object and sends to database,
     * along with generated salt and SHA256 Password.
     * If Error occurred when placing admin to
     * register requests table it will remove admin from
     * admin table so that admin cannot login.
     */

    private void registerAdmin() {

        if(checkFields()) {
            App a = new App();
            int date = Integer.parseInt(java.time.LocalDate.now().toString().replace("-", ""));
            Admin newAdmin = new Admin(name_field.getText(), lastname_field.getText(),
                                    email_field.getText(),
                                    a.fetchInstIdByName(network_dropdown.getValue().toString()),
                                    date);
            byte [] salt = Password.genSalt();
            String sha_pwd = Password.genPassword(salt, pwd_field.getText());
            a.addAdmin(newAdmin, salt, sha_pwd);
            try {
                newAdmin.setUid(a.fetchIdByEmail(UserType.ADMIN, email_field.getText()));
                a.addAdminRegisterRequest(newAdmin);
                backToLogin();
            } catch (SQLException e) {
                a.dropUser(newAdmin.getUserType().toString(), newAdmin.getUid());
                error_label.setText("An Error Occurred, Contact System Administrator");
                System.out.println(e.getMessage());
            }

        }

    }

    /**
     * Handles regular registration in the same way as admin.
     */

    private void registerRegular() {
        boolean sexCheck = false;
        boolean ageCheck = false;
        if(age_spinner.getValue().equals(0)){
            error_label.setText("Please choose a correct age");
        } else {
            ageCheck = true;
        }
        try {
            if (sex_dropdown.getValue().toString() != null) {
                sexCheck = true;
            }
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
            error_label.setText("Please choose a sex");
        }
        if(checkFields() && sexCheck && ageCheck){
            App a = new App();
            int date = Integer.parseInt(java.time.LocalDate.now().toString().replace("-", ""));
            Regular newRegular = new Regular(name_field.getText(), lastname_field.getText(),
                                                email_field.getText(),
                                                a.fetchInstIdByName(network_dropdown.getValue().toString()),
                                                date, Integer.parseInt(age_spinner.getValue().toString()),
                                                sex_dropdown.getValue().toString());
            byte [] salt = Password.genSalt();
            String sha_pwd = Password.genPassword(salt, pwd_field.getText());
            a.addRegular(newRegular, salt, sha_pwd);
            try {
                newRegular.setUid(a.fetchIdByEmail(UserType.REGULAR, email_field.getText()));
                a.addRegularRegisterRequest(newRegular);
                backToLogin();
            } catch (SQLException e) {
                a.dropUser(newRegular.getUserType().toString(), newRegular.getUid());
                error_label.setText("An Error Occurred, Contact System Administrator");
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     *
     * Checks so that all fields are filled properly.
     * Used by both Admin and Regular registration.
     */

    private boolean checkFields(){
        boolean nameCheck = false;
        boolean lastnameCheck = false;
        boolean emailCheck = false;
        boolean passwordCheck = false;
        boolean networkCheck = false;
        App a = new App();
        if(CheckInputs.checkIfBlank(name_field.getText())) {
            if(CheckInputs.checkString(name_field.getText())) {
                nameCheck = true;
            } else {
                error_label.setText("Name contains illegal symbols");
            }
        } else {
            error_label.setText("Name is blank");
        }
        if(CheckInputs.checkIfBlank(lastname_field.getText())) {
            if(CheckInputs.checkString(lastname_field.getText())) {
                lastnameCheck = true;
            } else {
                error_label.setText("Lastname contains illegal symbols");
            }
        } else {
            error_label.setText("Lastname is blank");
        }
        if(CheckInputs.checkMail(email_field.getText())){
            if(!a.searchForEmail(utype_dropdown.getValue().toString(), email_field.getText())){
                emailCheck = true;
            } else {
                error_label.setText("Email already registered");
            }
        } else {
            error_label.setText("Email not accepted");
        }
        if(CheckInputs.checkPasswordLength(pwd_field.getText())){
            if(pwd_field.getText().equals(pwd_conf_field.getText())){
                passwordCheck = true;
            } else {
                error_label.setText("Passwords does not match");
            }
        } else {
            error_label.setText("Password must contain 6 symbols or more");
        }

        try {
            if(network_dropdown.getValue().toString() != null) {
                networkCheck = true;
            }
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
            error_label.setText("Please choose correct Institution");
        }

        return nameCheck && lastnameCheck && emailCheck && passwordCheck && networkCheck;

    }


    /**
     * Fills network_dropdown combobox with the registered institutions
     * and sets instGen to true.
     */

    public void setNetwork_dropdown() {
        if(!instGen) {
            App a = new App();
            ArrayList<String> institutions = a.fetchAllInstitutionNames();
            for(String inst : institutions) {
                network_dropdown.getItems().add(inst);
                instGen = true;
            }
        }
    }


    /**
     * Fills sex_dropdown combobox,
     * and sets sexGen to true.
     */

    public void setSex_dropdown() {
        if(!sexGen) {
            String [] sexes = {"MALE", "FEMALE", "OTHER"};
            for(String sex : sexes) {
                sex_dropdown.getItems().add(sex);
                sexGen = true;
            }
        }
    }


}
