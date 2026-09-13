package org.github.melodiccougar7.immersivefixes.client;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.config.IEClientConfig;
import blusunrize.immersiveengineering.common.items.EarmuffsItem;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public final class EarmuffHandler {
    public static final float MIN_VOLUME = 0.001F;
    private static final Map<SoundSource, Float> MULTIPLIERS = defaultMultipliers();
    private static List<? extends String> blacklistSource = List.of();
    private static Set<ResourceLocation> blacklist = Set.of();

    private EarmuffHandler() {}

    @SubscribeEvent @SuppressWarnings("resource")
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START) { return; }
        LocalPlayer player = ClientUtils.mc().player;
        if (player == null) { return; }
        refreshBlacklist();
        ItemStack earmuffs = EarmuffsItem.EARMUFF_GETTERS.getFrom(player);
        for (SoundSource source : SoundSource.values()) {
            float multiplier = 1.0F;
            if (!earmuffs.isEmpty() && EarmuffsItem.affectedSoundCategories.contains(source.getName()) && !ItemNBTHelper.getBoolean(earmuffs, "IE:Earmuffs:Cat_" + source.getName())) { multiplier = EarmuffsItem.getVolumeMod(earmuffs); }
            if (MULTIPLIERS.get(source) != multiplier) {
                MULTIPLIERS.put(source, multiplier);
                ClientUtils.mc().getSoundManager().updateSourceVolume(source, ClientUtils.mc().options.getSoundSourceVolume(source));
            }
        }
    }

    public static float apply(float volume, SoundInstance sound) {
        if (volume <= 0.0F || blacklist.contains(sound.getLocation())) { return volume; }
        float multiplier = MULTIPLIERS.get(sound.getSource());
        if (multiplier >= 1.0F) { return volume; }
        return Math.max(volume * multiplier, MIN_VOLUME);
    }

    private static void refreshBlacklist() {
        List<? extends String> current = IEClientConfig.earDefenders_SoundBlacklist.get();
        if (current == blacklistSource) { return; }
        blacklistSource = current;
        blacklist = current.stream().filter(Objects::nonNull).map(name -> ResourceLocation.tryParse(name.toLowerCase(Locale.ROOT))).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    private static Map<SoundSource, Float> defaultMultipliers() {
        Map<SoundSource, Float> result = new EnumMap<>(SoundSource.class);
        for (SoundSource source : SoundSource.values()) { result.put(source, 1.0F); }
        return result;
    }
}
