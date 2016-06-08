public class Route {
    public int distance;
    public Stop source;
    public Stop destination;
    public Route(Stop source, Stop destination, int distance){
        this.distance = distance;
        this.source = source;
        this.destination = destination;
    }
}
