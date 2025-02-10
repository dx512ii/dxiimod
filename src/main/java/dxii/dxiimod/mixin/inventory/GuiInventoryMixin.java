package dxii.dxiimod.mixin.inventory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;


@Mixin(value = GuiInventory.class, remap = false)
public class GuiInventoryMixin {

	@Unique
	GuiInventory thisObject = (GuiInventory)(Object)this;


	@ModifyArg(
		method = "drawGuiContainerBackgroundLayer(F)V",
		at = @At(value = "INVOKE", target = "net/minecraft/client/render/RenderEngine.bindTexture (I)V"),
		index = 0
	)
	private int newTex(int i){
		return Minecraft.getMinecraft(this).renderEngine.getTexture("/assets/dxiimod/textures/gui/inventoryDxii.png");
	}

}
