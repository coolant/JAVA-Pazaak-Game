/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;


import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;


/**
 * FXML Controller class
 *
  * @author Aykut Yilmaz - aykut.yilmaz.u1495@student.hogent.be
 */
public class CreateSideDeckController extends BorderPane{

    @FXML
    private HBox centerBox;
    @FXML
    private ListView<String> listviewSelected;
    private MyButton[] cards;
    private int teller = 0;
    @FXML
    private Button btnSubmit;
    
    private final ShowSideDeckController[] showSideDeckController;
    
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnPrevious;
    
    /**
     * Contructor of CreateSideDeckController. Load a FXMLfile to construct a gui.
     * Initialize a listview with the selected players.
     * Make for each player a ShowSideDeckController
     * 
     * @param sideDeckLessParts 
     */
    public CreateSideDeckController(List<String> sideDeckLessParts) {
         FXMLLoader loader = new FXMLLoader(getClass().
                getResource("CreateSideDeck.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        
        showSideDeckController = makeShowSideDeckController(sideDeckLessParts);
        listviewSelected.setItems(FXCollections.observableArrayList(sideDeckLessParts));
        listviewSelected.getSelectionModel().select(teller);
        selectSideDeckController();
        
        btnPrevious.setDisable(true);
        
        Map<Node, String> nodesMap = Navigator.mapNodes(
                Arrays.asList(btnSubmit, btnPrevious, btnCancel),
                Arrays.asList("SUBMIT_BUTTON", "PREVIOUS_BUTTON","CANCEL_BUTTON" )
        );
        Navigator.dc.getTextController().translateNodes(nodesMap);
    }
    
    /**
     * make ShowSideDeckControllers for the amount of players
     * 
     * @param participants list with string of participants
     * @return Array of ShowSideDeckControllers
     */
    private ShowSideDeckController[] makeShowSideDeckController(List<String> participants){
        ShowSideDeckController[] controller = new ShowSideDeckController[participants.size()];
        for(int i = 0; i < participants.size(); i++){
            controller[i] = new ShowSideDeckController(participants.get(i),
                    fillCards(Navigator.dc.listCardsInPossessionOfPlayer(participants.get(i))));
        }
        return controller;
    }
    
    /**
     * Show the current ShowSideDeckController
     * 
     */
    private void selectSideDeckController(){
        int selectedItemNumber = listviewSelected.getSelectionModel().getSelectedIndex();
        centerBox.getChildren().clear();
        centerBox.getChildren().add(showSideDeckController[selectedItemNumber]);
    }
   
    /**
     *  Generate MyButton Objects with Strings of cards
     * 
     * @param cardList list with card Strings
     * @return MyButton[] array of my buttons Labels
     */
    private MyButton[] fillCards(List<String> cardList){
        MyButton[] kaarten = new MyButton[cardList.size()];
        for(int i = 0; i < kaarten.length; i++){
            kaarten[i] = new MyButton(cardList.get(i));
        }
        return kaarten;
    }
    /**
     * Submit the sidedeck of the current selected player
     * 
     * @param event 
     */
    @FXML
    private void submit(ActionEvent event) {
        int index = listviewSelected.getSelectionModel().getSelectedIndex();
        if(showSideDeckController[index].getSelectedCards().size() < 6){
            Alert alert = giveAlert("More Cards", "You have to chose 6 cards");
            alert.showAndWait();
        }else if(teller < listviewSelected.getItems().size()-1){
            String[] cardsChosen = new String[showSideDeckController[index].getSelectedCards().size()];
            for(int i = 0; i < showSideDeckController[index].getSelectedCards().size(); i++){
                cardsChosen[i] = showSideDeckController[index].getSelectedCards().get(i).toString();
            }
            Navigator.dc.addCardsToSideDeck(listviewSelected.getSelectionModel().getSelectedItem(), cardsChosen);
            listviewSelected.getSelectionModel().select(++teller);
            selectSideDeckController();
            btnPrevious.setDisable(false);
        }else{
            String[] cardsChosen = new String[showSideDeckController[index].getSelectedCards().size()];
            for(int i = 0; i < showSideDeckController[index].getSelectedCards().size(); i++){
                cardsChosen[i] = showSideDeckController[index].getSelectedCards().get(i).toString();
            }
            Navigator.dc.addCardsToSideDeck(listviewSelected.getSelectionModel().getSelectedItem(),cardsChosen);
            
            Navigator.loadScreen(new PlayGameController());
            System.out.println("naar gameGui");
        }
    }
    /**
     * Go to the previous selected player
     * 
     * @param event 
     */
    @FXML
    private void previous(ActionEvent event) {
        int index = listviewSelected.getSelectionModel().getSelectedIndex();
        System.out.println(index + " previous");
        
        if(listviewSelected.getSelectionModel().getSelectedIndex() == 1){
            listviewSelected.getSelectionModel().select(--teller);
            selectSideDeckController();
            btnPrevious.setDisable(true);
        }else{
            listviewSelected.getSelectionModel().select(--teller);
            selectSideDeckController();
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
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel");
        alert.setContentText(Navigator.dc.getTextController().translateString("CONFORMATION_DIALOG"));

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            Navigator.loadScreen(new MainMenuController());
        } else {
            
        }
    }
}
