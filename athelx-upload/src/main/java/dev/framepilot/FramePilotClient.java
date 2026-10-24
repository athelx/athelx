package dev.framepilot;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import org.lwjgl.glfw.GLFW;

public final class FramePilotClient implements ClientModInitializer {
    private static final FrameStats STATS = new FrameStats();
    private static boolean overlayEnabled = true;
    private static boolean f8WasDown = false;

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            boolean f8Down = GLFW.glfwGetKey(client.getWindow().getWindow(), GLFW.GLFW_KEY_F8)
                    == GLFW.GLFW_PRESS;
            if (f8Down && !f8WasDown) {
                overlayEnabled = !overlayEnabled;
            }
            f8WasDown = f8Down;
        });

        HudRenderCallback.EVENT.register((graphics, tickCounter) -> {
            Minecraft client = Minecraft.getInstance();
            if (!overlayEnabled || client.player == null || client.options.hideGui) {
                return;
            }

            STATS.sample(System.nanoTime());
            String line1 = "FramePilot | FPS: " + STATS.getFps();
            String line2 = "Frame time: " + String.format("%.2f ms", STATS.getAverageFrameMs());
            graphics.drawString(client.font, line1, 8, 8, 0xE0E0E0, true);
            graphics.drawString(client.font, line2, 8, 20, 0xE0E0E0, true);
            graphics.drawString(client.font, "F8: toggle overlay", 8, 32, 0xA0A0A0, true);
        });
    }

    private static final class FrameStats {
        private static final int WINDOW_SIZE = 60;
        private final long[] frameTimes = new long[WINDOW_SIZE];
        private int next;
        private int count;
        private long lastSample;
        private long lastStatsUpdate;
        private double averageFrameMs;
        private int fps;

        void sample(long now) {
            if (lastSample != 0L) {
                frameTimes[next] = now - lastSample;
                next = (next + 1) % WINDOW_SIZE;
                if (count < WINDOW_SIZE) count++;
            }
            lastSample = now;

            // Recompute at most four times per second, not on every frame.
            if (now - lastStatsUpdate >= 250_000_000L && count > 0) {
                long total = 0L;
                for (int i = 0; i < count; i++) total += frameTimes[i];
                averageFrameMs = (total / (double) count) / 1_000_000.0;
                fps = averageFrameMs > 0.0 ? (int) Math.round(1000.0 / averageFrameMs) : 0;
                lastStatsUpdate = now;
            }
        }

        int getFps() {
            return fps;
        }

        double getAverageFrameMs() {
            return averageFrameMs;
        }
    }
}
