package fmotm.game.entity;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.geom.Rectangle;

public class EntityManager {
	// List of all entities in world
	private List<Entity> entities;
	// QuadTree of all entities regenerated per update
	private QuadTree quadtree;
	
	public EntityManager(Rectangle bounds) {
		entities = new ArrayList<Entity>();
		quadtree = new QuadTree(0, bounds);
	}

	public boolean removeEntity(Entity e) {
		return entities.remove(e);
	}
	
	public boolean addEntity(Entity e) {
		return entities.add(e);
	}
	
	public void update(int delta) {
		quadtree.clear();
		for(Entity e : entities) {
			e.update(delta);
			quadtree.insert(e);
		}
		
		List<Entity> localEntities = new ArrayList<Entity>();
		for(Entity e : entities) {
			localEntities.clear();
			for(Entity comp : quadtree.getLocal(localEntities, e)) {
				testCollide(e, comp);
			}
		}
	}
	
	public void testCollide(Entity a, Entity b) {
		
	}
}
