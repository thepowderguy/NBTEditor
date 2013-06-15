package com.goncalomb.bukkit.nbteditor.nbt;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import com.goncalomb.bukkit.reflect.NBTTagCompoundWrapper;
import com.goncalomb.bukkit.reflect.NBTUtils;

public class TileNBTWrapper {
	
	protected Block _block;
	protected NBTTagCompoundWrapper _data;
	
	public static final boolean allowsCustomName(Material mat) {
		return (mat == Material.CHEST || mat == Material.FURNACE
				|| mat == Material.DISPENSER || mat == Material.DROPPER
				|| mat == Material.HOPPER || mat == Material.BREWING_STAND
				|| mat == Material.ENCHANTMENT_TABLE || mat == Material.COMMAND);
	}
	
	public TileNBTWrapper(Block block) {
		_block = block;
		_data = NBTUtils.getTileEntityNBTTagCompound(_block);
	}
	
	public final boolean allowsCustomName() {
		return allowsCustomName(_block.getType());
	}
	
	public final void setCustomName(String name) {
		if (allowsCustomName()) {
			if (name == null) {
				_data.setString("CustomName", "");
			} else {
				_data.setString("CustomName", name);
			}
		}
	}
	
	public final String getCustomName() {
		return (allowsCustomName() ? _data.getString("CustomName") : null);
	}
	
	public final Location getLocation() {
		return _block.getLocation();
	}
	
	public void save() {
		NBTUtils.setTileEntityNBTTagCompound(_block, _data);
	}
	
}
