package fmotm.game.entity;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

public class Player extends Entity {
	public Animation walkDown;
	public Animation walkUp;
	public float speed;
	public boolean facingRight, facingDown;
	private float tileScale;
	
	public Player(Rectangle position, float tileScale) {
		super(position);
		
		this.velocity = new Vector2f(64, 0);
		this.speed = 3;
		this.walkDown = new Animation(false);
		this.walkUp = new Animation(false);
		this.tileScale = tileScale;
		this.facingRight = true;
		this.facingDown = true;
		
		try {
			SpriteSheet ss = new SpriteSheet("res/player_walk.png", 16, 16);
			final int frameLength = 100;

			float playerScale = tileScale / (ss.getWidth() / ss.getHorizontalCount());
			
			walkDown.addFrame(ss.getSprite(0, 0).getScaledCopy(playerScale), frameLength);
			walkDown.addFrame(ss.getSprite(1, 0).getScaledCopy(playerScale), frameLength);
			walkDown.addFrame(ss.getSprite(2, 0).getScaledCopy(playerScale), frameLength);
			
			walkUp.addFrame(ss.getSprite(0, 1).getScaledCopy(playerScale), frameLength);
			walkUp.addFrame(ss.getSprite(1, 1).getScaledCopy(playerScale), frameLength);
			walkUp.addFrame(ss.getSprite(2, 1).getScaledCopy(playerScale), frameLength);
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
		
		position.setX(position.getX() + velocity.x * (delta / 1000f));
		position.setY(position.getY() + velocity.y * (delta / 1000f));
	}
	
	@Override
	public void render(Vector2f camera) {
		Image sprite;
		
		if(facingDown) {
			if(facingRight)
				sprite = walkDown.getCurrentFrame();
			else
				sprite = walkDown.getCurrentFrame().getFlippedCopy(true, false);
		} else {
			if(facingRight)
				sprite = walkUp.getCurrentFrame();
			else
				sprite = walkUp.getCurrentFrame().getFlippedCopy(true, false);
		}
		
		sprite.draw((position.getX() - camera.x) * tileScale - sprite.getWidth()/2, (position.getY() - camera.y) * tileScale - sprite.getHeight());
	}

}
