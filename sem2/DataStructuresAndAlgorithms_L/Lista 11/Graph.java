import java.util.*;

public class Graph<T> {
    private final Map<T, List<Edge<T>>> adjacencyList;
    public Graph(List<Edge<T>> edges) {
        adjacencyList = new HashMap<>();
        for (Edge<T> edge : edges) {
            // Dodaanie wszystkich wierzchołków do mapy, oraz wierzchołków do których mają połączenia
            T vertex1 = edge.getNode1();
            T vertex2 = edge.getNode2();
            adjacencyList.putIfAbsent(vertex1, new LinkedList<>());
            adjacencyList.putIfAbsent(vertex2, new LinkedList<>());
            adjacencyList.get(vertex1).add(edge);
            adjacencyList.get(vertex2).add(edge);
        }
    }

    public Map<T, Integer> calculateShortestPaths(T startNode) throws NoSuchElementException {
        if (!adjacencyList.containsKey(startNode)) throw new NoSuchElementException(); // jesli nie ma tego klucza - blad
        Map<T, Integer> distance = new HashMap<>(); // mapa z wierzchołkami oraz odleglosciami
        distance = Dikstrja(startNode,null);
        removeVertexesWithoutConnection(distance);
        return distance;
    }

    public Integer calculateShortestPath(T startNode, T endNode) throws NoSuchElementException {
        if (!adjacencyList.containsKey(startNode) || !adjacencyList.containsKey(endNode)) throw new NoSuchElementException(); // jesli nie ma tego klucza - blad
        return Dikstrja(startNode,endNode).get(endNode);
    }

    private Map<T, Integer> Dikstrja(T startNode,T endNode) {
        Map<T, Integer> distance = new HashMap<>(); // mapa z wierzchołkami oraz odleglosciami
        for (T vertex : adjacencyList.keySet()) {
            distance.put(vertex, Integer.MAX_VALUE); // przypisanie poczatkowej odleglosci jako maksymalna
        }
        distance.put(startNode, 0);

        Set<T> visited = new HashSet<>(); // do sprawdzania czy odleglosc do najdalszwo wierzclolka zostala juz obliczona

        PriorityQueue<T> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(distance::get)); // wierzcholki z najmniejsza odlegloscia beda na poczatku kolejki
        priorityQueue.offer(startNode);

        while (!priorityQueue.isEmpty()) {
            T currentNode = priorityQueue.poll();
            if(currentNode.equals(endNode)) return distance;
            if (visited.contains(currentNode)) {
                continue;
            }
            visited.add(currentNode);
            List<Edge<T>> neighborEdges = adjacencyList.get(currentNode);
            if (neighborEdges != null) {
                for (Edge<T> neighborEdge : neighborEdges) {
                    T neighborVertex = getNeighbor(neighborEdge, currentNode);
                    if (!visited.contains(neighborVertex)) {
                        int tempDistance = distance.get(currentNode) + neighborEdge.getDistance();
                        if (tempDistance < distance.get(neighborVertex)) {
                            distance.put(neighborVertex, tempDistance); // aktualizowanie wartosci odleglosci az dostaniemy najmniejsza
                            priorityQueue.offer(neighborVertex);
                        }
                    }
                }
            }
        }
        return distance;
    }



    private void removeVertexesWithoutConnection(Map<T, Integer> distance) {
        List<T> vertexesToDelete  = new ArrayList<>();
        for(T vertex : distance.keySet()){
            if(distance.get(vertex)==0 || distance.get(vertex)==Integer.MAX_VALUE){
                vertexesToDelete.add(vertex);
            }
        }
        for(T vertex : vertexesToDelete){
            distance.remove(vertex);
        }
    }

    private T getNeighbor(Edge<T> edge, T currentVertex) {
        T vertex1 = edge.getNode1();
        T vertex2 = edge.getNode2();
        return currentVertex.equals(vertex1) ? vertex2 : vertex1;
    }
}
