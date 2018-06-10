/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Aykut Yilmaz - aykut.yilmaz.u1495@student.hogent.be
 */
public class GameMapper {
 
    private Connection connection;
    
    public GameMapper(Connection connection){
        this.connection = connection;
    }
    
    /**
     * Grabs id of a game in the database
     * 
     * @param gameName string with name of the game
     * @return int with id of player
     * @throws SQLException 
     */
    public int getGameID(String gameName) throws SQLException{
        int gameID = -1;
        String sql = "SELECT ID222177_g53.Game.game_id FROM ID222177_g53.Game WHERE ID222177_g53.Game.game_name = ?";
        
        PreparedStatement stat = connection.prepareStatement(sql);
        stat.setString(1, gameName);
        
        ResultSet result = stat.executeQuery();
        
        if(result.next()){
            gameID = result.getInt(1);
        }
        
        return gameID;
    }
    
    /**
     * Save a game to the database
     * 
     * @param gameName string with game name
     * @throws SQLException 
     */
    public void saveGame(String gameName) throws SQLException{
        
        String sql = "INSERT INTO Game (game_name) VALUES (?)";
        
        PreparedStatement stat = connection.prepareStatement(sql);
        stat.setString(1, gameName);
        
        if(stat.execute()){
            System.out.println("insert player");
        }
    }
    
    /**
     * Delete all games from the database
     * 
     * @throws SQLException 
     */
    public void deleteGames() throws SQLException{
        String sql = "TRUNCATE Game";

        PreparedStatement stat = connection.prepareStatement(sql);
        if(stat.execute()){
            System.out.println("clear database");
        }
    }
    
}
