package dxii.dxiimod.item;

import dxii.dxiimod.interfaces.IReinforceable;
import dxii.dxiimod.item.enums.EUpgradeType;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;

public class itemWeaponBase extends Item {

	public final int maxReinforcement = 10;
	public final int maxReinforcementTwinkling = 5;
	public final double dmgPerLvlNormal = 0.1;
	public final double dmgPerLvlTwinkling = 0.3;

	public EUpgradeType upgradeType;

	public itemWeaponBase(String name, int id){
		super(name, id);
	}

	public int getReinforcement(ItemStack stack){
		return ((IReinforceable)(Object)stack).dxiimod$getReinforcement();
	}

	public int getMaxReinforcement(){
		if(upgradeType == EUpgradeType.DEFAULT){return 10;}
		if(upgradeType == EUpgradeType.TWINKLING){return 5;}
		return 0;
	}

	public double getDamageMul(ItemStack stack, EntityPlayer player){
		if(this.upgradeType != null) {
			switch (upgradeType) {
				case DEFAULT:
					return 1 + ((IReinforceable) (Object) stack).dxiimod$getReinforcement() * dmgPerLvlNormal;
				case TWINKLING:
					return 1 + ((IReinforceable) (Object) stack).dxiimod$getReinforcement() * dmgPerLvlTwinkling;
				default:
					return 1;
			}
		}else{
			return 1;
		}
	}
}
