package dxii.dxiimod.mixin;

import dxii.dxiimod.dxiimodItems;
import net.minecraft.core.WeightedRandomBag;
import net.minecraft.core.WeightedRandomLootObject;
import net.minecraft.core.world.World;
import net.minecraft.core.world.generate.feature.WorldFeatureLabyrinth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

//here i add new loot to labyrinths
@Mixin(value = WorldFeatureLabyrinth.class, remap = false)
public class LabyrinthMixin {

	@Shadow
	public WeightedRandomBag<WeightedRandomLootObject> chestLoot;

	@Inject(
		method = "generate(Lnet/minecraft/core/world/World;Ljava/util/Random;III)Z",
		at = @At(value = "FIELD", target = "net/minecraft/core/world/generate/feature/WorldFeatureLabyrinth.slabBlock : I", ordinal = 0)
	)
	public void addNewLoot(World world, Random random, int x, int y, int z, CallbackInfoReturnable<Boolean> cir){
		this.chestLoot.addEntry(new WeightedRandomLootObject(dxiimodItems.fogRing.getDefaultStack()), 1.0);
		this.chestLoot.addEntry(new WeightedRandomLootObject(dxiimodItems.sacrificering.getDefaultStack()), 2.0);
		this.chestLoot.addEntry(new WeightedRandomLootObject(dxiimodItems.evileyering.getDefaultStack()), 2.0);
		this.chestLoot.addEntry(new WeightedRandomLootObject(dxiimodItems.hornetRing.getDefaultStack()), 2.0);
	}



}
