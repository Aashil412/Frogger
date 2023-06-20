package application;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GridData implements Serializable {
	private static final long serialVersionUID = 1L;
	private int rows;
	private int cols;
	private List<ObjectData> objects;
	private int frogRow;
	private int frogCol;

	public GridData(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
		objects = new ArrayList<>();
		frogRow = -1;
		frogCol = -1;
	}

	public int getRows() {
		return rows;
	}

	public int getCols() {
		return cols;
	}

	public List<ObjectData> getObjects() {
		return objects;
	}

	public void addObject(ObjectData objectData) {
		objects.add(objectData);
	}

	public int getFrogRow() {
		return frogRow;
	}

	public void setFrogRow(int frogRow) {
		this.frogRow = frogRow;
	}

	public int getFrogCol() {
		return frogCol;
	}

	public void setFrogCol(int frogCol) {
		this.frogCol = frogCol;
	}
}