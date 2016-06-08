import java.util.HashSet;
import java.util.Set;

public class TrainGraph {
    public Set<Stop> stops = new HashSet<Stop>();
    public Set<Route> routes = new HashSet<Route>();

    public void addNode(Stop stop){
        stops.add(stop);
    }

    public void addEdge(Route route){
        routes.add(route);
    }
}
