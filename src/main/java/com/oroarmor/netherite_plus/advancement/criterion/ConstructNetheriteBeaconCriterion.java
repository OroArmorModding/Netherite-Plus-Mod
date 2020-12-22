package com.oroarmor.netherite_plus.advancement.criterion;

import static com.oroarmor.netherite_plus.NetheritePlusMod.id;

import com.google.gson.JsonObject;
import com.oroarmor.netherite_plus.block.entity.NetheriteBeaconBlockEntity;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class ConstructNetheriteBeaconCriterion extends SimpleCriterionTrigger<ConstructNetheriteBeaconCriterion.Conditions> {
	private static final ResourceLocation ID = id("construct_netherite_beacon");

	@Override
	public ResourceLocation getId() {
		return ID;
	}

	@Override
	public ConstructNetheriteBeaconCriterion.Conditions createInstance(JsonObject jsonObject, EntityPredicate.Composite extended, DeserializationContext advancementEntityPredicateDeserializer) {
		MinMaxBounds.Ints intRange = MinMaxBounds.Ints.fromJson(jsonObject.get("level"));
		return new ConstructNetheriteBeaconCriterion.Conditions(extended, intRange);
	}

	public void trigger(ServerPlayer player, NetheriteBeaconBlockEntity beacon) {
		trigger(player, (conditions) -> {
			return conditions.matches(beacon);
		});
	}

	public static class Conditions extends AbstractCriterionTriggerInstance {
		private final MinMaxBounds.Ints level;

		public Conditions(EntityPredicate.Composite player, MinMaxBounds.Ints level) {
			super(ConstructNetheriteBeaconCriterion.ID, player);
			this.level = level;
		}

		public static ConstructNetheriteBeaconCriterion.Conditions level(MinMaxBounds.Ints level) {
			return new ConstructNetheriteBeaconCriterion.Conditions(EntityPredicate.Composite.ANY, level);
		}

		public boolean matches(NetheriteBeaconBlockEntity beacon) {
			return level.matches(beacon.getBeaconLevel());
		}

		@Override
		public JsonObject serializeToJson(SerializationContext predicateSerializer) {
			JsonObject jsonObject = super.serializeToJson(predicateSerializer);
			jsonObject.add("level", level.serializeToJson());
			return jsonObject;
		}
	}
}
