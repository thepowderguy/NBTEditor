package com.goncalomb.bukkit.nbteditor.nbt;

import java.util.Collection;
import java.util.HashMap;

import net.iharder.Base64;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import com.goncalomb.bukkit.EntityTypeMap;
import com.goncalomb.bukkit.nbteditor.nbt.variable.BlockVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variable.BooleanVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variable.ByteVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variable.FloatVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variable.IntegerVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variable.NBTGenericVariableContainer;
import com.goncalomb.bukkit.nbteditor.nbt.variable.NBTVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variable.NBTVariableContainer;
import com.goncalomb.bukkit.nbteditor.nbt.variable.ShortVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variable.StringVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variable.VectorVariable;
import com.goncalomb.bukkit.reflect.NBTTagCompoundWrapper;
import com.goncalomb.bukkit.reflect.NBTUtils;

public class EntityNBT {
	
	private static HashMap<EntityType, Class<? extends EntityNBT>> _entityClasses;
	private static HashMap<Class<? extends EntityNBT>, EntityType> _entityTypes;
	
	private EntityType _entityType;
	protected NBTTagCompoundWrapper _data;
	
	static {
		_entityClasses = new HashMap<EntityType, Class<? extends EntityNBT>>();
		_entityTypes = new HashMap<Class<? extends EntityNBT>, EntityType>();
		
		registerEntity(EntityType.FIREWORK, FireworkNBT.class, true);
		
		registerEntity(EntityType.PIG, BreedNBT.class);
		registerEntity(EntityType.SHEEP, BreedNBT.class);
		registerEntity(EntityType.COW, BreedNBT.class);
		registerEntity(EntityType.CHICKEN, BreedNBT.class);
		registerEntity(EntityType.MUSHROOM_COW, BreedNBT.class);
		registerEntity(EntityType.SQUID, MobNBT.class);
		
		registerEntity(EntityType.WOLF, TamedNBT.class);
		registerEntity(EntityType.OCELOT, TamedNBT.class);
		
		registerEntity(EntityType.VILLAGER, VillagerNBT.class);
		registerEntity(EntityType.IRON_GOLEM, MobNBT.class);
		registerEntity(EntityType.SNOWMAN, MobNBT.class);
		
		registerEntity(EntityType.ZOMBIE, ZombieNBT.class);
		registerEntity(EntityType.PIG_ZOMBIE, ZombieNBT.class);
		registerEntity(EntityType.SLIME, SlimeNBT.class);
		registerEntity(EntityType.MAGMA_CUBE, SlimeNBT.class);
		registerEntity(EntityType.GHAST, MobNBT.class);
		registerEntity(EntityType.SKELETON, MobNBT.class);
		registerEntity(EntityType.CREEPER, MobNBT.class);
		registerEntity(EntityType.BAT, MobNBT.class);
		registerEntity(EntityType.BLAZE, MobNBT.class);
		registerEntity(EntityType.SPIDER, MobNBT.class);
		registerEntity(EntityType.CAVE_SPIDER, MobNBT.class);
		registerEntity(EntityType.GIANT, MobNBT.class);
		registerEntity(EntityType.ENDERMAN, MobNBT.class);
		registerEntity(EntityType.SILVERFISH, MobNBT.class);
		registerEntity(EntityType.WITCH, MobNBT.class);

		registerEntity(EntityType.ENDER_DRAGON, MobNBT.class);
		registerEntity(EntityType.WITHER, MobNBT.class);
		
		registerEntity(EntityType.PRIMED_TNT, EntityNBT.class);
		registerEntity(EntityType.FALLING_BLOCK, FallingBlockNBT.class);
		registerEntity(EntityType.DROPPED_ITEM, DroppedItemNBT.class);
		registerEntity(EntityType.EXPERIENCE_ORB, XPOrbNBT.class);
		
		registerEntity(EntityType.SPLASH_POTION, ThrownPotionNBT.class);
		
		
		NBTGenericVariableContainer variables = null;
		
		variables = new NBTGenericVariableContainer("Entity");
		variables.add("pos", new VectorVariable("Pos"));
		variables.add("vel", new VectorVariable("Motion"));
		variables.add("fall-distance", new FloatVariable("FallDistance", 0.0f));
		variables.add("fire", new ShortVariable("Fire"));
		variables.add("air", new ShortVariable("Air", (short) 0, (short) 200));
		variables.add("invulnerable", new BooleanVariable("Invulnerable"));
		variables.add("name", new StringVariable("CustomName"));
		EntityNBTVariableManager.registerVariables(EntityNBT.class, variables);
		
		variables = new NBTGenericVariableContainer("Pig");
		variables.add("saddle", new BooleanVariable("Saddle"));
		EntityNBTVariableManager.registerVariables(EntityType.PIG, variables);
		
		variables = new NBTGenericVariableContainer("Sheep");
		variables.add("sheared", new BooleanVariable("Saddle"));
		variables.add("color", new ByteVariable("Color", (byte) 0, (byte) 15));
		EntityNBTVariableManager.registerVariables(EntityType.SHEEP, variables);
		
		
		variables = new NBTGenericVariableContainer("Wolf");
		variables.add("angry", new BooleanVariable("Angry"));
		variables.add("collar-color", new ByteVariable("CollarColor", (byte) 0, (byte) 15));
		EntityNBTVariableManager.registerVariables(EntityType.WOLF, variables);
		
		variables = new NBTGenericVariableContainer("Ocelot");
		variables.add("cat-type", new IntegerVariable("CatType", 0, 3));
		EntityNBTVariableManager.registerVariables(EntityType.OCELOT, variables);
		
		
		variables = new NBTGenericVariableContainer("IronGolem");
		variables.add("player-created", new BooleanVariable("PlayerCreated"));
		EntityNBTVariableManager.registerVariables(EntityType.IRON_GOLEM, variables);
		
		
		variables = new NBTGenericVariableContainer("PigZombie");
		variables.add("anger", new ShortVariable("Anger"));
		EntityNBTVariableManager.registerVariables(EntityType.PIG_ZOMBIE, variables);
		
		variables = new NBTGenericVariableContainer("Ghast");
		variables.add("explosion-power", new IntegerVariable("ExplosionPower", 0, 25)); // Limited to 25
		EntityNBTVariableManager.registerVariables(EntityType.GHAST, variables);
		
		variables = new NBTGenericVariableContainer("Skeleton");
		variables.add("is-wither", new BooleanVariable("SkeletonType"));
		EntityNBTVariableManager.registerVariables(EntityType.SKELETON, variables);
		
		variables = new NBTGenericVariableContainer("Creeper");
		variables.add("powered", new BooleanVariable("powered"));
		variables.add("explosion-radius", new ByteVariable("ExplosionRadius", (byte) 0, (byte) 25)); // Limited to 25
		variables.add("fuse", new ShortVariable("Fuse", (short) 0));
		EntityNBTVariableManager.registerVariables(EntityType.CREEPER, variables);
		
		variables = new NBTGenericVariableContainer("Enderman");
		variables.add("block", new BlockVariable("carried", "carriedData", true));
		EntityNBTVariableManager.registerVariables(EntityType.ENDERMAN, variables);
		
		
		variables = new NBTGenericVariableContainer("Wither");
		variables.add("invul-time", new IntegerVariable("Invul", 0));
		EntityNBTVariableManager.registerVariables(EntityType.WITHER, variables);
		
		
		variables = new NBTGenericVariableContainer("PrimedTNT");
		variables.add("fuse", new ByteVariable("Fuse", (byte) 0));
		EntityNBTVariableManager.registerVariables(EntityType.PRIMED_TNT, variables);
	}
	
	private static void registerEntity(EntityType entityType, Class<? extends EntityNBT> entityClass) {
		registerEntity(entityType, entityClass, false);
	}
	
	private static void registerEntity(EntityType entityType, Class<? extends EntityNBT> entityClass, boolean dontInstantiate) {
		if(!dontInstantiate) {
			_entityClasses.put(entityType, entityClass);
		}
		_entityTypes.put(entityClass, entityType);
	}
	
	private static EntityNBT fromEntityType(EntityType entityType, NBTTagCompoundWrapper data) {
		Class<? extends EntityNBT> entityClass = _entityClasses.get(entityType);
		EntityNBT instance;
		try {
			instance = entityClass.newInstance();
		} catch (Exception e) {
			throw new Error("Error when instantiating " + entityClass.getName() + ".", e);
		}
		instance._entityType = entityType;
		instance._data = data;
		return instance;
	}
	
	public static boolean isValidType(EntityType entityType) {
		return _entityClasses.containsKey(entityType);
	}
	
	public static Collection<EntityType> getValidEntityTypes() {
		return _entityClasses.keySet();
	}
	
	public static EntityNBT fromEntityType(EntityType entityType) {
		if (!isValidType(entityType)) throw new IllegalArgumentException("Invalid argument entityType, " + entityType.getName() + ".");
		return fromEntityType(entityType, new NBTTagCompoundWrapper());
	}
	
	static EntityNBT fromAnyEntityType(EntityType entityType) {
		return fromAnyEntityType(entityType, new NBTTagCompoundWrapper());
	}
	
	static EntityNBT fromAnyEntityType(EntityType entityType, NBTTagCompoundWrapper data) {
		if (_entityClasses.containsKey(entityType)) {
			return fromEntityType(entityType, data);
		} else {
			return new EntityNBT(entityType, data);
		}
	}
	
	public static EntityNBT fromEntity(Entity entity) {
		if (!_entityClasses.containsKey(entity.getType())) throw new IllegalArgumentException("Invalid argument entity.");
		return fromEntityType(entity.getType(), NBTUtils.getEntityNBTTagCompound(entity));
	}
	
	protected EntityNBT() {
		_entityType = _entityTypes.get(this.getClass());
		_data = new NBTTagCompoundWrapper();
	}
	
	protected EntityNBT(EntityType entityType) {
		_entityType = entityType;
		_data = new NBTTagCompoundWrapper();
	}
	
	private EntityNBT(EntityType entityType, NBTTagCompoundWrapper data) {
		_entityType = entityType;
		_data = data;
	}
	
	public void setPos(double x, double y, double z) {
		_data.setList("Pos", x, y, z);
	}
	
	public void removePos() {
		_data.remove("Pos");
	}
	
	public void setMotion(double x, double y, double z) {
		_data.setList("Motion", x, y, z);
	}
	
	public void removeMotion() {
		_data.remove("Motion");
	}
	
	public EntityType getEntityType() {
		return _entityType;
	}
	
	public Entity spawn(Location location) {
		Entity entity = location.getWorld().spawnEntity(location, _entityType);
		NBTUtils.setEntityNBTTagCompound(entity, _data);
		return entity;
	}
	
	public NBTVariableContainer[] getAllVariables() {
		return EntityNBTVariableManager.getAllVariables(this);
	}
	
	public NBTVariable getVariable(String name) {
		return EntityNBTVariableManager.getVariable(this, name);
	}
	
	public String serialize() {
		try {
			return EntityTypeMap.getName(_entityType) + "," + Base64.encodeBytes(_data.serialize(), Base64.GZIP);
		} catch (Throwable e) {
			throw new Error("Error serializing EntityNBT.", e);
		}
	}
	
	public static EntityNBT unserialize(String serializedData) {
		try {
			int i = serializedData.indexOf(',');
			EntityType entityType = EntityTypeMap.getByName(serializedData.substring(0, i));
			return fromEntityType(entityType, NBTTagCompoundWrapper.unserialize(Base64.decode(serializedData.substring(i + 1))));
		} catch (Throwable e) {
			throw new Error("Error unserializing EntityNBT.", e);
		}
	}
	
	public EntityNBT clone() {
		return new EntityNBT(_entityType, _data.clone());
	}
}