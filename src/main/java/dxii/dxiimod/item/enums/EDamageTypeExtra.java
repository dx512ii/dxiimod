package dxii.dxiimod.item.enums;

public enum EDamageTypeExtra { //used for custom hurt func
	GENERIC,

	//phys
	THRUST,
	STRIKE,
	SLASH,

	//elemental
	MAGIC,
	LIGHTNING,
	DARK,
	FIRE, //duplicate cuz uuh idk, i dont want to keep two enums, this will go directly to hurt method

	//status effects
	POISON,
	BLOOD,
}
