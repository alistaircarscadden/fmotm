package fmotm.game.entity;

import org.newdawn.slick.geom.Rectangle;

import fmotm.game.world.World;

public class Scorpion extends MotherScorpion {
	private MotherScorpion mother;
	
	public Scorpion(World world, int typeID, Rectangle position, MotherScorpion mother) {
		super(world, typeID, position);
		this.dispSize = 0.5f;
		this.mother = mother;
		System.out.println(this.mother == null);
	}
	
	@Override
	protected void newTargetPos() {
		if(random.nextFloat() > 0.3) {
			super.newTargetPos();
		} else {
			timeGiveUp = System.currentTimeMillis() + 6000;
			
			targetPos.x = mother.position.getX();
			targetPos.y = mother.position.getY();
		}
	}

}
