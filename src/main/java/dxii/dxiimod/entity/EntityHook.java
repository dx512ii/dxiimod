package dxii.dxiimod.entity;


import dxii.dxiimod.dxiimodUtils;
import dxii.dxiimod.interfaces.ILivingEntityFunctions;
import net.minecraft.core.HitResult;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.entity.projectile.EntityProjectile;
import net.minecraft.core.enums.EnumBlockSoundEffectType;
import net.minecraft.core.util.helper.DamageType;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.world.World;

public class EntityHook extends EntityProjectile {

	public boolean active;
	public float lastRotx;
	public float lastRoty;

	public EntityHook(World world, EntityLiving entityliving){
		super(world, entityliving);
	}

	public void init() {
		this.defaultGravity = 0.04F;
		this.defaultProjectileSpeed = 1.0F;
	}

	@Override
	public void onHit(HitResult hitResult) {
		Block hitBlock = this.world.getBlock( hitResult.x, hitResult.y, hitResult.z );

		if (hitResult.hitType == HitResult.HitType.TILE && !this.active) {
			if(hitBlock != null && dxiimodUtils.isHitBlockSolid(this, hitResult) ){
				this.x -= this.xd / 1.2;
				this.y -= this.yd / 1.2;
				this.z -= this.zd / 1.2;

				this.active = true;
				this.lastRotx = this.xRot;
				this.lastRoty = this.yRot;
				this.world.playBlockSoundEffect(this, this.x, this.y, this.z, hitBlock, EnumBlockSoundEffectType.ENTITY_LAND);
				if(hitBlock.blockMaterial == Material.metal || hitBlock.blockMaterial == Material.stone){
					world.playSoundAtEntity(this.owner, this, "dxiimod.iron_stone", 0.1F, (float)(Math.random()/5 + .4) );
				}
			}
		}
		if (hitResult.hitType == HitResult.HitType.ENTITY && hitResult.entity != this.owner) {
			hitResult.entity.hurt(this.owner, 1, DamageType.COMBAT);
			double dist = this.distanceTo(this.owner);
			hitResult.entity.xd += ((this.owner.x - hitResult.entity.x)/dist)*0.7;
			hitResult.entity.yd += ((this.owner.y - hitResult.entity.y)/dist)/2;
			hitResult.entity.zd += ((this.owner.z - hitResult.entity.z)/dist)*0.7;
			world.playSoundAtEntity(this.owner, this, "dxiimod.stab", 0.25F, 1);
			this.remove();
		}
	}



	@Override
	public void tick() {
		super.tick();

		if(this.distanceTo(this.owner) > 10){
			world.playSoundAtEntity(this.owner, this.owner, "random.bow", 0.2F, 0.4F);
			this.active = false;
			this.remove();
		}
		//toooo maaaaany bracketsssss
		if(  ( ((ILivingEntityFunctions)this.owner).dxiimod$getIsJumping() || !this.owner.isAlive() ) && this.active && this.ticksInGround > 1 ){
			this.active = false;
			this.remove();
		}

		if(this.active){
			this.ticksInGround++;

			this.xRot = this.lastRotx;
			this.yRot = this.lastRoty;
			this.xd = 0;
			this.yd = 0;
			this.zd = 0;

			double dist = this.distanceTo(this.owner);

			if(dist <= 1.5 && this.ticksInGround > 1){
				this.owner.xd = 0;
				this.owner.yd = 0;
				this.owner.zd = 0;
				this.owner.yd += MathHelper.clamp( ((this.y - this.owner.y + .5)/dist)/2, -.8, .8 );
				this.owner.onGround = true;
			}else if(this.ticksInGround > 1){
				this.owner.onGround = true;

				this.owner.fallDistance = 0;
				this.owner.xd = 0;
				this.owner.yd = 0;
				this.owner.zd = 0;

				double speed = .3;
				this.owner.xd += MathHelper.clamp( ((this.x - this.owner.x)/dist)/2, -speed, speed );
				this.owner.yd += MathHelper.clamp( ((this.y - this.owner.y)/dist)/2, -speed, speed );
				this.owner.zd += MathHelper.clamp( ((this.z - this.owner.z)/dist)/2, -speed, speed );
			}

		}
	}
}
