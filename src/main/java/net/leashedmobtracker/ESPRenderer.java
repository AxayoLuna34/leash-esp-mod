package net.leashedmobtracker;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Box;
import org.joml.Matrix4f;

public class ESPRenderer {

    // Render a solid-colored box using provided coordinates and color values
    public static void drawSolidBox(Box bb, MatrixStack matrixStack, int r, int g, int b, int a) {

        float minX = (float) bb.minX;
        float minY = (float) bb.minY;
        float minZ = (float) bb.minZ;

        float maxX = (float) bb.maxX;
        float maxY = (float) bb.maxY;
        float maxZ = (float) bb.maxZ;

        // Get matrix transformation and set the shader to use position and attrivutes
        Matrix4f matrix = matrixStack.peek().getPositionMatrix();
        RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);

        // Initialize tesselator and buffer for quads rendering
        Tessellator tessellator = RenderSystem.renderThreadTesselator();
        BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

        // Draw Bottom face
        bufferBuilder.vertex(matrix, minX, minY, minZ).color(r, g, b, a);
        bufferBuilder.vertex(matrix, maxX, minY, minZ).color(r, g, b, a);
        bufferBuilder.vertex(matrix, maxX, minY, maxZ).color(r, g, b, a);
        bufferBuilder.vertex(matrix, minX, minY, maxZ).color(r, g, b, a);

        // Draw Top face
        bufferBuilder.vertex(matrix, minX, maxY, minZ).color(r, g, b, a);
        bufferBuilder.vertex(matrix, minX, maxY, maxZ).color(r, g, b, a);
        bufferBuilder.vertex(matrix, maxX, maxY, maxZ).color(r, g, b, a);
        bufferBuilder.vertex(matrix, maxX, maxY, minZ).color(r, g, b, a);

        // Draw Front face
        bufferBuilder.vertex(matrix, minX, minY, minZ).color(r, g, b, a);
        bufferBuilder.vertex(matrix, minX, maxY, minZ).color(r, g, b, a);
        bufferBuilder.vertex(matrix, maxX, maxY, minZ).color(r, g, b, a);
        bufferBuilder.vertex(matrix, maxX, minY, minZ).color(r, g, b, a);

        // Draw Back face
        bufferBuilder.vertex(matrix, minX, minY, maxZ).color(r, g, b, a);
        bufferBuilder.vertex(matrix, maxX, minY, maxZ).color(r, g, b, a);
        bufferBuilder.vertex(matrix, maxX, maxY, maxZ).color(r, g, b, a);
        bufferBuilder.vertex(matrix, minX, maxY, maxZ).color(r, g, b, a);

        // Draw Left face
        bufferBuilder.vertex(matrix, minX, minY, minZ).color(r, g, b, a);
        bufferBuilder.vertex(matrix, minX, minY, maxZ).color(r, g, b, a);
        bufferBuilder.vertex(matrix, minX, maxY, maxZ).color(r, g, b, a);
        bufferBuilder.vertex(matrix, minX, maxY, minZ).color(r, g, b, a);

        // Draw Right face
        bufferBuilder.vertex(matrix, maxX, minY, minZ).color(r, g, b, a);
        bufferBuilder.vertex(matrix, maxX, maxY, minZ).color(r, g, b, a);
        bufferBuilder.vertex(matrix, maxX, maxY, maxZ).color(r, g, b, a);
        bufferBuilder.vertex(matrix, maxX, minY, maxZ).color(r, g, b, a);

        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
    }
}
