package org.github.melodiccougar7.immersivefixes.mixin.common;

import org.github.melodiccougar7.immersivefixes.lib.IFLib;

import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.MetalPressLogic;
import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.MetalPressLogic.State;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(value = MetalPressLogic.class, remap = false)
public abstract class MetalPressLogicMixin {
    @Inject(method = "dropExtraItems(Lblusunrize/immersiveengineering/common/blocks/multiblocks/logic/MetalPressLogic$State;Ljava/util/function/Consumer;)V", at = @At("TAIL"))
    private void immersiveFixes$clearDroppedMold(State state, Consumer<ItemStack> drop, CallbackInfo ci) {
        IFLib.logMixinActive("MetalPressLogicMixin");
        state.mold = ItemStack.EMPTY;
    }
}
