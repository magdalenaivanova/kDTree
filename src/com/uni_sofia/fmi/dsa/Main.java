package com.uni_sofia.fmi.dsa;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.*;

public class Main {
	public static void main(String[] args) throws IOException {

		List<Point2d> points = new ArrayList<>();
		List<MyRectangle> rects = new ArrayList<>();
		List<MyCircle> cirs = new ArrayList<>();

		loadPoints(Paths.get("files", "Points.txt"), points);
		loadObjects(Paths.get("files", "Objects.txt"), rects, cirs);

		KdTree tree = new KdTree(points);
		
		System.out.println(tree.toString());

		for (MyRectangle myRectangle : rects) {
			System.out.println("Points contained by " + myRectangle.toString() + " -> " + tree.range(myRectangle));
		}

		for (MyCircle myCircle : cirs) {
			System.out.println("Points contained by " + myCircle.toString() + " -> " + tree.range(myCircle));
		}

	}

	private static void loadPoints(Path filePath, List<Point2d> points) throws IOException {
		try (Stream<String> lines = Files.lines(filePath)) {
			lines.forEach(line -> addPoints(points, line));
		}
	}

	private static void addPoints(List<Point2d> points, String info) {
		String elements[] = info.split(", ");

		Point2d point = new Point2d(Double.parseDouble(elements[0]), Double.parseDouble(elements[1]));

		points.add(point);
	}

	private static void loadObjects(Path filePath, List<MyRectangle> rects, List<MyCircle> cirs) throws IOException {
		try (Stream<String> lines = Files.lines(filePath)) {
			lines.forEach(line -> addObjects(rects, cirs, line));
		}
	}

	private static void addObjects(List<MyRectangle> rects, List<MyCircle> cirs, String info) {
		String elements[] = info.split(" ");
		if (elements[0].equals("rect")) {

			MyRectangle rect = new MyRectangle(Double.parseDouble(elements[1]), Double.parseDouble(elements[2]),
					Double.parseDouble(elements[3]), Double.parseDouble(elements[4]));

			rects.add(rect);
		} else if (elements[0].equals("circle")) {

			MyCircle cir = new MyCircle(Double.parseDouble(elements[1]), Double.parseDouble(elements[2]),
					Double.parseDouble(elements[3]));

			cirs.add(cir);
		}
	}

}
