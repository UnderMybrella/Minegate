package org.abimon.mods.minecraft.minegate.tileentity;

import cpw.mods.fml.common.Optional.Interface;
import vazkii.botania.api.mana.IManaCollisionGhost;

@Interface(iface = "vazkii.botania.api.mana.IManaCollisionGhost", modid = "Botania")
public class TileEntityFakePortal extends TileEntityStargatePortal implements IManaCollisionGhost{

	@Override
	public boolean isGhost() {
		return true;
	}

}
