package dxii.dxiimod.item;


import dxii.dxiimod.interfaces.ILivingEntityFunctions;
import dxii.dxiimod.interfaces.INewItemVars;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;

public class bucklerItem extends Item {

	public bucklerItem(String name, int id){
		super(name, id);
		((INewItemVars)this).dxiimod$setItemUsageCooldown(20);
		this.maxStackSize = 1;
		this.setMaxDamage(256);
	}

	@Override
	public ItemStack onUseItem(ItemStack itemstack, World world, EntityPlayer player) {
		((ILivingEntityFunctions)player).dxiimod$Parry(3, 3,false);
		return itemstack;
	}


	/*
	would be funny if anyone makes a nuke mod
	with corresponding projectile and this
	thing just 'uno-reverse-cards' it lol

	it works with anything that extends EntityProjectile class
	 */




}
