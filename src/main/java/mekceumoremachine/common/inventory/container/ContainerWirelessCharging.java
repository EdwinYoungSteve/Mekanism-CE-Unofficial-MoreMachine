package mekceumoremachine.common.inventory.container;

import mekanism.common.inventory.container.ContainerEnergyStorage;
import mekanism.common.inventory.slot.SlotArmor;
import mekanism.common.inventory.slot.SlotEnergy.*;
import mekceumoremachine.common.tile.machine.TileEntityWirelessChargingStation;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;

public class ContainerWirelessCharging extends ContainerEnergyStorage<TileEntityWirelessChargingStation> {

    public ContainerWirelessCharging(InventoryPlayer inventory,TileEntityWirelessChargingStation tile) {
        super(tile, inventory);
    }

    @Override
    protected void addSlots() {
        addSlotToContainer(new SlotCharge(tileEntity, 0, 26, 56 + 2));
        addSlotToContainer(new SlotDischarge(tileEntity, 1, 26, 14 + 2));
    }

    @Override
    protected void addPlayerArmmorSlot(InventoryPlayer inventory) {
        addSlotToContainer(new SlotArmor(inventory, EntityEquipmentSlot.HEAD, 180, 37 + 5));
        addSlotToContainer(new SlotArmor(inventory, EntityEquipmentSlot.CHEST, 180, 37 + 23));
        addSlotToContainer(new SlotArmor(inventory, EntityEquipmentSlot.LEGS, 180, 37 + 41));
        addSlotToContainer(new SlotArmor(inventory, EntityEquipmentSlot.FEET, 180, 37 + 59));
        addSlotToContainer(new SlotArmor(inventory, EntityEquipmentSlot.OFFHAND, 180, 37 + 77));
    }

    @Override
    protected int getInventorYOffset() {
        return 84 + 2;
    }
}
