package org.lo.d.minecraft.littlemaid.renderer;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;

import org.lo.d.commons.coords.Point3DDouble;
import org.lo.d.commons.renderer.Point3DRenderSupport;
import org.lo.d.minecraft.littlemaid.MaidExIcon;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderMaidExIcon {

	public void render(MaidExIcon icon, Point3DDouble renderPos, float par8, float par9, RenderManager renderManager) {
		FontRenderer fontRenderer = renderManager.getFontRenderer();

		float w = 4F / 16F;
		float h = 4F / 16F;
		double offset = icon.getOffsetX();

		GL11.glPushMatrix();
		Point3DRenderSupport.glTranslatef(renderPos);
		GL11.glNormal3f(0.0F, 1.0F, 0.0F);

		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_TEXTURE_2D);

		GL11.glRotatef(180 - renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GL11.glTranslatef((float) -offset, 0, 0);

		renderManager.renderEngine.bindTexture(icon.getTexture());
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(0.0F - w / 2, 0.0F - h / 2, 0.0D, 1, 1);
		tessellator.addVertexWithUV(w - w / 2, 0.0F - h / 2, 0.0D, 0, 1);
		tessellator.addVertexWithUV(w - w / 2, h - h / 2, 0.0D, 0, 0);
		tessellator.addVertexWithUV(0.0F - w / 2, h - h / 2, 0.0D, 1, 0);
		tessellator.draw();
		GL11.glRotatef(-180 + renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
		float var19 = (float) 0.02;
		GL11.glScalef(-var19, -var19, var19);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		String iconString = icon.getText();
		int color = icon.getTextColor();
		double leftPos = fontRenderer.getStringWidth(iconString) / 2.0D;
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		int bgcolor = (color & 16579836) >> 2 | color & -16777216;
		fontRenderer.drawString(iconString, (int) (-leftPos) + 1, 1 - 16,
				bgcolor);
		// GL11.glEnable(GL11.GL_DEPTH_TEST);
		// GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		fontRenderer.drawString(iconString, (int) (-leftPos), 0 - 16, color);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glPopMatrix();
	}
}
