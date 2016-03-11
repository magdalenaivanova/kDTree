# kDTree

When making geometrically-intensive applications such as games, movies and architectural visualizations it is crucial to use fast algorithms. A key geometrical algorithm is to detect whether a point lies within a box or a circle. This is usually done by looping over all shapes and checking whether the point lies within it. The kd-tree is a space-partitioning data structure that significantly improves this check by reducing the amount of shapes to test with.

Your task is to implement the a program that can find all objects that a given point intersects with. Implement the check whether the 2D point lies within a circle and an axis-aligned rectangle. Use a kd-tree to discover the shapes to loop over.
