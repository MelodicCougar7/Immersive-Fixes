package org.github.melodiccougar7.immersivefixes.mixin.gui;

import blusunrize.immersiveengineering.client.gui.CircuitTableScreen;
import blusunrize.immersiveengineering.client.gui.elements.GuiButtonLogicCircuitRegister;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = CircuitTableScreen.class, remap = false)
public abstract class CircuitTableSceenMixin {

    @Final
    @Shadow
    private List<GuiButtonLogicCircuitRegister> inputButtons;

    @Inject(
            method = "updateButtons()V",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/List;clear()V"
            )
    )
    private void beforeInputButtonsClear(CallbackInfo ci) {
        ScreenInvoker invoker = (ScreenInvoker)(Object)this;
        this.inputButtons.forEach(invoker::invokeRemoveWidget);
    }
}
