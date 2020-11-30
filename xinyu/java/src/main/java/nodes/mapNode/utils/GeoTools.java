package nodes.mapNode.utils;

import nodes.mapNode.models.Position;

/**
 * Created by Xinyu Zhu on 2020/11/29, 22:00
 * nodes.mapNode.utils in codingDimensionTemplate
 */
public class GeoTools {

    //赤道半径
    private static final double EARTH_RADIUS = 6378137;

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    // 计算经纬度坐标的近似距离公式
    public static double getDistance(double lon1, double lat1, double lon2, double lat2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lon1) - rad(lon2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        //单位米
        return s;
    }

    // Meter
    public double getDistanceBetween(Position currentPosition, Position otherPosition) {
        return GeoTools.getDistance(currentPosition.getLng(), currentPosition.getLat(), otherPosition.getLng(), otherPosition.getLat());
    }

    // 计算往特定方向位移distance后所在的位置
    public Position moveToDirection(Position currentPosition, double directionInRad, double distance) {
        return null;
    }

    public Position moveToEast(Position currentPosition, double distance) {
        return moveToDirection(currentPosition, rad(0), distance);
    }

    public Position moveToNorth(Position currentPosition, double distance) {
        return moveToDirection(currentPosition, rad(90), distance);
    }

    public Position moveToWest(Position currentPosition, double distance) {
        return moveToDirection(currentPosition, rad(180), distance);
    }

    public Position moveToSouth(Position currentPosition, double distance) {
        return moveToDirection(currentPosition, rad(270), distance);
    }


}
