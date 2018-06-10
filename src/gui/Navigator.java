/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import domain.DomainController;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.Node;
import javafx.scene.layout.Pane;



/**
 *
 * @author Aykut Yilmaz - aykut.yilmaz.u1495@student.hogent.be
 */
public class Navigator {
    
    public static SelectLanguageController selectLanguage;
    public static DomainController dc;
    
    private static MainController mainController;
    
    /**
     * Constructor to create a Navigator object.
     * Holds the DomainController and opens the SelectLanguageController
     * 
     * @throws SQLException 
     */
    public Navigator() throws SQLException {
        Navigator.dc = new DomainController();
        selectLanguage = new SelectLanguageController();
    }
    
    /**
     * Set the current maincontroller to given maincontroller
     * 
     * @param mainController 
     */
    public static void setMainController(MainController mainController) {
        Navigator.mainController = mainController;
    }
    /**
     * load a screen and sets it in the maincontroller
     * 
     * @param pane 
     */
    public static void loadScreen(Pane pane){
       mainController.setScreen(pane);
    }
   
    /**
     * maps all nodes with a String
     * @param nodes list of nodes
     * @param stringsToTranslate String to communicate with ResourceBundles
     * @return Map with Node as key, String as value
     */
    public static Map<Node, String> mapNodes(List<Node> nodes, List<String> stringsToTranslate){
        Map<Node, String> nodesToTranslate = new HashMap<>();
        for(int i = 0; i < nodes.size(); i++){
            nodesToTranslate.put(nodes.get(i), stringsToTranslate.get(i));
        }
        return nodesToTranslate;
    }
    
    
   
}
