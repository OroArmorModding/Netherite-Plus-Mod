package com.oroarmor.netherite_plus.block;

import com.oroarmor.netherite_plus.screen.NetheriteAnvilScreenHandler;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.state.BlockState;

public class NetheriteAnvilBlock extends AnvilBlock {

    private static final TranslatableComponent TITLE = new TranslatableComponent("container.repair");

    public static BlockState damage(BlockState fallingState) {
        return fallingState;
    }

    public NetheriteAnvilBlock(Properties settings) {
        super(settings);
    }

    @Override
    public MenuProvider getMenuProvider(BlockState state, Level world, BlockPos pos) {
        return new SimpleMenuProvider((i, playerInventory, playerEntity) -> {
            return new NetheriteAnvilScreenHandler(i, playerInventory, ContainerLevelAccess.create(world, pos));
        }, TITLE);
    }
}
