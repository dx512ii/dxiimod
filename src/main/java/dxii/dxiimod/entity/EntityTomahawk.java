package dxii.dxiimod.entity;

import dxii.dxiimod.dxiimodUtils;
import dxii.dxiimod.interfaces.ILivingEntityDamageTypes;
import dxii.dxiimod.interfaces.IReinforceable;
import dxii.dxiimod.item.enums.DamageInfo;
import dxii.dxiimod.item.enums.EDamageTypeExtra;
import net.minecraft.core.HitResult;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.entity.projectile.EntityProjectile;
import net.minecraft.core.enums.EnumBlockSoundEffectType;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.gamemode.Gamemode;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.util.phys.Vec3d;
import net.minecraft.core.world.World;

public class EntityTomahawk extends EntityProjectile{
	public int customRot;
	public String texture;
	public boolean doesDrop;
	public Item droppedItem;
	public DamageInfo dInfo = new DamageInfo();
	public byte upgrade;

	public EntityTomahawk(World world, EntityLiving entityliving, int damage, byte upgrade, String texture, float speed, float gravity, boolean doesDrop, Item droppedItem) {
		super(world, entityliving);
		this.damage = damage;
		this.texture = texture;
		this.doesDrop = doesDrop;
		this.droppedItem = droppedItem;
		this.defaultGravity = gravity;
		this.defaultProjectileSpeed = speed;
		this.upgrade = upgrade;

		dInfo.setDmg(damage);
		dInfo.setDmgType(EDamageTypeExtra.SLASH);
		dInfo.setAttacker(this.owner);
		dInfo.setIgnoresIframes(true);

	}



	public void tick(){
		super.tick();
		customRot = customRot + 45;
	}

	public void onHit(HitResult hitResult) {
		if (hitResult.hitType == HitResult.HitType.TILE) {
			Block hitBlock = this.world.getBlock( hitResult.x, hitResult.y, hitResult.z );

			if(dxiimodUtils.isHitBlockSolid(this, hitResult)) {
				if(((EntityPlayer)this.owner).gamemode != Gamemode.creative && this.doesDrop ) {
					ItemStack stack = new ItemStack(this.droppedItem, 1);
					((IReinforceable)(Object)stack).dxiimod$setReinforcement(this.upgrade);

					EntityItem item = new EntityItem(this.world, this.x, this.y, this.z, stack);
					this.world.entityJoinedWorld(item);

					returnToSender(item);
				}
				this.remove();
				this.world.playBlockSoundEffect(this, this.x, this.y, this.z, hitBlock, EnumBlockSoundEffectType.ENTITY_LAND);
				if(hitBlock.blockMaterial == Material.metal || hitBlock.blockMaterial == Material.stone){
					world.playSoundAtEntity(this.owner, this, "dxiimod.iron_stone", 0.25F, (float)(Math.random()/4 + .4) );
				}
			}

		}

		if (hitResult.hitType == HitResult.HitType.ENTITY) {

			//hitResult.entity.hurt(this.owner, this.damage, DamageType.COMBAT);
			((ILivingEntityDamageTypes)hitResult.entity).dxiimod$hurtCustom(this.dInfo);

			if(((EntityPlayer)this.owner).gamemode != Gamemode.creative && this.doesDrop) {
				ItemStack stack = new ItemStack(this.droppedItem, 1);
				((IReinforceable)(Object)stack).dxiimod$setReinforcement(this.upgrade);

				EntityItem item = new EntityItem(this.world, this.x, this.y, this.z, stack);
				this.world.entityJoinedWorld(item);

				returnToSender(item);
			}
//			world.playSoundAtEntity(world.getClosestPlayer(this.x, this.y, this.z, 20.0), this, "random.drr", 0.5F, 1f);
			this.remove();
		}

	}

	//pushes newly spawned item to its sender
	public void returnToSender(EntityItem item){
		Vec3d dir = Vec3d.createVector(
			this.owner.x - (this.x),
			this.owner.y - (this.y),
			this.owner.z - (this.z)
		).normalize();
		double dist = this.distanceTo(this.owner);

		double power = 0.75 * MathHelper.clamp(dist, 0, 10)/10;

		item.xd += dir.xCoord * power;
		item.yd += dir.yCoord * power;
		item.zd += dir.zCoord * power;
	}
}
