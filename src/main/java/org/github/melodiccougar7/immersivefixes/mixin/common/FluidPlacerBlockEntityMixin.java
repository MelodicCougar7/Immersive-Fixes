package org.github.melodiccougar7.immersivefixes.mixin.common;

import blusunrize.immersiveengineering.common.blocks.metal.FluidPlacerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.github.melodiccougar7.immersivefixes.util.ChunkDirty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = FluidPlacerBlockEntity.class, remap = false)
public abstract class FluidPlacerBlockEntityMixin {
    @Shadow
    private static boolean place(BlockPos pos, FluidTank tank, Level world) {
        throw new AssertionError();
    }

    @Redirect(method = "tickServer()V", at = @At(value = "INVOKE", target = "Lblusunrize/immersiveengineering/common/blocks/metal/FluidPlacerBlockEntity;place(Lnet/minecraft/core/BlockPos;Lnet/minecraftforge/fluids/capability/templates/FluidTank;Lnet/minecraft/world/level/Level;)Z"))
    private boolean immersiveFixes$dirtyOnPlace(BlockPos pos, FluidTank tank, Level world) {
        boolean placed = place(pos, tank, world);
        if (placed) ChunkDirty.mark((BlockEntity) (Object) this);
        return placed;
    }
}
