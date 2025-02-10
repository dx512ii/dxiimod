package dxii.dxiimod.mixin;

import com.mojang.nbt.CompoundTag;
import dxii.dxiimod.dxiimodUtils;
import dxii.dxiimod.interfaces.IReinforceable;
import dxii.dxiimod.item.enums.EAccBonus;
import dxii.dxiimod.item.enums.EUpgradeType;
import dxii.dxiimod.item.itemWeaponBase;
import dxii.dxiimod.mixin.accessors.IAItemStack;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//currently used for cherry wood ring and reinforcement stuff
@Mixin(value = ItemStack.class, remap = false)
public class ItemStackMixin implements IReinforceable {

	@Unique
	public Entity currentPlayer;

	@Unique
	public ItemStack thisObject = (ItemStack)(Object)this;

	@Unique
	public byte reinforcement = 0;

	@Override
	public byte dxiimod$getReinforcement(){
		return reinforcement;
	}

	@Override
	public void dxiimod$setReinforcement(byte reinforcement){
		this.reinforcement = reinforcement;
	}

	@Override
	public boolean dxiimod$reinforceItem(){
		if(thisObject.getItem() != null) {
			EUpgradeType upgradeType = ((itemWeaponBase) thisObject.getItem()).upgradeType;
			int maxUpgrade = 1;

			if (upgradeType == EUpgradeType.DEFAULT) {
				maxUpgrade = 10;
			} else if (upgradeType == EUpgradeType.TWINKLING) {
				maxUpgrade = 5;
			}

			if (this.reinforcement < maxUpgrade) {
				this.reinforcement++;
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean dxiimod$downgradeItem(){
		if(thisObject.getItem() != null) {
			if (this.reinforcement > 0) {
				this.reinforcement--;
				return true;
			}
		}

		return false;
	}

	@Inject(
		method = "writeToNBT(Lcom/mojang/nbt/CompoundTag;)Lcom/mojang/nbt/CompoundTag;",
		at = @At(value = "HEAD"))
	public void saveAdditional(CompoundTag nbt, CallbackInfoReturnable<CompoundTag> cir){
		nbt.putByte("UpgradeAmt", this.reinforcement);
	}

	@Inject(
		method = "readFromNBT(Lcom/mojang/nbt/CompoundTag;)V",
		at = @At(value = "HEAD"))
	public void loadAdditional(CompoundTag nbt, CallbackInfo ci){
		this.reinforcement = nbt.getByte("UpgradeAmt");;

	}

	//cherry wood grain ring
	@Inject(
		method = "damageItem(ILnet/minecraft/core/entity/Entity;)V",
		at = @At(value = "HEAD"))
	public void cherryRing(int i, Entity entity, CallbackInfo ci){
		currentPlayer = entity;
	}

	@Redirect(
		method = "damageItem(ILnet/minecraft/core/entity/Entity;)V",
		at = @At(value = "INVOKE", target = "net/minecraft/core/item/ItemStack.isItemStackDamageable ()Z")
	)
	private boolean cherryRing2(ItemStack instance){
		if(currentPlayer instanceof EntityPlayer) {
			boolean cherry = dxiimodUtils.playerHasAccessoryEffect((EntityPlayer) currentPlayer, EAccBonus.CHERRYWOOD);
			boolean rand = (Math.random() <= .66);

			return !instance.isItemStackDamageable() || !(cherry & rand);
		}else{
			return !instance.isItemStackDamageable();
		}
	}

	/**
	 * @author this mixin is purely to fix upgrade problems on dropped items
	 * @reason	upon copying, it doesnt copy all the stuff i need
	 */
	@Overwrite
	public ItemStack copy() {
		ItemStack stack = new ItemStack(thisObject.itemID, thisObject.stackSize, thisObject.getMetadata(), new CompoundTag( ((IAItemStack)(Object)thisObject).getTag() ));
		((IReinforceable)(Object)stack).dxiimod$setReinforcement(this.reinforcement);
		return stack;
	}

}
