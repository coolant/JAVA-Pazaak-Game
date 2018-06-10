/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import domain.DomainController;

import java.util.Scanner;

/**
 *
 * @author Aykut Yilmaz - aykut.yilmaz.u1495@student.hogent.be
 */
public class PlayGameState extends GameAppState{
    
    
    private final Scanner IN = new Scanner(System.in);
    private final DomainController dc;
    
    public PlayGameState(GameApp gameApp, DomainController dc) {
        super(gameApp);
        this.dc = dc;
    }
    
    @Override
    public void run(){
       playGame();
    }
    
    public void playGame(){
        if (dc.isGameReadyToStart()) {
            gameApp.toState(new PlaySetState(gameApp, dc));
            gameApp.run();
            } else {
            System.out.println("MESSAGE_NOT_READY_TO_PLAY");
        }
    }
    
}
