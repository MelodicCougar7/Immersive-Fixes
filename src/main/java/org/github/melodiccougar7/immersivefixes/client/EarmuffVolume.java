package org.github.melodiccougar7.immersivefixes.client;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.config.IEClientConfig;
import blusunrize.immersiveengineering.common.items.EarmuffsItem;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.world.item.ItemStack;

public final class EarmuffVolume {
    public static final float MIN_VOLUME = 0.001F;

    private EarmuffVolume() {}

    @SuppressWarnings("resource")
    public static float apply(float volume, SoundInstance sound) {
        if (volume <= 0.0F || sound == null) { return volume; }
        String category = sound.getSource().getName();
        if (!EarmuffsItem.affectedSoundCategories.contains(category)) { return volume; }
        LocalPlayer player = ClientUtils.mc().player;
        if (player == null) { return volume; }
        ItemStack earmuffs = EarmuffsItem.EARMUFF_GETTERS.getFrom(player);
        if (earmuffs.isEmpty() || ItemNBTHelper.getBoolean(earmuffs, "IE:Earmuffs:Cat_" + category)) { return volume; }
        String name = sound.getLocation().toString();
        for (String blacklist : IEClientConfig.earDefenders_SoundBlacklist.get()) {
            if (blacklist != null && blacklist.equalsIgnoreCase(name)) { return volume; }
        }
        float mod = EarmuffsItem.getVolumeMod(earmuffs);
        if (mod >= 1.0F) { return volume; }
        return Math.max(volume * mod, MIN_VOLUME);
    }
}
