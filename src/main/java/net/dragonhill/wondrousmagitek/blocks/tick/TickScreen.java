package net.dragonhill.wondrousmagitek.blocks.tick;

import net.dragonhill.wondrousmagitek.network.ModNetworkChannel;
import net.dragonhill.wondrousmagitek.ui.InventoryScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import org.lwjgl.opengl.GL11;

public class TickScreen extends InventoryScreen<TickContainer> {

	private TextFieldWidget name;
	private String nameTitle;

	public TickScreen(final TickContainer screenContainer, final PlayerInventory inv, final ITextComponent title) {
		super(screenContainer, inv, title);

		this.container.name.registerCallback(name -> {
			this.name.setText(name.get());
		});

		this.nameTitle = I18n.format("ui.wondrousmagitek.tick_block.name_title");
	}

	@Override
	protected void init() {
		super.init();

		final int x = this.getClientX();
		final int y = this.getClientY();

		this.addButton(this.name = new TextFieldWidget(this.font, x, y + 27, this.getClientWidth(), 16, ""));
		this.name.setResponder(name -> {
			if(this.container.name.set(name)) {
				ModNetworkChannel.sendToServer(new SetTickNameCommandMessage(this.container));
			}
		});
	}

	@Override
	protected void drawClientBackground(float partialTicks, int mouseX, int mouseY) {

	}

	@Override
	protected void drawClientForeground(int mouseX, int mouseY) {
		GL11.glPushMatrix();
		GL11.glScalef(0.8f, 0.8f, 0.8f);
		this.font.drawString(this.nameTitle, 10f, 30f, 0xFF000000);
		GL11.glPopMatrix();
	}

	@Override
	public boolean keyPressed(int p1, int p2, int p3) {
		if(this.name.isFocused()) {
			//TODO: maybe wrap the text widget to streamline the handling
			if(p1 == 256) { //ESC
				return super.keyPressed(p1, p2, p3);
			}
			return this.name.keyPressed(p1, p2, p3);
		}

		return super.keyPressed(p1, p2, p3);
	}
}
