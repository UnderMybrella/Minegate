package org.abimon.mods.minecraft.minegate;

import java.util.LinkedList;

import org.abimon.mods.minecraft.minegate.tileentity.TileEntityStargateController;

import net.minecraft.entity.Entity;


/** Just a quick note on directions:
 *  The direction of the gate determines the EXIT point
 *  Entering a Stargate doesn't require a direction, per se, just a general one.
 *  By this, if someone enters a direction with X_POSITIVE, it's the same as if you entered a gate with X_NEGATIVE or X_NEUTRAL
 *  However, the type of Axis for the exit is crucial! X_NEGATIVE is different from X_POSITIVE. X_NEGATIVE will convert it to the X axis, then make it negative, and the reverse happens for X_POSITIVE
 *  X_NEUTRAL doesn't change the sign of the X momentum
 * @author Undermybrella
 *
 */
public enum EnumDirection
{

	X_POSITIVE,
	Y_POSITIVE,
	Z_POSITIVE,

	X_NEGATIVE,
	Y_NEGATIVE,
	Z_NEGATIVE,

	X_NEUTRAL,
	Y_NEUTRAL,
	Z_NEUTRAL,
	UNKNOWN;

	public static EnumDirection getShapeOfStargate(TileEntityStargateController controller){
		//Check for 2D Stargate
		LinkedList<Integer> uniqueXAxis = new LinkedList<Integer>();
		LinkedList<Integer> uniqueYAxis = new LinkedList<Integer>();
		LinkedList<Integer> uniqueZAxis = new LinkedList<Integer>();

		controller.checkFrame();
		for(Location loc : controller.frameBlocks){
			if(!uniqueXAxis.contains(loc.x))
				uniqueXAxis.add(loc.x);
			if(!uniqueYAxis.contains(loc.y))
				uniqueYAxis.add(loc.y);
			if(!uniqueZAxis.contains(loc.z))
				uniqueZAxis.add(loc.z);
		}

		if(uniqueXAxis.size() > 1 && uniqueYAxis.size() > 1 && uniqueZAxis.size() == 1)
			return X_NEUTRAL;
		if(uniqueXAxis.size() > 1 && uniqueZAxis.size() > 1 && uniqueYAxis.size() == 1)
			return Y_NEUTRAL;
		if(uniqueZAxis.size() > 1 && uniqueYAxis.size() > 1 && uniqueXAxis.size() == 1)
			return Z_NEUTRAL;

		return UNKNOWN;
	}

	public boolean isXAxis(){
		return this == X_POSITIVE || this == X_NEGATIVE || this == X_NEUTRAL;
	}

	public boolean isYAxis(){
		return this == Y_POSITIVE || this == Y_NEGATIVE || this == Y_NEUTRAL;
	}

	public boolean isZAxis(){
		return this == Z_POSITIVE || this == Z_NEGATIVE || this == Z_NEUTRAL;
	}

	public boolean isPositive(){
		return this == X_POSITIVE || this == Y_POSITIVE || this == Z_POSITIVE;
	}

	public boolean isNegative(){
		return this == X_NEGATIVE || this == Y_NEGATIVE || this == Z_NEGATIVE;
	}

	public boolean isNeutral(){
		return this == X_NEUTRAL || this == Y_NEUTRAL || this == Z_NEUTRAL;
	}

	public void setMotion(Entity entity, double motion){
		if(isXAxis())
			entity.motionX = motion;
		if(isYAxis())
			entity.motionY = motion;
		if(isZAxis())
			entity.motionZ = motion;
	}

	public void setRotation(Entity entity){
		if(isNeutral())
			entity.rotationYaw = 0;
		else
		{
			if(isXAxis())
				entity.rotationYaw = isNegative() ? 90 : 270;
			if(isZAxis())
				entity.rotationYaw = isNegative() ? 180 : 0;
		}
	}
}
