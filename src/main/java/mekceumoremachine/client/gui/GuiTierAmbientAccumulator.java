package mekceumoremachine.client.gui;

import mekanism.client.gui.GuiMekanismTile;
import mekanism.client.gui.element.*;
import mekanism.client.gui.element.gauge.GuiGasGauge;
import mekanism.client.gui.element.gauge.GuiGauge;
import mekanism.client.gui.element.slot.GuiEnergySlot;
import mekanism.client.gui.element.slot.GuiOutputSlot;
import mekanism.client.gui.element.tab.GuiSecurityTab;
import mekanism.client.gui.element.tab.GuiSideConfigurationTab;
import mekanism.client.gui.element.tab.GuiUpgradeTab;

import mekanism.common.util.LangUtils;
import mekanism.common.util.MekanismUtils;
import mekceumoremachine.common.inventory.container.ContainerTierAmbientAccumulator;
import mekceumoremachine.common.tile.machine.TileEntityTierAmbientAccumulator;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;

public class GuiTierAmbientAccumulator extends GuiMekanismTile<TileEntityTierAmbientAccumulator> {

    public GuiTierAmbientAccumulator(InventoryPlayer inventory, TileEntityTierAmbientAccumulator tile) {
        super(tile, new ContainerTierAmbientAccumulator(inventory, tile));
        ResourceLocation resource = getGuiLocation();
        addGuiElement(new GuiSecurityTab(this, tileEntity, resource));
        addGuiElement(new GuiRedstoneControl(this, tileEntity, resource));
        addGuiElement(new GuiUpgradeTab(this, tileEntity, resource));
        addGuiElement(new GuiSideConfigurationTab(this, tileEntity, resource));
        addGuiElement(new GuiEnergyInfo(() -> {
            String usage = MekanismUtils.getEnergyDisplay(tileEntity.clientEnergyUsed);
            return Arrays.asList(LangUtils.localize("gui.using") + ": " + usage + "/t", LangUtils.localize("gui.needed") + ": " + MekanismUtils.getEnergyDisplay(tileEntity.getNeedEnergy()));
        }, this, resource));
        addGuiElement(new GuiGasGauge(() -> tileEntity.outputTank, GuiGauge.Type.WIDE, this, getGuiLocation(), 95, 13).withColor(GuiGauge.TypeColor.ORANGE));
        addGuiElement(new GuiPowerBarLong(this, tileEntity, resource, 165, 9));
        addGuiElement(new GuiEnergySlot(this, resource, 135, 66,tileEntity));
        addGuiElement(new GuiOutputSlot(this, resource, 102, 66,tileEntity).with(GuiSlot.SlotOverlay.PLUS));
        addGuiElement(new GuiInnerScreen(this, resource, 7, 13, 80, 65));
        addGuiElement(new GuiPlayerSlot(this, resource,7,88));
        ySize += 5;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        renderScaledText(tileEntity.getName(), (xSize / 2) - (fontRenderer.getStringWidth(tileEntity.getName()) / 2), 4, 0x404040,174);
        fontRenderer.drawString(LangUtils.localize("container.inventory"), 8, (ySize - 94) + 2, 0x404040);
        renderScaledText(LangUtils.localize("gui.dimensionId") + ":" + tileEntity.getWorld().provider.getDimension(), 8, 14, 0x33ff99);
        renderScaledText(LangUtils.localize("gui.dimensionName") + ":", 8, 23, 0x33ff99);
        renderScaledText(tileEntity.getWorld().provider.getDimensionType().getName(), 8, 32, 0x33ff99);
        if (tileEntity.getRecipe() != null){
            renderScaledText(LangUtils.localize("gui.dimensionGas") + ":", 8, 41, 0x33ff99);
            renderScaledText(tileEntity.getRecipe().getOutput().output.getGas().getLocalizedName(), 8, 50, 0x33ff99);
            float Chance = Math.round(tileEntity.getRecipe().getOutput().primaryChance * 100);
            renderScaledText(LangUtils.localize("gui.probability") + ":" + Chance + "%", 8, 59, 0x33ff99);
            renderScaledText(tileEntity.outputTank.getStored() + " / " + tileEntity.outputTank.getMaxGas(), 8, 68, 0x33ff99);
        }else {
            renderScaledText(LangUtils.localize("gui.dimensionNoGas"), 8, 41, 0x33ff99);
            renderScaledText(tileEntity.outputTank.getStored() + " / " + tileEntity.outputTank.getMaxGas(), 8, 50, 0x33ff99);
        }
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }


    public void renderScaledText(String text, int x, int y, int color) {
        renderScaledText(text,x,y,color,78);
    }
}
