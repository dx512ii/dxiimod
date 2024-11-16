package dxii.dxiimod.mixin.accessors;

import net.minecraft.core.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = EntityPlayer.class, remap = false)
public interface IAEntityPlayer {

	@Invoker("addMovementStat")
	void addMovementStatMixin(double d, double d1, double d2);

}
