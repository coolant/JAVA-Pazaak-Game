/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
  * @author Aykut Yilmaz - aykut.yilmaz.u1495@student.hogent.be
 */
public class BoardPlayerController extends VBox implements EventHandler<MouseEvent>  {

    @FXML
    private Label playerName;
    @FXML
    private GridPane cardGridPane;
    @FXML
    private HBox gameDeckBox;
    @FXML
    private HBox btnHBox;
    @FXML
    private Button btnEndTurn;
    @FXML
    private Button btnStand;
    @FXML
    private Button btnForfeit;
    @FXML
    private Label playerScore;

    private MyButton[][] boardCards; 
    private MyButton[] gameDeckCards;
    
    /**
     * Contructor of BoardPlayerController. Load a FXMLfile to construct a gui.
     * maps all the nodes to translate
     * set the player's name
     * and adds his cards to the gameDeck
     * 
     * @param name
     * @param gameDeckCards
     * @param score 
     */
    public BoardPlayerController(String name, List<String> gameDeckCards, int score) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("BoardPlayer.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        
        Map<Node, String> nodesMap = Navigator.mapNodes(
                Arrays.asList(btnEndTurn, btnStand, btnForfeit),
                Arrays.asList("BUTTON_ENDTURN", "BUTTON_STAND", "BUTTON_FORFEIT")
        );
        Navigator.dc.getTextController().translateNodes(nodesMap);
        
        
        playerName.setText(name);
        setScore(score);
        this.gameDeckCards = makeCards(gameDeckCards);
        gameDeckBox.getChildren().addAll(this.gameDeckCards);
        boardCards = new MyButton[3][3];
        
        fillGridPane(boardCards);
    
        
    }

    /**
     * Generate MyButton Objects with Strings of cards
     * 
     * @param cards list with strings of cards
     * @return MyButton[] array of my buttons Labels
     */
    private MyButton[] makeCards(List<String> cards){
        MyButton[] gameDeck = new MyButton[cards.size()];
        int teller = 0;
        for(int i = 0; i < cards.size(); i++){
            gameDeck[i] = new MyButton(cards.get(i));
            gameDeck[i].setOnMouseReleased(this);
        }
        return gameDeck;
    }
    
    /**
     * Fill the cardGripPane with boardcards
     * 
     * @param cards MyButton label cards
     */
    private void fillGridPane(MyButton[][] cards){
        for(int i = 0; i < cards.length; i++){
            for(int j = 0; j < cards.length; j++){
                cards[i][j] = new MyButton("**");
                cardGridPane.add(cards[i][j], i, j);
            }
        }
    }
    
    /**
     * set the score of the player by changing the playerScore Label
     * 
     * @param score 
     */
    private void setScore(int score){
        playerScore.setText(score + "/3");
    }
    /**
     * Ends the turn of the player
     * 
     * @param event 
     */
    @FXML
    private void endTurn(ActionEvent event) {
    }

    /**
     * player stands
     * 
     * @param event 
     */
    @FXML
    private void stand(ActionEvent event) {
    }

    /**
     * player concedes the game
     * 
     * @param event 
     */
    @FXML
    private void forfeit(ActionEvent event) {
        Navigator.loadScreen(new MainMenuController());
    }

    //TODO - gameDeckCards aan boardToevoegen
    /**
     * Adds cards from gamedeck to the boardGrid
     * 
     * @param event 
     */
    @Override
    public void handle(MouseEvent event) {
        System.out.println("add gameDeckCard to board");
    }
    
}
