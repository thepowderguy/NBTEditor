/*
 * Copyright (C) 2013-2015 Gonçalo Baltazar <me@goncalomb.com>
 *
 * This file is part of NBTEditor.
 *
 * NBTEditor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * NBTEditor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NBTEditor.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.goncalomb.bukkit.bkglib.bkgcommand;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import com.goncalomb.bukkit.bkglib.reflect.BukkitReflect;

public final class BKgCommandManager {

	public static void register(BKgCommand command, Plugin plugin) {
		if (command.getOwner() != null) {
			throw new RuntimeException("Command " + command.getName() + " already registered by " + command.getOwner().getName() + ".");
		}
		// Process methods.
		HashMap<String, Method> tabMethods = new HashMap<String, Method>();
		Method[] methods = command.getClass().getDeclaredMethods();
		// Find all tab completion methods.
		for (Method method : methods) {
			BKgCommand.TabComplete config = method.getAnnotation(BKgCommand.TabComplete.class);
			if (config != null) {
				// Verify parameter types.
				Class<?>[] params = method.getParameterTypes();
				if (params.length == 2 && method.getReturnType() == List.class && params[0] == CommandSender.class && params[1] == String[].class) {
					tabMethods.put(config.args().trim().toLowerCase(), method);
				} else {
					throw new RuntimeException("Invalid command tab completion method " + method.getName() + " on class " + command.getClass().getName() + ".");
				}
			}
		}
		// Find all execution methods.
		for (Method method : methods) {
			BKgCommand.Command config = method.getAnnotation(BKgCommand.Command.class);
			if (config != null) {
				// Verify parameter types.
				Class<?>[] params = method.getParameterTypes();
				if (params.length == 2 && method.getReturnType() == boolean.class && params[0] == CommandSender.class && params[1] == String[].class) {
					String argsString = config.args().trim().toLowerCase();
					String[] args = argsString.split("\\s+");
					args = (args.length == 1 && args[0].isEmpty() ? new String[0] : args);
					// Register the (Sub-)Command.
					if(!command.addSubCommand(args, 0, config, command, method, tabMethods.remove(argsString))) {
						throw new RuntimeException("(Sub-)Command '" + command.getName() + " " + argsString + "' already registered.");
					}
					continue;
				}
				throw new RuntimeException("Invalid command execution method " + method.getName() + " on class " + command.getClass().getName() + ".");
			}
		}
		if (tabMethods.size() > 0) {
			throw new RuntimeException("Tab completion method " + tabMethods.get(0).getName() + " on class " + command.getClass().getName() + " has no execution method.");
		}
		command.setup(BukkitReflect.getCommandMap(), plugin);
	}
	
	// Bukkit is an asshole, getCommands() now returns UnmodifiableCollection.
	/*
	public static void unregisterAll(SimpleCommandMap commandMap, Plugin plugin) {
		for (Iterator<Command> it = commandMap.getCommands().iterator(); it.hasNext(); ) {
			Command command = it.next();
			if (command instanceof InternalCommand && ((InternalCommand) command).getOwner() == plugin) {
				((InternalCommand) command).getCommand().removePermissions();
				it.remove();
			}
		}
	}
	*/
	
}