import java.util.ArrayList;
import java.util.List;

public class RouteCaculator {
    private TrainGraph trainGraph;

    public RouteCaculator(TrainGraph trainGraph) {
        this.trainGraph = trainGraph;
    }

    public int caculateDistance(List<String> stops) throws RouteNotFoundException {
        int totalDistance = 0;
        for (int i = 0; i < stops.size() - 1; i++) {
            totalDistance += getDistance(stops.get(i), stops.get(i + 1));
        }
        return totalDistance;
    }

    private int getDistance(String sourceName, String destinationName) throws RouteNotFoundException {
        for (Route route : trainGraph.routes) {
            boolean sourceMatched = route.source.name.equals(sourceName);
            boolean destinationMatched = route.destination.name.equals(destinationName);

            if (sourceMatched && destinationMatched) {
                return route.distance;
            }
        }
        throw new RouteNotFoundException("NO SUCH ROUTE");
    }

    public Integer getShortestTripDistance(String sourceName, String destinationName) throws RouteNotFoundException {
        List<Trip> shortestTrips = getTripsWithNoCircle(sourceName, destinationName, new ArrayList<>());
        System.out.println("trip without circle");
        shortestTrips.stream().forEach(System.out::println);
        System.out.println();

        int min = 99999;

        for (Trip trip : shortestTrips) {
            List<Route> routeList = trip.routeList;
            int sum = 0;
            for (Route route : routeList) {
                sum += getDistance(route.source.name, route.destination.name);
            }
            if (sum < min) {
                min = sum;
            }
        }
        return min;
    }

    public List<Trip> getTripsWithNoCircle(String sourceName, String destinationName, List<Route> preRoutes) {
        ArrayList<Trip> returnedTrips = new ArrayList<>();
        for (Route route : trainGraph.routes) {
            if (!isCircle(preRoutes, route) && sourceName.equals(route.source.name)) {
                if (route.destination.name.equals(destinationName)) {
                    Trip trip = new Trip();
                    trip.routeList.add(route);
                    returnedTrips.add(trip);
                } else {
                    ArrayList<Route> preRoutes1 = new ArrayList<>();
                    preRoutes1.addAll(preRoutes);
                    preRoutes1.add(route);
                    List<Trip> subTrips = getTripsWithNoCircle(route.destination.name, destinationName, preRoutes1);
                    for (Trip subTrip : subTrips) {
                        Trip trip = new Trip();
                        trip.routeList.add(route);
                        trip.routeList.addAll(subTrip.routeList);
                        returnedTrips.add(trip);
                    }
                }
            }
        }
        return returnedTrips;
    }


    public List<Trip> getCommonTripWithCircle(String sourceName, String destinationName, Condition condition, int distance, int stopNums) throws RouteNotFoundException {
        ArrayList<Trip> returnedTrips = new ArrayList<>();
        for (Route route : trainGraph.routes) {

            int currentRouteDistance = getDistance(route.source.name, route.destination.name);
            if (!condition.isMatch(distance + currentRouteDistance, stopNums + 1))
                continue;

            if (sourceName.equals(route.source.name)) {
                if (route.destination.name.equals(destinationName)) {
                    Trip trip = new Trip();
                    trip.routeList.add(route);
                    returnedTrips.add(trip);
                }

                List<Trip> subTrips = getCommonTripWithCircle(route.destination.name, destinationName, condition, distance + currentRouteDistance, stopNums + 1);
                for (Trip subTrip : subTrips) {
                    Trip trip = new Trip();
                    trip.routeList.add(route);
                    trip.routeList.addAll(subTrip.routeList);
                    returnedTrips.add(trip);
                }
            }
        }
        return returnedTrips;
    }


    private boolean isCircle(List<Route> preRoutes, Route route) {
        return preRoutes.
                stream().
                anyMatch(preRoute -> preRoute.destination.name.equals(route.destination.name));

    }
}
