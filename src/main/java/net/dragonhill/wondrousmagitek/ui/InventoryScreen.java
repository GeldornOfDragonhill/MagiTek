package net.dragonhill.wondrousmagitek.ui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.dragonhill.wondrousmagitek.config.Constants;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import org.lwjgl.opengl.GL11;

public abstract class InventoryScreen<T extends InventoryScreenContainer> extends ContainerScreen<T> {

	protected static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(Constants.modId, "textures/gui/generic_ui.png");

	protected final int clientHeight;

	protected InventoryScreen(final T screenContainer, final PlayerInventory inv, final ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);

		this.clientHeight = screenContainer.getClientHeight();

		this.guiLeft = 0;
		this.guiTop = 0;
		this.xSize = 176;
		this.ySize = 86 + clientHeight;
	}

	protected int getClientX() {
		return (this.width - this.xSize) / 2 + 7;
	}

	protected int getClientY() {
		return (this.height - this.ySize) / 2 + 7;
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		this.renderBackground();
		super.render(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		final int renderX = (this.width - this.xSize) / 2;
		final int renderY = (this.height - this.ySize) / 2;

		RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
		this.minecraft.getTextureManager().bindTexture(BACKGROUND_TEXTURE);
		this.blit(renderX, renderY, 0, 83, this.xSize, 3);

		//https://www.minecraftforge.net/forum/topic/77532-1144solved-scale-image-with-blit/?tab=comments#comment-371818
		innerBlit(renderX, renderX + this.xSize, renderY + 3, renderY + this.clientHeight + 3, this.getBlitOffset(), 0f, 176f / 256f, 77f / 256f, 78f / 256f);

		this.blit(renderX, renderY + this.clientHeight + 3, 0, 0, this.xSize, 80);

		this.drawClientBackground(partialTicks, mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		this.font.drawString(this.title.getFormattedText(), 8f, 5f, 0xFF000000);

		if(!this.container.getNetValueList().getWasUpdated()) {
			GL11.glPushMatrix();
			GL11.glScalef(0.5f, 0.5f, 0.5f);
			this.font.drawString("Waiting for server...", 15f, this.clientHeight * 2 - 5, 0xFFCC6666);
			GL11.glPopMatrix();
		}

		this.drawClientForeground(mouseX, mouseY);
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	abstract protected void drawClientBackground(float partialTicks, int mouseX, int mouseY);
	abstract protected void drawClientForeground( int mouseX, int mouseY);
}
