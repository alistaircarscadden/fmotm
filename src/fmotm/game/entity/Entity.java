package fmotm.game.entity;

import static fmotm.util.RectUtils.intersection2D;

import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import fmotm.game.world.Camera;
import fmotm.game.world.World;

public class Entity {
	protected Image[] sprites; // Array of sprites to make animations from, or to display
	public Rectangle position;
	public Vector2f velocity;
	protected World world;
	protected float moveSpeed; // Move speed (in tiles per second)
	protected float dispSize; // Scale (in tiles) of the sprite to display
	protected boolean onFloor;
	protected int typeID; // Unique ID of this type of entity
	
	public Entity(World world, int typeID, Rectangle position) {
		this.world = world;
		this.typeID = typeID;
		this.dispSize = 1;
		this.position = position;
		this.velocity = new Vector2f(0f, 0f);
		this.onFloor = true;
	}
	
	public void update(int delta) {
		move(delta);
	}
	
	public void render(Camera camera) {
		/* NOTHING */
	}
	
	/* Anything that 'isFloor' is something an entity can pass over
	 */
	protected boolean isFloor(char c) {
		switch(c) {
			case 'F':
				return true;
			default:
				return false;
		}
	}
	
	/* Anything that 'isWall' is something an entity cannot pass through
	 */
	protected boolean isWall(char c) {
		switch(c) {
			case 'W':
				return true;
			default:
				return false;
		}
	}
	
	protected void move(int delta) {
		onFloor = false;
		
		position.setX(position.getX() + velocity.getX() * (delta / 1000f));
		position.setY(position.getY() + velocity.getY() * (delta / 1000f));

		/* Iterate through tiles to check for intersection
		 * (Local tiles to the player)
		 */
		int centerX = (int) (position.getX() + position.getWidth() / 2);
		int centerY = (int) (position.getY() + position.getHeight() / 2);

		for(int x = centerX - 1; x < centerX + 2; x++) {
			for(int y = centerY - 1; y < centerY + 2; y++) {
				// Generate the intersection box to resolve between this' position and the unpassable tile
				Rectangle intersection = intersection2D(position.getX(), position.getY(), position.getWidth(), position.getHeight(), x, y, 1, 1);

				// If there is no intersection we can't resolve it
				if(intersection != null) {
					// Resolve intersection if inside wall
					if(isWall(world.getTile(x, y))) {
						// Resolve on x-axis
						if(intersection.getWidth() < intersection.getHeight()) {
							// Moving right, so resolve back to the left
							if(velocity.getX() > 0) {
								position.setX(position.getX() - intersection.getWidth());
							} else /* Moving left, resolve right */ {
								position.setX(position.getX() + intersection.getWidth());
							}
						} else /* Resolve on y-axis */ {
							// Moving down, so resolve upwards
							if(velocity.getY() > 0) {
								position.setY(position.getY() - intersection.getHeight());
							} else /* Moving up, so resolve down */ {
								position.setY(position.getY() + intersection.getHeight());
							}
						}
					} else if(isFloor(world.getTile(x, y))) {
						onFloor = true;
					}
				}
			}
		}
		
		if(!onFloor) {
			position.setX(0);
			position.setY(0);
		}
	}
	
	public void setSprites(Image[] sprites) {
		this.sprites = sprites;
		initAnimations();
	}
	
	protected void initAnimations() {
		/* NOTHING */
	}
}
