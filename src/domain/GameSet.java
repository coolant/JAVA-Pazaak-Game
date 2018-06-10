package domain;

import exceptions.GameIsTiedException;
import exceptions.GameNotFinishedException;

import java.util.*;
import java.util.stream.IntStream;

/**
 * Class containing behavior for a set in a game.
 * Domain rules state sets are played in a game until one of the participants
 * has won 3 sets.
 *
  * @author Aykut Yilmaz - aykut.yilmaz.u1495@student.hogent.be
 */
public class GameSet {
    private Stack<BoardCard> playStack;
    private LinkedHashMap<Player, Board> playerBoards;
    private TreeMap<Player, List<Card>> playerGameDecks = new TreeMap<>();
    private Map<Player, Boolean> isFrozenStatus;
    private boolean gameOver = true;
    private Player activePlayer;

    public GameSet(Map<Player, List<Card>> playerDeckMap) {
        this.playerGameDecks.putAll(playerDeckMap);
        List<Player> unsortedPlayers = new ArrayList<>(playerGameDecks.keySet());
        List<Player> sortedPlayers = calculatePlayOrder(unsortedPlayers);
        playStack = new Stack<>();
        generateDeck();

        // initialiseer voor elke speler een nieuw speelbord
        this.playerBoards = new LinkedHashMap<Player, Board>(sortedPlayers.size()) {{
            sortedPlayers.forEach(p -> put(p, new Board()));
        }};

        //  initialiseer elke speler op niet frozen voor de set
        this.isFrozenStatus = new HashMap<Player, Boolean>(sortedPlayers.size()) {{
            sortedPlayers.forEach(p -> put(p, false));
        }};
        this.gameOver = false;

        playTurn();
        // Check turn cycling?
    }

    /**
     * Uses a multi-layered comparator to determine the order of play:
     * First ordered by year of birth, oldest person to play first,
     * then ordered lexicographically by player name.
     *
     * @param players Requires a list of player objects to compare to eachother
     * @return Returns an ordered list of player objects
     */
    private List<Player> calculatePlayOrder(List<Player> players) {
        players.sort(Comparator
                .comparing(Player::getBirthYear)
                .thenComparing(p -> p.getName().toLowerCase()));
        this.activePlayer = players.get(0);
        return players;
    }

    /**
     * Determines if a card should be taken from the top of the game deck (4 * 1 through 10 game cards)
     * and then lays it on the active player's table board
     */
    public void playTurn() {

        if (!gameOver) {
            // Check for null pointer!
            Board currentBoard = this.playerBoards.get(activePlayer);

            if (isFrozenStatus.values().stream().allMatch(f -> f == true)) {// if there are still unfrozen players
                this.gameOver = true;
            } else {
                // pop a card from the game stack
                BoardCard poppedCard = this.playStack.pop();
                // place the card on the active player's board
                currentBoard.placeCard(poppedCard);
                this.playerBoards.replace(activePlayer, currentBoard);
            }
        }
    }

    /**
     * Generates 4 sets of 10 cards (from 1 through 10) and shuffles them
     */
    private void generateDeck() {
        for (int i = 0; i < 4; i++) {
            for (int j = 1; j < 11; j++) {
                playStack.add(new BoardCard(j));
            }
        }
        Collections.shuffle(playStack);
    }

    /**
     * Retrieves everything on the table: both players' boards
     *
     * @return Returns an ordered list of ordered boards with playing cards on them
     */
    public List<String[]> getBoardSituation() {
        List<String[]> boards = new ArrayList<>();
        playerBoards.values().forEach(tab -> boards.add(tab.displayBoard()));
        return boards;
    }

    /**
     * Attempts to end the currently active player's turn
     */
    public void endTurn() {
        if (!gameOver) {
            if (isFrozenStatus.values().stream().anyMatch(f -> f == false)) {// if there are still unfrozen players
                if (!isFrozenStatus.get(getNextActivePlayer())) { // check if the next active player is frozen
                    incrementActivePlayer();
                    playTurn();
                } else {
                    incrementActivePlayer();
                    endTurn();
                }
            }
        }

    }

    /**
     * Helper method to shift the player turn cycle to the next person
     */
    private void incrementActivePlayer() {
        this.activePlayer = getNextActivePlayer();
    }

    /**
     * Finds out who the next active player is.
     *
     * @return Returns a player object of the next active player
     */
    private Player getNextActivePlayer() {
        List<Player> players = new ArrayList<>(this.playerGameDecks.keySet());
        int index = players.indexOf(this.activePlayer);
        index = (index + 1) % this.playerGameDecks.size(); // mod by how many players
        return players.get(index);
    }

    /**
     * Finds the active player
     * @return Returns a player object of the person who's turn is currently active
     */
    public Player getActivePlayer() {
        return activePlayer;
    }

    /**
     * Finds all set scores (scores of the combined values of the cards on each player's side of the table)
     * @return Returns an ordered (by participant) list of positive integer values representing set scores.
     */
    public List<Integer> getTotalSetScores() {
        List<Integer> scores = new ArrayList<>();

        this.playerBoards.values().forEach(b -> scores.add(b.getScore()));

        return scores;
    }

    /**
     * Finds out if the currently active player has any game deck cards left
     *
     * @return whether the active player has any cards left to play
     */
    public boolean hasCardToPlay() {
        return playerGameDecks.get(this.activePlayer).size() > 0;
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
    public boolean playCard(SimpleCard card) {
        CardType type = card.getType();
        if (card.getType().equals(CardType.POSITIVESWAPPABLE) || card.getType().equals(CardType.NEGATIVESWAPPABLE)) {
            card.setType(CardType.SWAPPABLE);
        }

        // changed type to regular swappable so we can check if contained in game deck list
        if (playerGameDecks.get(this.activePlayer).contains(card)) {
            // update player game decks
            List<Card> cards = playerGameDecks.get(this.activePlayer);
            cards.remove(card);
            playerGameDecks.remove(this.activePlayer);
            playerGameDecks.put(this.activePlayer, cards);

            //resetting card to original positive swappable / negative swappable so we can change score
            card.setType(type);

            // updating the board and score
            Board currentBoard = this.playerBoards.get(this.activePlayer);
            currentBoard.placeCard(card);
            this.playerBoards.put(this.activePlayer, currentBoard);

            return true;
        }
        return false;
    }

    /**
     * Attempts to freeze the currently active player's board in the current state.
     */
    public void standTurn() {
        if (!gameOver) {
            isFrozenStatus.remove(this.activePlayer);
            // TODO - when score is 20, automatically call this method to make sure
            // they are frozen and can't play any more.
            isFrozenStatus.put(this.activePlayer, true);
            incrementActivePlayer();
            playTurn();
        }
    }

    /**
     * Attempts to forfeit the game
     */
    public void concede() {
        Player concedingPlayer = this.activePlayer;
        // reset the conceding player's score to 0, so we know who won the game
        this.playerBoards.get(concedingPlayer).resetScore();
        this.gameOver = true;
    }

    /**
     * Goes through a checklist to see if a set has reached an end condition
     */
    private void checkGameOver() {
        List<Board> boards = new ArrayList<>();
        boards.addAll(this.playerBoards.values());

        // one of the boards has nine cards on it
        for (Board b : boards) {
            if (b.getCardCount() == 9) {
                this.gameOver = true;
            }
        }

        // all scores are 20
        // (perhaps implied since when reaching 20 frozen is set to true?)
        if (boards.stream().allMatch(b -> b.getScore() == 20)) {
            this.gameOver = true;
        }

        // all boards are frozen
        if (this.isFrozenStatus.values().stream().allMatch(v -> v)) {
            this.gameOver = true;
        }

        // any board has a score over 20 at the end of the turn
        if (boards.stream().anyMatch(b -> b.getScore() > 20)) {
            this.gameOver = true;
        }


    }

    /**
     * Finds out if the set is in fact over
     * @return Returns true if the set is finished
     */
    public boolean isGameOver() {
        checkGameOver();
        return gameOver;
    }

    /**
     * Finds out if all players' scores are equal, thus tying the game
     * @return Returns true if all players' scores are equal.
     * @throws GameNotFinishedException if this method was called when a game was still in progress.
     */
    public boolean isTiedGame() throws GameNotFinishedException {
        if (isGameOver()) {
            List<Board> boards = new ArrayList<>();
            boards.addAll(this.playerBoards.values());
            // returns true if the all scores are equal, thus tying the game up.
            return boards.stream().allMatch(b -> Objects.equals(b.getScore(), boards.get(0).getScore()));
        }
        throw new GameNotFinishedException();
    }

    /**
     * Finds out what the set score of the currently active player is.
     * @return a postive integer contrained from 0 to 35 (19, then a 10 card, then a +6 card)
     */
    public int getActivePlayerSetScore() {
        return this.playerBoards.get(this.activePlayer).getScore();
    }

    /**
     * Finds whoever won the game.
     * <p>
     * Important to note: we have already determined that a winner is to be found, the game is not a tie.
     *
     * @return a Player object of the winner
     */
    public Player getWinner() {
        // TODO - actually find out who won...

        // create the variable and then set it to values depending on the situation
        Map.Entry<Player, Board> winner = null;

        if (isTiedGame()) {
            throw new GameIsTiedException();
        }

        // if everyone pressed 'stand', aka froze their scoreboards
        if (isFrozenStatus.values().stream().allMatch(fr -> fr == true)) {
            // in this scenario all players froze/stood and we are determining who has the highest score
            if (playerBoards.values().stream().allMatch(board -> board.getScore() <= 20)) {
                // we have determined that all players have a score under 20
                int[] scores = new int[playerBoards.size()];
                int highest = Arrays.stream(this.playerBoards.values()
                        .stream()
                        .flatMapToInt(b -> IntStream.of(b.getScore())).toArray())
                        .max()
                        .orElse(0);


                for (Map.Entry<Player, Board> entry : playerBoards.entrySet()) {
//                    Player p = entry.getKey();
                    Board b = entry.getValue();

                    if (b.getScore() == highest) {
                        winner = entry;
                    }
                }

                // we should have someone with that score.
                if (winner == null)
                    throw new RuntimeException("ERROR_GENERIC");
            }
        }

        // if no winner yet, find someone who has 9 cards and a score below or equal to 20
        if (winner == null) {
            winner = this.playerBoards.entrySet()
                    .stream()
                    .filter(e -> e.getValue().getCardCount() == 9)
                    .filter(e -> e.getValue().getScore() <= 20)
                    .findFirst()
                    .orElse(null);
        }

        // if no one was found yet, try finding someone with score equal to 20
        if (winner == null) {
            long count = this.playerBoards.entrySet()
                    .stream()
                    .filter(e -> e.getValue().getScore() == 20)
                    .count();

            if (count == 1) {
                winner = this.playerBoards.entrySet()
                        .stream()
                        .filter(e -> e.getValue().getScore() == 20)
                        .findFirst()
                        .orElse(null);
            }
        }

        // i guess only thing that's left is someone got over 20
        if (winner == null) {
            return this.playerBoards.entrySet().stream()
                    .filter(e -> e.getValue().getScore() <= 20)
                    .findFirst()
                    .orElse(null).getKey();
        }

        if (winner != null)
            return winner.getKey();
        throw new GameNotFinishedException();
    }
}
