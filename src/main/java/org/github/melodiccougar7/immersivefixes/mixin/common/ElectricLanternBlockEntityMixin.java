package org.github.melodiccougar7.immersivefixes.mixin.common;

import blusunrize.immersiveengineering.common.blocks.metal.ElectricLanternBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.github.melodiccougar7.immersivefixes.util.ChunkDirty;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ElectricLanternBlockEntity.class, remap = false)
public abstract class ElectricLanternBlockEntityMixin {
    @Inject(method = "tickServer()V", at = @At(value = "FIELD", target = "Lblusunrize/immersiveengineering/common/blocks/metal/ElectricLanternBlockEntity;energyStorage:I", opcode = Opcodes.PUTFIELD))
    private void immersiveFixes$dirtyOnDraw(CallbackInfo ci) {
        ChunkDirty.mark((BlockEntity) (Object) this);
    }
}
