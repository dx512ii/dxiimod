package dxii.dxiimod.item;

import dxii.dxiimod.entity.EntityGoldTomahawk;
import dxii.dxiimod.interfaces.INewItemVars;
import dxii.dxiimod.interfaces.IWorldVariables;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.gamemode.Gamemode;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;

public class tomahawkGoldItem extends Item {

	public tomahawkGoldItem(String name, int id) {
		super(name, id);
		this.maxStackSize = 4;
		((INewItemVars)this).dxiimod$setItemRange(3f);
	}

	public int getDamageVsEntity(Entity entity) {
		return 4;
	}

	@Override
	public boolean beforeDestroyBlock(World world, ItemStack itemStack, int blockId, int x, int y, int z, Side side, EntityPlayer player) {
		return player.getGamemode() != Gamemode.creative;
	}

	public ItemStack onUseItem(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		itemstack.consumeItem(entityplayer);
		if (!world.isClientSide) {
			entityplayer.swingItem();
			world.entityJoinedWorld(new EntityGoldTomahawk(world, entityplayer, !entityplayer.gamemode.areMobsHostile()));
			world.playSoundAtEntity(entityplayer, entityplayer, "dxiimod.throw", 0.33F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

		}

		return itemstack;
	}

	/*
	public boolean onUseItemOnBlock(ItemStack itemstack, EntityPlayer ply, World world, int blockX, int blockY, int blockZ, Side side, double xPlaced, double yPlaced) {
		ply.hurt(ply, 1000, DamageType.COMBAT);
		return false;
	}*/
}
