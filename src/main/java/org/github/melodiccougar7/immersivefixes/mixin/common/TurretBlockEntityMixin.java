package org.github.melodiccougar7.immersivefixes.mixin.common;

import blusunrize.immersiveengineering.common.blocks.metal.TurretBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.github.melodiccougar7.immersivefixes.util.ChunkDirty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = TurretBlockEntity.class, remap = false)
public abstract class TurretBlockEntityMixin {
    @Inject(method = "doGraphicalUpdates()V", at = @At("HEAD"))
    private void immersiveFixes$dirtyOnInventoryChange(CallbackInfo ci) {
        ChunkDirty.mark((BlockEntity) (Object) this);
    }
}
