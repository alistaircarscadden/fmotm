package fmotm.game.world;

import java.util.HashMap;
import java.util.Map;
import static java.lang.Math.pow;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.ImageBuffer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public class TileFeel {
	private Map<Long, Image> parts = new HashMap<Long, Image>();
	long largestKey;
	private Map<Long, Image> tiles = new HashMap<Long, Image>();

	private float scale;
	
	/* Constructor initializes the parts used to create tile images. Tiles
	 * must be individually added externally by calling the initTile function
	 * with the ID of the tile you would like to initialize. On world load
	 * every tile should be loaded.
	 */
	public TileFeel(String ref, float scale) {
		this.scale = scale;
		
		try {
			SpriteSheet sprites = new SpriteSheet(ref, 8, 8);
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
		Map<Long, Image> includedParts = new HashMap<Long, Image>();
		long id_cpy = id;

			for(long g = largestKey; g > 0; g /= 2) {
				if(id_cpy - g >= 0) {
					id_cpy -= g;
					includedParts.put(g, parts.get(g));
				}
			}

			ImageBuffer finalImgBuf = new ImageBuffer(8, 8);
			for(long k : parts.keySet()) {
				addImageToBuffer(finalImgBuf, parts.get(k));
			}
			
			tiles.put(id, finalImgBuf.getImage().getScaledCopy(scale));
	}
	
	/* Given an ImageBuffer to and an Image from it adds the pixels from from to to.
	 * Pixels with Color = (254, 1, 0) will be erased from the buffer (eraser pixels)
	 */
	// TODO the eraser pixels
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
}
