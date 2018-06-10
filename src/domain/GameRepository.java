/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import persistence.Mapper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

/**
 *
  * @author Aykut Yilmaz - aykut.yilmaz.u1495@student.hogent.be
 */
public class GameRepository {
    private static List<Game> games;
    private Mapper mapper;
    
    /**
     * Contructor for creating a GameRepository, and grabs all games out of the database
     * 
     * @param mapper Mapper object to connect to the database
     * @throws SQLException 
     */
    public GameRepository(Mapper mapper) throws SQLException {
        this.mapper = mapper;
        games = loadAllGames();
    }
    
    /**
     * Search a game with a given name
     * 
     * @param gameName string with game name
     * @return Game 
     */
    public static Game findGame(String gameName) {
        if (games.stream().anyMatch(g -> g.getGameName().equals(gameName))) {
            return games.stream().filter(g -> g.getGameName().equalsIgnoreCase(gameName)).findAny().get();
        } else {
            return null;
        }
    }
    /**
     * Grab all games of the gameRepository
     * 
     * @return list with games
     */
    public List<Game> getAllGames(){
        return Collections.unmodifiableList(games);
    }
    /**
     * load all the games of the database
     * 
     * @return list with games
     * @throws SQLException 
     */
    private List<Game> loadAllGames() throws SQLException{
        List<Game> games = new ArrayList<>();

        List<String> allGames = mapper.loadGames();

        for(int i = 0; i < allGames.size(); i = i + 2){
            List<Player> players = new ArrayList<>();
            List<Integer> partScore = new ArrayList<>();
            List<List<Card>> cards = new ArrayList<>();

            String[] gegevens1 = allGames.get(i).split("/");
            String[] gegevens2 = allGames.get(i+1).split("/");

            String gameName = gegevens1[0];
            players.add(PlayerRepository.findPlayer(gegevens1[1]));
            players.add(PlayerRepository.findPlayer(gegevens2[1]));

            cards.add(listStringToCards(gegevens1[2]));
            cards.add(listStringToCards(gegevens2[2]));

            partScore.add(Integer.parseInt(gegevens1[3]));
            partScore.add(Integer.parseInt(gegevens2[3]));

            games.add(new Game(gameName, players, partScore, cards));

            players.clear();
            partScore.clear();
            cards.clear();
        }

        return games;
    }
    
    /**
     * Save a game in the database, with players, score and gamedecks
     * 
     * @param game given game
     * @throws SQLException 
     */
    public void saveGameWithPlayers(Game game) throws SQLException{
        List<String> cards = new ArrayList<>();

        for(List<Card> gameDeckPlayer: game.listGameDeck()){
            List<String> gameDeck = new ArrayList<>();
            StringBuilder cardString = new StringBuilder();
            for(Card card: gameDeckPlayer){
                cardString.append(card.toString()).append("#");
            }
            cards.add(cardString.toString());
        }
        mapper.saveGamePlayer(game.getGameName(), game.displayParticipantNames(), game.getParticipantScores(), cards);
    }
    
    /**
     * update the score of players when the game already exists
     * 
     * @param game given game
     * @throws SQLException 
     */
    public void updateGame(Game game) throws SQLException {
        List<String> cards = new ArrayList<>();
        
        for(List<Card> gameDeckPlayer: game.listGameDeck()){
            List<String> gameDeck = new ArrayList<>();
            StringBuilder cardString = new StringBuilder();
            for(Card card: gameDeckPlayer){
                cardString.append(card.toString()).append("#");
            }
            cards.add(cardString.toString());
        }
        mapper.updateGame(game.getGameName(), game.displayParticipantNames(), game.getParticipantScores(), cards);
    }

    /**
     * check if a game already exists
     * 
     * @param gName name of a game
     * @return true when game already exists 
     */ 
    public boolean gameExists(String gName) {
        try{
            return games.stream().anyMatch(g -> g.getGameName().equals(gName));
        }catch (NullPointerException ex){
            return false;
        }
    }
    
    //TODO - methode wordt niet gebruikt
    public boolean gameExists(List<Game> games, String pName) {
        try{
            return games.stream().anyMatch(g -> g.getGameName().equals(pName));
        }catch (NullPointerException ex){
            return false;
        }
    }
    /**
     * List all games of a given player
     * 
     * @param player Player
     * @return list of games
     */
    public List<Game> listAllGameOfPlayer(Player player){
        List<Game> games = new ArrayList<>();
        for (Game g : GameRepository.games) {
            if(g.getParticipants().contains(player)){
                games.add(g);
            }
        }
        return games;
    }

    /**
     * List String to card Objects
     * 
     * @param cards strings of cards
     * @return list of cards
     */
    private List<Card> listStringToCards(String cards){
        List<Card> gameDeck = new ArrayList<>();
        String[] cardsArray = cards.split("#");
        for(String card: cardsArray){
            gameDeck.add(CardRepository.findCard(card));
        }
        return gameDeck;
    }

}
