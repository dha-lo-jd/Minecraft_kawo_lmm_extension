package org.lo.d.minecraft.littlemaid.renderer;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;

import org.lo.d.commons.coords.Point3DDouble;
import org.lo.d.commons.gl.SafetyGL;
import org.lo.d.commons.renderer.Point3DRenderSupport;
import org.lo.d.minecraft.littlemaid.MaidExIcon;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderMaidExIcon {

	public void render(final MaidExIcon icon, final Point3DDouble renderPos, final float par8, final float par9,
			final RenderManager renderManager) {
		final FontRenderer fontRenderer = renderManager.getFontRenderer();

		final float w = 4F / 16F;
		final float h = 4F / 16F;
		final double offset = icon.getOffsetX();

		//OpenGL描画プロセス開始
		SafetyGL.safetyGLProcess(new SafetyGL.Processor() {
			@Override
			public void process(SafetyGL safetyGL) {
				safetyGL.pushMatrix();
				Point3DRenderSupport.glTranslatef(renderPos);
				GL11.glNormal3f(0.0F, 1.0F, 0.0F);

				resetRotationFromPlayerViewRotate(renderManager);

				GL11.glTranslatef((float) -offset, 0, 0);

				drawIcon(safetyGL, icon, renderManager, w, h);

				float var19 = (float) 0.02;
				GL11.glScalef(-var19, -var19, var19);

				drawText(icon, fontRenderer, safetyGL);

				GL11.glDepthMask(true);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			}

			/**
			 * アイコン画像の描画
			 * @param safetyGL
			 * @param icon
			 * @param renderManager
			 * @param w
			 * @param h
			 */
			private void drawIcon(SafetyGL safetyGL, MaidExIcon icon, RenderManager renderManager, float w, float h) {
				safetyGL.enable(GL12.GL_RESCALE_NORMAL);
				safetyGL.disable(GL11.GL_LIGHTING);
				safetyGL.disable(GL11.GL_BLEND);
				safetyGL.enable(GL11.GL_TEXTURE_2D);
				renderManager.renderEngine.bindTexture(icon.getTexture());
				Tessellator tessellator = Tessellator.instance;
				tessellator.startDrawingQuads();
				tessellator.addVertexWithUV(0.0F - w / 2, 0.0F - h / 2, 0.0D, 1, 1);
				tessellator.addVertexWithUV(w - w / 2, 0.0F - h / 2, 0.0D, 0, 1);
				tessellator.addVertexWithUV(w - w / 2, h - h / 2, 0.0D, 0, 0);
				tessellator.addVertexWithUV(0.0F - w / 2, h - h / 2, 0.0D, 1, 0);
				tessellator.draw();
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
				double leftPos = fontRenderer.getStringWidth(iconString) / 2.0D;

				/*
				 * 文字の影描画
				 */
				{
					safetyGL.enable(GL11.GL_BLEND);
					safetyGL.enable(GL11.GL_TEXTURE_2D);
					safetyGL.disable(GL11.GL_LIGHTING);
					safetyGL.disable(GL11.GL_DEPTH_TEST);
					GL11.glDepthMask(false);
					GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
					int bgcolor = (color & 16579836) >> 2 | color & -16777216;
					fontRenderer.drawString(iconString, (int) (-leftPos) + 1, 1 - 16, bgcolor);
				}

				/*
				 * 文字の描画
				 */
				{
					safetyGL.disable(GL11.GL_BLEND);
					fontRenderer.drawString(iconString, (int) (-leftPos), 0 - 16, color);
				}
			}

			/**
			 * プレイヤー視点に合わせたカメラの回転をリセット(画面に対して縦横を平行にする)
			 * @param renderManager
			 */
			private void resetRotationFromPlayerViewRotate(final RenderManager renderManager) {
				GL11.glRotatef(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
				GL11.glRotatef(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
			}
		});

	}
}
