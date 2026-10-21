package org.github.melodiccougar7.immersivefixes.util;

import blusunrize.immersiveengineering.api.energy.NullEnergyStorage;
import blusunrize.immersiveengineering.api.energy.WrappingEnergyStorage;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IMultiblockContext;
import blusunrize.immersiveengineering.common.blocks.metal.CapacitorCreativeBlockEntity;
import blusunrize.immersiveengineering.common.blocks.multiblocks.blockimpl.MultiblockContext;
import blusunrize.immersiveengineering.common.blocks.multiblocks.blockimpl.WrappingMultiblockContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;

public final class ChunkDirty {
    private ChunkDirty() {}

    public static void mark(BlockEntity be) {
        Level level = be.getLevel();
        if (level == null || level.isClientSide) return;
        if (be instanceof Stamped stamped) {
            long tick = level.getGameTime();
            if (stamped.immersiveFixes$getDirtyTick() == tick) return;
            stamped.immersiveFixes$setDirtyTick(tick);
        }
        level.blockEntityChanged(be.getBlockPos());
    }

    public static void mark(IMultiblockContext<?> ctx) {
        IMultiblockContext<?> inner = ctx;
        while (inner instanceof WrappingMultiblockContext<?> wrapping) inner = wrapping.inner();
        if (inner instanceof MultiblockContext<?> context) mark(context.masterHelper().getMasterBE());
        else ctx.markMasterDirty();
    }

    public static int extractEnergy(BlockEntity be, IEnergyStorage storage, int maxExtract, boolean simulate) {
        int extracted = storage.extractEnergy(maxExtract, simulate);
        if (!simulate && extracted > 0) mark(be);
        return extracted;
    }

    public static int extractEnergy(IMultiblockContext<?> ctx, IEnergyStorage storage, int maxExtract, boolean simulate) {
        int extracted = storage.extractEnergy(maxExtract, simulate);
        if (!simulate && extracted > 0) mark(ctx);
        return extracted;
    }

    public static int fill(BlockEntity be, FluidTank tank, FluidStack resource, IFluidHandler.FluidAction action) {
        int filled = tank.fill(resource, action);
        if (action.execute() && filled > 0) mark(be);
        return filled;
    }

    public static FluidStack drain(BlockEntity be, FluidTank tank, int maxDrain, IFluidHandler.FluidAction action) {
        FluidStack drained = tank.drain(maxDrain, action);
        if (action.execute() && !drained.isEmpty()) mark(be);
        return drained;
    }

    public static Object wrap(Object value, BlockEntity be) {
        if (be instanceof CapacitorCreativeBlockEntity) return value;
        if (value instanceof FluidTank tank) return new DirtyFluidHandler(tank, be);
        if (value instanceof IEnergyStorage storage && !(value instanceof WrappingEnergyStorage) && !(value instanceof NullEnergyStorage)) return new DirtyEnergyStorage(storage, be);
        return value;
    }

    public interface Stamped {
        long immersiveFixes$getDirtyTick();

        void immersiveFixes$setDirtyTick(long tick);
    }

    private record DirtyEnergyStorage(IEnergyStorage base, BlockEntity be) implements IEnergyStorage {
        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            int received = base.receiveEnergy(maxReceive, simulate);
            if (!simulate && received > 0) mark(be);
            return received;
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            return ChunkDirty.extractEnergy(be, base, maxExtract, simulate);
        }

        @Override
        public int getEnergyStored() {
            return base.getEnergyStored();
        }

        @Override
        public int getMaxEnergyStored() {
            return base.getMaxEnergyStored();
        }

        @Override
        public boolean canExtract() {
            return base.canExtract();
        }

        @Override
        public boolean canReceive() {
            return base.canReceive();
        }
    }

    private record DirtyFluidHandler(FluidTank tank, BlockEntity be) implements IFluidHandler {
        @Override
        public int getTanks() {
            return tank.getTanks();
        }

        @Override
        public @NotNull FluidStack getFluidInTank(int index) {
            return tank.getFluidInTank(index);
        }

        @Override
        public int getTankCapacity(int index) {
            return tank.getTankCapacity(index);
        }

        @Override
        public boolean isFluidValid(int index, @NotNull FluidStack stack) {
            return tank.isFluidValid(index, stack);
        }

        @Override
        public int fill(FluidStack resource, FluidAction action) {
            return ChunkDirty.fill(be, tank, resource, action);
        }

        @Override
        public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
            FluidStack drained = tank.drain(resource, action);
            if (action.execute() && !drained.isEmpty()) mark(be);
            return drained;
        }

        @Override
        public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
            return ChunkDirty.drain(be, tank, maxDrain, action);
        }
    }
}
