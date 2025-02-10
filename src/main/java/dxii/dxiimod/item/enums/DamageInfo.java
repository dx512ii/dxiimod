package dxii.dxiimod.item.enums;

import net.minecraft.core.entity.Entity;

public class DamageInfo { //my try on reducing code bloat in certain functions

	private int dmg = 1;
	private EDamageTypeExtra dmgType = EDamageTypeExtra.GENERIC;
	private double knockback = 1;
	private Entity attacker = null;
	private boolean ignoresIframes = false;
	private boolean ignoresResistances= false;

	public int getDmg(){
		return this.dmg;
	}
	public EDamageTypeExtra getDmgType(){
		return this.dmgType;
	}
	public double getKnockback(){
		return this.knockback;
	}
	public Entity getAttacker(){
		return this.attacker;
	}
	public boolean ignoresIframes(){
		return this.ignoresIframes;
	}
	public boolean ignoresResistances(){
		return this.ignoresResistances;
	}


	public DamageInfo setDmg(int newdmg){
		this.dmg = newdmg;
		return this;
	}
	public DamageInfo setDmgType(EDamageTypeExtra newDmgType){
		this.dmgType = newDmgType;
		return this;
	}
	public DamageInfo setKnockback(double newKnockback){
		this.knockback = newKnockback;
		return this;
	}
	public DamageInfo setAttacker( Entity newAtker){
		this.attacker = newAtker;
		return this;
	}
	public DamageInfo setIgnoresIframes(boolean yea){
		this.ignoresIframes = yea;
		return this;
	}
	public DamageInfo setIgnoresResistances(boolean yeah){
		this.ignoresResistances = yeah;
		return this;
	}

}
