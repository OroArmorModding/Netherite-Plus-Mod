package com.oroarmor.netherite_plus.advancement.criterion;

import com.google.gson.JsonObject;
import com.oroarmor.netherite_plus.block.entity.NetheriteBeaconBlockEntity;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import static com.oroarmor.netherite_plus.NetheritePlusMod.id;

public class FullNetheriteNetheriteBeaconCriterion extends AbstractCriterion<FullNetheriteNetheriteBeaconCriterion.Conditions> {
	public static final Identifier id = id("full_netherite_netherite_beacon");

	@Override
	public Identifier getId() {
		return id;
	}

	@Override
	public FullNetheriteNetheriteBeaconCriterion.Conditions conditionsFromJson(JsonObject jsonObject, EntityPredicate.Extended extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
		NumberRange.IntRange intRange = NumberRange.IntRange.fromJson(jsonObject.get("netherite_level"));
		return new FullNetheriteNetheriteBeaconCriterion.Conditions(extended, intRange);
	}

	public void trigger(ServerPlayerEntity player, NetheriteBeaconBlockEntity beacon) {
		test(player, (conditions) -> {
			return conditions.matches(beacon);
		});
	}

	public static class Conditions extends AbstractCriterionConditions {

		private final NumberRange.IntRange netheriteLevel;

		public Conditions(EntityPredicate.Extended player, NumberRange.IntRange netheriteLevel) {
			super(id, player);
			this.netheriteLevel = netheriteLevel;
		}

		public static FullNetheriteNetheriteBeaconCriterion.Conditions level(NumberRange.IntRange netheriteLevel) {
			return new FullNetheriteNetheriteBeaconCriterion.Conditions(EntityPredicate.Extended.EMPTY, netheriteLevel);
		}

		public boolean matches(NetheriteBeaconBlockEntity beacon) {
			System.out.println(netheriteLevel.toJson());
			return netheriteLevel.test(beacon.getNetheriteLevel());
		}

		@Override
		public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
			JsonObject jsonObject = super.toJson(predicateSerializer);
			jsonObject.add("netherite_level", netheriteLevel.toJson());
			return jsonObject;
		}

	}

}
