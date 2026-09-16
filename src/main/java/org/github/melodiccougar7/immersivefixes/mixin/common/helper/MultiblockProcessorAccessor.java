package org.github.melodiccougar7.immersivefixes.mixin.common.helper;

import blusunrize.immersiveengineering.common.blocks.multiblocks.process.MultiblockProcess;
import blusunrize.immersiveengineering.common.blocks.multiblocks.process.MultiblockProcessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(value = MultiblockProcessor.class, remap = false)
public interface MultiblockProcessorAccessor {
    @Accessor("processQueue")
    List<MultiblockProcess<?, ?>> getProcessQueue();

    @Accessor("markDirty")
    Runnable getMarkDirty();

    @Accessor("onQueueChange")
    Runnable getOnQueueChange();
}
