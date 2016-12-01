package fmotm.game.world;

import java.util.Random;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import fmotm.game.entity.EntityManager;
import fmotm.game.entity.Player;

public class World {
	public char tiles[][];
	private long tileStyles[][];
	private TileFeel tileFeel;
	public Player player;
	private Vector2f camera; // Camera refers to position of display within world coordinates
	private EntityManager entityManager;
	
	// World Properties
	public final float tileScale = 64f;
	
	public World() {
		entityManager = new EntityManager(new Rectangle(-2f, -2f, 20f, 20f));
		player = new Player(new Rectangle(0f, 0f, 0.5f, 0.5f), tileScale);
		entityManager.addEntity(player);
		camera = new Vector2f();
	}
	
	public void update(int delta) {
		entityManager.update(delta);
	}
	
	public void setTileFeel(String ref) {
		tileFeel = new TileFeel(ref, tileScale);
		tileStyles = tileFeel.generateTileStyles(tiles);
		System.out.printf("TileFeel (%s) inits: %d\n", ref, tileFeel.numInits);
	}
	
	public void drawWorld(GameContainer gc, StateBasedGame sbg, Graphics g) {
		camera.x = player.position.getX() - (gc.getWidth() / 2) / tileScale;
		camera.y = player.position.getY() - (gc.getHeight() / 2) / tileScale;
		
		drawTiles((int) camera.x, (int) camera.y, (int) (gc.getWidth() / tileScale), (int) (gc.getHeight() / tileScale));
		
		player.render(camera);
	}
	
	public void drawTile(Vector2f camera, Image img, int x, int y) {
		img.draw((x - camera.x) * tileScale, (y - camera.y) * tileScale);
	}
	
	public void drawTiles(int x, int y, int width, int height) {
		// Initial loop draws base for all tiles
		for(int x_c = x; x_c <= x + width; x_c++) {
			for(int y_c = y; y_c <= y + height; y_c++) {
				drawTile(camera, tileFeel.getTile(getTileStyle(x_c, y_c)), x_c, y_c);
			}
		}
	}
	
	public void generateRandom(int X, int Y) {
		tiles = new char[X][Y];
		tileStyles = new long[X][Y];
		
		Random random = new Random(System.currentTimeMillis());
		
		// Init random tiles (probability 0.2 empty, 0.8 floor)
		for(int y = 0; y < Y; y++) {
			for(int x = 0; x < X; x++) {
				float f = random.nextFloat();
				
				if(f > 0.9) {
					tiles[x][y] = ' ';
				} else if(f > 0.75) {
					tiles[x][y] = 'W';
				} else {
					tiles[x][y] = 'F';
				}
				
				if(y == Y - 1) tiles[x][y] = ' ';
			}
		}
	}
	
	public char getTile(int x, int y) {
		if(tiles.length > 0 && tiles[0].length > 0 && x >= 0 && x < tiles.length && y >= 0 && y < tiles[0].length) {
			return tiles[x][y];
		}
		
		return ' ';
	}
	
	public long getTileStyle(int x, int y) {
		if(tileStyles.length > 0 && tileStyles[0].length > 0 && x >= 0 && x < tileStyles.length && y >= 0 && y < tileStyles[0].length) {
			return tileStyles[x][y];
		}
		
		return 0;
	}
}