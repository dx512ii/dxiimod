package dxii.dxiimod.mixin.accessors;

import com.mojang.nbt.CompoundTag;
import net.minecraft.core.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = ItemStack.class, remap = false)
public interface IAItemStack {

	@Accessor("tag")
	CompoundTag getTag();
}
