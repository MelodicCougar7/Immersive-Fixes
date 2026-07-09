package org.github.melodiccougar7.immersivefixes.mixin.gui;

import blusunrize.immersiveengineering.client.gui.CircuitTableScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = net.minecraft.client.gui.screens.Screen.class, remap = true)
public interface ScreenInvoker {

    @Invoker("removeWidget")
    void invokeRemoveWidget(
            net.minecraft.client.gui.components.events.GuiEventListener listener
    );
}
