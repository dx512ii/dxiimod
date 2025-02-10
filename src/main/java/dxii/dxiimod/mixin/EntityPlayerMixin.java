package dxii.dxiimod.mixin;


import dxii.dxiimod.dxiimodMain;
import dxii.dxiimod.dxiimodUtils;
import dxii.dxiimod.interfaces.ILivingEntityFunctions;
import dxii.dxiimod.interfaces.INewItemVars;
import dxii.dxiimod.interfaces.IPlayerStuff;
import dxii.dxiimod.item.enums.EAccBonus;
import dxii.dxiimod.mixin.accessors.IAEntityPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.entity.monster.EntityMonster;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.player.gamemode.Gamemode;
import net.minecraft.core.sound.SoundCategory;
import net.minecraft.core.util.helper.DamageType;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.util.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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
	public int dodgeDelay = 20;

	@Unique
	public int dodgeSoundTimer = 0;

	@Unique
	public int redeyeTimer = 0;

	@Unique
	public int speedModTimer;

	@Unique
	public float lastSpeedModifier = 1;

	@Unique
	public short animVar = 0;

	@Unique
	public float oldBBWidth = thisObject.bbWidth;

	@Unique
	public float oldBBHeight = thisObject.bbHeight;

	@Unique
	public DamageType lastDMGType;

	@Unique
	public EntityLiving lastAtkEntity;

	@Unique
	public int lastDMG;

	@Unique
	public int feralCurse = 0;

	@Unique
	public int feralCurseSatiety = 0;



	@Override
	public short dxiimod$VMgetAnimVariant(){
		return this.animVar;
	}

	@Override
	public void dxiimod$VMswitchAnimVariant(){
		if(this.animVar == 0) {
			this.animVar = 1;
		}else {
			this.animVar = 0;
		}
	}

	@Override
	public void dxiimod$VMsetSpecialAnimVariant(){
		this.animVar = 3;
	}



	@Inject(
		method ="attackTargetEntityWithCurrentItem(Lnet/minecraft/core/entity/Entity;)V",
		at = @At(value = "HEAD")
	)
	public void getAtkLocals(Entity entity, CallbackInfo ci){
		this.lastAtkEntity = (EntityLiving)entity;
	}

	@ModifyArg(
		method ="attackTargetEntityWithCurrentItem(Lnet/minecraft/core/entity/Entity;)V",
		at = @At(value = "INVOKE", target = "net/minecraft/core/entity/Entity.hurt (Lnet/minecraft/core/entity/Entity;ILnet/minecraft/core/util/helper/DamageType;)Z"),
		index = 1
	)
	private int atkDamageMixin(int dmg){
		int newDmg = dmg;

		boolean rtsr = dxiimodUtils.playerHasAccessoryEffect(thisObject, EAccBonus.RTSR);;
		boolean hornet = dxiimodUtils.playerHasAccessoryEffect(thisObject, EAccBonus.HORNETRING);;


		if(rtsr & thisObject.getHealth() <= 4){
			newDmg *= 2;
		}

		if( ((ILivingEntityFunctions)(lastAtkEntity)).dxiimod$getParriedTicks() > 0 ){
			if(hornet){
				newDmg *= 3;
			}else{
				newDmg *= 2;
			}
		}

		return newDmg;
	}

	@Redirect(
		method = "push(Lnet/minecraft/core/entity/Entity;)V",
		at = @At(value = "FIELD", target = "net/minecraft/core/entity/player/EntityPlayer.noPhysics : Z")
	)
	public boolean canBePushed(EntityPlayer player){
		return thisObject.noPhysics || this.dodgeTimer >= dodgeDelay - 5;
	}

	@Inject(
		method = "hurt(Lnet/minecraft/core/entity/Entity;ILnet/minecraft/core/util/helper/DamageType;)Z",
		at = @At(value = "HEAD")
	)
	public void getLastDmgType(Entity attacker, int damage, DamageType type, CallbackInfoReturnable<Boolean> cir){
		this.lastDMGType = type;
		this.lastDMG = damage;
	}

	@Redirect(
		method = "hurt(Lnet/minecraft/core/entity/Entity;ILnet/minecraft/core/util/helper/DamageType;)Z",
		at = @At(value = "INVOKE", target = "net/minecraft/core/player/gamemode/Gamemode.isPlayerInvulnerable ()Z")
	)
	public boolean dodgeImmunity(Gamemode instance){
		if(this.dodgeSoundTimer == 0 && this.dodgeTimer >= (dodgeDelay - 3) && (lastDMGType == DamageType.COMBAT || lastDMGType == DamageType.BLAST || lastDMGType == DamageType.FALL ) ){
			this.dodgeSoundTimer = 3;
			thisObject.world.playSoundEffect(thisObject, SoundCategory.ENTITY_SOUNDS, thisObject.x, thisObject.y, thisObject.z, "dxiimod.dodge", MathHelper.clamp(this.lastDMG/100f, 0.02f, 0.3f), (float)(1 - Math.random()/5 ) );
		}

		return thisObject.gamemode.isPlayerInvulnerable() || (this.dodgeTimer >= (dodgeDelay - 3) && this.lastDMG < thisObject.getHealth() && (lastDMGType == DamageType.COMBAT || lastDMGType == DamageType.BLAST || lastDMGType == DamageType.FALL ) );
	}

	@Inject(
		method = "onLivingUpdate()V",
		at = @At(value = "HEAD")
	)
	public void tickMixin(CallbackInfo ci){
		//hungry curse yum yum
		if( dxiimodUtils.playerHasAccessoryEffect(thisObject, EAccBonus.FERALBONE) ){
			this.feralCurse = 24000;
			if(this.feralCurseSatiety == 0){
				this.feralCurseSatiety = 2400;
			}
		}else if(this.feralCurse > 0){
			System.out.println("remaining curse: " + this.feralCurse);
			--this.feralCurse;
		}
		if(this.feralCurseSatiety > 0){
			System.out.println("remaining curse satiety: " + this.feralCurseSatiety);
			--this.feralCurseSatiety;
		}
		//tummy is hungry(((
		if(this.feralCurse > 0 && this.feralCurseSatiety == 0){
			thisObject.hurt(null, 1, DamageType.GENERIC);
			this.feralCurseSatiety = 720;
		}


		if(this.dodgeTimer >= this.dodgeDelay * 0.75){
			thisObject.bbWidth = .01f;
		}else{
			thisObject.bbWidth = this.oldBBWidth;
		}

		boolean speedy = dxiimodUtils.playerHasAccessoryEffect(thisObject, EAccBonus.CLORANTHY);
		boolean parrying = ((ILivingEntityFunctions)thisObject).dxiimod$getParryTicks() > 0;

		if(!parrying && this.dodgeTimer == 0 && dxiimodMain.keyDodge.isPressed()){
			float dodgePower = .75f;

			if(!thisObject.onGround){
				dodgePower *= .66f;
			}

			float dodgeJump = .0f;

			if(this.mc.gameSettings.keyForward.isPressed() && this.mc.gameSettings.keyLeft.isPressed()
			|| this.mc.gameSettings.keyForward.isPressed() && this.mc.gameSettings.keyRight.isPressed()
			|| this.mc.gameSettings.keyBack.isPressed() && this.mc.gameSettings.keyLeft.isPressed()
			|| this.mc.gameSettings.keyBack.isPressed() && this.mc.gameSettings.keyRight.isPressed()){
				dodgePower *= .66f;
			}

			if(
				this.mc.gameSettings.keyForward.isPressed() ||
				this.mc.gameSettings.keyBack.isPressed() ||
				this.mc.gameSettings.keyLeft.isPressed() ||
				this.mc.gameSettings.keyRight.isPressed()){
				thisObject.yd += dodgeJump;
			}

			if(this.mc.gameSettings.keyForward.isPressed()) {
				dxiimodUtils.setVelRelative(thisObject, 0, 1, dodgePower);
				this.dodgeTimer = this.dodgeDelay;
			}
			if(this.mc.gameSettings.keyBack.isPressed()) {
				dxiimodUtils.setVelRelative(thisObject, 0, -1, dodgePower);
				this.dodgeTimer = this.dodgeDelay;
			}
			if(this.mc.gameSettings.keyLeft.isPressed()){
				dxiimodUtils.setVelRelative(thisObject, 1, 0, dodgePower);
				this.dodgeTimer = this.dodgeDelay;
			}
			if(this.mc.gameSettings.keyRight.isPressed()){
				dxiimodUtils.setVelRelative(thisObject, -1, 0, dodgePower);
				this.dodgeTimer = this.dodgeDelay;
			}

		}

		if(this.dodgeSoundTimer != 0) {
			this.dodgeSoundTimer--;
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

		if(speedy) {
			((IAEntityPlayer) thisObject).setBaseSpeed(parrying ? 0.01f : .125f - this.lastSpeedModifier);
			((IAEntityPlayer) thisObject).setBaseFlySpeed(.025f - this.lastSpeedModifier*2/10);
		}else{
			((IAEntityPlayer) thisObject).setBaseSpeed(parrying ? 0.01f : .1f - this.lastSpeedModifier);
			((IAEntityPlayer) thisObject).setBaseFlySpeed(.02f - this.lastSpeedModifier*2/10);
		}


	}

	@Inject(
		method = "onLivingUpdate()V",
		at = @At(value = "TAIL")
	)
	public void redEye(CallbackInfo ci){
		if(redeyeTimer != 0) {
			redeyeTimer--;
		}

		boolean redeye = dxiimodUtils.playerHasAccessoryEffect(thisObject, EAccBonus.REDEYE);

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

	/**
	 * @author aa
	 * @reason ss
	 */
	@Overwrite
	public void updatePlayerActionState() {
		if (thisObject.isSwinging) {
			++thisObject.swingProgressInt;
			if (thisObject.swingProgressInt >= 10) {
				thisObject.swingProgressInt = 0;
				thisObject.isSwinging = false;
			}
		} else {
			thisObject.swingProgressInt = 0;
		}
		thisObject.swingProgress = (float)thisObject.swingProgressInt / 10;
	}

}
