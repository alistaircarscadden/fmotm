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
	public long tileStyles[][];
	public TileFeel tileFeel;
	public Player player;
	// Camera refers to position of display within world coordinates
	public Vector2f camera;
	
	// World Properties
	public final float tileScale = 64f;
	
	public World() {
		player = new Player(new Vector2f(0f, 0f), tileScale);
		camera = new Vector2f();
	}
	
	public void setTileFeel(String ref) {
		tileFeel = new TileFeel(ref, tileScale);
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
		
		tiles[0][0] = ' ';
	}
	
	@SuppressWarnings("unused")
	public void initTileStyles() {
		Random random = new Random(System.currentTimeMillis());
		float r;
		
		final long floorVA = 1L;
		final long floorVB = 2L;
		final long floorVC = 4L;
		final long floorVD = 8L;
		final long floorCTR = 16L;
		final long floorCBR = 32L;
		final long floorCBL = 64L;
		final long floorCTL = 128L;
		final long floorETVA = 256L;
		final long floorETVB = 512L;
		final long floorERVA = 1024L;
		final long floorERVB = 2048L;
		final long floorEBVA = 4096L;
		final long floorEBVB = 8192L;
		final long floorELVA = 16384L;
		final long floorELVB = 32768L;
		final long wallVA = 65536L;
		final long wallVB = 131072L;
		final long wallVC = 262144L;
		final long wallVD = 524288L;
		final long wallCTR = 1048576L;
		final long wallCBR = 2097152L;
		final long wallCBL = 4194304L;
		final long wallCTL = 8388608L;
		final long wallETVA = 16777216L;
		final long wallETVB = 33554432L;
		final long wallERVA = 67108864L;
		final long wallERVB = 134217728L;
		final long wallEBVA = 268435456L;
		final long wallEBVB = 536870912L;
		final long wallELVA = 1073741824L;
		final long wallELVB = 2147483648L;
		final long wallBottomER = 4294967296L;
		final long wallbottomEL = 8589934592L;
		final long wallBottomFixR = 17179869184L;
		final long wallBottomFixL = 34359738368L;

		final long dropVA = 1099511627776L;
		final long dropVB = 2199023255552L;
		final long dropVC = 4398046511104L;
		final long dropVD = 8796093022208L;

		// Init tile styles by comparing each tile to it's adjacent tiles
		for(int y = 0; y < tiles[0].length; y++) {
			for(int x = 0; x < tiles.length; x++) {
				tileStyles[x][y] = 0;
				
				if(getTile(x, y) == 'F') {
					boolean topedg = false;
					boolean rigedg = false;
					boolean botedg = false;
					boolean lefedg = false;
					
					r = random.nextFloat();
					if (r > 0.75) tileStyles[x][y] += floorVA;
					else if (r > 0.5) tileStyles[x][y] += floorVB;
					else if (r > 0.25) tileStyles[x][y] += floorVC;
					else tileStyles[x][y] += floorVD;
					
					// top edge?
					if(getTile(x, y - 1) == ' ') {
						topedg = true;
						r = random.nextFloat();
						if (r > 0.5) tileStyles[x][y] += floorETVA;
						else tileStyles[x][y] += floorETVB;
					}
					
					// right edge?
					if(getTile(x + 1, y) == ' ') {
						rigedg = true;
						r = random.nextFloat();
						if (r > 0.5) tileStyles[x][y] += floorERVA;
						else tileStyles[x][y] += floorERVB;
					}
					
					// bottom edge?
					if(getTile(x, y + 1) == ' ') {
						botedg = true;
						r = random.nextFloat();
						if (r > 0.5) tileStyles[x][y] += floorEBVA;
						else tileStyles[x][y] += floorEBVB;
					}
					
					// left edge?
					if(getTile(x - 1, y) == ' ') {
						lefedg = true;
						r = random.nextFloat();
						if (r > 0.5) tileStyles[x][y] += floorELVA;
						else tileStyles[x][y] += floorELVB;
					}
					
					// top right corner
					if(getTile(x + 1, y - 1) == ' ' && !(topedg || rigedg)) {
						tileStyles[x][y] += floorCTR;
					}
					
					// bottom right corner
					if(getTile(x + 1, y + 1) == ' ' && !(rigedg || botedg)) {
						tileStyles[x][y] += floorCBR;
					}
					
					// bottom left corner
					if(getTile(x - 1, y + 1) == ' ' && !(botedg || lefedg)) {
						tileStyles[x][y] += floorCBL;
					}
					
					// top left corner
					if(getTile(x - 1, y - 1) == ' ' && !(lefedg || topedg)) {
						tileStyles[x][y] += floorCTL;
					}
				}
				if(getTile(x, y) == 'W') {
					boolean topedg = false;
					boolean rigedg = false;
					boolean botedg = false;
					boolean lefedg = false;
					
					r = random.nextFloat();
					if (r > 0.75) tileStyles[x][y] += wallVA;
					else if (r > 0.5) tileStyles[x][y] += wallVB;
					else if (r > 0.25) tileStyles[x][y] += wallVC;
					else tileStyles[x][y] += wallVD;
					
					// top edge?
					if(getTile(x, y - 1) != 'W') {
						topedg = true;
						r = random.nextFloat();
						if (r > 0.5) tileStyles[x][y] += wallETVA;
						else tileStyles[x][y] += wallETVB;
					}
					
					// right edge?
					if(getTile(x + 1, y) != 'W') {
						rigedg = true;
						r = random.nextFloat();
						if (r > 0.5) tileStyles[x][y] += wallERVA;
						else tileStyles[x][y] += wallERVB;
					}
					
					// bottom edge?
					if(getTile(x, y + 1) != 'W') {
						botedg = true;
						r = random.nextFloat();
						if (r > 0.5) tileStyles[x][y] += wallEBVA;
						else tileStyles[x][y] += wallEBVB;
					}
					
					// left edge?
					if(getTile(x - 1, y) != 'W') {
						lefedg = true;
						r = random.nextFloat();
						if (r > 0.5) tileStyles[x][y] += wallELVA;
						else tileStyles[x][y] += wallELVB;
					}
					
					// top right corner
					if(getTile(x + 1, y - 1) != 'W' && !(topedg || rigedg)) {
						tileStyles[x][y] += wallCTR;
					}
					
					// bottom right corner
					if(getTile(x + 1, y + 1) != 'W' && !(rigedg || botedg)) {
						tileStyles[x][y] += wallCBR;
					}
					
					// bottom left corner
					if(getTile(x - 1, y + 1) != 'W' && !(botedg || lefedg)) {
						tileStyles[x][y] += wallCBL;
					}
					
					// top left corner
					if(getTile(x - 1, y - 1) != 'W' && !(lefedg || topedg)) {
						tileStyles[x][y] += wallCTL;
					}
				}
				if(getTile(x, y) == ' ' && getTile(x, y - 1) != ' ') {
					r = random.nextFloat();
					if (r > 0.75) tileStyles[x][y] += dropVA;
					else if (r > 0.5) tileStyles[x][y] += dropVB;
					else if (r > 0.25) tileStyles[x][y] += dropVC;
					else tileStyles[x][y] += dropVD;
				}
				
				tileFeel.initTile(tileStyles[x][y]);
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