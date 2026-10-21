package org.github.melodiccougar7.immersivefixes.mixin.common;

import blusunrize.immersiveengineering.common.blocks.IEBaseBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.github.melodiccougar7.immersivefixes.util.ChunkDirty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value = IEBaseBlockEntity.class, remap = false)
public abstract class IEBaseBlockEntityMixin implements ChunkDirty.Stamped {
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

    @ModifyVariable(method = "registerCapability(Ljava/lang/Object;)Lblusunrize/immersiveengineering/common/util/ResettableCapability;", at = @At("HEAD"), argsOnly = true)
    private Object immersiveFixes$wrapStorage(Object value) {
        return ChunkDirty.wrap(value, (BlockEntity) (Object) this);
    }
}
