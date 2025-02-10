package dxii.dxiimod.mixin;


import dxii.dxiimod.interfaces.IReinforceable;
import dxii.dxiimod.item.itemWeaponBase;
import net.minecraft.client.gui.GuiTooltip;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;


@Mixin(value = GuiTooltip.class, remap = false)
public class GuiTooltipMixin {
	@Redirect(
		method ="getTooltipText(Lnet/minecraft/core/item/ItemStack;ZLnet/minecraft/core/player/inventory/slot/Slot;)Ljava/lang/String;",
		at = @At(value = "INVOKE", target = "net/minecraft/core/item/ItemStack.getDisplayName ()Ljava/lang/String;", ordinal = 0)
	)
	private String giveThisWeaponAPlus(ItemStack stack){
		Item item = stack.getItem();
		int reinforcement = ((IReinforceable)(Object)stack).dxiimod$getReinforcement();
		if(item instanceof itemWeaponBase) {
			return stack.getDisplayName() + " +" + reinforcement;
		}else{
			return stack.getDisplayName();
		}
	}


}
