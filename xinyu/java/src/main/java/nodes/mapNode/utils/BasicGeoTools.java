package nodes.mapNode.utils;

import nodes.mapNode.models.Position;
import org.geotools.referencing.GeodeticCalculator;

import java.awt.geom.Point2D;

/**
 * Created by Xinyu Zhu on 2020/11/29, 22:00
 * nodes.mapNode.utils in codingDimensionTemplate
 */
public class BasicGeoTools {

    private static GeodeticCalculator gc = new GeodeticCalculator();

    // 计算经纬度坐标的距离
    public static double getDistance(double lon1, double lat1, double lon2, double lat2) {
        synchronized (gc) {
            gc.setStartingGeographicPoint(lon1, lat1);
            gc.setDestinationGeographicPoint(lon2, lat2);
            // 单位米
            return gc.getOrthodromicDistance();
        }
    }

    // Meter
    public static double getDistance(Position currentPosition, Position otherPosition) {
        return BasicGeoTools.getDistance(currentPosition.getLng(), currentPosition.getLat(), otherPosition.getLng(), otherPosition.getLat());
    }

    // 计算往特定方向位移distance后所在的位置
    public static Position moveToDirection(Position currentPosition, double directionInDegree, double distance) {
        synchronized (gc) {
            gc.setStartingGeographicPoint(currentPosition.getLng(), currentPosition.getLat());
            gc.setDirection(directionInDegree, distance);
            Point2D dest = gc.getDestinationGeographicPoint();
            return new Position(dest.getX(), dest.getY());
        }
    }

    public static Position moveToNorth(Position currentPosition, double distance) {
        return moveToDirection(currentPosition, 0, distance);
    }

    public static Position moveToEast(Position currentPosition, double distance) {
        return moveToDirection(currentPosition, 90, distance);
    }

    public static Position moveToSouth(Position currentPosition, double distance) {
        return moveToDirection(currentPosition, 180, distance);
    }


    public static Position moveToWest(Position currentPosition, double distance) {
        return moveToDirection(currentPosition, 270, distance);
    }


    public static void main(String[] args) {
        Position center = new Position( -122.05981155644098, 37.41063670903733);
        Position dest = moveToWest(center, 2000);
        System.out.println(center.getLat() + ", " + center.getLng());
        System.out.println(dest.getLat() + ", " + dest.getLng());
    }

}
