package fmotm.game.entity;

import java.util.BitSet;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;

import fmotm.game.world.Camera;
import fmotm.game.world.World;

public class Player extends Entity {
	public Animation walkDown;
	public Animation walkUp;
	public boolean facingRight, facingDown;
	private BitSet input;
	private long lastTeleport;
	
	public Player(World world, int typeID, Rectangle position) {
		super(world, typeID, position);
		this.dispSize = 1f;
		this.moveSpeed = 5;
		this.facingRight = true;
		this.facingDown = true;
		this.lastTeleport = 0;
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
		
		velocity.x = x * moveSpeed;
		velocity.y = y * moveSpeed;
		
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
	public void render(Camera camera) {
		if(walkUp == null || walkDown == null) return;
		
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
		
		sprite.draw((position.getX() + (position.getWidth() / 2) - 0.5f - camera.getX()) * camera.getTileScale(), (position.getY() - (1 - position.getHeight()) - camera.getY()) * camera.getTileScale());
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

	@Override
	protected void initAnimations() {
		this.walkDown = new Animation(false);
		this.walkUp = new Animation(false);
		
		int frameLength = 100;
		
		walkDown.addFrame(sprites[0], frameLength);
		walkDown.addFrame(sprites[1], frameLength);
		walkDown.addFrame(sprites[2], frameLength);
		
		walkUp.addFrame(sprites[3], frameLength);
		walkUp.addFrame(sprites[4], frameLength);
		walkUp.addFrame(sprites[5], frameLength);
	}
}
