package dxii.dxiimod.mixin;

import dxii.dxiimod.interfaces.INewItemFunctions;
import dxii.dxiimod.interfaces.INewItemVars;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;


//here i implement new functions for items, range, cooldowns, and should item be swung on us
@Mixin(value = Item.class, remap = false)
public class ItemMixin implements INewItemVars, INewItemFunctions {

	@Unique
	int itemCooldown = 0;

	@Unique
	int usageCooldown = 0;

	@Unique
	double itemRange = 3.5;

	@Unique
	double speedPenalty = 0;

	@Unique
	boolean itemBreaksBlocks = true;

	@Override
	public boolean dxiimod$onItemAttack(EntityPlayer player, ItemStack itemstack, boolean doFunc){
		return true;
	}

	@Override
	public boolean dxiimod$onItemAltAttackTimed(EntityPlayer player, ItemStack itemstack, boolean timed){
		return false;
	}

	@Override
	public boolean dxiimod$onItemParry(EntityPlayer player){
		return false;
	}

	@Override
	public boolean dxiimod$doesBreakBlocks(){
		return this.itemBreaksBlocks;
	}

	@Override
	public void dxiimod$setDoesBreakBlocks(boolean breaks){
		this.itemBreaksBlocks = breaks;
	}

	@Override
	public int dxiimod$getItemCooldown(){
		return this.itemCooldown;
	}

	@Override
	public void dxiimod$setItemUsageCooldown(int ucooldown){
		this.usageCooldown = ucooldown;
	}

	@Override
	public void dxiimod$setItemSpeedPenalty(double mod){
		this.speedPenalty = mod;
	}

	@Override
	public double dxiimod$getItemSpeedMod(){
		return this.speedPenalty;
	}

	@Override
	public int dxiimod$getItemUsageCooldown(){
		return this.usageCooldown;
	}

	@Override
	public void dxiimod$setItemCooldown(int cooldown){
		this.itemCooldown = cooldown;
	}

	@Override
	public double dxiimod$getItemRange(){
		return this.itemRange;
	}

	@Override
	public void dxiimod$setItemRange(double range){
		this.itemRange = range;
	}

}
