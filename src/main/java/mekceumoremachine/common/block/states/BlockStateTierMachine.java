package mekceumoremachine.common.block.states;

import mekanism.common.block.states.BlockStateFacing;
import mekceumoremachine.common.block.BlockTierMachine;
import mekceumoremachine.common.tier.MachineTier;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;

public class BlockStateTierMachine extends BlockStateFacing {

    public static final PropertyEnum<MachineTier> typeProperty = PropertyEnum.create("tier", MachineTier.class);

    public BlockStateTierMachine(BlockTierMachine block){
        super(block, typeProperty);
    }


    public BlockStateTierMachine(BlockTierMachine block, PropertyBool activeProperty){
        super(block, typeProperty,activeProperty);
    }

}
