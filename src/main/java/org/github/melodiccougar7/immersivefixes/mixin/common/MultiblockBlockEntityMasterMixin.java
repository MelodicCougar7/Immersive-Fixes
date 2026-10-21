package org.github.melodiccougar7.immersivefixes.mixin.common;

import blusunrize.immersiveengineering.api.multiblocks.blocks.registry.MultiblockBlockEntityMaster;
import org.github.melodiccougar7.immersivefixes.util.ChunkDirty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = MultiblockBlockEntityMaster.class, remap = false)
public abstract class MultiblockBlockEntityMasterMixin implements ChunkDirty.Stamped {
    @Unique
    private long immersiveFixes$dirtyTick = -1;

    @Override
    public long immersiveFixes$getDirtyTick() {
        return immersiveFixes$dirtyTick;
    }

    @Override
    public void immersiveFixes$setDirtyTick(long tick) {
        immersiveFixes$dirtyTick = tick;
    }
}
