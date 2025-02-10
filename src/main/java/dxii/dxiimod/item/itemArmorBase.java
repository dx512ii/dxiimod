package dxii.dxiimod.item;

import dxii.dxiimod.interfaces.IReinforceable;
import dxii.dxiimod.item.enums.EUpgradeType;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemArmor;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.material.ArmorMaterial;

public class itemArmorBase extends ItemArmor {

	public final int weight = 0;


	public final int ThrustResist = 0;
	public final int StrikeResist = 0;
	public final int SlashResist = 0;
	public final int MagicResist = 0;
	public final int PoisonResist = 0;
	public final int BloodResist = 0;
	public final int LightningResist = 0;
	public final int DarkResist = 0;
	public final int FireResist = 0;

	public final double dmgPerLvlNormal = 0.1;
	public final double dmgPerLvlTwinkling = 0.3;

	public EUpgradeType upgradeType;

	public itemArmorBase(String name, int id, int piece){
		super(name, id, ArmorMaterial.IRON, piece);
	}

	public double getDamageMul(ItemStack stack, EntityPlayer player){
		switch(upgradeType) {
			case DEFAULT:
				return 1 + ((IReinforceable)(Object)stack).dxiimod$getReinforcement() * dmgPerLvlNormal;
			case TWINKLING:
				return 1 + ((IReinforceable)(Object)stack).dxiimod$getReinforcement() * dmgPerLvlTwinkling;
			default: return 1;
		}
	}
}
