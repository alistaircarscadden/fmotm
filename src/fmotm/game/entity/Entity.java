package fmotm.game.entity;

import org.newdawn.slick.geom.Vector2f;

public class Entity {
	public Vector2f position;
	public Vector2f velocity;
	
	public Entity(Vector2f position) {
		this.position = position;
		this.velocity = new Vector2f(0f, 0f);
	}
	
	public void update(int delta) {
		/* NOTHING */
	}
	
	public void render(Vector2f camera) {
		/* NOTHING */
	}
}
