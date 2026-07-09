package org.github.melodiccougar7.immersivefixes;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Earmuff equip/unequip handler.
 * @author tgstyle
 */

@SuppressWarnings("resource")
public class ClientEventHandler {

    @SubscribeEvent
    public static void onEquipmentChange(LivingEquipmentChangeEvent event) {
        if (event.getSlot() != EquipmentSlot.HEAD) {
            return;
        }
        if (!event.getEntity().level().isClientSide()) {
            return;
        }
        if (event.getEntity() != ClientUtils.mc().player) {
            return;
        }

        refreshAffectedCategory(SoundSource.AMBIENT);
        refreshAffectedCategory(SoundSource.WEATHER);
        refreshAffectedCategory(SoundSource.RECORDS);
        refreshAffectedCategory(SoundSource.BLOCKS);
        refreshAffectedCategory(SoundSource.NEUTRAL);
        refreshAffectedCategory(SoundSource.HOSTILE);
        refreshAffectedCategory(SoundSource.PLAYERS);

        stopAffectedCategory(SoundSource.AMBIENT);
        stopAffectedCategory(SoundSource.WEATHER);
        stopAffectedCategory(SoundSource.RECORDS);
        stopAffectedCategory(SoundSource.BLOCKS);
        stopAffectedCategory(SoundSource.NEUTRAL);
        stopAffectedCategory(SoundSource.HOSTILE);
        stopAffectedCategory(SoundSource.PLAYERS);
    }

    private static void refreshAffectedCategory(SoundSource source) {
        if (source != null && ClientUtils.mc() != null) {
            float vol = ClientUtils.mc().options.getSoundSourceVolume(source);
            ClientUtils.mc().getSoundManager().updateSourceVolume(source, vol);
        }
    }

    private static void stopAffectedCategory(SoundSource source) {
        if (source != null && ClientUtils.mc() != null) {
            ClientUtils.mc().getSoundManager().stop(null, source);
        }
    }
}
