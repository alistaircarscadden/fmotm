package fmotm.game.entity;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.geom.Rectangle;

public class QuadTree {
	private final int CAPACITY = 10;
	private final int MAX_DEPTH = 10;
	private int depth;
	private List<Entity> entities;
	private Rectangle bounds;
	private QuadTree[] children;
	
	public QuadTree(int depth, Rectangle bounds) {
		this.depth = depth;
		this.bounds = bounds;
		entities = new ArrayList<Entity>();
		children = new QuadTree[4];
	}
	
	public void clear() {
		entities.clear();
		
		for(QuadTree ch : children) {
			if(ch != null) ch.clear();
			ch = null;
		}
	}

	public void split() {
		float halfWidth = bounds.getWidth() / 2;
		float halfHeight = bounds.getHeight() / 2;
		children[0] = new QuadTree(depth + 1, new Rectangle(bounds.getX(), bounds.getY(), halfWidth, halfHeight));
		children[1] = new QuadTree(depth + 1, new Rectangle(bounds.getX() + halfWidth, bounds.getY(), halfWidth, halfHeight));
		children[2] = new QuadTree(depth + 1, new Rectangle(bounds.getX(), bounds.getY() + halfHeight, halfWidth, halfHeight));
		children[3] = new QuadTree(depth + 1, new Rectangle(bounds.getX() + halfWidth, bounds.getY() + halfHeight, halfWidth, halfHeight));
	}
	
	public int getIndex(Entity e) {
		float verticalMiddle = bounds.getX() + (bounds.getWidth() / 2);
		float horizonalMiddle = bounds.getY() + (bounds.getHeight() / 2);
		
		boolean fitInTop = 
		boolean fitInBot;
		boolean fitInLef;
		boolean fitInRig;
	}
	
	public void insert(Entity e) {
		int index = getIndex(e);
		if(index != -1) {
			children[index].insert(e);
			return;
		}
		
		entities.add(e);
		if(entities.size() > CAPACITY & depth < MAX_DEPTH) {
			split();
			
			int i = 0;
		     while (i < entities.size()) {
		       int iIndex = getIndex(entities.get(i));
		       if (iIndex != -1) {
		         children[iIndex].insert(entities.remove(i));
		       }
		       else {
		         i++;
		       }
		     }
		}
	}
	
	public List<Entity> getLocal(List<Entity> localEntities, Entity e) {
		   int index = getIndex(e);
		   if (index != -1 && children[0] != null) {
		     children[index].getLocal(localEntities, e);
		   }
		 
		   localEntities.addAll(entities);
		 
		   return localEntities;
		 }
}
