package fmotm.game.entity;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import static fmotm.util.RectUtils.intersection2D;

import fmotm.game.world.World;

public class Entity {
	public Rectangle position;
	public Vector2f velocity;
	private World world;
	
	public Entity(World world, Rectangle position) {
		this.world = world;
		this.position = position;
		this.velocity = new Vector2f(0f, 0f);
	}
	
	public void update(int delta) {
		move(delta);
	}
	
	public void render(Vector2f camera) {
		/* NOTHING */
	}
	
	protected void move(int delta) {
		position.setX(position.getX() + velocity.getX() * (delta / 1000f));
		position.setY(position.getY() + velocity.getY() * (delta / 1000f));

		/* Iterate through tiles to check for intersection
		 * (Local tiles to the player)
		 */
		int centerX = (int) (position.getX() + position.getWidth() / 2);
		int centerY = (int) (position.getY() + position.getHeight() / 2);

		for(int x = centerX - 1; x < centerX + 2; x++) {
			for(int y = centerY - 1; y < centerY + 2; y++) {
				// If tile is walkable we don't need to check collision
				if(world.tileIsUnpassable(x, y)) {

					// Generate the intersection box to resolve between this' position and the unpassable tile
					Rectangle intersection = intersection2D(position.getX(), position.getY(), position.getWidth(), position.getHeight(), x, y, 1, 1);

					// If there is no intersection we can't resolve it
					if(intersection != null) {
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
					}
				}
			}
		}
	}
}
