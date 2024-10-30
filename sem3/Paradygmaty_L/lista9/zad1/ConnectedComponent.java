import java.util.*;
// do wykonania tego zadania posłużyłem się algorytmem Kosaraju'sa
// aby wyznaczyć silnie spójne składowe grafu
// kolejne kroki w uproszczeniu:
// 1 - odwiedzic kazdy wierzcholek grafu metodą DFS i odkladac kolejne odwiedzone wierzcholki na stos
// 2 - wykonac transpozycje grafu (odwrocic skierowane krawedzie)
// 3 - zgodnie z kolejnoscia stosu odwiedin, wykonac DFS dla kolejnych wierzcholkow
// zapisujac te odwiedzone wierzcholki, ktore jeszcze nie byly odwiedzone w transponowanym grafie
// ilosc powtorzen kroku 3 daje informacje o ilosci silnie spojnych podgrafow


public class ConnectedComponent<W, S> {
    private final MyGraph<W, S> graph;
    Stack<W> visitedNodes = new Stack<>();

    public ConnectedComponent(MyGraph<W, S> graph) {
        this.graph = graph;
    }

    public List<List<W>> getSCC() {
        MyGraph<W, S> transposedGraph = getTransposedGraph();
        List<List<W>> output = new ArrayList<>();
        
        for (W w : graph.getVertices()) {
            DFStraversal(w);
        }
        System.out.println(visitedNodes);

        while (!visitedNodes.isEmpty()) {
            Stack<W> tVisitedNodes = new Stack<>();
            List<W> currentList = new ArrayList<>();
            tVisitedNodes.push(visitedNodes.firstElement());
            while (!tVisitedNodes.isEmpty()) {
                W current = tVisitedNodes.pop();
                if (visitedNodes.contains(current)) {
                    currentList.add(current);
                    visitedNodes.remove(current);
                    for (W neighbor : transposedGraph.getEdges(current)) {
                        if(visitedNodes.contains(neighbor)) {
                            tVisitedNodes.push(neighbor);
                        }
                    }
                }
                
            }
            output.add(currentList);
        }
        return output;
    }

    private MyGraph<W, S> getTransposedGraph() {
        Map<W, Map<W, S>> transposedEdges = new HashMap<>();
        for (W w : graph.getVertices()) {
            for (W neighbor : graph.getEdges(w)) {
                transposedEdges.putIfAbsent(neighbor, new HashMap<>());
                transposedEdges.get(neighbor).put(w, graph.getEdgeValue(w, neighbor));
            }
        }
        return new MyGraph<>(transposedEdges);
    }

    private void DFStraversal(W startNode) {
        Stack<W> nodesToVisit = new Stack<>();
        nodesToVisit.push(startNode);
        while (!nodesToVisit.isEmpty()) {
            W current = nodesToVisit.pop();
            if (!visitedNodes.contains(current)) {
                visitedNodes.push(current);
                for (W neighbor : graph.getEdges(current)) {
                    if (!visitedNodes.contains(neighbor)) {
                        nodesToVisit.push(neighbor);
                    }
                }
            }
        }
    }
}