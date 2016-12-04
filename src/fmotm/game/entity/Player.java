package fmotm.game.entity;

import java.util.BitSet;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import fmotm.game.world.World;

public class Player extends Entity {
	public Animation walkDown;
	public Animation walkUp;
	public float speed;
	public boolean facingRight, facingDown;
	private float tileScale;
	private BitSet input;
	private long lastTeleport;
	
	public Player(World world, Rectangle position, float tileScale) {
		super(world, position);
		
		this.velocity = new Vector2f(64, 0);
		this.speed = 5;
		this.walkDown = new Animation(false);
		this.walkUp = new Animation(false);
		this.tileScale = tileScale;
		this.facingRight = true;
		this.facingDown = true;
		this.lastTeleport = 0;
		
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
		float x = 0;
		float y = 0;

		if(input.get(Input.KEY_W)) y -= 1;
		if(input.get(Input.KEY_A)) x -= 1;
		if(input.get(Input.KEY_S)) y += 1;
		if(input.get(Input.KEY_D)) x += 1;

		if(x != 0 && y != 0) {
			x *= 0.707;
			y *= 0.707;
		}
		
		if(input.get(Input.KEY_LSHIFT)) {
			x *= 0.3;
			y *= 0.3;
		}

		if(input.get(Input.KEY_SPACE)) {
			teleport();
		}
		
		velocity.x = x * speed;
		velocity.y = y * speed;
		
		move(delta);
		
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
		
		sprite.draw((position.getX() + (position.getWidth() / 2) - 0.5f - camera.getX()) * tileScale, (position.getY() - (1 - position.getHeight()) - camera.getY()) * tileScale);
	}
	
	private void teleport() {
		if(System.currentTimeMillis() < lastTeleport + 250) return;
		lastTeleport = System.currentTimeMillis();
		
		int x = 0;
		int y = 0;

		if(input.get(Input.KEY_W)) y -= 1;
		if(input.get(Input.KEY_A)) x -= 1;
		if(input.get(Input.KEY_S)) y += 1;
		if(input.get(Input.KEY_D)) x += 1;
		
		position.setX(position.getX() + x * 5);
		position.setY(position.getY() + y * 5);
	}
	
	public void setInput(BitSet input) {
		this.input = input;
	}

}
