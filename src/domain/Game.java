package domain;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Overkoepelende, eventueel geserialiseerde klasse die het laden en opslaan van
 * spellen vergemakkelijkt. Deze klasse houdt een timestamp bij over wanneer de
 * game werd gestart.
 *
  * @author Aykut Yilmaz - aykut.yilmaz.u1495@student.hogent.be
 * @since 2017-02-23
 * @version 1.0
 */
public class Game implements Serializable {

    /**
     * See: https://docs.oracle.com/javase/7/docs/api/java/sql/Timestamp.html
     * SQL-ready timestamp property
     */
    private final Timestamp creationTimeStamp;
    private String gameName;
    private GameSet currentSet = null;
    private List<PlayerDataTriplet> playerDataTriplets = new ArrayList<>();
    
    /**
     * Constructor for creating a game with participants
     * 
     * @param gameParts list of Players
     */
    public Game(List<Player> gameParts) {
        this(
                "-1",
                gameParts,
                new ArrayList<>(Arrays.asList(0, 0)), // TODO - dynamic size depending on size of participants
                null // TODO - insert actual side decks later on through setter injection
        );
    }

    /**
     * Constructor for creating an in-progress game loaded from the persistence
     * stack.
     *
     * @param gameName The game name that was used to load the game
     * @param gameParts The player participants
     * @param participantScores The scores for each of the participants. The
     * values correspond the index of this list to the index of the participants
     * list
     */
    public Game(String gameName, List<Player> gameParts, List<Integer> participantScores, List<List<Card>> decks) {
        this.gameName = gameName;

        // als het een nieuw spel is, dan maken we dat decks gewoon een lege lijst is met
        if (decks == null) {
            decks = new ArrayList<>();
            for (int i = 0; i < gameParts.size(); i++) {
                decks.add(new ArrayList<Card>() {{
                    add(null);
                }});
            }
        }

        // check if all the parameters are filled with equal length lists, so we don't mismatch
        if (!isOfEqualLength(gameParts.size(), participantScores.size(), decks.size())) {
            throw new InputMismatchException("ERROR_NOT_OF_EQUAL_LENGTH");
        }

        // store all the stuff in triplets so we can couple info for each player
        for (int i = 0; i < gameParts.size(); i++) {
            playerDataTriplets.add(new PlayerDataTriplet(gameParts.get(i), decks.get(i), participantScores.get(i)));
        }

        // TODO - pull the side decks from the database

        this.creationTimeStamp = Timestamp.valueOf(LocalDateTime.now());


        // TODO - grab 4 of the 6 side deck cards randomly
    }


    private boolean isOfEqualLength(int a, int b, int c) {
        return (a == b && a == c);
    }

    /**
     * Get the score of the participants
     * 
     * @return list with the scores of a player
     */
    public List<Integer> getParticipantScores() {
        List<Integer> scores = new ArrayList<>();
        for (PlayerDataTriplet pdt : this.playerDataTriplets) {
            scores.add(pdt.getScore());
        }
        return scores;
    }

    /**
     * Increment a given participant's set score by 1
     *
     * @param participant A given player for whom the score should be
     * incremented
     */
    public void incrementParticipantScore(Player participant) {
        int playerId;
        boolean successful = false;
        for (playerId = 0; playerId < this.playerDataTriplets.size(); playerId++) {
            if (this.playerDataTriplets.get(playerId).getPlayer().equals(participant)) {
                PlayerDataTriplet pdt = this.playerDataTriplets.get(playerId);
                pdt.incrementScore();
                this.playerDataTriplets.set(playerId, pdt);
                successful = true;
                break;
            }
        }

        if (!successful)
            throw new NoSuchElementException("ERROR_PLAYER_NOT_FOUND");
    }

    /**
     * Prepares a set to be played.
     */
    public void initializeSet() {
        Map<Player, List<Card>> playerDeckMap = new HashMap<>();
        for (PlayerDataTriplet pdt : playerDataTriplets) {
            playerDeckMap.put(pdt.getPlayer(), pdt.getGameDeck());
        }
        this.currentSet = new GameSet(playerDeckMap);
    }

    /**
     * Gets a postive integer representative of the set score for a given participant
     * participant
     *
     * @param participant The given participant for which to return the score
     * @return A non-negative integer representing a given player's score
     * @throws NoSuchElementException if the given player is not found in the
     * participants list
     */
    private int getParticipantScore(Player participant) {
        for (PlayerDataTriplet pdt : playerDataTriplets) {
            if (pdt.getPlayer().equals(participant))
                return pdt.getScore();
        }
        throw new NoSuchElementException();
    }

    /**
     * Grabs the game name
     * 
     * @return string with the gameName
     */
    public String getGameName() {
        return this.gameName;
    }

    /**
     * set the name of the current game
     * 
     * @param gameName string with game name
     */
    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    /**
     * Grabs the names of the participants
     * 
     * @return list with strings
     */
    public List<String> displayParticipantNames() {
        return getParticipants().stream().map(Player::getName).collect(Collectors.toList());
    }
    
    /**
     * Grabs the participants without a sidedeck
     * 
     * @return list of Players
     */
    public List<Player> listUnreadyParticipants() {
        List<Player> players = new ArrayList<>();
        for (PlayerDataTriplet pdt : playerDataTriplets) {
            if (pdt.getGameDeck().size() != 4)
                players.add(pdt.getPlayer());
        }
        return players;
    }

    /**
     * Grab all players of a current playing game
     * 
     * @return list of players 
     */
    public List<Player> getParticipants() {
        List<Player> players = new ArrayList<>();
        playerDataTriplets.forEach(pdt -> players.add(pdt.getPlayer()));
        return players;
    }

    public Timestamp getCreationTimeStamp() {
        return this.creationTimeStamp;
    }
    /**
     * Add gamedeck cards to the playerDataTriplet for a given player
     * 
     * @param player player Object
     * @param sideDeck list of cards
     */
    public void addGameDeckForPlayer(Player player, List<Card> sideDeck) {
        Collections.shuffle(sideDeck);
        PlayerDataTriplet pdt = playerDataTriplets.stream().filter(triplet -> triplet.getPlayer().equals(player)).findFirst().get();
        playerDataTriplets.remove(pdt);
        List<Card> gameDeck = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            gameDeck.add(sideDeck.get(i));
        }
        pdt.setGameDeck(gameDeck);
        playerDataTriplets.add(pdt);
    }

    /**
     * Method to check if all preparations to starting a game are in place. So
     * far the only requirement is that all players have compiled a side deck to
     * play the game with.
     *
     * Because the .noneMatch() returns true if the participant stream has no
     * objects, I check to see if the game has participants in the first place.
     *
     * @return Returns true if there are two participants and they've both
     * compiled cards to form a side deck.
     */
    public boolean isReadyToStart() {
        boolean ready = false;
        for (PlayerDataTriplet pdt : this.playerDataTriplets) {
            if (pdt.getGameDeck().size() != 4) {
                ready = false;
                break;
            } else {
                ready = true;
            }
        }
        return ready;
    }

    /**
     * Method that initiates the start of a match and keeps looping true sets
     * while no player has 3 wins
     *
     * @return Returns winner of the game
     */
    @Override
    public String toString() {
        return "Game{" + ", gameName = " + gameName + '}';
    }

    public GameSet getCurrentSet() {
        return currentSet;
    }

    /**
     * Compiles all participants their game decks and orders them.
     * Each player is denoted to have 4 available spots for game deck cards.
     * <p>
     * Finding out the cards for a given player is as simple as starting to grab the next 4 cards
     * from the player's index multiplied by 4.
     * <p>
     * Empty card spots have a null value, and can be ignored as such.
     *
     * @return Returns an ordered list of cards and nulls containing all players' play cards.
     */
    public List<Card> getAllGameDecks() {
        List<Card> cards = new ArrayList<>();

        for (PlayerDataTriplet pdt : playerDataTriplets) {
            cards.addAll(pdt.getGameDeck());
            int size = pdt.getGameDeck().size();
            for (int i = 0; i < 4 - size; i++) {
                cards.add(null);
            }
        }
        return cards;
    }
    
    /**
     *List all gamedecks of the players
     * 
     * @return list of list of cards
     */
    public List<List<Card>> listGameDeck(){
       List<List<Card>> cards = new ArrayList<>();
       for(PlayerDataTriplet pdt: this.playerDataTriplets){
           cards.add(pdt.getGameDeck());
       }
       return cards;
    }

    /**
     * Grabs gameDeck of a given player
     * 
     * @param activePlayer player Object
     * @return list of cards
     */
    public List<Card> getGameDeckForPlayer(Player activePlayer) {
        for (PlayerDataTriplet pdt : playerDataTriplets) {
            if (pdt.getPlayer().equals(activePlayer))
                return pdt.getGameDeck();
        }
        throw new NoSuchElementException("ERROR_NO_SUCH_PLAYER");
    }
    
    /**
     * Get the name of a game
     * 
     * @return StringProperty 
     */
    public StringProperty nameProp(){
        StringProperty nameProp = new SimpleStringProperty();
        nameProp.set(gameName);
        return nameProp;
    }
    
    /**
     * Get names of a game, separated with Vs.
     * 
     * @return StringProperty
     */
    public StringProperty playerProp(){
        StringProperty playerProp = new SimpleStringProperty();
        String playersString = ""; 
                
        for(PlayerDataTriplet pdt: playerDataTriplets){
            playersString += pdt.getPlayer().getName() + " Vs. ";
        }
        playerProp.set(playersString.substring(0, playersString.length() - 4));
        return playerProp;
    }
    /**
     * Get scores of a game, separated with -
     * 
     * @return StringProperty
     */
    public StringProperty scoreProp(){
        StringProperty scoreProp = new SimpleStringProperty();
        String score = "";

        for(PlayerDataTriplet pdt: playerDataTriplets){
            score += pdt.getScore() + " - ";
        }
        scoreProp.set(score.substring(0, score.length()-3));
        return scoreProp;
    }

    /**
     * Finds out if enough sets have been played to crown a player victory in the game.
     *
     * If a player has won 3 sets, they are crowned victorious and a game can no longer
     * be continued.
     *
     * @return Returns true if more sets need to be played in order to determine a winner, false if someone has won 3 sets.
     */
    public boolean canContinuePlaying() {
        PlayerDataTriplet pdtOfWinner = this.playerDataTriplets.stream().filter(pdt -> pdt.getScore() == 3).findFirst().orElse(null);
        if (pdtOfWinner == null) {
            return true;
        }
        pdtOfWinner.getPlayer().updateCredits(5);

        return false;
    }
}
