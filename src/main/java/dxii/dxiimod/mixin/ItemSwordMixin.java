package dxii.dxiimod.mixin;


import dxii.dxiimod.dxiimodUtils;
import dxii.dxiimod.interfaces.INewItemFunctions;
import dxii.dxiimod.interfaces.INewItemVars;
import dxii.dxiimod.interfaces.IPlayerStuff;
import dxii.dxiimod.item.enums.DamageInfo;
import dxii.dxiimod.item.enums.EDamageTypeExtra;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.material.ToolMaterial;
import net.minecraft.core.item.tool.ItemToolSword;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//changes vanilla swords (I COULD LEAVE THEM UNTOUCHED, BUT THEYRE SO BLAND AFTER CHANGED WEAPONS)
@Mixin(value = ItemToolSword.class, remap = false)
public class ItemSwordMixin extends Item implements INewItemFunctions {

	@Shadow
	private int weaponDamage;

	@Shadow
	private ToolMaterial material;

	@Unique
	public ItemToolSword thisObject = (ItemToolSword)(Object)this;

	@Unique
	public DamageInfo dInfo1 = new DamageInfo()
		.setDmgType(EDamageTypeExtra.SLASH)
		.setKnockback(1.4)
		.setIgnoresIframes(true)
		.setIgnoresResistances(false);

	@Unique
	public DamageInfo dInfo2 = new DamageInfo()
		.setDmgType(EDamageTypeExtra.THRUST)
		.setKnockback(1)
		.setIgnoresIframes(true)
		.setIgnoresResistances(false);


	public ItemSwordMixin(String name, int id) {
		super(name, id);
	}

	@Inject(
		method = "<init>",
		at = @At(value = "TAIL")
	)
	public void initNewSword(String name, int id, ToolMaterial enumtoolmaterial, CallbackInfo ci){
		((INewItemVars)thisObject).dxiimod$setItemCooldown(5);
		((INewItemVars)thisObject).dxiimod$setItemUsageCooldown(15);
		((INewItemVars)thisObject).dxiimod$setDoesBreakBlocks(false);

		dInfo1.setDmg(this.weaponDamage);
		dInfo2.setDmg( (int)(this.weaponDamage*1.25) );
	}

	@Unique
	public boolean isSwordVanilla(){
		return (
					thisObject == Item.toolSwordStone
				||
					thisObject == Item.toolSwordDiamond
				||
					thisObject == Item.toolSwordGold
				||
					thisObject == Item.toolSwordIron
				||
					thisObject == Item.toolSwordSteel
				||
					thisObject == Item.toolSwordWood
		);
	}

	@Override
	public boolean dxiimod$onItemAttack(EntityPlayer player, ItemStack itemstack, boolean flag){
		((INewItemVars)thisObject).dxiimod$setItemCooldown(5);
		dInfo1.setAttacker(player);

		if(flag) {
			dxiimodUtils.playerAABBAttack(itemstack, player, this.dInfo1, .5, 2, .5, "", 1, 1, 1);
		}else{
			player.swingItem();
			if(material == ToolMaterial.stone) {
				player.world.playSoundAtEntity(player, player, "dxiimod.broadsword_swing", 0.25F, (float) (.66 / (Math.random() * 0.4f + 0.8f)));
			}else{
				player.world.playSoundAtEntity(player, player, "dxiimod.broadsword_swing", 0.2F, (float) (1 / (Math.random() * 0.4f + 0.8f)));
			}
		}

		return !this.isSwordVanilla();
	}

	@Override
	public boolean dxiimod$onItemAltAttackTimed(EntityPlayer player, ItemStack itemstack, boolean timed){
		((IPlayerStuff)player).dxiimod$VMsetSpecialAnimVariant();
		((INewItemVars)thisObject).dxiimod$setItemCooldown(15);
		dInfo2.setAttacker(player);


		if(timed) {
			dxiimodUtils.playerAABBAttack(itemstack, player, this.dInfo2, 2.5, .5, .5, "", 1, 1, 1);
			dxiimodUtils.playerAABBAttack(itemstack, player, this.dInfo2, .5, .5, .5, "", 1, 1, 1);
		}else{
			player.swingItem();
			if(material == ToolMaterial.stone) {
				player.world.playSoundAtEntity(player, player, "dxiimod.broadsword_swing", 0.25F, (float) (.66 / (Math.random() * 0.4f + 0.8f)));
			}else{
				player.world.playSoundAtEntity(player, player, "dxiimod.broadsword_swing", 0.2F, (float) (1 / (Math.random() * 0.4f + 0.8f)));
			}
		}

		return this.isSwordVanilla();
	}

	@Override
	public boolean dxiimod$onItemParry(EntityPlayer player){
		return false;
	}

}
