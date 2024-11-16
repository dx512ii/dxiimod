package dxii.dxiimod.entity.enemy;

import dxii.dxiimod.interfaces.IWorldVariables;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.entity.monster.EntityMonster;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.player.gamemode.Gamemode;
import net.minecraft.core.util.helper.DamageType;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.world.World;
import net.minecraft.core.world.season.Seasons;

import java.util.List;

public class EnemyFogLurker extends EntityMonster {

	public double moveBoost = .1;
	public int moveBoostDistTR1 = 10;
	public int moveBoostDistTR2 = 5;
	public int voicePitch = 1;
	public double attackDist = 2;

	public int alertDelay;


	//this mob is geniune nightmare fuel, it kills everything except its own kind
	public EnemyFogLurker(World world){
		super(world);
		this.moveSpeed = 1f;
		if(world.getSeasonManager().getCurrentSeason() == Seasons.OVERWORLD_WINTER){
			this.moveSpeed = .2f;
		}
		this.scoreValue = 100;
		this.attackStrength = 15;
		this.entityToAttack = null;

		if( !( ((IWorldVariables) (this.world.getLevelData())).dxiimod$getFog() ) || this.world.getWorldTime() / 24000f + .5 < ((IWorldVariables)(this.world.getLevelData())).dxiimod$getFogDay() + 1.4){
			this.remove();
		}
	}

	@Override
	public int getMaxHealth() {
		return 30;
	}

	public String getEntityTexture() {return "/assets/dxiimod/textures/enemy/foglurker1.png";}
	public String getDefaultEntityTexture() {
		return "/assets/dxiimod/textures/enemy/foglurker1.png";
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		alertDelay--;
		if(this.entityToAttack == null){
			this.getEntToAttack();
		}

		if(this.entityToAttack != null){
			if ( this.onGround && this.entityToAttack.distanceTo(this) < this.moveBoostDistTR1 && !(this.entityToAttack.distanceTo(this) < this.moveBoostDistTR2) ) {
				double pushX = MathHelper.clamp(this.x - this.entityToAttack.x, -1, 1);
				double pushZ = MathHelper.clamp(this.z - this.entityToAttack.z, -1, 1);
				this.push(-pushX * this.moveBoost, .01, -pushZ * this.moveBoost);
				if(alertDelay == 0) {
					this.world.playSoundAtEntity(null, this, "dxiimod.foglurker_target", this.getSoundVolume(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + this.voicePitch);
					alertDelay = 25;
				}
			}
			if (this.entityToAttack.distanceTo(this) > 20) {
				this.entityToAttack = null;
			}
			if(this.entityToAttack instanceof EntityPlayer) {
				if (((EntityPlayer) this.entityToAttack).getGamemode() == Gamemode.creative) {
					this.entityToAttack = null;
				}
			}
			this.searchForPlayers();


		}
	}

	public void searchForPlayers(){
		if(this.findPlayerToAttack() == null){
			return;
		}
		this.entityToAttack = this.findPlayerToAttack();
	}

	public void getEntToAttack(){
		/*
		there it searches for every living entity to kill,
		might produce bugs but anyways, it will still prioritize player
		 */

		double bound = 18;
		List<Entity> entityList = this.world.getEntitiesWithinAABB(EntityLiving.class, this.bb.expand(bound, bound, bound) );

		for (Entity entity : entityList) {
			if (
				entity instanceof EntityLiving
				&& !(entity instanceof EnemyFogLurker)
				&& !(entity instanceof EntityPlayer)
			) {

				this.entityToAttack = entity;
//				System.out.println(this + " : found ent to attack!!");
			}
		}
	}

	@Override
	protected void attackEntity(Entity entity, float distance) {
		if (this.attackTime <= 0 && distance < this.attackDist && entity.bb.maxY > this.bb.minY && entity.bb.minY < this.bb.maxY) {
			this.attackTime = 20;
			entity.hurt(this, this.attackStrength, DamageType.COMBAT);
		}
	}

	public int getTalkInterval() {
		return 350;
	}


	//all these sounds are me btw >=}
	@Override
	public String getLivingSound() {
		return "dxiimod.foglurker_random";
	}

	@Override
	protected String getHurtSound() {
		return "dxiimod.foglurker_random";
	}

	@Override
	protected String getDeathSound() {
		return "dxiimod.foglurker_random";
	}

	@Override
	public void playLivingSound() {
		String s = this.getLivingSound();
		if (s != null && !this.world.isClientSide) {
			this.world.playSoundAtEntity(null, this, s, this.getSoundVolume(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + this.voicePitch);
		}
	}

	@Override
	public void playHurtSound() {
		this.world.playSoundAtEntity(null, this, this.getHurtSound(), this.getSoundVolume(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + this.voicePitch);
	}

	@Override
	public void playDeathSound() {
		this.world.playSoundAtEntity(null, this, this.getDeathSound(), this.getSoundVolume(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + this.voicePitch);
	}

	@Override
	public int getMaxSpawnedInChunk() {
		return 8;
	}


	//it spawns only if world meets requirements (fog)
	@Override
	public boolean getCanSpawnHere() {
	    if (dxii.dxiimod.dxiimodMain.THE_FOG == 0) return false;
		int z;
		int y;
		int x = MathHelper.floor_double(this.x);
		int id = this.world.getBlockId(x, (y = MathHelper.floor_double(this.bb.minY)) - 1, z = MathHelper.floor_double(this.z));
		if (Block.blocksList[id] != null) {
			return (Block.blocksList[id].hasTag(BlockTags.PASSIVE_MOBS_SPAWN) || id == Block.layerSnow.id)
				&& this.world.getFullBlockLightValue(x, y, z) > 1
				&& ((IWorldVariables) (this.world.getLevelData())).dxiimod$getFog()
				&& this.world.getWorldTime() / 24000f + .5 > ((IWorldVariables)(this.world.getLevelData())).dxiimod$getFogDay() + 1.4
				;
		}
		return false;
	}

}
