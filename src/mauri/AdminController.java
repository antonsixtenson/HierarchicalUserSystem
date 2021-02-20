package mauri;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class AdminController {

    @FXML
    TextField name_field, lastname_field, email_field, sex_field;
    @FXML
    Spinner<Integer> age_spinner;
    @FXML
    Label network_label, admin_label;
    @FXML
    ListView<String> requests_lst;
    @FXML
    TreeView<String> tree_lst;
    @FXML
    Label error_label;

    private Admin admin;
    private ArrayList<Regular> regReqs;
    private Regular selected;
    private String temp_email;

    public void init(Admin adm){
        App a = new App();
        admin = adm;
        network_label.setText(a.fetchInstById(adm.getSuperKey()).getName());
        admin_label.setText(adm.getName() +  " " + adm.getLastName());
        setRequests();
        fillTree();
    }

    public void setRequests(){
        App a = new App();
        regReqs = a.fetchAllRegularRequests(admin.getSuperKey());
        for(Regular regular : regReqs){
            requests_lst.getItems().add(regular.getLastName() + ", " + regular.getName());
        }

    }

    public void getSelected(){
       selected = regReqs.get(requests_lst.getSelectionModel().getSelectedIndex());
       name_field.setText(selected.getName());
       lastname_field.setText(selected.getLastName());
       email_field.setText(selected.getEmail());
       sex_field.setText(selected.getSex());
       age_spinner.getValueFactory().setValue(selected.getAge());

    }

    public void accept() {
        addRegular();

    }

    /**
     * Denies register request and removes from database
     */

    public void deny(){
        App a = new App();
        a.dropRequest(UserType.REGULAR.toString(), selected.getUid());
        a.dropUser(UserType.REGULAR.toString(), selected.getUid());
        regReqs.remove(requests_lst.getSelectionModel().getSelectedIndex());
        requests_lst.getItems().remove(requests_lst.getSelectionModel().getSelectedIndex());
        getSelected();
    }


    /**
     * Checks so that fields is correctly filled.
     * @return Boolean
     */

    public boolean checkInputs(){
        boolean nameCheck = false;
        boolean lastnameCheck = false;
        boolean emailCheck = false;
        boolean ageCheck = false;
        boolean sexCheck = false;
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
        if(age_spinner.getValue().equals(0)){
            error_label.setText("Please choose a correct age");
        } else {
            ageCheck = true;
        }
        if(CheckInputs.checkIfBlank(sex_field.getText())){
            if(CheckInputs.checkString(sex_field.getText())){
                sexCheck = true;
            } else {
                error_label.setText("Sex contains illegal symbols");
            }
        } else {
            error_label.setText("Please input the right sex");
        }
        if(CheckInputs.checkMail(email_field.getText())){
            try {
                if (a.searchForEmail(UserType.REGULAR.toString(), email_field.getText()) &&
                        (a.searchForRequest(UserType.REGULAR.toString(),
                                a.fetchIdByEmail(UserType.REGULAR, email_field.getText()))) != -1) {
                    emailCheck = true;
                } else {
                    error_label.setText("Email already registered");
                }
            } catch (SQLException e) {
                error_label.setText("An Error Occurred, Contact System Administrator");
                System.out.println(e.getMessage());
            }

        } else {
            error_label.setText("Email not accepted");
        }
        return nameCheck && lastnameCheck && emailCheck && sexCheck && ageCheck;
    }

    /**
     * Updates regulars information and removes the regular from registration requests table.
     * So that regular can login.
     */

    public void addRegular(){
        App a = new App();
        if(checkInputs()){
            try {
                Regular curr_regular = a.fetchRegularById(a.fetchIdByEmail(UserType.REGULAR,
                        selected.getEmail()));
                curr_regular.setName(name_field.getText());
                curr_regular.setLastName(lastname_field.getText());
                curr_regular.setEmail(email_field.getText());
                curr_regular.setSex(sex_field.getText());
                curr_regular.setAge(age_spinner.getValue());
                int group_key = admin.getUid();
                curr_regular.setGroupKey(group_key);
                a.updateRegularBase(curr_regular);
                a.dropRequest(UserType.REGULAR.toString(), curr_regular.getUid());
                regReqs.remove(requests_lst.getSelectionModel().getSelectedIndex());
                requests_lst.getItems().remove(requests_lst.getSelectionModel().getSelectedIndex());
                getSelected();
            } catch (SQLException e) {
                error_label.setText("That email is not registered");
                System.out.println(e.getMessage());
            }
        }
    }

    public void fillTree(){
        App a = new App();
        ArrayList<Regular> regulars = a.fetchAllRegularsByGroup(admin.getUid());
        TreeItem<String> root = new TreeItem<>(admin.getName() +  ", Group: " + admin.getUid());
        for(Regular regular : regulars){
            TreeItem<String> temp = new TreeItem<>(regular.getLastName() + ", " + regular.getName());
            root.getChildren().add(temp);
        }
        tree_lst.setRoot(root);
    }

    public void logout() throws IOException {
        Stage mainWindow = Main.getWindow();
        Parent register = FXMLLoader.load(getClass().getResource("login.fxml"));
        mainWindow.setScene(new Scene(register, 900, 700));
        mainWindow.show();
    }

}
