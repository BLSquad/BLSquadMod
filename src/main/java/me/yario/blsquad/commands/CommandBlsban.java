package me.yario.blsquad.commands;

import me.yario.blsquad.BlSquadMain;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

public class CommandBlsban extends CommandBase{

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender p_71519_1_) {
        return true;
    }

    @Override
    public void processCommand(ICommandSender p_71515_1_, String[] p_71515_2_) {
        BlSquadMain.openBanGui = true;
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_) {
        return "/blsban";
    }

    @Override
    public String getCommandName() {
        return "blsban";
    }
}
