package me.yario.blsquad;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import me.yario.blsquad.commands.*;
import me.yario.blsquad.gui.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiPlayerInfo;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;

import java.io.*;
import java.util.List;

@Mod(modid = "blsquadmod", version = "1.5", acceptedMinecraftVersions = "[1.7.10]")
public class BlSquadMain {

    private static BlSquadSettings settings;
    private static KeyBinding guiKey;
    private static KeyBinding banKey;
    private static KeyBinding muteKey;
    private static KeyBinding warnKey;
    private static KeyBinding kickKey;
    private static KeyBinding freezzeKey;
    private static KeyBinding unfreezeKey;
    public static Minecraft mc = Minecraft.getMinecraft();
    public static boolean openModGui;
    public static boolean openBanGui;
    public static boolean openMuteGui;
    public static String playerToMute;
    public static String reasonToMute;
    public static boolean openWarnGui;
    public static boolean openKickGui;
    public static boolean openFreezeGui;

    @SubscribeEvent
    public void onJoin(FMLNetworkEvent.ClientConnectedToServerEvent event)
    {
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
    }

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event)
    {
        try {
            if (getSettings().getCheckBox("muteMessage")) {
                File insults = new File(mc.mcDataDir, "insults.txt");
                if (!insults.exists()) {
                    try {
                        insults.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                boolean nameFounded = false;
                String playerName = "";
                IChatComponent message = event.message;
                IChatComponent newMesagge = new ChatComponentText("");
                for (Object siblingsObject : message.getSiblings()) {
                    IChatComponent siblings = (IChatComponent) siblingsObject;
                    if (!nameFounded) {
                        for (GuiPlayerInfo playerInfo : (List<GuiPlayerInfo>) mc.getNetHandler().playerInfoList) {
                            if (siblings.getFormattedText().contains(playerInfo.name)) {
                                playerName = playerInfo.name;
                                nameFounded = true;
                                break;
                            }
                        }
                    }
                    for (String word : siblings.getFormattedText().split(" ")) {
                        try {
                            String line;
                            BufferedReader reader = new BufferedReader(new FileReader(insults));
                            while ((line = reader.readLine()) != null) {
                                String insultWord = line.split(":")[0].toLowerCase();
                                if (word.toLowerCase().contains(insultWord) && !playerName.equals("") && !playerName.equals(mc.thePlayer.getDisplayName())) {
                                    siblings.getChatStyle().setColor(EnumChatFormatting.RED);
                                    if (line.split(":").length >= 2)
                                        siblings.setChatStyle(siblings.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/blsmute " + playerName + " " + line.split(":")[1])));
                                    else
                                        siblings.setChatStyle(siblings.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/blsmute " + playerName)));
                                    break;
                                }
                            }
                            reader.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    newMesagge.appendSibling(siblings);
                }
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        try {
            settings = new BlSquadSettings(new File(mc.mcDataDir.getPath(), "blsquadmod.cfg"));
            if (getSettings().getCheckBox("enableKeys") == null)
                getSettings().saveCheckBox("enableKeys", true);
            if (getSettings().getCheckBox("checkPlayer") == null)
                getSettings().saveCheckBox("checkPlayer", false);
            if (getSettings().getCheckBox("freezePlayer") == null)
                getSettings().saveCheckBox("freezePlayer", false);
            if (getSettings().getCheckBox("muteMessage") == null)
                getSettings().saveCheckBox("muteMessage", true);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        guiKey = new KeyBinding("Open BLSquad Menu", Keyboard.KEY_RSHIFT, "BLSquadMod");
        banKey = new KeyBinding("Open Ban Menu", Keyboard.KEY_B, "BLSquadMod");
        muteKey = new KeyBinding("Open Mute Menu", Keyboard.KEY_M, "BLSquadMod");
        warnKey = new KeyBinding("Open Warn Menu", Keyboard.KEY_P, "BLSquadMod");
        kickKey = new KeyBinding("Open Kick Menu", Keyboard.KEY_K, "BLSquadMod");
        freezzeKey = new KeyBinding("Open Freeze Menu", Keyboard.KEY_F, "BLSquadMod");
        unfreezeKey = new KeyBinding("Unfreeze Key", Keyboard.KEY_U, "BLSquadMod");
        ClientRegistry.registerKeyBinding(guiKey);
        ClientRegistry.registerKeyBinding(banKey);
        ClientRegistry.registerKeyBinding(muteKey);
        ClientRegistry.registerKeyBinding(warnKey);
        ClientRegistry.registerKeyBinding(kickKey);
        ClientRegistry.registerKeyBinding(freezzeKey);
        ClientRegistry.registerKeyBinding(unfreezeKey);
        ClientCommandHandler.instance.registerCommand(new CommandBlsmod());
        ClientCommandHandler.instance.registerCommand(new CommandBlsban());
        ClientCommandHandler.instance.registerCommand(new CommandBlsmute());
        ClientCommandHandler.instance.registerCommand(new CommandBlswarn());
        ClientCommandHandler.instance.registerCommand(new CommandBlsfreeze());
        ClientCommandHandler.instance.registerCommand(new CommandBlskick());
        FMLCommonHandler.instance().bus().register(this);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event)
    {
        try {
            if (guiKey.isPressed())
                mc.displayGuiScreen(new GuiBlSquad());
            if (banKey.isPressed() && getSettings().getCheckBox("enableKeys"))
                mc.displayGuiScreen(new GuiBanPlayer(mc.pointedEntity instanceof EntityPlayer && getSettings().getCheckBox("checkPlayer") ? ((EntityPlayer) mc.pointedEntity).getDisplayName() : ""));
            if (muteKey.isPressed() && getSettings().getCheckBox("enableKeys"))
                mc.displayGuiScreen(new GuiMutePlayer(mc.pointedEntity instanceof EntityPlayer && getSettings().getCheckBox("checkPlayer") ? ((EntityPlayer) mc.pointedEntity).getDisplayName() : ""));
            if (warnKey.isPressed() && getSettings().getCheckBox("enableKeys"))
                mc.displayGuiScreen(new GuiWarnPlayer(mc.pointedEntity instanceof EntityPlayer && getSettings().getCheckBox("checkPlayer") ? ((EntityPlayer) mc.pointedEntity).getDisplayName() : ""));
            if (kickKey.isPressed() && getSettings().getCheckBox("enableKeys"))
                mc.displayGuiScreen(new GuiKickPlayer(mc.pointedEntity instanceof EntityPlayer && getSettings().getCheckBox("checkPlayer") ? ((EntityPlayer) mc.pointedEntity).getDisplayName() : ""));
            if (freezzeKey.isPressed() && getSettings().getCheckBox("enableKeys")) {
                if(!getSettings().getCheckBox("freezePlayer"))
                    mc.displayGuiScreen(new GuiFreezePlayer(((EntityPlayer) mc.pointedEntity).getDisplayName()));
                else if(mc.func_147104_D() != null && mc.pointedEntity instanceof EntityPlayer)
                    mc.thePlayer.sendChatMessage(getSettings().getFreezeCommandByServer(mc.func_147104_D().serverIP).replace("&name", mc.pointedEntity instanceof EntityPlayer ? ((EntityPlayer) mc.pointedEntity).getDisplayName() : ""));
            }
            if (unfreezeKey.isPressed() && getSettings().getCheckBox("enableKeys")) {
                if(!getSettings().getCheckBox("freezePlayer"))
                    mc.displayGuiScreen(new GuiFreezePlayer(((EntityPlayer) mc.pointedEntity).getDisplayName()));
                else if(mc.func_147104_D() != null && mc.pointedEntity instanceof EntityPlayer)
                    mc.thePlayer.sendChatMessage(getSettings().getUnfreezeCommandByServer(mc.func_147104_D().serverIP).replace("&name",mc.pointedEntity instanceof EntityPlayer ? ((EntityPlayer) mc.pointedEntity).getDisplayName() : ""));
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.RenderTickEvent event) {
        if (openModGui) {
            openModGui = false;
            mc.displayGuiScreen(new GuiBlSquad());
        }
        if (openBanGui) {
            openBanGui = false;
            mc.displayGuiScreen(new GuiBanPlayer());
        }
        if (openMuteGui) {
            openMuteGui = false;
            if (playerToMute != null) {
                if (reasonToMute != null)
                    mc.displayGuiScreen(new GuiMutePlayer(playerToMute, reasonToMute));
                else
                    mc.displayGuiScreen(new GuiMutePlayer(playerToMute));
            } else
                mc.displayGuiScreen(new GuiMutePlayer());
            playerToMute = null;
            reasonToMute = null;
        }
        if (openWarnGui) {
            openWarnGui = false;
            mc.displayGuiScreen(new GuiWarnPlayer());
        }
        if (openFreezeGui) {
            openFreezeGui = false;
            mc.displayGuiScreen(new GuiFreezePlayer());
        }
        if (openKickGui) {
            openKickGui = false;
            mc.displayGuiScreen(new GuiKickPlayer());
        }
        if (GuiBanPlayer.isEnabled())
            GuiBanPlayer.tickEvent(event);
        if (GuiMutePlayer.isEnabled())
            GuiMutePlayer.tickEvent(event);
        if (GuiWarnPlayer.isEnabled())
            GuiWarnPlayer.tickEvent(event);
        if (GuiFreezePlayer.isEnabled())
            GuiFreezePlayer.tickEvent(event);
        if (GuiBLOptions.isEnabled())
            GuiBLOptions.tickEvent(event);
        if (GuiKickPlayer.isEnabled())
            GuiKickPlayer.tickEvent(event);
    }

    public static BlSquadSettings getSettings()
    {
        return settings;
    }

}
