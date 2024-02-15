package GraphPackage;
import ADTPackage.*; // Classes that implement various ADTs
import Metro.Test;

public class StationInfo {
    private double arrivalTime;
    private String directionID;
    private String routeShortName;
    private String routeLongName;

    public StationInfo(double arrivalTime, String directionID, String routeShortName, String routeLongName) {
        this.arrivalTime = arrivalTime;
        this.directionID = directionID;
        this.routeShortName = routeShortName;
        this.routeLongName = routeLongName;
    }

    public double getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(double arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getDirectionID() {
        return directionID;
    }

    public void setDirectionID(String directionID) {
        this.directionID = directionID;
    }

    public String getRouteShortName() {
        return routeShortName;
    }

    public void setRouteShortName(String routeShortName) {
        this.routeShortName = routeShortName;
    }

    public String getRouteLongName() {
        return routeLongName;
    }

    public void setRouteLongName(String routeLongName) {
        this.routeLongName = routeLongName;
    }
}
