package org.github.melodiccougar7.immersivefixes.mixin.common;

import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.sawmill.SawmillLogic;
import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.sawmill.SawmillLogic.State;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(value = SawmillLogic.class, remap = false)
public abstract class SawmillLogicMixin {
    @Inject(method = "dropExtraItems(Lblusunrize/immersiveengineering/common/blocks/multiblocks/logic/sawmill/SawmillLogic$State;Ljava/util/function/Consumer;)V", at = @At("TAIL"))
    private void immersiveFixes$clearDroppedSawblade(State state, Consumer<ItemStack> drop, CallbackInfo ci) { state.sawblade = ItemStack.EMPTY; }
}
