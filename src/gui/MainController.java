/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import domain.DomainController;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

/**
 * FXML Controller class
 *
  * @author Aykut Yilmaz - aykut.yilmaz.u1495@student.hogent.be
 */
public class MainController extends StackPane {
    
    private DomainController dc; 
    
    @FXML
    private StackPane screenHolder;

    /**
     * Contructor of MainController. Load a FXMLfile to construct a gui.
     * Add the selectLanguage controller to the screenholder
     */
    public MainController() {
        FXMLLoader loader
                = new FXMLLoader(getClass().getResource("Main.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        
        screenHolder.getChildren().add(Navigator.selectLanguage);
    }

    /**
     * Add a pane to the screenHolder
     * 
     * @param pane
     */
    public void setScreen(Pane pane){
        screenHolder.getChildren().clear();
        screenHolder.getChildren().add(pane);
    }

    
}
