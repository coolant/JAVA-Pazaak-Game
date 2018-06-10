/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

/**
 *
 * @author Aykut Yilmaz - aykut.yilmaz.u1495@student.hogent.be
 */

public abstract class GameAppState {

	protected GameApp gameApp;

	public GameAppState(GameApp gameApp) {
            this.gameApp = gameApp;
	}

	public void run() {
            throw new UnsupportedOperationException();
	}

}
    
