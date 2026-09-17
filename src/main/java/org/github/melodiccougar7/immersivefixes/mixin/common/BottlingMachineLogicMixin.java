package org.github.melodiccougar7.immersivefixes.mixin.common;

import org.github.melodiccougar7.immersivefixes.mixin.common.helper.MultiblockProcessorAccessor;

import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.bottling_machine.BottlingMachineLogic;
import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.bottling_machine.BottlingMachineLogic.State;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(value = BottlingMachineLogic.class, remap = false)
public abstract class BottlingMachineLogicMixin {
    @Inject(method = "dropExtraItems(Lblusunrize/immersiveengineering/common/blocks/multiblocks/logic/bottling_machine/BottlingMachineLogic$State;Ljava/util/function/Consumer;)V", at = @At("TAIL"))
    private void immersiveFixes$clearDroppedProcesses(State state, Consumer<ItemStack> drop, CallbackInfo ci) {
        MultiblockProcessorAccessor processor = (MultiblockProcessorAccessor) state.processor;
        if (processor.getProcessQueue().isEmpty()) { return; }
        processor.getProcessQueue().clear();
        processor.getMarkDirty().run();
        processor.getOnQueueChange().run();
    }
}
