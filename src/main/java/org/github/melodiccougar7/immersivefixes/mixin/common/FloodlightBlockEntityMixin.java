package org.github.melodiccougar7.immersivefixes.mixin.common;

import blusunrize.immersiveengineering.common.blocks.metal.FloodlightBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(value = FloodlightBlockEntity.class, remap = false)
public abstract class FloodlightBlockEntityMixin {
    @ModifyConstant(method = "readCustomNBT(Lnet/minecraft/nbt/CompoundTag;Z)V", constant = @Constant(stringValue = "energy"))
    private String immersiveFixes$fixEnergyStorageNbtKey(String key) {
        return "energyStorage";
    }
}
