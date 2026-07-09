package org.github.melodiccougar7.immersivefixes.mixin;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.config.IEClientConfig;
import blusunrize.immersiveengineering.common.items.EarmuffsItem;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.sound.IEMuffledSound;
import blusunrize.immersiveengineering.common.util.sound.IEMuffledTickableSound;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Applies live earmuff volume modulation directly in SoundEngine.calculateVolume.
 * This allows equipping earmuffs to immediately affect already-playing sounds
 * (both wrapped and unwrapped) without requiring a world reload.
 * @author tgstyle
 */
@Mixin(net.minecraft.client.sounds.SoundEngine.class)
public class SoundEngineMixin {

    @SuppressWarnings("resource")
    @Inject(method = "calculateVolume(Lnet/minecraft/client/resources/sounds/SoundInstance;)F", at = @At("RETURN"), cancellable = true)
    private void onCalculateVolume(SoundInstance pSound, CallbackInfoReturnable<Float> cir) {
        float baseVol = cir.getReturnValue();
        if (ClientUtils.mc().player == null) {
            return;
        }
        ItemStack earmuffs = EarmuffsItem.EARMUFF_GETTERS.getFrom(ClientUtils.mc().player);
        if (earmuffs.isEmpty()) {
            return;
        }
        if (pSound instanceof IEMuffledSound || pSound instanceof IEMuffledTickableSound) {
            return;
        }
        String catName = pSound.getSource().getName();
        if (ItemNBTHelper.getBoolean(earmuffs, "IE:Earmuffs:Cat_" + catName)) {
            return;
        }
        boolean blacklisted = false;
        for (String blacklist : IEClientConfig.earDefenders_SoundBlacklist.get()) {
            if (blacklist != null && blacklist.equalsIgnoreCase(pSound.getLocation().toString())) {
                blacklisted = true;
                break;
            }
        }
        if (blacklisted) {
            return;
        }
        float mod = EarmuffsItem.getVolumeMod(earmuffs);
        cir.setReturnValue(baseVol * mod);
    }
}
