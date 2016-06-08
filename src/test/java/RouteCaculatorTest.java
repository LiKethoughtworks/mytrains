import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class RouteCaculatorTest {
    private RouteCaculator routeCaculator;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        List<String> edges = Arrays.asList("AB5", "BC4", "CD8", "DC8", "DE6", "AD5", "CE2", "EB3", "AE7");
        TrainGraph trainGraph = new TrainGraph();

        for (String edge : edges) {
            char[] edgeInfo = edge.toCharArray();
            Stop source = new Stop(Character.toString(edgeInfo[0]));
            Stop destination = new Stop(Character.toString(edgeInfo[1]));

            trainGraph.stops.add(source);
            trainGraph.stops.add(destination);
            trainGraph.routes.add(new Route(source, destination, Integer.parseInt(Character.toString(edgeInfo[2]))));
        }

        routeCaculator = new RouteCaculator(trainGraph);
    }

    @Test
    public void shouldCaculateDistanceBetweenTwoNode() throws RouteNotFoundException {
        List<String> stops = Arrays.asList(("A-B-C").split("-"));
        int distance = routeCaculator.caculateDistance(stops);

        assertEquals(9, distance);
    }

    @Test
    public void shouldRaiseRouteNotFoundExceptionWhenRouteNotFound() throws RouteNotFoundException {
        List<String> stops = Arrays.asList(("A-F").split("-"));

        thrown.expect(RouteNotFoundException.class);
        thrown.expectMessage("NO SUCH ROUTE");
        routeCaculator.caculateDistance(stops);
    }


    @Test
    public void shouldGetShortestStop() throws RouteNotFoundException {
        int distance = routeCaculator.getShortestTripDistance("A", "C");

        assertEquals(distance, 9);
    }

    @Test
    public void shouldGetShortestStop1() throws RouteNotFoundException {
        int distance = routeCaculator.getShortestTripDistance("B", "B");

        assertEquals(distance, 9);
    }

    @Test
    public void shouldGetShortestStop2() throws RouteNotFoundException {
        int distance = routeCaculator.getShortestTripDistance("C", "C");

        assertEquals(distance, 9);
    }

    @Test
    public void shouldGetRoutesDistanceLessThanSomePoints() throws RouteNotFoundException {
        List<Trip> trips = routeCaculator.getCommonTripWithCircle("C", "C", new DistanceCondition(new LT(30)), 0, 0);
//        CDC, CEBC, CEBCDC, CDCEBC, CDEBC, CEBCEBC, CEBCEBCEBC.
        trips.stream().forEach(System.out::println);

        assertEquals(trips.size(), 7);
    }

    @Test
    public void shouldGetRoutesForStopsWithExactlyStops() throws RouteNotFoundException {
        List<Trip> tripStream = routeCaculator.
                getCommonTripWithCircle("A", "C", new StopCondition(new LE(4)), 0, 0)
                .stream()
                .filter(trip -> trip.routeList.size() == 4)
                .collect(Collectors.toList());

        tripStream.stream().forEach(System.out::println);
        assertEquals(3, tripStream.size());
        //a-b b-c c-d d-c
        //a-d d-c c-d d-c
        //a-d d-e e-b b-c
    }


    @Test
    public void shouldGetRoutesForStopsWithLimitedStops() throws RouteNotFoundException {
        List<Trip> trips = routeCaculator.getCommonTripWithCircle("C", "C", new StopCondition(new LE(3)), 0, 0);

        assertEquals(2, trips.size());
        for (Trip trip : trips) {
            if (trip.routeList.size() == 2) {
                assertEquals("C", trip.routeList.get(0).source.name);
                assertEquals("D", trip.routeList.get(0).destination.name);
                assertEquals("D", trip.routeList.get(1).source.name);
                assertEquals("C", trip.routeList.get(1).destination.name);
            }
            if (trip.routeList.size() == 3) {
                assertEquals("C", trip.routeList.get(0).source.name);
                assertEquals("E", trip.routeList.get(0).destination.name);
                assertEquals("E", trip.routeList.get(1).source.name);
                assertEquals("B", trip.routeList.get(1).destination.name);
                assertEquals("B", trip.routeList.get(2).source.name);
                assertEquals("C", trip.routeList.get(2).destination.name);
            }
        }
    }

}
