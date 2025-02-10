package dxii.dxiimod.interfaces;

import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;

public interface INewItemFunctions {

	boolean dxiimod$onItemAttack(EntityPlayer player, ItemStack itemstack, boolean doFunc);

	boolean dxiimod$onItemAltAttackTimed(EntityPlayer player, ItemStack itemstack, boolean timed);

	boolean dxiimod$onItemParry(EntityPlayer player); //applies weapon (parry) cooldown if returns true

}
