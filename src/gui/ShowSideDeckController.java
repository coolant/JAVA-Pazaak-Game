/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;

/**
 * FXML Controller class
 *
 * @author Aykut Yilmaz - aykut.yilmaz.u1495@student.hogent.be
 */
public class ShowSideDeckController extends BorderPane implements EventHandler<MouseEvent> {

    @FXML
    private Label playerName;
    @FXML
    private TilePane centerBox;
    @FXML
    private HBox bottomHBox;
    
    private List<MyButton> selectedCards;
        
    /**
     * Contructor of ShowSideDeckController. Load a FXMLfile to construct a gui.
     * Gui show the card in possession of a given player
     * @param player String with player name
     * @param cards Array of MyButtons
     */
    public ShowSideDeckController(String player, MyButton[] cards) {
        FXMLLoader loader = new FXMLLoader(getClass().
                getResource("ShowSideDeck.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        
        playerName.setText(player);
        selectedCards = new ArrayList<>();
        fillPaneWithCards(cards);
    }
    
    /**
     * fill pane with cards
     * 
     * @param cards array of MyButton
     */
    private void fillPaneWithCards(MyButton[] cards){
        for(int i = 0; i < cards.length; i++){
            cards[i].setOnMouseClicked(this);
            centerBox.getChildren().add(cards[i]);
        }
    }
    
    /**
     * get selected Card
     * 
     * @return List of MyButton
     */
    public List<MyButton> getSelectedCards() {
        return selectedCards;
    }

    /**
     * Add/remove cards from the tilepane and remove/add to the HBox
     * 
     * @param event 
     */
    @Override
    public void handle(MouseEvent event) {
       MyButton btn = (MyButton) event.getSource();
        if(btn.getParent().equals(centerBox) && bottomHBox.getChildren().size() < 6){
            selectedCards.add(btn);
            bottomHBox.getChildren().add(btn);
        }else if(btn.getParent().equals(bottomHBox)){
            selectedCards.remove(btn);
            centerBox.getChildren().add(btn);
        }
    }
    
    
   
}
