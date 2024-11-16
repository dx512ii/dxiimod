package dxii.dxiimod.item;

import dxii.dxiimod.interfaces.IWorldVariables;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;

public class itemSuspiciousTotem extends Item {

	public itemSuspiciousTotem(String name, int id){
		super(name, id);
		this.maxStackSize = 1;
	}

	public ItemStack onUseItem(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		if( !((IWorldVariables)(world.getLevelData()) ).dxiimod$getFog() && dxii.dxiimod.dxiimodMain.THE_FOG == 1) {
			world.sendGlobalMessage("Something irreversible happened...");

			if (!world.isClientSide) {
				entityplayer.swingItem();
				world.playSoundAtEntity(entityplayer, entityplayer, "step.wood", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
			}

			((IWorldVariables) (world.getLevelData())).dxiimod$setFog(true);
			((IWorldVariables) (world.getLevelData())).dxiimod$setFogDay( (int)(world.getWorldTime() / 24000f) );

			itemstack.consumeItem(entityplayer);
		} else if (dxii.dxiimod.dxiimodMain.THE_FOG == 0) {
            world.sendGlobalMessage("Nothing happened, it seems to be broken...");
		}
		return itemstack;
	}

}
