package org.github.melodiccougar7.immersivefixes.mixin;

import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.CokeOvenLogic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

// Accessor to help resolve #6305: Coke Oven Exploit

@Mixin(CokeOvenLogic.State.class)
public interface CokeOvenStateAccessor {
    @Accessor("process")
    void setProcess(int value);

    @Accessor("processMax")
    void setProcessMax(int value);
}