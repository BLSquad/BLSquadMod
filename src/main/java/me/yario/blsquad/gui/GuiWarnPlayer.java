package me.yario.blsquad.gui;

import cpw.mods.fml.common.gameevent.TickEvent;
import me.yario.blsquad.BlSquadMain;
import me.yario.blsquad.utils.FontUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiPlayerInfo;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.IOException;

public class GuiWarnPlayer extends GuiScreen {

    private static boolean isEnabled = false;
    private static GuiTextField playerName;
    private String name = "";
    private static GuiTextField reason;
    private static GuiButton warn;
    private String tabulationText;
    private String nameHistory;
    private boolean lastKeyIsTab;
    private static int tick = 3;
    private static int tick2 = 40;

    public GuiWarnPlayer()
    {

    }
    public GuiWarnPlayer(String name)
    {
        this.name = name;
    }

    public static void tickEvent(TickEvent.RenderTickEvent event)
    {
        if(Keyboard.isKeyDown(Keyboard.KEY_BACK) && tick == 0)
        {
            if(tick2 == 0) {
                if (playerName.isFocused())
                    playerName.deleteFromCursor(-1);
                if (reason.isFocused())
                    reason.deleteFromCursor(-1);
                tick = 3;
            }
            else{
                tick2--;
            }
        }
        if(!Keyboard.isKeyDown(Keyboard.KEY_BACK))
            tick2 = 40;
        if(tick != 0)
            tick--;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {


        try {
            FontUtils font1 = new FontUtils(Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/assets/blsquadmod/MagmaWaveCaps.otf")));
            font1.drawString("Warn", this.width/2 - font1.getWidth("Warn")/2, this.height/2-105, Color.WHITE.getRGB());
        } catch (FontFormatException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        if(!playerName.getText().equals("")) {
            if (!reason.getText().equals("")) {
                warn.enabled = true;
            } else
                warn.enabled = false;
        }
        else
            warn.enabled = false;

        drawString(this.mc.fontRenderer,"Player name:", this.width/2-204, this.height/2-24 -this.mc.fontRenderer.FONT_HEIGHT, Color.WHITE.getRGB());
        drawString(this.mc.fontRenderer,"Reason:", this.width/2+4, this.height/2-24 - this.mc.fontRenderer.FONT_HEIGHT, Color.WHITE.getRGB());

        playerName.drawTextBox();
        reason.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if(playerName.isFocused())
        {
            if(keyCode == Keyboard.KEY_TAB && !playerName.getText().equals(""))
            {
                if(!lastKeyIsTab) {
                    tabulationText = playerName.getText();
                    lastKeyIsTab = true;
                }
                for(Object playerInfoObject : this.mc.getNetHandler().playerInfoList)
                {
                    GuiPlayerInfo playerInfo = (GuiPlayerInfo) playerInfoObject;
                    if(playerInfo.name.toLowerCase().startsWith(tabulationText.toLowerCase()) && !nameHistory.contains("-" + playerInfo.name + "-"))
                    {
                        nameHistory += "-" + playerInfo.name + "-";
                        playerName.setText(playerInfo.name);
                        break;
                    }
                }
            }
            else
            {
                nameHistory = "";
                lastKeyIsTab = false;
                tabulationText = "";
            }
        }
        playerName.textboxKeyTyped(typedChar, keyCode);
        reason.textboxKeyTyped(typedChar, keyCode);
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void onGuiClosed() {

        isEnabled = false;
        super.onGuiClosed();
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0:
                String cmdString;
                try {
                    if (mc.func_147104_D() != null)
                        cmdString = BlSquadMain.getSettings().getWarnCommandByServer(mc.func_147104_D().serverIP);
                    else
                        cmdString = "Only work on multiplayer !";
                }
                catch (IOException e)
                {
                    cmdString = "An error occurred !";
                    e.printStackTrace();
                }
                if(cmdString == null)
                    cmdString = "/warn &name &reason";
                cmdString = cmdString.replace("&name", playerName.getText());
                cmdString = cmdString.replace("&reason", reason.getText());

                this.mc.thePlayer.sendChatMessage(cmdString);
                this.mc.displayGuiScreen(null);
                break;
            case 1:
                this.mc.displayGuiScreen(new GuiBlSquad());
                break;
        }

        super.actionPerformed(button);
    }



    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        playerName.mouseClicked(mouseX, mouseY, mouseButton);
        reason.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public static boolean isEnabled()
    {
        return isEnabled;
    }

    @Override
    public void initGui() {
        isEnabled = true;
        playerName = new GuiTextField(this.mc.fontRenderer, this.width/2-204, this.height/2-22, 200, 20);
        playerName.setMaxStringLength(16);
        playerName.setText(this.name);
        reason = new GuiTextField(this.mc.fontRenderer, this.width/2+4, this.height/2-22, 200, 20);
        reason.setMaxStringLength(100);
        warn = new GuiButton(0, this.width/2+4, this.height/2+2, "Warn this player");
        this.buttonList.add(warn);
        this.buttonList.add(new GuiButton(1, this.width/2-204, this.height/2+2, "Cancel"));
        super.initGui();
    }
}
