/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import configuration.Configuration;
import domain.DomainController;
import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;
import java.util.Scanner;

/**
 *
 * @author Aykut Yilmaz - aykut.yilmaz.u1495@student.hogent.be
 */
public class CreateSideDeckState extends GameAppState {
    
    private final Scanner IN = new Scanner(System.in);
    private final DomainController dc;
    private final String player;
    private final int DR_SELECT_CARDS = Integer.parseInt(Configuration.getProp("DR_SELECT_CARDS"));
    
    public CreateSideDeckState(GameApp gameApp, DomainController dc, String player) {
        super(gameApp);
        this.dc = dc;
        this.player = player;
    }
    
    @Override
    public void run() {
        createSideDeck(this.player);
    }
    
    public void createSideDeck(String player){
        List<String> chosenCards = new ArrayList<>();
        Application.clearScreen();
        System.out.println(player);
        do{
            chosenCards.clear();
            selectCardsForSideDeck(chosenCards, player);
            showSelectedCards(chosenCards);
        }while(!conformSideDeck());
        dc.addCardsToSideDeck(player, chosenCards.toArray(new String[chosenCards.size()]));
        chosenCards.clear();
    }
    
    private void selectCardsForSideDeck(List<String> cards , String player){
        List<String> availableCards = dc.listCardsInPossessionOfPlayer(player);
        while (cards.size() < DR_SELECT_CARDS) {
            System.out.println(dc.getTextController().translateString("MESSAGE_ALL_CARD") + " " + player);
            int teller = 0;

            for (String card : availableCards) {
                System.out.println(teller++ + ") " + card);
            }
            System.out.println(teller + ") " + dc.getTextController().translateString("MESSAGE_BUY_A_CARD"));
            System.out.println(dc.getTextController().translateString("MESSAGE_SELECT_CARD"));
            int input = IN.nextInt();
            if (input >= 0 && input < availableCards.size()) {
                cards.add(availableCards.get(input));
                System.out.printf(dc.getTextController().translateString("MESSAGE_SUCCESSFULL_CARDS") + ": %s.%n", availableCards.get(input).toUpperCase());
                availableCards.remove(input);
            }else if(input == availableCards.size()){
                //TODO - HILDA - player buys card
                System.out.println("Buy card");
            }
        }
            
    }
    
    private boolean conformSideDeck(){
        System.out.println("----------------------------------------------------------------");
        System.out.println(dc.getTextController().translateString("MESSAGE_CONFIRM_CARDS"));
        if (IN.next().substring(0, 1).toLowerCase().equals("y")) {
            System.out.println(dc.getTextController().translateString("MESSAGE_SIDEDECK_CREATED"));
            return true;
        }
        System.out.println(dc.getTextController().translateString("MESSAGE_CREATE_NEW_SIDEDECK"));
        return false;
    }
    
    private void showSelectedCards(List<String> cards){
        System.out.println("\n" + dc.getTextController().translateString("MESSAGE_SELECTED_SIDEDECK"));
        System.out.println("----------------------------------------------------------------");
        for(String chosenCard: cards){
            System.out.println(chosenCard);
        }
    }
    
}
