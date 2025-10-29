package mekceumoremachine.common.item.itemBlock;

import mekanism.common.base.IFluidItemWrapper;
import mekanism.common.base.ISustainedTank;
import mekanism.common.block.states.BlockStateMachine;
import mekanism.common.util.ItemDataUtils;
import mekceumoremachine.common.tier.MachineTier;
import mekceumoremachine.common.tile.machine.TileEntityTierElectricPump;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

public class ItemBlockTierElectricPump extends ItemBlockTierEnergyMachine implements ISustainedTank, IFluidItemWrapper {

    public ItemBlockTierElectricPump(Block block) {
        super(block, "TierElectricPump");
    }


    @Override
    public void setTierMachine(TileEntity tileEntity, ItemStack stack) {
        if (tileEntity instanceof TileEntityTierElectricPump tile) {
            tile.tier = MachineTier.values()[getBaseTier(stack).ordinal()];
            tile.fluidTank.setCapacity(tile.tier.processes * TileEntityTierElectricPump.MAX_FLUID);
        }
    }

    @Override
    public void addOtherMachine(TileEntity tileEntity, ItemStack stack, World world) {
        super.addOtherMachine(tileEntity,stack,world);
        if (tileEntity instanceof ISustainedTank tank) {
            if (hasTank(stack) && getFluidStack(stack) != null) {
                tank.setFluidStack(getFluidStack(stack));
            }
        }
    }


    @Override
    public void setFluidStack(FluidStack fluidStack, Object... data) {
        if (data[0] instanceof ItemStack itemStack) {
            if (fluidStack == null || fluidStack.amount == 0) {
                ItemDataUtils.removeData(itemStack, "fluidTank");
            } else {
                ItemDataUtils.setCompound(itemStack, "fluidTank", fluidStack.writeToNBT(new NBTTagCompound()));
            }
        }
    }

    @Override
    public FluidStack getFluidStack(Object... data) {
        if (data[0] instanceof ItemStack itemStack) {
            if (!ItemDataUtils.hasData(itemStack, "fluidTank")) {
                return null;
            }
            return FluidStack.loadFluidStackFromNBT(ItemDataUtils.getCompound(itemStack, "fluidTank"));
        }
        return null;
    }

    @Override
    public boolean hasTank(Object... data) {
        return data[0] instanceof ItemStack stack && stack.getItem() instanceof ISustainedTank;
    }

    @Override
    public double getMachineStorage() {
        return BlockStateMachine.MachineType.ELECTRIC_PUMP.getStorage();
    }

    @Override
    public FluidStack getFluid(ItemStack container) {
        return getFluidStack(container);
    }

    @Override
    public int getCapacity(ItemStack container) {
        return MachineTier.values()[getBaseTier(container).ordinal()].processes * TileEntityTierElectricPump.MAX_FLUID;
    }

    @Override
    public int fill(ItemStack container, FluidStack resource, boolean doFill) {
        return 0;
    }

    @Override
    public FluidStack drain(ItemStack container, int maxDrain, boolean doDrain) {
        return null;
    }


}
