package net.dragonhill.wondrousmagitek.ui.widgets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.Widget;

import java.util.function.Consumer;

public class RangeSelector extends Widget {
	private int min;
	private int max;
	private int current;

	final int buttonSize;
	private final Consumer<RangeSelector> onUpdate;
	final int buttonLeft;
	final int buttonRight;

	public void setMin(int newMin) {
		if(newMin > this.max) {
			this.max = newMin;
		}

		if(newMin > this.current) {
			this.current = newMin;
		}

		this.min = newMin;
	}

	public void setMax(int newMax) {
		if(newMax < this.min) {
			this.min = newMax;
		}

		if(newMax < this.current) {
			this.current = newMax;
		}

		this.max = newMax;
	}

	public void setCurrent(int newCurrent) {
		if(newCurrent < this.min || newCurrent > this.max) {
			return;
		}

		this.current = newCurrent;
	}

	public int getCurrent() {
		return this.current;
	}

	public RangeSelector(final int x, final int y, final int width, final int height, int min, int max, int current, Consumer<RangeSelector> onUpdate) {
		super(x, y, width, height, "msg");

		this.min = min;
		this.max = max;

		this.current = current;

		this.onUpdate = onUpdate;

		this.buttonSize = height / 2;
		this.buttonRight = this.x + this.width;
		this.buttonLeft = this.buttonRight - this.buttonSize;
	}

	private void onPlus() {
		if(this.current < this.max) {
			this.current += 1;
			this.onValueChanged();
		}
	}

	private void onMinus() {
		if(this.current > this.min) {
			this.current -= 1;
			this.onValueChanged();
		}
	}

	private void onValueChanged() {
		this.onUpdate.accept(this);
		this.playDownSound(Minecraft.getInstance().getSoundHandler());
	}

	private boolean isInButton(int mouseX, int mouseY, boolean plus) {

		if(mouseX < this.buttonLeft ||  mouseX > this.buttonRight) {
			return false;
		}

		if(plus) {
			return mouseY >= this.y && mouseY < this.y + this.buttonSize;
		} else {
			return mouseY >= this.y + this.buttonSize && mouseY < this.y + this.height;
		}
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		Minecraft minecraft = Minecraft.getInstance();
		FontRenderer fontrenderer = minecraft.fontRenderer;

		AbstractGui.fill(this.x, this.y, this.x + width, this.y + this.height, 0xFFAAAAAA);
		AbstractGui.fill(this.x + 1, this.y + 1, this.x + width - 1, this.y + this.height - 1, 0xFF000000);

		this.drawCenteredString(fontrenderer, Integer.toString(this.current), this.x + (this.width - this.buttonSize) / 2, this.y + (this.height / 2) - 4, 0xFFFFFFFF);

		final int halfButton = this.buttonSize / 2;
		final int buttonXCenter = this.buttonLeft + halfButton;
		final int buttonYCenter = this.y + halfButton - 4;

		AbstractGui.fill(this.buttonLeft, this.y, this.buttonRight, this.y + this.buttonSize, this.isInButton(mouseX, mouseY, true) ? 0xFFAAFFAA : 0xFFAAAAAA);
		this.drawCenteredString(fontrenderer, "+", buttonXCenter, buttonYCenter, 0xFFFFFFFF);

		AbstractGui.fill(this.buttonLeft, this.y + this.buttonSize, this.buttonRight, this.y + 2 * this.buttonSize, this.isInButton(mouseX, mouseY, false) ? 0xFFFFAAAA : 0xFFAAAAAA);
		this.drawCenteredString(fontrenderer, "-", buttonXCenter, buttonYCenter + this.buttonSize, 0xFFFFFFFF);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if(this.isInButton((int)mouseX, (int)mouseY, true)) {
			this.onPlus();
		}

		if(this.isInButton((int)mouseX, (int)mouseY, false)) {
			this.onMinus();
		}

		return false;
	}

	@Override
	public boolean mouseReleased(double p_mouseReleased_1_, double p_mouseReleased_3_, int p_mouseReleased_5_) {
		return false;
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double scrollDirection) {
		if(scrollDirection > 0) {
			this.onPlus();
		} else if(scrollDirection < 0) {
			this.onMinus();
		}

		return true;
	}
}
