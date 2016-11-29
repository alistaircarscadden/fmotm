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
	public int tileStyles[][];
	public TileFeel tileFeel;
	public Player player;
	public Vector2f camera;
	
	// World Properties
	public final float displayScale = 8f;
	
	public World() {
		player = new Player(new Vector2f(3f, 3f), displayScale);
		camera = new Vector2f();
	}
	
	public void setTileFeel(String ref) {
		tileFeel = new TileFeel(ref, displayScale);
	}
	
	public void drawWorld(GameContainer gc, StateBasedGame sbg, Graphics g) {
		camera.x = player.position.x - gc.getWidth() / 2;
		camera.y = player.position.y - gc.getHeight() / 2;
		
		drawTiles(gc);
		
		player.render(camera);
		
	}
	
	public void drawTile(GameContainer gc, Image img, int x, int y) {
		Vector2f screenPos = new Vector2f(player.position.x - gc.getWidth() / 2, player.position.y - gc.getHeight() / 2);

		img.draw(img.getWidth() * x - screenPos.x, img.getHeight() * y - screenPos.y);
	}
	
	public void drawTiles(GameContainer gc) {
		// Initial loop draws base for all tiles
		for(int x = 0; x < tileStyles.length; x++) {
			for(int y = 0; y < tileStyles[0].length; y++) {
				if(tiles[x][y] == 'F') {
					drawTile(gc, tileFeel.getFloor(tileStyles[x][y]), x, y);
				} else if(tiles[x][y] == 'W') {
					drawTile(gc, tileFeel.getWall(tileStyles[x][y]), x, y);
				}
			}
		}
		
		// Secondary loop draws wall bottoms and drops for bottom tiles
		for(int x = 0; x < tileStyles.length; x++) {
			for(int y = 0; y < tileStyles[0].length; y++) {
				// Draw drops
				if(tiles[x][y] != ' ' && getTile(x, y + 1) == ' ') {
					drawTile(gc, tileFeel.getDrop(), x, y + 1);
				}
				
				// Draw wall bottoms
				if(getTile(x, y) == 'W' && getTile(x, y + 1) != 'W') {
					// Base for wall bottom with no edges
					drawTile(gc, tileFeel.getWallBottom(0), x, y);
					
					// Add right border
					if(getTile(x + 1, y) != 'W') {
						drawTile(gc, tileFeel.getWallBottom(1), x, y);
					}
					
					// Add left border
					if(getTile(x - 1, y) != 'W') {
						drawTile(gc, tileFeel.getWallBottom(2), x, y);
					}
					
					// Fix tile to left
					if(getTile(x - 1, y) == 'W' && getTile(x - 1, y + 1) == 'W') {
						drawTile(gc, tileFeel.getWallBottom(3), x - 1, y);
					}
					
					// Fix tile to right
					if(getTile(x + 1, y) == 'W' && getTile(x + 1, y + 1) == 'W') {
						drawTile(gc, tileFeel.getWallBottom(4), x + 1, y);
					}
				}
			}
		}
	}
	
	public void generateRandom(int X, int Y) {
		tiles = new char[X][Y];
		tileStyles = new int[X][Y];
		
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
		
		tiles[0][0] = ' ';
		
		initTileStyles();
	}
	
	public void initTileStyles() {
		// Init tile styles by comparing each tile to it's adjacent tiles
		for(int y = 0; y < tiles[0].length; y++) {
			for(int x = 0; x < tiles.length; x++) {
				tileStyles[x][y] = 0;
				if(tiles[x][y] == 'F') {
					// Add relevant amount to tileStyles if there is a missing floor on an edge
					if(getTile(x    , y - 1) == ' ') tileStyles[x][y] += 1;
					if(getTile(x + 1, y    ) == ' ') tileStyles[x][y] += 2;
					if(getTile(x    , y + 1) == ' ') tileStyles[x][y] += 4;
					if(getTile(x - 1, y    ) == ' ') tileStyles[x][y] += 8;
					// Add relevant amount to tileStyles if there is a missing floor on a corner, does not add if there are two walls adjacent (just looks stupid)
					if(getTile(x + 1, y - 1) == ' ' && !(getTile(x + 1, y) == 'W' && getTile(x, y - 1) == 'W')) tileStyles[x][y] += 16;
					if(getTile(x + 1, y + 1) == ' ' && !(getTile(x + 1, y) == 'W' && getTile(x, y + 1) == 'W')) tileStyles[x][y] += 32;
					if(getTile(x - 1, y + 1) == ' ' && !(getTile(x - 1, y) == 'W' && getTile(x, y + 1) == 'W')) tileStyles[x][y] += 64;
					if(getTile(x - 1, y - 1) == ' ' && !(getTile(x - 1, y) == 'W' && getTile(x, y - 1) == 'W')) tileStyles[x][y] += 128;
				} else if(tiles[x][y] == 'W') {
					if(getTile(x    , y - 1) != 'W') tileStyles[x][y] += 1;
					if(getTile(x + 1, y    ) != 'W') tileStyles[x][y] += 2;
					if(getTile(x    , y + 1) != 'W') tileStyles[x][y] += 4;
					if(getTile(x - 1, y    ) != 'W') tileStyles[x][y] += 8;
					if(getTile(x + 1, y - 1) != 'W') tileStyles[x][y] += 16;
					if(getTile(x + 1, y + 1) != 'W') tileStyles[x][y] += 32;
					if(getTile(x - 1, y + 1) != 'W') tileStyles[x][y] += 64;
					if(getTile(x - 1, y - 1) != 'W') tileStyles[x][y] += 128;
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
