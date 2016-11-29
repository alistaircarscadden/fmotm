package fmotm.states;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import fmotm.Application;

public class MainMenu extends BasicGameState {
	
	private int stateID;
	private Image title;
	private List<MenuButton> buttons = new ArrayList<MenuButton>();
	
	public MainMenu(int ID) {
		super();
		this.stateID = ID;
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		try {
			title = new Image("res/title.png");
			title.setFilter(Image.FILTER_NEAREST);
			title = title.getScaledCopy(6f);
			
			gc.setIcons(new String[]{"res/icon16.png", "res/icon32.png"});
			
			Image playImageUp = new Image("res/play.png");
			playImageUp.setFilter(Image.FILTER_NEAREST);
			playImageUp = playImageUp.getScaledCopy(6f);
			Image playImageDown = new Image("res/play_pressed.png");
			playImageDown.setFilter(Image.FILTER_NEAREST);
			playImageDown = playImageDown.getScaledCopy(6f);
			MenuButton playButton = new MenuButton(sbg, playImageUp, playImageDown, gc.getWidth() / 2 - playImageUp.getWidth() / 2, gc.getHeight() / 2 - playImageUp.getHeight() / 2, playImageUp.getWidth(), playImageUp.getHeight()) {
				@Override
				public void used() {
		    		sbg.enterState(Application.GAME);
		    	}
			};
			
			Image exitImageUp = new Image("res/exit.png");
			exitImageUp.setFilter(Image.FILTER_NEAREST);
			exitImageUp = exitImageUp.getScaledCopy(6f);
			Image exitImageDown = new Image("res/exit_pressed.png");
			exitImageDown.setFilter(Image.FILTER_NEAREST);
			exitImageDown = exitImageDown.getScaledCopy(6f);
			MenuButton exitButton = new MenuButton(sbg, exitImageUp, exitImageDown, gc.getWidth() / 2 - exitImageUp.getWidth() / 2, 20 + playImageUp.getHeight() + gc.getHeight() / 2 - exitImageUp.getHeight() / 2, exitImageUp.getWidth(), exitImageUp.getHeight()) {
				@Override
				public void used() {
		    		System.exit(0);
		    	}
			};
			
			
			buttons.add(playButton);
			buttons.add(exitButton);
		} catch (SlickException e) {
			System.err.println("MainMenu failed to load assets.");
			e.printStackTrace();
		}
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		g.drawString(String.format("version %.1f", 1.0), 5, gc.getHeight() - 20);
		
		for(MenuButton b : buttons) {
			if(!b.pressed)
				b.up.draw(b.x, b.y);
			else
				b.down.draw(b.x, b.y);
		}
		
		title.draw(gc.getWidth() / 2 - title.getWidth() / 2, 20 + title.getHeight() / 2);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int arg2) throws SlickException {
	}

	@Override
	public int getID() {
		return stateID;
	}
	
	@Override
    public void mousePressed(int button, int x, int y) {
    	for(MenuButton b : buttons) {
    		if(x > b.x && y > b.y && x <= b.x + b.width && y <= b.y + b.height) {
    			b.pressed = true;
    		}
    	}
    }
	
    @Override
    public void mouseReleased(int button, int x, int y) {
    	for(MenuButton b : buttons) {
    		b.pressed = false;
    		
    		if(x > b.x && y > b.y && x <= b.x + b.width && y <= b.y + b.height) {
    			b.used();
    		}
    	}
    }
    
    private class MenuButton {
    	public Image up, down;
    	public int x, y, width, height;
    	public boolean pressed;
    	public StateBasedGame sbg;
    	
    	public MenuButton(StateBasedGame sbg, Image up, Image down, int x, int y, int width, int height) {
			super();
			this.sbg = sbg;
			this.up = up;
			this.down = down;
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
			this.pressed = false;
		}
    	
    	public void used() {
    		// To be overridden
    	}
    }
}