import java.util.List;

public interface IGraph<W,S>{
    public List<W> getVertices();
    public S getEdgeValue(W w1, W w2);
    public List<W> getEdges(W w);
}