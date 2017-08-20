package me.yario.blsquad.gui;

import cpw.mods.fml.client.config.GuiCheckBox;
import cpw.mods.fml.common.gameevent.TickEvent;
import me.yario.blsquad.BlSquadMain;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.IOException;

public class GuiBLOptions extends GuiScreen{

    private String gui = "commands";
    private static boolean isEnabled = false;
    private static GuiTextField freezecmd;
    private static GuiTextField unfreezecmd;
    private static GuiTextField tempcmd;
    private static GuiTextField permcmd;
    private static GuiTextField mutePermcmd;
    private static GuiTextField muteTempcmd;
    private static GuiTextField warnPermcmd;
    private static GuiTextField kickPermcmd;
    private static GuiCheckBox enableKeys;
    private static GuiCheckBox checkPlayer;
    private static GuiCheckBox freezePlayer;
    private static GuiCheckBox muteMessage;
    private static int tick = 5;
    private static int tick2 = 40;

    public static boolean isEnabled()
    {
        return isEnabled;
    }

    public static void tickEvent(TickEvent.RenderTickEvent event)
    {
        if(Keyboard.isKeyDown(Keyboard.KEY_BACK) && tick == 0)
        {
            if(tick2 == 0) {
                if (tempcmd.isFocused())
                    tempcmd.deleteFromCursor(-1);
                if (permcmd.isFocused())
                    permcmd.deleteFromCursor(-1);
                if (freezecmd.isFocused())
                    freezecmd.deleteFromCursor(-1);
                if (unfreezecmd.isFocused())
                    unfreezecmd.deleteFromCursor(-1);
                if(warnPermcmd.isFocused())
                    warnPermcmd.deleteFromCursor(-1);
                if(kickPermcmd.isFocused())
                    kickPermcmd.deleteFromCursor(-1);
                if(mutePermcmd.isFocused())
                    mutePermcmd.deleteFromCursor(-1);
                if(muteTempcmd.isFocused())
                    muteTempcmd.deleteFromCursor(-1);
                tick = 1;
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
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(gui.equals("commands")) {
            tempcmd.mouseClicked(mouseX, mouseY, mouseButton);
            permcmd.mouseClicked(mouseX, mouseY, mouseButton);
            freezecmd.mouseClicked(mouseX, mouseY, mouseButton);
            unfreezecmd.mouseClicked(mouseX, mouseY, mouseButton);
            warnPermcmd.mouseClicked(mouseX, mouseY, mouseButton);
            kickPermcmd.mouseClicked(mouseX, mouseY, mouseButton);
            muteTempcmd.mouseClicked(mouseX, mouseY, mouseButton);
            mutePermcmd.mouseClicked(mouseX, mouseY, mouseButton);
        }
        else if (gui.equals("choices"))
        {
            enableKeys.mousePressed(this.mc, mouseX, mouseY);
            checkPlayer.mousePressed(this.mc, mouseX, mouseY);
            freezePlayer.mousePressed(this.mc, mouseX, mouseY);
            muteMessage.mousePressed(this.mc, mouseX, mouseY);
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch(button.id)
        {
            case 0:
                try {
                    if (mc.func_147104_D() != null) {
                        BlSquadMain.getSettings().setServerForTempCommand(mc.func_147104_D().serverIP, tempcmd.getText());
                        BlSquadMain.getSettings().setServerForCommand(mc.func_147104_D().serverIP, permcmd.getText());
                        BlSquadMain.getSettings().setServerForTempMuteCommand(mc.func_147104_D().serverIP, muteTempcmd.getText());
                        BlSquadMain.getSettings().setServerForMuteCommand(mc.func_147104_D().serverIP, mutePermcmd.getText());
                        BlSquadMain.getSettings().setServerForWarnCommand(mc.func_147104_D().serverIP, warnPermcmd.getText());
                        BlSquadMain.getSettings().setServerForKickCommand(mc.func_147104_D().serverIP, kickPermcmd.getText());
                        BlSquadMain.getSettings().setServerForFreezeCommand(mc.func_147104_D().serverIP, freezecmd.getText());
                        BlSquadMain.getSettings().setServerForUnfreezeCommand(mc.func_147104_D().serverIP, unfreezecmd.getText());
                    }
                    BlSquadMain.getSettings().saveCheckBox("enableKeys", enableKeys.isChecked());
                    BlSquadMain.getSettings().saveCheckBox("checkPlayer", checkPlayer.isChecked());
                    BlSquadMain.getSettings().saveCheckBox("freezePlayer", freezePlayer.isChecked());
                    BlSquadMain.getSettings().saveCheckBox("muteMessage", muteMessage.isChecked());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                this.mc.displayGuiScreen(new GuiBlSquad());
                break;
            case 2:
                gui = "choices";
                break;
            case 3:
                gui = "commands";
                break;
        }
        super.actionPerformed(button);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if(gui.equals("commands")) {
            tempcmd.textboxKeyTyped(typedChar, keyCode);
            permcmd.textboxKeyTyped(typedChar, keyCode);
            freezecmd.textboxKeyTyped(typedChar, keyCode);
            unfreezecmd.textboxKeyTyped(typedChar, keyCode);
            warnPermcmd.textboxKeyTyped(typedChar, keyCode);
            kickPermcmd.textboxKeyTyped(typedChar, keyCode);
            muteTempcmd.textboxKeyTyped(typedChar, keyCode);
            mutePermcmd.textboxKeyTyped(typedChar, keyCode);
        }
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if(gui.equals("commands")) {
            drawString(this.mc.fontRenderer, "Freeze command:", this.width / 2 - 204, this.height / 2 - 90 - this.mc.fontRenderer.FONT_HEIGHT - 2, Color.WHITE.getRGB());
            drawString(this.mc.fontRenderer, "Unfreeze command:", this.width / 2 + 4, this.height / 2 - 90 - this.mc.fontRenderer.FONT_HEIGHT - 2, Color.WHITE.getRGB());
            drawString(this.mc.fontRenderer, "Temp ban command:", this.width / 2 - 204, this.height / 2 - 55 - this.mc.fontRenderer.FONT_HEIGHT - 2, Color.WHITE.getRGB());
            drawString(this.mc.fontRenderer, "Perm ban command:", this.width / 2 + 4, this.height / 2 - 55 - this.mc.fontRenderer.FONT_HEIGHT - 2, Color.WHITE.getRGB());
            drawString(this.mc.fontRenderer, "Temp mute command:", this.width / 2 - 204, this.height / 2 - 20 - this.mc.fontRenderer.FONT_HEIGHT - 2, Color.WHITE.getRGB());
            drawString(this.mc.fontRenderer, "Perm mute command:", this.width / 2 + 4, this.height / 2 - 20 - this.mc.fontRenderer.FONT_HEIGHT - 2, Color.WHITE.getRGB());
            drawString(this.mc.fontRenderer, "Warn command:", this.width / 2 + 4, this.height / 2 + 4, Color.WHITE.getRGB());
            drawString(this.mc.fontRenderer, "Kick command:", this.width / 2 - 204, this.height / 2 + 4, Color.WHITE.getRGB());
            tempcmd.drawTextBox();
            warnPermcmd.drawTextBox();
            freezecmd.drawTextBox();
            unfreezecmd.drawTextBox();
            kickPermcmd.drawTextBox();
            permcmd.drawTextBox();
            muteTempcmd.drawTextBox();
            mutePermcmd.drawTextBox();
        }
        else if (gui.equals("choices"))
        {
            enableKeys.drawButton(this.mc, mouseX, mouseY);
            checkPlayer.drawButton(this.mc, mouseX, mouseY);
            freezePlayer.drawButton(this.mc, mouseX, mouseY);
            muteMessage.drawButton(this.mc, mouseX, mouseY);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void onGuiClosed() {
        isEnabled = false;
        super.onGuiClosed();
    }

    @Override
    public void initGui() {
        isEnabled = true;
        enableKeys = new GuiCheckBox(0, this.width/2-204, this.height/2-55, "Enable Keys?", true);
        checkPlayer = new GuiCheckBox(1, this.width/2+4, this.height/2-55, "Check player on pointing?", false);
        freezePlayer = new GuiCheckBox(2, this.width/2-204, this.height/2-33, "Freeze player on pointing?", false);
        muteMessage = new GuiCheckBox(3, this.width/2+4, this.height/2-33, "Show message with insult(s)?", false);

        this.buttonList.add(new GuiButton(0, this.width/2-204, this.height/2+52 + this.mc.fontRenderer.FONT_HEIGHT +2, "Save settings"));
        this.buttonList.add(new GuiButton(1, this.width/2+4, this.height/2+52 + this.mc.fontRenderer.FONT_HEIGHT +2, "Cancel"));
        this.buttonList.add(new GuiButton(2, this.width / 2 + 4, this.height / 2 + 30 + this.mc.fontRenderer.FONT_HEIGHT + 2, "Choices"));
        this.buttonList.add(new GuiButton(3, this.width / 2 - 204, this.height / 2 + 30 + this.mc.fontRenderer.FONT_HEIGHT + 2, "Commands"));
        tempcmd = new GuiTextField(this.mc.fontRenderer, this.width/2-204, this.height/2-55, 200, 20);
        tempcmd.setMaxStringLength(100);
        permcmd = new GuiTextField(this.mc.fontRenderer, this.width/2+4, this.height/2-55, 200, 20);
        permcmd.setMaxStringLength(100);
        freezecmd = new GuiTextField(this.mc.fontRenderer, this.width/2-204, this.height/2-90, 200, 20);
        freezecmd.setMaxStringLength(100);
        unfreezecmd = new GuiTextField(this.mc.fontRenderer, this.width/2+4, this.height/2-90, 200, 20);
        unfreezecmd.setMaxStringLength(100);
        warnPermcmd = new GuiTextField(this.mc.fontRenderer, this.width/2+4, this.height/2+4 + this.mc.fontRenderer.FONT_HEIGHT +2, 200, 20);
        warnPermcmd.setMaxStringLength(100);
        kickPermcmd = new GuiTextField(this.mc.fontRenderer, this.width/2-204, this.height/2+4 + this.mc.fontRenderer.FONT_HEIGHT +2, 200, 20);
        kickPermcmd.setMaxStringLength(100);
        muteTempcmd = new GuiTextField(this.mc.fontRenderer, this.width/2-204, this.height/2-20, 200, 20);
        muteTempcmd.setMaxStringLength(100);
        mutePermcmd = new GuiTextField(this.mc.fontRenderer, this.width/2+4, this.height/2-20, 200, 20);
        mutePermcmd.setMaxStringLength(100);

        String tempCmdString = null;
        String permCmdString = null;
        String warnPermCmdString = null;
        String kickPermCmdString = null;
        String mutePermCmdString = null;
        String muteTempCmdString = null;
        String freezeCmdString = null;
        String unfreezeCmdString = null;
        Boolean enableKeysB = null;
        Boolean checkPlayerB = null;
        Boolean freezePlayerB = null;
        Boolean muteMessageB = null;
        try {
            if(mc.func_147104_D() != null) {
                permCmdString = BlSquadMain.getSettings().getCommandByServer(mc.func_147104_D().serverIP);
                tempCmdString = BlSquadMain.getSettings().getTempCommandByServer(mc.func_147104_D().serverIP);
                warnPermCmdString = BlSquadMain.getSettings().getWarnCommandByServer(mc.func_147104_D().serverIP);
                kickPermCmdString = BlSquadMain.getSettings().getKickCommandByServer(mc.func_147104_D().serverIP);
                mutePermCmdString = BlSquadMain.getSettings().getMuteCommandByServer(mc.func_147104_D().serverIP);
                muteTempCmdString = BlSquadMain.getSettings().getTempMuteCommandByServer(mc.func_147104_D().serverIP);
                freezeCmdString = BlSquadMain.getSettings().getFreezeCommandByServer(mc.func_147104_D().serverIP);
                unfreezeCmdString = BlSquadMain.getSettings().getUnfreezeCommandByServer(mc.func_147104_D().serverIP);
            }
            enableKeysB = BlSquadMain.getSettings().getCheckBox("enableKeys");
            checkPlayerB = BlSquadMain.getSettings().getCheckBox("checkPlayer");
            freezePlayerB = BlSquadMain.getSettings().getCheckBox("freezePlayer");
            muteMessageB = BlSquadMain.getSettings().getCheckBox("muteMessage");
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        if(tempCmdString != null)
            tempcmd.setText(tempCmdString);
        else
            tempcmd.setText("/ban &name &date &reason");
        if(permCmdString != null)
            permcmd.setText(permCmdString);
        else
            permcmd.setText("/ban &name &reason");
        if(warnPermCmdString != null)
            warnPermcmd.setText(warnPermCmdString);
        else
            warnPermcmd.setText("/warn &name &reason");
        if(kickPermCmdString != null)
            kickPermcmd.setText(kickPermCmdString);
        else
            kickPermcmd.setText("/kick &name &reason");
        if(mutePermCmdString != null)
            mutePermcmd.setText(mutePermCmdString);
        else
            mutePermcmd.setText("/mute &name &reason");
        if(muteTempCmdString != null)
            muteTempcmd.setText(muteTempCmdString);
        else
            muteTempcmd.setText("/tempmute &name &date &reason");
        if(freezeCmdString != null)
            freezecmd.setText(freezeCmdString);
        else
            freezecmd.setText("/freeze &name");
        if(unfreezeCmdString != null)
            unfreezecmd.setText(unfreezeCmdString);
        else
            unfreezecmd.setText("/unfreeze &name");
        if(enableKeysB != null)
            enableKeys.setIsChecked(enableKeysB);
        if(checkPlayerB != null)
            checkPlayer.setIsChecked(checkPlayerB);
        if(freezePlayerB != null)
            freezePlayer.setIsChecked(freezePlayerB);
        if(muteMessageB != null)
            muteMessage.setIsChecked(muteMessageB);
        super.initGui();
    }
}
