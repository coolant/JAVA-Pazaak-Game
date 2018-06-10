package persistence;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Mapper for saving and loading player information
 *
 * @author Aykut Yilmaz - aykut.yilmaz.u1495@student.hogent.be
 * @version 1.0
 */
public class PlayerMapper{
    
    private Connection connection;
    
    public PlayerMapper(Connection connection){
        this.connection = connection;
    }
    
    /**
     * Save a player in the database
     * 
     * @param name String with the name
     * @param birthyear int with the birthyear
     * @param credit int with the current credits
     * @throws SQLException 
     */
    public void addPlayer(String name, int birthyear, int credit) throws SQLException{
        
        String sql = "INSERT INTO Player (name, birthyear, credit) VALUES (?, ?, ?)";

        PreparedStatement stat = connection.prepareStatement(sql);
        stat.setString(1, name);
        stat.setInt(2, birthyear);
        stat.setInt(3, credit);
        
        if(stat.execute()){
            System.out.println("insert player");
        }
    }
    
    /**
     * Grabs the id of a player out the database
     * 
     * @param player name of the player
     * @return int with the id
     * @throws SQLException 
     */
    public int getPlayerID(String player) throws SQLException{
        int playerID = -1;
        String sql = "SELECT ID222177_g53.Player.player_id \n" +
                        "FROM ID222177_g53.Player\n" +
                        "WHERE ID222177_g53.Player.name = ?";
        
        PreparedStatement stat = connection.prepareStatement(sql);
        stat.setString(1, player);
        
        ResultSet result = stat.executeQuery();
        
        if(result.next()){
            playerID = result.getInt(1);
        }
        
        return playerID;
    }
    
    /**
     * Grabs all the players out the database
     * 
     * @return Map with String and int[] string is the name of the player, the int[] has birthyear and current credit
     */
    public Map<String, int[]> getPlayers(){
        try {
            Map<String, int[]> spelers = new HashMap<>();
            
            String sql = "SELECT * FROM Player";
            
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            
            while(result.next()){
                spelers.put(result.getString(2), new int[]{result.getInt(3), result.getInt(4)});
            }
            
            return spelers;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }
   
    /**
     * Update credit of a player
     * 
     * @param name string of the player
     * @param credit int with the new amount of credits
     */
    public void updateCredit(String name, int credit){
        String sql = "UPDATE Player SET credit=? WHERE name=?";
        
        try {
            PreparedStatement stat = connection.prepareStatement(sql);
            stat.setInt(1, credit);
            stat.setString(2, name);
            if (stat.execute()) {
                System.out.println("update player");
            }
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Delete all the players of the database
     * 
     * @param name 
     */
    public void deletePlayer(String name){
        try {
            String sql = "DELETE FROM Player WHERE name=?";
            
            PreparedStatement stat = connection.prepareStatement(sql);
            stat.setString(1, name);
            
            
            if (stat.execute()) {
                System.out.println("delete player");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void deletePlayers(){
        try {
            String sql = "TRUNCATE Player";
            
            PreparedStatement stat = connection.prepareStatement(sql);
            if(stat.execute()){
                System.out.println("clear database");
            }
           
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
    }
}
