package dxii.dxiimod.mixin;


import net.minecraft.core.entity.EntityItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;


@Mixin(value = EntityItem.class, remap = false)
public class EntityItemMixin {

	@Unique
	EntityItem thisObject = (EntityItem) (Object)this;

	@Inject(
		method = "tick()V",
		at = @At(value = "HEAD")
	)
	public void soulsNoGravity(CallbackInfo ci){
        if (thisObject.item == null) return;
		if(Objects.equals(thisObject.item.getItemKey(), "item.dxiimod.SOULPEACE")
			|| Objects.equals(thisObject.item.getItemKey(), "item.dxiimod.SOULPEACEGREAT")
			|| Objects.equals(thisObject.item.getItemKey(), "item.dxiimod.SOULEVIL")
			|| Objects.equals(thisObject.item.getItemKey(), "item.dxiimod.SOULEVILGREAT"))
		{
			thisObject.xd *= .8;
			thisObject.yd = 0;
			thisObject.yd += 0.04;
			thisObject.zd *= .8;
		}
	}
}
