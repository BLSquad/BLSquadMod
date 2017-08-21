package me.yario.blsquad.gui;

import cpw.mods.fml.client.config.GuiCheckBox;
import cpw.mods.fml.common.gameevent.TickEvent;
import me.yario.blsquad.BlSquadMain;
import me.yario.blsquad.utils.FontUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiPlayerInfo;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GuiMutePlayer extends GuiScreen {

    private static boolean isEnabled = false;
    private String nickname;
    private String muteReason;
    private static GuiTextField playerName;
    private static GuiButton mute;
    private static GuiTextField reason;
    private static GuiComboBox reasonBox;
    private static GuiTextField dateNumber;
    private static GuiComboBox dateString;
    private GuiCheckBox permanent;
    private String tabulationText;
    private String nameHistory;
    private boolean lastKeyIsTab;
    private static int tick = 3;
    private static int tick2 = 40;

    public GuiMutePlayer()
    {

    }

    public GuiMutePlayer(String nickname)
    {
        this.nickname = nickname;
    }

    public GuiMutePlayer(String nickname, String muteReason)
    {
        this.nickname = nickname;
        this.muteReason = muteReason;
    }

    @Override
    public void handleMouseInput() {
        reasonBox.handleMouseInput();
        dateString.handleMouseInput();
        super.handleMouseInput();
    }

    public static void tickEvent(TickEvent.RenderTickEvent event)
    {
        if(Keyboard.isKeyDown(Keyboard.KEY_BACK) && tick == 0)
        {
            if (dateNumber.isFocused())
                dateNumber.deleteFromCursor(-1);
            if(tick2 == 0) {
                if (playerName.isFocused())
                    playerName.deleteFromCursor(-1);
                if (reason.isFocused())
                    reason.deleteFromCursor(-1);
                if (dateNumber.isFocused())
                    dateNumber.deleteFromCursor(-1);
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
            FontUtils font1 = new FontUtils(Font.createFont(Font.TRUETYPE_FONT, new File(ClassLoader.getSystemClassLoader().getResource(".").getPath(), "assets/blsquadmod/MagmaWave Caps.otf")));
            font1.drawString("Mute", this.width/2 - font1.getWidth("Mute")/2, this.height/2-105, Color.WHITE.getRGB());
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
            if(!dateNumber.getText().equals("") || permanent.isChecked())
            {
                if(!reason.getText().equals("") || !reasonBox.getSelectedValue().equals("NONE"))
                {
                    mute.enabled = true;
                }
                else
                    mute.enabled = false;
            }
            else
                mute.enabled = false;
        }
        else
            mute.enabled = false;

        drawString(this.mc.fontRenderer,"Player name:", this.width/2-204, this.height/2-63 -this.mc.fontRenderer.FONT_HEIGHT, Color.WHITE.getRGB());
        drawString(this.mc.fontRenderer,"Reason:", this.width/2+4, this.height/2-63 -this.mc.fontRenderer.FONT_HEIGHT, Color.WHITE.getRGB());
        drawString(this.mc.fontRenderer,"Date:", this.width/2-204, this.height/2-16 - this.mc.fontRenderer.FONT_HEIGHT -2, Color.WHITE.getRGB());

        if(permanent.isChecked())
        {
            dateNumber.setEnabled(false);
            dateString.setExpanded(false);
            dateString.setEnabled(false);
        }
        else
        {
            dateNumber.setEnabled(true);
            dateString.setEnabled(true);
        }

        if(reasonBox.getSelectedValue().equals("NONE"))
            reason.setEnabled(true);
        else
            reason.setEnabled(false);
        playerName.drawTextBox();
        reason.drawTextBox();
        dateNumber.drawTextBox();
        permanent.drawButton(this.mc, mouseX, mouseY);
        super.drawScreen(mouseX, mouseY, partialTicks);
        dateString.drawComboBox(mouseX, mouseY, partialTicks);
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
        if (typedChar >= '0' && typedChar <= '9')
            dateNumber.textboxKeyTyped(typedChar, keyCode);
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
                if (!permanent.isChecked()) {
                    try {
                        if (mc.func_147104_D() != null)
                            cmdString = BlSquadMain.getSettings().getTempMuteCommandByServer(mc.func_147104_D().serverIP);
                        else
                            cmdString = "Only work on multiplayer !";
                    }
                    catch (IOException e)
                    {
                        cmdString = "An error occurred !";
                        e.printStackTrace();
                    }
                    if(cmdString == null)
                        cmdString = "/tempmute &name &date &reason";
                    cmdString = cmdString.replace("&name", playerName.getText());
                    cmdString = cmdString.replace("&date", dateNumber.getText() + dateString.getSelectedValue());
                    if(reasonBox.getSelectedValue().equals("NONE"))
                        cmdString = cmdString.replace("&reason", reason.getText());
                    else
                    {
                        cmdString = cmdString.replace("&reason", reasonBox.getSelectedValue());
                    }
                } else {
                    try {
                        if (mc.func_147104_D() != null)
                            cmdString = BlSquadMain.getSettings().getMuteCommandByServer(mc.func_147104_D().serverIP);
                        else
                            cmdString = "Only work on multiplayer !";
                    }
                    catch (IOException e)
                    {
                        cmdString = "An error occurred !";
                        e.printStackTrace();
                    }
                    if(cmdString == null)
                        cmdString = "/mute &name &reason";
                    cmdString = cmdString.replace("&name", playerName.getText());
                    cmdString = cmdString.replace("&reason", reason.getText());
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
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        playerName.mouseClicked(mouseX, mouseY, mouseButton);
        reason.mouseClicked(mouseX, mouseY, mouseButton);
        dateNumber.mouseClicked(mouseX, mouseY, mouseButton);
        dateString.mouseClicked(mouseX, mouseY, mouseButton);
        reasonBox.mouseClicked(mouseX, mouseY, mouseButton);
        permanent.mousePressed(this.mc, mouseX, mouseY);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public static boolean isEnabled()
    {
        return isEnabled;
    }

    @Override
    public void initGui() {
        isEnabled = true;
        permanent = new GuiCheckBox(1, this.width/2-68, this.height/2-10,"Permanent", false);
        playerName = new GuiTextField(this.mc.fontRenderer, this.width/2-204, this.height/2-61, 200, 20);
        playerName.setMaxStringLength(16);
        reason = new GuiTextField(this.mc.fontRenderer, this.width/2+4, this.height/2-61, 200, 20);
        reason.setMaxStringLength(100);
        dateNumber = new GuiTextField(this.mc.fontRenderer, this.width/2-204, this.height/2-16, 20, 20);
        dateNumber.setMaxStringLength(2);
        java.util.List<String> dateValues = new ArrayList<String>();
        dateValues.add("year");
        dateValues.add("month");
        dateValues.add("week");
        dateValues.add("day");
        dateValues.add("hour");
        dateValues.add("minute");
        dateValues.add("second");
        dateString = new GuiComboBox(this.width/2-180, this.height/2-16, 100, 20, dateValues);
        java.util.List<String> reasonValues = new ArrayList<String>();
        reasonValues.add("NONE");
        reasonValues.add("Disrespect");
        reasonValues.add("Staff Disrespect");
        reasonValues.add("Racism");
        reasonValues.add("Spamming");
        reasonValues.add("Forbidden adversiting");
        reasonValues.add("Useless message");
        reasonBox = new GuiComboBox(this.width/2+4, this.height/2-16, 200, 20,reasonValues);
        if(nickname != null)
            playerName.setText(nickname);
        if(muteReason != null)
        {
            if(reasonValues.contains(muteReason))
                reasonBox.setSelectedValue(muteReason);
            else
            {
                reasonBox.setSelectedValue("NONE");
                reason.setText(muteReason);
            }
        }
        mute = new GuiButton(0, this.width/2-97, this.height/2 + this.mc.fontRenderer.FONT_HEIGHT +2, 98, 20, "Mute this player");
        this.buttonList.add(mute);
        this.buttonList.add(new GuiButton(1, this.width/2-97, this.height/2+22 + this.mc.fontRenderer.FONT_HEIGHT +2, 98, 20, "Cancel"));
        super.initGui();
    }
}
