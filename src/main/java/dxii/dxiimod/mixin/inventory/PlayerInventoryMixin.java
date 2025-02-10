package dxii.dxiimod.mixin.inventory;



import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.ListTag;
import dxii.dxiimod.interfaces.IPlayerInventory;
import dxii.dxiimod.interfaces.IReinforceable;
import dxii.dxiimod.item.enums.EAccBonus;
import dxii.dxiimod.item.accessory.baseAccessory;
import dxii.dxiimod.dxiimodItems;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.InventoryPlayer;
import net.minecraft.core.sound.SoundCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = InventoryPlayer.class, remap = false)
public class PlayerInventoryMixin implements IPlayerInventory {
	@Unique
	public ItemStack[] accInventory = new ItemStack[4];

	@Unique
	protected InventoryPlayer thisObject = (InventoryPlayer)(Object)this ;

	@Override
	public ItemStack[] dxiimod$getAccInv(){
		return this.accInventory;
	}

	/*
	this mixin is a hall of one tortured soul
	here are my first attempts on adding custom accessory slots


	I MADE IT YIPEEEEEEEE
	download nbt explorer if you want this system to be more understandable
	 */

//	@Inject(
//		method ="insertItem(Lnet/minecraft/core/item/ItemStack;Z)V",
//		at = @At(value = "HEAD")
//	)
//	public void upgradeFix(ItemStack stackToAdd, boolean useHotbarOffset, CallbackInfo ci){
//		System.out.println( "insert reinforce = "+((IReinforceable)(Object)stackToAdd).dxiimod$getReinforcement() );
//	}

	/**
	 * @author oooga
	 * @reason boooga
	 */
	@Overwrite
	public ItemStack decrStackSize(int i, int j) {

		ItemStack[] aitemstack = thisObject.mainInventory;

		if(i >= aitemstack.length + thisObject.armorInventory.length){
			i -= aitemstack.length + thisObject.armorInventory.length;
			aitemstack = this.accInventory;
		}
		if (i >= thisObject.mainInventory.length) {
			aitemstack = thisObject.armorInventory;
			i -= thisObject.mainInventory.length;
		}

		if (aitemstack[i] != null) {
			if (aitemstack[i].stackSize <= j) {
				ItemStack itemstack = aitemstack[i];
				aitemstack[i] = null;
				return itemstack;
			}
			ItemStack itemstack1 = aitemstack[i].splitStack(j);
			if (aitemstack[i].stackSize <= 0) {
				aitemstack[i] = null;
			}
			return itemstack1;
		}
		return null;
	}

	/**
	 * @author youre playing
	 * @reason with my nerves
	 */
	@Overwrite
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		ItemStack[] aitemstack = thisObject.mainInventory;


		if(i >= aitemstack.length + thisObject.armorInventory.length){
			i -= aitemstack.length + thisObject.armorInventory.length;
			aitemstack = this.accInventory;
		}
		if (i >= aitemstack.length) {
			i -= aitemstack.length;
			aitemstack = thisObject.armorInventory;
		}
		aitemstack[i] = itemstack;
	}

	/**
	 * @author
	 * @reason
	 */
	@Overwrite
	public ListTag writeToNBT(ListTag nbttaglist) {
		for (int i = 0; i < thisObject.mainInventory.length; ++i) {
			if (thisObject.mainInventory[i] == null) continue;
			CompoundTag nbttagcompound = new CompoundTag();
			nbttagcompound.putByte("Slot", (byte)i);
			thisObject.mainInventory[i].writeToNBT(nbttagcompound);
			nbttaglist.addTag(nbttagcompound);
		}
		for (int j = 0; j < thisObject.armorInventory.length; ++j) {
			if (thisObject.armorInventory[j] == null) continue;
			CompoundTag nbttagcompound1 = new CompoundTag();
			nbttagcompound1.putByte("Slot", (byte)(j + 100));
			thisObject.armorInventory[j].writeToNBT(nbttagcompound1);
			nbttaglist.addTag(nbttagcompound1);
		}
		for (int j = 0; j < this.accInventory.length; ++j) {
			if (this.accInventory[j] == null) continue;
			CompoundTag nbttagcompound2 = new CompoundTag();
			nbttagcompound2.putInt("Acc", j + 512 );
			this.accInventory[j].writeToNBT(nbttagcompound2);
			nbttaglist.addTag(nbttagcompound2);
		}

		return nbttaglist;
	}

	/**
	 * @author
	 * @reason
	 */
	@Overwrite
	public void readFromNBT(ListTag nbttaglist) {

		thisObject.mainInventory = new ItemStack[36];
		thisObject.armorInventory = new ItemStack[4];
		this.accInventory = new ItemStack[4];
		for (int i = 0; i < nbttaglist.tagCount(); ++i) {
			CompoundTag nbttagcompound = (CompoundTag)nbttaglist.tagAt(i);
			ItemStack itemstack = ItemStack.readItemStackFromNbt(nbttagcompound);
			int g = nbttagcompound.getInteger("Acc");
			if (itemstack == null) continue;
			if (g >= 512) {
				this.accInventory[g - 512] = itemstack;
			}
			int j = nbttagcompound.getByte("Slot") & 0xFF;
			if (j >= 0 && j < thisObject.mainInventory.length && g < 512) {
				thisObject.mainInventory[j] = itemstack;
			}
			if (j < 100 || j >= thisObject.armorInventory.length + 100) continue;
			thisObject.armorInventory[j - 100] = itemstack;
		}
	}

	/**
	 * @author
	 * @reason
	 */
	@Overwrite
	public int getSizeInventory() {
		return thisObject.mainInventory.length + this.accInventory.length;
	}

	/**
	 * @author
	 * @reason
	 */
	@Overwrite
	public ItemStack getStackInSlot(int i) {
		ItemStack[] aitemstack = thisObject.mainInventory;

		if(i >= aitemstack.length + thisObject.armorInventory.length){
			i -= aitemstack.length + thisObject.armorInventory.length;
			aitemstack = this.accInventory;
		}
		if (i >= aitemstack.length) {

			i -= aitemstack.length;
			aitemstack = thisObject.armorInventory;
		}
		return aitemstack[i];
	}

	@Inject(
		method = "transferAllContents(Lnet/minecraft/core/player/inventory/InventoryPlayer;)V",
		at = @At(value = "TAIL")
	)
	public void transferAdditional(InventoryPlayer inventoryPlayer, CallbackInfo ci){
		for (int i = 0; i < this.accInventory.length; ++i) {
			this.accInventory[i] = ((IPlayerInventory)(inventoryPlayer)).dxiimod$getAccInv()[i];
			((IPlayerInventory)(inventoryPlayer)).dxiimod$getAccInv()[i] = null;
		}
	}

	/**
	 * @author author
	 * @reason reason :troll:
	 */
	@Overwrite
	public void dropAllItems() {
		boolean sacrifice = false;
		int accSlot = 0;

		if (this.accInventory[0] != null && ((baseAccessory) (this.accInventory[0].getItem())).bonus == EAccBonus.SACRIFICE) {
			sacrifice = true;
		}else if (this.accInventory[1] != null && ((baseAccessory) (this.accInventory[1].getItem())).bonus == EAccBonus.SACRIFICE) {
			sacrifice = true;
			accSlot = 1;
		}else if (this.accInventory[2] != null && ((baseAccessory) (this.accInventory[2].getItem())).bonus == EAccBonus.SACRIFICE) {
			sacrifice = true;
			accSlot = 2;
		}else if (this.accInventory[3] != null && ((baseAccessory) (this.accInventory[3].getItem())).bonus == EAccBonus.SACRIFICE) {
			sacrifice = true;
			accSlot = 3;
		}

		if(sacrifice){
			this.accInventory[accSlot] = new ItemStack(dxiimodItems.sacrificering_broken, 1);
			thisObject.player.world.playSoundEffect(thisObject.player, SoundCategory.ENTITY_SOUNDS, thisObject.player.x, thisObject.player.y, thisObject.player.z, "dxiimod.break_ring", 1, 1);
		}else {
			for (int i = 0; i < thisObject.mainInventory.length; ++i) {
				if (thisObject.mainInventory[i] == null || thisObject.mainInventory[i].getItem() == dxiimodItems.sacrificering_broken) continue;
				thisObject.player.dropPlayerItemWithRandomChoice(thisObject.mainInventory[i], true);
				thisObject.mainInventory[i] = null;
			}
			for (int j = 0; j < thisObject.armorInventory.length; ++j) {
				if (thisObject.armorInventory[j] == null) continue;
				thisObject.player.dropPlayerItemWithRandomChoice(thisObject.armorInventory[j], true);
				thisObject.armorInventory[j] = null;
			}
			for (int g = 0; g < this.accInventory.length; ++g) {
				if (this.accInventory[g] == null || this.accInventory[g].getItem() == dxiimodItems.sacrificering_broken) continue;
				thisObject.player.dropPlayerItemWithRandomChoice(this.accInventory[g], true);
				this.accInventory[g] = null;
			}
		}
	}
}
