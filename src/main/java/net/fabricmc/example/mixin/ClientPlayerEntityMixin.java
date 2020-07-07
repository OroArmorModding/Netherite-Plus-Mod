package net.fabricmc.example.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import com.mojang.authlib.GameProfile;

import net.fabricmc.example.NetheriteElytraMod;
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
		++this.ticksSinceSprintingChanged;
		if (this.ticksLeftToDoubleTapSprint > 0) {
			--this.ticksLeftToDoubleTapSprint;
		}

		this.updateNausea();
		boolean bl = this.input.jumping;
		boolean bl2 = this.input.sneaking;
		boolean bl3 = this.isWalking();
		this.field_23093 = !this.abilities.flying && !this.isSwimming()
				&& this.wouldPoseNotCollide(EntityPose.CROUCHING)
				&& (this.isSneaking() || !this.isSleeping() && !this.wouldPoseNotCollide(EntityPose.STANDING));
		this.input.tick(this.isHoldingSneakKey());
		this.client.getTutorialManager().onMovement(this.input);
		if (this.isUsingItem() && !this.hasVehicle()) {
			Input var10000 = this.input;
			var10000.movementSideways *= 0.2F;
			var10000 = this.input;
			var10000.movementForward *= 0.2F;
			this.ticksLeftToDoubleTapSprint = 0;
		}

		boolean bl4 = false;
		if (this.ticksToNextAutojump > 0) {
			--this.ticksToNextAutojump;
			bl4 = true;
			this.input.jumping = true;
		}

		if (!this.noClip) {
			this.pushOutOfBlocks(this.getX() - this.getWidth() * 0.35D, this.getY() + 0.5D,
					this.getZ() + this.getWidth() * 0.35D);
			this.pushOutOfBlocks(this.getX() - this.getWidth() * 0.35D, this.getY() + 0.5D,
					this.getZ() - this.getWidth() * 0.35D);
			this.pushOutOfBlocks(this.getX() + this.getWidth() * 0.35D, this.getY() + 0.5D,
					this.getZ() - this.getWidth() * 0.35D);
			this.pushOutOfBlocks(this.getX() + this.getWidth() * 0.35D, this.getY() + 0.5D,
					this.getZ() + this.getWidth() * 0.35D);
		}

		if (bl2) {
			this.ticksLeftToDoubleTapSprint = 0;
		}

		boolean bl5 = this.getHungerManager().getFoodLevel() > 6.0F || this.abilities.allowFlying;
		if ((this.onGround || this.isSubmergedInWater()) && !bl2 && !bl3 && this.isWalking() && !this.isSprinting()
				&& bl5 && !this.isUsingItem() && !this.hasStatusEffect(StatusEffects.BLINDNESS)) {
			if (this.ticksLeftToDoubleTapSprint <= 0 && !this.client.options.keySprint.isPressed()) {
				this.ticksLeftToDoubleTapSprint = 7;
			} else {
				this.setSprinting(true);
			}
		}

		if (!this.isSprinting() && (!this.isTouchingWater() || this.isSubmergedInWater()) && this.isWalking() && bl5
				&& !this.isUsingItem() && !this.hasStatusEffect(StatusEffects.BLINDNESS)
				&& this.client.options.keySprint.isPressed()) {
			this.setSprinting(true);
		}

		boolean bl8;
		if (this.isSprinting()) {
			bl8 = !this.input.hasForwardMovement() || !bl5;
			boolean bl7 = bl8 || this.horizontalCollision || this.isTouchingWater() && !this.isSubmergedInWater();
			if (this.isSwimming()) {
				if (!this.onGround && !this.input.sneaking && bl8 || !this.isTouchingWater()) {
					this.setSprinting(false);
				}
			} else if (bl7) {
				this.setSprinting(false);
			}
		}

		bl8 = false;
		if (this.abilities.allowFlying) {
			if (this.client.interactionManager.isFlyingLocked()) {
				if (!this.abilities.flying) {
					this.abilities.flying = true;
					bl8 = true;
					this.sendAbilitiesUpdate();
				}
			} else if (!bl && this.input.jumping && !bl4) {
				if (this.abilityResyncCountdown == 0) {
					this.abilityResyncCountdown = 7;
				} else if (!this.isSwimming()) {
					this.abilities.flying = !this.abilities.flying;
					bl8 = true;
					this.sendAbilitiesUpdate();
					this.abilityResyncCountdown = 0;
				}
			}
		}

		if (this.input.jumping && !bl8 && !bl && !this.abilities.flying && !this.hasVehicle() && !this.isClimbing()) {
			ItemStack itemStack = this.getEquippedStack(EquipmentSlot.CHEST);
			if (NetheriteElytraMod.isStackUsableAsElytra(itemStack) && this.checkFallFlying()) {
				this.networkHandler
						.sendPacket(new ClientCommandC2SPacket(this, ClientCommandC2SPacket.Mode.START_FALL_FLYING));
			}
		}

		this.field_3939 = this.isFallFlying();
		if (this.isTouchingWater() && this.input.sneaking && this.method_29920()) {
			this.knockDownwards();
		}

		int j;
		if (this.isSubmergedIn(FluidTags.WATER)) {
			j = this.isSpectator() ? 10 : 1;
			this.underwaterVisibilityTicks = MathHelper.clamp(this.underwaterVisibilityTicks + j, 0, 600);
		} else if (this.underwaterVisibilityTicks > 0) {
			this.isSubmergedIn(FluidTags.WATER);
			this.underwaterVisibilityTicks = MathHelper.clamp(this.underwaterVisibilityTicks - 10, 0, 600);
		}

		if (this.abilities.flying && this.isCamera()) {
			j = 0;
			if (this.input.sneaking) {
				--j;
			}

			if (this.input.jumping) {
				++j;
			}

			if (j != 0) {
				this.setVelocity(this.getVelocity().add(0.0D, j * this.abilities.getFlySpeed() * 3.0F, 0.0D));
			}
		}

		if (this.hasJumpingMount()) {
			JumpingMount jumpingMount = (JumpingMount) this.getVehicle();
			if (this.field_3938 < 0) {
				++this.field_3938;
				if (this.field_3938 == 0) {
					this.field_3922 = 0.0F;
				}
			}

			if (bl && !this.input.jumping) {
				this.field_3938 = -10;
				jumpingMount.setJumpStrength(MathHelper.floor(this.method_3151() * 100.0F));
				this.startRidingJump();
			} else if (!bl && this.input.jumping) {
				this.field_3938 = 0;
				this.field_3922 = 0.0F;
			} else if (bl) {
				++this.field_3938;
				if (this.field_3938 < 10) {
					this.field_3922 = this.field_3938 * 0.1F;
				} else {
					this.field_3922 = 0.8F + 2.0F / (this.field_3938 - 9) * 0.1F;
				}
			}
		} else {
			this.field_3922 = 0.0F;
		}

		super.tickMovement();
		if (this.onGround && this.abilities.flying && !this.client.interactionManager.isFlyingLocked()) {
			this.abilities.flying = false;
			this.sendAbilitiesUpdate();
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
