package dxii.dxiimod.mixin;


import dxii.dxiimod.interfaces.IWorldVariables;
import net.minecraft.client.Minecraft;
import net.minecraft.client.option.enums.RenderDistance;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.core.util.helper.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import dxii.dxiimod.dxiimodMain;
import org.spongepowered.asm.mixin.injection.Redirect;


//first mixin i managed to make, adds harmless fog
@Mixin(value = WorldRenderer.class, remap = false)
public class FogMixin {
	@Redirect(
		method ="setupCameraTransform(F)V",
		at = @At(value = "FIELD", target = "net/minecraft/client/option/enums/RenderDistance.chunks : I")
	)
	private int mixinVar(RenderDistance instance){
		if(Minecraft.getMinecraft(Minecraft.class).theWorld != null && dxii.dxiimod.dxiimodMain.THE_FOG == 1) {
			double fogDay = ((IWorldVariables)(Minecraft.getMinecraft(Minecraft.class).theWorld.getLevelData() )).dxiimod$getFogDay();

			if(((IWorldVariables)(Minecraft.getMinecraft(Minecraft.class).theWorld.getLevelData() )).dxiimod$getFog()) {
				if (Minecraft.getMinecraft(Minecraft.class).theWorld.getLevelData().getWorldTime() / 24000f > (fogDay + 1.35)) {
					return MathHelper.clamp(dxiimodMain.FogDist.value, 1, 6);
				} else if (Minecraft.getMinecraft(Minecraft.class).theWorld.getLevelData().getWorldTime() / 24000f > (fogDay + 1.2)) {
					return MathHelper.clamp(dxiimodMain.FogDist.value, 4, 6);
				} else if (Minecraft.getMinecraft(Minecraft.class).theWorld.getLevelData().getWorldTime() / 24000f > (fogDay + 1)) {
					return 8;
				} else if (Minecraft.getMinecraft(Minecraft.class).theWorld.getLevelData().getWorldTime() / 24000f > (fogDay + .7)) {
					return 16;
				} else {
					return 24;
				}
			}
		}
		return 24;
	}


}
