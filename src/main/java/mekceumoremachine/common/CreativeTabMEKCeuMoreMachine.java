package mekceumoremachine.common;

import mekceumoremachine.common.registries.MEKCeuMoreMachineBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class CreativeTabMEKCeuMoreMachine extends CreativeTabs {

    public CreativeTabMEKCeuMoreMachine() {
        super("tabmekaceumoremachine");
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(MEKCeuMoreMachineBlocks.WirelessCharging);
    }
}
