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

/**
 *
 * @author Aykut Yilmaz - aykut.yilmaz.u1495@student.hogent.be
 */
public class MakeGameState extends GameAppState {

    private static final Scanner IN = new Scanner(System.in);
    private static final int DR_SPELERS_AANTAL = 2;
    private final DomainController dc;
    
    public MakeGameState(GameApp gameApp, DomainController dc) {
        super(gameApp);
        this.dc = dc;
    }
    
    @Override
    public void run() {
        makeNewGame();
    }

    private String translateString(String toTranslate) {
        return dc.getTextController().translateString(toTranslate);
    }

    public void makeNewGame() {
        //TODO - delegate this to different methods or even application screens to declutter the code...
        // This is an absolute nightmare to debug! -Denis
        try {
            

            List<String> chosenParticipants = new ArrayList<>(DR_SPELERS_AANTAL); // account for two possible valid participants
            
            do{
                chosenParticipants.clear();
                List<String> availablePlayers = dc.listAvailablePlayers(); // grab all player names
                for (int i = 0; i < DR_SPELERS_AANTAL; i++) {
                    
                    showPlayers(availablePlayers);

                    // TODO - fix bug where input mismatches indexes
                    System.out.println(translateString("MESSAGE_SELECT_PLAYER"));
                    int input = IN.nextInt();

                    if (input >= 0 && input < availablePlayers.size()) {
                        chosenParticipants.add(availablePlayers.get(input));
                        System.out.printf(dc.getTextController().translateString("MESSAGE_SELECTED_PLAYER") + ": %s.%n", availablePlayers.get(input).toUpperCase());
                        availablePlayers.remove(input); 
                    }
                }
                Application.clearScreen();
                showSelectedPlayers(chosenParticipants);
            }while(!confirmPlayerSelection());
            // 2 players were successfully added to the participants hashset
            System.out.println(dc.getTextController().translateString("MESSAGE_SELECTED_PLAYER_SUCCES"));
            
            dc.startNewGame(chosenParticipants);
            
            while (!dc.isGameReadyToStart()) {
                // TODO - needs MOAR debugging!!! UGHHH
                List<String> unreadyParticipants = dc.listSideDecklessParticipants();
                int uPCount = unreadyParticipants.size();
                int selectedParticipant;

                System.out.printf(dc.getTextController().translateString("MESSAGE_STILL_PLAYERS"),
                        uPCount, uPCount == 1 ? "" : "s", uPCount == 1 ? "s" : "");
                System.out.println("-----------------");

                int teller = 0;
                // TODO - check mail reply to leen vuyge sent on Sat 18 march
                // on whether or not to loop over all players automatically
                for (String unreadyParticipant : unreadyParticipants) {
                    System.out.print(teller++ + ") ");
                    System.out.println(unreadyParticipant);
                }

                System.out.println(translateString("MESSAGE_SELECT_PLAYER_FOR_SIDEDECK_CREATION"));
                int input = IN.nextInt();

                if (input >= 0 && input < unreadyParticipants.size()) {
                    System.out.println("correct");
                    selectedParticipant = input;
                    System.out.printf(dc.getTextController().translateString("MESSAGE_SELECTED") + " %s%n", unreadyParticipants.get(selectedParticipant));

                   // TODO - call for these fools to get their side decks registered!
                   gameApp.toState(new CreateSideDeckState(gameApp,dc , unreadyParticipants.get(selectedParticipant)));
                   gameApp.run();
                }
                
            }
            gameApp.toState(new PlayGameState(gameApp, dc));
            gameApp.run();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
            IN.nextLine();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(dc.getTextController().translateString("ERROR_INVALIDINPUT_GENERIC"));
            IN.nextLine();
        }
    }
    
    
    private void showPlayers(List<String> players){
        System.out.println(translateString("MESSAGE_LIST_OF_AVAILABLE_PLAYERS"));
        int teller = 0;
        for (String player : players) {
            System.out.print(teller++ + ") ");
            System.out.println(player);
        }
    }
    
    private void showSelectedPlayers(List<String> players){
        // TODO - resource bundles
        System.out.printf(dc.getTextController().translateString("MESSAGE_SELECTED_PLAYERS") + ":%n", players.size());
        System.out.println("----------------------------------------------------------------");
        for (String chosenParticipant : players) {
            String[] pInfo = dc.grabPlayerInformation(chosenParticipant);
            String pName = pInfo[0];
            int pBY = Integer.parseInt(pInfo[1]);
            int pCredits = Integer.parseInt(pInfo[2]);
            // TODO - resource bundles
            System.out.printf(dc.getTextController().translateString("MESSAGE_SHOWDETAIL_PLAYER"), pName.toUpperCase(), pBY, pCredits);
        }
        System.out.println("----------------------------------------------------------------");
    }
    
    private boolean confirmPlayerSelection(){
        System.out.println(dc.getTextController().translateString("MESSAGE_CONFIRM_PLAYERS"));
        if (IN.next().substring(0, 1).toLowerCase().equals("y")) {
            return true;
        }
        // TODO - if not correct, selected players should be readded to the player repo players list
        System.out.println(dc.getTextController().translateString("MESSAGE_NOT_SELECTED"));
        return false;
    }
    
    
}
