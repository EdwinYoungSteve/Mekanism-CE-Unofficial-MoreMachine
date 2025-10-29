package mekceumoremachine.client.gui;

import mekanism.client.gui.GuiMekanismTile;
import mekanism.client.gui.element.*;
import mekanism.client.gui.element.gauge.GuiFluidGauge;
import mekanism.client.gui.element.gauge.GuiGauge;
import mekanism.client.gui.element.slot.GuiEnergySlot;
import mekanism.client.gui.element.slot.GuiNormalSlot;
import mekanism.client.gui.element.tab.GuiSecurityTab;
import mekanism.client.gui.element.tab.GuiUpgradeTab;
import mekanism.common.util.LangUtils;
import mekanism.common.util.MekanismUtils;
import mekceumoremachine.common.inventory.container.ContainerTierElectricPump;
import mekceumoremachine.common.tile.machine.TileEntityTierElectricPump;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class GuiTierElectricPump extends GuiMekanismTile<TileEntityTierElectricPump> {

    public GuiTierElectricPump(InventoryPlayer inventory, TileEntityTierElectricPump tile) {
        super(tile, new ContainerTierElectricPump(inventory, tile));
        ResourceLocation resource = getGuiLocation();
        addGuiElement(new GuiNormalSlot(this, resource, 27, 19));
        addGuiElement(new GuiNormalSlot(this, resource, 27, 50));
        addGuiElement(new GuiEnergySlot(this, resource, 142, 34, tileEntity));
        addGuiElement(new GuiPowerBar(this, tileEntity, resource, 164, 15));
        addGuiElement(new GuiFluidGauge(() -> tileEntity.fluidTank, GuiGauge.Type.STANDARD, this, resource, 6, 13));
        addGuiElement(new GuiEnergyInfo(tileEntity, this, resource));
        addGuiElement(new GuiSecurityTab(this, tileEntity, resource));
        addGuiElement(new GuiRedstoneControl(this, tileEntity, resource));
        addGuiElement(new GuiUpgradeTab(this, tileEntity, resource));
        addGuiElement(new GuiInnerScreen(this, resource, 48, 23, 80, 41));
        addGuiElement(new GuiPlayerSlot(this, resource));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(int xAxis, int yAxis) {
        super.drawGuiContainerBackgroundLayer(xAxis, yAxis);
        mc.getTextureManager().bindTexture(MekanismUtils.getResource(MekanismUtils.ResourceType.GUI, "Other_Icon.png"));
        drawTexturedModalRect(guiLeft + 32, guiTop + 39, 13, 0, 8, 9);
        boolean input = tileEntity.fluidTank.getFluidAmount() == tileEntity.fluidTank.getCapacity();
        boolean energy = tileEntity.getEnergy() < tileEntity.energyPerTick || tileEntity.getEnergy() == 0;
        if (input) {
            mc.getTextureManager().bindTexture(MekanismUtils.getResource(MekanismUtils.ResourceType.GUI, "Warning.png"));
            drawTexturedModalRect(guiLeft + 6 + 9, guiTop + 13 + 1, 9, 1, 8, 29);
            drawTexturedModalRect(guiLeft + 6 + 9, guiTop + 13 + 31, 9, 32, 8, 28);
        }
        if (input || energy) {
            mc.getTextureManager().bindTexture(MekanismUtils.getResource(MekanismUtils.ResourceType.TAB, "Warning_Info.png"));
            drawTexturedModalRect(guiLeft - 26, guiTop + 112, 0, 0, 26, 26);
            addGuiElement(new GuiWarningInfo(this, getGuiLocation(), false));
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRenderer.drawString(tileEntity.getName(), (xSize / 2) - (fontRenderer.getStringWidth(tileEntity.getName()) / 2), 4, 0x404040);
        fontRenderer.drawString(LangUtils.localize("container.inventory"), 8, (ySize - 94) + 2, 0x404040);
        fontRenderer.drawString(MekanismUtils.getEnergyDisplay(tileEntity.getEnergy(), tileEntity.getMaxEnergy()), 51, 26, 0xFF3CFE9A);
        String text = tileEntity.fluidTank.getFluid() != null ? LangUtils.localizeFluidStack(tileEntity.fluidTank.getFluid()) + ": " + tileEntity.fluidTank.getFluid().amount
                : LangUtils.localize("gui.noFluid");
        renderScaledText(text, 51, 35, 0xFF3CFE9A, 74);

        int xAxis = mouseX - guiLeft;
        int yAxis = mouseY - guiTop;
        if (xAxis >= -21 && xAxis <= -3 && yAxis >= 116 && yAxis <= 134) {
            List<String> info = new ArrayList<>();
            boolean energy = tileEntity.getEnergy() < tileEntity.energyPerTick || tileEntity.getEnergy() == 0;
            boolean input = tileEntity.fluidTank.getFluidAmount() == tileEntity.fluidTank.getCapacity();
            if (energy) {
                info.add(LangUtils.localize("gui.no_energy"));
            }
            if (input) {
                info.add(LangUtils.localize("gui.fluid_no_space"));
            }
            if (input || energy) {
                this.displayTooltips(info, xAxis, yAxis);
            }
        }

        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }


}
