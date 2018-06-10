/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import domain.DomainController;
import exceptions.InvalidNameException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;


/**
 * FXML Controller class
 *
 * @author Aykut Yilmaz - aykut.yilmaz.u1495@student.hogent.be
 */
public class RegisterPlayerController extends BorderPane {

    @FXML
    private Label lblRegister;
    @FXML
    private Label lblName;
    @FXML
    private Label lblBirthyear;
    @FXML
    private Button btnSubmit;

    @FXML
    private TextField txtName;
    @FXML
    private TextField txtbirthyear;
    @FXML
    private Button btnCancel;

    /**
     * Contructor of RegisterPlayerController. Load a FXMLfile to construct a gui.
     * Translate all nodes
     */
    public RegisterPlayerController() {
        FXMLLoader loader = new FXMLLoader(getClass().
                getResource("RegisterPlayer.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        
        Map<Node, String> nodesMap = Navigator.mapNodes(
                Arrays.asList(lblRegister, lblName, lblBirthyear, btnSubmit, btnCancel),
                Arrays.asList("REGISTER_PLAYER_LABEL", "REGISTER_NAME_LABEL", "REGISTER_BIRTHYEAR_LABEL", "SUBMIT_BUTTON", "CANCEL_BUTTON")
        );
        Navigator.dc.getTextController().translateNodes(nodesMap);
    }
    /**
     * Submit the given name and birthyear
     * 
     * @param event 
     */
    @FXML
    private void submit(ActionEvent event) {
        try {
            String name = txtName.getText();
            int birthyear = Integer.parseInt(txtbirthyear.getText());
            Navigator.dc.registerPlayer(name, birthyear);
            Navigator.loadScreen(new MainMenuController());
        } catch (InvalidNameException ine){
            Alert alert = giveAlert(Navigator.dc.getTextController().translateString("INVALID_NAME_HEADER"), ine.getMessage());
            alert.showAndWait();
        } catch (NullPointerException npe) {
            Alert alert = giveAlert(Navigator.dc.getTextController().translateString("NO_DATA_HEADER"), npe.getMessage());
            alert.showAndWait();
        } catch (IllegalArgumentException ex) {
            Alert alert = giveAlert(Navigator.dc.getTextController().translateString("WRONG_BIRTH_NAME_HEADER"), ex.getMessage());
            alert.showAndWait();
        } catch (SQLException ex) {
            Logger.getLogger(RegisterPlayerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        emptyTextFields();
    }
    
    /**
     * cancel all operations and go back to MainMenu
     * 
     * @param event 
     */
    @FXML
    private void cancel(ActionEvent event) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(Navigator.dc.getTextController().translateString("CANCEL_TITLE"));
        alert.setContentText(Navigator.dc.getTextController().translateString("CONFORMATION_DIALOG"));

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            Navigator.loadScreen(new MainMenuController());
        }
    }
    
     /**
     * Give a error Alert
     * 
     * @param titleMessage title of the alert
     * @param message message of the alert
     * @return Alert
     */
    private Alert giveAlert(String titleMessage, String message){
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(Navigator.dc.getTextController().translateString("ERROR_TITLE"));
        alert.setHeaderText(titleMessage);
        alert.setContentText(message);
        return alert;
    }
    /**
     * Empty the textfields of the gui
     * 
     */
    private void emptyTextFields(){
        txtName.setText("");
        txtbirthyear.setText("");
    }
    
}
