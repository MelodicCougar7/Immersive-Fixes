package org.github.melodiccougar7.immersivefixes.mixin.client;

import blusunrize.immersiveengineering.common.items.InternalStorageItem;
import net.minecraft.network.protocol.game.ServerboundSetCreativeModeSlotPacket;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ServerboundSetCreativeModeSlotPacket.class)
public abstract class ServerboundSetCreativeModeSlotPacketMixin {
    @ModifyArg(method = "write", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/FriendlyByteBuf;writeItemStack(Lnet/minecraft/world/item/ItemStack;Z)Lnet/minecraft/network/FriendlyByteBuf;", remap = false), index = 1)
    private boolean immersiveFixes$shareInternalStorage(ItemStack stack, boolean limitedTag) { return limitedTag || stack.getItem() instanceof InternalStorageItem; }
}
