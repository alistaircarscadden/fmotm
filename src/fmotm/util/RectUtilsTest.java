package fmotm.util;

import org.newdawn.slick.geom.Rectangle;

public class RectUtilsTest {

	public RectUtilsTest() {
		
	}
	
	public static void main(String[] args) {
		float[] inter = RectUtils.intersection1D(0, 1, 0.5f, 1);
		if(inter == null) return;
			System.out.printf("%f, %f", inter[0], inter[1]);
		
		//Rectangle intersection = RectUtils.intersection2D(1, 0, 1, 1, 0.5f, 0.5f, 1, 1);
		//System.out.printf("Intersection: (%.3f, %.3f, %.3f, %.3f\n", intersection.getX(), intersection.getY(), intersection.getWidth(), intersection.getHeight());
	}

}
