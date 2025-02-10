package dxii.dxiimod.item;

import dxii.dxiimod.dxiimodUtils;
import dxii.dxiimod.interfaces.INewItemFunctions;
import dxii.dxiimod.interfaces.INewItemVars;
import dxii.dxiimod.item.enums.DamageInfo;
import dxii.dxiimod.item.enums.EDamageTypeExtra;
import dxii.dxiimod.item.enums.EUpgradeType;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.gamemode.Gamemode;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;

public class itemWeaponHammer extends itemWeaponBase implements INewItemFunctions {

	public int damage;
	public double knockback;
	public DamageInfo dInfo = new DamageInfo();

	public itemWeaponHammer(String name, int id, int dmg, double knockback, int cooldown, int durability) {
		super(name, id);
		((INewItemVars)this).dxiimod$setItemRange(3f);
		((INewItemVars)this).dxiimod$setItemCooldown(cooldown);
		((INewItemVars)this).dxiimod$setItemSpeedPenalty(.03);
		((INewItemVars)this).dxiimod$setDoesBreakBlocks(false);
		((INewItemVars)this).dxiimod$setDoesBreakBlocks(false);
		this.setMaxDamage(durability);
		this.damage = dmg;
		this.knockback = knockback;

		this.upgradeType = EUpgradeType.DEFAULT;

		dInfo.setDmg(this.damage);
		dInfo.setDmgType(EDamageTypeExtra.STRIKE);
		dInfo.setKnockback(knockback);
		dInfo.setIgnoresIframes(true);
		dInfo.setIgnoresResistances(false);
	}

	public int getDamageVsEntity(Entity entity) {
		return this.damage;
	}

	@Override
	public boolean dxiimod$onItemAttack(EntityPlayer player, ItemStack itemstack, boolean flag){
		dInfo.setDmg( (int)( this.damage * getDamageMul(itemstack, player) ) );

		if(flag) {
			dxiimodUtils.playerAABBAttack(itemstack, player, this.dInfo, .75, 1.5, 1.5, "step.stone", 2, 2, 1);
		}else{
			player.swingItem();
			player.world.playSoundAtEntity(player, player, "dxiimod.swing_hammer_large", 0.33F, .8F);
		}

		return false;
		//if it returns true - it behaves as usual, breaks blocks, hitscans mobs etc..
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
