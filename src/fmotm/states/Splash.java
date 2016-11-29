package fmotm.states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import fmotm.Application;

public class Splash extends BasicGameState {
	
	private int stateID;
	
	private Image titleSplash;
	private long closeTime;
	
	public Splash(int ID) {
		super();
		this.stateID = ID;
		closeTime = System.currentTimeMillis() + 3000;
	}

	// init-method for initializing all resources
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		titleSplash = new Image("res/title.png");
		titleSplash.setFilter(Image.FILTER_NEAREST);
		titleSplash = titleSplash.getScaledCopy(6f);
	}

	// render-method for all the things happening on-screen
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		titleSplash.draw(gc.getWidth() / 2 - titleSplash.getWidth() / 2, gc.getHeight() / 2 - titleSplash.getHeight() / 2);
	}

	// update-method with all the magic happening in it
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int arg2) throws SlickException {
		//System.out.println(System.currentTimeMillis() > closeTime);
		if(System.currentTimeMillis() > closeTime) {
			sbg.enterState(Application.MAINMENU);
		}
	}

	// Returning 'ID' from class 'MainMenu'
	@Override
	public int getID() {
		return stateID;
	}

}
