package me.yario.blsquad.commands;

import me.yario.blsquad.BlSquadMain;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

public class CommandBlsmute extends CommandBase{

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender p_71519_1_) {
        return true;
    }

    @Override
    public void processCommand(ICommandSender p_71515_1_, String[] args) {
        switch(args.length)
        {
            case 0:
                break;
            case 1:
                BlSquadMain.playerToMute = args[0];
                break;
            default:
                BlSquadMain.playerToMute = args[0];
                BlSquadMain.reasonToMute = "";
                for(int i = 1 ; i < args.length ; i++)
                    BlSquadMain.reasonToMute += args[i] + " ";
                break;
        }
        BlSquadMain.openMuteGui = true;
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_) {
        return "/blsmute";
    }

    @Override
    public String getCommandName() {
        return "blsmute";
    }
}
