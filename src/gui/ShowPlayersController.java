/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import domain.DomainController;
import domain.Player;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

/**
 * FXML Controller class
 *
 * @author Aykut Yilmaz - aykut.yilmaz.u1495@student.hogent.be
 */
public class ShowPlayersController extends BorderPane {

    @FXML
    private Label lblPlayers;
    @FXML
    private TableView<Player> playerTable;
    @FXML
    private TableColumn<Player, String> colName;
    @FXML
    private TableColumn<Player, String> colBirthYear;
    @FXML
    private TableColumn<Player, String> colCredits;
    @FXML
    private Button btnSendRight;
    @FXML
    private Button btnSendLeft;
    @FXML
    private Label lblSelectedPlayers;
    @FXML
    private ListView<String> listSelectedPlayers;
    @FXML
    private Button btnSubmit;
    @FXML
    private Button btnCancel;
    
    /**
     * Contructor of ShowPlayersController. Load a FXMLfile to construct a gui.
     * Translate all nodes.
     * Set all players in a Tableview
     */
    public ShowPlayersController() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ShowPlayers.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        
        colName.setText(Navigator.dc.getTextController().translateString("NAME_COLUMN"));
        colBirthYear.setText(Navigator.dc.getTextController().translateString("BIRTHYEAR_COLUMN"));
        colCredits.setText(Navigator.dc.getTextController().translateString("CREDIT_COLUMN"));
        
        Map<Node, String> nodesMap = Navigator.mapNodes(
                Arrays.asList(lblPlayers, lblSelectedPlayers, btnSubmit, btnCancel),
                Arrays.asList("SELECT_PLAYER_TITLE", "SELECTED_PLAYER_LABEL","SUBMIT_BUTTON","CANCEL_BUTTON")
        );
        Navigator.dc.getTextController().translateNodes(nodesMap);
        
        colName.setCellValueFactory(data -> data.getValue().nameProperty());
        colBirthYear.setCellValueFactory(data -> data.getValue().birthYearProp());
        colCredits.setCellValueFactory(data -> data.getValue().creditProp());
        
        playerTable.setItems(Navigator.dc.getPlayerList());
        playerTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        
        
        listSelectedPlayers.setItems(Navigator.dc.getSelectedPlayers());
    }

    /**
     * Add the selected player to the listSelectedPlayers
     * 
     * @param event 
     */
    @FXML
    private void sendRight(ActionEvent event) {
        Player potential = playerTable.getSelectionModel().getSelectedItem();
        if(potential != null){
            Navigator.dc.addSelectedPlayer(potential);
            Navigator.dc.removeShowPlayer(potential);
            btnSendRight.setDisable(checkSize(Navigator.dc.getSelectedPlayers()));
        }
    }

    /**
     * removes selected player from the listSelectedPlayers
     * 
     * @param event 
     */    
    @FXML
    private void sendLeft(ActionEvent event) {
        String potential = listSelectedPlayers.getSelectionModel().getSelectedItem();
        if(potential != null && !potential.isEmpty()) {
            Navigator.dc.removeSelectedPlayer(potential);
            Navigator.dc.addShowPlayer(potential);
            btnSendRight.setDisable(checkSize(Navigator.dc.getSelectedPlayers()));
        }
    }

    /**
     * removes selected player from the listSelectedPlayers by double clicking
     * 
     * @param event 
     */
    @FXML
    private void sendLeftMouseClicked(MouseEvent event) {
        if(event.getClickCount() == 2){
            Navigator.dc.addShowPlayer(listSelectedPlayers.getSelectionModel().getSelectedItem());
            Navigator.dc.removeSelectedPlayer(listSelectedPlayers.getSelectionModel().getSelectedItem());
            btnSendRight.setDisable(checkSize(Navigator.dc.getSelectedPlayers()));
        }
    }

    /**
     * Add the selected player to the listSelectedPlayers by double clicking
     * 
     * @param event 
     */
    @FXML
    private void sendRightMouseClicked(MouseEvent event) {
        if(event.getClickCount() == 2 && !(Navigator.dc.getSelectedPlayers().size() >= 2)){
            Navigator.dc.addSelectedPlayer(playerTable.getSelectionModel().getSelectedItem());
            Navigator.dc.removeShowPlayer(playerTable.getSelectionModel().getSelectedItem());
            btnSendRight.setDisable(checkSize(Navigator.dc.getSelectedPlayers()));
        }
    }
    
    /**
     * check the size of a given list
     * 
     * @param <E>
     * @param list list of Observable Objects
     * @return boolean
     */
    private <E> boolean checkSize(ObservableList<E> list){
        if(list.size() >= 2){
            return true;
        }else{
            return false;
        }
    }

    /**
     * submit when players are selected
     * 
     * @param event 
     */
    @FXML
    private void submit(ActionEvent event) {
        List<String> players = Navigator.dc.getSelectedPlayers().stream().collect(Collectors.toList());
        if(players.size() == 2){
            Navigator.dc.startNewGame(players);
            System.out.println(Navigator.dc.listSideDecklessParticipants());
            Navigator.loadScreen(new CreateSideDeckController(Navigator.dc.listSideDecklessParticipants()));
        }else{
            Alert alert = giveAlert("Not enough", "You have to select 2 players");
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
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(titleMessage);
        alert.setContentText(message);
        return alert;
    }

    /**
     * cancel all operations and go back to MainMenu
     * 
     * @param event 
     */
    @FXML
    private void cancel(ActionEvent event) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Cancel");
        alert.setContentText(Navigator.dc.getTextController().translateString("CONFORMATION_DIALOG"));

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            Navigator.loadScreen(new MainMenuController());
        } else {
            
        }
    }
}
