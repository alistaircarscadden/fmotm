package fmotm;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import fmotm.states.Game;
import fmotm.states.MainMenu;

public class Application extends StateBasedGame {

    // Game States
	public static final int SPLASH       = 0;
    public static final int MAINMENU     = 1;
    public static final int GAME         = 2;

    // Application Properties
    public static final int WIDTH   = 1024;
    public static final int HEIGHT  = 768;
    public static final int FPS     = 60;

    public Application(String appName) {
        super(appName);
    }

    public void initStatesList(GameContainer gc) throws SlickException {
    	//this.addState(new Splash(SPLASH));
    	this.addState(new MainMenu(MAINMENU));
    	this.addState(new Game(GAME));
    }

    public static void main(String[] args) {
        try {
            AppGameContainer app = new AppGameContainer(new Application("First Man On The Marble"));
            app.setDisplayMode(WIDTH, HEIGHT, false);
            app.setTargetFrameRate(FPS);
            app.setShowFPS(false);
            app.start();
        } catch(SlickException e) {
            e.printStackTrace();
        }
    }
}
