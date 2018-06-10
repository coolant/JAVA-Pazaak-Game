package domain;

import exceptions.InvalidNameException;
import exceptions.InvalidPlayerCountException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.Mapper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * Primary controller, delegates operations to various domain classes and
 * handles requests to and from the presentation and persistence layer
 * <p>
 * Also keeps track of the application wide language and has behaviour to
 * translate from a dictionary file.
 *
 * @author Aykut Yilmaz - aykut.yilmaz.u1495@student.hogent.be
 */
public class DomainController{

    //GUI
    private ObservableList<Player> showPlayersList;
    private ObservableList<String> selectedPlayers;
    private Mapper mapper;
    private PlayerRepository playerRepo;
    private Game currentGame = null;
    private CardRepository cardRepo;
    private GameRepository gameRepo;
    private TextController text;

    /**
     * Creates a new instance of DomainController
     *
     */
    public DomainController(){
        // TODO - remove the throw and catch the sql exception in the persistence layer
        try
        {
            this.mapper = new Mapper();
            this.text = new TextController();
            this.playerRepo = new PlayerRepository(mapper);
            this.cardRepo = new CardRepository(mapper);
            this.gameRepo = new GameRepository(mapper);
            
            //GUI
            showPlayersList = FXCollections.observableArrayList(playerRepo.getPlayers());
            selectedPlayers = FXCollections.observableArrayList();
        } catch (SQLException e) {
            System.err.print(text.translateString("ERROR_DATABASE"));
        }
    }

    /**
     * Makes a call to the player repository to grab all the players
     *
     * @return Returns a List of Player objects
     */
    private List<Player> getPlayers()
    {
        return playerRepo.getPlayers();
    }

    /**
     * Makes a call to the Card Repository to grab the default Card Stack.
     *
     * @return A List of Card objects.
     */
    private List<Card> getDefaultCardStack()
    {
        return cardRepo.getDefaultCardStack();
    }

    /**
     * Register a player in the player repository after checking for validity.
     *
     * @param name The desired player name (complying with
     * DR_SPELER_NAAM_LEEFTIJD)
     * @param birthYear The desired year of birth (complying with
     * DR_SPELER_NAAM_LEEFTIJD)
     * @return Returns true if registering the player was successful
     * @throws IllegalArgumentException if the name was already chosen, the name
     * was invalid, the birthdate was invalid
     */
    public boolean registerPlayer(String name, int birthYear) throws IllegalArgumentException, SQLException
    {
        try
        {
            Player toBeRegisteredPlayer = new Player(name, birthYear);
            if (!playerRepo.playerExists(toBeRegisteredPlayer.getName())) {
                playerRepo.addPlayer(toBeRegisteredPlayer);
                return true;
            }else{
                //throw new InvalidNameException(lang.getString("ERROR_UNIQUE_PLAYERNAME"));
                throw new InvalidNameException(text.translateString("ERROR_UNIQUE_PLAYERNAME"));
            }
        } catch (IllegalArgumentException iae) {
            //throw new IllegalArgumentException(lang.getString(iae.getMessage()));
            throw new IllegalArgumentException(text.translateString(iae.getMessage()));
        } catch (SQLException sqle) {
            throw new SQLException(sqle);
        }
    }


    /**
     * Checks if the provided player name is already present in the database for
     * another player
     *
     * @param name A player name String
     */
    public void validatePlayerName(String name)
    {
        if (playerRepo.playerExists(name))
        {
            throw new IllegalArgumentException(text.translateString("ERROR_UNIQUE_PLAYERNAME"));
        }
    }
    
    /**
     * Checks if the provided game name is already present in the database for
     * another game
     *
     * @param name A game name String
     */
    public void validateGameName(String name){
        if (gameRepo.gameExists(name))
        {
            throw new IllegalArgumentException(text.translateString("ERROR_UNIQUE_GAMENAME"));
        }
    }

    /**
     * Lists all player names of players that are suitable to play a game
     *
     * @return List of strings with player names as dictated by the game spec
     */
    public List<String> listAvailablePlayers() {
        return getPlayers().stream().map(Player::getName).collect(Collectors.toList());
    }

    /**
     * Grabs all participants in the currently active game.
     *
     * @return List of Strings with player names
     */
    public List<String> listActiveParticipants() {
        return this.currentGame.getParticipants().stream() // open stream
                .map(Player::getName) // voor elke speler mappen we de naam
                .collect(Collectors.toList()); // output naar een list
    }

    /**
     * Grabs all the cards in possession of a player
     * 
     * @param player A player name string
     * @return list with string with cards
     */
    public List<String> listCardsInPossessionOfPlayer(String player) {
        return PlayerRepository.findPlayer(player)
                .getCardsInPossession()
                .stream()
                .map(Card::giveDescription)
                .collect(Collectors.toList());
    }
    
    /**
     * Add cards to the sidedeck of a given player
     * 
     * @param player a name of a player string
     * @param cardStrings Array of card strings
     */
    public void addCardsToSideDeck(String player, String[] cardStrings)
    {
        List<Card> cards = new ArrayList<>();
        for (String card : cardStrings) {
            cards.add(getDefaultCardStack()
                    .stream()
                    .filter(e -> e.giveDescription().equals(card))
                    .findFirst()
                    .get());
        }
        this.currentGame.addGameDeckForPlayer(PlayerRepository.findPlayer(player), cards);
    }

    /**
     * Method that attempts to instantiate a Game object with the 2 player names
     * that were provided. Checks to see if the correct number of players
     *
     * @param playerNames a List of Strings containing all the player names
     * @throws InvalidPlayerCountException if the amount of players provided in
     * the list differs from expected value
     */
    public void startNewGame(List<String> playerNames)
    {
        int DR_SPELERS_AANTAL = 2;

        List<Player> participantsClones = new ArrayList<>();

        playerNames.forEach((pName) -> participantsClones.add(PlayerRepository.findPlayer(pName)));

        if (participantsClones.size() == DR_SPELERS_AANTAL)
        {
              this.currentGame = new Game(participantsClones);
//            this.currentGame = new Game(participantsClones, new SimpleCard[][]{
//                    {
//                            new SimpleCard(CardType.POSITIVE, 1),
//                            new SimpleCard(CardType.POSITIVE, 1),
//                            new SimpleCard(CardType.POSITIVE, 1),
//                            new SimpleCard(CardType.POSITIVE, 1),
//                            new SimpleCard(CardType.POSITIVE, 1),
//                            new SimpleCard(CardType.POSITIVE, 1)
//                    },
//                    {
//                            new SimpleCard(CardType.NEGATIVE, 2),
//                            new SimpleCard(CardType.NEGATIVE, 2),
//                            new SimpleCard(CardType.NEGATIVE, 2),
//                            new SimpleCard(CardType.NEGATIVE, 2),
//                            new SimpleCard(CardType.NEGATIVE, 2),
//                            new SimpleCard(CardType.NEGATIVE, 2)
//                    }});
        } else {
            throw new InvalidPlayerCountException();
        }
    }
    
    /**
     * set currentGame to the new game with GameName
     * 
     * @param gameName a game name string
     */
    public void continuePreviousGame(String gameName){
        this.currentGame = GameRepository.findGame(gameName);
    }


    /**
     * Grabs the amount of registered players in the game
     *
     * @return Returns a positive int with the amount of players so far
     * registered in the repository
     */
    public int countRegistrations()
    {
        return getPlayers().size();
    }

    /**
     * Grabs all the information of a player
     * 
     * @param pName a player name String
     * @return Array of Strings
     */
    public String[] grabPlayerInformation(String pName)
    {
        Player p = PlayerRepository.findPlayer(pName);
        String[] info = new String[3];
        info[0] = p.getName();
        info[1] = Integer.toString(p.getBirthYear());
        info[2] = Integer.toString(p.getCredits());
        return info;
    }

    /**
     * Checks if the game is ready to start
     * 
     * @return boolean 
     */
    public boolean isGameReadyToStart() {
        return this.currentGame.isReadyToStart();
    }

    /**
     * Grabs all the players without a sidedeck
     * 
     * @return list with String, player names
     */
    public List<String> listSideDecklessParticipants() {
        List<String> names = new ArrayList<>();
        this.currentGame.listUnreadyParticipants().forEach(p -> names.add(p.getName()));
        return names;
    }

    /**
     * Push a game to the database 
     * 
     * @param name a game name
     * @return return true if the game is saved
     * @throws SQLException 
     */
    public boolean pushGame(String name) throws SQLException{
        try{
            if (!gameRepo.gameExists(this.currentGame.getGameName())){
                this.currentGame.setGameName(name);
                gameRepo.saveGameWithPlayers(this.currentGame);
                return true;
            }else{
                gameRepo.updateGame(this.currentGame);
                return true;
            }
        } catch (IllegalArgumentException iae) {
            throw new IllegalArgumentException(text.translateString(iae.getMessage()));
        } 
    }
    
    /**
     * Lists all the games of a player
     * 
     * @param playerName 
     * @return list all games of a given player
     * @throws SQLException 
     */
    public List<String> listAllGamesOfPlayer(String playerName) throws SQLException{
        List<String> games = new ArrayList<>();
        Player p = PlayerRepository.findPlayer(playerName);

        List<Game> gamesOfPlayer = gameRepo.listAllGameOfPlayer(p);
        
        for(Game g: gamesOfPlayer){
            StringBuilder game = new StringBuilder();
            game.append(g.getGameName() + ";");
            for(int i = 0; i < g.getParticipants().size(); i++){
               game.append(g.getParticipants().get(i).getName() + ";" + g.getParticipantScores().get(i) + ";");
            }
            games.add(game.toString());
        }
        return games;
    }
    
    /**
     * Grabs the score of the participants
     * 
     * @return list of Integers with the score
     */
    public List<Integer> listParticipantScores() {
        return this.currentGame.getParticipantScores();
    }

    /**
     * Attempts to increment a given Game participant's overall score of sets won by one
     *
     * @param participantName the name of the game participant
     */
    public void incrementParticipantScore(String participantName) {
        try {
            Player participant = PlayerRepository.findPlayer(participantName);
            this.currentGame.incrementParticipantScore(participant);
        } catch (NoSuchElementException e) {
            System.err.println(text.translateString("ERROR_NOSUCHPLAYER"));
        }
    }

    /**
     * Retrieves everything on the table: both players' boards
     *
     * @return Returns an ordered list of ordered boards with playing cards on them
     */
    public List<String[]> getCurrentBoardSituation() {
        return this.currentGame.getCurrentSet().getBoardSituation();
    }

    /**
     * Helper method to initialize a new set.
     */
    public void startSet() {
        this.currentGame.initializeSet();
    }

    /**
     * Call method for when the active player decides to end their turn
     */
    public void endTurn() {
        this.currentGame.getCurrentSet().endTurn();
    }

    /**
     * Finds the active player in the set
     * @return Returns a String with the active player's name
     */
    public String getActivePlayerName() {
        return this.currentGame.getCurrentSet().getActivePlayer().getName();
    }

    /**
     * Finds the total set scores for every player
     *
     * Set score is the score contained within the set, being the combined score of all the cards currently on the table
     * @return Returns an ordered list of integers, indexed via play order of all the players scores.
     * Scores are constrained as being positive integers
     */
    public List<Integer> getTotalSetScores() {
        return this.currentGame.getCurrentSet().getTotalSetScores();
    }

    //Methodes voor GUI

    /**
     * Grabs all the players from the database
     * 
     * @return ObservableList with Players
     */
    public ObservableList<Player> getPlayerList(){
        return FXCollections.observableArrayList(playerRepo.getPlayers());
    }
    
    /**
     * Grabs all the game of a given player
     * 
     * @param player string of player name
     * @return ObservableList with games of given player
     */
    public ObservableList<Game> getGamesList(String player){
        return FXCollections.observableArrayList(gameRepo.listAllGameOfPlayer(PlayerRepository.findPlayer(player)));
    }

    /**
     * Add a player to the showPlayer list
     * 
     * @param playerName a String player name
     */
    public void addShowPlayer(String playerName){
        showPlayersList.add(PlayerRepository.findPlayer(playerName));
    }

    /**
     * Remove a player from the showPlayer list
     * 
     * @param player player object
     */
    public void removeShowPlayer(Player player){
        showPlayersList.remove(player);
    }

    /**
     * Grabs all the players that are selected
     * 
     * @return ObservableList of the player names
     */
    public ObservableList<String> getSelectedPlayers() {
        return FXCollections.unmodifiableObservableList(selectedPlayers);
    }
    
    /**
     * add a player to the selectedPlayer list
     * 
     * @param player player Object
     */
    public void addSelectedPlayer(Player player){
        selectedPlayers.add(player.getName());
    }

    /**
     * remove a player of the selectedPlayer list
     * 
     * @param playerName String playername
     */
    public void removeSelectedPlayer(String playerName){
        selectedPlayers.remove(playerName);
    }
    
    /**
     * Grabs the game scores
     * 
     * @return list of integers
     */
    public List<Integer> getGameScores() {
        return this.currentGame.getParticipantScores();
    }

    /**
     * Checks if a new instance of a GameSet was successfully created
     *
     * @return whether there is an active set being played
     */
    public boolean isSetActive() {
        return this.currentGame.getCurrentSet() != null;
    }

    /**
     * Grabs the cards of the players
     * 
     * @return list gamedeck of the players 
     */
    public List<String> getGameDecks() {
        List<Card> cards = this.currentGame.getAllGameDecks();
        List<String> cardsInfo = new ArrayList<>();

        for (Card c : cards) {
            if (c == null) {
                cardsInfo.add("E*0");
            } else {
                cardsInfo.add(c.toBoardString());
            }
        }
        return cardsInfo;
    }

    /**
     * Starts a virtual game to test
     *
     * Boilerplate method to ease debugging entry in a midpoint during a set
     */
    public void startVirtualGame() {
        List<Player> players = new ArrayList<>();
        List<Card> cards = new ArrayList<>();

        players.add(PlayerRepository.findPlayer("Senne"));
        players.add(PlayerRepository.findPlayer("TestKerel"));

        cards.add(new SimpleCard(CardType.SWAPPABLE, 2));
        cards.add(new SimpleCard(CardType.SWAPPABLE, 3));
        cards.add(new SimpleCard(CardType.SWAPPABLE, 4));
        cards.add(new SimpleCard(CardType.SWAPPABLE, 5));
        cards.add(new SimpleCard(CardType.SWAPPABLE, 2));
        cards.add(new SimpleCard(CardType.SWAPPABLE, 1));
//        cards.add(new SimpleCard(CardType.NEGATIVE, 6));
//        cards.add(new SimpleCard(CardType.NEGATIVE, 6));
//        cards.add(new SimpleCard(CardType.NEGATIVE, 6));
//        cards.add(new SimpleCard(CardType.NEGATIVE, 6));
//        cards.add(new SimpleCard(CardType.NEGATIVE, 6));
//        cards.add(new SimpleCard(CardType.NEGATIVE, 6));

        this.currentGame = new Game(players);
        for (Player player : players) {
            this.currentGame.addGameDeckForPlayer(player, cards);
        }

    }


    /**
     * Checks if the active player has any player cards left to play
     *
     * @return true if the player has any cards left
     */
    public boolean hasCardToPlay() {
        return this.currentGame.getCurrentSet().hasCardToPlay();
    }

    /**
     * Finds the active player and returns their game deck (max 4 cards, of the 6 possible cards the player chose)
     *
     * @return Returns an ordered list of String representations of player cards
     */
    public List<String> getActivePlayerGameDeck() {
        List<Card> cards = this.currentGame.getGameDeckForPlayer(this.currentGame.getCurrentSet().getActivePlayer());
        List<String> cardInfo = new ArrayList<>();

        cards.forEach(c -> cardInfo.add(c.toBoardString()));

        return cardInfo;
    }

    /**
     * Attempts to play a card on the table.
     *
     * Note: The given card must be both in the player's hand and there must also be room on the table to play the card.
     *
     * If complex cards are added, other restraints and conditions may apply
     *
     * @param card A valid card string to be played, contained in the active player's hand
     * @return Returns a boolean of whether the attempted play was valid. True if the card was successfully played on the table
     */
    public boolean playGameDeckCard(String card) {
        CardType type;

        switch (card.charAt(0)) {
            case '+':
                type = CardType.POSITIVE;
                break;
            case '-':
                type = CardType.NEGATIVE;
                break;
            case '†':
                type = CardType.POSITIVESWAPPABLE;
                break;
            case '_':
                type = CardType.NEGATIVESWAPPABLE;
                break;
            default:
                throw new RuntimeException("ERROR_GENERIC");
        }

        int value = Integer.parseInt(String.valueOf(card.charAt(2)));

        SimpleCard cardToPlay = new SimpleCard(type, value);

        return this.currentGame.getCurrentSet().playCard(cardToPlay);

    }

    /**
     * Caller method to freeze the active player's board
     */
    public void standTurn() {
        this.currentGame.getCurrentSet().standTurn();
    }

    /**
     * Caller method to forfeit the active player's set
     */
    public void concedeSet() {
        this.currentGame.getCurrentSet().concede();
    }

    /**
     * Method to determine if the set has reached end conditions
     *
     * @return Returns true if the game is finished and a winner/tie has been found
     */
    public boolean isGameOver() {
        return this.currentGame.getCurrentSet().isGameOver();
    }

    /**
     * Finds the set score (combined score of all the card's on the active player's side of the table) for the active player
     *
     * @return Returns a positive integer between 0 and 35 (19 on table, end turn, get 10, total of 29, play +6 card, get 35)
     */
    public int getActivePlayerScore() {
        return this.currentGame.getCurrentSet().getActivePlayerSetScore();
    }

    /**
     * Returns the name of the winner who won the set
     *
     * @return A non-null String of the winner's name
     */
    public String getWinnerName() {
        return this.currentGame.getCurrentSet().getWinner().getName();
    }

    /**
     * returns the instance of the text controller singleton
     *
     * @return Returns the active TextController instance
     */
    public TextController getTextController() {
        return text;
    }

    /**
     * Finds out if the current set was a tie
     *
     * @return Returns true if the set was a tie, false if it was not (either game not finished, or winner was found)
     */
    public boolean isSetResultTied() {
        return this.currentGame.getCurrentSet().isTiedGame();
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
        return this.currentGame.canContinuePlaying();
    }
}
