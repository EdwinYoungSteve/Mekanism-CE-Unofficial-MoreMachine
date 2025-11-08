package mekceumoremachine.common.inventory.container;

import mekanism.common.inventory.container.ContainerMekanism;
import mekanism.common.inventory.slot.SlotEnergy;
import mekanism.common.recipe.RecipeHandler;
import mekanism.common.recipe.inputs.ItemStackInput;
import mekanism.common.util.ChargeUtils;
import mekceumoremachine.common.tier.MachineTier;
import mekceumoremachine.common.tile.machine.TierOxidizer.TileEntityTierChemicalOxidizer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;

public class ContainerTierChemicalOxidizer extends ContainerMekanism<TileEntityTierChemicalOxidizer> {

    public ContainerTierChemicalOxidizer(InventoryPlayer inventory, TileEntityTierChemicalOxidizer tile) {
        super(tile, inventory);
    }

    @Override
    protected void addSlots() {
        addSlotToContainer(new SlotEnergy.SlotDischarge(tileEntity, 0, 7, 13));
        int xOffset = tileEntity.tier == MachineTier.BASIC ? 55 : tileEntity.tier == MachineTier.ADVANCED ? 35 : tileEntity.tier == MachineTier.ELITE ? 29 : 27;
        int xDistance = tileEntity.tier == MachineTier.BASIC ? 38 : tileEntity.tier == MachineTier.ADVANCED ? 26 : 19;
        for (int i = 0; i < tileEntity.tier.processes; i++) {
            addSlotToContainer(new Slot(tileEntity, getInputSlotIndex(i), xOffset + (i * xDistance), 13) {
                @Override
                public boolean isItemValid(ItemStack itemstack) {
                    return isInputItem(itemstack);
                }
            });
        }
    }

    private int getInputSlotIndex(int processNumber) {
        return 2 + processNumber;
    }


    private boolean isInputItem(ItemStack itemstack) {
        for (ItemStackInput input : RecipeHandler.Recipe.CHEMICAL_OXIDIZER.get().keySet()) {
            if (ItemHandlerHelper.canItemStacksStack(input.ingredient, itemstack)) {
                return true;
            }
        }
        return false;
    }


    @Override
    protected int getInventorYOffset() {
        return 100;
    }


    @Override
    protected int getInventorXOffset() {
        return tileEntity.tier == MachineTier.ULTIMATE ? 27 : 8;
    }

    //todo
    @Nonnull
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotID) {
        ItemStack stack = ItemStack.EMPTY;
        Slot currentSlot = inventorySlots.get(slotID);
        if (currentSlot != null && currentSlot.getHasStack()) {
            ItemStack slotStack = currentSlot.getStack();
            stack = slotStack.copy();
            if (RecipeHandler.getNutritionalRecipe(new ItemStackInput(slotStack)) != null) {
                if (isInputSlot(slotID)) {
                    if (!mergeItemStack(slotStack, tileEntity.inventory.size() - 1, inventorySlots.size(), true)) {
                        return ItemStack.EMPTY;
                    } else if (!mergeItemStack(slotStack, 1, 1 + tileEntity.tier.processes, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (ChargeUtils.canBeDischarged(slotStack)) {
                if (slotID == 0) {
                    if (!mergeItemStack(slotStack, tileEntity.inventory.size() - 1, inventorySlots.size(), false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!mergeItemStack(slotStack, 0, 1, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                int slotEnd = tileEntity.inventory.size() - 1;
                if (slotID >= slotEnd && slotID <= (slotEnd + 26)) {
                    if (!mergeItemStack(slotStack, slotEnd + 27, inventorySlots.size(), false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slotID > (slotEnd + 26)) {
                    if (!mergeItemStack(slotStack, slotEnd, slotEnd + 26, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!mergeItemStack(slotStack, slotEnd, inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            }

            if (slotStack.getCount() == 0) {
                currentSlot.putStack(ItemStack.EMPTY);
            } else {
                currentSlot.onSlotChanged();
            }
            if (slotStack.getCount() == stack.getCount()) {
                return ItemStack.EMPTY;
            }
            currentSlot.onTake(player, slotStack);
        }
        return stack;
    }

    public boolean isInputSlot(int slot) {
        return slot >= 1 && slot < 1 + tileEntity.tier.processes;
    }

    private boolean transferExtraSlot(int slotID, ItemStack slotStack) {
        if (slotID >= tileEntity.inventory.size() - 1) {
            return !mergeItemStack(slotStack, 0, 1, false);
        }
        return !mergeItemStack(slotStack, tileEntity.inventory.size() - 1, inventorySlots.size(), true);
    }


}
