package mekceumoremachine.common.registries;


import mekceumoremachine.common.MEKCeuMoreMachine;
import mekceumoremachine.common.item.ItemCompositeTierInstaller;
import mekceumoremachine.common.item.ItemLargeMachineryUpgradeComponents;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;

@ObjectHolder(MEKCeuMoreMachine.MODID)
public class MEKCeuMoreMachineItems {

    public static final Item CompositeTierInstaller = new ItemCompositeTierInstaller();
    public static final Item LargeMachineryUpgradeComponents = new ItemLargeMachineryUpgradeComponents();

    public static void registerItems(IForgeRegistry<Item> registry) {
        registry.register(init(CompositeTierInstaller, "CompositeTierInstaller"));
        if (Loader.isModLoaded("mekanismmultiblockmachine")) {
            registry.register(init(LargeMachineryUpgradeComponents, "LargeMachineryUpgradeComponents"));
        }
    }

    public static Item init(Item item, String name) {
        return item.setTranslationKey(name).setRegistryName(new ResourceLocation(MEKCeuMoreMachine.MODID, name));
    }
}
