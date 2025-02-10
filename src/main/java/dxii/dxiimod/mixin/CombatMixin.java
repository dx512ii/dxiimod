package dxii.dxiimod.mixin;

import dxii.dxiimod.dxiimodMain;
import dxii.dxiimod.interfaces.INewItemFunctions;
import dxii.dxiimod.interfaces.INewItemVars;
import dxii.dxiimod.interfaces.IPlayerControllerStuff;
import dxii.dxiimod.interfaces.IPlayerStuff;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.controller.PlayerController;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.gamemode.Gamemode;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


//cant really comment much about this mixin... its one of my first and i dont really know how it works, i just... made it..
@Mixin(value = PlayerController.class, remap = false)
public class CombatMixin implements IPlayerControllerStuff {

	@Final
	@Shadow
	protected Minecraft mc;


	@Shadow
	protected int swingCooldown = 0;

	@Unique
	public int attackCooldown = 0;

	@Unique
	public int lastAttackCooldown = 0;
	@Unique
	public int lastAltAttackCooldown = 0;

	@Unique
	public int usageCooldown = 0;

	@Unique
	public int lastUsageCooldown = 0;

	/**
	 * @author me
	 * @reason idk
	 */
	@Overwrite
	public float getBlockReachDistance(){
		if(this.mc.thePlayer.getHeldItem() != null){
			Item currentItem = this.mc.thePlayer.getHeldItem().getItem();
			if(this.mc.thePlayer.getGamemode() == Gamemode.creative){
				return 4.5f;
			}else {
				return (float)(((INewItemVars) currentItem).dxiimod$getItemRange());
			}
		}else{
			return 3f;
		}

		//range * 0.875 = blocks range
	}

	/**
	 * @author me
	 * @reason idk (added these just to make it stop annoying me with piss yellow things, cyka)
	 */
	@Overwrite
	public float getEntityReachDistance(){
		if(this.mc.thePlayer.getHeldItem() != null){
			Item currentItem = this.mc.thePlayer.getHeldItem().getItem();
			return (float)((INewItemVars) currentItem).dxiimod$getItemRange();
		}else {
			return 4f;
		}
	}

	/**
	 * @author can you
	 * @reason stop please
	 */
	@Overwrite
	public boolean useItem(EntityPlayer player, World world, ItemStack stack) {
		if(this.attackCooldown == 0 && ((INewItemFunctions) stack.getItem()).dxiimod$onItemAltAttackTimed(player, stack, false)){
			this.attackCooldown = ((INewItemVars) stack.getItem()).dxiimod$getItemCooldown();
			this.lastAltAttackCooldown = ((INewItemVars) stack.getItem()).dxiimod$getItemCooldown();
			this.lastAttackCooldown = 0;
		}

		if (!player.getGamemode().canInteract() | (this.usageCooldown != 0 & ((INewItemVars) stack.getItem()).dxiimod$getItemUsageCooldown() > 0) ) {
			return false;
		}
		int prevStackSize = stack.stackSize;
		ItemStack newItemStack = stack.useItemRightClick(world, player);
		if(((INewItemVars) stack.getItem()).dxiimod$getItemUsageCooldown() > 0){
			this.usageCooldown = ((INewItemVars) stack.getItem()).dxiimod$getItemUsageCooldown();
			this.lastUsageCooldown = ((INewItemVars) stack.getItem()).dxiimod$getItemUsageCooldown();
		}
		if (newItemStack != stack || newItemStack.stackSize != prevStackSize) {
			player.inventory.mainInventory[player.inventory.currentItem] = newItemStack;
			if (newItemStack == null || newItemStack.stackSize <= 0) {
				player.inventory.mainInventory[player.inventory.currentItem] = null;
			}
			return true;
		}
		return false;
	}

	@Unique
	public boolean onCurrentItemAttack(){
		if(this.attackCooldown == 0) {
			if(this.mc.thePlayer.getHeldItem() != null) {
				Item currentItem = this.mc.thePlayer.getHeldItem().getItem();
				if (((INewItemFunctions) currentItem).dxiimod$onItemAttack(this.mc.thePlayer, this.mc.thePlayer.getHeldItem(), false)) {
					((IPlayerStuff)this.mc.thePlayer).dxiimod$VMswitchAnimVariant();
					return true;
				} else {
					this.swingCooldown = ((INewItemVars) currentItem).dxiimod$getItemCooldown();
					this.attackCooldown = ((INewItemVars) currentItem).dxiimod$getItemCooldown();
					this.lastAttackCooldown = ((INewItemVars) currentItem).dxiimod$getItemCooldown();
					this.lastAltAttackCooldown = 0;
					((IPlayerStuff)this.mc.thePlayer).dxiimod$VMswitchAnimVariant();
					return false;
				}
			}
			else{
				return true;
			}
		}else{
			return false;
		}
	}

	/**
	 * @author me
	 * @reason idk
	 */
	@Overwrite
	public void attack(EntityPlayer entityplayer, Entity entity) {
		if(this.mc.thePlayer.getHeldItem() != null ) {
			Item currentItem = this.mc.thePlayer.getHeldItem().getItem();

			if (this.mc.thePlayer.getGamemode().canInteract() && this.attackCooldown == 0 && onCurrentItemAttack()) {
				entityplayer.attackTargetEntityWithCurrentItem(entity);
				if (this.mc.thePlayer.getHeldItem() != null) {
					this.swingCooldown = ((INewItemVars) currentItem).dxiimod$getItemCooldown();
					this.attackCooldown = ((INewItemVars) currentItem).dxiimod$getItemCooldown();
				}
			}
		}else if (this.mc.thePlayer.getGamemode().canInteract()) {
			entityplayer.attackTargetEntityWithCurrentItem(entity);
			this.mc.thePlayer.swingItem();
		}
	}

	/**
	 * @author can you
	 * @reason stop please
	 */
	@Overwrite
	public boolean swingItem(boolean force) {
		if(force) {
			if (this.swingCooldown == 0) {
				if (this.attackCooldown == 0 && onCurrentItemAttack()) {
					this.mc.thePlayer.swingItem();
					this.swingCooldown = 5;
					return true;
				}
			}
		}else if (this.swingCooldown == 0) {
			if (this.attackCooldown == 0) {
				this.mc.thePlayer.swingItem();
				this.swingCooldown = 5;

				((IPlayerStuff)this.mc.thePlayer).dxiimod$VMswitchAnimVariant();
				return true;
			}
		}
		return false;
	}



	@Redirect(
		method = "startDestroyBlock(IIILnet/minecraft/core/util/helper/Side;DDZ)V",
		at = @At(value = "INVOKE", target = "net/minecraft/core/player/gamemode/Gamemode.canInteract ()Z")
	)
	private boolean blockBreakMixin(Gamemode instance){
		if(this.mc.thePlayer.getHeldItem() != null) {
			Item currentItem = this.mc.thePlayer.getHeldItem().getItem();
			return this.mc.thePlayer.getGamemode().canInteract() && this.attackCooldown == 0 && ((INewItemVars) currentItem).dxiimod$doesBreakBlocks();
		}else{
			return this.mc.thePlayer.getGamemode().canInteract() && this.attackCooldown == 0;
		}
	}

	@Redirect(
		method ="continueDestroyBlock(IIILnet/minecraft/core/util/helper/Side;DD)V",
		at = @At(value = "INVOKE", target = "net/minecraft/core/player/gamemode/Gamemode.canInteract ()Z")
	)
	private boolean mineBLock(Gamemode instance){
		if(this.mc.thePlayer.getHeldItem() != null) {
			Item currentItem = this.mc.thePlayer.getHeldItem().getItem();
			return this.mc.thePlayer.getGamemode().canInteract() && this.attackCooldown == 0 && ((INewItemVars) currentItem).dxiimod$doesBreakBlocks();
		}else{
			return this.mc.thePlayer.getGamemode().canInteract() && this.attackCooldown == 0;
		}

	}

	@Inject(
		method = "tick()V",
		at = @At(value = "INVOKE", target = "net/minecraft/client/player/controller/PlayerController.syncCurrentPlayItem ()V")
	)
	private void attackDelayMixin(CallbackInfo ci){

		if(this.mc.thePlayer.getHeldItem() != null && this.mc.thePlayer.getHeldItem().getItem() != null) {
			Item currentItem = this.mc.thePlayer.getHeldItem().getItem();

			if (this.attackCooldown == 0
				&& dxiimodMain.keyParry.isPressed()
				&& currentItem != null) {

				if( ((INewItemFunctions) currentItem).dxiimod$onItemParry(this.mc.thePlayer) ) {
					this.swingCooldown = ((INewItemVars) currentItem).dxiimod$getItemCooldown();
					this.attackCooldown = ((INewItemVars) currentItem).dxiimod$getItemCooldown();
				}
			}

			if (this.attackCooldown == (this.lastAttackCooldown - 2) ) {
				if(currentItem != null){
					((INewItemFunctions) currentItem).dxiimod$onItemAttack(this.mc.thePlayer, this.mc.thePlayer.getHeldItem(), true);
				}
			}

			if (this.attackCooldown == (this.lastAltAttackCooldown - 2) ) {
				if(currentItem != null){
					((INewItemFunctions) currentItem).dxiimod$onItemAltAttackTimed(this.mc.thePlayer, this.mc.thePlayer.getHeldItem(), true);
				}
			}
		}

		if (this.attackCooldown > 0) {
			--this.attackCooldown;
		}

		if (this.usageCooldown > 0) {
			--this.usageCooldown;
		}
	}

	@Override
	public int dxiimod$getAttackDelay(){
		return this.attackCooldown;
	}

	@Override
	public void dxiimod$setAttackDelay(int ticks){
		this.attackCooldown += ticks;
	}




}
