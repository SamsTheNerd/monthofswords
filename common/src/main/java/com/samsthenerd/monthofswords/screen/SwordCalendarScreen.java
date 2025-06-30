package com.samsthenerd.monthofswords.screen;

import com.samsthenerd.monthofswords.SwordsMod;
import com.samsthenerd.monthofswords.registry.SwordsModItems;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import org.lwjgl.glfw.GLFW;

public class SwordCalendarScreen extends Screen {
    public SwordCalendarScreen() {
        super(Text.literal("Sword Calendar"));
    }

    public static final Identifier CALENDAR_BACK = SwordsMod.id("textures/gui/calendar_back.png");
    public static final Identifier CALENDAR_STICKERS = SwordsMod.id("textures/gui/calendar_stickers.png");

    public static final int CALENDAR_WIDTH = 256;
    public static final int CALENDAR_HEIGHT = 208;

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        int guiX = (context.getScaledWindowWidth()-CALENDAR_WIDTH)/2;
        int guiY = (context.getScaledWindowHeight()-CALENDAR_HEIGHT)/2;
        super.render(context, mouseX, mouseY, delta);
        int hoverSwordIdx = getSwordAtCoordinate(mouseX - guiX, mouseY - guiY);
        context.drawTexture(CALENDAR_BACK, guiX, guiY, 100, 0, 0, CALENDAR_WIDTH, CALENDAR_HEIGHT, 512, 512);
        for(int i = 0; i < SwordsModItems.ALL_SWORDS.size(); i++){
            if(hoverSwordIdx != i) continue;
            Identifier swordId = SwordsModItems.ALL_SWORDS.get(i);
            Item swordItem = Registries.ITEM.get(swordId);
            var swordPos = getSwordPosition(i);
            int swordX = swordPos.getLeft()  + guiX;
            int swordY = swordPos.getRight() + guiY;
            context.drawTexture(CALENDAR_STICKERS, swordX, swordY, 101, swordPos.getLeft(), swordPos.getRight(),
                32, 32, 512, 512);
            context.drawItem(swordItem.getDefaultStack(), swordX+8, swordY+8);
        }
        if(hoverSwordIdx != -1){
            Identifier swordId = SwordsModItems.ALL_SWORDS.get(hoverSwordIdx);
            Item swordItem = Registries.ITEM.get(swordId);
            context.drawItemTooltip(MinecraftClient.getInstance().textRenderer, swordItem.getDefaultStack(), mouseX, mouseY);
//            context.drawTooltip(MinecraftClient.getInstance().textRenderer, swordItem.getName(), mouseX, mouseY);
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if(keyCode == GLFW.GLFW_KEY_E){
            this.close();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    public int getSwordAtCoordinate(int x, int y){
        if(x < 16 || y < 12) return -1;
        int ix = (x - 16) / 32;
        int iy = (y - 12) / 32;
        int mx = (x-16) % 32;
        int my = (y-12) % 32;
        if( mx < 2 || mx > 29 || my < 2 || my > 29) return -1;
        if(ix > 6 || ix < 0 || iy > 4 || iy < 0) return -1;
        int idx = iy * 7 + ix;
        if(idx == 29) return -1;
        if(idx == 34) idx = 29;
        if(idx > 29) return -1;
        return idx;
    }

    public static Pair<Integer, Integer> getSwordPosition(int i){
        int idx = i;
        if(i == 29) idx = 34;
        int x = idx % 7;
        int y = idx / 7;
        int xPx = (x * 32) + 16;
        int yPx = (y * 32) + 12;
        return new Pair<>(xPx, yPx);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
