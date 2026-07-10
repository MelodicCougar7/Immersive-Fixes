package org.github.melodiccougar7.immersivefixes.mixin.client;

import blusunrize.immersiveengineering.client.ClientEventHandler;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.config.IEClientConfig;
import blusunrize.immersiveengineering.common.items.EarmuffsItem;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.sound.IEMuffledSound;
import blusunrize.immersiveengineering.common.util.sound.IEMuffledTickableSound;
import net.minecraft.client.resources.sounds.TickableSoundInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.github.melodiccougar7.immersivefixes.lib.IFLib;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ClientEventHandler.class)
public class ClientEventHandlerMixin {
    /**
     * Overwrites the PlaySoundEvent handler to use the dynamic IEMuffledSound
     * wrapper when earmuffs are worn. Only wraps when the player currently
     * has earmuffs that should muffle the category (preserves original
     * behavior and avoids breaking sounds that were never intended to be
     * wrapped).
     * @author tgstyle
     * @reason Preserve compatibility while allowing live volume updates for
     *         already-wrapped sounds when earmuff config or equipment changes.
     */
    @SuppressWarnings("resource")
    @SubscribeEvent
    @Overwrite(remap = false)
    public void onPlaySound(PlaySoundEvent event) {
        IFLib.logMixinActive("ClientEventHandlerMixin");
        if (event.getSound() == null) {
            return;
        } else {
            event.getSound().getSource();
        }
        if (!EarmuffsItem.affectedSoundCategories.contains(event.getSound().getSource().getName())) {
            return;
        }
        if (ClientUtils.mc().player != null) {
            ItemStack earmuffs = EarmuffsItem.EARMUFF_GETTERS.getFrom(ClientUtils.mc().player);
            if (!earmuffs.isEmpty() &&
                    !ItemNBTHelper.getBoolean(earmuffs, "IE:Earmuffs:Cat_" + event.getSound().getSource().getName())) {
                boolean blacklisted = false;
                String soundLoc = event.getSound().getLocation().toString();
                for (String blacklist : IEClientConfig.earDefenders_SoundBlacklist.get()) {
                    if (blacklist != null && blacklist.equalsIgnoreCase(soundLoc)) {
                        blacklisted = true;
                        break;
                    }
                }
                if (!blacklisted) {
                    float volMod = EarmuffsItem.getVolumeMod(earmuffs);
                    if (event.getSound() instanceof TickableSoundInstance) {
                        event.setSound(new IEMuffledTickableSound((TickableSoundInstance)event.getSound(), volMod));
                    } else {
                        event.setSound(new IEMuffledSound(event.getSound(), volMod));
                    }
                }
            }
        }
    }
}
