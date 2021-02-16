package mauri;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;
import java.util.ArrayList;

public class InstitutionController {

    @FXML
    Label network_label, type_label, error_label;
    @FXML
    ListView<String> admin_requests_list;
    @FXML
    ListView<String> regular_requests_list;
    @FXML
    TextField name_field, lastname_field, email_field, sex_field;
    @FXML
    ComboBox<String> groups_dropdown;
    @FXML
    Spinner<Integer> age_spinner;
    @FXML
    Button accept_btn, deny_btn;
    @FXML
    TreeView<String> tree_lst;

    private Institution institution;
    private ArrayList<Admin> adminReqs;
    private ArrayList<Regular> regularReqs;
    private UserType current_type;
    private Admin curr_admin;
    private Regular curr_regular;
    private int admin_index;
    private int regular_index;
    private boolean groupsGen = false;


    /**
     * Called from LoginController if login is successfull.
     * Fetches vital information from Database
     * Sets labels
     * @param inst Institution Object
     */

    public void init(Institution inst){
        App a = new App();
        institution = inst;
        network_label.setText(inst.getName());
        type_label.setText(UserType.INSTITUTION.toString());
        inst.setAdmins(a.fetchAllAdminsBySuper(inst.getSuperKey()));
        inst.setRegulars(a.fetchAllRegularsBySuper(inst.getSuperKey()));

        disableAll();
        setRequests();
        fillTreeView();
    }


    /**
     * Fills the ListViews with Users that wants to register at Institution
     */

    public void setRequests(){
        App a = new App();
        adminReqs = a.fetchAllAdminRequests(institution.getSuperKey());
        regularReqs = a.fetchAllRegularRequests(institution.getSuperKey());
        for(Admin admin : adminReqs){
            admin_requests_list.getItems().add(admin.getLastName() + ", " + admin.getName());
        }
        for(Regular regular : regularReqs){
            regular_requests_list.getItems().add(regular.getLastName() + ", " + regular.getName());
        }
    }

    /**
     * Fills the fields with selected Admins information.
     * Information can then be updated/corrected by Institution and
     * saved to database before registration is accepted.
     */

    public void getSelectedAdmin(){
        current_type = UserType.ADMIN;
        error_label.setText("");
        enableAll();
        sex_field.setDisable(true);
        age_spinner.setDisable(true);
        groups_dropdown.setDisable(true);
        admin_index = admin_requests_list.getSelectionModel().getSelectedIndex();
        curr_admin = adminReqs.get(admin_index);
        name_field.setText(curr_admin.getName());
        lastname_field.setText(curr_admin.getLastName());
        email_field.setText(curr_admin.getEmail());
    }

    /**
     * Fills the fields with selected Regulars information.
     * Information can then be updated/corrected by Institution and
     * saved to database before registration is accepted.
     */

    public void getSelectedRegular(){
        current_type = UserType.REGULAR;
        error_label.setText("");
        enableAll();
        sex_field.setDisable(false);
        age_spinner.setDisable(false);
        groups_dropdown.setDisable(false);
        regular_index = regular_requests_list.getSelectionModel().getSelectedIndex();
        curr_regular = regularReqs.get(regular_index);
        name_field.setText(curr_regular.getName());
        lastname_field.setText(curr_regular.getLastName());
        email_field.setText(curr_regular.getEmail());
        sex_field.setText(curr_regular.getSex());
        age_spinner.getValueFactory().setValue(curr_regular.getAge());
    }


    public void accept(){
        if(current_type.equals(UserType.ADMIN)){
            addAdmin();
        } else if(current_type.equals(UserType.REGULAR)){
            addRegular();
        }
    }

    /**
     * Updates regulars information and removes the regular from registration requests table.
     * So that regular can login.
     */

    public void addRegular(){
        App a = new App();
        boolean emailCheck = false;
        boolean ageCheck = false;
        boolean sexCheck = false;
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
                if (a.searchForEmail(current_type.toString(), email_field.getText()) &&
                        (a.searchForRequest(current_type.toString(),
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
        if(checkInputs() && emailCheck && ageCheck && sexCheck){
            System.out.println(curr_regular.getUid());
            curr_regular.setName(name_field.getText());
            curr_regular.setLastName(lastname_field.getText());
            curr_regular.setEmail(email_field.getText());
            curr_regular.setSex(sex_field.getText());
            curr_regular.setAge(age_spinner.getValue());
            try {
                int key_index = groups_dropdown.getValue().indexOf(":")+2;
                int group_key = Integer.parseInt(groups_dropdown.getValue().substring(key_index));
                curr_regular.setGroupKey(group_key);
                a.updateRegularBase(curr_regular);
                a.dropRequest(current_type.toString(), curr_regular.getUid());
                regular_requests_list.getItems().remove(regular_index);
                regularReqs.remove(regular_index);
                institution.addRegular(curr_regular);
                getSelectedRegular();
            } catch (NullPointerException e){
                error_label.setText("Please choose a group");
                System.out.println(e.getMessage());
            }
        }
    }


    /**
     * Fills groups_dropdown with admin that have been accepted. (There is no
     * pending registration request with that admins ID)
     * TODO Update when Admin is accepted
     */

    public void fillGroupsDropdown(){
        if(!groupsGen) {
            ArrayList<Admin> al = institution.getAdmins();
            App a = new App();
            for (Admin admin : al) {
                if(a.searchForRequest(UserType.ADMIN.toString(), admin.getUid()) == -1) {
                    groups_dropdown.getItems().add(admin.getName() + ", Key: " + admin.getUid());
                }
            }
            groupsGen = true;
        }
    }

    /**
     * Updates Admin information
     * and removes admin from requests table.
     */

    public void addAdmin(){
        App a = new App();
        boolean emailCheck = false;
        if(CheckInputs.checkMail(email_field.getText())){
            try {
                if (a.searchForEmail(current_type.toString(), email_field.getText()) &&
                        (a.searchForRequest(current_type.toString(),
                                a.fetchIdByEmail(UserType.ADMIN, email_field.getText()))) != -1) {
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
        if(checkInputs() && emailCheck){
            curr_admin.setName(name_field.getText());
            curr_admin.setLastName(lastname_field.getText());
            curr_admin.setEmail(email_field.getText());
            a.updateAdminBase(curr_admin);
            a.dropRequest(current_type.toString(), curr_admin.getUid());
            admin_requests_list.getItems().remove(admin_index);
            adminReqs.remove(admin_index);
            institution.addAdmin(curr_admin);
            getSelectedAdmin();
        }
    }

    /**
     * Checks so that fields is correctly filled.
     * @return Boolean
     */

    public boolean checkInputs(){
        boolean nameCheck = false;
        boolean lastnameCheck = false;
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
        return nameCheck && lastnameCheck;
    }

    /**
     * Disables all fields, combobox and spinner
     */

    public void disableAll(){
        name_field.setDisable(true);
        lastname_field.setDisable(true);
        email_field.setDisable(true);
        sex_field.setDisable(true);
        age_spinner.setDisable(true);
        groups_dropdown.setDisable(true);
        accept_btn.setDisable(true);
        deny_btn.setDisable(true);
    }

    /**
     * Enables all fields, combobox and spinner
     */

    public void enableAll(){
        name_field.setDisable(false);
        lastname_field.setDisable(false);
        email_field.setDisable(false);
        sex_field.setDisable(false);
        age_spinner.setDisable(false);
        groups_dropdown.setDisable(false);
        accept_btn.setDisable(false);
        deny_btn.setDisable(false);
    }


    /**
     * Fills TreeView. Institution is root node, Admin are Nodes
     * and Regulars Leaf
     * TODO Update treeView when Admin/Regular registration request is accepted
     */


    public void fillTreeView(){
        ArrayList<Admin> admins = institution.getAdmins();
        ArrayList<Regular> regulars = institution.getRegUsers();
        TreeItem<String> root = new TreeItem<>(institution.getName());
        App a = new App();
        for(Admin admin : admins){
            if(a.searchForRequest(UserType.ADMIN.toString(), admin.getUid()) == -1){
                TreeItem<String> adminTemp = new TreeItem<>(admin.getLastName() + ", " +
                        admin.getName() + ", Key: " + admin.getUid());
                for(Regular regular : regulars){
                    if(regular.getGroupKey() == admin.getUid()){
                        TreeItem<String> regTemp = new TreeItem<>(regular.getLastName() + ", " +
                                regular.getName());
                        adminTemp.getChildren().add(regTemp);
                    }
                }
                root.getChildren().add(adminTemp);
            }
        }
        tree_lst.setRoot(root);
    }

    /**
     * Logs out Institution
     * TODO
     */

    public void logout(){
        System.out.println("Logging out");
    }

    /**
     * Denies registration request.
     * TODO
     */

    public void denyRequest(){
        System.out.println("DENIED");
    }

}
