/*
 * MIT License
 *
 * Copyright (c) 2021-2026 OroArmor (Eli Orona)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.oroarmor.netherite_plus.client.data.models;

import static com.oroarmor.netherite_plus.NetheritePlusMod.id;
import static com.oroarmor.netherite_plus.client.renderer.NetheritePlusSheets.colorToShulkerSprite;
import static net.minecraft.client.data.models.BlockModelGenerators.*;
import static net.minecraft.client.data.models.ItemModelGenerators.createFlatModelDispatch;
import static net.minecraft.client.data.models.model.TextureMapping.getBlockTexture;
import static net.minecraft.client.data.models.model.TexturedModel.createDefault;

import java.util.Optional;

import com.mojang.math.Transformation;
import com.oroarmor.netherite_plus.client.renderer.special.NetheriteShieldSpecialRenderer;
import com.oroarmor.netherite_plus.client.renderer.special.NetheriteTridentSpecialRenderer;
import com.oroarmor.netherite_plus.world.item.NetheritePlusItems;
import com.oroarmor.netherite_plus.world.level.block.NetheritePlusBlocks;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import org.jspecify.annotations.Nullable;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.client.renderer.blockentity.ShulkerBoxRenderer;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.special.ShulkerBoxSpecialRenderer;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ColorCollection;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class NetheritePlusModelProvider extends FabricModelProvider {
    public static final TextureSlot GLASS = TextureSlot.create("glass");
    public static final TextureSlot OBSIDIAN = TextureSlot.create("obsidian");
    public static final TextureSlot BEACON = TextureSlot.create("beacon");
    public static final TextureSlot BODY = TextureSlot.create("body");
    public static final ModelTemplate BEACON_TEMPLATE = new ModelTemplate(
            Optional.of(ModelLocationUtils.getModelLocation(Blocks.BEACON)),
            Optional.empty(),
            TextureSlot.PARTICLE,
            GLASS,
            OBSIDIAN,
            BEACON
    );
    public static final TexturedModel.Provider BEACON_PROIVDER = createDefault(block -> new TextureMapping()
                    .put(TextureSlot.PARTICLE, getBlockTexture(Blocks.GLASS))
                    .put(GLASS, getBlockTexture(Blocks.GLASS))
                    .put(OBSIDIAN, getBlockTexture(Blocks.NETHERITE_BLOCK))
                    .put(BEACON, getBlockTexture(block)),
            BEACON_TEMPLATE);
    public static final TexturedModel.Provider POWERED_BEACON_PROIVDER = createDefault(block -> new TextureMapping()
                    .put(TextureSlot.PARTICLE, getBlockTexture(Blocks.GLASS))
                    .put(GLASS, getBlockTexture(Blocks.GLASS))
                    .put(OBSIDIAN, getBlockTexture(Blocks.NETHERITE_BLOCK))
                    .put(BEACON, getBlockTexture(block, "_powered")),
            BEACON_TEMPLATE);
    public static final ModelTemplate ANVIL_TEMPLATE = new ModelTemplate(
            Optional.of(Identifier.withDefaultNamespace("block/template_anvil")),
            Optional.empty(),
            TextureSlot.TOP,
            BODY,
            TextureSlot.PARTICLE
    );
    public static final TexturedModel.Provider ANVIL_PROVIDER = createDefault(block -> new TextureMapping()
                    .put(TextureSlot.TOP, getBlockTexture(block, "_top"))
                    .put(BODY, getBlockTexture(block))
                    .put(TextureSlot.PARTICLE, getBlockTexture(block)),
            ANVIL_TEMPLATE
    );

    public NetheritePlusModelProvider(FabricPackOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockModelGenerators) {
        blockModelGenerators.createTrivialCube(NetheritePlusBlocks.FAKE_NETHERITE_BLOCK);
        createAnvil(NetheritePlusBlocks.NETHERITE_ANVIL, blockModelGenerators);

        var unpowered = plainVariant(BEACON_PROIVDER.create(NetheritePlusBlocks.NETHERITE_BEACON, blockModelGenerators.modelOutput));
        var powered = plainVariant(POWERED_BEACON_PROIVDER.createWithSuffix(NetheritePlusBlocks.NETHERITE_BEACON, "_powered", blockModelGenerators.modelOutput));

        blockModelGenerators.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(NetheritePlusBlocks.NETHERITE_BEACON)
                        .with(createBooleanModelDispatch(BlockStateProperties.POWERED, powered, unpowered))
        );

        createShulkerBox(blockModelGenerators, NetheritePlusBlocks.NETHERITE_SHULKER_BOX, null);
        ColorCollection.zipApply(NetheritePlusBlocks.DYED_NETHERITE_SHULKER_BOX, ColorCollection.VALUES, (block, dyeColor) -> createShulkerBox(blockModelGenerators, block, dyeColor));
    }

    public final void createAnvil(Block block, BlockModelGenerators blockModelGenerators) {
        MultiVariant anvilModel = plainVariant(ANVIL_PROVIDER.create(block, blockModelGenerators.modelOutput));
        blockModelGenerators.blockStateOutput.accept(createSimpleBlock(block, anvilModel).with(ROTATION_HORIZONTAL_FACING_ALT));
    }

    public final void createShulkerBox(BlockModelGenerators blockModelGenerators, final Block block, final @Nullable DyeColor color) {
        blockModelGenerators.createParticleOnlyBlock(block);
        Item item = block.asItem();
        Identifier baseModel = ModelTemplates.SHULKER_BOX_INVENTORY.create(item, TextureMapping.particle(block), blockModelGenerators.modelOutput);
        Transformation transformation = ShulkerBoxRenderer.modelTransform(Direction.UP);
        ItemModel.Unbaked itemModel = color != null
                ? ItemModelUtils.specialModel(baseModel, transformation, new ShulkerBoxSpecialRenderer.Unbaked(colorToShulkerSprite(color), 0.0f))
                : ItemModelUtils.specialModel(baseModel, transformation, new ShulkerBoxSpecialRenderer.Unbaked(id("netherite_shulker"), 0.0f));
        blockModelGenerators.itemModelOutput.accept(item, itemModel);
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerators) {
        itemModelGenerators.generateElytra(NetheritePlusItems.NETHERITE_ELYTRA);
        itemModelGenerators.generateFishingRod(NetheritePlusItems.NETHERITE_FISHING_ROD);
        this.generateShield(itemModelGenerators, NetheritePlusItems.NETHERITE_SHIELD);

        itemModelGenerators.createFlatItemModel(NetheritePlusItems.NETHERITE_BOW, ModelTemplates.BOW);
        itemModelGenerators.generateBow(NetheritePlusItems.NETHERITE_BOW);

        itemModelGenerators.createFlatItemModel(NetheritePlusItems.NETHERITE_CROSSBOW, ModelTemplates.CROSSBOW);
        itemModelGenerators.generateCrossbow(NetheritePlusItems.NETHERITE_CROSSBOW);

        this.generateTrident(itemModelGenerators, NetheritePlusItems.NETHERITE_TRIDENT);
        itemModelGenerators.generateFlatItem(NetheritePlusItems.NETHERITE_SHEARS, ModelTemplates.FLAT_ITEM);
    }

    public final void generateTrident(ItemModelGenerators itemModelGenerators, final Item item) {
        ItemModel.Unbaked flatModel = ItemModelUtils.plainModel(itemModelGenerators.createFlatItemModel(item, ModelTemplates.FLAT_ITEM));
        ItemModel.Unbaked inHandNormalModel = ItemModelUtils.specialModel(ModelLocationUtils.getModelLocation(Items.TRIDENT, "_in_hand"), new NetheriteTridentSpecialRenderer.Unbaked());
        ItemModel.Unbaked inHandThrowingModel = ItemModelUtils.specialModel(
                ModelLocationUtils.getModelLocation(Items.TRIDENT, "_throwing"), new NetheriteTridentSpecialRenderer.Unbaked()
        );
        ItemModel.Unbaked inHandModel = ItemModelUtils.conditional(
                NetheriteTridentSpecialRenderer.DEFAULT_TRANSFORMATION, ItemModelUtils.isUsingItem(), inHandThrowingModel, inHandNormalModel
        );
        itemModelGenerators.itemModelOutput.accept(item, createFlatModelDispatch(flatModel, inHandModel));
    }

    public final void generateShield(ItemModelGenerators itemModelGenerators, final Item item) {
        Identifier vanillaShieldModelLocation = ModelLocationUtils.getModelLocation(Items.SHIELD);
        Identifier modelLocation = ModelLocationUtils.getModelLocation(item);

        copyVanilla(itemModelGenerators, vanillaShieldModelLocation, modelLocation);
        copyVanilla(itemModelGenerators, vanillaShieldModelLocation.withSuffix("_blocking"), modelLocation.withSuffix("_blocking"));

        itemModelGenerators.itemModelOutput.accept(
                item,
                ItemModelUtils.conditional(NetheriteShieldSpecialRenderer.DEFAULT_TRANSFORMATION, ItemModelUtils.isUsingItem(),
                        ItemModelUtils.specialModel(modelLocation.withSuffix("_blocking"), NetheriteShieldSpecialRenderer.Unbaked.INSTANCE),
                        ItemModelUtils.specialModel(modelLocation, NetheriteShieldSpecialRenderer.Unbaked.INSTANCE)
                ));
    }

    private void copyVanilla(ItemModelGenerators itemModelGenerators, Identifier vanilla, Identifier output) {
        ModelTemplate blockingShieldTemplate = new ModelTemplate(
                Optional.of(vanilla),
                Optional.empty(),
                TextureSlot.PARTICLE
        );
        blockingShieldTemplate.create(
                output,
                TextureMapping.singleSlot(
                        TextureSlot.PARTICLE,
                        new Material(ModelLocationUtils.getModelLocation(Blocks.NETHERITE_BLOCK))
                ),
                itemModelGenerators.modelOutput
        );
    }
}
