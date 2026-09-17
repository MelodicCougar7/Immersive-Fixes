package org.github.melodiccougar7.immersivefixes.mixin.client;

import blusunrize.immersiveengineering.client.ClientEventHandler;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ClientEventHandler.class, remap = false)
public abstract class ClientEventHandlerMixin {
    @Inject(method = "onPlaySound(Lnet/minecraftforge/client/event/sound/PlaySoundEvent;)V", at = @At("HEAD"), cancellable = true)
    private void immersiveFixes$keepSoundIdentity(PlaySoundEvent event, CallbackInfo ci) { ci.cancel(); }
}
