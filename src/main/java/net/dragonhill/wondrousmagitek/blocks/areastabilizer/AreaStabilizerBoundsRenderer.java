package net.dragonhill.wondrousmagitek.blocks.areastabilizer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import org.lwjgl.opengl.GL11;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class AreaStabilizerBoundsRenderer {

	private static class CustomRenderType extends RenderType {

		public CustomRenderType() {
			super(null, null, 0, 0, false, false, null, null);
			throw new NotImplementedException();
		}

		private static final RenderType BORDER_BOX = makeType("area_stabilizer_border",
			DefaultVertexFormats.POSITION_COLOR, GL11.GL_QUADS,
			256,
			RenderType.State.getBuilder()
				.writeMask(COLOR_WRITE)
				.transparency(TRANSLUCENT_TRANSPARENCY)
				.texture(NO_TEXTURE)
				.depthTest(DEPTH_ALWAYS)
				.cull(CULL_DISABLED)
				.lightmap(LIGHTMAP_DISABLED)
				.build(false));
	}

	private static void drawSide(IVertexBuilder builder, Matrix4f mat, float x1, float x2, float z1, float z2, float r, float g, float b, float a) {
		builder.pos(mat, x1, 0, z1).color(r, g, b, a).endVertex();
		builder.pos(mat, x1, 255, z1).color(r, g, b, a).endVertex();
		builder.pos(mat, x2, 255, z2).color(r, g, b, a).endVertex();
		builder.pos(mat, x2, 0, z2).color(r, g, b, a).endVertex();
	}

	private static void drawBounds(IVertexBuilder builder, Matrix4f mat, ChunkPos center, int radius) {

		final float startX = (center.x - radius) * 16;
		final float endX = (center.x + 1 + radius) * 16;

		final float startZ = (center.z - radius) * 16;
		final float endZ = (center.z + 1 + radius) * 16;

		float r = 1f;
		float g = 0.3f;
		float b = 0.3f;
		float a = 0.5f;

		ClientPlayerEntity player = Minecraft.getInstance().player;
		float playerX = (float)player.getPosX();
		float playerZ = (float)player.getPosZ();
		if(playerX >= startX && playerX <= endX && playerZ >= startZ && playerZ <= endZ) {
			r = 0.3f;
			g = 1.0f;
		}

		drawSide(builder, mat, startX, endX, startZ, startZ, r, g, b, a);
		drawSide(builder, mat, startX, endX, endZ, endZ, r, g, b, a);
		drawSide(builder, mat, startX, startX, startZ, endZ, r, g, b, a);
		drawSide(builder, mat, endX, endX, startZ, endZ, r, g, b, a);
	}

	public static void render(final RenderWorldLastEvent event, final Tuple<ChunkPos, Integer> bounds) {

		final ActiveRenderInfo activeRenderInfo = Minecraft.getInstance().getRenderManager().info;
		final Vec3d viewPosition = activeRenderInfo.getProjectedView();

		MatrixStack matrixstack = event.getMatrixStack();
		matrixstack.push();

		matrixstack.translate(-viewPosition.x, -viewPosition.y, -viewPosition.z);
		Matrix4f matrix4f = matrixstack.getLast().getMatrix();

		RenderSystem.disableCull();

		IRenderTypeBuffer.Impl buffer = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
		IVertexBuilder builder = buffer.getBuffer(CustomRenderType.BORDER_BOX);

		drawBounds(builder, matrix4f, bounds.getA(), bounds.getB());

		buffer.finish(CustomRenderType.BORDER_BOX);

		RenderSystem.enableCull();

		matrixstack.pop();
	}
}
