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
	public Map<Long, Image> parts;
	public Map<Long, Image> tiles;
	
	long largestKey;
	private float tileScale;
	private final int tilePixelSize = 8;
	private int numTiles;
	
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
}
