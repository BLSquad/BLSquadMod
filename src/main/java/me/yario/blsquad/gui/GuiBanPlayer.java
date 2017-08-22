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
import java.io.IOException;
import java.util.ArrayList;

public class GuiBanPlayer extends GuiScreen {

    private static boolean isEnabled = false;
    private static GuiTextField playerName;
    private String name = "";
    private static GuiTextField reason;
    private static GuiComboBox reasonBox;
    private static GuiTextField dateNumber;
    private static GuiComboBox dateString;
    private static GuiButton ban;
    private GuiCheckBox permanent;
    private GuiCheckBox screenshared;
    private String tabulationText;
    private String nameHistory;
    private boolean lastKeyIsTab;
    private java.util.List<String> cheatingReasonValues = new ArrayList<String>();
    private static int tick = 3;
    private static int tick2 = 40;

    public GuiBanPlayer()
    {

    }

    public GuiBanPlayer(String name)
    {
        this.name = name;
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

        if(!playerName.getText().equals(""))
        {
            if(!dateNumber.getText().equals("") || permanent.isChecked())
            {
                if(!reason.getText().equals("") || !reasonBox.getSelectedValue().equals("NONE"))
                {
                    ban.enabled = true;
                }
                else
                    ban.enabled = false;
            }
            else
                ban.enabled = false;
        }
        else
            ban.enabled = false;

        try {
            FontUtils font1 = new FontUtils(Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/assets/blsquadmod/MagmaWaveCaps.otf")));
            font1.drawString("Ban", this.width/2 - font1.getWidth("Ban")/2, this.height/2-105, Color.WHITE.getRGB());
        } catch (FontFormatException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
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
        screenshared.drawButton(this.mc, mouseX, mouseY);
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
                            cmdString = BlSquadMain.getSettings().getTempCommandByServer(mc.func_147104_D().serverIP);
                        else
                            cmdString = "Only work on multiplayer !";
                    }
                    catch (IOException e)
                    {
                        cmdString = "An error occurred !";
                        e.printStackTrace();
                    }
                    if(cmdString == null)
                        cmdString = "/ban &name &date &reason";
                    cmdString = cmdString.replace("&name", playerName.getText());
                    cmdString = cmdString.replace("&date", dateNumber.getText() + dateString.getSelectedValue());
                    if(reasonBox.getSelectedValue().equals("NONE"))
                        cmdString = cmdString.replace("&reason", reason.getText() + (screenshared.isChecked() ? " [SS]" : ""));
                    else
                    {
                        if(cheatingReasonValues.contains(reasonBox.getSelectedValue()))
                        {
                            cmdString = cmdString.replace("&reason", "Cheating : " + reasonBox.getSelectedValue() + (screenshared.isChecked() ? " [SS]" : ""));
                        }
                        else
                        {
                            cmdString = cmdString.replace("&reason", reasonBox.getSelectedValue() + (screenshared.isChecked() ? " [SS]" : ""));
                        }
                    }
                } else {
                    try {
                        if (mc.func_147104_D() != null)
                            cmdString = BlSquadMain.getSettings().getCommandByServer(mc.func_147104_D().serverIP);
                        else
                            cmdString = "Only work on multiplayer !";
                    }
                    catch (IOException e)
                    {
                        cmdString = "An error occurred !";
                        e.printStackTrace();
                    }
                    if(cmdString == null)
                        cmdString = "/ban &name &reason";
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
        screenshared.mousePressed(this.mc, mouseX, mouseY);
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
        screenshared = new GuiCheckBox(1, this.width/2+4, this.height/2-30,"Screenshared?", false);
        playerName = new GuiTextField(this.mc.fontRenderer, this.width/2-204, this.height/2-61, 200, 20);
        playerName.setMaxStringLength(16);
        playerName.setText(this.name);
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
        cheatingReasonValues.add("NONE");
        cheatingReasonValues.add("Autoclick");
        cheatingReasonValues.add("Triggerbot");
        cheatingReasonValues.add("Aimbot");
        cheatingReasonValues.add("Anti-Knockback");
        cheatingReasonValues.add("FlyHack");
        cheatingReasonValues.add("Forcefield / KillAura");
        cheatingReasonValues.add("SpeedHack");
        cheatingReasonValues.add("Reach");
        cheatingReasonValues.add("Hitboxes");
        cheatingReasonValues.add("NoFall");
        cheatingReasonValues.add("Xray");
        cheatingReasonValues.add("Cheating Confession");
        cheatingReasonValues.add("ScreenShare Denied");
        java.util.List<String> reasonValues = new ArrayList<String>();
        reasonValues.add("Hack Threats");
        reasonValues.add("Disrespect");
        reasonValues.add("Staff Disrespect");
        reasonValues.add("Incorrect username");
        reasonValues.add("Incorrect skin");
        reasonValues.add("Racism");
        reasonValues.add("Forbidden adversiting");
        reasonValues.add("Forbidden link");
        reasonValues.add("Usebug");
        reasonValues.add("Ban Evading");
        reasonBox = new GuiComboBox(this.width/2+4, this.height/2-16, 200, 20,cheatingReasonValues);
        reasonBox.addValues(reasonValues);
        ban = new GuiButton(0, this.width/2-97, this.height/2 + this.mc.fontRenderer.FONT_HEIGHT +2, 98, 20, "Ban this player");
        this.buttonList.add(ban);
        this.buttonList.add(new GuiButton(1, this.width/2-97, this.height/2+22 + this.mc.fontRenderer.FONT_HEIGHT +2, 98, 20, "Cancel"));
        super.initGui();
    }
}