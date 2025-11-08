package mekceumoremachine.client.gui;

import mekanism.api.TileNetworkList;
import mekanism.client.gui.GuiMekanismTile;
import mekanism.client.gui.IJeiFactoryRecipe;
import mekanism.client.gui.button.GuiDisableableButton;
import mekanism.client.gui.element.*;
import mekanism.client.gui.element.gauge.GuiGasGauge;
import mekanism.client.gui.element.gauge.GuiGauge;
import mekanism.client.gui.element.slot.GuiEnergySlot;
import mekanism.client.gui.element.slot.GuiInputSlot;
import mekanism.client.gui.element.tab.GuiSecurityTab;
import mekanism.client.gui.element.tab.GuiSideConfigurationTab;
import mekanism.client.gui.element.tab.GuiTransporterConfigTab;
import mekanism.client.gui.element.tab.GuiUpgradeTab;
import mekanism.client.sound.SoundHandler;
import mekanism.common.Mekanism;
import mekanism.common.network.PacketTileEntity;
import mekanism.common.recipe.RecipeHandler;
import mekanism.common.util.LangUtils;
import mekanism.common.util.MekanismUtils;
import mekceumoremachine.client.gui.element.tab.GuiSortingTabTierMachine;
import mekceumoremachine.common.inventory.container.ContainerTierChemicalOxidizer;
import mekceumoremachine.common.tier.MachineTier;
import mekceumoremachine.common.tile.machine.TierOxidizer.TileEntityTierChemicalOxidizer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GuiTierChemicalOxidizer extends GuiMekanismTile<TileEntityTierChemicalOxidizer> implements IJeiFactoryRecipe {

    private GuiButton oldSortingButton;

    public GuiTierChemicalOxidizer(InventoryPlayer inventory, TileEntityTierChemicalOxidizer tile) {
        super(tile, new ContainerTierChemicalOxidizer(inventory, tile));
        ResourceLocation resource = getGuiLocation();
        int xmove = tileEntity.tier == MachineTier.ULTIMATE ? 34 : 0;
        xSize += xmove;
        ySize += 16;
        addGuiElement(new GuiPowerBar(this, tileEntity, resource, 164 + xmove, 15));
        addGuiElement(new GuiUpgradeTab(this, tileEntity, resource, xmove, 0));
        addGuiElement(new GuiSecurityTab(this, tileEntity, resource, xmove, 0));
        addGuiElement(new GuiRedstoneControl(this, tileEntity, resource, xmove, 0));
        int xPlayerOffset = tileEntity.tier == MachineTier.ULTIMATE ? 19 : 0;
        //玩家背包插槽
        addGuiElement(new GuiPlayerSlot(this, resource, 7 + xPlayerOffset, 83 + 16));
        //slot
        //能量插槽
        addGuiElement(new GuiEnergySlot(this, resource, 6, 12, tileEntity));
        //输入插槽
        int Slotlocation = tileEntity.tier == MachineTier.BASIC ? 54 : tileEntity.tier == MachineTier.ADVANCED ? 34 : tileEntity.tier == MachineTier.ELITE ? 28 : 26;
        int xDistance = tileEntity.tier == MachineTier.BASIC ? 38 : tileEntity.tier == MachineTier.ADVANCED ? 26 : 19;
        for (int i = 0; i < tileEntity.tier.processes; i++) {
            //输入插槽
            int finalI = i;
            addGuiElement(new GuiInputSlot(this, resource, Slotlocation + (i * xDistance), 12, new GuiSlot.ISlotInfoHandler() {
                @Override
                public boolean getSlotCanTip() {
                    return tileEntity.inventory.get(tileEntity.getInputSlot(finalI)).isEmpty();
                }
            }));
            addGuiElement(new GuiGasGauge(() -> tile.outPutTanks[finalI], GuiGauge.Type.SMALL, this, resource, Slotlocation + (i * xDistance), 56).withColor(GuiGauge.TypeColor.BLUE));
        }
        addGuiElement(new GuiSideConfigurationTab(this, tileEntity, resource));
        addGuiElement(new GuiTransporterConfigTab(this, 32, tileEntity, resource));
        addGuiElement(new GuiSortingTabTierMachine<>(this, tileEntity, resource));
        addGuiElement(new GuiEnergyInfo(tileEntity, this, resource));

        int xOffset = tileEntity.tier == MachineTier.BASIC ? 57 : tileEntity.tier == MachineTier.ADVANCED ? 37 : tileEntity.tier == MachineTier.ELITE ? 31 : 29;
        for (int i = 0; i < tileEntity.tier.processes; i++) {
            int cacheIndex = i;
            int xPos = xOffset + (i * xDistance);
            addGuiElement(new GuiProgress(new GuiProgress.IProgressInfoHandler() {
                @Override
                public double getProgress() {
                    return tileEntity.getScaledProgress(cacheIndex);
                }
            }, GuiProgress.ProgressBar.DOWN, this, resource, xPos, 33));
        }
    }

    @Override
    public boolean getJeiRecipe(int mouseX, int mouseY) {
        int xAxis = mouseX - guiLeft;
        int yAxis = mouseY - guiTop;
        int xDistance = tileEntity.tier == MachineTier.BASIC ? 38 : tileEntity.tier == MachineTier.ADVANCED ? 26 : 19;
        int xOffset = tileEntity.tier == MachineTier.BASIC ? 57 : tileEntity.tier == MachineTier.ADVANCED ? 37 : tileEntity.tier == MachineTier.ELITE ? 31 : 29;
        for (int i = 0; i < tileEntity.tier.processes; i++) {
            int xPos = xOffset + (i * xDistance);
            if (xAxis >= xPos && xAxis <= xPos + 12 && yAxis >= 33 && yAxis <= 33 + 22) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<String> getRecipe() {
        return Collections.singletonList(RecipeHandler.Recipe.CHEMICAL_OXIDIZER.getJEICategory());
    }

    @Override
    public void initGui() {
        super.initGui();
        buttonList.add(oldSortingButton = new GuiDisableableButton(2, guiLeft - 21, guiTop + 90, 18, 18).with(GuiDisableableButton.ImageOverlay.ROUND_ROBIN));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRenderer.drawString(tileEntity.getName(), (xSize / 2) - (fontRenderer.getStringWidth(tileEntity.getName()) / 2), 4, 0x404040);
        int xOffset = tileEntity.tier == MachineTier.ULTIMATE ? 27 : 8;
        fontRenderer.drawString(LangUtils.localize("container.inventory"), xOffset, (ySize - 93) + 2, 0x404040);
        int xAxis = mouseX - guiLeft;
        int yAxis = mouseY - guiTop;
        if (oldSortingButton.isMouseOver()) {
            List<String> info = new ArrayList<>();
            info.add(LangUtils.localize("gui.factory.autoSort.old") + ":" + LangUtils.transOnOff(tileEntity.oldSorting));
            info.add(LangUtils.localize("gui.factory.autoSort.old.info"));
            info.add(LangUtils.localize("gui.factory.autoSort.old.info2"));
            this.displayTooltips(info, xAxis, yAxis);
        }
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(int xAxis, int yAxis) {
        super.drawGuiContainerBackgroundLayer(xAxis, yAxis);

        mc.getTextureManager().bindTexture(MekanismUtils.getResource(MekanismUtils.ResourceType.GUI, "State.png"));
        drawTexturedModalRect(guiLeft - 10, guiTop + 81, 6, 6, 8, 8);
        drawTexturedModalRect(guiLeft - 9, guiTop + 82, tileEntity.oldSorting ? 0 : 6, 0, 6, 6);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        if (button == oldSortingButton) {
            TileNetworkList data = TileNetworkList.withContents(1);
            Mekanism.packetHandler.sendToServer(new PacketTileEntity.TileEntityMessage(tileEntity, data));
            SoundHandler.playSound(SoundEvents.UI_BUTTON_CLICK);
        }
    }
}
