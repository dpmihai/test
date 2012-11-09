package test;

import java.io.Serializable;

/**
 * User: mihai.panaitescu
 * Date: 20-May-2010
 * Time: 13:54:58
 */
public class Border implements Serializable {

    private static final long serialVersionUID = -7417875051872527094L;

    private int left;
	private int right;
	private int top;
	private int bottom;

    public Border() {
    }

	public Border(int left, int right, int top, int bottom) {
		this.left = left;
		this.right = right;
		this.top = top;
		this.bottom = bottom;
	}

	public int getBottom() {
		return bottom;
	}

	public void setBottom(int bottom) {
		this.bottom = bottom;
	}

	public int getLeft() {
		return left;
	}

	public void setLeft(int left) {
		this.left = left;
	}

	public int getRight() {
		return right;
	}

	public void setRight(int right) {
		this.right = right;
	}

	public int getTop() {
		return top;
	}

	public void setTop(int top) {
		this.top = top;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Border border = (Border) o;

        if (bottom != border.bottom) return false;
        if (left != border.left) return false;
        if (right != border.right) return false;
        if (top != border.top) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        result = left;
        result = 29 * result + right;
        result = 29 * result + top;
        result = 29 * result + bottom;
        return result;
    }

    @Override
    public String toString() {
        return "(" + top + "," + left + "," + bottom + "," + right + ")";
    }

}

