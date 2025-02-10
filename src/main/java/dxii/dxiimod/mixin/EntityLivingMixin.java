package dxii.dxiimod.mixin;



import dxii.dxiimod.dxiimodMain;
import dxii.dxiimod.dxiimodUtils;
import dxii.dxiimod.interfaces.ILivingEntityFunctions;
import dxii.dxiimod.item.enums.EAccBonus;
import dxii.dxiimod.mixin.accessors.IAEntity;
import dxii.dxiimod.mixin.accessors.IAEntityLiving;
import net.minecraft.client.Minecraft;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.entity.animal.EntityAnimal;
import net.minecraft.core.entity.monster.EntityMonster;
import net.minecraft.core.entity.player.EntityPlayer;
import dxii.dxiimod.dxiimodItems;
import net.minecraft.core.sound.SoundCategory;
import net.minecraft.core.util.helper.DamageType;
import net.minecraft.core.util.helper.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


import static net.minecraft.core.util.helper.MathHelper.clamp;

//this one is huge..
@Mixin(value = EntityLiving.class, remap = false)
public class EntityLivingMixin implements ILivingEntityFunctions {

	@Shadow
	protected float moveSpeed;

	@Shadow
	protected boolean isJumping;

	@Unique
	public float oldMoveSpeed;

	@Unique
	public int ticksAboveGround;

	@Unique
	public int moanTimer;

	@Unique
	public boolean doubleJumping;

	@Unique
	public boolean canDoubleJump;

	@Unique
	public EntityLiving thisObject = (EntityLiving)(Object)this ;

	@Unique
	public int parryTicks;

	@Unique
	public int lastParryTicks;

	@Unique
	public int lastParryDelay = -1;
	@Unique
	public boolean lastParryAccurate;

	@Unique
	public int parriedTicks = 0;

	@Unique
	public int extraJumps = 0;

	@Unique
	public int doublejumpTimer = 0;

	@Unique
	public int evilEyeCounter = 0;

	@Unique
	public final Minecraft mc = Minecraft.getMinecraft(Minecraft.class);

	@Unique
	public Entity currentParryAttacker;

	@Unique
	public int currentHurtDamage;

	@Unique
	public int getInventorySlotContainItemDX(EntityPlayer ply, int i) {
		for (int j = 0; j < ply.inventory.mainInventory.length; ++j) {
			if (ply.inventory.mainInventory[j] == null || ply.inventory.mainInventory[j].itemID != i) continue;
			return j;
		}
		return -1;
	}

	public boolean dxiimod$getIsJumping(){
		return this.isJumping;
	}

	public void dxiimod$evileyeheal(){
		evilEyeCounter++;
		if(evilEyeCounter == 2){
			thisObject.heal(1);
			evilEyeCounter = 0;
		}
	}

	/**
	 * @author three things here are protected
	 * @reason PROTECTED FROM WHO?????? this encapsulation crap drives me insane because i need to write 999th accessor mixin, for the love of god make all stuff public i beg you
	 */

	/*
	this was made just to stop mobs from spamming hurt sound, if their iframes are reset
	but for some reason almost all vars/methods here were made private/protected
	 */
	@Overwrite
	public void playHurtSound() {
		if(this.moanTimer == 0) {
			thisObject.world.playSoundAtEntity(null, thisObject, ((IAEntityLiving) thisObject).getHurtSoundNew(), ((IAEntityLiving) thisObject).getSoundVolNew(), (((IAEntity) thisObject).randomm().nextFloat() - ((IAEntity) thisObject).randomm().nextFloat()) * 0.2f + 1.0f);
			this.moanTimer = 5;
		}
	}

	//parrying stuff
	//adds parrying ticks to an entity
	@Override
	public void dxiimod$Parry(int parryTicks, int delay, boolean accurate){
		this.parryTicks = parryTicks + delay;
		this.lastParryTicks = parryTicks + delay;
		this.lastParryDelay = delay;
		this.lastParryAccurate = accurate;

		if(thisObject instanceof EntityPlayer) {
			((EntityPlayer)thisObject).swingItem();
			thisObject.world.playSoundAtEntity(thisObject, thisObject, "dxiimod.shield_swing", 0.3f, 1.0f);
		}


	}

	//previously stunned an enemy on successful parry, will still do tho but for new enemies
	@Override
	public void dxiimod$parryStun(Entity attacker, int damage){
		if(thisObject instanceof EntityPlayer) {
			this.parriedTicks = 50;
		}else{
			this.parriedTicks = 24;
		}

		double d = clamp(attacker.x - thisObject.x, -1, 1);
		double d1 = clamp(attacker.z - thisObject.z, -1, 1);
		attacker.push(d*4 , .3, d1*4);

		if(damage  >= 20){ //play cooler sound if the damage is nutzzzz
			attacker.world.playSoundEffect(
				attacker,
				SoundCategory.ENTITY_SOUNDS,
				attacker.x,
				attacker.y,
				attacker.z,
				"dxiimod.ds2_parry",
				0.75f,
				1f);
		}else{
			attacker.world.playSoundEffect(
				attacker,
				SoundCategory.ENTITY_SOUNDS,
				attacker.x,
				attacker.y,
				attacker.z,
				"dxiimod.parry",
				1f,
				1f);
		}

	}

	//getters for parry and parried ticks
	@Override
	public int dxiimod$getParryTicks(){
		return parryTicks;
	}
	@Override
	public int dxiimod$getParriedTicks(){
		return parriedTicks;
	}

	//arent used atm
	@Inject(
		method = "hurt(Lnet/minecraft/core/entity/Entity;ILnet/minecraft/core/util/helper/DamageType;)Z",
		at = @At(value = "HEAD")
	)
	public void captureHurtLocals(Entity attacker, int damage, DamageType type, CallbackInfoReturnable<Boolean> cir){
		this.currentParryAttacker = attacker;
		this.currentHurtDamage = damage;
	}


	//stuns entity that is to be parried, damages parrying shield item, plays gorgeous sound
	@Inject(
		method = "hurt(Lnet/minecraft/core/entity/Entity;ILnet/minecraft/core/util/helper/DamageType;)Z",
		at = @At(value = "HEAD"),
		cancellable = true)
	public void parryStunOnHurt(Entity attacker, int damage, DamageType type, CallbackInfoReturnable<Boolean> cir){
		if(attacker != null) {

			if (dxiimodUtils.isEntityInFront((EntityLiving)attacker, thisObject) && type == DamageType.COMBAT && (this.parryTicks > 0 && this.parryTicks <= this.lastParryTicks - this.lastParryDelay) && thisObject.distanceTo(attacker) <= 3) {
				if(thisObject instanceof EntityPlayer) {
					int bucklerId = getInventorySlotContainItemDX(((EntityPlayer) thisObject), dxiimodItems.shieldBuckler.id);
					((EntityPlayer) thisObject).inventory.mainInventory[bucklerId].damageItem(1, thisObject);
				}

				((ILivingEntityFunctions) (attacker)).dxiimod$parryStun(attacker, damage);

				cir.setReturnValue(false);
			}else{
				if(attacker instanceof EntityPlayer) {
					System.out.println("Ouch!! you hit me with " + damage + " damage!");
				}
			}

			if (this.parriedTicks > 0) {
				attacker.world.playSoundEffect(attacker, SoundCategory.ENTITY_SOUNDS, attacker.x, attacker.y, attacker.z, "dxiimod.riposte", .35f, 1f);
			}
		}
	}


	//used mostly for timers and timed events, and also double jump
	@Inject(
		method = "tick()V",
		at = @At(value = "INVOKE", target = "net/minecraft/core/entity/EntityLiving.onLivingUpdate ()V")
	)
	public void tickMixin(CallbackInfo ci){
		if(this.parryTicks == this.lastParryTicks - this.lastParryDelay & thisObject instanceof EntityPlayer){
			dxiimodUtils.parryHitbox(thisObject, this.lastParryAccurate);
		}

		if(this.oldMoveSpeed == 0){
			this.oldMoveSpeed = this.moveSpeed;
		}

		if(this.parryTicks != 0) {
			this.parryTicks--;
		}

		if(this.parriedTicks != 0){
			this.parriedTicks--;
		}
		if(this.parriedTicks < 0){
			this.parriedTicks = 0;
		}

		if(this.doublejumpTimer != 0){
			this.doublejumpTimer--;
		}


		//i cant really remember whats the purpose of this block but anyways, it works1!!!
		if(!thisObject.onGround & !this.doubleJumping & this.canDoubleJump){
			this.extraJumps = 1;
			this.doubleJumping = true;
		}

		if(thisObject.onGround){
			this.canDoubleJump = true;
			this.ticksAboveGround = 0;
		}

		if(!thisObject.onGround){
			this.ticksAboveGround ++;
		}





		if(this.moanTimer != 0){
			this.moanTimer--;
		}
	}



	//double jumping & frogg leg stuff



	@Unique
	boolean bigJump = false;

	@Inject(
		method = "onLivingUpdate()V",
		at = @At(value = "INVOKE", target = "net/minecraft/core/entity/EntityLiving.isInLava ()Z"))
	public void doubleJump2(CallbackInfo ci){

	//double jumping and frog leg accessory checks
		if(thisObject instanceof EntityPlayer) {
			boolean FF = dxiimodUtils.playerHasAccessoryEffect((EntityPlayer) thisObject, EAccBonus.FEATHERFALL);;
			boolean DJ = dxiimodUtils.playerHasAccessoryEffect((EntityPlayer) thisObject, EAccBonus.DOUBLEJUMP);
			double jumpMul = 1;
			bigJump = dxiimodUtils.playerHasAccessoryEffect((EntityPlayer) thisObject, EAccBonus.FROGLEG);

			if(bigJump){
				jumpMul = 1.5;
			}

			if (this.isJumping && this.extraJumps > 0 && ticksAboveGround >= 5 && DJ) {
				thisObject.fallDistance = 0;
				this.extraJumps--;

				thisObject.yd = .42 * jumpMul;

				float jumpPush = (float)jumpMul/8;

				if(this.mc.gameSettings.keyForward.isPressed() && this.mc.gameSettings.keyLeft.isPressed()
					|| this.mc.gameSettings.keyForward.isPressed() && this.mc.gameSettings.keyRight.isPressed()
					|| this.mc.gameSettings.keyBack.isPressed() && this.mc.gameSettings.keyLeft.isPressed()
					|| this.mc.gameSettings.keyBack.isPressed() && this.mc.gameSettings.keyRight.isPressed()){
					jumpPush /= 1.5f;
				}

				if(this.mc.gameSettings.keyForward.isPressed()) {
					dxiimodUtils.pushRelative(thisObject, 0, 1, jumpPush);
				}
				if(this.mc.gameSettings.keyBack.isPressed()) {
					dxiimodUtils.pushRelative(thisObject, 0, -1, jumpPush);
				}
				if(this.mc.gameSettings.keyLeft.isPressed()){
					dxiimodUtils.pushRelative(thisObject, 1, 0, jumpPush);
				}
				if(this.mc.gameSettings.keyRight.isPressed()){
					dxiimodUtils.pushRelative(thisObject, -1, 0, jumpPush);
				}

				this.doubleJumping = false;
				this.canDoubleJump = false;

				thisObject.world.spawnParticle("snowshovel", thisObject.x, thisObject.y - 1, thisObject.z, 0.0, 0.0, 0.0, 0);
				thisObject.world.spawnParticle("snowshovel", thisObject.x + Math.random(), thisObject.y - 1 + Math.random()*.1, thisObject.z + Math.random(), 0.0, 0.0, 0.0, 0);
				thisObject.world.spawnParticle("snowshovel", thisObject.x - Math.random(), thisObject.y - 1 + Math.random()*.1, thisObject.z - Math.random(), 0.0, 0.0, 0.0, 0);
				thisObject.world.spawnParticle("snowshovel", thisObject.x - Math.random(), thisObject.y - 1 + Math.random()*.1, thisObject.z + Math.random(), 0.0, 0.0, 0.0, 0);
				thisObject.world.spawnParticle("snowshovel", thisObject.x + Math.random(), thisObject.y - 1 + Math.random()*.1, thisObject.z - Math.random(), 0.0, 0.0, 0.0, 0);
				thisObject.world.spawnParticle("snowshovel", thisObject.x + Math.random(), thisObject.y - 1 + Math.random()*.1, thisObject.z + Math.random(), 0.0, 0.0, 0.0, 0);
				thisObject.world.spawnParticle("snowshovel", thisObject.x - Math.random(), thisObject.y - 1 + Math.random()*.1, thisObject.z - Math.random(), 0.0, 0.0, 0.0, 0);
				thisObject.world.spawnParticle("snowshovel", thisObject.x - Math.random(), thisObject.y - 1 + Math.random()*.1, thisObject.z + Math.random(), 0.0, 0.0, 0.0, 0);
				thisObject.world.spawnParticle("snowshovel", thisObject.x + Math.random(), thisObject.y - 1 + Math.random()*.1, thisObject.z - Math.random(), 0.0, 0.0, 0.0, 0);
			}

			if(FF){
				thisObject.yd = MathHelper.clamp(thisObject.yd, -.3, 99);
				thisObject.fallDistance = 0;
			}
		}
	}


	//fall damage protection provided by frog leg
	@ModifyArg(
		method = "causeFallDamage(F)V",
		at = @At(value = "INVOKE", target = "java/lang/Math.ceil (D)D"))
	public double frogLegLessFallDMG(double a){
		if(bigJump) {
			return a - 2;
		}
		else{
			return a;
		}
	}


	//overwritten cuz idk
	/**
	 * @author a
	 * @reason ss
	 */
	@Overwrite
	public void jump() {
		if (thisObject.noPhysics) {
			return;
		}
		if(bigJump) {
			thisObject.yd = 0.6;
		}else{
			thisObject.yd = 0.42;
		}

		if (thisObject.isSprinting()) {
			float f = thisObject.yRot * 0.01745329f;
			thisObject.xd -= (MathHelper.sin(f) * 0.2);
			thisObject.zd += (MathHelper.cos(f) * 0.2);
		}
	}



	// evil eye ring and souls drops from mobs (everything that extends EntityAnimal or EntityMonster)
	@Inject(
		method = "onDeath(Lnet/minecraft/core/entity/Entity;)V",
		at = @At(value = "TAIL"))
	public void evilEyeAndSouls(Entity entityKilledBy, CallbackInfo ci) {
		if(entityKilledBy instanceof EntityPlayer && dxiimodMain.PLY_SOULS) {
			if (thisObject instanceof EntityMonster) {
				if (Math.random() <= .5) {
					thisObject.spawnAtLocation(dxiimodItems.soulevil.id, 1);
				}
			}

			if (thisObject instanceof EntityAnimal) {
				if (Math.random() <= .5) {
					thisObject.spawnAtLocation(dxiimodItems.soulpeace.id, 1);
				}
			}
		}

		//evil eye stuff
		if(entityKilledBy instanceof EntityPlayer){
			if (dxiimodUtils.playerHasAccessoryEffect((EntityPlayer)entityKilledBy, EAccBonus.EVILEYE)) {
				((ILivingEntityFunctions) entityKilledBy).dxiimod$evileyeheal();
			}
		}
	}

}
