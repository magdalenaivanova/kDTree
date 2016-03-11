package com.uni_sofia.fmi.dsa;

public class Region {

	double xmin;
	double xmax;
	double ymin;
	double ymax;

	public Region(double xmin, double xmax, double ymin, double ymax) {
		this.xmin = xmin;
		this.xmax = xmax;
		this.ymin = ymin;
		this.ymax = ymax;
	}

	public Region() {
		this.xmin = 0;
		this.ymin = 0;
		this.xmax = 999;
		this.ymax = 999;
	}

	public double getXmin() {
		return xmin;
	}

	public double getYmin() {
		return ymin;
	}

	public double getXmax() {
		return xmax;
	}

	public double getYmax() {
		return ymax;
	}

	public String toString() {
		return "[" + xmin + ", " + ymin + "] x [" + xmax + ", " + ymax + "]";
	}

}