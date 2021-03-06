/*
 * Copyright (C) 2013-2016 Gonçalo Baltazar <me@goncalomb.com>
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

package com.goncalomb.bukkit.nbteditor.nbt;

import org.bukkit.inventory.ItemStack;

import com.goncalomb.bukkit.mylib.reflect.NBTUtils;
import com.goncalomb.bukkit.nbteditor.nbt.variable.NBTGenericVariableContainer;
import com.goncalomb.bukkit.nbteditor.nbt.variable.ShortVariable;

public class DroppedItemNBT extends ItemNBT implements SingleItemBasedNBT {

	static {
		NBTGenericVariableContainer variables = new NBTGenericVariableContainer("DroppedItem");
		variables.add("Health", new ShortVariable("Health"));
		variables.add("PickupDelay", new ShortVariable("PickupDelay"));
		registerVariables(DroppedItemNBT.class, variables);
	}

	public void setItem(ItemStack item) {
		if (item == null) {
			_data.remove("Item");
		} else {
			_data.setCompound("Item", NBTUtils.itemStackToNBTData(item));
		}
	}

	public ItemStack getItem() {
		if (_data.hasKey("Item")) {
			return NBTUtils.itemStackFromNBTData(_data.getCompound("Item"));
		}
		return null;
	}

	public boolean isSet() {
		return _data.hasKey("Item");
	}

}
