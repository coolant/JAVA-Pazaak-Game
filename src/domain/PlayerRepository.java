package domain;

import exceptions.InvalidNameException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.Mapper;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 
  * @author Aykut Yilmaz - aykut.yilmaz.u1495@student.hogent.be
 * @since 2017-02-23
 * @version 1.0
 */
public class PlayerRepository {

    private static List<Player> players;
    private static ObservableList<Player> playerList;
    private Mapper mapper;
    
    /**
     * Constructor to create a new PlayerRepository. Connects with the database 
     * and pulls all the players out of the database
     * 
     * @param mapper mapper to connect to the database
     */
    public PlayerRepository(Mapper mapper) {
        this.mapper = mapper;
        players = pullPlayers();
        playerList = FXCollections.observableArrayList(players);
    }

    /**
     * Finds the player object that corresponds to a given player name
     *
     * @param playerName A player name for which to return a Player object
     * @return a Player object that corresponds to the given player name String
     * @throws InvalidNameException Throws an exception if the player name
     *                              did not match any player in the repository.
     */
    public static Player findPlayer(String playerName) throws InvalidNameException {
        if (players.stream().anyMatch(p -> p.getName().equals(playerName))) {
            return players.stream().filter(p -> p.getName().equalsIgnoreCase(playerName)).findAny().get();
        }
        throw new InvalidNameException();
    }
    
    /**
     * Get all the players from player list
     * 
     * @return list of players
     */
    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }
    
    /**
     * get all the players in an ObservableList
     * 
     * @return ObservableList of players
     */
    public ObservableList<Player> getPlayersOb(){
        return playerList;
    }

    /**
     * Grabs all the players of the database
     * 
     * @return list of players
     */
    private List<Player> pullPlayers() {
        //Map<name, {birthYear, credit}>
        Map<String, int[]> spelersMap = mapper.getPlayerMapper().getPlayers();

        //add new Player to ArrayList players
        return spelersMap.entrySet().stream()
                .map(e -> new Player(e.getKey(), e.getValue()[0], e.getValue()[1])).collect(Collectors.toList());
    }
    
    /**
     * Checks the repository if it contains a player with the specified player name
     *
     * @param pName the game name to look up
     * @return whether the game exists in the repository
     */
    public boolean playerExists(String pName) {
        return players.stream().anyMatch(p -> p.getName().equals(pName));
    }

    /**
     * Add a player to the repository and push it to the database
     * 
     * @param p given player
     * @throws SQLException 
     */
    public void addPlayer(Player p) throws SQLException {
        pushPlayer(p);
        players.add(p);
    }
    
    /**
     * if player doesn't exist push it to the database
     * if it already exists update the credit of the player
     * 
     * @param p given player
     * @throws SQLException 
     */
    private void pushPlayer(Player p) throws SQLException {
        if (playerExists(p.getName())) {
            mapper.getPlayerMapper().updateCredit(p.getName(), p.getCredits());
        } else {
            mapper.getPlayerMapper().addPlayer(p.getName(), p.getBirthYear(), p.getCredits());
        }
    }

    /**
     * remove a player of the repository
     * 
     * @param p given player
     * @return 
     */
    public boolean removePlayerToParticipateInGame(Player p) {
        return players.remove(p);
    }
    
}
