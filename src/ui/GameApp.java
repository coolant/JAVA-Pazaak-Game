package ui;

import domain.DomainController;
import java.util.Arrays;

public class GameApp implements Application{

    private GameAppState currentState;
    private DomainController dc;

    public GameApp(int getal, DomainController dc) {
        this.dc = dc;
        switch (getal) {
            case 2:
                toState(new MakeGameState(this, this.dc));
                break;
            case 3:
                toState(new LoadGameState(this, this.dc));
                break;
            case 4:
                dc.startVirtualGame();
                toState(new PlayGameState(this, this.dc));
                break;
            case 5:
                dc.startVirtualGame();
                toState(new SaveGameState(this, this.dc));
                break;
                
            default:
                throw new RuntimeException("");
        }
    }

    /**
     * 
     * @param state
     */
    protected void toState(GameAppState state) {
        currentState = state;
    }

    @Override
    public void run() {
        currentState.run();
    }
    
    
    
   


}