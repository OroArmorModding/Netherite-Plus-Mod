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

public class FullNetheriteNetheriteBeaconCriterion extends SimpleCriterionTrigger<FullNetheriteNetheriteBeaconCriterion.Conditions> {

    public static final ResourceLocation id = id("full_netherite_netherite_beacon");

    public static class Conditions extends AbstractCriterionTriggerInstance {

        private final MinMaxBounds.Ints netheriteLevel;

        public Conditions(EntityPredicate.Composite player, MinMaxBounds.Ints netheriteLevel) {
            super(id, player);
            this.netheriteLevel = netheriteLevel;
        }

        public static FullNetheriteNetheriteBeaconCriterion.Conditions level(MinMaxBounds.Ints netheriteLevel) {
            return new FullNetheriteNetheriteBeaconCriterion.Conditions(EntityPredicate.Composite.ANY, netheriteLevel);
        }

        public boolean matches(NetheriteBeaconBlockEntity beacon) {
            System.out.println(netheriteLevel.serializeToJson());
            return netheriteLevel.matches(beacon.getNetheriteLevel());
        }

        @Override
        public JsonObject serializeToJson(SerializationContext predicateSerializer) {
            JsonObject jsonObject = super.serializeToJson(predicateSerializer);
            jsonObject.add("netherite_level", netheriteLevel.serializeToJson());
            return jsonObject;
        }

    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public FullNetheriteNetheriteBeaconCriterion.Conditions createInstance(JsonObject jsonObject, EntityPredicate.Composite extended, DeserializationContext advancementEntityPredicateDeserializer) {
        MinMaxBounds.Ints intRange = MinMaxBounds.Ints.fromJson(jsonObject.get("netherite_level"));
        return new FullNetheriteNetheriteBeaconCriterion.Conditions(extended, intRange);
    }

    public void trigger(ServerPlayer player, NetheriteBeaconBlockEntity beacon) {
        trigger(player, (conditions) -> {
            return conditions.matches(beacon);
        });
    }

}
