package dxii.dxiimod.mixin;



import dxii.dxiimod.dxiimodUtils;
import dxii.dxiimod.interfaces.IPlayerInventory;
import dxii.dxiimod.item.enums.EAccBonus;
import dxii.dxiimod.item.accessory.baseAccessory;
import dxii.dxiimod.mixin.accessors.IAConsumedFood;
import net.minecraft.core.entity.ConsumedFood;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;


@Mixin(value = ConsumedFood.class, remap = false)
public class FoodMixin {

	@Unique
	protected ConsumedFood thisObject = (ConsumedFood)(Object)this ;

	/**
	 * @author aghaah
	 * @reason aaah
	 */
	@Overwrite
	public void tick() {
		EntityLiving player = ((IAConsumedFood)thisObject).getFoodPlayer();

		if (thisObject.isFinished()) {
			return;
		}

		boolean feral = dxiimodUtils.playerHasAccessoryEffect((EntityPlayer)player, EAccBonus.FERALBONE);

		//whoever made 90% of all variables in this class private - I hate you
		((IAConsumedFood) thisObject).setTickCounter(((IAConsumedFood) thisObject).getTickCounter() + 1);
		if(feral) {
			if (((IAConsumedFood) thisObject).getTickCounter() >= ((IAConsumedFood) thisObject).getFood().getTicksPerHeal() / 3) {
				((IAConsumedFood) thisObject).setTickCounter(0);
				((IAConsumedFood) thisObject).setHealRemaining(((IAConsumedFood) thisObject).getHealRemaining() - 1);
				player.heal(1);
			}
		}else{
			if (((IAConsumedFood) thisObject).getTickCounter() >= ((IAConsumedFood) thisObject).getFood().getTicksPerHeal()) {
				((IAConsumedFood) thisObject).setTickCounter(0);
				((IAConsumedFood) thisObject).setHealRemaining(((IAConsumedFood) thisObject).getHealRemaining() - 1);
				player.heal(1);
			}
		}
	}
}
