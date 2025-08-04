package net.leashesp.mod.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.leashesp.mod.*;

public class LeashESP implements ClientModInitializer {
    private static int lastLeashedEntityCount = 0;
    private static long warningEndTime = 0;
    private static long additionEndTime = 0;

    @Override
    public void onInitializeClient() {
        LeashESPConfig.init();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null && LeashESPConfig.getBoolean("showEntityCounter", true)) {
                updateLeashedEntityCount(client.player);
            }
        });

        WorldRenderEvents.AFTER_ENTITIES.register(context -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player != null && LeashESPConfig.getBoolean("showEntityOutline", true)) {
                drawLeadTracers(client.player, context.camera(), context.matrixStack(), 0);
            }
        });
    }

    public static void updateLeashedEntityCount(PlayerEntity player) {
        World world = player.getEntityWorld();
        Box box = new Box(player.getBlockPos()).expand(10);
        int leashedEntityCount = 0;


        // Leashed Mobs
        for (Entity entity : world.getEntitiesByClass(MobEntity.class, box, mob -> mob.isLeashed() && mob.getLeashHolder() == player)) {
            leashedEntityCount++;
        }

        // Leashed Boats
        for (Entity entity : world.getEntitiesByClass(BoatEntity.class, box, boat -> boat.isLeashed() && boat.getLeashHolder() == player)) {
            leashedEntityCount++;
        }

        // Check if an entity broke its leash
        if (leashedEntityCount < lastLeashedEntityCount) {
            warningEndTime = System.currentTimeMillis() + 1250;
        }

        // Check if a new entity was leashed
        if (leashedEntityCount < lastLeashedEntityCount) {
            warningEndTime = System.currentTimeMillis() + 1250;
        } else if (leashedEntityCount > lastLeashedEntityCount && leashedEntityCount > 1) {
            additionEndTime = System.currentTimeMillis() + 1250;
        }

        lastLeashedEntityCount = leashedEntityCount;

        if (leashedEntityCount > 0) {
            Text message = Text.literal("Leashed Entities: " + leashedEntityCount);
            if (leashedEntityCount == 0) {
                player.sendMessage(message, true);
            } else {
                if (System.currentTimeMillis() < warningEndTime) {
                    message = Text.literal("Leashed Entities: " + leashedEntityCount).styled(style -> style.withColor(0x9E0505));
                } else if (System.currentTimeMillis() < additionEndTime) {
                    message = Text.literal("Leashed Entities: " + leashedEntityCount).styled(style -> style.withColor(0x14e018));
                }
                player.sendMessage(message, true);
            }
        }
    }

    // Draw boxes around leashed entities
    public static void drawLeadTracers(PlayerEntity player, Camera camera, MatrixStack matrixStack, float tickDelta) {
        World world = player.getEntityWorld();
        Box box = new Box(player.getBlockPos()).expand(10); // Adjust the range as needed
        for (Entity entity : world.getEntitiesByClass(Entity.class, box, e -> (e instanceof MobEntity || e instanceof BoatEntity) && isLeashedByPlayer(e, player))) {
            renderBoxAroundEntity(player, entity, camera, matrixStack);
        }
    }

    // Check if entity is leashed by player
    private static boolean isLeashedByPlayer(Entity entity, PlayerEntity player) {
        if (entity instanceof MobEntity) {
            MobEntity mob = (MobEntity) entity;
            return mob.isLeashed() && mob.getLeashHolder() == player;
        } else if (entity instanceof BoatEntity) {
            BoatEntity boat = (BoatEntity) entity;
            return boat.isLeashed() && boat.getLeashHolder() == player;
        }
        return false;
    }

    private static void renderBoxAroundEntity(PlayerEntity player, Entity entity, Camera camera, MatrixStack matrixStack) {
        Vec3d cameraPos = camera.getPos();
        Vec3d entityPos = new Vec3d(
                MathHelper.lerp(1.0F, entity.prevX, entity.getX()),
                MathHelper.lerp(1.0F, entity.prevY, entity.getY()),
                MathHelper.lerp(1.0F, entity.prevZ, entity.getZ())
        ).subtract(cameraPos);

        Box box = entity.getBoundingBox().offset(-entity.getX(), -entity.getY(), -entity.getZ()).expand(0.1);
        matrixStack.push();
        matrixStack.translate(entityPos.x, entityPos.y, entityPos.z);
        RenderSystem.lineWidth(2.0F);
        RenderSystem.enableDepthTest();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        float transparency = LeashESPConfig.getFloat("transparencyLevel", 0.5f);
        int a = LeashESPConfig.getInt("transparencyLevel", 128);

        float distance = (float) player.distanceTo(entity);
        int r = 0, g = 255, b = 0;

        if (distance > 8) {
            r = 255;
            g = 0;
        } else if (distance > 7) {
            r = 255;
            g = 64;
        } else if (distance > 6) {
            r = 255;
            g = 128;
        } else if (distance > 5) {
            r = 255;
            g = 192;
        } else if (distance > 4) {
            r = 255;
            g = 255;
        } else if (distance > 3) {
            r = 192;
            g = 255;
        } else if (distance > 2) {
            r = 128;
            g = 255;
        } else if (distance > 1) {
            r = 64;
            g = 255;
        }

        ESPRenderer.drawSolidBox(box, matrixStack, r, g, b, a);

        matrixStack.pop();
    }
}