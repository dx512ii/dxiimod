package dxii.dxiimod.interfaces;


import dxii.dxiimod.item.enums.DamageInfo;
import dxii.dxiimod.item.enums.DamageResistModule;

public interface ILivingEntityDamageTypes {

	DamageResistModule dxiimod$getDamageResistModule();
	void dxiimod$hurtCustom(DamageInfo dInfo);


}
