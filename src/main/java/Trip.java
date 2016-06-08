import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Trip {
    List<Route> routeList = new ArrayList<Route>();

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        routeList.stream().forEach(route -> {
            stringBuilder.append(route.source.name)
                    .append("->")
                    .append(route.destination.name)
                    .append("; ");
        });

        return stringBuilder.toString();
    }

    public List<String> getStopNames(){
        HashSet<String> stops = new HashSet<>();
        for (Route route: routeList){
            stops.add(route.destination.name);
        }
        return new ArrayList<>(stops);
    }
}


