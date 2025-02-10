package dxii.dxiimod.item;

import dxii.dxiimod.dxiimodUtils;
import dxii.dxiimod.interfaces.INewItemFunctions;
import dxii.dxiimod.interfaces.INewItemVars;
import dxii.dxiimod.item.enums.DamageInfo;
import dxii.dxiimod.item.enums.EDamageTypeExtra;
import dxii.dxiimod.item.enums.EUpgradeType;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.gamemode.Gamemode;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;


public class ItemWeaponSpear extends itemWeaponBase implements INewItemFunctions {

	public float range;
	public int damage;
	public DamageInfo dInfo = new DamageInfo();
	public DamageInfo dInfo2 = new DamageInfo();


	public ItemWeaponSpear(String name, int id, float range, int dmg, int cooldown, int durability) {
		super(name, id);
		this.range = range-1;
		this.damage = dmg;
		((INewItemVars)this).dxiimod$setItemRange(range);
		((INewItemVars)this).dxiimod$setItemCooldown(cooldown);
		((INewItemVars)this).dxiimod$setItemSpeedPenalty(.012);
		((INewItemVars)this).dxiimod$setDoesBreakBlocks(false);
		this.setMaxDamage(durability);

		dInfo.setDmg(this.damage);
		dInfo.setDmgType(EDamageTypeExtra.THRUST);
		dInfo.setKnockback(1.4);
		dInfo.setIgnoresIframes(false);
		dInfo.setIgnoresResistances(false);

		this.upgradeType = EUpgradeType.DEFAULT;

		dInfo2 = dInfo;
		dInfo2.setIgnoresIframes(true);
	}

	@Override
	public boolean onBlockDestroyed(World world, ItemStack itemstack, int i, int j, int k, int l, Side side, EntityLiving entityliving) {
		if( ((EntityPlayer)entityliving).gamemode != Gamemode.creative ) {
			itemstack.damageItem(4, entityliving);
		}
		return true;
	}

	@Override
	public boolean dxiimod$onItemAttack(EntityPlayer player, ItemStack itemstack, boolean flag){
		dInfo.setDmg( (int)( this.damage * getDamageMul(itemstack, player) ) );

		if(flag) {
			dxiimodUtils.playerAABBAttack(itemstack, player, this.dInfo, 0, .3, .3, "", 1, 1, 1);
			dxiimodUtils.playerAABBAttack(itemstack, player, this.dInfo, 2*(this.range/4), .3, .3, "", 1, 1, 1);
			dxiimodUtils.playerAABBAttack(itemstack, player, this.dInfo, 3*(this.range/4), .3, .3, "", 1, 1, 1);
			dxiimodUtils.playerAABBAttack(itemstack, player, this.dInfo2, this.range, .3, .3, "", 1, 1, 1);
		}else{
			player.swingItem();
			player.world.playSoundAtEntity(player, player, "dxiimod.spear", 0.1F, 1F);
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

	@Override
	public boolean beforeDestroyBlock(World world, ItemStack itemStack, int blockId, int x, int y, int z, Side side, EntityPlayer player) {
		return player.getGamemode() != Gamemode.creative;
	}

}
