package application;

import java.io.Serializable;

public class ObjectData implements Serializable {
    private static final long serialVersionUID = 1L;

    private String type;
    private int row;
    private int col;

    public ObjectData(String type, int row, int col) {
        this.type = type;
        this.row = row;
        this.col = col;
    }

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}