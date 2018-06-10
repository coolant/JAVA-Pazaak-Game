/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import configuration.Configuration;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;


/**
 *
  * @author Aykut Yilmaz - aykut.yilmaz.u1495@student.hogent.be
 */
public class TextController {

    private Locale currentLang;
    private final Locale[] supportedLocales;
    private ResourceBundle bundle;
    
    /**
     * Contructor of TextController. Set supportedLocales with all Locales
     * set the current locale to default
     * and initialize the bundle
     */
    public TextController() {
        supportedLocales = new Locale[]{new Locale("nl", "BE"), new Locale("fr", "BE"), new Locale("en", "US")};
        currentLang = Locale.getDefault();
        bundle = ResourceBundle.getBundle("language/textBundle" , currentLang);
    }
    
     /**
     * Attempts to define an application-wide language based on user input.
     *
     * @param language a two letter ISO-standardized abbreviation of a language
     * that is available in the application
     */
    public void setLanguage(String language){
        int getal = 0;
        switch (language.toLowerCase())
        {
            case "nl":
            case "nederlands":
                getal = 0;
                currentLang = supportedLocales[getal];
                bundle = ResourceBundle.getBundle("language/textBundle" , currentLang);
                break;
            case "fr":
            case "francais":
            case "français":
                getal = 1;
                currentLang = supportedLocales[getal];
                bundle = ResourceBundle.getBundle("language/textBundle" , currentLang);
                break;
            case "en":
            case "english":
                getal = 2;
                currentLang = supportedLocales[getal];
                bundle = ResourceBundle.getBundle("language/textBundle" , currentLang);
                break;
            default:
                throw new IllegalArgumentException();
        }
    }
    
    /**
     * Translate a resource string to the appropriate application language
     * \😂\😂\😂
     *
     * @param toTranslate The to be translated string, matching a key in the
     * dictionaries.
     * @return Returns a translated string
     */
    public String translateString(String toTranslate) {
        try {
            return bundle.getString(toTranslate);
        } catch (MissingResourceException e) {
            return "MISSING STRING for " + toTranslate;
        }
    }
    
    /**
     * Text to translate of the different nodes. Keys are nodes with a String value
     * 
     * @param nodesToTranslate map with a node as key and a String as value
     */
    public void translateNodes(Map<Node, String> nodesToTranslate){
        for(Map.Entry<Node, String> node: nodesToTranslate.entrySet()){
            if(node.getKey() instanceof Button){
                Button btn = (Button) node.getKey();
                btn.setText(translateString(node.getValue()));
            } else if(node.getKey() instanceof Label){
                Label lbl = (Label) node.getKey();
                lbl.setText(translateString(node.getValue()));
            } 
        }
    }
    
}
