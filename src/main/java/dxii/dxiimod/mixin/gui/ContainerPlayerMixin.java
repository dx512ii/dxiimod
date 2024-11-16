package dxii.dxiimod.mixin.gui;

import dxii.dxiimod.gui.SlotAccessory;
import dxii.dxiimod.mixin.accessors.IAContainerPlayer;
import net.minecraft.core.player.inventory.ContainerPlayer;
import net.minecraft.core.player.inventory.InventoryPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(value = ContainerPlayer.class, remap = false)
public class ContainerPlayerMixin {

	@Unique
	protected ContainerPlayer thisObject = (ContainerPlayer)(Object)this;

	@Inject(
		method = "<init>(Lnet/minecraft/core/player/inventory/InventoryPlayer;Z)V",
		at = @At(value = "INVOKE", target = "onCraftMatrixChanged")
	)
	public void newSlots(InventoryPlayer inventory, boolean isNotClientSide, CallbackInfo ci){
		for (int i = 0; i < 4; ++i) {
			((IAContainerPlayer) this).addSlotMixin(new SlotAccessory(inventory, 40 + i, 88 + i*18, 64));
		}
	}
}
