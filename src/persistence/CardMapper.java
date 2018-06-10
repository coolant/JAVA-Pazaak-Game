/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package persistence;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Aykut Yilmaz - aykut.yilmaz.u1495@student.hogent.be
 */
public class CardMapper {

    private Connection connection;
    
    public CardMapper(Connection connection) {
        this.connection = connection;
    }
    
    /**
     * Add a card to the database
     * 
     * @param type String with type
     * @param value String with type
     * @param description String with description
     * @throws SQLException 
     */
    public void addCard(String type, int value, String description) throws SQLException{
        
        String sql = "INSERT INTO Card (type, value, description) VALUES (?, ?, ?)";
        
        PreparedStatement stat = connection.prepareStatement(sql);
        stat.setString(1, type);
        stat.setInt(2, value);
        stat.setString(3, description);
        
        if(stat.execute()){
            System.out.println("insert Card");
        }
    }
    
    /**
     * Grabs ID of a card in the database 
     * 
     * @param card String card
     * @return int with the id of the card
     * @throws SQLException 
     */
    public int getCardID(String card) throws SQLException{
        int cardID = -1;
        String sql = "SELECT ID222177_g53.Card.card_id\n" +
                        "FROM ID222177_g53.Card\n" +
                        "WHERE ID222177_g53.Card.type = ? AND ID222177_g53.Card.value = ?;";
        
        PreparedStatement stat = connection.prepareStatement(sql);
        stat.setString(1, ""+typeOfCard(card));
        stat.setString(2, ""+card.charAt(2));
        
        ResultSet result = stat.executeQuery();
        
        if(result.next()){
            cardID = result.getInt(1);
        }
        return cardID;
    }

    /**
     * Grabs all cards of the database
     * 
     * @return list of Strings
     */
    public List<String> getCards() {
        try {
            List<String> cards = new ArrayList<>();

            String sql = "SELECT * FROM Card";
            
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            
            while(result.next()){
                cards.add(result.getString(2) + result.getString(3));
            }
            return cards;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }
    
    /**
     * Deletes all cards in the database
     * 
     */
    public void deleteCards(){
        try {
            String sql = "TRUNCATE Card";
            
            PreparedStatement stat = connection.prepareStatement(sql);
            if(stat.execute()){
                System.out.println("clear database");
            }
           
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
    }
    
    /**
     * replace type of card to character with type
     * @param card
     * @return 
     */
    private Character typeOfCard(String card){
        switch(card.charAt(0)){
            case '+':
                return 'P';
            case '-':
                return 'N';
            case '±':
                return 'S';
            default:
                return 'E';
        }
    }

}
