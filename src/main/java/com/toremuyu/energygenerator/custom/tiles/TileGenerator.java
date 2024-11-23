package com.toremuyu.energygenerator.custom.tiles;

import com.google.common.base.Predicates;
import com.toremuyu.energygenerator.custom.tiles.util.CustomEnergyStorage;
import com.toremuyu.energygenerator.init.TilesEG;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.tiles.TileSyncableTickable;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.toremuyu.energygenerator.custom.blocks.BlockGenerator.SENDING;

public class TileGenerator extends TileSyncableTickable {
    private static final int MAX_ENERGY = 16000;
    private static final int MAX_EXTRACT = 100;
    private static final int DETECTION_RADIUS = 8;

    private final CustomEnergyStorage energy = new CustomEnergyStorage(this, MAX_ENERGY, 0, MAX_EXTRACT, 0);
    private final LazyOptional<CustomEnergyStorage> energyOptional = LazyOptional.of(() -> energy);

    public TileGenerator(BlockPos pos, BlockState state) {
        super(TilesEG.GENERATOR, pos, state);
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state, BlockEntity be) {
        if (this.level == null || this.level.isClientSide()) return;

        if (this.energy.getEnergyStored() != this.energy.getMaxEnergyStored()) {
            detectLightning();
        }

        outputEnergy();

        super.tick(level, pos, state, be);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ENERGY) {
            return this.energyOptional.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.energyOptional.invalidate();
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        nbt.putInt("generator.energy", this.energy.getEnergyStored());

        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.energy.setEnergy(nbt.getInt("generator.energy"));
    }

    private void detectLightning() {
        if (this.level == null) return;

        List<Entity> entities = level.getEntitiesOfClass(
                Entity.class,
                getDetectionBox(),
                Predicates.instanceOf(LightningBolt.class));

        for (Entity entity : entities) {
            if (entity instanceof LightningBolt) {
                if (!entity.getTags().contains("energy_collected")) {
                    entity.addTag("energy_collected");

                    double distance = entity.position().distanceTo(Vec3.atCenterOf(this.getBlockPos()));
                    double energyMultiplier = Math.max(0, (DETECTION_RADIUS - distance) / DETECTION_RADIUS);
                    int generatedEnergy = (int) (MAX_ENERGY * energyMultiplier);
                    this.energy.addEnergy(generatedEnergy);

                    sendUpdate();
                    break;
                }
            }

        }
    }

    private void outputEnergy() {
        if (this.level == null) return;

        AtomicBoolean isSending = new AtomicBoolean(false);

        for (Direction direction : Direction.values()) {
            BlockEntity be = this.level.getBlockEntity(this.worldPosition.relative(direction));

            if (be == null || be instanceof TileGenerator) continue;

            be.getCapability(ForgeCapabilities.ENERGY, direction.getOpposite()).ifPresent(storage -> {
                if (storage.getEnergyStored() < storage.getMaxEnergyStored()) {
                    int toSend = this.energy.extractEnergy(MAX_EXTRACT, false);
                    int received = storage.receiveEnergy(toSend, false);

                    this.energy.setEnergy(this.energy.getEnergyStored() + toSend - received);

                    if (toSend > 0) {
                        isSending.set(true);
                    }
                }
            });


        }

        BlockState state = this.level.getBlockState(this.worldPosition);
        if (state.getValue(SENDING) != isSending.get()) {
            this.level.setBlock(this.worldPosition, state.setValue(SENDING, isSending.get()), 3);
            sendUpdate();
        }
    }

    private AABB getDetectionBox() {
        return new AABB(
                getBlockPos().offset(-DETECTION_RADIUS, -DETECTION_RADIUS, -DETECTION_RADIUS),
                getBlockPos().offset(DETECTION_RADIUS, DETECTION_RADIUS, DETECTION_RADIUS));
    }

    public CustomEnergyStorage getEnergyStorage() {
        return this.energy;
    }

    private void sendUpdate() {
        setChanged();

        if (this.level != null) {
            this.level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    }
}
