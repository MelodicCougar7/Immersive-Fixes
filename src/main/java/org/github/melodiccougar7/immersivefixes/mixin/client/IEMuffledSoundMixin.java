package org.github.melodiccougar7.immersivefixes.mixin.client;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.config.IEClientConfig;
import blusunrize.immersiveengineering.common.items.EarmuffsItem;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.sound.IEMuffledSound;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.world.item.ItemStack;
import org.github.melodiccougar7.immersivefixes.lib.IFLib;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(IEMuffledSound.class)
public class IEMuffledSoundMixin {
    @Shadow(remap = false)
    SoundInstance originalSound;

    /**
     * Overwrites getVolume so the muffling factor is recomputed every time
     * the sound engine polls it. This makes earmuffs take effect (and
     * stop taking effect) the instant they are equipped or unequipped,
     * without requiring a world restart.
     * @author tgstyle
     * @reason Recompute volume mod on every poll so earmuff state changes apply live.
     */
    @SuppressWarnings("resource")
    @Overwrite
    public float getVolume() {
        IFLib.logMixinActive("IEMuffledSoundMixin");
        float baseVolume = this.originalSound.getVolume();
        if (ClientUtils.mc().player == null) {
            return baseVolume;
        }
        ItemStack earmuffs = EarmuffsItem.EARMUFF_GETTERS.getFrom(ClientUtils.mc().player);
        if (earmuffs.isEmpty()) {
            return baseVolume;
        }
        String catName = this.originalSound.getSource().getName();
        if (ItemNBTHelper.getBoolean(earmuffs, "IE:Earmuffs:Cat_" + catName)) {
            return baseVolume;
        }
        boolean blacklisted = false;
        for (String blacklist : IEClientConfig.earDefenders_SoundBlacklist.get()) {
            if (blacklist != null && blacklist.equalsIgnoreCase(this.originalSound.getLocation().toString())) {
                blacklisted = true;
                break;
            }
        }
        if (blacklisted) {
            return baseVolume;
        }
        float mod = EarmuffsItem.getVolumeMod(earmuffs);
        return baseVolume * mod;
    }
}
