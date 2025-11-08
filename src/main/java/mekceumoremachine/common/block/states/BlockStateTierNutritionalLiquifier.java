package mekceumoremachine.common.block.states;

import it.unimi.dsi.fastutil.ints.Int2ReferenceMap;
import it.unimi.dsi.fastutil.ints.Int2ReferenceOpenHashMap;
import mekanism.common.base.IBlockType;
import mekanism.common.block.states.BlockStateFacing;
import mekanism.common.block.states.BlockStateUtils;
import mekceumoremachine.common.MEKCeuMoreMachine;
import mekceumoremachine.common.block.BlockTierNutritionalLiquifier;
import mekceumoremachine.common.registries.MEKCeuMoreMachineBlocks;
import mekceumoremachine.common.tier.MachineTier;
import mekceumoremachine.common.tile.machine.TierNutritional.*;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Plane;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class BlockStateTierNutritionalLiquifier extends ExtendedBlockState {


    public BlockStateTierNutritionalLiquifier(BlockTierNutritionalLiquifier block, PropertyEnum<?> typeProperty) {
        super(block, new IProperty[]{BlockStateFacing.facingProperty, typeProperty, BlockStateActiveTierMachine.activeProperty, BlockStateTierMachine.typeProperty}, new IUnlistedProperty[]{});
    }

    public enum TierNutritionalLiquifierMachineBlock {
        MACHINE_BLOCK;

        PropertyEnum<MachineType> machineTypeProperty;

        public PropertyEnum<MachineType> getProperty() {
            if (machineTypeProperty == null) {
                machineTypeProperty = PropertyEnum.create("type", MachineType.class, input -> input != null && input.typeBlock == this && input.isValidMachine());
            }
            return machineTypeProperty;
        }

        public Block getBlock() {
            return switch (this) {
                case MACHINE_BLOCK -> MEKCeuMoreMachineBlocks.TierNutritionalLiquifier;
            };
        }
    }

    public enum MachineType implements IStringSerializable, IBlockType {
        TIER_NUTRITIONAL_LIQUIFIER_BASIC(0, TileEntityTierNutritionalLiquifierBasic::new, Plane.HORIZONTAL,MachineTier.BASIC),
        TIER_NUTRITIONAL_LIQUIFIER_ADVANCED(1, TileEntityTierNutritionalLiquifierAdvanced::new, Plane.HORIZONTAL,MachineTier.ADVANCED),
        TIER_NUTRITIONAL_LIQUIFIER_ELITE(2, TileEntityTierNutritionalLiquifierElite::new, Plane.HORIZONTAL,MachineTier.ELITE),
        TIER_NUTRITIONAL_LIQUIFIER_ULTIMATE(3, TileEntityTierNutritionalLiquifierUltimate::new, Plane.HORIZONTAL,MachineTier.ULTIMATE);

        private static final Map<TierNutritionalLiquifierMachineBlock, Int2ReferenceMap<MachineType>> VALID_METAS = new EnumMap<>(TierNutritionalLiquifierMachineBlock.class);

        static {
            Arrays.stream(values()).forEach(MachineType::registerType);
        }

        public final TierNutritionalLiquifierMachineBlock typeBlock = TierNutritionalLiquifierMachineBlock.MACHINE_BLOCK;
        public int meta;
        public final String blockName = "TierNutritionalLiquifier";
        public Supplier<TileEntity> tileEntitySupplier;
        public boolean isElectric = true;
        public boolean hasModel = false;
        public boolean supportsUpgrades = true;
        public Predicate<EnumFacing> facingPredicate;
        public boolean activable = true;
        public boolean isFullBlock = false;
        public boolean isOpaqueCube = false;
        public MachineTier tier;


        MachineType(int m, Supplier<TileEntity> tileClass, Predicate<EnumFacing> predicate,MachineTier machineTier) {
            meta = m;
            tileEntitySupplier = tileClass;
            facingPredicate = predicate;
            tier = machineTier;
        }

        private void registerType() {
            VALID_METAS.computeIfAbsent(typeBlock, k -> new Int2ReferenceOpenHashMap<>()).put(meta, this);
        }

        private static final List<MachineType> VALID_MACHINES = new ArrayList<>();

        static {
            Arrays.stream(MachineType.values()).filter(MachineType::isValidMachine).forEach(VALID_MACHINES::add);
        }

        public static List<MachineType> getValidMachines() {
            return VALID_MACHINES;
        }

        public static MachineType get(IBlockState state) {
            if (state.getBlock() instanceof BlockTierNutritionalLiquifier machine) {
                return state.getValue(machine.getTypeProperty());
            }
            return null;
        }

        public static MachineType get(Block block, int meta) {
            if (block instanceof BlockTierNutritionalLiquifier machine) {
                return get(machine.getMachineBlock(), meta);
            }
            return null;
        }

        public static MachineType get(TierNutritionalLiquifierMachineBlock block, int meta) {
            Int2ReferenceMap<MachineType> meta2Type = VALID_METAS.get(block);
            if (meta2Type == null) {
                return null;
            }
            return meta2Type.get(meta);
        }

        public static MachineType get(ItemStack stack) {
            return get(Block.getBlockFromItem(stack.getItem()), stack.getItemDamage());
        }

        @Override
        public String getBlockName() {
            return blockName;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }

        public boolean isValidMachine() {
            return true;
        }

        public TileEntity create() {
            return this.tileEntitySupplier != null ? this.tileEntitySupplier.get() : null;
        }

        public ItemStack getStack() {
            return new ItemStack(typeBlock.getBlock(), 1, meta);
        }

        @Override
        public String getName() {
            return name().toLowerCase(Locale.ROOT);
        }

        @Override
        public String toString() {
            return getName();
        }

        public boolean canRotateTo(EnumFacing side) {
            return facingPredicate.test(side);
        }

        public boolean hasRotations() {
            return !facingPredicate.equals(BlockStateUtils.NO_ROTATION);
        }

        public boolean hasActiveTexture() {
            return activable;
        }
    }

    public static class MachineBlockStateMapper extends StateMapperBase {
        @Nonnull
        @Override
        protected ModelResourceLocation getModelResourceLocation(@Nonnull IBlockState state) {
            BlockTierNutritionalLiquifier block = (BlockTierNutritionalLiquifier) state.getBlock();
            MachineType type = state.getValue(block.getTypeProperty());
            StringBuilder builder = new StringBuilder();
            if (type.hasActiveTexture()) {
                builder.append(BlockStateActiveTierMachine.activeProperty.getName());
                builder.append("=");
                builder.append(state.getValue(BlockStateActiveTierMachine.activeProperty));
            }

            if (type.hasRotations()) {
                EnumFacing facing = state.getValue(BlockStateFacing.facingProperty);
                if (!type.canRotateTo(facing)) {
                    facing = EnumFacing.NORTH;
                }
                if (builder.length() > 0) {
                    builder.append(",");
                }
                builder.append(BlockStateFacing.facingProperty.getName());
                builder.append("=");
                builder.append(facing.getName());
            }

            if (builder.length() == 0) {
                builder.append("normal");
            }

            ResourceLocation baseLocation = new ResourceLocation(MEKCeuMoreMachine.MODID, type.getName());
            return new ModelResourceLocation(baseLocation, builder.toString());
        }
    }
}
