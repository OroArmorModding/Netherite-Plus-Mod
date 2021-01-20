package com.oroarmor.netherite_plus.block;

import com.oroarmor.netherite_plus.screen.NetheriteAnvilScreenHandler;

import net.minecraft.block.AnvilBlock;
import net.minecraft.block.BlockState;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class NetheriteAnvilBlock extends AnvilBlock {
    private static final TranslatableText TITLE = new TranslatableText("container.repair");

    public NetheriteAnvilBlock(Settings settings) {
        super(settings);
    }

    public static BlockState getLandingState(BlockState fallingState) {
        return fallingState;
    }

    @Override
    public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        return new SimpleNamedScreenHandlerFactory((i, playerInventory, playerEntity) -> {
            return new NetheriteAnvilScreenHandler(i, playerInventory, ScreenHandlerContext.create(world, pos));
        }, TITLE);
    }
}
