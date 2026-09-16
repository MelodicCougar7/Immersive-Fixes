package org.github.melodiccougar7.immersivefixes.mixin.client.gui;

import org.github.melodiccougar7.immersivefixes.lib.IFLib;

import blusunrize.immersiveengineering.client.gui.RedstoneConnectorScreen;
import blusunrize.immersiveengineering.client.gui.elements.GuiButtonBoolean;
import blusunrize.immersiveengineering.client.gui.elements.GuiButtonState;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = RedstoneConnectorScreen.class, remap = false)
public abstract class RedstoneConnectorScreenMixin {
    @WrapOperation(method = "drawGuiContainerForegroundLayer(Lnet/minecraft/client/gui/GuiGraphics;IIF)V", at = @At(value = "INVOKE", target = "Lblusunrize/immersiveengineering/client/gui/elements/GuiButtonState;isHoveredOrFocused()Z", remap = true))
    private boolean immersiveFixes$stateHoveredOnly(GuiButtonState<?> button, Operation<Boolean> original) {
        IFLib.logMixinActive("RedstoneConnectorScreenMixin");
        return button.isHovered();
    }

    @WrapOperation(method = "drawGuiContainerForegroundLayer(Lnet/minecraft/client/gui/GuiGraphics;IIF)V", at = @At(value = "INVOKE", target = "Lblusunrize/immersiveengineering/client/gui/elements/GuiButtonBoolean;isHoveredOrFocused()Z", remap = true))
    private boolean immersiveFixes$colorHoveredOnly(GuiButtonBoolean button, Operation<Boolean> original) { return button.isHovered(); }
}
