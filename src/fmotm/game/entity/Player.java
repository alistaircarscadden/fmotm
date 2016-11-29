package fmotm.game.entity;

import org.newdawn.slick.Animation;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Vector2f;

public class Player extends Entity {
	public Animation walkDown;
	public Animation walkUp;
	public int speed;
	public boolean facingRight, facingDown;
	
	public Player(Vector2f position, float displayScale) {
		super(position);
		
		this.velocity = new Vector2f(64, 0);
		this.speed = 180;
		this.walkDown = new Animation(false);
		this.walkUp = new Animation(false);
		displayScale /= 2;
		
		try {
			SpriteSheet ss = new SpriteSheet("res/player_walk.png", 16, 16);
			final int frameLength = 100;

			walkDown.addFrame(ss.getSprite(0, 0).getScaledCopy(displayScale), frameLength);
			walkDown.addFrame(ss.getSprite(1, 0).getScaledCopy(displayScale), frameLength);
			walkDown.addFrame(ss.getSprite(2, 0).getScaledCopy(displayScale), frameLength);
			
			walkUp.addFrame(ss.getSprite(0, 1).getScaledCopy(displayScale), frameLength);
			walkUp.addFrame(ss.getSprite(1, 1).getScaledCopy(displayScale), frameLength);
			walkUp.addFrame(ss.getSprite(2, 1).getScaledCopy(displayScale), frameLength);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void update(int delta) {
		walkDown.update(delta);
		walkUp.update(delta);
		
		if(velocity.length() == 0) {
			walkUp.stop();
			walkDown.stop();
		} else if(velocity.length() != 0) {
			walkUp.start();
			walkDown.start();
		}
		
		if(velocity.getX() > 0) facingRight = true;
		else if(velocity.getX() < 0) facingRight = false;
		
		if(velocity.getY() > 0) facingDown = true;
		else if(velocity.getY() < 0) facingDown = false;
		
		position.x += velocity.x * (delta / 1000f);
		position.y += velocity.y * (delta / 1000f);
	}
	
	@Override
	public void render(Vector2f camera) {
		if(facingDown) {
			if(facingRight)
				walkDown.draw(position.x - camera.x, position.y - camera.y);
			else
				walkDown.getCurrentFrame().getFlippedCopy(true, false).draw(position.x - camera.x, position.y - camera.y);
		} else {
			if(facingRight)
				walkUp.draw(position.x - camera.x, position.y - camera.y);
			else
				walkUp.getCurrentFrame().getFlippedCopy(true, false).draw(position.x - camera.x, position.y - camera.y);
		}
	}

}
