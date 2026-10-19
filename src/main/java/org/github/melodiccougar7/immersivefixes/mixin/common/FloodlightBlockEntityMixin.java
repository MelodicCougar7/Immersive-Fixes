package org.github.melodiccougar7.immersivefixes.mixin.common;

import blusunrize.immersiveengineering.common.blocks.metal.FloodlightBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.github.melodiccougar7.immersivefixes.util.ChunkDirty;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = FloodlightBlockEntity.class, remap = false)
public abstract class FloodlightBlockEntityMixin {
    @ModifyConstant(method = "readCustomNBT(Lnet/minecraft/nbt/CompoundTag;Z)V", constant = @Constant(stringValue = "energy"))
    private String immersiveFixes$fixEnergyStorageNbtKey(String key) {
        return "energyStorage";
    }

    @Inject(method = "tickServer()V", at = @At(value = "FIELD", target = "Lblusunrize/immersiveengineering/common/blocks/metal/FloodlightBlockEntity;energyStorage:I", opcode = Opcodes.PUTFIELD))
    private void immersiveFixes$dirtyOnDraw(CallbackInfo ci) {
        ChunkDirty.mark((BlockEntity) (Object) this);
    }

    @Inject(method = "tickServer()V", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"))
    private void immersiveFixes$dirtyOnFakeLight(CallbackInfo ci) {
        ChunkDirty.mark((BlockEntity) (Object) this);
    }
}
