import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MyGraph<W, S> implements IGraph<W, S> {
    // mapa wierzcholkow wraz z lista sasiedztwa w postaci listy wiązanej
    // uzycie listy wiazanej umozliwia latwe dodawanie nowych krawedzi
    // krawedzie sa przedstawione za pomoca klasy Edge, ktora jest skierowana - posiada informacje o powiazaniu
    // do kolejnego wierzcholka oraz informacje o wartosci - etykiecie
    private final Map<W,LinkedList<Edge>> adjList;

    public MyGraph(Map<W, Map<W, S>> edges) {
        this.adjList = new HashMap<>();
        for (W w : edges.keySet()) {
            // dodanie kazdego wierzcholka do mapy i zainicjowanie pustej listy sasiedztwa
            adjList.putIfAbsent(w, new LinkedList<>());
            Map<W,S> destinations = edges.get(w);
            for (W dest : destinations.keySet()) {
                // jesli istnieje taki wierzcholek, dodanie powiazania
                if(edges.containsKey(dest)){
                    adjList.get(w).add(new Edge(dest, destinations.get(dest)));
                }
            }
        }
    }

    // prywatna klasa krawedzi
    private class Edge {
        private final W destination;
        private final S label;

        public Edge(W destination, S label) {
            this.destination = destination;
            this.label = label;
        }

        public W getDestination() {
            return destination;
        }
        public S getLabel(){
            return label;
        } 
        
    }

    @Override
    public List<W> getVertices() {
        return new ArrayList<>(adjList.keySet());
    }

    // pobranie informacji o etykiecie, jesli istnieje takie powiazanie
    @Override
    public S getEdgeValue(W w1, W w2) {
        if(adjList.containsKey(w1)){
            for (Edge edge : adjList.get(w1)) {
                if (edge.destination.equals(w2))
                    return edge.getLabel();
            }
        }
        return null;
    }

    @Override
    public List<W> getEdges(W w) {
        List<W> edges = new ArrayList<>();
        if (adjList.containsKey(w)) {
            for (Edge edge : adjList.get(w)) {
                edges.add(edge.getDestination());
            }
        }
        return edges;
    }
    
}