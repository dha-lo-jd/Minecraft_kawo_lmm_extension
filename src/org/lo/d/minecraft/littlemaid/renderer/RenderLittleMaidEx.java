package org.lo.d.minecraft.littlemaid.renderer;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.src.LMM_EntityLittleMaid;
import net.minecraft.src.LMM_EntityModeBase;
import net.minecraft.src.LMM_RenderLittleMaid;

import org.lo.d.commons.coords.Point3DDouble;
import org.lo.d.commons.renderer.Point3DRenderSupport;
import org.lo.d.minecraft.littlemaid.LMMExtension;
import org.lo.d.minecraft.littlemaid.MaidExIcon;
import org.lo.d.minecraft.littlemaid.mode.LMMModeExIconHandler;
import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;

public class RenderLittleMaidEx extends LMM_RenderLittleMaid {

	private static final RenderMaidExIcon RENDER_MAID_ICON = new RenderMaidExIcon();

	public RenderLittleMaidEx(float f) {
		super(f);
	}

	@Override
	public void doRenderLitlleMaid(LMM_EntityLittleMaid plittleMaid, double px, double py, double pz, float f, float f1) {
		super.doRenderLitlleMaid(plittleMaid, px, py, pz, f, f1);
		if (!Minecraft.isGuiEnabled() || Minecraft.getMinecraft().currentScreen != null
				|| plittleMaid == renderManager.livingPlayer || !plittleMaid.isMaidContract()
				|| plittleMaid.func_98034_c(Minecraft.getMinecraft().thePlayer)
				|| plittleMaid != renderManager.field_96451_i || !LMMExtension.sugarCountVisible) {
			return;
		}
		FontRenderer fontRenderer = renderManager.getFontRenderer();

		List<LMMModeExIconHandler> iconHandlers = Lists.newArrayList();
		double iconsWidth = 0;
		double iconsOffsetY = -4F / 16F;
		double iconsVirticalOffsetY = 5F / 16F;
		if (plittleMaid.func_94056_bM()) {
			iconsOffsetY += 4F / 16F;
			iconsVirticalOffsetY += 3F / 16F;
		}

		for (LMM_EntityModeBase entityModeBase : plittleMaid.maidEntityModeList) {
			if (entityModeBase instanceof LMMModeExIconHandler) {
				LMMModeExIconHandler iconHandler = (LMMModeExIconHandler) entityModeBase;
				for (MaidExIcon icon : iconHandler.getIcons(plittleMaid.getMaidModeInt())) {
					iconsWidth += getDrawWidth(icon, fontRenderer);
				}
				iconHandlers.add(iconHandler);
			}
		}

		GL11.glPushMatrix();
		Point3DRenderSupport.glTranslatef(new Point3DDouble(px, py + plittleMaid.height + iconsVirticalOffsetY, pz));
		GL11.glNormal3f(0.0F, 1.0F, 0.0F);
		resetRotationFromPlayerViewRotate(renderManager);
		Point3DDouble renderPos = new Point3DDouble((iconsWidth / 2F) - 0.4D, iconsOffsetY, 0D);
		for (LMMModeExIconHandler iconHandler : iconHandlers) {
			double drawedWidth = doRenderMaidIcon(iconHandler, plittleMaid, renderPos, f, f1, renderManager);
			renderPos = renderPos.addX(drawedWidth);
		}
		GL11.glPopMatrix();
	}

	public double doRenderMaidIcon(LMMModeExIconHandler iconHandler, LMM_EntityLittleMaid plittleMaid,
			Point3DDouble renderPos, float par8, final float par9, RenderManager renderManager) {
		FontRenderer fontRenderer = renderManager.getFontRenderer();
		double drawedWidth = 0;
		for (MaidExIcon icon : iconHandler.getIcons(plittleMaid.getMaidModeInt())) {
			double oX = -getDrawWidth(icon, fontRenderer);
			RENDER_MAID_ICON.render(icon, renderPos, par8, par9, renderManager);
			drawedWidth += oX;
			renderPos = renderPos.addX(oX);
		}
		return drawedWidth;
	}

	private double getDrawWidth(MaidExIcon icon, FontRenderer fontRenderer) {
		double drawWidth = 0;
		drawWidth += icon.getOffsetX();
		drawWidth += (fontRenderer.getStringWidth(icon.getText()) / 16F) * 0.45;
		drawWidth += 0.1D;
		return drawWidth;
	}

	/**
	 * プレイヤー視点に合わせたカメラの回転をリセット(画面に対して縦横を平行にする)
	 * @param renderManager
	 */
	private void resetRotationFromPlayerViewRotate(final RenderManager renderManager) {
		GL11.glRotatef(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
	}

}
