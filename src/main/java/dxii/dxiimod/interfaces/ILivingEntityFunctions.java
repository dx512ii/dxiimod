package dxii.dxiimod.interfaces;

import net.minecraft.core.entity.Entity;

public interface ILivingEntityFunctions {

	void dxiimod$parryStun(Entity attacker, int damage);
	boolean dxiimod$getIsJumping();

	void dxiimod$evileyeheal(); // that one could be in EntityPlayerMixin but ah, lets make another bloat of a mixin

	int dxiimod$getParryTicks();
	int dxiimod$getParriedTicks();
	void dxiimod$Parry(int parryTicks, int delay, boolean accurate);

}
