package org.lo.d.minecraft.littlemaid;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.EventPriority;
import net.minecraftforge.event.ForgeSubscribe;

import org.lo.d.commons.gui.FontRendererConstants;

/**
 * @author dha_lo_jd
 */
public class LMMExtensionWarningForDisabledEventHandler {

	@ForgeSubscribe(priority = EventPriority.NORMAL)
	public void renderHUDText(RenderGameOverlayEvent.Text event) {
		if (!Minecraft.getMinecraft().gameSettings.showDebugInfo) {
			event.left.add(FontRendererConstants.Color.RED
					+ "Kawo_Commons_LMM_Extension's uninstall mode configuration is ENABLED.");
			event.left.add(FontRendererConstants.Color.RED
					+ "Kawo_Commons_LMM_Extension's mostly functions are DISABLED.");
		}
	}

}
