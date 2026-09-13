package org.github.melodiccougar7.immersivefixes.client;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.items.EarmuffsItem;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ClientEventHandler {
    @SubscribeEvent @SuppressWarnings("resource")
    public static void onEquipmentChange(LivingEquipmentChangeEvent event) {
        if (event.getSlot() != EquipmentSlot.HEAD || !event.getEntity().level().isClientSide() || event.getEntity() != ClientUtils.mc().player) { return; }
        for (SoundSource source : SoundSource.values()) {
            if (EarmuffsItem.affectedSoundCategories.contains(source.getName())) { ClientUtils.mc().getSoundManager().updateSourceVolume(source, ClientUtils.mc().options.getSoundSourceVolume(source)); }
        }
    }
}
