package fmotm.game.world;

import static java.lang.Math.pow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.ImageBuffer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public class TileFeel {
	private Map<Long, Image> parts;
	private Map<Long, Image> tiles;
	
	private long largestKey;
	private float tileScale;
	private final int tilePixelSize = 8;
	private int numTiles;
	
	// Part identifiers
	private final long floorVA        = 1L;
	private final long floorVB        = 2L;
	private final long floorVC        = 4L;
	private final long floorVD        = 8L;
	private final long floorCTR       = 16L;
	private final long floorCBR       = 32L;
	private final long floorCBL       = 64L;
	private final long floorCTL       = 128L;
	private final long floorETVA      = 256L;
	private final long floorETVB      = 512L;
	private final long floorERVA      = 1024L;
	private final long floorERVB      = 2048L;
	private final long floorEBVA      = 4096L;
	private final long floorEBVB      = 8192L;
	private final long floorELVA      = 16384L;
	private final long floorELVB      = 32768L;
	private final long wallVA         = 65536L;
	private final long wallVB         = 131072L;
	private final long wallVC         = 262144L;
	private final long wallVD         = 524288L;
	private final long wallCTR        = 1048576L;
	private final long wallCBR        = 2097152L;
	private final long wallCBL        = 4194304L;
	private final long wallCTL        = 8388608L;
	private final long wallETVA       = 16777216L;
	private final long wallETVB       = 33554432L;
	private final long wallERVA       = 67108864L;
	private final long wallERVB       = 134217728L;
	private final long wallEBVA       = 268435456L;
	private final long wallEBVB       = 536870912L;
	private final long wallELVA       = 1073741824L;
	private final long wallELVB       = 2147483648L;
	private final long wallBottomER   = 4294967296L;
	private final long wallbottomEL   = 8589934592L;
	private final long wallBottomFixR = 17179869184L;
	private final long wallBottomFixL = 34359738368L;
	// Missing values of 2^n
	private final long dropVA         = 1099511627776L;
	private final long dropVB         = 2199023255552L;
	private final long dropVC         = 4398046511104L;
	private final long dropVD         = 8796093022208L;
	
	/* Constructor initializes the parts used to create tile images. Tiles
	 * must be individually added externally by calling the initTile function
	 * with the ID of the tile you would like to initialize. On world load
	 * every tile should be loaded.
	 */
	public TileFeel(String ref, float tileScale) {
		this.parts = new HashMap<Long, Image>();
		this.tiles = new HashMap<Long, Image>();
		this.tileScale = tileScale;
		this.numTiles = 0;
		
		try {
			Image blank = new Image(tilePixelSize, tilePixelSize);
			blank.setAlpha(0);
			tiles.put(0L, blank);
			
			SpriteSheet sprites = new SpriteSheet(ref, tilePixelSize, tilePixelSize);
			int x = sprites.getHorizontalCount();
			int y = sprites.getVerticalCount();
			int numSprites = x * y;
			
			for(int i = 0; i < numSprites; i++) {
				long key = (long) pow(2, i);
				parts.put(key, sprites.getSprite(i % x, i / x));
				largestKey = key;
			}
		} catch(SlickException e) {
			e.printStackTrace();
		}
	}
	
	/* Given a long ID, this function will generate the tile Image associated
	 * with that number. First it finds all the parts it needs, then adds them
	 * smallest to largest (left to right, top down in the SpriteSheet). It must
	 * find the parts largest to smallest then reverse the order to add them.
	 */
	public void initTile(long id) {
		if(tiles.get(id) != null) return;

		List<Image> includedParts = new ArrayList<Image>();
		long id_cpy = id;

		for(long g = largestKey; g > 0; g /= 2) {
			if(id_cpy - g >= 0) {
				id_cpy -= g;
				includedParts.add(parts.get(g));
			}
		}

		ImageBuffer finalImgBuf = new ImageBuffer(8, 8);
		
		for(int i = includedParts.size() - 1; i >= 0; i--)
			addImageToBuffer(finalImgBuf, includedParts.get(i));
		
		Image finalImage = finalImgBuf.getImage();
		finalImage.setFilter(Image.FILTER_NEAREST);
		finalImage = finalImage.getScaledCopy(tileScale / tilePixelSize);
		tiles.put(id, finalImage);
		System.out.printf("%d inits.\n", ++numTiles);
	}
	
	/* Given an ImageBuffer to and an Image from it adds the pixels from from to to.
	 * Pixels with Color = (45, 52, 41) (ERA in HEX) will be erased from the buffer (eraser pixels)
	 */
	public void addImageToBuffer(ImageBuffer to, Image from) {
		for(int x = 0; x < from.getWidth(); x++) {
			for(int y = 0; y < from.getHeight(); y++) {
				Color pixelColor = from.getColor(x, y);
				
				if(pixelColor.getAlpha() > 0) {
					to.setRGBA(x, y, pixelColor.getRed(), pixelColor.getGreen(), pixelColor.getBlue(), pixelColor.getAlpha());
				}
			}
		}
	}
	
	public Image getTile(long id) {
		if(!tiles.containsKey(id)) {
			return tiles.get(0L);
		}
		
		return tiles.get(id);
	}

	public long[][] generateTileStyles(final char[][] tiles) {
		if(tiles.length == 0 || tiles[0].length == 0)
			return new long[0][0];
		
		long[][] tileStyles;
		tileStyles = new long[tiles.length][tiles[0]length];
		
		for(int x = 0; x < tiles.length; x++) {
			for(int y = 0; y < tiles[0].length; y++) {
				// Generate tile style identifier for (x, y)
				tileStyles[x][y] = generateTileStyle(tiles, x, y);
				// Teach this object the texture for that identifier
				initTile(tileStyles[x][y]);
			}
		}
		
		return tileStyles;
	}
	
	public void updateTileStyles(final char[][] tiles, long[][] tileStyles, int x, int y, int width, int height) {
		for(; x < x + width; x++) {
			for(; y < y + height; y++) {
				// Generate tile style identifier for (x, y)
				tileStyles[x][y] = generateTileStyle(tiles, x, y);
				// Teach this object the texture for that identifier
				initTile(tileStyles[x][y]);
			}
		}
		
		return tileStyles;
	}
	
	private long generateTileStyle(final char[][] tiles, int x, int y) {
		Random random = new Random(System.currentTimeMillis());
		float r;
		long tileStyle = 0;
				
		if(getTile(tiles, x, y) == 'F') {
			boolean topedg = false;
			boolean rigedg = false;
			boolean botedg = false;
			boolean lefedg = false;
			
			r = random.nextFloat();
			if (r > 0.75) tileStyle += floorVA;
			else if (r > 0.5) tileStyle += floorVB;
			else if (r > 0.25) tileStyle += floorVC;
			else tileStyle += floorVD;
			
			// top edge?
			if(getTile(tiles, x, y - 1) == ' ') {
				topedg = true;
				r = random.nextFloat();
				if (r > 0.5) tileStyle += floorETVA;
				else tileStyle += floorETVB;
			}
			
			// right edge?
			if(getTile(tiles, x + 1, y) == ' ') {
				rigedg = true;
				r = random.nextFloat();
				if (r > 0.5) tileStyle += floorERVA;
				else tileStyle += floorERVB;
			}
			
			// bottom edge?
			if(getTile(tiles, x, y + 1) == ' ') {
				botedg = true;
				r = random.nextFloat();
				if (r > 0.5) tileStyle += floorEBVA;
				else tileStyle += floorEBVB;
			}
			
			// left edge?
			if(getTile(tiles, x - 1, y) == ' ') {
				lefedg = true;
				r = random.nextFloat();
				if (r > 0.5) tileStyle += floorELVA;
				else tileStyle += floorELVB;
			}
			
			// top right corner
			if(getTile(tiles, x + 1, y - 1) == ' ' && !(topedg || rigedg)) {
				tileStyle += floorCTR;
			}
			
			// bottom right corner
			if(getTile(tiles, x + 1, y + 1) == ' ' && !(rigedg || botedg)) {
				tileStyle += floorCBR;
			}
			
			// bottom left corner
			if(getTile(tiles, x - 1, y + 1) == ' ' && !(botedg || lefedg)) {
				tileStyle += floorCBL;
			}
			
			// top left corner
			if(getTile(tiles, x - 1, y - 1) == ' ' && !(lefedg || topedg)) {
				tileStyle += floorCTL;
			}
		}
		if(getTile(tiles, x, y) == 'W') {
			boolean topedg = false;
			boolean rigedg = false;
			boolean botedg = false;
			boolean lefedg = false;
			
			r = random.nextFloat();
			if (r > 0.75) tileStyle += wallVA;
			else if (r > 0.5) tileStyle += wallVB;
			else if (r > 0.25) tileStyle += wallVC;
			else tileStyle += wallVD;
			
			// top edge?
			if(getTile(tiles, x, y - 1) != 'W') {
				topedg = true;
				r = random.nextFloat();
				if (r > 0.5) tileStyle += wallETVA;
				else tileStyle += wallETVB;
			}
			
			// right edge?
			if(getTile(tiles, x + 1, y) != 'W') {
				rigedg = true;
				r = random.nextFloat();
				if (r > 0.5) tileStyle += wallERVA;
				else tileStyle += wallERVB;
			}
			
			// bottom edge?
			if(getTile(tiles, x, y + 1) != 'W') {
				botedg = true;
				r = random.nextFloat();
				if (r > 0.5) tileStyle += wallEBVA;
				else tileStyle += wallEBVB;
			}
			
			// left edge?
			if(getTile(tiles, x - 1, y) != 'W') {
				lefedg = true;
				r = random.nextFloat();
				if (r > 0.5) tileStyle += wallELVA;
				else tileStyle += wallELVB;
			}
			
			// top right corner
			if(getTile(tiles, x + 1, y - 1) != 'W' && !(topedg || rigedg)) {
				tileStyle += wallCTR;
			}
			
			// bottom right corner
			if(getTile(tiles, x + 1, y + 1) != 'W' && !(rigedg || botedg)) {
				tileStyle += wallCBR;
			}
			
			// bottom left corner
			if(getTile(tiles, x - 1, y + 1) != 'W' && !(botedg || lefedg)) {
				tileStyle += wallCBL;
			}
			
			// top left corner
			if(getTile(tiles, x - 1, y - 1) != 'W' && !(lefedg || topedg)) {
				tileStyle += wallCTL;
			}
			
			// wall bottom left&right edges
			if(botedg && rigedg) {
				tileStyle += wallBottomER;
			}
			
			if(botedg && lefedg) {
				tileStyle += wallBottomEL;
			}
			
			// wall bottom neighboring edge fixes
			if(!botedg) {
				if(!rigedg && getTile(tiles, x + 1, y + 1) != 'W') {
					tileStyle += wallBottomFixR;
				}
			
				if(!lefedg && getTile(tiles, x - 1, y + 1) != 'W') {
					tileStyle += wallBottomFixL;
				}
			}
		}
		if(getTile(tiles, x, y) == ' ' && getTile(tiles, x, y - 1) != ' ') {
			r = random.nextFloat();
			if (r > 0.75) tileStyle += dropVA;
			else if (r > 0.5) tileStyle += dropVB;
			else if (r > 0.25) tileStyle += dropVC;
			else tileStyle += dropVD;
		}
		
		return tileStyle;
	}
	
	/* Gets a specific tile char from **tiles at the coordinates (x, y)
	 * returns ' ' if out of bounds (or if tile IS ' ')
	 */
	private char getTileChar(final char[][] tiles, int x, int y) {
		if(tiles.length > 0 && tiles[0].length > 0 && x >= 0 && x < tiles.length && y >= 0 && y < tiles[0].length) {
			return tiles[x][y];
		}
		
		return ' ';
	}
}
