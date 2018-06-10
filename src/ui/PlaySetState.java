/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import domain.DomainController;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PlaySetState extends GameAppState {

    private final Scanner IN = new Scanner(System.in);
    private final DomainController dc;
    private List<String> pNames;
    private List<Integer> pScores;
    private List<String[]> playerBoards = new ArrayList<>();
    private List<Integer> pTotalScores = new ArrayList<>();
    private List<String> pGameDecks = new ArrayList<>();
    private boolean gameOver = true;

    public PlaySetState(GameApp gameApp, DomainController dc) {
        super(gameApp);
        this.dc = dc;
        pNames = dc.listActiveParticipants();
        pScores = dc.listParticipantScores();
        pGameDecks = dc.getGameDecks();

        if (!dc.isSetActive()) {
            //System.out.println("Set is not active, so making new set.");
            this.dc.startSet();
        }

        playerBoards.addAll(dc.getCurrentBoardSituation());
        // and let the application decide it to be a 3x3 tile
    }

    @Override
    public void run() {
        playSet();
    }

    private void playSet() {

        // already grab the total scores so far because the first card has been dealt to the first player
        pTotalScores.addAll(dc.getTotalSetScores());

        // print the scoreboard and player names
        printTopOfBoard();

        //gameApp.toState(new PlayTurnApp(gameApp, dc, ));
        //gameApp.run();

        // init each of the players' 3x3 grid to empty slots
        // TODO - replace with domain call for filling the boarddddddddddddddddddddddddddddd
//        for (int i = 0; i < pNames.size(); i++) {
//            // test initialization to check if filling + printing works
//            playerBoards.add(new String[]{"P*10", "P*4", "P*6", "±*3", "-*2", "+*3", "E*0", "E*0", "E*0"});
//        }

        // draw the main playing area (3x3 grids)
        System.out.printf(drawBoard());

//        for (int i = 0; i < 10; i++) {
//            System.out.println("ACTIVE PLAYER: " + dc.getActivePlayerName());
//            endTurn();
//        }

        gameOver = false;

        while (!gameOver) {
            performAction();
            draw();
        }

        // TODO - if... game is tie -> show tie box else showWinnerBox()

        if (dc.isSetResultTied()) {
            showTieBox();
        } else {
            showWinnerBox();
        }

        // increment the game score of the winner
        dc.incrementParticipantScore(dc.getWinnerName());

        if (dc.canContinuePlaying()) {
            saveGameDialog();
        } else {
            // go back to main menu
        }

    }

    private void saveGameDialog() {
        int option = -1;

        while (isNotValid(option, 3)) {
            // TODO - resource bundles
            System.out.println(dc.getTextController().translateString("MESSAGE_SELECT_OPTION"));
            System.out.println(dc.getTextController().translateString("MESSAGE_SAVE_GAME_SET"));
            System.out.println(dc.getTextController().translateString("MESSAGE_SAVE_DISCARD"));
            System.out.println(dc.getTextController().translateString("MESSAGE_SAVE_QUIT"));
            option = IN.nextInt();
        }

        switch (option) {
            case 1:
                gameApp.toState(new SaveGameState(gameApp, this.dc));
                gameApp.run();
                break;
            case 2:
                System.out.println(dc.getTextController().translateString("MESSAGE_NEW_SCORE") + " : " + dc.getGameScores().toString());
                dc.startSet();
                break;
            case 3:

        }
    }

    private void showWinnerBox() {
        System.out.println(pad(49, "="));
        String winnerMessage = dc.getTextController().translateString("MESSAGE_CONGRAT");
        int lengthLeft = 45 - winnerMessage.length();
        System.out.printf("||%s%s%s||%n", pad(lengthLeft / 2, " "), winnerMessage, pad(lengthLeft / 2, " "));
        System.out.println(pad(49, "="));
        System.out.println("||" + pad(45, " ") + "||");
        System.out.printf("||\t\t" + dc.getTextController().translateString("MESSAGE_WINNER") + "%s||%n", pad(19, " "));
        System.out.printf("||\t\t\t\t...%s!%s||%n", dc.getWinnerName(), pad(27 - dc.getWinnerName().length(), " "));
        System.out.println("||" + pad(45, " ") + "||");

        System.out.println(pad(49, "="));
    }

    private void showTieBox() {
        System.out.println(pad(49, "="));
        String winnerMessage = dc.getTextController().translateString("MESSAGE_DAISY");
        int lengthLeft = 45 - winnerMessage.length();
        System.out.printf("||%s%s%s||%n", pad(lengthLeft / 2, " "), winnerMessage, pad(lengthLeft / 2, " "));
        System.out.println(pad(49, "="));
        System.out.println("||" + pad(45, " ") + "||");
        System.out.printf("||\t\t" + dc.getTextController().translateString("MESSAGE_TIE") + "%s||%n", pad(17, " "));
        System.out.printf("||\t\t\t\t...%s!%s||%n", dc.getTextController().translateString("MESSAGE_NO_WINNER"), pad(5, " "));
        System.out.println("||" + pad(45, " ") + "||");

        System.out.println(pad(49, "="));
    }

    private void performAction() {
        int option = -1;
        option = getValidOption();

        System.out.println(dc.getTextController().translateString("OPTION_CHOSE") + " " + option);

        switch (option) {
            case 1: // END TURN
                dc.endTurn();
                break;
            case 2: // PLAY A CARD
                playCard();
                break;
            case 3: // STAND (FREEZE BOARD)
                standTurn();
                break;
            case 4: // FORFEIT THE GAME
                dc.concedeSet();
                break;
            default:
                throw new RuntimeException("ERROR_UNSUPPORTED");
        }

        if (option != 2) {
            dc.isGameOver();
        }
    }

    private void standTurn() {
        // TODO - maybe check if player is allowed to stand? Not sure if there are requirements...
        dc.standTurn();
    }

    private int getValidOption() {
        int option = -1;

        while (isNotValid(option, 4)) {
            // TODO - resource bundles
            System.out.println(dc.getTextController().translateString("MESSAGE_SELECT_ACTION"));
            System.out.println(dc.getTextController().translateString("MESSAGE_END_TURN"));
            System.out.println(dc.getTextController().translateString("MESSAGE_PLAY_CARD"));
            System.out.println(dc.getTextController().translateString("MESSAGE_STAND"));
            System.out.println(dc.getTextController().translateString("MESSAGE_CONCEDE"));
            option = IN.nextInt();
        }

        return option;
    }

    private void playCard() {
        if (!dc.hasCardToPlay()) {
            System.out.println(dc.getTextController().translateString("MESSAGE_NO_REMAINING"));
            performAction();
        } else {
            List<String> activeCards = dc.getActivePlayerGameDeck();
            int option = -1;

            while (isNotValid(option, activeCards.size())) {
                System.out.println(dc.getTextController().translateString("MESSAGE_SELECT_CARD"));
                for (int i = 0; i < activeCards.size(); i++) { // be careful with index mashing (zero based memory, 1 based UI)
                    System.out.println(i + 1 + ": " + drawCard(activeCards.get(i)));
                }
                option = IN.nextInt();
            }

            // decrement option by 1 to match the index of the possible cards again
            --option;

            String activeCard = activeCards.get(option);

            // CHECK IF CARD IS SWAPPABLE
            if (activeCards.get(option).charAt(0) == '±') {
                int swapoption = -1;

                while (isNotValid(swapoption, 2)) {
                    System.out.println(dc.getTextController().translateString("MESSAGE_SWAP_CARD"));
                    String card = drawCard(activeCard);
                    card = card.substring(2, 4);
                    System.out.printf(dc.getTextController().translateString("MESSAGE_POS") + " (%s)%n", card.replace('±', '+'));
                    System.out.printf(dc.getTextController().translateString("MESSAGE_NEG") + " (%s)%n", card.replace('±', '-'));

                    swapoption = IN.nextInt();
                }
                if (swapoption == 1) {
                    activeCard = activeCard.replace('±', '†'); // char for Swappable, but positive
                } else if (swapoption == 2) {
                    activeCard = activeCard.replace('±', '_'); // char for swappable, but negative
                }
            }

            if (!dc.playGameDeckCard(activeCard))
                throw new RuntimeException("https://www.youtube.com/watch?v=0n_Ty_72Qds");
            // TODO - check at domain level if score should be frozen after playing card


            // if playing the card made the player reach a score of twenty,
            // we shouldnt ask whether to end turn or stand
            // it should automatically stand
            if (dc.getActivePlayerScore() == 20) {
                dc.standTurn();
            } else {
                int choiceOption = -1;

                while (isNotValid(choiceOption, 2)) {
                    System.out.println(dc.getTextController().translateString("MESSAGE_SELECT_OPTION"));
                    System.out.println(dc.getTextController().translateString("MESSAGE_END_TURN"));
                    System.out.println(dc.getTextController().translateString("MESSAGE_STAND_2"));
                    choiceOption = IN.nextInt();
                }

                if (choiceOption == 1) {
                    dc.endTurn();
                } else {
                    dc.standTurn();
                }
            }


            getDomainInfo();
        }
    }

    private boolean isNotValid(int option, int maxincluded) {
        return option < 1 || option > maxincluded;
    }

    private void endTurn() {
        dc.endTurn();
        getDomainInfo();
        draw();
    }

    private void getDomainInfo() {
        pTotalScores.clear();
        pTotalScores.addAll(dc.getTotalSetScores());
        playerBoards.clear();
        playerBoards.addAll(dc.getCurrentBoardSituation());
        pGameDecks.clear();
        pGameDecks.addAll(dc.getGameDecks());
        gameOver = dc.isGameOver();
    }

    private void draw() {
        Application.clearScreen();
        getDomainInfo();
        printTopOfBoard();
        printBoardArea();
        //printPlayerDeckArea();
    }

    private void printBoardArea() {
        System.out.printf(drawBoard());
    }

    private void incrementScore(String pName) {
        dc.incrementParticipantScore(pName);
        this.pScores = dc.getGameScores();
    }

    private void printTopOfBoard() {
        System.out.println("=================================================");
        System.out.printf("||%18s PAZAAK %18s||%n", " ", " ");
        System.out.println("++----------------------+----------------------++");
        System.out.printf("|| %-15s %4s | %-4s %15s ||%n", pNames.get(0), pScores.get(0) + "/3", pScores.get(1) + "/3", pNames.get(1));
        System.out.printf("|| %-15s %4s | %-4s %15s ||%n", " ", pTotalScores.get(0), pTotalScores.get(1), " ");
        System.out.println("++----------------------+----------------------++");
        System.out.printf("||%22s|%22s||%n", " ", " ");
//        System.out.printf(drawCard(10));
//        System.out.printf(drawCard("±", 5));
//        System.out.printf(drawCard("+", 1));
//        System.out.printf(drawCard(0));
//        System.out.printf(drawEmptyCardSlot());


    }

    private String drawCard(String value) {
//        System.out.println("THIS IS THE VALUE" + value);
        // Cards are built like this:
        // they are a 3 length long string.
        // the first character is a preset.
        // either + for positive, - for negative, ± for swappable, or a space for a card from the set deck
        // or a capital e (E) if the card is a blank placeholder card
        // the next two characters are the face values
        // if the face value is not a 10 (meaning it's only 1 character long: 1-9) then leftmost character
        // of the two will be occupied, the last character will be left empty
        // if the card is a placeholder (aka first character is a capital E), then the last two characters will be left empty.
        String[] cardData = value.split("\\*");
        String preset = "";
        int val = Integer.parseInt(cardData[1]);
        if (cardData[0].equals("B")) {
            preset = "0";
        } else if (cardData[0].equals("E")) {
            preset = "";
            val = -1;
        } else {
            preset = cardData[0];
        }
        String out = "";
        out += "| ";
        if (val == -1) {
            out += "  ";
        } else {
            if (val == 10)
                out += val;
            else
                out += preset + val;
        }
        out += " |";
        return out;
    }

    private String drawCard(int value) {
        return drawCard("B*" + value);
    }

    private String drawCardBorder() {
        return "+----+";
    }

    private String drawEmptyCardSlot() {
        return drawCard("E*00");
    }

    private String drawBoard() {
        StringBuilder board = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            // board
            for (int j = 0; j < 3; j++) {
                // j = amt of rows to be drawn for a card representation
                board.append("|| ");
                if (j == 1) { // middle row of card drawing (aka where the card value is)
                    for (int k = 0; k < 6; k++) {
                        int player = (k < 3 ? 0 : 1);
                        //System.out.printf("PLAYER ID IS %s, value of i is: %s, value of k is: %s%n", player, i, k);
                        int callItMagic = -1;
                        callItMagic = i * 3 + k % 3;
                        board.append(drawCard(playerBoards.get(player)[callItMagic]));
                        //board += drawCard(5);
                        board.append(" ");
                        if (k == 2)
                            board.append("| ");
                    }
                    board.append("||%n");
                } else { // bottom row of cards
                    for (int k = 0; k < 6; k++) {
                        board.append(drawCardBorder());
                        board.append(" ");
                        if (k == 2)
                            board.append("| ");
                    }
                    board.append("||%n");
                }
            }
            board.append("|| ");
            board.append(pad(21, " "));
            board.append("|");
            board.append(pad(21, " "));
            board.append(" ||%n");
        }
        board.append("||");
        board.append(pad(22, "-"));
        board.append("+");
        board.append(pad(22, "-"));
        board.append("||%n");

        for (int i = 0; i < pNames.size(); i++) {
            board.append("|| ");
            board.append(pNames.get(i));
            board.append("'s GAME DECK: %n");
            board.append("|| ");
            for (int j = 0; j < 4; j++) {
                board.append(drawCardBorder());
                board.append(" ");
            }
            board.append(pad(16, " "));
            board.append("||%n");
            board.append("|| ");
            for (int j = 0; j < 4; j++) {
                board.append(drawCard(pGameDecks.get((i * 4) + j)));
                board.append(" ");
            }
            board.append(pad(16, " "));
            board.append("||%n");
            board.append("|| ");
            for (int j = 0; j < 4; j++) {
                board.append(drawCardBorder());
                board.append(" ");
            }
            board.append(pad(16, " "));
            board.append("||%n");

        }
        board.append(pad(49, "="));
        board.append("%n");

        return board.toString();
    }

    private String pad(int count, String character) {
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < count; i++) {
            out.append(character);
        }

        return out.toString();
    }


}
