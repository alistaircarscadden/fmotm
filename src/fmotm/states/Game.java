package fmotm.states;

import java.util.BitSet;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import fmotm.game.world.World;

public class Game extends BasicGameState {
	
	private int stateID;
	
	private World world;
	private BitSet keys;
	
	public Game(int ID) {
		super();
		this.stateID = ID;
		keys = new BitSet(256);
		world = new World(keys);
		world.loadWorld("res/lev/lev1");
	}

	// init-method for initializing all resources
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
	}

	// render-method for all the things happening on-screen
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		world.drawWorld(gc, sbg, g);
	}

	// update-method with allsd the magic happening in it
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		world.update(delta);
	}

	// Returning 'ID' from class 'MainMenu'
	@Override
	public int getID() {
		return stateID;
	}
	
	@Override
	public void keyPressed(int key, char c) {
		keys.set(key);
	}
	
	@Override
	public void keyReleased(int key, char c) {
		keys.clear(key);
	}
}
