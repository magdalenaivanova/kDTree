package com.uni_sofia.fmi.dsa;

public class MyRectangle {

	private double xLeft;
	private double yTop;
	private double width;
	private double height;

	public MyRectangle(double xLeft, double yTop, double width, double height) {
		this.xLeft = xLeft;
		this.yTop = yTop;
		this.width = width;
		this.height = height;
	}

	public double getxLeft() {
		return xLeft;
	}

	public double getyTop() {
		return yTop;
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(height);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(width);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(xLeft);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(yTop);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MyRectangle other = (MyRectangle) obj;
		if (Double.doubleToLongBits(height) != Double.doubleToLongBits(other.height))
			return false;
		if (Double.doubleToLongBits(width) != Double.doubleToLongBits(other.width))
			return false;
		if (Double.doubleToLongBits(xLeft) != Double.doubleToLongBits(other.xLeft))
			return false;
		if (Double.doubleToLongBits(yTop) != Double.doubleToLongBits(other.yTop))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Rectangle [xMin=" + xLeft + " yTop= " + yTop + " Width= " + width + " height= " + height + "]";
	}

}