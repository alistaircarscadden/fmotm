package fmotm.game.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;

import fmotm.game.world.Camera;

public class EntityManager {
	// List of all entities in world
	private List<Entity> entities;
	// QuadTree of all entities regenerated per update
	private QuadTree quadtree;
	
	private Map<Integer, Image[]> imageCache;
	
	private float tileScale;
	
	public EntityManager(Rectangle bounds, float tileScale) {
		entities = new ArrayList<Entity>();
		quadtree = new QuadTree(0, bounds);
		imageCache = new HashMap<Integer, Image[]>();
		this.tileScale = tileScale;
	}

	public boolean removeEntity(Entity e) {
		return entities.remove(e);
	}
	
	public boolean addEntity(Entity e) {
		initEntityImages(e);
		e.setSprites(imageCache.get(e.typeID));
		return entities.add(e);
	}
	
	public void update(int delta) {
		quadtree.clear();
		for(Entity e : entities) {
			e.update(delta);
			//quadtree.insert(e);
		}
		/*
		List<Entity> localEntities = new ArrayList<Entity>();
		for(Entity e : entities) {
			localEntities.clear();
			for(Entity comp : quadtree.getLocal(localEntities, e)) {
				//testCollide(e, comp);
			}
		}*/
	}
	
	public void drawEntities(Camera camera) {
		for(Entity e : entities) {
			if(e.position.getX() > camera.getMaxX()) continue;
			if(e.position.getY() > camera.getMaxY()) continue;
			if(e.position.getX() + e.position.getWidth() < camera.getX()) continue;
			if(e.position.getY() + e.position.getHeight() < camera.getY()) continue;
			
			e.render(camera);
		}
	}
	
	private void initEntityImages(Entity e) {
		if(imageCache.get(e.typeID) != null) return;
		
		SpriteSheet ss = null;

		try {
			switch(e.typeID) {
				case 1:
					ss = new SpriteSheet("res/player_walk.png", 16, 16);
					break;
				case 2:
				case 3:
					ss = new SpriteSheet("res/scorpion_walk.png", 16, 16);
					break;
			}
		} catch(SlickException x) {
			x.printStackTrace();
		}

		if(ss != null) {
			int numSprites = ss.getHorizontalCount() * ss.getVerticalCount();
			Image[] sprites = new Image[numSprites];
			for(int i = 0; i < numSprites; i++) {
				Image sprite = ss.getSprite(i % ss.getHorizontalCount(), i / ss.getHorizontalCount());
				sprites[i] = sprite.getScaledCopy((tileScale * e.dispSize) / sprite.getWidth());
			}
			
			imageCache.put(e.typeID, sprites);
		}
	}
}
