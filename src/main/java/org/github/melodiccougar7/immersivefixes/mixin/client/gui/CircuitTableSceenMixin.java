package org.github.melodiccougar7.immersivefixes.mixin.client.gui;

import blusunrize.immersiveengineering.client.gui.CircuitTableScreen;
import blusunrize.immersiveengineering.client.gui.elements.GuiButtonLogicCircuitRegister;
import org.github.melodiccougar7.immersivefixes.lib.IFLib;
import org.github.melodiccougar7.immersivefixes.mixin.client.gui.helper.ScreenInvoker;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

// TODO This mixin heavily breaks the menu
/* reminder: drop this bit into the mixin json once everything is fixed
 "client.gui.CircuitTableSceenMixin",
    "client.gui.GuiButtonLogicCircuitRegisterMixins",
    "client.gui.helper.ScreenInvoker",
 */
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
        IFLib.logMixinActive("CircuitTableSceenMixin");
        ScreenInvoker invoker = (ScreenInvoker) this;
        this.inputButtons.forEach(invoker::invokeRemoveWidget);
    }
}
