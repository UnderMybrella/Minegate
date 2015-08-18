package org.abimon.mods.minecraft.minegate;

import java.lang.reflect.Field;

public class PortalOutThread extends Thread 
{

	TileEntityStargateController te;

	public void setTE(TileEntityStargateController te){
		this.te = te;
	}

	public void run(){
		te.checkFrame();
		te.dialOut();
		MineGate.returnThreadToPool(this);
	}

	@Override
	public synchronized void start() {
		try{
			Field field = Thread.class.getDeclaredField("threadStatus");
			field.setAccessible(true);
			field.set(this, 0);
			
			super.start();
		}
		catch(Throwable th){
		}
	}
}
