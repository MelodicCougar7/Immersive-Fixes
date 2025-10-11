package org.github.melodiccougar7.immersivefixes;

import com.google.common.collect.ImmutableMap;
import net.minecraftforge.fml.loading.LoadingModList;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class ImmersiveFixesMixinPlugin implements IMixinConfigPlugin {
    // Code taken from https://github.com/Juuxel/Adorn/blob/bd70a2955640897bc68ff1f4f201fe5e6c10bc32/fabric/src/main/java/juuxel/adorn/AdornMixinPlugin.java under the MIT License
    private static final Supplier<Boolean> TRUE = () -> true;

    private static final Map<String, Supplier<Boolean>> CONDITIONS = ImmutableMap.of(
            "melodiccougar7.immersivefixes.mixin.ArcFurnaceSecondariesChanceMixin", () -> LoadingModList.get().getModFileById("compressedengineering") != null
    );


    @Override
    public void onLoad(String mixinPackage) {}

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        // Mod compatibility for Compressed Engineering
        return CONDITIONS.getOrDefault(mixinClassName, TRUE).get();
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, org.objectweb.asm.tree.ClassNode targetClass,
                         String mixinClassName, IMixinInfo mixinInfo) {}

    @Override
    public void postApply(String targetClassName, org.objectweb.asm.tree.ClassNode targetClass,
                          String mixinClassName, IMixinInfo mixinInfo) {}
}
