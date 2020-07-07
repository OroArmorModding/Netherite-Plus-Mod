package com.oroarmor.netherite_elytra.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import com.mojang.authlib.GameProfile;
import com.oroarmor.netherite_elytra.NetheriteElytraMod;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.JumpingMount;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.MathHelper;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {

	@Shadow
	private int ticksSinceSprintingChanged;
	@Shadow
	private int ticksLeftToDoubleTapSprint;
	@Shadow
	private Input input;
	@Shadow
	private boolean field_23093;
	@Shadow
	private MinecraftClient client;
	@Shadow
	private int ticksToNextAutojump;
	@Shadow
	private ClientPlayNetworkHandler networkHandler;
	@Shadow
	private boolean field_3939;
	@Shadow
	private int underwaterVisibilityTicks;
	@Shadow
	private int field_3938;
	@Shadow
	private float field_3922;

	public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
		super(world, profile);
	}

	@Override
	@Overwrite
	public void tickMovement() {
		++ticksSinceSprintingChanged;
		if (ticksLeftToDoubleTapSprint > 0) {
			--ticksLeftToDoubleTapSprint;
		}

		updateNausea();
		boolean bl = input.jumping;
		boolean bl2 = input.sneaking;
		boolean bl3 = isWalking();
		field_23093 = !abilities.flying && !isSwimming()
				&& wouldPoseNotCollide(EntityPose.CROUCHING)
				&& (isSneaking() || !isSleeping() && !wouldPoseNotCollide(EntityPose.STANDING));
		input.tick(isHoldingSneakKey());
		client.getTutorialManager().onMovement(input);
		if (isUsingItem() && !hasVehicle()) {
			Input var10000 = input;
			var10000.movementSideways *= 0.2F;
			var10000 = input;
			var10000.movementForward *= 0.2F;
			ticksLeftToDoubleTapSprint = 0;
		}

		boolean bl4 = false;
		if (ticksToNextAutojump > 0) {
			--ticksToNextAutojump;
			bl4 = true;
			input.jumping = true;
		}

		if (!noClip) {
			pushOutOfBlocks(getX() - getWidth() * 0.35D, getY() + 0.5D,
					getZ() + getWidth() * 0.35D);
			pushOutOfBlocks(getX() - getWidth() * 0.35D, getY() + 0.5D,
					getZ() - getWidth() * 0.35D);
			pushOutOfBlocks(getX() + getWidth() * 0.35D, getY() + 0.5D,
					getZ() - getWidth() * 0.35D);
			pushOutOfBlocks(getX() + getWidth() * 0.35D, getY() + 0.5D,
					getZ() + getWidth() * 0.35D);
		}

		if (bl2) {
			ticksLeftToDoubleTapSprint = 0;
		}

		boolean bl5 = getHungerManager().getFoodLevel() > 6.0F || abilities.allowFlying;
		if ((onGround || isSubmergedInWater()) && !bl2 && !bl3 && isWalking() && !isSprinting()
				&& bl5 && !isUsingItem() && !hasStatusEffect(StatusEffects.BLINDNESS)) {
			if (ticksLeftToDoubleTapSprint <= 0 && !client.options.keySprint.isPressed()) {
				ticksLeftToDoubleTapSprint = 7;
			} else {
				setSprinting(true);
			}
		}

		if (!isSprinting() && (!isTouchingWater() || isSubmergedInWater()) && isWalking() && bl5
				&& !isUsingItem() && !hasStatusEffect(StatusEffects.BLINDNESS)
				&& client.options.keySprint.isPressed()) {
			setSprinting(true);
		}

		boolean bl8;
		if (isSprinting()) {
			bl8 = !input.hasForwardMovement() || !bl5;
			boolean bl7 = bl8 || horizontalCollision || isTouchingWater() && !isSubmergedInWater();
			if (isSwimming()) {
				if (!onGround && !input.sneaking && bl8 || !isTouchingWater()) {
					setSprinting(false);
				}
			} else if (bl7) {
				setSprinting(false);
			}
		}

		bl8 = false;
		if (abilities.allowFlying) {
			if (client.interactionManager.isFlyingLocked()) {
				if (!abilities.flying) {
					abilities.flying = true;
					bl8 = true;
					sendAbilitiesUpdate();
				}
			} else if (!bl && input.jumping && !bl4) {
				if (abilityResyncCountdown == 0) {
					abilityResyncCountdown = 7;
				} else if (!isSwimming()) {
					abilities.flying = !abilities.flying;
					bl8 = true;
					sendAbilitiesUpdate();
					abilityResyncCountdown = 0;
				}
			}
		}

		if (input.jumping && !bl8 && !bl && !abilities.flying && !hasVehicle() && !isClimbing()) {
			ItemStack itemStack = getEquippedStack(EquipmentSlot.CHEST);
			if (NetheriteElytraMod.isStackUsableAsElytra(itemStack) && checkFallFlying()) {
				networkHandler
						.sendPacket(new ClientCommandC2SPacket(this, ClientCommandC2SPacket.Mode.START_FALL_FLYING));
			}
		}

		field_3939 = isFallFlying();
		if (isTouchingWater() && input.sneaking && method_29920()) {
			knockDownwards();
		}

		int j;
		if (isSubmergedIn(FluidTags.WATER)) {
			j = isSpectator() ? 10 : 1;
			underwaterVisibilityTicks = MathHelper.clamp(underwaterVisibilityTicks + j, 0, 600);
		} else if (underwaterVisibilityTicks > 0) {
			isSubmergedIn(FluidTags.WATER);
			underwaterVisibilityTicks = MathHelper.clamp(underwaterVisibilityTicks - 10, 0, 600);
		}

		if (abilities.flying && isCamera()) {
			j = 0;
			if (input.sneaking) {
				--j;
			}

			if (input.jumping) {
				++j;
			}

			if (j != 0) {
				this.setVelocity(getVelocity().add(0.0D, j * abilities.getFlySpeed() * 3.0F, 0.0D));
			}
		}

		if (hasJumpingMount()) {
			JumpingMount jumpingMount = (JumpingMount) getVehicle();
			if (field_3938 < 0) {
				++field_3938;
				if (field_3938 == 0) {
					field_3922 = 0.0F;
				}
			}

			if (bl && !input.jumping) {
				field_3938 = -10;
				jumpingMount.setJumpStrength(MathHelper.floor(method_3151() * 100.0F));
				startRidingJump();
			} else if (!bl && input.jumping) {
				field_3938 = 0;
				field_3922 = 0.0F;
			} else if (bl) {
				++field_3938;
				if (field_3938 < 10) {
					field_3922 = field_3938 * 0.1F;
				} else {
					field_3922 = 0.8F + 2.0F / (field_3938 - 9) * 0.1F;
				}
			}
		} else {
			field_3922 = 0.0F;
		}

		super.tickMovement();
		if (onGround && abilities.flying && !client.interactionManager.isFlyingLocked()) {
			abilities.flying = false;
			sendAbilitiesUpdate();
		}

	}

	@Shadow
	protected abstract float method_3151();

	@Shadow
	protected abstract void startRidingJump();

	@Shadow
	protected abstract boolean hasJumpingMount();

	@Shadow
	protected abstract boolean isCamera();

	@Shadow
	protected abstract boolean isHoldingSneakKey();

	@Shadow
	protected abstract boolean isWalking();

	@Shadow
	protected abstract void updateNausea();
}
