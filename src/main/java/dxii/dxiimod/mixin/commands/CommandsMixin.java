package dxii.dxiimod.mixin.commands;


import dxii.dxiimod.commands.ReinforceCommand;
import net.minecraft.core.net.command.Command;
import net.minecraft.core.net.command.Commands;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;


@Mixin(value = Commands.class, remap = false)
public class CommandsMixin {

//	@Unique
//	Commands thisObject = (Commands)(Object)this;

	@Shadow
	public static List<Command> commands = new ArrayList<Command>();

	@Inject(
		method = "net/minecraft/core/net/command/Commands.initCommands ()V",
		at = @At(value = "TAIL"
	))
	private static void addDxiimodCommands(CallbackInfo ci){
		commands.add(new ReinforceCommand());
	}
}
