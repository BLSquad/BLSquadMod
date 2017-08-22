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

public class GuiFreezePlayer extends GuiScreen {

    private static boolean isEnabled = false;
    private static GuiTextField playerName;
    private String tabulationText;
    private String name = "";
    private GuiButton freeze;
    private GuiButton unfreeze;
    private String nameHistory;
    private boolean lastKeyIsTab;
    private static int tick = 3;
    private static int tick2 = 40;

    public GuiFreezePlayer()
    {

    }

    public GuiFreezePlayer(String name)
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
            font1.drawString("Freeze", this.width/2 - font1.getWidth("Freeze")/2, this.height/2-105, Color.WHITE.getRGB());
        } catch (FontFormatException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        if(!playerName.getText().equals(""))
        {
            freeze.enabled = true;
            unfreeze.enabled = true;
        }
        else {
            freeze.enabled = false;
            unfreeze.enabled = false;
        }

        drawString(this.mc.fontRenderer,"Player name:", this.width/2-204, this.height/2-24, Color.WHITE.getRGB());

        playerName.drawTextBox();
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
                        cmdString = BlSquadMain.getSettings().getFreezeCommandByServer(mc.func_147104_D().serverIP);
                    else
                        cmdString = "Only work on multiplayer !";
                }
                catch (IOException e)
                {
                    cmdString = "An error occurred !";
                    e.printStackTrace();
                }
                if(cmdString == null)
                    cmdString = "/freeze &name";
                cmdString = cmdString.replace("&name", playerName.getText());
                this.mc.thePlayer.sendChatMessage(cmdString);
                this.mc.displayGuiScreen(null);
                break;
            case 1:
                String ucmdString;
                try {
                    if (mc.func_147104_D() != null)
                        ucmdString = BlSquadMain.getSettings().getUnfreezeCommandByServer(mc.func_147104_D().serverIP);
                    else
                        ucmdString = "Only work on multiplayer !";
                }
                catch (IOException e)
                {
                    ucmdString = "An error occurred !";
                    e.printStackTrace();
                }
                if(ucmdString == null)
                    ucmdString = "/unfreeze &name";
                ucmdString = ucmdString.replace("&name", playerName.getText());
                this.mc.thePlayer.sendChatMessage(ucmdString);
                this.mc.displayGuiScreen(null);
                break;
            case 2:
                this.mc.displayGuiScreen(new GuiBlSquad());
                break;
        }

        super.actionPerformed(button);
    }



    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        playerName.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public static boolean isEnabled()
    {
        return isEnabled;
    }

    @Override
    public void initGui() {
        isEnabled = true;
        playerName = new GuiTextField(this.mc.fontRenderer, this.width/2-204, this.height/2-24 + this.mc.fontRenderer.FONT_HEIGHT +2, 200, 20);
        playerName.setMaxStringLength(16);
        playerName.setText(this.name);
        freeze = new GuiButton(0, this.width/2+4, this.height/2-24 + this.mc.fontRenderer.FONT_HEIGHT +2, "Freeze this player");
        this.buttonList.add(freeze);
        unfreeze = new GuiButton(1, this.width/2+4, this.height/2 + this.mc.fontRenderer.FONT_HEIGHT +2, "Unfreeze this player");
        this.buttonList.add(unfreeze);
        this.buttonList.add(new GuiButton(2, this.width/2-204, this.height/2 + this.mc.fontRenderer.FONT_HEIGHT +2, "Cancel"));
        super.initGui();
    }
}
