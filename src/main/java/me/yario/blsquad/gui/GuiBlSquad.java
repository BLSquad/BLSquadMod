package me.yario.blsquad.gui;

import me.yario.blsquad.BlSquadMain;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;

public class GuiBlSquad extends GuiScreen{

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GL11.glColor4f(1, 1, 1, 1);
        GL11.glScalef(0.25f,0.25f, 0.25f);
        this.mc.getTextureManager().bindTexture(new ResourceLocation("blsquadmod", "logo.png"));
        drawTexturedModalRect(this.width/2*4-128, this.height/2*4-420, 0, 0, 256, 256);
        GL11.glScalef(4f,4f, 4f);
        drawString(Minecraft.getMinecraft().fontRenderer, "Made by Yario & Controversed", this.width-Minecraft.getMinecraft().fontRenderer.getStringWidth("Made by Yario & Controversed")-1, this.height-Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT-1, Color.CYAN.getRGB());
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch(button.id)
        {
            case 0:
                this.mc.displayGuiScreen(new GuiBanPlayer());
                break;
            case 1:
                this.mc.displayGuiScreen(new GuiMutePlayer());
                break;
            case 2:
                this.mc.displayGuiScreen(new GuiWarnPlayer());
                break;
            case 3:
                this.mc.displayGuiScreen(new GuiFreezePlayer());
                break;
            case 4:
                this.mc.displayGuiScreen(new GuiKickPlayer());
                break;
            case 5:
                this.mc.displayGuiScreen(new GuiBLOptions());
                break;
            case 6:
                this.mc.displayGuiScreen(null);
                break;
        }
        super.actionPerformed(button);
    }

    @Override
    public void initGui() {


        try {
            if(mc.func_147104_D() != null) {
                String permCmdString = BlSquadMain.getSettings().getCommandByServer(mc.func_147104_D().serverIP);
                String tempCmdString = BlSquadMain.getSettings().getTempCommandByServer(mc.func_147104_D().serverIP);
                String warnPermCmdString = BlSquadMain.getSettings().getWarnCommandByServer(mc.func_147104_D().serverIP);
                String kickPermCmdString = BlSquadMain.getSettings().getKickCommandByServer(mc.func_147104_D().serverIP);
                String mutePermCmdString = BlSquadMain.getSettings().getMuteCommandByServer(mc.func_147104_D().serverIP);
                String muteTempCmdString = BlSquadMain.getSettings().getTempMuteCommandByServer(mc.func_147104_D().serverIP);
                String freezeCmdString = BlSquadMain.getSettings().getFreezeCommandByServer(mc.func_147104_D().serverIP);
                String unfreezeCmdString = BlSquadMain.getSettings().getUnfreezeCommandByServer(mc.func_147104_D().serverIP);
                if (permCmdString == null)
                    BlSquadMain.getSettings().setServerForCommand(mc.func_147104_D().serverIP, "/ban &name &reason");
                if (tempCmdString == null)
                    BlSquadMain.getSettings().setServerForTempCommand(mc.func_147104_D().serverIP, "/ban &name &date &reason");
                if (warnPermCmdString == null)
                    BlSquadMain.getSettings().setServerForWarnCommand(mc.func_147104_D().serverIP, "/warn &name &reason");
                if (kickPermCmdString == null)
                    BlSquadMain.getSettings().setServerForKickCommand(mc.func_147104_D().serverIP, "/kick &name &reason");
                if (mutePermCmdString == null)
                    BlSquadMain.getSettings().setServerForMuteCommand(mc.func_147104_D().serverIP, "/mute &name &reason");
                if (muteTempCmdString == null)
                    BlSquadMain.getSettings().setServerForTempMuteCommand(mc.func_147104_D().serverIP, "/tempmute &name &date &reason");
                if (freezeCmdString == null)
                    BlSquadMain.getSettings().setServerForFreezeCommand(mc.func_147104_D().serverIP, "/freeze &name");
                if (unfreezeCmdString == null)
                    BlSquadMain.getSettings().setServerForUnfreezeCommand(mc.func_147104_D().serverIP, "/unfreeze &name");
            }

        } catch (IOException e)
        {
            e.printStackTrace();
        }

        this.buttonList.add(new GuiButton(0, this.width/2-204, this.height/2-40, "Ban"));
        this.buttonList.add(new GuiButton(1, this.width/2+4, this.height/2-40, "Mute"));
        this.buttonList.add(new GuiButton(2, this.width/2-204, this.height/2-18,"Warn"));
        this.buttonList.add(new GuiButton(3, this.width/2+4, this.height/2-18,"Freeze"));
        this.buttonList.add(new GuiButton(4, this.width/2-204, this.height/2+4,"Kick"));
        this.buttonList.add(new GuiButton(5, this.width/2+4, this.height/2+4,"Settings"));
        this.buttonList.add(new GuiButton(6, this.width/2-100, this.height/2+26,"Exit"));
        super.initGui();
    }


}
