package org.lo.d.minecraft.littlemaid.renderer;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;

import org.lo.d.commons.coords.Point3DDouble;
import org.lo.d.commons.gl.SafetyGL;
import org.lo.d.commons.renderer.Point3DRenderSupport;
import org.lo.d.minecraft.littlemaid.MaidExIcon;
import org.lwjgl.opengl.GL11;

public class RenderMaidExIcon {

	public double zLevel = 0.0D;

	public void render(final MaidExIcon icon, final Point3DDouble renderPos, final float par8, final float par9,
			final RenderManager renderManager) {
		final FontRenderer fontRenderer = renderManager.getFontRenderer();

		final float w = 4F / 16F;
		final float h = 4F / 16F;

		//OpenGL描画プロセス開始
		SafetyGL.safetyGLProcess(new SafetyGL.Processor() {
			@Override
			public void process(SafetyGL safetyGL) {
				safetyGL.pushMatrix();
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				Point3DRenderSupport.glTranslatef(renderPos);

				drawIcon(safetyGL, icon, renderManager, w, h);

				float var19 = (float) 0.02;
				GL11.glScalef(-var19, -var19, var19);

				drawText(icon, fontRenderer, safetyGL);
			}

			/**
			 * アイコン画像の描画
			 * @param safetyGL
			 * @param icon
			 * @param renderManager
			 * @param w
			 * @param h
			 */
			private void drawIcon(SafetyGL safetyGL, final MaidExIcon icon, final RenderManager renderManager,
					final float w, final float h) {
				SafetyGL.safetyGLProcess(new SafetyGL.Processor() {
					@Override
					public void process(SafetyGL safetyGL) {
						safetyGL.pushMatrix();
						GL11.glTranslatef(w / 2, h / 2, 0);

						safetyGL.disable(GL11.GL_LIGHTING);
						safetyGL.disable(GL11.GL_BLEND);
						safetyGL.enable(GL11.GL_TEXTURE_2D);
						renderManager.renderEngine.func_110577_a(icon.getTexture());
						Tessellator tessellator = Tessellator.instance;
						tessellator.startDrawingQuads();
						tessellator.addVertexWithUV(0, h, zLevel, 0, 0);
						tessellator.addVertexWithUV(w, h, zLevel, 1, 0);
						tessellator.addVertexWithUV(w, 0, zLevel, 1, 1);
						tessellator.addVertexWithUV(0, 0, zLevel, 0, 1);
						tessellator.draw();
					}
				});
			}

			/**
			 * アイコンテキストの影付き描画
			 * @param icon
			 * @param fontRenderer
			 * @param safetyGL
			 */
			private void drawText(final MaidExIcon icon, final FontRenderer fontRenderer, SafetyGL safetyGL) {
				String iconString = icon.getText();
				int color = icon.getTextColor();

				safetyGL.disable(GL11.GL_LIGHTING);
				fontRenderer.drawStringWithShadow(iconString, -4, 1 - 16 - 2, color);
			}
		});

	}
}
