/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistence;

import configuration.Configuration;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Aykut Yilmaz - aykut.yilmaz.u1495@student.hogent.be
 */
public class Mapper {
    private String[] dbProps = { // default values - gets most recent ones from config.properties
            "jdbc:mysql://ID222177_g53.db.webhosting.be:3306/ID222177_g53?zeroDateTimeBehavior=convertToNull", // database connect url
            "ID222177_g53", // database username
            "Jubs#OfWeec4" // database password
    };

    private PlayerMapper playerMapper;
    private CardMapper cardMapper;
    private GameMapper gameMapper;

    public Mapper() {
        if(getConnection() != null){
            playerMapper = new PlayerMapper(getConnection());
            cardMapper = new CardMapper(getConnection());
            gameMapper = new GameMapper(getConnection());
            dbProps = Configuration.getDatabaseProperties();
        }else{
            System.exit(0);
        }
    }

    /**
     * Grabs the connection with the database
     * 
     * @return connection
     */
    private Connection getConnection() {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            try {
                return DriverManager.getConnection(dbProps[0], dbProps[1], dbProps[2]);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException ex) {
            System.exit(0); // TODO - fix this shit
        } 
        return null;
    }
    
    /**
     * Save a game with current players, gamedeck cards and scores
     * 
     * @param gameName string name of the game
     * @param partsGame list with participants of the game
     * @param partsScore list with the scores of the players
     * @param gameDeck list with the cards of the playes
     * @throws SQLException 
     */
    public void saveGamePlayer(String gameName, List<String> partsGame, List<Integer> partsScore, List<String> gameDeck) throws SQLException{
        gameMapper.saveGame(gameName);
        
        String sql = "INSERT INTO Game_Player (game_id, player_id, sideDeckCard, setScore) " +
                        "VALUES (?, ?, ?, ?);";
        
        PreparedStatement stat = getConnection().prepareStatement(sql);
        stat.setInt(1 , gameMapper.getGameID(gameName));
        for(int i = 0; i < partsGame.size(); i++){
            stat.setInt(2, playerMapper.getPlayerID(partsGame.get(i)));
            stat.setString(3, gameDeck.get(i));
            stat.setInt(4, partsScore.get(i));
            
            if(stat.execute()){
                System.out.println("executed");
            }
        }
    }
    
    /**
     * Load all the games from the database
     * 
     * @return list with Strings of the game
     * @throws SQLException 
     */
    public List<String> loadGames() throws SQLException{
        List<String> games = new ArrayList<>();
        
        String sql = "SELECT Game.game_name, Player.name, Game_Player.sideDeckCard, Game_Player.setScore " +
                    "FROM Game_Player \n" +
                    "JOIN Game On Game_Player.game_id = Game.game_id " +
                    "JOIN Player ON Game_Player.player_id = Player.player_id";
        
        Statement stat = getConnection().createStatement();
        ResultSet result = stat.executeQuery(sql);
        
        while(result.next()){
            games.add(result.getString(1) + "/" + result.getString(2) + "/" + result.getString(3) + "/" + result.getInt(4));
        }
        return games;
    }
    /**
     * Update the scores and gamedecks of a game
     * 
     * @param gameName String with the name of the game
     * @param partsGame list with the participants
     * @param partsScore list with the scores
     * @param gameDeck list with the gamedecks
     * @throws SQLException 
     */
    public void updateGame(String gameName, List<String> partsGame, List<Integer> partsScore, List<String> gameDeck) throws SQLException{
        String sql = "UPDATE Game_Player " +
                        "SET sideDeckCard = ?, setScore = ? " +
                        "WHERE game_id = ? AND player_id = ?";
        
        PreparedStatement stat = getConnection().prepareStatement(sql);
        stat.setInt(3, gameMapper.getGameID(gameName));
        for(int i = 0; i < partsGame.size(); i++){
            stat.setString(1, gameDeck.get(i));
            stat.setInt(2, partsScore.get(i));
            stat.setInt(4, playerMapper.getPlayerID(partsGame.get(i)));
            
            if(stat.execute()){
                System.out.println("executed");
            }
        }
    }
    
    /**
     * Grab the playermapper
     * 
     * @return PlayerMapper
     */
    public PlayerMapper getPlayerMapper(){
        return playerMapper;
    }
    
    /**
     * Grab the cardmapper
     * 
     * @return CardMapper
     */
    public CardMapper getCardMapper(){
        return cardMapper;
    }
    
    /**
     * Grab the gamemapper
     * 
     * @return GameMapper
     */
    public GameMapper getGameMapper(){
        return gameMapper;
    }
    
}
