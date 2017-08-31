package me.yario.blsquad.gui;

import cpw.mods.fml.common.gameevent.TickEvent;
import me.yario.blsquad.BlSquadMain;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiPlayerInfo;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class GuiKickPlayer extends GuiScreen {

    private static boolean isEnabled = false;
    private static GuiTextField playerName;
    private static GuiTextField reason;
    private static GuiComboBox reasonBox;
    private static GuiButton kick;
    private String tabulationText;
    private String name = "";
    private String nameHistory;
    private boolean lastKeyIsTab;
    private static int tick = 3;
    private static int tick2 = 40;

    public GuiKickPlayer()
    {

    }
    public GuiKickPlayer(String name)
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

        BlSquadMain.font.drawString("Kick", this.width/2 - BlSquadMain.font.getWidth("Kick")/2, this.height/2-105, Color.WHITE.getRGB());

        if(!playerName.getText().equals("")) {
            if (!reason.getText().equals("") || !reasonBox.getSelectedValue().equals("NONE")) {
                kick.enabled = true;
            } else
                kick.enabled = false;
        }
        else
            kick.enabled = false;

        if(reasonBox.getSelectedValue().equals("NONE"))
            reason.setEnabled(true);
        else
            reason.setEnabled(false);
        drawString(this.mc.fontRenderer,"Player name:", this.width/2-204, this.height/2-24 -this.mc.fontRenderer.FONT_HEIGHT, Color.WHITE.getRGB());
        drawString(this.mc.fontRenderer,"Reason:", this.width/2+4, this.height/2-24 - this.mc.fontRenderer.FONT_HEIGHT, Color.WHITE.getRGB());

        playerName.drawTextBox();
        reason.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
        reasonBox.drawComboBox(mouseX, mouseY, partialTicks);
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
                        cmdString = BlSquadMain.getSettings().getKickCommandByServer(mc.func_147104_D().serverIP);
                    else
                        cmdString = "Only work on multiplayer !";
                }
                catch (IOException e)
                {
                    cmdString = "An error occurred !";
                    e.printStackTrace();
                }
                if(cmdString == null)
                    cmdString = "/kick &name &reason";
                cmdString = cmdString.replace("&name", playerName.getText());
                if(reasonBox.getSelectedValue().equals("NONE"))
                    cmdString = cmdString.replace("&reason", reason.getText());
                else
                {
                    cmdString = cmdString.replace("&reason", reasonBox.getSelectedValue());
                }
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
    public void handleMouseInput() {
        reasonBox.handleMouseInput();
        super.handleMouseInput();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        playerName.mouseClicked(mouseX, mouseY, mouseButton);
        reason.mouseClicked(mouseX, mouseY, mouseButton);
        reasonBox.mouseClicked(mouseX, mouseY, mouseButton);
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
        java.util.List<String> reasonValues = new ArrayList<String>();
        reasonValues.add("NONE");
        reasonValues.add("Usebug");
        reasonValues.add("Camping");
        reasonValues.add("Disrespect");
        reasonValues.add("Staff Disrespect");
        reasonBox = new GuiComboBox(this.width/2+4, this.height/2 + this.mc.fontRenderer.FONT_HEIGHT +2, 200, 20, reasonValues);
        kick = new GuiButton(0, this.width/2-204, this.height/2 + this.mc.fontRenderer.FONT_HEIGHT +2, "Kick this player");
        this.buttonList.add(kick);
        this.buttonList.add(new GuiButton(1, this.width/2-204, this.height/2+22 + this.mc.fontRenderer.FONT_HEIGHT +2, "Cancel"));
        super.initGui();
    }
}
