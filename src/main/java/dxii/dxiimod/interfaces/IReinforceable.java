package dxii.dxiimod.interfaces;

import dxii.dxiimod.item.enums.EUpgradeType;

public interface IReinforceable {

	byte dxiimod$getReinforcement();
	void dxiimod$setReinforcement(byte reinforcement);
	boolean dxiimod$reinforceItem();
	boolean dxiimod$downgradeItem();


}
