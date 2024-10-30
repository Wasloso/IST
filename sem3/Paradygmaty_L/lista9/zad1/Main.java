import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Map<String, Map<String, Integer>> map = new HashMap<>();
        map.put("A", Map.of("B", 2, "C", 3, "D", 4));
        map.put("B", new HashMap<>());
        map.put("C", Map.of("E",10));
        map.put("D", Map.of("B",2));
        map.put("E", Map.of("A",3));
        MyGraph<String, Integer> graph1 = new MyGraph<>(map);
        System.out.println("Graf 1");
        System.out.println("Wierzcholki: " + graph1.getVertices().toString());
        System.out.println("Krawedzie wychodzace z A: " + graph1.getEdges("A").toString());
        System.out.println("Krawedzie wychodzace z D: " + graph1.getEdges("D").toString());
        System.out.println("Etykieta krawedzi z C do E: " + graph1.getEdgeValue("C", "E").toString());

        ConnectedComponent<String, Integer> graph1SCC = new ConnectedComponent<>(graph1);
        System.out.println("Silnie spojne skladowe grafu 1: "+graph1SCC.getSCC());
        
        Map<Integer, Map<Integer, Character>> map2 = new HashMap<>();
        map2.put(1, Map.of(5,'g'));
        map2.put(2, Map.of(1,'a',3,'b',4,'c'));
        map2.put(3, new HashMap<>());
        map2.put(4, new HashMap<>());
        map2.put(5, Map.of(6,'d',3,'e',2,'h'));
        map2.put(6, Map.of(5,'f'));
        MyGraph<Integer, Character> graph2 = new MyGraph<>(map2);

        System.out.println("Graf 2");
        System.out.println("Wierzcholki: " + graph2.getVertices().toString());
        System.out.println("Krawedzie wychodzace z 2: " + graph2.getEdges(2).toString());
        System.out.println("Krawedzie wychodzace z 5: " + graph2.getEdges(5).toString());
        System.out.println("Etykieta krawedzi z 3 do 4: " + graph2.getEdgeValue(3, 4));

        ConnectedComponent<Integer, Character> graph2SCC = new ConnectedComponent<>(graph2);
        System.out.println("Silnie spojne skladowe grafu 2: " + graph2SCC.getSCC());
        
        Map<Integer, Map<Integer, Integer>> map3 = new HashMap<>();
        MyGraph<Integer, Integer> graph3 = new MyGraph<>(map3);
        System.out.println("Graf 3 (pusty)");
        System.out.println("Wierzcholki: " + graph3.getVertices().toString());
        System.out.println("Krawedzie wychodzace z 0: " + graph3.getEdges(0).toString());

        Map<Integer, Map<Integer, Integer>> map4 = new HashMap<>();
        map4.put(1, Map.of(2,0));
        map4.put(2, Map.of(3,0,4,0));
        map4.put(3, Map.of(1,0,4,0));
        map4.put(4, new HashMap<>());
        map4.put(5, Map.of(6,0,7,0));
        map4.put(6, Map.of(8,0));
        map4.put(7, Map.of(6,0));
        map4.put(8, Map.of(7,0));
        MyGraph<Integer, Integer> graph4 = new MyGraph<>(map4);
        ConnectedComponent<Integer, Integer> graph4SCC = new ConnectedComponent<>(graph4);

        System.out.println("Graf 4");
        System.out.println("Silnie spojne skladowe grafu 4: " + graph4SCC.getSCC());





        

        
    }
}
