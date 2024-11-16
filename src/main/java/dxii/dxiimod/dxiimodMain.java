package dxii.dxiimod;

import dxii.dxiimod.entity.*;
import dxii.dxiimod.entity.enemy.EnemyFogLurker;
import dxii.dxiimod.entity.enemy.EnemyFogLurker2;
import dxii.dxiimod.render.*;
import dxii.dxiimod.render.enemy.RenderFogLurker1;
import dxii.dxiimod.render.enemy.RenderFogLurker2;
import dxii.dxiimod.render.old.*;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.gui.options.components.KeyBindingComponent;
import net.minecraft.client.gui.options.components.OptionsCategory;
import net.minecraft.client.gui.options.components.ToggleableOptionComponent;
import net.minecraft.client.gui.options.data.OptionsPage;
import net.minecraft.client.gui.options.data.OptionsPages;
import net.minecraft.client.input.InputDeviceKeyboard;
import net.minecraft.client.option.GameSettings;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.RangeOption;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.entity.SpawnListEntry;
import net.minecraft.core.enums.EnumCreatureType;
import net.minecraft.core.world.biome.Biome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.helper.EntityHelper;
import turniplabs.halplibe.helper.SoundHelper;
import turniplabs.halplibe.util.ConfigHandler;
import turniplabs.halplibe.util.GameStartEntrypoint;
import turniplabs.halplibe.util.RecipeEntrypoint;

import java.util.Properties;


public class dxiimodMain implements ModInitializer, GameStartEntrypoint, RecipeEntrypoint {
    public static final String MOD_ID = "dxiimod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);


	public static OptionsPage optionsPage;
	public static RangeOption FogDist;
	public static KeyBinding keyDodge;

	public static int ITEM_ID;
	public static int ENTITY_ID;
	public static int THE_FOG;
	public static int MUST_BE_PLAYER;

	static {
		Properties props = new Properties();
		props.setProperty("starting_item_id", "19000");
		props.setProperty("starting_entity_id", "15000");
		props.setProperty("thefog", "1");
		props.setProperty("souls_only_from_player_kills", "1");
		ConfigHandler config = new ConfigHandler(dxiimodMain.MOD_ID, props);
		ITEM_ID = config.getInt("starting_item_id");
		ENTITY_ID = config.getInt("starting_entity_id");
		THE_FOG = config.getInt("thefog");
		MUST_BE_PLAYER = config.getInt("souls_only_from_player_kills");
		config.updateConfig();
	}

    @Override
    public void onInitialize() {
		LOGGER.debug("dxiimod is active!!!!!!");

		SoundHelper.addSound(MOD_ID, "foglurker_random1.ogg");
		SoundHelper.addSound(MOD_ID, "foglurker_random2.ogg");
		SoundHelper.addSound(MOD_ID, "foglurker_random3.ogg");
		SoundHelper.addSound(MOD_ID, "foglurker_random4.ogg");
		SoundHelper.addSound(MOD_ID, "foglurker_target.ogg");
		SoundHelper.addSound(MOD_ID, "parry.ogg");
		SoundHelper.addSound(MOD_ID, "ds2_parry.ogg");
		SoundHelper.addSound(MOD_ID, "shield_swing.ogg");
		SoundHelper.addSound(MOD_ID, "riposte.ogg");
		SoundHelper.addSound(MOD_ID, "shield_bash.ogg");
		SoundHelper.addSound(MOD_ID, "spear.ogg");
		SoundHelper.addSound(MOD_ID, "spear_large.ogg");
		SoundHelper.addSound(MOD_ID, "spear_large.ogg");
		SoundHelper.addSound(MOD_ID, "broadsword_swing.ogg");
		SoundHelper.addSound(MOD_ID, "greatsword_swing.ogg");
		SoundHelper.addSound(MOD_ID, "greatsword_impact.ogg");
		SoundHelper.addSound(MOD_ID, "swing_hammer.ogg");
		SoundHelper.addSound(MOD_ID, "swing_hammer_large.ogg");
		SoundHelper.addSound(MOD_ID, "throw.ogg");
		SoundHelper.addSound(MOD_ID, "molotov.ogg");
		SoundHelper.addSound(MOD_ID, "damage.ogg");
		SoundHelper.addSound(MOD_ID, "stab.ogg");
		SoundHelper.addSound(MOD_ID, "iron_stone.ogg");
		SoundHelper.addSound(MOD_ID, "break_ring.ogg");

		for(Biome b : Registries.BIOMES){
			b.getSpawnableList(EnumCreatureType.monster).add(new SpawnListEntry(EnemyFogLurker.class, 50));
			b.getSpawnableList(EnumCreatureType.monster).add(new SpawnListEntry(EnemyFogLurker2.class, 50));
		}
    }

	@Override
	public void beforeGameStart() {
		dxiimodItems.Initialize();

		EntityHelper.createEntity(EntityDagger.class, ENTITY_ID++, "dxiimod$stonedagger", RenderStoneDagger::new);
		EntityHelper.createEntity(EntityTomahawk.class, ENTITY_ID++, "dxiimod$tomahawk", RenderTomahawk::new);
		EntityHelper.createEntity(EnemyFogLurker.class, ENTITY_ID++, "dxiimod$foglurker1", RenderFogLurker1::new);
		EntityHelper.createEntity(EnemyFogLurker2.class, ENTITY_ID++, "dxiimod$foglurker2", RenderFogLurker2::new);
		EntityHelper.createEntity(EntityBullet.class, ENTITY_ID++, "dxiimod$bullet", RenderBullet::new);
		EntityHelper.createEntity(EntityHook.class, ENTITY_ID++, "dxiimod$hook", RenderHook::new);
		EntityHelper.createEntity(EntityFlameParticle.class, ENTITY_ID++, "dxiimod$fireParticle", RenderFlameParticle::new);
	}

	@Override
	public void afterGameStart() {
		optionsPage = new OptionsPage(MOD_ID+".options", dxiimodItems.suspiciousTotem.getDefaultStack());
		OptionsPages.register(optionsPage);

		optionsPage
			.withComponent(
				new OptionsCategory(MOD_ID + ".category.fog")
					.withComponent(new ToggleableOptionComponent<>(FogDist))
			.withComponent(
				new OptionsCategory(MOD_ID + ".category.keybinds")
					.withComponent(new KeyBindingComponent(keyDodge)) )
			);


	}

	public static void optionsInit(GameSettings gs){
		//credits to big sir for conveniently showing me how to implement mod's own settings :troll
		FogDist = new RangeOption(gs, MOD_ID+ ".fog", 1, 7);
		keyDodge = new KeyBinding(MOD_ID+ ".dodge").setDefault(InputDeviceKeyboard.keyboard, 56);

	}


	@Override
	public void onRecipesReady() {
		dxiimodRecipes.Initialize();
	}

	@Override
	public void initNamespaces() {
		dxiimodRecipes.InitNamespaces();
	}
}
