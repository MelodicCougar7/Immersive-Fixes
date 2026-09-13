package org.github.melodiccougar7.immersivefixes.mixin.client;

import org.github.melodiccougar7.immersivefixes.lib.IFLib;

import blusunrize.immersiveengineering.common.util.sound.MultiblockSound;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SoundEngine.class)
public abstract class SoundEngineMuteMixin {
    @ModifyExpressionValue(method = "tickNonPaused", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Options;getSoundSourceVolume(Lnet/minecraft/sounds/SoundSource;)F"))
    private float immersiveFixes$keepMutedMultiblockSounds(float categoryVolume, @Local SoundInstance sound) {
        if (categoryVolume > 0.0F || !(sound instanceof MultiblockSound)) { return categoryVolume; }
        IFLib.logMixinActive("SoundEngineMuteMixin");
        return 1.0F;
    }
}
