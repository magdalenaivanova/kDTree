package com.uni_sofia.fmi.dsa;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KdTree {

	private KdNode root = null;

	protected static final int X_AXIS = 0;
	protected static final int Y_AXIS = 1;

	public KdTree(List<Point2d> list) {
		super();
		root = createNode(list, 0);
	}

	private KdNode createNode(List<Point2d> list, int depth) {

		List<Point2d> less = new ArrayList<Point2d>(list.size());
		List<Point2d> more = new ArrayList<Point2d>(list.size());

		if (list == null || list.size() == 0)
			return null;

		int axis = depth % 2;
		if (axis == X_AXIS)
			Collections.sort(list, Point2d.X_COMPARATOR);
		else
			Collections.sort(list, Point2d.Y_COMPARATOR);

		int mediaIndex = list.size() / 2;
		KdNode node = new KdNode(list.get(mediaIndex), depth);
		if (list.size() > 0) {
			if (list.size() > 0) {
				int medianIndex = list.size() / 2;

				node = new KdNode(list.get(medianIndex), depth);

				for (int i = 0; i < list.size(); i++) {
					Point2d p = list.get(i);
					if (i == medianIndex)
						continue;

					if (KdNode.compareTo(depth, p, node.point) <= 0) {
						less.add(p);
					} else {
						more.add(p);
					}
				}

				if ((medianIndex + 1) <= (list.size() - 1) && more.size() > 0) {

					node.rightTopSubtree = createNode(more, depth + 1);
					node.rightTopSubtree.parent = node;

				}

				if ((medianIndex - 1) >= 0 && less.size() > 0) {

					node.leftBottomSubtree = createNode(less, depth + 1);
					node.leftBottomSubtree.parent = node;

				}

			}

		}
		return node;

	}

	public boolean add(Point2d value) {
		if (value == null)
			return false;

		if (root == null) {
			root = new KdNode(value);
			return true;
		}

		KdNode node = root;
		while (true) {
			if (KdNode.compareTo(node.depth, value, node.point) <= 0) {
				if (node.leftBottomSubtree == null) {
					KdNode newNode = new KdNode(value, node.depth + 1);
					newNode.parent = node;
					node.leftBottomSubtree = newNode;
					break;
				}
				node = node.leftBottomSubtree;
			} else {
				if (node.rightTopSubtree == null) {
					KdNode newNode = new KdNode(value, node.depth + 1);
					newNode.parent = node;
					node.rightTopSubtree = newNode;
					break;
				}
				node = node.rightTopSubtree;
			}
		}

		return true;
	}

	public boolean isEmpty() {
		return root == null;
	}

	private List<Point2d> getTree(KdNode root) {
		List<Point2d> list = new ArrayList<>();

		if (root == null) {
			return list;
		}

		if (root.leftBottomSubtree != null) {
			list.add(root.leftBottomSubtree.point);
			list.addAll(getTree(root.leftBottomSubtree));
		}
		if (root.rightTopSubtree != null) {
			list.add(root.rightTopSubtree.point);
			list.addAll(getTree(root.rightTopSubtree));
		}

		return list;
	}

	// top & left - inclusive
	// right & bottom - exclusive

	// point in query test
	public boolean inRectangle(Point2d point, MyRectangle query) {
		return ((point.getX() >= query.getxLeft() && point.getX() < (query.getxLeft() + query.getWidth()))
				&& (point.getY() > (query.getyTop() - query.getHeight()) && point.getY() <= query.getyTop()));
	}

	private double getDistance(Point2d p1, Point2d p2) {
		return Math.sqrt(Math.pow((p1.getX() - p2.getX()), 2) + Math.pow((p1.getY() - p2.getY()), 2));
	}

	public boolean inCircle(Point2d point, MyCircle query) {

		Point2d centre = new Point2d(query.getX(), query.getY());

		return (getDistance(point, centre) < query.getRadius());

	}

	// region fully contained in query
	public boolean fullyContainedRegions(Region reg, MyRectangle query) {
		return ((reg.getXmin() >= query.getxLeft()) && (reg.getXmax() < (query.getxLeft() + query.getWidth()))
				&& (reg.getYmin() > (query.getyTop() - query.getHeight())) && (reg.getYmax() <= query.getyTop()));
	}

	public boolean fullyContainedRegionsCircle(Region reg, MyCircle query) {

		Point2d p1 = new Point2d(reg.getXmin(), reg.getYmax());
		Point2d p2 = new Point2d(reg.getXmin(), reg.getYmin());
		Point2d p3 = new Point2d(reg.getXmax(), reg.getYmax());
		Point2d p4 = new Point2d(reg.getXmax(), reg.getYmin());

		Point2d centre = new Point2d(query.getX(), query.getY());

		List<Point2d> point = new ArrayList<>();
		point.add(p1);
		point.add(p2);
		point.add(p3);
		point.add(p4);

		for (Point2d point2d : point) {
			if (getDistance(point2d, centre) < query.getRadius()) {
				return true;
			}
		}

		return false;
	}

	// region intersects query
	public boolean IntersectsRegion(Region r, MyRectangle query) {
		return (((query.getxLeft() + query.getWidth()) >= r.getXmin()) && (r.getXmax() >= query.getxLeft())
				&& (query.getyTop() >= r.getYmin()) && (r.getYmax() >= (query.getyTop() - query.getHeight())));
	}

	public boolean IntersectsRegionCircle(Region r, MyCircle query) {
		double x = Math.abs(query.getX() - (r.getXmin() + r.getXmax()) / 2);
		double y = Math.abs(query.getY() - (r.getYmin() + r.getYmax()) / 2);

		if (x > ((r.getXmin() + r.getXmax()) / 2 + query.getRadius())) {
			return false;
		} else if (y > ((r.getYmax() + r.getYmin()) / 2 + query.getRadius())) {
			return false;
		} else if (x <= ((r.getXmin() + r.getXmax()) / 2)) {
			return true;
		} else if (y <= (r.getYmax() + r.getYmin()) / 2) {
			return true;
		}

		double cornerDistance = Math.pow((x - (r.getXmin() + r.getXmax()) / 2), 2)
				+ Math.pow((y - (r.getYmax() + r.getYmin()) / 2), 2);
		
		return cornerDistance <= Math.pow(query.getRadius(), 2);
	}

	public void ReportSubTree(KdNode v, List<Point2d> found) {

		List<Point2d> list = new ArrayList<>();
		list = getTree(v);

		for (Point2d point2d : list) {
			found.add(point2d);
		}
	}

	public List<Point2d> range(MyRectangle rect) {

		List<Point2d> list = new ArrayList<>();

		if (!isEmpty()) {
			findPoints(list, rect, null, 0, root);
		}

		return list;
	}
	
	public List<Point2d> range(MyCircle cir) {

		List<Point2d> list = new ArrayList<>();

		if (!isEmpty()) {
			findPoints(list, cir, null, 0, root);
		}

		return list;
	}

	private void findPoints(List<Point2d> list, MyRectangle rect, Region reg, int depth, KdNode node) {

		if (inRectangle(node.point, rect)) {
			list.add(node.point);
		}

		boolean x_axis = (depth % 2) == 0;

		Region left = (reg == null) ? new Region() : new Region(reg.xmin, reg.xmax, reg.ymin, reg.ymax);
		Region right = (reg == null) ? new Region() : new Region(reg.xmin, reg.xmax, reg.ymin, reg.ymax);

		if (x_axis) {
			left.xmax = node.point.getX();
			right.xmin = node.point.getX();
		} else {
			left.ymax = node.point.getY();
			right.ymin = node.point.getY();
		}

		if (node.rightTopSubtree != null) {
			if (fullyContainedRegions(right, rect)) {
				list.add(node.rightTopSubtree.point);
				ReportSubTree(node.rightTopSubtree, list);
			} else if (IntersectsRegion(right, rect)) {
				findPoints(list, rect, right, depth + 1, node.rightTopSubtree);
			}

		}

		if (node.leftBottomSubtree != null) {
			if (fullyContainedRegions(left, rect)) {
				ReportSubTree(node.leftBottomSubtree, list);
				list.add(node.leftBottomSubtree.point);
			} else if (IntersectsRegion(left, rect)) {
				findPoints(list, rect, left, depth + 1, node.leftBottomSubtree);
			}

		}

	}

	private void findPoints(List<Point2d> list, MyCircle cir, Region reg, int depth, KdNode node) {

		if (inCircle(node.point, cir)) {
			list.add(node.point);
		}

		boolean x_axis = (depth % 2) == 0;

		Region left = (reg == null) ? new Region() : new Region(reg.xmin, reg.xmax, reg.ymin, reg.ymax);
		Region right = (reg == null) ? new Region() : new Region(reg.xmin, reg.xmax, reg.ymin, reg.ymax);

		if (x_axis) {
			left.xmax = node.point.getX();
			right.xmin = node.point.getX();
		} else {
			left.ymax = node.point.getY();
			right.ymin = node.point.getY();
		}

		if (node.rightTopSubtree != null) {
			if (fullyContainedRegionsCircle(right, cir)) {
				list.add(node.rightTopSubtree.point);
				ReportSubTree(node.rightTopSubtree, list);
			} else if (IntersectsRegionCircle(right, cir)) {
				findPoints(list, cir, right, depth + 1, node.rightTopSubtree);
			}

		}

		if (node.leftBottomSubtree != null) {
			if (fullyContainedRegionsCircle(left, cir)) {
				ReportSubTree(node.leftBottomSubtree, list);
				list.add(node.leftBottomSubtree.point);
			} else if (IntersectsRegionCircle(left, cir)) {
				findPoints(list, cir, left, depth + 1, node.leftBottomSubtree);
			}

		}

	}
	
	@Override
	public String toString() {
		return TreePrinter.getString(this);
	}

	// KdNode------------------------------------------------------

	public static class KdNode implements Comparable<KdNode> {

		private Point2d point;
		private int depth;

		private KdNode parent;
		public KdNode leftBottomSubtree;
		public KdNode rightTopSubtree;;

		public KdNode(Point2d id) {
			this.point = id;
			this.depth = 0;
		}

		public KdNode(Point2d id, int depth) {
			this.point = id;
			this.depth = depth;
		}

		public static int compareTo(int depth, Point2d o1, Point2d o2) {
			int axis = depth % 2;
			if (axis == X_AXIS)
				return Point2d.X_COMPARATOR.compare(o1, o2);
			return Point2d.Y_COMPARATOR.compare(o1, o2);
		}

		public static int compare(int depth, double s, Point2d p) {
			boolean x_axis = (depth % 2 == 0);

			if (x_axis) {
				if (s != p.getX())
					return (p.getX() < s) ? -1 : 1;
				return 0;
			}
			if (s != p.getY())
				return (p.getY() < s) ? -1 : 1;
			return 0;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + depth;
			result = prime * result + ((rightTopSubtree == null) ? 0 : rightTopSubtree.hashCode());
			result = prime * result + ((point == null) ? 0 : point.hashCode());
			result = prime * result + ((leftBottomSubtree == null) ? 0 : leftBottomSubtree.hashCode());
			result = prime * result + ((parent == null) ? 0 : parent.hashCode());
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
			KdNode other = (KdNode) obj;
			if (depth != other.depth)
				return false;
			if (rightTopSubtree == null) {
				if (other.rightTopSubtree != null)
					return false;
			} else if (!rightTopSubtree.equals(other.rightTopSubtree))
				return false;
			if (point == null) {
				if (other.point != null)
					return false;
			} else if (!point.equals(other.point))
				return false;
			if (leftBottomSubtree == null) {
				if (other.leftBottomSubtree != null)
					return false;
			} else if (!leftBottomSubtree.equals(other.leftBottomSubtree))
				return false;
			if (parent == null) {
				if (other.parent != null)
					return false;
			} else if (!parent.equals(other.parent))
				return false;
			return true;
		}

		@Override
		public int compareTo(KdNode o) {
			return compareTo(depth, this.point, o.point);
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("depth=").append(depth);
			builder.append(" id=").append(point.toString());
			return builder.toString();
		}
	}

	protected static class TreePrinter {

		public static String getString(KdTree tree) {
			if (tree.root == null)
				return "Tree has no nodes.";
			return getString(tree.root, "", true);
		}

		private static String getString(KdNode node, String prefix, boolean isTail) {
			StringBuilder builder = new StringBuilder();

			if (node.parent != null) {
				String side = "left";
				if (node.parent.rightTopSubtree != null && node.point.equals(node.parent.rightTopSubtree.point))
					side = "right";
				builder.append(prefix + (isTail ? "└── " : "├── ") + "[" + side + "] " + "depth=" + node.depth + " id="
						+ node.point + "\n");
			} else {
				builder.append(
						prefix + (isTail ? "└── " : "├── ") + "depth=" + node.depth + " id=" + node.point + "\n");
			}
			List<KdNode> children = null;
			if (node.leftBottomSubtree != null || node.rightTopSubtree != null) {
				children = new ArrayList<KdNode>(2);
				if (node.leftBottomSubtree != null)
					children.add(node.leftBottomSubtree);
				if (node.rightTopSubtree != null)
					children.add(node.rightTopSubtree);
			}
			if (children != null) {
				for (int i = 0; i < children.size() - 1; i++) {
					builder.append(getString(children.get(i), prefix + (isTail ? "    " : "│   "), false));
				}
				if (children.size() >= 1) {
					builder.append(
							getString(children.get(children.size() - 1), prefix + (isTail ? "    " : "│   "), true));
				}
			}

			return builder.toString();
		}
	}
}