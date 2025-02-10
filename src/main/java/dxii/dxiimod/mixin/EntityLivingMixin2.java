package dxii.dxiimod.mixin;


import dxii.dxiimod.dxiimodUtils;
import dxii.dxiimod.interfaces.ILivingEntityDamageTypes;
import dxii.dxiimod.interfaces.ILivingEntityFunctions;
import dxii.dxiimod.item.enums.DamageInfo;
import dxii.dxiimod.item.enums.DamageResistModule;
import dxii.dxiimod.item.enums.EAccBonus;
import dxii.dxiimod.item.enums.EDamageTypeExtra;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;


@Mixin(value = EntityLiving.class, remap = false)
public abstract class EntityLivingMixin2 implements ILivingEntityDamageTypes {
	@Shadow
	public abstract boolean sendDeathMessage(Entity entityKilledBy);

	@Unique
	public EntityLiving thisObject = (EntityLiving)(Object)this ;

	@Unique
	public DamageResistModule dResists = new DamageResistModule();


	@Override
	public DamageResistModule dxiimod$getDamageResistModule(){
		return this.dResists;
	}

	@Override
	public void dxiimod$hurtCustom(DamageInfo dInfo){
		EDamageTypeExtra dmgtype = dInfo.getDmgType();
		int resist = dResists.getDefAgainstEDamage(dmgtype);
		double weakness = dResists.getWeakMulAgainstEDamage(dmgtype);
		if(weakness < 1){
			weakness = 1;
		}

		switch(dmgtype){
			case FIRE:
				double rand = Math.random();
				thisObject.world.spawnParticle("flame", thisObject.getPosition(1).xCoord +rand*0.2, thisObject.getPosition(1).yCoord +rand*0.2, thisObject.getPosition(1).zCoord -rand*0.2, -rand* 0.01, rand* 0.01, -rand* 0.01, 0);
				thisObject.world.spawnParticle("flame", thisObject.getPosition(1).xCoord -rand*0.2, thisObject.getPosition(1).yCoord -rand*0.2, thisObject.getPosition(1).zCoord +rand*0.2, rand* 0.01, -rand* 0.01, rand* 0.01, 0);
				thisObject.world.spawnParticle("flame", thisObject.getPosition(1).xCoord +rand*0.2, thisObject.getPosition(1).yCoord +rand*0.2, thisObject.getPosition(1).zCoord -rand*0.2, -rand* 0.01, rand* 0.01, -rand* 0.01, 0);
				break;
		}

		int newDmg = calculateAtkDamage(dInfo);
		if(newDmg < 0){ newDmg = 1; }
		if(!dInfo.ignoresResistances()) {
			newDmg -= resist;
		}
		newDmg = (int)(newDmg * weakness);

		thisObject.hurt(dInfo.getAttacker(), newDmg, null);
	}

	//calculates raw damage condsidering rtsr and hornet ring
	@Unique
	public int calculateAtkDamage(DamageInfo dInfo){
		int newDmg = dInfo.getDmg();
		EntityLiving attacker = (EntityLiving)dInfo.getAttacker();
		EDamageTypeExtra dmgType = dInfo.getDmgType();

		if(attacker instanceof EntityPlayer) {
			boolean rtsr = dxiimodUtils.playerHasAccessoryEffect( (EntityPlayer)attacker, EAccBonus.RTSR);
			boolean hornet = dxiimodUtils.playerHasAccessoryEffect( (EntityPlayer)attacker, EAccBonus.HORNETRING);

			//small check to make sure parried entities dont get doubled/tripled status effect dmg
			if (!(dmgType == EDamageTypeExtra.POISON || dmgType == EDamageTypeExtra.BLOOD)) {
				if (rtsr & attacker.getHealth() <= 4) {
					newDmg *= 2;
				}

				if (((ILivingEntityFunctions) (thisObject)).dxiimod$getParriedTicks() > 0) {
					if (hornet) {
						newDmg *= 3;
					} else {
						newDmg *= 2;
					}
				}
			}
		}

		return newDmg;
	}

}
