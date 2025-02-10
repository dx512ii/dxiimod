package dxii.dxiimod.item;

import dxii.dxiimod.dxiimodItems;
import dxii.dxiimod.dxiimodUtils;
import dxii.dxiimod.entity.EntityTomahawk;
import dxii.dxiimod.interfaces.INewItemFunctions;
import dxii.dxiimod.interfaces.INewItemVars;
import dxii.dxiimod.interfaces.IPlayerStuff;
import dxii.dxiimod.item.enums.DamageInfo;
import dxii.dxiimod.item.enums.EDamageTypeExtra;
import dxii.dxiimod.item.enums.EUpgradeType;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.material.ToolMaterial;
import net.minecraft.core.world.World;

public class itemTomahawk extends itemWeaponBase implements INewItemFunctions {

	public ToolMaterial toolMat;
	public int damage;
	public int rangedDamage;

	public DamageInfo dInfo1 = new DamageInfo()
		.setDmgType(EDamageTypeExtra.FIRE)
		.setKnockback(1)
		.setIgnoresIframes(true)
		.setIgnoresResistances(false);


	public itemTomahawk(String name, int id, int stackSize, int cooldown, float range, int meleeDamage, int rangedDamage, ToolMaterial toolMat) {
		super(name, id);
		this.maxStackSize = stackSize;
		this.toolMat = toolMat;
		this.damage = meleeDamage;
		this.rangedDamage = rangedDamage;

		this.upgradeType = EUpgradeType.DEFAULT;

		((INewItemVars)this).dxiimod$setItemRange(range);
		((INewItemVars)this).dxiimod$setItemUsageCooldown(cooldown);
		((INewItemVars)this).dxiimod$setItemCooldown(7);
		((INewItemVars)this).dxiimod$setDoesBreakBlocks(false);
	}

	@Override
	public boolean dxiimod$onItemAttack(EntityPlayer player, ItemStack itemstack, boolean flag){
		dInfo1.setDmg( (int)( this.rangedDamage * getDamageMul(itemstack, player) ) );

		if(flag) {
			dxiimodUtils.playerAABBAttack(itemstack, player, this.dInfo1, 0.25, 1.25, .75, "", .66f, 1, 1);
		}else{
			player.swingItem();
			player.world.playSoundAtEntity(player, player, "dxiimod.swing_hammer", 0.2F, 1F);
		}

		return false;
	}

	@Override
	public boolean dxiimod$onItemParry(EntityPlayer player){
		return false;
	}

	public ItemStack onUseItem(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		((IPlayerStuff)entityplayer).dxiimod$VMsetSpecialAnimVariant();
		byte upgrade = (byte)this.getReinforcement(itemstack);
		if (!world.isClientSide) {
			EntityTomahawk tomahawk = null;
			int dmg = (int)( this.rangedDamage * getDamageMul(itemstack, entityplayer) );

			if(this.toolMat == ToolMaterial.stone) {
				tomahawk = new EntityTomahawk(world, entityplayer, dmg, upgrade, "/assets/dxiimod/textures/entity/tomahawk_stone.png", 0.9f, 0.08f, true, dxiimodItems.stoneTomahawk);
			}else if(this.toolMat == ToolMaterial.iron){
				tomahawk = new EntityTomahawk(world, entityplayer, dmg, upgrade, "/assets/dxiimod/textures/entity/tomahawk_iron.png", 0.97f, 0.08f, true, dxiimodItems.ironTomahawk);
			}else if(this.toolMat == ToolMaterial.gold){
				tomahawk = new EntityTomahawk(world, entityplayer, dmg, upgrade, "/assets/dxiimod/textures/entity/tomahawk_gold.png", 0.97f, 0.065f, true, dxiimodItems.goldTomahawk);
			}else if(this.toolMat == ToolMaterial.diamond){
				tomahawk = new EntityTomahawk(world, entityplayer, dmg, upgrade, "/assets/dxiimod/textures/entity/tomahawk_diamond.png", 0.98f, 0.035f, false, dxiimodItems.diamondTomahawk);
			}

			if(tomahawk != null) {
				entityplayer.swingItem();
				itemstack.consumeItem(entityplayer);
				world.entityJoinedWorld(tomahawk);
				world.playSoundAtEntity(entityplayer, entityplayer, "dxiimod.throw", 0.33F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
			}

		}

		return itemstack;
	}

	@Override
	public boolean dxiimod$onItemAltAttackTimed(EntityPlayer player, ItemStack itemstack, boolean timed){
		return false;
	}

}
