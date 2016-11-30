package fmotm.game.world;

import java.util.Random;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import fmotm.game.entity.Player;

public class World {
	public char tiles[][];
	private long tileStyles[][];
	private TileFeel tileFeel;
	public Player player;
	private Vector2f camera; // Camera refers to position of display within world coordinates
	
	// World Properties
	public final float tileScale = 64f;
	
	public World() {
		player = new Player(new Vector2f(0f, 0f), tileScale);
		camera = new Vector2f();
	}
	
	public void setTileFeel(String ref) {
		tileFeel = new TileFeel(ref, tileScale);
		tileStyles = tileFeel.generateTileStyles(tiles);
	}
	
	public void drawWorld(GameContainer gc, StateBasedGame sbg, Graphics g) {
		camera.x = player.position.x - (gc.getWidth() / 2) / tileScale;
		camera.y = player.position.y - (gc.getHeight() / 2) / tileScale;
		
		drawTiles();
		
		player.render(camera);
	}
	
	public void drawTile(Vector2f camera, Image img, int x, int y) {
		img.draw((x - camera.x) * tileScale, (y - camera.y) * tileScale);
	}
	
	public void drawTiles() {
		// Initial loop draws base for all tiles
		for(int x = 0; x < tileStyles.length; x++) {
			for(int y = 0; y < tileStyles[0].length; y++) {
				drawTile(camera, tileFeel.getTile(tileStyles[x][y]), x, y);
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
				
				if(f > 0.8) {
					tiles[x][y] = ' ';
				} else if(f > 0.5) {
					tiles[x][y] = 'W';
				} else {
					tiles[x][y] = 'F';
				}
			}
		}
	}
	
	public char getTile(int x, int y) {
		if(tiles.length > 0 && tiles[0].length > 0 && x >= 0 && x < tiles.length && y >= 0 && y < tiles[0].length) {
			return tiles[x][y];
		}
		
		return ' ';
	}
}