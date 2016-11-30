package fmotm.states;

import java.util.Random;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import fmotm.game.world.TileFeel;

public class TileFeelTest extends BasicGameState {
	
	private int stateID;
	public char tiles[][];
	public long tileStyles[][];
	
	TileFeel tf;

	public TileFeelTest(int ID) {
		super();
		this.stateID = ID;
		tf = new TileFeel("res/lf_nexus.png", 32f);
		generateRandom(30, 30);
		initTileStyles();
	}

	// init-method for initializing all resources
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
	}

	// render-method for all the things happening on-screen
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		int numDrawn = 0;
		for(long id : tf.tiles.keySet()) {
			Image img = tf.tiles.get(id);
			img.draw(numDrawn % 10 * 80, numDrawn++ / 10 * 64);
			g.drawString("" + id, numDrawn % 10 * 80 - 40, numDrawn++ / 10 * 64);
		}
	}

	// update-method with all the magic happening in it
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int arg2) throws SlickException {
	}

	// Returning 'ID' from class 'MainMenu'
	@Override
	public int getID() {
		return stateID;
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
					
					tileStyles[x][y] += floorVA;
					
					// top edge?
					if(getTile(x, y - 1) == ' ') {
						topedg = true;
						tileStyles[x][y] += floorETVA;
					}
					
					// right edge?
					if(getTile(x + 1, y) == ' ') {
						rigedg = true;
						tileStyles[x][y] += floorERVA;
					}
					
					// bottom edge?
					if(getTile(x, y + 1) == ' ') {
						botedg = true;
						tileStyles[x][y] += floorEBVA;
					}
					
					// left edge?
					if(getTile(x - 1, y) == ' ') {
						lefedg = true;
						tileStyles[x][y] += floorELVA;
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
					
					tileStyles[x][y] += wallVA;
					
					// top edge?
					if(getTile(x, y - 1) == ' ') {
						topedg = true;
						tileStyles[x][y] += wallETVA;
					}
					
					// right edge?
					if(getTile(x + 1, y) == ' ') {
						rigedg = true;
						tileStyles[x][y] += wallERVA;
					}
					
					// bottom edge?
					if(getTile(x, y + 1) == ' ') {
						botedg = true;
						tileStyles[x][y] += wallEBVA;
					}
					
					// left edge?
					if(getTile(x - 1, y) == ' ') {
						lefedg = true;
						tileStyles[x][y] += wallELVA;
					}
					
					// top right corner
					if(getTile(x + 1, y - 1) == ' ' && !(topedg || rigedg)) {
						tileStyles[x][y] += wallCTR;
					}
					
					// bottom right corner
					if(getTile(x + 1, y + 1) == ' ' && !(rigedg || botedg)) {
						tileStyles[x][y] += wallCBR;
					}
					
					// bottom left corner
					if(getTile(x - 1, y + 1) == ' ' && !(botedg || lefedg)) {
						tileStyles[x][y] += wallCBL;
					}
					
					// top left corner
					if(getTile(x - 1, y - 1) == ' ' && !(lefedg || topedg)) {
						tileStyles[x][y] += wallCTL;
					}
				}
				if(getTile(x, y) == ' ' && getTile(x, y - 1) != ' ') {
					tileStyles[x][y] += dropVA;
				}
			}
		}
		
		for(int y = 0; y < tiles[0].length; y++) {
			for(int x = 0; x < tiles.length; x++) {
				tf.initTile(tileStyles[x][y]);
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
