package mauri;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class RegularController {

    @FXML
    Label name_label, network_label, info_txt;


    public void init(Regular regular){
        App a = new App();
        name_label.setText(regular.getName() + " " + regular.getLastName());
        network_label.setText(a.fetchInstById(regular.getSuperKey()).getName());
        ArrayList<Regular> group = a.fetchAllRegularsByGroup(regular.getGroupKey());
        info_txt.setText("You are logged in as Regular: " + regular.getName() + " " + regular.getLastName() +
                "\nLeader of the group is " + a.fetchAdminById(regular.getGroupKey()).getName() +
                "\nThere are " + group.size() + " regulars in this group.");
    }

    public void logout() throws IOException {
        Stage mainWindow = Main.getWindow();
        Parent register = FXMLLoader.load(getClass().getResource("login.fxml"));
        mainWindow.setScene(new Scene(register, 900, 700));
        mainWindow.show();
    }

}
