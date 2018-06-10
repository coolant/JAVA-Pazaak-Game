/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import domain.Game;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
  * @author Aykut Yilmaz - aykut.yilmaz.u1495@student.hogent.be
 */
public class PlayGameController extends VBox {

    @FXML
    private Label lblPlayGame;
    @FXML
    private HBox centerBox;
    
    BoardPlayerController[] boardPlayerControllers;

    /**
     * Contructor of PlayGameController. Load a FXMLfile to construct a gui.
     * Translates all nodes in the gui.
     * Hold the BoardPlayerControllers
     */
    public PlayGameController() {
        FXMLLoader loader = new FXMLLoader(getClass().
                getResource("PlayGame.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        
        List<String> players = Navigator.dc.listActiveParticipants();
        
        boardPlayerControllers = new BoardPlayerController[players.size()];
        
        for(int i = 0; i < players.size(); i++){
            List<String> kaarten = new ArrayList<>();
            for(int j = 0; j < (Navigator.dc.getGameDecks().size()/2); j++){
                kaarten.add(Navigator.dc.getGameDecks().get(j + 4*i));
            }
            boardPlayerControllers[i] = new BoardPlayerController(players.get(i), kaarten, Navigator.dc.listParticipantScores().get(i));
            centerBox.getChildren().add(boardPlayerControllers[i]);
        }
        
        lblPlayGame.setText(Navigator.dc.getTextController().translateString("PLAYGAME_TITLE"));
    }
    
}
