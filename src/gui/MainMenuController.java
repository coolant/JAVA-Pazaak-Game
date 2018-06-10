/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import domain.DomainController;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;



/**
 * FXML Controller class
 *
  * @author Aykut Yilmaz - aykut.yilmaz.u1495@student.hogent.be
 */
public class MainMenuController extends BorderPane {

    @FXML
    private Label lblTitle;
    @FXML
    private Button btnRegisterPlayer;
    @FXML
    private Button btnPlayGame;
    @FXML
    private Button btnLoadGame;
    @FXML
    private Button btnChanLan;
    
    /**
     * Contructor of MainMenuController. Load a FXMLfile to construct a gui.
     * Translate all nodes in the gui.
     * 
     */
    public MainMenuController() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainMenu.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        
        Map<Node, String> nodesMap = Navigator.mapNodes(
                Arrays.asList(lblTitle, btnRegisterPlayer, btnPlayGame, btnLoadGame, btnChanLan),
                Arrays.asList("MAIN_MENU_TITLE", "REGISTER_PLAYER_BUTTON", "PLAY_GAME_BUTTON", "LOAD_GAME_BUTTON", "CHANGE_LANGUAGE_BUTTON")
        );
        Navigator.dc.getTextController().translateNodes(nodesMap);
        
    }

    /**
     * Navigate to the RegisterPlayerController
     * 
     * @param event 
     */
    @FXML
    private void btnRegisterPlayer(ActionEvent event) {
        Navigator.loadScreen(new RegisterPlayerController());
    }
    /**
     * Navigate to the PlayGameController
     * 
     * @param event 
     */
    @FXML
    private void btnPlayGame(ActionEvent event) {
        Navigator.loadScreen(new ShowPlayersController());
    }

    /**
     * Navigate to the LoadGameController
     * 
     * @param event 
     */
    @FXML
    private void btnLoadGame(ActionEvent event) {
        Navigator.loadScreen(new LoadGameController());
    }

    /**
     * Navigate to the SelectLanguageController
     * 
     * @param event 
     */
    @FXML
    private void changeLanguage(ActionEvent event) {
        Navigator.loadScreen(new SelectLanguageController());
    }
}
