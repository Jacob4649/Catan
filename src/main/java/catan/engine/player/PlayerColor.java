package catan.engine.player;

import java.awt.Color;

import catan.renderer.Colors;

public enum PlayerColor {
	RED(Colors.PLAYER_RED), WHITE(Colors.PLAYER_WHITE), BLUE(Colors.PLAYER_BLUE), GREEN(Colors.PLAYER_GREEN), YELLOW(
			Colors.PLAYER_YELLOW);

	private Color m_color;

	private PlayerColor(Color color) {
		m_color = color;
	}
	
	public Color getColor() {
		return m_color;
	}
}
