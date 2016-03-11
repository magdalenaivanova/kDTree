package com.uni_sofia.fmi.dsa;

import java.util.Comparator;

public class Point2d implements Comparable<Point2d> {

	private double x;
	private double y;

	public Point2d(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public static final Comparator<Point2d> X_COMPARATOR = new Comparator<Point2d>() {

		@Override
		public int compare(Point2d p1, Point2d p2) {
			if (p1.x != p2.x) {
				return (p1.x < p2.x) ? -1 : 1;
			} else {
				return 0;
			}
		}
	};

	public static final Comparator<Point2d> Y_COMPARATOR = new Comparator<Point2d>() {

		@Override
		public int compare(Point2d p1, Point2d p2) {
			if (p1.y != p2.y) {
				return (p1.y < p2.y) ? -1 : 1;
			} else {
				return 0;
			}
		}
	};

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
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
		Point2d other = (Point2d) obj;
		if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
			return false;
		if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
			return false;
		return true;
	}

	@Override
	public int compareTo(Point2d o) {
		int xComp = X_COMPARATOR.compare(this, o);
		if (xComp != 0)
			return xComp;
		int yComp = Y_COMPARATOR.compare(this, o);
		return yComp;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("(");
		builder.append(x).append(", ");
		builder.append(y).append(")");
		return builder.toString();
	}
}