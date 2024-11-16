package dxii.dxiimod.mixin;


import dxii.dxiimod.dxiimodMain;
import dxii.dxiimod.dxiimodUtils;
import dxii.dxiimod.interfaces.ILivingEntityFunctions;
import dxii.dxiimod.interfaces.INewItemVars;
import dxii.dxiimod.interfaces.IPlayerInventory;
import dxii.dxiimod.interfaces.IPlayerStuff;
import dxii.dxiimod.item.enums.EAccBonus;
import dxii.dxiimod.item.accessory.baseAccessory;
import dxii.dxiimod.mixin.accessors.IAEntityPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.monster.EntityMonster;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.util.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;


@Mixin(value = EntityPlayer.class, remap = false)
public class EntityPlayerMixin implements IPlayerStuff {
	/*
	DODGINGG i guess
	this mixin is all about accessories/rings, speed increase, also weapon animation variants
	enemy attraction, parrying damage increase, RTSR (2x dmg if hp < 4)
	 */

	@Unique
	public EntityPlayer thisObject = (EntityPlayer)(Object)this ;

	@Unique
	public Minecraft mc = Minecraft.getMinecraft(Minecraft.class);

	@Unique
	public int dodgeTimer = 0;

	@Unique
	public int redeyeTimer = 0;

	@Unique
	public int speedModTimer;

	@Unique
	public float lastSpeedModifier = 1;

	@Unique
	public short animVar = 0;

    @Shadow protected float baseSpeed;
    @Shadow protected float baseFlySpeed;
    @Unique public float oldDiffSpeed = 0;
    @Unique public float oldDiffFlySpeed = 0;

	@Override
	public short dxiimod$getAnimVariant(){
		return this.animVar;
	}

	@Override
	public void dxiimod$switchAnimVariant(){
		if(this.animVar == 0) {
			this.animVar = 1;
		}else {
			this.animVar = 0;
		}
	}

	@Override
	public void dxiimod$setSpecialAnimVariant(){
		this.animVar = 3;
	}

	@ModifyArg(
		method ="attackTargetEntityWithCurrentItem(Lnet/minecraft/core/entity/Entity;)V",
		at = @At(value = "INVOKE", target = "net/minecraft/core/entity/Entity.hurt (Lnet/minecraft/core/entity/Entity;ILnet/minecraft/core/util/helper/DamageType;)Z"),
		index = 1
	)
	private int atkDamageMixin(int dmg){
		int newDmg = dmg;

		boolean rtsr = false;
		boolean hornet = false;

		ItemStack[] accInv = ((IPlayerInventory)(thisObject.inventory)).dxiimod$getAccInv();
		if (accInv[0] != null && ((baseAccessory) (accInv[0].getItem())).bonus == EAccBonus.HORNETRING) {
			hornet = true;
		}else if (accInv[1] != null && ((baseAccessory) (accInv[1].getItem())).bonus == EAccBonus.HORNETRING) {
			hornet = true;
		}else if (accInv[2] != null && ((baseAccessory) (accInv[2].getItem())).bonus == EAccBonus.HORNETRING) {
			hornet = true;
		}else if (accInv[3] != null && ((baseAccessory) (accInv[3].getItem())).bonus == EAccBonus.HORNETRING) {
			hornet = true;
		}

		if (accInv[0] != null && ((baseAccessory) (accInv[0].getItem())).bonus == EAccBonus.RTSR) {
			rtsr = true;
		}else if (accInv[1] != null && ((baseAccessory) (accInv[1].getItem())).bonus == EAccBonus.RTSR) {
			rtsr = true;
		}else if (accInv[2] != null && ((baseAccessory) (accInv[2].getItem())).bonus == EAccBonus.RTSR) {
			rtsr = true;
		}else if (accInv[3] != null && ((baseAccessory) (accInv[3].getItem())).bonus == EAccBonus.RTSR) {
			rtsr = true;
		}


		if(rtsr & thisObject.getHealth() <= 4){
			newDmg *= 2;
		}

		if( ((ILivingEntityFunctions)(thisObject)).dxiimod$getParryTicks() > 0 ){
			if(hornet){
				newDmg *= 3;
			}else{
				newDmg *= 2;
			}
		}

		return newDmg;
	}



	@Inject(
		method = "onLivingUpdate()V",
		at = @At(value = "HEAD")
	)
	public void speedMixin(CallbackInfo ci){
		boolean speedy = false;

		ItemStack[] accInv = ((IPlayerInventory)(thisObject.inventory)).dxiimod$getAccInv();
		if (accInv[0] != null && ((baseAccessory) (accInv[0].getItem())).bonus == EAccBonus.CLORANTHY) {
			speedy = true;
		}else if (accInv[1] != null && ((baseAccessory) (accInv[1].getItem())).bonus == EAccBonus.CLORANTHY) {
			speedy = true;
		}else if (accInv[2] != null && ((baseAccessory) (accInv[2].getItem())).bonus == EAccBonus.CLORANTHY) {
			speedy = true;
		}else if (accInv[3] != null && ((baseAccessory) (accInv[3].getItem())).bonus == EAccBonus.CLORANTHY) {
			speedy = true;
		}

		if(this.dodgeTimer == 0 && dxiimodMain.keyDodge.isPressed()){
			float dodgePower = .66f;

			if(!thisObject.onGround){
				dodgePower *= .66f;
			}

			int dodgeTimer = 20;
			float dodgeJump = .0f;

			if(this.mc.gameSettings.keyForward.isPressed() && this.mc.gameSettings.keyLeft.isPressed()
			|| this.mc.gameSettings.keyForward.isPressed() && this.mc.gameSettings.keyRight.isPressed()
			|| this.mc.gameSettings.keyBack.isPressed() && this.mc.gameSettings.keyLeft.isPressed()
			|| this.mc.gameSettings.keyBack.isPressed() && this.mc.gameSettings.keyRight.isPressed()){
				dodgePower /= 1.5f;
			}

			if(
				this.mc.gameSettings.keyForward.isPressed() ||
				this.mc.gameSettings.keyBack.isPressed() ||
				this.mc.gameSettings.keyLeft.isPressed() ||
				this.mc.gameSettings.keyRight.isPressed()){
				thisObject.yd += dodgeJump;
			}

			if(this.mc.gameSettings.keyForward.isPressed()) {
				dxiimodUtils.pushRelative(thisObject, 0, 1, dodgePower);
				this.dodgeTimer = dodgeTimer;
			}
			if(this.mc.gameSettings.keyBack.isPressed()) {
				dxiimodUtils.pushRelative(thisObject, 0, -1, dodgePower);
				this.dodgeTimer = dodgeTimer;
			}
			if(this.mc.gameSettings.keyLeft.isPressed()){
				dxiimodUtils.pushRelative(thisObject, 1, 0, dodgePower);
				this.dodgeTimer = dodgeTimer;
			}
			if(this.mc.gameSettings.keyRight.isPressed()){
				dxiimodUtils.pushRelative(thisObject, -1, 0, dodgePower);
				this.dodgeTimer = dodgeTimer;
			}
		}


		if(this.dodgeTimer != 0) {
			this.dodgeTimer--;
		}

		if(this.speedModTimer != 0) {
			this.speedModTimer--;
		}

		if(thisObject.getHeldItem() != null) {
			Item currentItem = thisObject.getHeldItem().getItem();

			float speedModifier = (float)((INewItemVars) currentItem).dxiimod$getItemSpeedMod();
			if (this.speedModTimer == 0) {
				this.lastSpeedModifier = speedModifier;
			}

			if (speedModifier > 0) {
				this.lastSpeedModifier = speedModifier;
				this.speedModTimer = 3;
			}
		}else if(this.speedModTimer == 0){
			this.lastSpeedModifier = 0;
		}

        this.baseSpeed -= oldDiffSpeed;
        this.baseFlySpeed -= oldDiffFlySpeed;
		if(speedy) {
		    oldDiffSpeed = .075f - this.lastSpeedModifier;
		    oldDiffFlySpeed = .075f - this.lastSpeedModifier*2/10;
		}else{
		    oldDiffSpeed = -this.lastSpeedModifier;
		    oldDiffFlySpeed = -this.lastSpeedModifier*2/10;
		}
	    this.baseSpeed += oldDiffSpeed;
	    this.baseFlySpeed += oldDiffFlySpeed;


	}

	@Inject(
		method = "onLivingUpdate()V",
		at = @At(value = "TAIL")
	)
	public void redEye(CallbackInfo ci){
		if(redeyeTimer != 0) {
			redeyeTimer--;
		}

		boolean redeye = false;

		ItemStack[] accInv = ((IPlayerInventory)(thisObject.inventory)).dxiimod$getAccInv();
		if (accInv[0] != null && ((baseAccessory) (accInv[0].getItem())).bonus == EAccBonus.REDEYE) {
			redeye = true;
		}else if (accInv[1] != null && ((baseAccessory) (accInv[1].getItem())).bonus == EAccBonus.REDEYE) {
			redeye = true;
		}else if (accInv[2] != null && ((baseAccessory) (accInv[2].getItem())).bonus == EAccBonus.REDEYE) {
			redeye = true;
		}else if (accInv[3] != null && ((baseAccessory) (accInv[3].getItem())).bonus == EAccBonus.REDEYE) {
			redeye = true;
		}

		int bound = 32;

		if(redeye){
			thisObject.world.spawnParticle("reddust",
				thisObject.x + thisObject.getLookAngle().xCoord/4 - (double)(MathHelper.cos(thisObject.yRot / 180.0f * (float)Math.PI) * 0.16f),
				thisObject.y + .1 + thisObject.getLookAngle().yCoord/4,
				thisObject.z + thisObject.getLookAngle().zCoord/4 - (double)(MathHelper.sin(thisObject.yRot / 180.0f * (float)Math.PI) * 0.16f),
				1.0, 0.0, 0.0, 0);

		}

		if(redeye & redeyeTimer == 0){
			redeyeTimer = 30;

			AABB aabb1 = new AABB(
				thisObject.x - bound,
				thisObject.y - bound,
				thisObject.z - bound,
				thisObject.x + bound,
				thisObject.y + bound,
				thisObject.z + bound
			);

			List<Entity> entityList = thisObject.world.getEntitiesWithinAABB(EntityMonster.class, aabb1 );
			for (Entity entity : entityList) {
				if( thisObject.canEntityBeSeen( entity) ) {
					((EntityMonster)entity).setTarget(thisObject);
				}
			}
		}

	}

}
