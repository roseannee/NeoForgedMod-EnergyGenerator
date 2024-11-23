package com.toremuyu.energygenerator.custom.tiles.util;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.energy.EnergyStorage;

public class CustomEnergyStorage extends EnergyStorage {
    private final BlockEntity be;

    public CustomEnergyStorage(BlockEntity be, int capacity, int maxReceive, int maxExtract, int energy) {
        super(capacity, maxReceive, maxExtract, energy);
        this.be = be;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        this.be.setChanged();
        return super.extractEnergy(maxExtract, simulate);
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        this.be.setChanged();
        return super.receiveEnergy(maxReceive, simulate);
    }

    public void setEnergy(int energy) {
        this.energy = Math.max(0, Math.min(energy, this.capacity));
    }

    public void addEnergy(int energy) {
        setEnergy(this.energy + energy);
    }
}
