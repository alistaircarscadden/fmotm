package fmotm.game.entity;

import java.util.Random;

import org.newdawn.slick.Animation;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import fmotm.game.world.Camera;
import fmotm.game.world.World;

public class MotherScorpion extends Entity {
	protected Vector2f targetPos;
	protected long timeGiveUp;
	protected Random random;
	protected Animation left;
	protected Animation right;
	protected boolean facingLeft;
	
	public MotherScorpion(World world, int typeID, Rectangle position) {
		super(world, typeID, position);
		this.targetPos = new Vector2f(0f, 0f);
		this.random = new Random();
		this.dispSize = 2f;
		this.timeGiveUp = 0;
		this.moveSpeed = 2;
		this.facingLeft = true;
	}

	
	public void update(int delta) {
		// Update animations, direction
		left.update(delta);
		right.update(delta);
		
		// Set Target Position
		if(System.currentTimeMillis() > timeGiveUp) {
			newTargetPos();
		} else {
			boolean atTarget = true;
			if(position.getX() > targetPos.x + 0.2) atTarget = false;
			if(atTarget && position.getY() > targetPos.y + 0.2) atTarget = false;
			if(atTarget && position.getX() + position.getWidth() < targetPos.x - 0.2) atTarget = false;
			if(atTarget && position.getY() + position.getHeight() < targetPos.y - 0.2) atTarget = false;

			if(atTarget) {
				newTargetPos();
			}
		}
		
		// Set Velocity
		float x = 0;
		if(targetPos.x > position.getX())
			x += 1;
		else if(targetPos.x < position.getX())
			x += -1;
		
		float y = 0;
		if(targetPos.y > position.getY())
			y += 1;
		else if(targetPos.y < position.getY())
			y += -1;
		
		if(velocity.x < 0)
			facingLeft = true;
		if(velocity.x > 0)
			facingLeft = false;
		
		
		if(x != 0 && y != 0) {
			x *= 0.707;
			y *= 0.707;
		}
		
		velocity.x = x * moveSpeed;
		velocity.y = y * moveSpeed;

		move(delta);
	}
	
	@Override
	public void render(Camera camera) {
		if(facingLeft)
			left.getCurrentFrame().draw((position.getX() - camera.getX()) * camera.getTileScale(), (position.getY() - camera.getY()) * camera.getTileScale());
		else
			right.getCurrentFrame().draw((position.getX() - camera.getX()) * camera.getTileScale(), (position.getY() - camera.getY()) * camera.getTileScale());
	}
	
	protected void newTargetPos() {
		timeGiveUp = System.currentTimeMillis() + 2000;
		
		targetPos.x = position.getX() + random.nextInt(9) - 4;
		targetPos.y = position.getY() + random.nextInt(9) - 4;
	}
	
	/* Anything that 'isWall' is something an entity cannot pass through
	 */
	protected boolean isWall(char c) {
		switch(c) {
			case 'W':
			case ' ':
				return true;
			default:
				return false;
		}
	}
	
	protected void initAnimations() {
		left = new Animation(false);
		right = new Animation(false);
		
		left.addFrame(sprites[0], 100);
		left.addFrame(sprites[1], 100);
		right.addFrame(sprites[0].getFlippedCopy(true, false), 100);
		right.addFrame(sprites[1].getFlippedCopy(true, false), 100);
	}
}
