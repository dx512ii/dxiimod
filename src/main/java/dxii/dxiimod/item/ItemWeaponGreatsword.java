package dxii.dxiimod.item;

import dxii.dxiimod.dxiimodUtils;
import dxii.dxiimod.interfaces.ILivingEntityFunctions;
import dxii.dxiimod.interfaces.INewItemFunctions;
import dxii.dxiimod.interfaces.INewItemVars;
import dxii.dxiimod.interfaces.IPlayerStuff;
import dxii.dxiimod.item.enums.DamageInfo;
import dxii.dxiimod.item.enums.EDamageTypeExtra;
import dxii.dxiimod.item.enums.EUpgradeType;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;

public class ItemWeaponGreatsword extends itemWeaponBase implements INewItemFunctions{

	public float range;
	public int damage = 4;
	public double knockback;
	public int cooldown;
	public DamageInfo dInfo1 = new DamageInfo()
		.setDmgType(EDamageTypeExtra.SLASH)
		.setIgnoresIframes(true)
		.setIgnoresResistances(false);
	public DamageInfo dInfo2;

	public DamageInfo dInfo3;

	public ItemWeaponGreatsword(String name, int id, int dmg, float range, int cooldown, int durability, double knockback) {
		super(name, id);
		this.range = range;
		((INewItemVars)this).dxiimod$setItemRange(range);
		((INewItemVars)this).dxiimod$setItemCooldown(cooldown);
		((INewItemVars)this).dxiimod$setItemSpeedPenalty(.03);
		((INewItemVars)this).dxiimod$setDoesBreakBlocks(false);
		this.setMaxDamage(durability);
		this.cooldown = cooldown;
		this.knockback = knockback;
		this.damage = dmg;

		this.upgradeType = EUpgradeType.DEFAULT;

		dInfo1.setDmg(this.damage);
		dInfo1.setKnockback(knockback);


		dInfo2 = dInfo1;
		dInfo2.setDmg((int)(this.damage*1.25));

		dInfo3 = dInfo2;
		dInfo3.setIgnoresIframes(false);


	}


	@Override
	public boolean dxiimod$onItemAttack(EntityPlayer player, ItemStack itemstack, boolean flag){
		((IPlayerStuff)player).dxiimod$VMswitchAnimVariant();

		dInfo1.setDmg( (int)( this.damage * getDamageMul(itemstack, player) ) );

		if(flag) {
			dxiimodUtils.playerAABBAttack(itemstack, player, this.dInfo1, 1, 2.5, 1.5, "dxiimod.greatsword_impact", .66f, 1, 1);
		}else{
			player.swingItem();
			player.world.playSoundAtEntity(player, player, "dxiimod.greatsword_swing", 0.33F, 1F);
		}

		return false;
	}

	@Override
	public boolean dxiimod$onItemAltAttackTimed(EntityPlayer player, ItemStack itemstack, boolean timed){

		dInfo2.setDmg( (int)( dInfo2.getDmg() * getDamageMul(itemstack, player) ) );
		dInfo3.setDmg( (int)( dInfo3.getDmg() * getDamageMul(itemstack, player) ) );

		if(timed) {
			dxiimodUtils.playerAABBAttack(itemstack, player, this.dInfo2, 3.5, .5, 2, "dxiimod.greatsword_impact", .45f, 1, 1);
			dxiimodUtils.playerAABBAttack(itemstack, player, this.dInfo3, .5, 1, 2, "dxiimod.greatsword_impact", .45f, 1, 1);
		}else{
			((INewItemVars)this).dxiimod$setItemCooldown((int)(cooldown*1.25));
			player.swingItem();
			player.world.playSoundAtEntity(player, player, "dxiimod.greatsword_swing", 0.33F, 1F);
			((IPlayerStuff)player).dxiimod$VMsetSpecialAnimVariant();
		}

		return true;
	}

	@Override
	public boolean dxiimod$onItemParry(EntityPlayer player){
		((ILivingEntityFunctions)player).dxiimod$Parry(2, 3,false);

		return true;
	}
}
