/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;

/**
 *
  * @author Aykut Yilmaz - aykut.yilmaz.u1495@student.hogent.be
 */
public class MyButton extends Label {
    
    /**
     * Constructor to make a MyButton object. MyButtons are used to visualize cards
     * @param card String of a card
     */
    public MyButton(String card){
        this.setAlignment(Pos.CENTER);
        this.setStyle("-fx-font: 22 arial; -fx-base: #b6e7c9; -fx-border-width: 1px; -fx-border-color: black;");
        this.setMinSize(100.0, 100.0);
        this.setText(card);
        this.setPadding(new Insets(10.0));
        
        if(card.charAt(0) == '+'){
            this.setStyle("-fx-font: 22 arial; -fx-base: #b6e7c9; -fx-border-width: 1px; -fx-border-color: black; -fx-background-color: #3b82b8;");
        }else if (card.charAt(0) == '-'){
            this.setStyle("-fx-font: 22 arial; -fx-base: #b6e7c9; -fx-border-width: 1px; -fx-border-color: black; -fx-background-color: #b0584c;");
        }else if(card.charAt(0) == '±'){
            this.setStyle("-fx-font: 22 arial; -fx-base: #b6e7c9; -fx-border-width: 1px; -fx-border-color: black; -fx-background-color: #e4dd00;");
        }else{
            this.setStyle("-fx-font: 22 arial; -fx-base: #b6e7c9; -fx-border-width: 1px; -fx-border-color: black; -fx-background-color: #64ff6e;");
        }
    }
    
    /**
     * Grabs the text of the label
     * @return String with text of the label 
     */
    @Override
    public String toString() {
        return this.getText();
    }
    
    
    
}
