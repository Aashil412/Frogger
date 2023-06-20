package objects;

import javafx.scene.layout.Region;

public abstract class GameObject extends Region{
	private int row;
	private int col;
	private String label;

	public GameObject(int row, int col, String l) {
		this.row = row;
		this.col = col;
		this.label = l;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public String getLabel() {
		return label;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public void setLabel(String label) {
		this.label = label;
	}
}