/*
 * Decompiled with CFR 0.2.1 (FabricMC 53fa44c9).
 */
package dxii.dxiimod.commands;

import dxii.dxiimod.interfaces.IReinforceable;
import dxii.dxiimod.item.itemWeaponBase;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.*;

public class ReinforceCommand
	extends Command {
	public ReinforceCommand() {
		super("reinforce", "upgrade");
	}

	@Override
	public boolean execute(CommandHandler handler, CommandSender sender, String[] args) {
		ItemStack stack = sender.getPlayer().getHeldItem();
		if (stack != null) {
				if (stack.getItem() instanceof itemWeaponBase) {
					IReinforceable weapon = ((IReinforceable) (Object) stack);

					if (args.length == 0 || args[0].equals("+")) {
						if (weapon.dxiimod$reinforceItem()) {
							handler.sendCommandFeedback(sender, "Reinforced held item to " + "+" + weapon.dxiimod$getReinforcement());
						} else {
							throw new CommandError("This weapon has reached it's max upgrade!");
						}

					} else if (args[0].equals("-")) {
						if (weapon.dxiimod$downgradeItem()) {
							handler.sendCommandFeedback(sender, "Downgraded item to " + "+" + weapon.dxiimod$getReinforcement());
						}else {
							throw new CommandError("Cant downgrade no more!");
						}
					}
				} else {
					throw new CommandError("This item can't be upgraded!");
				}
			}

		return true;
	}

	@Override
	public boolean opRequired(String[] args) {
		return true;
	}

	@Override
	public void sendCommandSyntax(CommandHandler handler, CommandSender sender) {
		if (sender instanceof PlayerCommandSender) {
			sender.sendMessage("/reinforce|upgrade <+/...> (to upgrade) / <-> (to downgrade)");
		}
	}
}

