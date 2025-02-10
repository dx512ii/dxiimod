package dxii.dxiimod.mixin;


import dxii.dxiimod.dxiimodUtils;
import dxii.dxiimod.interfaces.IPlayerInventory;
import dxii.dxiimod.item.enums.EAccBonus;
import dxii.dxiimod.item.accessory.baseAccessory;
import net.minecraft.client.render.entity.PlayerRenderer;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//fog ring stuff, makes player almost invisible
@Mixin(value = PlayerRenderer.class, remap = false)
public class PlayerRendererMixin {


	@Inject(
		method = "preRenderCallback(Lnet/minecraft/core/entity/player/EntityPlayer;F)V",
		at = @At(value = "HEAD")
	)
	private void transparencyMixin(EntityPlayer player, float f, CallbackInfo ci){
		if(dxiimodUtils.playerHasAccessoryEffect(player, EAccBonus.INVIS)) {
			GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.1001f);
		}
		else{
			GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		}
	}


//	@ModifyArg(
//		method ="drawFirstPersonHand(Lnet/minecraft/core/entity/player/EntityPlayer;Z)V",
//		at = @At(value = "INVOKE", target = "net/minecraft/client/render/model/Cube.render (F)V", ordinal = 0), index = 0
//	)
//	public float ass(float scale){
//		return 0;
//	}
//
//	@ModifyArg(
//		method ="drawFirstPersonHand(Lnet/minecraft/core/entity/player/EntityPlayer;Z)V",
//		at = @At(value = "INVOKE", target = "net/minecraft/client/render/model/Cube.render (F)V", ordinal = 1), index = 0
//	)
//	public float ass2(float scale){
//		return 0;
//	}


//	@Inject(
//		method = "drawFirstPersonHand(Lnet/minecraft/core/entity/player/EntityPlayer;Z)V",
//		at = @At(value = "HEAD")
//	)
//	private void transparencyMixin2(EntityPlayer player, boolean isLeft, CallbackInfo ci){
//		return;
//	}

//		@ModifyArg(
//		method ="drawFirstPersonHand(Lnet/minecraft/core/entity/player/EntityPlayer;Z)V",
//		at = @At(value = "INVOKE", target = "net/minecraft/client/render/model/ModelBiped.setRotationAngles (FFFFFF)V"), index = 0
//	)
//	private float transparencyMixin2(float limbSwing){
//		return 90;
//	}


}
