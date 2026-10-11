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

package com.oroarmor.netherite_plus.mixin;

import java.util.Map;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import com.oroarmor.netherite_plus.world.level.block.NetheritePlusBlocks;

@Mixin(AnvilBlock.class)
public class AnvilBlockMixin {
	@Unique
	private static final Map<Direction, VoxelShape> NETHERITE_SHAPES = Shapes.rotateHorizontal(
			Shapes.or(
					Block.box(2.0, 0.0, 1.0, 14.0, 4.0, 12.0),
					Block.box(4.0, 4.0, 2.0, 12.0, 5.0, 11.0),
					Block.box(6.0, 5.0, 3.0, 10.0, 10.0, 10.0),
					Block.box(3.0, 10.0, 0.0, 13.0, 11.0, 13.0),
					Block.box(3.0, 11.0, 0.0, 13.0, 12.0, 14.0),
					Block.box(3.0, 12.0, 0.0, 13.0, 13.0, 15.0),
					Block.box(3.0, 13.0, 0.0, 13.0, 14.0, 16.0),
					Block.box(3.0, 14.0, 1.0, 13.0, 15.0, 16.0),
					Block.box(3.0, 15.0, 2.0, 13.0, 16.0, 16.0)
			)
	);
	@Shadow
	@Final
	public static EnumProperty<Direction> FACING;

	@Inject(method = "damage", at = @At("HEAD"), cancellable = true)
	private static void doNotDamageNetheriteAnvil(BlockState blockState, CallbackInfoReturnable<BlockState> cir) {
		if (blockState.is(NetheritePlusBlocks.NETHERITE_ANVIL)) {
			cir.setReturnValue(blockState);
		}
	}

	@Inject(method = "getShape", at = @At("HEAD"), cancellable = true)
	void useNetheriteAnvilShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context, CallbackInfoReturnable<VoxelShape> cir) {
		if (state.is(NetheritePlusBlocks.NETHERITE_ANVIL)) {
			cir.setReturnValue(
					NETHERITE_SHAPES.get(state
							.getValue(FACING)
							.getOpposite())
			);
		}
	}

	@ModifyArgs(method = "falling", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/item/FallingBlockEntity;setHurtsEntities(FI)V"))
	void increaseDamage(Args args, FallingBlockEntity entity) {
		if (entity
				.getBlockState()
				.is(NetheritePlusBlocks.NETHERITE_ANVIL)) {
			args.set(0, args.<Float>get(0) * 2.0f);
			args.set(1, args.<Integer>get(1) * 2);
		}
	}
}
