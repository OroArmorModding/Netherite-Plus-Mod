package com.oroarmor.netherite_plus.advancement.criterion;

import static com.oroarmor.netherite_plus.NetheritePlusMod.id;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class RiptideNetheriteTridentCriterion extends SimpleCriterionTrigger<RiptideNetheriteTridentCriterion.Conditions> {
	public static final ResourceLocation id = id("riptide_netherite_trident");

	public static class Conditions extends AbstractCriterionTriggerInstance {

		public Conditions(EntityPredicate.Composite player) {
			super(id, player);
		}

		public boolean matches(ServerPlayer player) {
			return true;
		}

		@Override
		public JsonObject serializeToJson(SerializationContext predicateSerializer) {
			JsonObject jsonObject = super.serializeToJson(predicateSerializer);
			return jsonObject;
		}

	}

	@Override
	public ResourceLocation getId() {
		return id;
	}

	@Override
	public RiptideNetheriteTridentCriterion.Conditions createInstance(JsonObject jsonObject, EntityPredicate.Composite extended, DeserializationContext advancementEntityPredicateDeserializer) {
		return new RiptideNetheriteTridentCriterion.Conditions(extended);
	}

	public void trigger(ServerPlayer player) {
		trigger(player, (conditions) -> {
			return conditions.matches(player);
		});
	}
}
