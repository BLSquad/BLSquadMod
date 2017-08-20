package me.yario.blsquad;

import java.io.*;

public class BlSquadSettings {

    private File file;

    public BlSquadSettings(File settings) throws IOException
    {
        this.file = settings;
        if(!this.file.exists())
            this.file.createNewFile();
    }

    public void setServerForFreezeCommand(String ip, String command) throws IOException
    {
        String content = "";
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("FREEZE " + ip))
                line = "FREEZE " + ip + ":" + command;
            content += line + '\n';
        }
        if(this.getFreezeCommandByServer(ip) == null)
        {
            content += "FREEZE " + ip + ":" + command + '\n';
        }
        reader.close();
        BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
        writer.write(content);
        writer.close();
    }

    public void setServerForUnfreezeCommand(String ip, String command) throws IOException
    {
        String content = "";
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("UNFREEZE " + ip))
                line = "UNFREEZE " + ip + ":" + command;
            content += line + '\n';
        }
        if(this.getUnfreezeCommandByServer(ip) == null)
        {
            content += "UNFREEZE " + ip + ":" + command + '\n';
        }
        reader.close();
        BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
        writer.write(content);
        writer.close();
    }

    public void setServerForWarnCommand(String ip, String command) throws IOException
    {
        String content = "";
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("WARN " + ip))
                line = "WARN " + ip + ":" + command;
            content += line + '\n';
        }
        if(this.getWarnCommandByServer(ip) == null)
        {
            content += "WARN " + ip + ":" + command + '\n';
        }
        reader.close();
        BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
        writer.write(content);
        writer.close();
    }

    public void setServerForKickCommand(String ip, String command) throws IOException
    {
        String content = "";
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("KICK " + ip))
                line = "KICK " + ip + ":" + command;
            content += line + '\n';
        }
        if(this.getKickCommandByServer(ip) == null)
        {
            content += "KICK " + ip + ":" + command + '\n';
        }
        reader.close();
        BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
        writer.write(content);
        writer.close();
    }

    public String getWarnCommandByServer(String ip)throws IOException
    {
        String line;
        String finalResult = "";
        BufferedReader reader = new BufferedReader(new FileReader(file));
        while((line = reader.readLine()) != null)
        {
            if(line.contains(ip))
            {
                String[] result = line.split(":");
                if(result.length >= 2) {
                    if(result[0].startsWith("WARN " + ip)) {
                        for (int i = 1; i < result.length; i++) {
                            finalResult += result[i];
                        }
                        reader.close();
                        return finalResult;
                    }
                }
            }
        }
        reader.close();
        return null;
    }

    public String getKickCommandByServer(String ip)throws IOException
    {
        String line;
        String finalResult = "";
        BufferedReader reader = new BufferedReader(new FileReader(file));
        while((line = reader.readLine()) != null)
        {
            if(line.contains(ip))
            {
                String[] result = line.split(":");
                if(result.length >= 2) {
                    if(result[0].startsWith("KICK " + ip)) {
                        for (int i = 1; i < result.length; i++) {
                            finalResult += result[i];
                        }
                        reader.close();
                        return finalResult;
                    }
                }
            }
        }
        reader.close();
        return null;
    }


    public String getFreezeCommandByServer(String ip)throws IOException
    {
        String line;
        String finalResult = "";
        BufferedReader reader = new BufferedReader(new FileReader(file));
        while((line = reader.readLine()) != null)
        {
            if(line.contains(ip))
            {
                String[] result = line.split(":");
                if(result.length >= 2) {
                    if(result[0].startsWith("FREEZE " + ip)) {
                        for (int i = 1; i < result.length; i++) {
                            finalResult += result[i];
                        }
                        reader.close();
                        return finalResult;
                    }
                }
            }
        }
        reader.close();
        return null;
    }

    public String getUnfreezeCommandByServer(String ip)throws IOException
    {
        String line;
        String finalResult = "";
        BufferedReader reader = new BufferedReader(new FileReader(file));
        while((line = reader.readLine()) != null)
        {
            if(line.contains(ip))
            {
                String[] result = line.split(":");
                if(result.length >= 2) {
                    if(result[0].startsWith("UNFREEZE " + ip)) {
                        for (int i = 1; i < result.length; i++) {
                            finalResult += result[i];
                        }
                        reader.close();
                        return finalResult;
                    }
                }
            }
        }
        reader.close();
        return null;
    }

    public String getMuteCommandByServer(String ip) throws IOException
    {
        String line;
        String finalResult = "";
        BufferedReader reader = new BufferedReader(new FileReader(file));
        while((line = reader.readLine()) != null)
        {

            if(line.contains(ip))
            {
                String[] result = line.split(":");
                if(result.length >= 2) {
                    if(result[0].startsWith("MUTE " + ip)) {
                        for (int i = 1; i < result.length; i++) {
                            finalResult += result[i];
                        }
                        reader.close();
                        return finalResult;
                    }
                }
            }
        }
        reader.close();
        return null;
    }

    public String getTempMuteCommandByServer(String ip) throws IOException
    {
        String line;
        String finalResult = "";
        BufferedReader reader = new BufferedReader(new FileReader(file));
        while((line = reader.readLine()) != null)
        {

            if(line.contains(ip))
            {
                String[] result = line.split(":");
                if(result.length >= 2) {
                    if(result[0].startsWith("MUTE TEMP " + ip)) {
                        for (int i = 1; i < result.length; i++) {
                            finalResult += result[i];
                        }
                        reader.close();
                        return finalResult;
                    }
                }
            }
        }
        reader.close();
        return null;
    }

    public void setServerForMuteCommand(String ip, String command) throws IOException
    {
        String content = "";
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("MUTE " + ip))
                line = "MUTE " + ip + ":" + command;
            content += line + '\n';
        }
        if(this.getMuteCommandByServer(ip) == null)
        {
            content += "MUTE " + ip + ":" + command + '\n';
        }
        reader.close();
        BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
        writer.write(content);
        writer.close();
    }

    public void setServerForTempMuteCommand(String ip, String command) throws IOException {
        String content = "";
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("MUTE TEMP " + ip))
                line = "MUTE TEMP " + ip + ":" + command;
            content += line + '\n';
        }
        if(this.getTempMuteCommandByServer(ip) == null)
        {
            content += "MUTE TEMP " + ip + ":" + command + '\n';
        }
        reader.close();
        BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
        writer.write(content);
        writer.close();
    }

    public String getCommandByServer(String ip) throws IOException
    {
        String line;
        String finalResult = "";
        BufferedReader reader = new BufferedReader(new FileReader(file));
        while((line = reader.readLine()) != null)
        {

            if(line.contains(ip))
            {
                String[] result = line.split(":");
                if(result.length >= 2) {
                    if(result[0].startsWith(ip)) {
                        for (int i = 1; i < result.length; i++) {
                            finalResult += result[i];
                        }
                        reader.close();
                        return finalResult;
                    }
                }
            }
        }
        reader.close();
        return null;
    }

    public String getTempCommandByServer(String ip) throws IOException
    {
        String line;
        String finalResult = "";
        BufferedReader reader = new BufferedReader(new FileReader(file));
        while((line = reader.readLine()) != null)
        {

            if(line.contains(ip))
            {
                String[] result = line.split(":");
                if(result.length >= 2) {
                    if(result[0].startsWith("TEMP " + ip)) {
                        for (int i = 1; i < result.length; i++) {
                            finalResult += result[i];
                        }
                        reader.close();
                        return finalResult;
                    }
                }
            }
        }
        reader.close();
        return null;
    }

    public Boolean getCheckBox(String name) throws IOException
    {
        String line;
        String finalResult = "";
        BufferedReader reader = new BufferedReader(new FileReader(file));
        while((line = reader.readLine()) != null)
        {

            if(line.contains(name))
            {
                String[] result = line.split(":");
                if(result.length >= 2) {
                    if(result[0].startsWith(name)) {
                        for (int i = 1; i < result.length; i++) {
                            finalResult += result[i];
                        }
                        reader.close();
                        return Boolean.valueOf(finalResult);
                    }
                }
            }
        }
        reader.close();
        return null;
    }

    public void setServerForCommand(String ip, String command) throws IOException
    {
        String content = "";
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith(ip))
                line = ip + ":" + command;
            content += line + '\n';
        }
        if(this.getCommandByServer(ip) == null)
        {
            content += ip + ":" + command + '\n';
        }
        reader.close();
        BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
        writer.write(content);
        writer.close();
    }

    public void saveCheckBox(String name, boolean checked) throws IOException
    {
        String content = "";
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith(name))
                line = name + ":" + String.valueOf(checked);
            content += line + '\n';
        }
        if(this.getCheckBox(name) == null)
        {
            content += name + ":" + String.valueOf(checked) + '\n';
        }
        reader.close();
        BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
        writer.write(content);
        writer.close();
    }


    public void setServerForTempCommand(String ip, String command) throws IOException {
        String content = "";
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("TEMP " + ip))
                line = "TEMP " + ip + ":" + command;
            content += line + '\n';
        }
        if(this.getTempCommandByServer(ip) == null)
        {
            content += "TEMP " + ip + ":" + command + '\n';
        }
        reader.close();
        BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
        writer.write(content);
        writer.close();
    }

}
