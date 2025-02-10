package dxii.dxiimod.item;

import dxii.dxiimod.dxiimodItems;
import dxii.dxiimod.entity.EntityBullet;
import dxii.dxiimod.interfaces.INewItemFunctions;
import dxii.dxiimod.interfaces.INewItemVars;
import net.minecraft.core.block.Block;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.phys.Vec3d;

public class ItemWeaponFlintlock extends Item implements INewItemFunctions {

	//0 - normal swing/attack, 1 - shooting
	public int status = 1;


	public ItemWeaponFlintlock(String name, int id){
		super(name, id);
		((INewItemVars)this).dxiimod$setItemCooldown(20);
		((INewItemVars)this).dxiimod$setDoesBreakBlocks(false);
		this.maxStackSize = 1;
		this.setMaxDamage(4096);
	}


	@Override
	public float getStrVsBlock(ItemStack itemstack, Block block) {
		return 0;
	}

	@Override
	public boolean dxiimod$onItemAttack(EntityPlayer entityplayer, ItemStack itemstack, boolean flag){
		if(!flag) {
			if (entityplayer.inventory.consumeInventoryItem(dxiimodItems.bullet_iron.id)) {
				EntityBullet bullet = new EntityBullet(entityplayer.world, entityplayer, false);
				bullet.damage = 20;
				entityplayer.world.entityJoinedWorld(bullet);
				entityplayer.world.playSoundAtEntity(entityplayer, entityplayer, "random.explode", 1.0F, 2.5F);
				entityplayer.world.playSoundAtEntity(entityplayer, entityplayer, "random.explode", 0.8F, 0.5F);
				Vec3d plylook = entityplayer.getLookAngle();
				double newX = bullet.x + plylook.xCoord * .5;
				double newY = bullet.y + plylook.yCoord * .5;
				double newZ = bullet.z + plylook.zCoord * .5;
				entityplayer.world.spawnParticle("largesmoke", newX, newY, newZ, 0.0, 0.0, 0.0, 0);
				itemstack.damageItem(1, entityplayer);
				entityplayer.swingItem();
			} else if (entityplayer.inventory.consumeInventoryItem(dxiimodItems.bullet_steel.id)) {
				EntityBullet bullet = new EntityBullet(entityplayer.world, entityplayer, true);
				bullet.damage = 25;
				entityplayer.world.entityJoinedWorld(bullet);
				entityplayer.world.playSoundAtEntity(entityplayer, entityplayer, "random.explode", 1.0F, 2.5F);
				entityplayer.world.playSoundAtEntity(entityplayer, entityplayer, "random.explode", 0.8F, 0.5F);
				Vec3d plylook = entityplayer.getLookAngle();
				double newX = bullet.x + plylook.xCoord * .5;
				double newY = bullet.y + plylook.yCoord * .5;
				double newZ = bullet.z + plylook.zCoord * .5;
				entityplayer.world.spawnParticle("largesmoke", newX, newY, newZ, 0.0, 0.0, 0.0, 0);
				itemstack.damageItem(1, entityplayer);
				entityplayer.swingItem();
			} else {
				entityplayer.world.playSoundAtEntity(entityplayer, entityplayer, "random.click", 0.25F, 0.5F);
			}
		}

		return false;
	}

	@Override
	public boolean dxiimod$onItemAltAttackTimed(EntityPlayer player, ItemStack itemstack, boolean timed){
		return false;
	}

	@Override
	public boolean dxiimod$onItemParry(EntityPlayer player){
		return false;
	}

}
