/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import domain.DomainController;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Aykut Yilmaz - aykut.yilmaz.u1495@student.hogent.be
 */
public class LoadGameState extends GameAppState {

    private final Scanner IN = new Scanner(System.in);
    private DomainController dc;
    
    public LoadGameState(GameApp gameApp, DomainController dc) {
        super(gameApp);
        this.dc = dc;
    }

    @Override
    public void run(){
        loadGame();
    }
    
    public void loadGame(){
        List<String> currentGames;
        try {
            currentGames = dc.listAllGamesOfPlayer(getPlayerName());
            if(currentGames.size() > 0){
                showGames(currentGames);
                System.out.println(dc.getTextController().translateString("MESSAGE_SELECT_GAME"));
                int input = IN.nextInt();

                if (input >= 0 && input < currentGames.size()) {
                    String game = currentGames.get(input);
                    String[] gameGegevens = game.split(";");
                    dc.continuePreviousGame(gameGegevens[0]);
                    gameApp.toState(new PlayGameState(gameApp, dc));
                    gameApp.run();
                }
            }else{
                System.out.println(dc.getTextController().translateString("MESSAGE_NO_GAME"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoadGameState.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void showGames(List<String> games){
        int teller = 0;
        System.out.println(dc.getTextController().translateString("HEADER_SHOW_GAMES"));
        for(String g:games){
            String[] gameGegevens = g.split(";");
            System.out.println(teller++ + ")\t" + gameGegevens[0] + "\t" + gameGegevens[1] + " vs. " + gameGegevens[3] 
                    + "\t\t" + gameGegevens[2] + "-" + gameGegevens[4]);
        }
    }
    
    private String getPlayerName(){
        System.out.println(dc.getTextController().translateString("ERROR_VALID_PLAYERNAME"));
        return IN.next();
    }
        
}
