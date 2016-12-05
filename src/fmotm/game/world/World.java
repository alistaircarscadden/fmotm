package fmotm.game.world;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.ResourceLoader;

import fmotm.Application;
import fmotm.game.entity.EntityManager;
import fmotm.game.entity.MotherScorpion;
import fmotm.game.entity.Player;
import fmotm.game.entity.Scorpion;

public class World {
	public char tiles[][];
	private long tileStyles[][];
	private TileFeel tileFeel;
	public Player player;
	Camera camera; // Camera refers to position of display within world coordinates
	private EntityManager entityManager;
	private BitSet input;
	
	// World Properties
	public final float tileScale = 80f;
	
	// TypeIDs
	private final int entityID = 0;
	private final int playerID = 1;
	private final int motherScorpionID = 2;
	private final int scorpionID = 3;
	
	public World(BitSet input) {
		this.input = input;
		this.entityManager = new EntityManager(new Rectangle(0f, 0f, 100f, 100f), tileScale);
		this.player = new Player(this, playerID, new Rectangle(0f, 0f, .7f, .7f));
		player.setInput(input);
		this.entityManager.addEntity(player);
		this.camera = new Camera(0, 0, tileScale, Application.WIDTH, Application.HEIGHT);
		
		MotherScorpion motherScorpion = new MotherScorpion(this, motherScorpionID, new Rectangle(13f, 5f, 2f, 2f));
		this.entityManager.addEntity(motherScorpion);
		for(int i = 0; i < 8; i++)
			this.entityManager.addEntity(new Scorpion(this, scorpionID, new Rectangle(13f, 5f, 2f, 2f), motherScorpion));
	}
	
	public void loadWorld(String ref) {
		try {
			InputStream file = ResourceLoader.getResourceAsStream(ref);
			Scanner scanner = new Scanner(file);
			String tileFeelString = null;
			
			if(scanner.hasNextLine()) {
				tileFeelString = scanner.nextLine();
			}
			
			List<String> lines = new ArrayList<String>();
			int longestLine = 0;
			
			while(scanner.hasNextLine()) {
				String line = scanner.nextLine();
				lines.add(line);
				if(line.length() > longestLine) longestLine = line.length();
			}
			
			scanner.close();
			
			char[][] tiles = new char[longestLine][lines.size() + 1];
			
			for(int i = 0; i < tiles[0].length; i++) {
				for(int j = 0; j < tiles.length; j++) {
					tiles[j][i] = ' ';
				}
			}
			
			for(int i = 0; i < lines.size(); i++) {
				for(int j = 0; j < lines.get(i).length(); j++) {
					tiles[j][i] = lines.get(i).charAt(j);
					
					if(tiles[j][i] == 'P') {
						player.position.setX(j);
						player.position.setY(i);
						tiles[j][i] = 'F';
					}
				}
			}
			
			this.tiles = tiles;
			
			setTileFeel(tileFeelString);
			this.tileStyles = this.tileFeel.generateTileStyles(tiles);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
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
		camera.setX(player.position.getX() - (gc.getWidth() / 2) / tileScale);
		camera.setY(player.position.getY() - (gc.getHeight() / 2) / tileScale);
		
		drawTiles(camera);
		entityManager.drawEntities(camera);
	}
	
	public void drawTile(int x, int y) {
		tileFeel.getTile(getTileStyle(x, y)).draw((x - camera.getX()) * tileScale, (y - camera.getY()) * tileScale);
	}
	
	public void drawTiles(Camera camera) {
		for(int x0 = (int) camera.getX(); x0 < camera.getMaxX(); x0++) {
			for(int y0 = (int) camera.getY(); y0 < camera.getMaxY(); y0++) {
				drawTile(x0, y0);
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
				
				if(f > 1) {
					tiles[x][y] = ' ';
				} else if(f > 0.9) {
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
	
	public boolean tileIsUnpassable(int x, int y) {
		if(tiles.length > 0 && tiles[0].length > 0 && x >= 0 && x < tiles.length && y >= 0 && y < tiles[0].length) {
			if(tiles[x][y] == 'W') return true;
		}
		
		return false;
	}
}