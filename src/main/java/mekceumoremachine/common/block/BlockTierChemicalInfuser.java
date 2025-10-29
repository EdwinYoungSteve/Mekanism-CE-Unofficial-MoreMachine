package mekceumoremachine.common.block;

import mekceumoremachine.common.block.states.BlockStateTierMachine;
import mekceumoremachine.common.registries.MEKCeuMoreMachineBlocks;
import mekceumoremachine.common.tile.machine.TileEntityTierChemicalInfuser;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.jetbrains.annotations.NotNull;

public class BlockTierChemicalInfuser extends BlockTierMachine {

    public BlockTierChemicalInfuser() {
        super();
    }

    @Override
    public IBlockState AddActualState(@NotNull IBlockState state, IBlockAccess worldIn, BlockPos pos, TileEntity tile) {
        if (tile instanceof TileEntityTierChemicalInfuser tiers) {
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
        return 6;
    }


    @Override
    public TileEntity getTileEntity() {
        return new TileEntityTierChemicalInfuser();
    }

    @Override
    public Block getMachineBlock() {
        return MEKCeuMoreMachineBlocks.TierChemicalInfuser;
    }
}
