/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import domain.DomainController;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

/**
 * FXML Controller class
 *
  * @author Aykut Yilmaz - aykut.yilmaz.u1495@student.hogent.be
 */
public class SelectLanguageController extends BorderPane {

    @FXML
    private Button btnNed;
    @FXML
    private Button btnEng;
    @FXML
    private Button btnFra;

    @FXML
    private BorderPane rootPane;
    
    /**
     * Contructor of SelectLanguageController. Load a FXMLfile to construct a gui.
     * Made to select or change the language of the gui
     */
    public SelectLanguageController() {
        FXMLLoader loader = new FXMLLoader(getClass().
                getResource("SelectLanguage.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        
    }

    /**
     * set Language to Dutch
     * 
     * @param event
     * @throws IOException 
     */
    @FXML
    private void chooseNed(ActionEvent event) throws IOException {
       Navigator.dc.getTextController().setLanguage("nederlands");
       Navigator.loadScreen(new MainMenuController());
    }

    /**
     * set language to English
     * 
     * @param event 
     */
    @FXML
    private void chooseEng(ActionEvent event) {
        Navigator.dc.getTextController().setLanguage("english");
        Navigator.loadScreen(new MainMenuController());
    }

    /**
     * set language to French
     * 
     * @param event 
     */
    @FXML
    private void chooseFra(ActionEvent event) {
        Navigator.dc.getTextController().setLanguage("français");
        Navigator.loadScreen(new MainMenuController());
    }

}
