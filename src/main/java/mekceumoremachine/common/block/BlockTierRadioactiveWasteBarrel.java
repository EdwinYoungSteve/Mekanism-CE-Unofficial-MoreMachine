package mekceumoremachine.common.block;

import mekceumoremachine.common.block.states.BlockStateTierMachine;
import mekceumoremachine.common.registries.MEKCeuMoreMachineBlocks;
import mekceumoremachine.common.tile.machine.TileEntityTierRadioactiveWasteBarrel;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class BlockTierRadioactiveWasteBarrel extends BlockTierMachine {

    private static final AxisAlignedBB TANK = new AxisAlignedBB(0.125F, 0.0F, 0.125F, 0.875F, 1.0F, 0.875F);

    public BlockTierRadioactiveWasteBarrel() {
        super();
    }


    @Override
    public IBlockState AddActualState(@NotNull IBlockState state, IBlockAccess worldIn, BlockPos pos, TileEntity tile) {
        if (tile instanceof TileEntityTierRadioactiveWasteBarrel tiers) {
            if (tiers.tier != null) {
                state = state.withProperty(BlockStateTierMachine.typeProperty, tiers.tier);
            }
        }
        return state;
    }

    @Override
    public Block getBlock() {
        return this;
    }


    @Override
    public int getGuiID() {
        return -1;
    }


    @Override
    public TileEntity getTileEntity() {
        return new TileEntityTierRadioactiveWasteBarrel();
    }

    @Override
    public Block getMachineBlock() {
        return MEKCeuMoreMachineBlocks.TierRadioactiveWasteBarrel;
    }


    @Nonnull
    @Override
    @Deprecated
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        return TANK;
    }
}
