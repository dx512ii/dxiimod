package dxii.dxiimod;

import dxii.dxiimod.item.*;
import dxii.dxiimod.item.enums.EAccBonus;
import dxii.dxiimod.item.accessory.baseAccessory;
import dxii.dxiimod.render.itemModelGreatSword;
import dxii.dxiimod.render.itemModelGun;
import dxii.dxiimod.render.itemModelSpear;
import dxii.dxiimod.render.itemModelHammer;
import net.minecraft.client.render.item.model.ItemModelStandard;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.material.ToolMaterial;
import turniplabs.halplibe.helper.ItemBuilder;

public class dxiimodItems {
	public static itemSuspiciousTotem suspiciousTotem;
	public static bucklerItem shieldBuckler;

	public static throwingDaggerItem trowingDagger;

	public static itemTomahawk stoneTomahawk;
	public static itemTomahawk ironTomahawk;
	public static itemTomahawk goldTomahawk;
	public static itemTomahawk diamondTomahawk;

	public static itemWeaponHammer stoneHammer;
	public static itemWeaponHammer ironHammer;
	public static itemWeaponHammer goldHammer;
	public static itemWeaponHammer steelHammer;

	public static ItemWeaponSpear woodenSpear;
	public static ItemWeaponSpear stoneSpear;
	public static ItemWeaponSpear ironSpear;
	public static ItemWeaponSpear goldSpear;
	public static ItemWeaponSpear diamondSpear;
	public static ItemWeaponSpear steelSpear;

	public static ItemWeaponGreatsword greatswordStone;
	public static ItemWeaponGreatsword greatswordIron;
	public static ItemWeaponGreatsword greatswordGold;
	public static ItemWeaponGreatsword greatswordSteel;

	public static ItemWeaponFlintlock itemFlintlock;

	public static baseAccessory fogRing;
	public static baseAccessory cloudBottle;
	public static baseAccessory hornetRing;
	public static baseAccessory frogLeg;
	public static baseAccessory sacrificering;
	public static baseAccessory sacrificering_broken;
	public static Item soulpeace;
	public static Item soulevil;
	public static Item soulevil_great;
	public static Item soulpeace_great;
	public static baseAccessory evileyering;
	public static baseAccessory featherfall;
	public static baseAccessory cherrywoodring;
	public static baseAccessory rtsr;
	public static baseAccessory redeye;

	public static graplingHook ghook;
	public static baseAccessory dragoncrest;
	public static baseAccessory cloranthyRing;
	public static baseAccessory feralBone;

	public static ItemWeaponFlamespewer flameSpewer;

	public static Item bullet_iron;
	public static Item bullet_steel;


	// Function that make the items. Holy jonk i made lots of rings
	public void Initialize() {

		trowingDagger = new ItemBuilder(dxiimodMain.MOD_ID).setIcon(dxiimodMain.MOD_ID + ":item/dagger").setItemModel(item -> new ItemModelStandard(item, dxiimodMain.MOD_ID).setFull3D()).build(new throwingDaggerItem("THROWING_DAGGER", dxiimodMain.ITEM_ID++) );

		/*
		there were actually classes for each tomahawk item,
		each entity, each renderer but i tried to make a
		single classes for every purpose, feels kinda over-
		loaded
		 */
		stoneTomahawk = new ItemBuilder(dxiimodMain.MOD_ID).setIcon(dxiimodMain.MOD_ID + ":item/tomahawk_stone").setItemModel(item -> new ItemModelStandard(item, dxiimodMain.MOD_ID).setFull3D()).build(new itemTomahawk("STONE_TOMAHAWK", dxiimodMain.ITEM_ID++, 1, 15, 3, 6, 7, ToolMaterial.stone) );
		ironTomahawk = new ItemBuilder(dxiimodMain.MOD_ID).setIcon(dxiimodMain.MOD_ID + ":item/tomahawk_iron").setItemModel(item -> new ItemModelStandard(item, dxiimodMain.MOD_ID).setFull3D()).build(new itemTomahawk("IRON_TOMAHAWK", dxiimodMain.ITEM_ID++, 1, 10, 3, 8, 9, ToolMaterial.iron) );
		goldTomahawk = new ItemBuilder(dxiimodMain.MOD_ID).setIcon(dxiimodMain.MOD_ID + ":item/tomahawk_gold").setItemModel(item -> new ItemModelStandard(item, dxiimodMain.MOD_ID).setFull3D()).build(new itemTomahawk("GOLD_TOMAHAWK", dxiimodMain.ITEM_ID++, 1, 8, 3, 8, 8, ToolMaterial.gold) );
		diamondTomahawk = new ItemBuilder(dxiimodMain.MOD_ID).setIcon(dxiimodMain.MOD_ID + ":item/tomahawk_diamond").setItemModel(item -> new ItemModelStandard(item, dxiimodMain.MOD_ID).setFull3D()).build(new itemTomahawk("DIAMOND_TOMAHAWK", dxiimodMain.ITEM_ID++, 1, 10, 3, 12, 12, ToolMaterial.diamond) );

		stoneHammer = new ItemBuilder(dxiimodMain.MOD_ID).setIcon(dxiimodMain.MOD_ID + ":item/hammer_stone").setItemModel(item -> new itemModelHammer(item, dxiimodMain.MOD_ID).setFull3D()).build(new itemWeaponHammer("STONE_HAMMER", dxiimodMain.ITEM_ID++, 17, 2f, 17, 128) );
		ironHammer = new ItemBuilder(dxiimodMain.MOD_ID).setIcon(dxiimodMain.MOD_ID + ":item/hammer_iron").setItemModel(item -> new itemModelHammer(item, dxiimodMain.MOD_ID).setFull3D()).build(new itemWeaponHammer("IRON_HAMMER", dxiimodMain.ITEM_ID++, 20, 2.4f, 18, 512) );
		goldHammer = new ItemBuilder(dxiimodMain.MOD_ID).setIcon(dxiimodMain.MOD_ID + ":item/hammer_gold").setItemModel(item -> new itemModelHammer(item, dxiimodMain.MOD_ID).setFull3D()).build(new itemWeaponHammer("GOLD_HAMMER", dxiimodMain.ITEM_ID++, 25, 3.2f, 22, 512) );
		steelHammer = new ItemBuilder(dxiimodMain.MOD_ID).setIcon(dxiimodMain.MOD_ID + ":item/hammer_steel").setItemModel(item -> new itemModelHammer(item, dxiimodMain.MOD_ID).setFull3D()).build(new itemWeaponHammer("STEEL_HAMMER", dxiimodMain.ITEM_ID++, 20, 2.6f, 18, 4096) );


		woodenSpear = new ItemBuilder(dxiimodMain.MOD_ID).setIcon(dxiimodMain.MOD_ID + ":item/spear_wood").setItemModel(item -> new itemModelSpear(item, dxiimodMain.MOD_ID).setFull3D()).build(new ItemWeaponSpear("WOOD_SPEAR", dxiimodMain.ITEM_ID++, 5,4, 8, 64));
		stoneSpear = new ItemBuilder(dxiimodMain.MOD_ID).setIcon(dxiimodMain.MOD_ID + ":item/spear_stone").setItemModel(item -> new itemModelSpear(item, dxiimodMain.MOD_ID).setFull3D()).build(new ItemWeaponSpear("STONE_SPEAR", dxiimodMain.ITEM_ID++, 5, 5,12, 128));
		ironSpear = new ItemBuilder(dxiimodMain.MOD_ID).setIcon(dxiimodMain.MOD_ID + ":item/spear_iron").setItemModel(item -> new itemModelSpear(item, dxiimodMain.MOD_ID).setFull3D()).build(new ItemWeaponSpear("IRON_SPEAR", dxiimodMain.ITEM_ID++, 5, 8, 15, 256) );
		goldSpear = new ItemBuilder(dxiimodMain.MOD_ID).setIcon(dxiimodMain.MOD_ID + ":item/spear_gold").setItemModel(item -> new itemModelSpear(item, dxiimodMain.MOD_ID).setFull3D()).build(new ItemWeaponSpear("GOLD_SPEAR", dxiimodMain.ITEM_ID++, 5, 8, 12, 256) );
		diamondSpear = new ItemBuilder(dxiimodMain.MOD_ID).setIcon(dxiimodMain.MOD_ID + ":item/spear_diamond").setItemModel(item -> new itemModelSpear(item, dxiimodMain.MOD_ID).setFull3D()).build(new ItemWeaponSpear("DIAMOND_SPEAR", dxiimodMain.ITEM_ID++, 5, 12, 12, 512) );
		steelSpear = new ItemBuilder(dxiimodMain.MOD_ID).setIcon(dxiimodMain.MOD_ID + ":item/spear_steel").setItemModel(item -> new itemModelSpear(item, dxiimodMain.MOD_ID).setFull3D()).build(new ItemWeaponSpear("STEEL_SPEAR", dxiimodMain.ITEM_ID++, 5, 10, 14, 4096) );

		greatswordStone = new ItemBuilder(dxiimodMain.MOD_ID).setIcon(dxiimodMain.MOD_ID + ":item/greatsword_stone").setItemModel(item -> new itemModelGreatSword(item, dxiimodMain.MOD_ID).setFull3D()).build(new ItemWeaponGreatsword("STONE_GREATSWORD", dxiimodMain.ITEM_ID++, 15, 4, 17, 128, .1) );
		greatswordIron = new ItemBuilder(dxiimodMain.MOD_ID).setIcon(dxiimodMain.MOD_ID + ":item/greatsword_iron").setItemModel(item -> new itemModelGreatSword(item, dxiimodMain.MOD_ID).setFull3D()).build(new ItemWeaponGreatsword("IRON_GREATSWORD", dxiimodMain.ITEM_ID++, 22, 4, 18, 384, .2) );
		greatswordGold = new ItemBuilder(dxiimodMain.MOD_ID).setIcon(dxiimodMain.MOD_ID + ":item/greatsword_gold").setItemModel(item -> new itemModelGreatSword(item, dxiimodMain.MOD_ID).setFull3D()).build(new ItemWeaponGreatsword("GOLD_GREATSWORD", dxiimodMain.ITEM_ID++, 20, 4,22, 256, 1) );
		greatswordSteel = new ItemBuilder(dxiimodMain.MOD_ID).setIcon(dxiimodMain.MOD_ID + ":item/greatsword_steel").setItemModel(item -> new itemModelGreatSword(item, dxiimodMain.MOD_ID).setFull3D()).build(new ItemWeaponGreatsword("STEEL_GREATSWORD", dxiimodMain.ITEM_ID++, 20, 4,20, 4608, .2) );


		suspiciousTotem = new ItemBuilder(dxiimodMain.MOD_ID).setIcon(dxiimodMain.MOD_ID + ":item/susp_totem").build(new itemSuspiciousTotem("SUSP_TOTEM", dxiimodMain.ITEM_ID++) );

		shieldBuckler = new ItemBuilder(dxiimodMain.MOD_ID).setIcon(dxiimodMain.MOD_ID + ":item/buckler").build(new bucklerItem("BUCKLER", dxiimodMain.ITEM_ID++) );

		itemFlintlock = new ItemBuilder(dxiimodMain.MOD_ID).setIcon(dxiimodMain.MOD_ID + ":item/flintlock").setItemModel(item -> new itemModelGun(item, dxiimodMain.MOD_ID).setFull3D()).build(new ItemWeaponFlintlock("FLINTLOCK", dxiimodMain.ITEM_ID++) );

		fogRing = new ItemBuilder(dxiimodMain.MOD_ID).setIcon(dxiimodMain.MOD_ID + ":item/fogring").build(new baseAccessory("FOGRING", dxiimodMain.ITEM_ID++, EAccBonus.INVIS) );
		cloudBottle = new ItemBuilder(dxiimodMain.MOD_ID).setIcon(dxiimodMain.MOD_ID + ":item/cloudbottle").build(new baseAccessory("CLOUDBOTTLE", dxiimodMain.ITEM_ID++, EAccBonus.DOUBLEJUMP) );
		hornetRing = new ItemBuilder(dxiimodMain.MOD_ID).setIcon(dxiimodMain.MOD_ID + ":item/hornetring").build(new baseAccessory("HORNETRING", dxiimodMain.ITEM_ID++, EAccBonus.HORNETRING) );
		frogLeg = new ItemBuilder(dxiimodMain.MOD_ID).setIcon(dxiimodMain.MOD_ID + ":item/frogleg").build(new baseAccessory("FROGLEG", dxiimodMain.ITEM_ID++, EAccBonus.FROGLEG) );
		sacrificering_broken = new ItemBuilder(dxiimodMain.MOD_ID).setIcon(dxiimodMain.MOD_ID + ":item/sacrificering_broken").build(new baseAccessory("SACRIFICERINGBROKEN", dxiimodMain.ITEM_ID++, EAccBonus.SACRIFICE_BROKEN) );
		sacrificering = new ItemBuilder(dxiimodMain.MOD_ID).setIcon(dxiimodMain.MOD_ID + ":item/sacrificering").build(new baseAccessory("SACRIFICERING", dxiimodMain.ITEM_ID++, EAccBonus.SACRIFICE) );

		soulpeace = new ItemBuilder(dxiimodMain.MOD_ID).setIcon(dxiimodMain.MOD_ID + ":item/soulpeace").setItemModel(item -> new ItemModelStandard(item, dxiimodMain.MOD_ID).setFullBright()).build(new Item("SOULPEACE", dxiimodMain.ITEM_ID++) );
		soulpeace_great = new ItemBuilder(dxiimodMain.MOD_ID).setIcon(dxiimodMain.MOD_ID + ":item/soulpeace_great").setItemModel(item -> new ItemModelStandard(item, dxiimodMain.MOD_ID).setFullBright()).build(new Item("SOULPEACEGREAT", dxiimodMain.ITEM_ID++) );

		evileyering = new ItemBuilder(dxiimodMain.MOD_ID).setIcon(dxiimodMain.MOD_ID + ":item/evileyering").build(new baseAccessory("EVILEYERING", dxiimodMain.ITEM_ID++, EAccBonus.EVILEYE) );
		featherfall = new ItemBuilder(dxiimodMain.MOD_ID).setIcon(dxiimodMain.MOD_ID + ":item/featherfall").build(new baseAccessory("FEATHERFALL", dxiimodMain.ITEM_ID++, EAccBonus.FEATHERFALL) );
		cherrywoodring = new ItemBuilder(dxiimodMain.MOD_ID).setIcon(dxiimodMain.MOD_ID + ":item/cherrywoodring").build(new baseAccessory("CHERRYWOODRING", dxiimodMain.ITEM_ID++, EAccBonus.CHERRYWOOD) );
		rtsr = new ItemBuilder(dxiimodMain.MOD_ID).setIcon(dxiimodMain.MOD_ID + ":item/rtsr").build(new baseAccessory("RTSR", dxiimodMain.ITEM_ID++, EAccBonus.RTSR) );
		redeye = new ItemBuilder(dxiimodMain.MOD_ID).setIcon(dxiimodMain.MOD_ID + ":item/redeye").build(new baseAccessory("REDEYE", dxiimodMain.ITEM_ID++, EAccBonus.REDEYE) );

		soulevil = new ItemBuilder(dxiimodMain.MOD_ID).setIcon(dxiimodMain.MOD_ID + ":item/soulevil").setItemModel(item -> new ItemModelStandard(item, dxiimodMain.MOD_ID).setFullBright()).build(new Item("SOULEVIL", dxiimodMain.ITEM_ID++) );
		soulevil_great = new ItemBuilder(dxiimodMain.MOD_ID).setIcon(dxiimodMain.MOD_ID + ":item/soulevil_great").setItemModel(item -> new ItemModelStandard(item, dxiimodMain.MOD_ID).setFullBright()).build(new Item("SOULEVILGREAT", dxiimodMain.ITEM_ID++) );

		ghook = new ItemBuilder(dxiimodMain.MOD_ID).setIcon(dxiimodMain.MOD_ID + ":item/item_hook").setItemModel(item -> new ItemModelStandard(item, dxiimodMain.MOD_ID)).build(new graplingHook("GHOOK", dxiimodMain.ITEM_ID++) );

		dragoncrest = new ItemBuilder(dxiimodMain.MOD_ID).setIcon(dxiimodMain.MOD_ID + ":item/dragoncrestring").setItemModel(item -> new ItemModelStandard(item, dxiimodMain.MOD_ID)).build(new baseAccessory("DRAGONCREST", dxiimodMain.ITEM_ID++, EAccBonus.NOSTEPS) );
		cloranthyRing = new ItemBuilder(dxiimodMain.MOD_ID).setIcon(dxiimodMain.MOD_ID + ":item/cloranthyring").setItemModel(item -> new ItemModelStandard(item, dxiimodMain.MOD_ID)).build(new baseAccessory("CLORANTHY", dxiimodMain.ITEM_ID++, EAccBonus.CLORANTHY) );
		feralBone = new ItemBuilder(dxiimodMain.MOD_ID).setIcon(dxiimodMain.MOD_ID + ":item/feralbone").setItemModel(item -> new ItemModelStandard(item, dxiimodMain.MOD_ID)).build(new baseAccessory("FERALBONE", dxiimodMain.ITEM_ID++, EAccBonus.FERALBONE) );

		flameSpewer = new ItemBuilder(dxiimodMain.MOD_ID).setIcon(dxiimodMain.MOD_ID + ":item/flamespewer").setItemModel(item -> new ItemModelStandard(item, dxiimodMain.MOD_ID).setFull3D()).build(new ItemWeaponFlamespewer("FLAMESPEWER", dxiimodMain.ITEM_ID++) );

		bullet_iron = new ItemBuilder(dxiimodMain.MOD_ID).setIcon(dxiimodMain.MOD_ID + ":item/bullet_iron").setItemModel(item -> new ItemModelStandard(item, dxiimodMain.MOD_ID)).build(new Item("BULLETIRON", dxiimodMain.ITEM_ID++).setMaxStackSize(16) );
		bullet_steel = new ItemBuilder(dxiimodMain.MOD_ID).setIcon(dxiimodMain.MOD_ID + ":item/bullet_steel").setItemModel(item -> new ItemModelStandard(item, dxiimodMain.MOD_ID)).build(new Item("BULLETSTEEL", dxiimodMain.ITEM_ID++).setMaxStackSize(16) );
	}

}

