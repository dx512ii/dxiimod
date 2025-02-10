package dxii.dxiimod.render;

import dxii.dxiimod.interfaces.IPlayerControllerStuff;
import dxii.dxiimod.interfaces.IPlayerStuff;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.EntityPlayerSP;
import net.minecraft.client.player.controller.PlayerController;
import net.minecraft.client.render.ItemRenderer;
import net.minecraft.client.render.LightmapHelper;
import net.minecraft.client.render.item.model.ItemModelStandard;
import net.minecraft.client.render.stitcher.TextureRegistry;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.MathHelper;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

public class itemModelBroadSword extends ItemModelStandard {


	final Minecraft mc = Minecraft.getMinecraft(Minecraft.class);

	public itemModelBroadSword(Item item, String namespace) {
		super(item, namespace);
		if (namespace != null) {
			this.icon = TextureRegistry.getTexture(namespace + ":item/" + item.getKey().substring("item.".length()).replace(".", "_"));
		}
	}

	@Override
	public void renderItemFirstPerson(Tessellator tessellator, ItemRenderer renderer, EntityPlayer player, ItemStack stack, float partialTick) {
		float brightness = 1.0f;
		if (this.mc.fullbright || this.itemfullBright || LightmapHelper.isLightmapEnabled()) {
			if (LightmapHelper.isLightmapEnabled()) {
				int lightmapCoord = player.getLightmapCoord(partialTick);
				if (this.itemfullBright) {
					lightmapCoord = LightmapHelper.setBlocklightValue(lightmapCoord, 15);
				}
				LightmapHelper.setLightmapCoord(lightmapCoord);
			}
		} else {
			brightness = player.getBrightness(1.0f);
		}

		int attackDelay = 0;

		if(player instanceof EntityPlayerSP){
			PlayerController pc = Minecraft.getMinecraft(Minecraft.class).playerController;
			attackDelay = ((IPlayerControllerStuff)pc).dxiimod$getAttackDelay();
		}

		short animVariant = ((IPlayerStuff)player).dxiimod$VMgetAnimVariant();

		float scale = .5f;
		GL11.glScalef(scale*.6f, scale, scale);

		float swingProgress = player.getSwingProgress(partialTick);
		float animationProgress2 = MathHelper.sin(swingProgress * (float)Math.PI);
		float animationProgress = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float)Math.PI);

		float animationProgress3 = MathHelper.sin(swingProgress * swingProgress * (float)Math.PI);

//		GL11.glTranslatef(-2f, -.2f, -.5f);

		if(animVariant == 0) {//right swing
			float swingProgress2 = 0;
			if(swingProgress > 0){
				swingProgress2 = 1 - swingProgress;
			}else{
				swingProgress2 = 0;
			}

			GL11.glTranslatef(animationProgress * 12.0f - 15*swingProgress2, MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float) Math.PI * 2.0f) * -0.0f - 1, -animationProgress*.66f - .4f);
			GL11.glTranslatef(3f, -0.52f - (1.0f - renderer.getEquippedProgress(partialTick)) * 0.6f - (float) attackDelay / 25, -2 - animationProgress2);

			GL11.glEnable(32826);
			GL11.glRotatef(-animationProgress * 120, 1.0f, 0.0f, 0.0f);
			GL11.glRotatef(animationProgress * 120, 0.0f, -1.0f, 0.0f);
			GL11.glRotatef(-animationProgress2 * 90.0f, 1.0f, 0.0f, 0.0f);
			GL11.glRotatef(50, 0.0f, 1.0f, 0.0f);
		}else if(animVariant ==3){//alt attack
			GL11.glTranslatef(animationProgress*-3, MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float)Math.PI * 2.0f) * -0.4f - 1, animationProgress*-1 -.4f);
			GL11.glTranslatef(3f, -0.52f - (1.0f - renderer.getEquippedProgress(partialTick)) * 0.6f - (float) attackDelay / 25, -2-animationProgress2);

			GL11.glEnable(32826);


			GL11.glRotatef(animationProgress * 90, 0.0f, 0f, 1f);
			GL11.glRotatef(animationProgress*-100, 1.0f, -0.0f, 0.0f);
			GL11.glRotatef(animationProgress2 * -10, 1.0f, .0f, 1.0f);
			GL11.glRotatef(50, 0.0f, 1.0f, 0.0f);
		}else{//left swing
			GL11.glTranslatef(-animationProgress * 5.9f, MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float)Math.PI * 2.0f) * 0.2f - 1, animationProgress3 -.4f);
			GL11.glTranslatef(3f, -0.52f - (1.0f - renderer.getEquippedProgress(partialTick)) * 0.6f - (float) attackDelay / 25, -2-animationProgress2);

			GL11.glEnable(32826);
			GL11.glRotatef(-animationProgress*75, 1.0f, 0.0f, 0.0f);
			GL11.glRotatef(animationProgress * 160, 0.0f, 1.0f, 0.0f);
			GL11.glRotatef(-animationProgress2 * 100.0f, 1.0f, 0.0f, 0.0f);
			GL11.glRotatef(-animationProgress*60, 0.0f, -0.0f, 1.0f);
			GL11.glRotatef(50, 0.0f, 1.0f, 0.0f);
		}

		this.heldTransformFirstPerson(renderer, player, stack);
		this.renderItem(tessellator, renderer, stack, player, brightness, true);
	}

	@Override
	public void renderItem(Tessellator tessellator, ItemRenderer renderer, ItemStack itemstack, @Nullable Entity entity, float brightness, boolean handheldTransform) {
		if (this.itemfullBright || LightmapHelper.isLightmapEnabled()) {
			brightness = 1.0f;
		}
		if (handheldTransform) {
			GL11.glTranslatef(0.1f, -0.3f, 0.01f);
			float handheldScale = 1.66f;
			GL11.glScalef(handheldScale, handheldScale, handheldScale);
			GL11.glRotatef(50.0f, 0.0f, 1.0f, 0.0f);
			GL11.glRotatef(335.0f, 0.0f, 0.0f, 1.0f);
			GL11.glTranslatef(-0.9375f, -0.0625f, 0.0f);
			GL11.glScalef(1f, 1, .75f);
		}
		this.renderItemInWorld(tessellator, entity, itemstack, brightness, 1.0f, false);
	}

}


