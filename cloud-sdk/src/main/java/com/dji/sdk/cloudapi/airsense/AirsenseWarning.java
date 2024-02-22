package com.dji.sdk.cloudapi.airsense;

/**
 * @author sean
 * @version 1.7
 * @date 2023/10/16
 */
public class AirsenseWarning {

    /**
     * ICAO civil aviation aircraft address
     */
    private String icao;

    /**
     * The higher the danger level, the more dangerous it is.
     * For levels greater than or equal to 3, it is recommended for aircraft to take evasive action.
     */
    private WarningLevelEnum warningLevel;

    /**
     * The latitude of aircraft location is angular values.
     * Negative values for south latitude and positive values for north latitude.
     * It is accurate to six decimal places.
     */
    private Float latitude;

    /**
     * The longitude of aircraft location is angular values.
     * Negative values for west longitude and positive values for east longitude.
     * It is accurate to six decimal places.
     */
    private Float longitude;

    /**
     * Absolute height of flight.
     * Unit: meter
     */
    private Integer altitude;

    /**
     * Absolute height type
     */
    private AltitudeTypeEnum altitudeType;

    /**
     * The angle of heading is angular values.
     * 0 is north. 90 is east.
     * It is accurate to one decimal places.
     */
    private Float heading;

    /**
     * Relative height of flight to aircraft.
     * Unit: meter
     */
    private Integer relativeAltitude;

    /**
     * Relative height change trend
     */
    private VertTrendEnum vertTrend;

    /**
     * Horizontal distance to aircraft.
     * Unit: meter
     */
    private Integer distance;

    public AirsenseWarning() {
    }

    @Override
    public String toString() {
        return "AirsenseWarning{" +
                "icao='" + icao + '\'' +
                ", warningLevel=" + warningLevel +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", altitude=" + altitude +
                ", altitudeType=" + altitudeType +
                ", heading=" + heading +
                ", relativeAltitude=" + relativeAltitude +
                ", vertTrend=" + vertTrend +
                ", distance=" + distance +
                '}';
    }

    public String getIcao() {
        return icao;
    }

    public AirsenseWarning setIcao(String icao) {
        this.icao = icao;
        return this;
    }

    public WarningLevelEnum getWarningLevel() {
        return warningLevel;
    }

    public AirsenseWarning setWarningLevel(WarningLevelEnum warningLevel) {
        this.warningLevel = warningLevel;
        return this;
    }

    public Float getLatitude() {
        return latitude;
    }

    public AirsenseWarning setLatitude(Float latitude) {
        this.latitude = latitude;
        return this;
    }

    public Float getLongitude() {
        return longitude;
    }

    public AirsenseWarning setLongitude(Float longitude) {
        this.longitude = longitude;
        return this;
    }

    public Integer getAltitude() {
        return altitude;
    }

    public AirsenseWarning setAltitude(Integer altitude) {
        this.altitude = altitude;
        return this;
    }

    public AltitudeTypeEnum getAltitudeType() {
        return altitudeType;
    }

    public AirsenseWarning setAltitudeType(AltitudeTypeEnum altitudeType) {
        this.altitudeType = altitudeType;
        return this;
    }

    public Float getHeading() {
        return heading;
    }

    public AirsenseWarning setHeading(Float heading) {
        this.heading = heading;
        return this;
    }

    public Integer getRelativeAltitude() {
        return relativeAltitude;
    }

    public AirsenseWarning setRelativeAltitude(Integer relativeAltitude) {
        this.relativeAltitude = relativeAltitude;
        return this;
    }

    public VertTrendEnum getVertTrend() {
        return vertTrend;
    }

    public AirsenseWarning setVertTrend(VertTrendEnum vertTrend) {
        this.vertTrend = vertTrend;
        return this;
    }

    public Integer getDistance() {
        return distance;
    }

    public AirsenseWarning setDistance(Integer distance) {
        this.distance = distance;
        return this;
    }
}
