package dxii.dxiimod.item.enums;


import org.spongepowered.asm.mixin.Unique;

//basically holds some variables so i dont have a bloat of classes and interfaces
public class DamageResistModule {
	//aight, now i kinda understand why soem variables get private or protected attribute
	//sometimes you want to access a function but you get a bloat of variables dropped from the context menu
	private int ThrustResist = 0;
	private int StrikeResist = 0;
	private int SlashResist = 0;
	private int MagicResist = 0;
	private int PoisonResist = 0;
	private int BloodResist = 0;
	private int LightningResist = 0;
	private int DarkResist = 0;
	private int FireResist = 0;

	private double ThrustWeakness = 1;
	private double StrikeWeakness = 1;
	private double SlashWeakness = 1;
	private double MagicWeakness = 1;
	private double PoisonWeakness = 1;
	private double BloodWeakness= 1;
	private double LightningWeakness = 1;
	private double DarkWeakness = 1;
	private double FireWeakness = 1;


	public void setDefAgainstEDamage(EDamageTypeExtra type, int def){
		switch(type){
			case THRUST:
				this.ThrustResist = def;
			case STRIKE:
				this.StrikeResist = def;
			case SLASH:
				this.SlashResist = def;
			case MAGIC:
				this.MagicResist = def;
			case POISON:
				this.PoisonResist = def;
			case BLOOD:
				this.BloodResist = def;
			case LIGHTNING:
				this.LightningResist = def;
			case DARK:
				this.DarkResist = def;
			case FIRE:
				this.FireResist = def;
		}
	}

	public int getDefAgainstEDamage(EDamageTypeExtra type){
		switch(type){
			case THRUST:
				return this.ThrustResist;
			case STRIKE:
				return this.StrikeResist;
			case SLASH:
				return this.SlashResist;
			case MAGIC:
				return this.MagicResist;
			case POISON:
				return this.PoisonResist;
			case BLOOD:
				return this.BloodResist;
			case LIGHTNING:
				return this.LightningResist;
			case DARK:
				return this.DarkResist;
			case FIRE:
				return this.FireResist;
			default: return 0;
		}
	}

	public void setWeakMulAgainstEDamage(EDamageTypeExtra type, int weak){
		switch(type){
			case THRUST:
				this.ThrustWeakness = weak;
			case STRIKE:
				this.StrikeWeakness = weak;
			case SLASH:
				this.SlashWeakness = weak;
			case MAGIC:
				this.MagicWeakness = weak;
			case POISON:
				this.PoisonWeakness = weak;
			case BLOOD:
				this.BloodWeakness = weak;
			case LIGHTNING:
				this.LightningWeakness = weak;
			case DARK:
				this.DarkWeakness = weak;
			case FIRE:
				this.FireWeakness = weak;
		}
	}

	public double getWeakMulAgainstEDamage(EDamageTypeExtra type){
		switch(type){
			case THRUST:
				return this.ThrustWeakness;
			case STRIKE:
				return this.StrikeWeakness;
			case SLASH:
				return this.SlashWeakness;
			case MAGIC:
				return this.MagicWeakness;
			case POISON:
				return this.PoisonWeakness;
			case BLOOD:
				return this.BloodWeakness;
			case LIGHTNING:
				return this.LightningWeakness;
			case DARK:
				return this.DarkWeakness;
			case FIRE:
				return this.FireWeakness;
			default: return 0;
		}
	}

}
