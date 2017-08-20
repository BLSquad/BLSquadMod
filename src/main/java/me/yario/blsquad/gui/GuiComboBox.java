package me.yario.blsquad.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class GuiComboBox extends Gui {

    private int mouseX;
    private int mouseY;
    private int xPosition;
    private int yPosition;
    private int width;
    private int height;
    private int startAt = 0;
    private boolean isEnabled = true;
    private boolean visible = true;
    private boolean expanded = false;
    private String selectedValue;
    private List<String> values;

    public void setEnabled(boolean enabled)
    {
        this.isEnabled = enabled;
    }

    public void setExpanded(boolean expanded)
    {
        this.expanded = expanded;
    }

    public GuiComboBox(int x, int y, int width, int height)
    {
        this(x, y, width, height, new ArrayList<String>());
    }

    public GuiComboBox(int x, int y, int width, int height, List<String> values)
    {
        this.xPosition = x;
        this.yPosition = y;
        this.width = width;
        this.height = height;
        this.values = new ArrayList<String>();
        if(values.size() == 0)
            this.values.add("");
        else
            this.values.addAll(values);
        this.selectedValue = values.get(0);
    }

    public void addValues(List<String> values)
    {
     this.values.addAll(values);
    }

    public void addValue(String value)
    {
        this.values.add(value);
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseX >= this.xPosition && mouseX <= this.xPosition + this.width && mouseY >= this.yPosition && mouseY <= this.yPosition + this.height && mouseButton == 0) {
            this.expanded = !this.expanded;
            startAt = 0;
        } else if (this.expanded) {
            if (values.size() > 6) {
                int startAtCount = 0;
                int number = 0;
                int i = 20;
                for (String value : this.values) {
                    if (startAtCount != startAt) {
                        startAtCount++;
                        continue;
                    }
                    if (!value.equals(this.selectedValue))
                        number++;
                    if (number == 7)
                        break;
                    if (!value.equals(this.selectedValue)) {
                        if (mouseX >= this.xPosition && mouseX <= this.xPosition + this.width - this.height && mouseY >= this.yPosition + i && mouseY <= this.yPosition + this.height + i && mouseButton == 0) {
                            this.selectedValue = value;
                            this.expanded = false;
                            break;
                        }
                        i+= 20;
                    }
                }
            } else {
                int i = 20;
                for (String value : this.values) {
                    if (!value.equals(this.selectedValue)) {
                        if (mouseX >= this.xPosition && mouseX <= this.xPosition + this.width - this.height && mouseY >= this.yPosition + i && mouseY <= this.yPosition + this.height + i && mouseButton == 0) {
                            this.selectedValue = value;
                            this.expanded = false;
                            break;
                        }
                        i+= 20;
                    }
                }
            }
        }
    }

    public void handleMouseInput()
    {
        if(this.expanded) {
            int m = Mouse.getEventDWheel();

            if (m != 0) {

                if (m > 0 && startAt > 0) {
                    int mouseX = this.mouseX;
                    int mouseY = this.mouseY;

                    int bound = values.size() >= 6 ? 6 : values.size()-1;

                    if (mouseX >= this.xPosition && mouseX <= this.xPosition + this.width - this.height && mouseY >= this.yPosition && mouseY <= this.yPosition + this.height + bound*20) {
                        startAt--;
                    }
                }

                if (m < 0 && startAt < values.size() - 7) {
                    int mouseX = this.mouseX;
                    int mouseY = this.mouseY;

                    int bound = values.size() >= 6 ? 6 : values.size()-1;

                    if (mouseX >= this.xPosition && mouseX <= this.xPosition + this.width - this.height && mouseY >= this.yPosition && mouseY <= this.yPosition + this.height + bound*20) {
                        startAt++;
                    }
                }

            }
        }
    }

    public String getSelectedValue()
    {
        return this.selectedValue;
    }

    public void setSelectedValue(String value)
    {
        if(values.contains(value))
            this.selectedValue = value;
    }

    public void drawComboBox(int mouseX, int mouseY, float partialTicks)
    {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        if(this.visible) {
            if (this.isEnabled) {
                drawRect(this.xPosition, this.yPosition, this.xPosition+this.width - this.height, this.yPosition+this.height, new Color(Color.GRAY.getRed(), Color.GRAY.getGreen(), Color.GRAY.getBlue(), 128).getRGB());
                drawString(Minecraft.getMinecraft().fontRenderer, selectedValue, this.xPosition+5, this.yPosition+(this.height/2-Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT/2), Color.WHITE.getRGB());
                drawRect(this.xPosition + this.width - this.height, this.yPosition, this.xPosition + this.width, this.yPosition+this.height, Color.GRAY.getRGB());
                GL11.glColor4f(1.0f,1.0f,1.0f,1.0f);
                Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("blsquadmod", "arrow.png"));
                if(this.expanded )
                {
                    drawTexturedModalRect(this.xPosition + this.width - this.height, this.yPosition, 20, 0, 20, 20);
                    if(values.size() > 6)
                    {
                        int startAtCount = 0;
                        int number = 0;
                        int i = 20;
                        for(String value : this.values)
                        {
                            if(startAtCount != startAt)
                            {
                                startAtCount++;
                                continue;
                            }
                            if(!value.equals(this.selectedValue))
                                number++;
                            if(number == 7)
                                break;
                            if(!value.equals(this.selectedValue)) {
                                drawRect(this.xPosition, this.yPosition + i, this.xPosition + this.width - this.height, this.yPosition + this.height + i, new Color(Color.GRAY.getRed(), Color.GRAY.getGreen(), Color.GRAY.getBlue(), 128).getRGB());
                                drawString(Minecraft.getMinecraft().fontRenderer, value, this.xPosition + 5, this.yPosition + (this.height / 2 - Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT / 2) + i, Color.WHITE.getRGB());
                                i += 20;
                            }
                        }
                    }
                    else {
                        int i = 20;
                        for (String value : this.values) {
                            if (!value.equals(this.selectedValue)) {
                                drawRect(this.xPosition, this.yPosition + i, this.xPosition + this.width - this.height, this.yPosition + this.height + i, new Color(Color.GRAY.getRed(), Color.GRAY.getGreen(), Color.GRAY.getBlue(), 128).getRGB());
                                drawString(Minecraft.getMinecraft().fontRenderer, value, this.xPosition + 5, this.yPosition + (this.height / 2 - Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT / 2) + i, Color.WHITE.getRGB());
                                i += 20;
                            }
                        }
                    }
                }
                else
                    drawTexturedModalRect(this.xPosition + this.width - this.height, this.yPosition, 0, 0, 20, 20);
            } else {
                drawRect(this.xPosition, this.yPosition, this.xPosition+this.width - this.height, this.yPosition+this.height, new Color(Color.DARK_GRAY.getRed(), Color.DARK_GRAY.getGreen(), Color.DARK_GRAY.getBlue(), 128).getRGB());
                drawString(Minecraft.getMinecraft().fontRenderer, selectedValue, this.xPosition+5, this.yPosition+(this.height/2-Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT/2), Color.GRAY.getRGB());
                drawRect(this.xPosition + this.width - this.height, this.yPosition, this.xPosition + this.width, this.yPosition+this.height, Color.DARK_GRAY.getRGB());
                GL11.glColor4f(0.5f,0.5f,0.5f,0.5f);
                Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("blsquadmod", "arrow.png"));
                drawTexturedModalRect(this.xPosition + this.width - this.height, this.yPosition, 0, 0, 20, 20);
            }
        }
    }


}
