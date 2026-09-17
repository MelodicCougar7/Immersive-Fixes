package org.github.melodiccougar7.immersivefixes.mixin.common;

import blusunrize.immersiveengineering.api.tool.IConfigurableTool;
import blusunrize.immersiveengineering.api.tool.IUpgradeableTool;
import blusunrize.immersiveengineering.common.gui.IEContainerMenu;
import blusunrize.immersiveengineering.common.gui.IESlot;
import blusunrize.immersiveengineering.common.gui.ModWorkbenchContainer;
import blusunrize.immersiveengineering.common.items.EngineersBlueprintItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import javax.annotation.Nonnull;

@Mixin(ModWorkbenchContainer.class)
public abstract class ModWorkbenchContainerMixin extends IEContainerMenu {
    protected ModWorkbenchContainerMixin(MenuContext ctx) { super(ctx); }

    /**
     * @author tgstyle
     * @reason Stop shift-click from dropping a live or uncrafted stack
     */
    @Nonnull
    @Overwrite
    public ItemStack quickMoveStack(Player player, int slot) {
        ItemStack resultStack = ItemStack.EMPTY;
        Slot slotObject = slots.get(slot);
        if (slotObject.hasItem()) {
            ItemStack stackInSlot = slotObject.getItem();
            resultStack = stackInSlot.copy();
            if (slot < ownSlotCount) {
                if (!super.moveItemStackTo(stackInSlot, ownSlotCount, ownSlotCount + 36, true)) { return ItemStack.EMPTY; }
            }
            else if (!stackInSlot.isEmpty()) {
                boolean singleSlot = ownSlotCount == 1;
                if (stackInSlot.getItem() instanceof EngineersBlueprintItem
                        || (stackInSlot.getItem() instanceof IUpgradeableTool uTool && uTool.canModify(stackInSlot))
                        || (stackInSlot.getItem() instanceof IConfigurableTool cTool && cTool.canConfigure(stackInSlot))) {
                    if (!this.moveItemStackTo(stackInSlot, 0, 1, false) && singleSlot) { return ItemStack.EMPTY; }
                }
                if (!singleSlot) {
                    if (!this.moveItemStackTo(stackInSlot, 1, ownSlotCount, false)) { return ItemStack.EMPTY; }
                }
            }
            slotObject.setChanged();
            if (stackInSlot.getCount() == resultStack.getCount()) { resultStack = ItemStack.EMPTY; }
            if (slotObject instanceof IESlot.BlueprintOutput && slotObject.hasItem()) { player.drop(slotObject.getItem().copy(), false); }
            slotObject.onTake(player, resultStack);
        }
        return resultStack;
    }
}
