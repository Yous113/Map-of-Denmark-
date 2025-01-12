package org.example.firstyearproject.ShortestPath;

import Helpers.Generated;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.example.firstyearproject.Node;

import java.io.Serializable;
import java.util.*;

public class Astar implements Serializable {


    public List<Long> findNeighborsAStar(Long startNode, Long goalNode, EdgeWeightedDigraph graph, HashMap<Long, Node> id2node) {
        Set<Long> openSet = new HashSet<>();
        Set<Long> closedSet = new HashSet<>();
        Map<Long, Long> cameFrom = new HashMap<>();
        Map<Long, Double> gScore = new HashMap<>();
        Map<Long, Double> fScore = new HashMap<>();


        // Initialize scores for start node
        gScore.put(startNode, 0.0);
        fScore.put(startNode, id2node.get(startNode).heuristic(id2node.get(goalNode)));

        // Add start node to open set
        openSet.add(startNode);

        while (!openSet.isEmpty()) {
            // Find the node in the open set with the lowest fScore
            Long current = openSet.stream().min(Comparator.comparing(fScore::get)).get();

            if (current.equals(goalNode)) {
                // Reconstruct and return the path
                return reconstructPath(cameFrom, current);
            }

            openSet.remove(current);
            closedSet.add(current);

            // Expand neighbors of the current node

            for (DirectedEdge edge : graph.adj(current)) {
                Long neighbor = edge.to();

                if (closedSet.contains(neighbor)) {
                    continue; // Ignore already evaluated neighbor
                }

                double tentativeGScore = gScore.getOrDefault(current, Double.MAX_VALUE) + edge.weight();

                if (!openSet.contains(neighbor) || tentativeGScore < gScore.getOrDefault(neighbor, Double.MAX_VALUE)) {
                    // Update the path to this node
                    cameFrom.put(neighbor, current);
                    gScore.put(neighbor, tentativeGScore);
                    fScore.put(neighbor, tentativeGScore + id2node.get(neighbor).heuristic(id2node.get(goalNode)));
                    if (!openSet.contains(neighbor)) {
                        openSet.add(neighbor);
                    }
                }
            }
        }

        // No path found
        return null;
    }

    private static List<Long> reconstructPath(Map<Long, Long> cameFrom, Long current) {
        List<Long> path = new ArrayList<>();
        while (cameFrom.containsKey(current)) {
            path.add(current);
            current = cameFrom.get(current);
        }
        Collections.reverse(path);
        return path;
    }

    @Generated
    public void shortestPathCar(Long startNode, Long goalNode, EdgeWeightedDigraph graph, GraphicsContext gc, HashMap<Long, Node> id2node) {

        List<Long> shortest = findNeighborsAStar(startNode, goalNode, graph, id2node);

        gc.beginPath();
        gc.moveTo(0.56 * id2node.get(startNode).getLong(), -id2node.get(startNode).getLat());

        assert shortest != null;
        for (Long id : shortest) {
            gc.lineTo(0.56 * id2node.get(id).getLong(), -id2node.get(id).getLat());
            gc.setLineWidth(0.001);
            Color color = Color.BLUE;
            gc.setStroke(color);
        }
        gc.stroke();
    }
}