/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import domain.Game;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
/**
 * FXML Controller class
 *
  * @author Aykut Yilmaz - aykut.yilmaz.u1495@student.hogent.be
 */
public class LoadGameController extends BorderPane implements EventHandler<ActionEvent> {

    @FXML
    private Label lblLoadGame;
    @FXML
    private TableView<Game> tableViewGames;
    @FXML
    private TableColumn<Game, String> gameNameColumn;
    @FXML
    private TableColumn<Game, String> playersColumn;
    @FXML
    private TableColumn<Game, String> scoreColumn;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnSubmit;
    @FXML
    private ComboBox<String> playerComboBox;

    private String speler;
    
    /**
     * Contructor of LoadGameController. Load a FXMLfile to construct a gui.
     * Add all players to the comboBox
     * Translate the nodes and column titles.
     */
    public LoadGameController() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LoadGame.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        
        gameNameColumn.setCellValueFactory(data -> data.getValue().nameProp());
        playersColumn.setCellValueFactory(data -> data.getValue().playerProp());
        scoreColumn.setCellValueFactory(data -> data.getValue().scoreProp());
        
        playerComboBox.setItems(FXCollections.observableArrayList(Navigator.dc.listAvailablePlayers()));
             
        playerComboBox.setOnAction(this);
        
        Map<Node, String> nodesMap = Navigator.mapNodes(
            Arrays.asList(lblLoadGame, btnCancel, btnSubmit),
            Arrays.asList("LOADGAME_LABEL", "CANCEL_BUTTON" ,"SUBMIT_BUTTON")
        );
        
        gameNameColumn.setText(Navigator.dc.getTextController().translateString("GAMENAME_COLUMN"));
        playersColumn.setText(Navigator.dc.getTextController().translateString("PLAYERNAME_COLUMN"));
        scoreColumn.setText(Navigator.dc.getTextController().translateString("SCORE_COLUMN"));
        
    }
    /**
     * cancel all operations and go back to MainMenu
     * 
     * @param event 
     */
    @FXML
    private void cancel(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(Navigator.dc.getTextController().translateString("CANCEL_TITLE"));
        alert.setContentText(Navigator.dc.getTextController().translateString("CONFORMATION_DIALOG"));

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            Navigator.loadScreen(new MainMenuController());
        } else {
            
        }
    }

    /**
     * Submit the selected game
     * 
     * @param event 
     */
    @FXML
    private void submit(ActionEvent event) {
        String gameName = tableViewGames.getSelectionModel().getSelectedItem().getGameName();
        if(gameName == null){
            Alert alert = giveAlert(Navigator.dc.getTextController().translateString("SELECT_GAME_TITLE"), Navigator.dc.getTextController().translateString("SELECT_GAME_MESSAGE"));
            alert.showAndWait();
        }else{
            Navigator.dc.continuePreviousGame(gameName);
            Navigator.loadScreen(new PlayGameController());
        }
    }
    /**
     * Add inforamtion to the tableView of a given player
     * 
     * @param player String with a player name
     * @throws SQLException 
     */
    private void setTable(String player) throws SQLException{
        tableViewGames.setItems(Navigator.dc.getGamesList(player));
    }
    /**
     * Grabs selected item of the combobox and setTable
     * 
     * @param event 
     */
    @Override
    public void handle(ActionEvent event) {
        String name = playerComboBox.getSelectionModel().getSelectedItem();
        try {
            if(!name.isEmpty()){
                 setTable(name);
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoadGameController.class.getName()).log(Level.SEVERE, null, ex);
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
        alert.setTitle(Navigator.dc.getTextController().translateString("ERROR_TITLE"));
        alert.setHeaderText(titleMessage);
        alert.setContentText(message);
        return alert;
    }
    
}
