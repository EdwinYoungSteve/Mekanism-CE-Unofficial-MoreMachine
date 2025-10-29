package mekceumoremachine.mixin.mekanism;

import mekanism.common.Mekanism;
import mekanism.common.base.IBoundingBlock;
import mekanism.common.base.IRedstoneControl;
import mekanism.common.base.ITierUpgradeable;
import mekanism.common.tier.BaseTier;
import mekanism.common.tile.component.TileComponentSecurity;
import mekanism.common.tile.component.TileComponentUpgrade;
import mekanism.common.tile.machine.TileEntityElectricPump;
import mekanism.common.tile.prefab.TileEntityElectricBlock;
import mekceumoremachine.common.registries.MEKCeuMoreMachineBlocks;
import mekceumoremachine.common.tile.interfaces.ITierFirstUpgrade;
import mekceumoremachine.common.tile.machine.TileEntityTierElectricPump;
import net.minecraftforge.fluids.FluidTank;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = TileEntityElectricPump.class, remap = false)
public abstract class MixinTileEntityElectricPump extends TileEntityElectricBlock implements ITierUpgradeable, ITierFirstUpgrade {

    @Shadow
    public int operatingTicks;

    @Shadow
    public abstract IRedstoneControl.RedstoneControl getControlType();

    @Shadow
    public TileComponentUpgrade upgradeComponent;

    @Shadow
    public TileComponentSecurity securityComponent;

    @Shadow
    public FluidTank fluidTank;


    public MixinTileEntityElectricPump(String name, double baseMaxEnergy) {
        super(name, baseMaxEnergy);
    }

    @Override
    public boolean upgrade(BaseTier upgradeTier) {
        if (upgradeTier != BaseTier.BASIC) {
            return false;
        }
        if (world.getTileEntity(getPos()) instanceof IBoundingBlock block){
            block.onBreak();
        }else {
            world.setBlockToAir(getPos());
        }

        world.setBlockState(getPos(), MEKCeuMoreMachineBlocks.TierElectricPump.getDefaultState(), 3);
        if (world.getTileEntity(getPos()) instanceof TileEntityTierElectricPump tile) {

            //Basic
            tile.facing = facing;
            tile.clientFacing = clientFacing;
            tile.ticker = ticker;
            tile.redstone = redstone;
            tile.redstoneLastTick = redstoneLastTick;
            tile.doAutoSync = doAutoSync;

            //Electric
            tile.electricityStored.set(electricityStored.get());
            //Machine

            tile.operatingTicks = operatingTicks;
            tile.setControlType(getControlType());

            tile.upgradeComponent.readFrom(upgradeComponent);
            tile.upgradeComponent.setUpgradeSlot(upgradeComponent.getUpgradeSlot());

            tile.securityComponent.readFrom(securityComponent);
            for (int i = 0; i < inventory.size(); i++) {
                tile.inventory.set(i, inventory.get(i));
            }
            tile.fluidTank.setFluid(fluidTank.getFluid());

            tile.upgradeComponent.getSupportedTypes().forEach(tile::recalculateUpgradables);
            tile.markNoUpdateSync();
            Mekanism.packetHandler.sendUpdatePacket(tile);
            markNoUpdateSync();
            return true;
        }
        return false;
    }


}
