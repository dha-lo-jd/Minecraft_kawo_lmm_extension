package org.lo.d.minecraft.littlemaid.gui;

import java.util.regex.Pattern;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.src.LMM_EntityLittleMaid;
import net.minecraft.src.MMM_TextureBox;
import net.minecraft.src.MMM_TextureManager;

import org.lo.d.commons.coords.Point2D;
import org.lo.d.commons.coords.Rect2D;
import org.lo.d.commons.gl.SafetyGL;
import org.lo.d.commons.gui.ContainerTab;
import org.lo.d.commons.gui.GuiTabContainer;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiMaidExContainer extends GuiTabContainer implements GuiTabContainer.TabBase {
	public static class MaidExTabEntry implements TabEntry {

		private static final int SIZE = 16;
		protected final GuiMaidExContainer tabContainer;
		protected final GuiContainer container;
		protected final LMM_EntityLittleMaid entitylittlemaid;
		protected final String iconTexture;

		public MaidExTabEntry(GuiMaidExContainer tabContainer, GuiContainer container,
				LMM_EntityLittleMaid entitylittlemaid, String iconTexture) {
			this.tabContainer = tabContainer;
			this.container = container;
			this.entitylittlemaid = entitylittlemaid;
			this.iconTexture = iconTexture;
		}

		@Override
		public void drawCurrentTab(Point2D tabDrawPoint, int tabIndex, RenderEngine renderEngine) {
			drawTab(tabDrawPoint, tabIndex, renderEngine);
		}

		@Override
		public void drawForcusTab(Point2D tabDrawPoint, int tabIndex, RenderEngine renderEngine) {
			drawTab(tabDrawPoint, tabIndex, renderEngine);

		}

		/**
		 * Draws a textured rectangle at the stored z-value. Args: x, y, u, v, width, height
		 */
		public void drawFullyTextureRect(int par1, int par2, int par5, int par6) {
			Tessellator tessellator = Tessellator.instance;
			tessellator.startDrawingQuads();
			tessellator.addVertexWithUV(par1 + 0, par2 + par6, tabContainer.zLevel, 0, 1);
			tessellator.addVertexWithUV(par1 + par5, par2 + par6, tabContainer.zLevel, 1, 1);
			tessellator.addVertexWithUV(par1 + par5, par2 + 0, tabContainer.zLevel, 1, 0);
			tessellator.addVertexWithUV(par1 + 0, par2 + 0, tabContainer.zLevel, 0, 0);
			tessellator.draw();
		}

		@Override
		public void drawTab(final Point2D tabDrawPoint, final int tabIndex, final RenderEngine renderEngine) {
			SafetyGL.safetyGLProcess(new SafetyGL.Processor() {
				@Override
				public void process(SafetyGL safetyGL) {
					GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
					safetyGL.disableStandardItemLighting();
					renderEngine.bindTexture(iconTexture);
					int drawLeft = tabDrawPoint.getX();
					int drawTop = tabDrawPoint.getY();
					drawFullyTextureRect(drawLeft, drawTop, SIZE, SIZE);
				}
			});
		}

		@Override
		public GuiContainer getContainer() {
			return container;
		}
	}

	private static final int TAB_WIDTH = 28;

	private static final int TAB_HEIGHT = 28;

	private static final int OFFSET = 6;

	private static final Pattern PATTERN_EXT = Pattern.compile("\\.[0-9a-zA-Z]+$");

	private static final String iconTexture = "/gui/gui_tab.png";
	private static final Point2D tabUV = new Point2D(TAB_WIDTH, 0);
	private static final Point2D tabForcusUV = new Point2D(0, TAB_HEIGHT);
	private static final Point2D currentTabUV = new Point2D(0, 0);

	private static final String TEXTURE_SUFFIX_TAB = "_tab";

	public final LMM_EntityLittleMaid entitylittlemaid;

	public GuiMaidExContainer(ContainerTab par1Container, EntityPlayer player, LMM_EntityLittleMaid maid) {
		super(par1Container);
		tabBase = this;
		entitylittlemaid = maid;
	}

	public void add(MaidExTabEntry entry) {
		addTab(entry);
	}

	@Override
	public void drawCurrentTab(final Point2D tabDrawPoint, final int tabIndex, final RenderEngine renderEngine) {
		renderEngine.bindTexture(getTabTexture());
		doDrawTab(tabDrawPoint, getCurrentTabUV(tabIndex), tabIndex, renderEngine);
	}

	@Override
	public void drawForcusTab(Point2D tabDrawPoint, int tabIndex, RenderEngine renderEngine) {
		mc.renderEngine.bindTexture(getTabTexture());
		doDrawTab(tabDrawPoint, tabForcusUV, tabIndex, renderEngine);
	}

	@Override
	public void drawTab(final Point2D tabDrawPoint, final int tabIndex, final RenderEngine renderEngine) {
		mc.renderEngine.bindTexture(getTabTexture());
		doDrawTab(tabDrawPoint, tabUV, tabIndex, renderEngine);
	}

	@Override
	public Point2D getFixedDrawPoint(Point2D tabDrawPoint, int tabIndex) {
		//		int x = (width - (tabs.size() * TAB_WIDTH)) / 2;//中央寄せ
		int x = screen.guiLeft + screen.xSize + 16;
		x = x + TAB_WIDTH * tabIndex;//現在のタブ位置までずらす
		int y = height - TAB_HEIGHT;
		return new Point2D(x, y);
	}

	@Override
	public Point2D getForeGroundDrawPoint(Point2D tabDrawPoint, int tabIndex) {
		return tabDrawPoint.addX(OFFSET).addY(OFFSET);
	}

	@Override
	public Rect2D getHandleClickRect(Point2D tabDrawPoint, int tabIndex) {
		return new Rect2D(getFixedDrawPoint(tabDrawPoint, tabIndex), TAB_WIDTH, TAB_HEIGHT);
	}

	private void doDrawTab(final Point2D tabDrawPoint, final Point2D iconUV, final int tabIndex,
			final RenderEngine renderEngine) {
		SafetyGL.safetyGLProcess(new SafetyGL.Processor() {
			@Override
			public void process(SafetyGL safetyGL) {
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				safetyGL.disableStandardItemLighting();
				int drawLeft = tabDrawPoint.getX();
				int drawTop = tabDrawPoint.getY();
				drawTexturedModalRect(drawLeft, drawTop, iconUV.getX(), iconUV.getY(), TAB_WIDTH, TAB_HEIGHT);
			}
		});
	}

	private Point2D getCurrentTabUV(int tabIndex) {
		return currentTabUV;
	}

	private String getTabTexture() {
		return getTextureName(TEXTURE_SUFFIX_TAB, iconTexture);
	}

	private String getTextureName(String suffix, String defualt) {
		String s = ((MMM_TextureBox) entitylittlemaid.textureBox[0]).getTextureName(MMM_TextureManager.tx_gui);
		if (s != null) {
			s = PATTERN_EXT.matcher(s).replaceAll(suffix + "\\0");
			if (!mc.texturePackList.getSelectedTexturePack().func_98138_b(s, true)) {
				s = null;
			}
		}
		if (s == null) {
			s = defualt;
		}

		return s;
	}
}
