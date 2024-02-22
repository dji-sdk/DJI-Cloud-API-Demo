package com.dji.sdk.cloudapi.property;

import com.dji.sdk.cloudapi.device.DockSilentMode;
import com.dji.sdk.common.BaseModel;
import com.dji.sdk.config.version.CloudSDKVersionEnum;
import com.dji.sdk.config.version.GatewayTypeEnum;
import com.dji.sdk.exception.CloudSDKException;

import java.util.Arrays;
import java.util.Set;

/**
 * @author sean
 * @version 1.3
 * @date 2022/10/27
 */
public enum PropertySetEnum {

    NIGHT_LIGHTS_STATE("night_lights_state", NightLightsStateSet.class, CloudSDKVersionEnum.V0_0_1, Set.of(GatewayTypeEnum.DOCK, GatewayTypeEnum.DOCK2)),

    HEIGHT_LIMIT("height_limit", HeightLimitSet.class, CloudSDKVersionEnum.V0_0_1, Set.of(GatewayTypeEnum.DOCK, GatewayTypeEnum.DOCK2)),

    DISTANCE_LIMIT_STATUS("distance_limit_status", DistanceLimitStatusSet.class, CloudSDKVersionEnum.V0_0_1, Set.of(GatewayTypeEnum.DOCK, GatewayTypeEnum.DOCK2)),

    OBSTACLE_AVOIDANCE("obstacle_avoidance", ObstacleAvoidanceSet.class, CloudSDKVersionEnum.V0_0_1, Set.of(GatewayTypeEnum.DOCK, GatewayTypeEnum.DOCK2)),

    RTH_ALTITUDE("rth_altitude", RthAltitudeSet.class, CloudSDKVersionEnum.V0_0_1, Set.of(GatewayTypeEnum.DOCK, GatewayTypeEnum.DOCK2)),

    OUT_OF_CONTROL_ACTION("rc_lost_action", RcLostActionSet.class, CloudSDKVersionEnum.V0_0_1, Set.of(GatewayTypeEnum.DOCK, GatewayTypeEnum.DOCK2)),

    EXIT_WAYLINE_WHEN_RC_LOST("exit_wayline_when_rc_lost", ExitWaylineWhenRcLostSet.class, CloudSDKVersionEnum.V0_0_1, Set.of(GatewayTypeEnum.DOCK, GatewayTypeEnum.DOCK2), true),

    THERMAL_CURRENT_PALETTE_STYLE("thermal_current_palette_style", ThermalCurrentPaletteStyleSet.class, CloudSDKVersionEnum.V0_0_1, Set.of(GatewayTypeEnum.DOCK, GatewayTypeEnum.DOCK2)),

    THERMAL_GAIN_MODE("thermal_gain_mode", ThermalGainModeSet.class, CloudSDKVersionEnum.V0_0_1, Set.of(GatewayTypeEnum.DOCK, GatewayTypeEnum.DOCK2)),

    THERMAL_ISOTHERM_STATE("thermal_isotherm_state", ThermalIsothermStateSet.class, CloudSDKVersionEnum.V0_0_1, Set.of(GatewayTypeEnum.DOCK, GatewayTypeEnum.DOCK2)),

    THERMAL_ISOTHERM_UPPER_LIMIT("thermal_isotherm_upper_limit", ThermalIsothermUpperLimitSet.class, CloudSDKVersionEnum.V0_0_1, Set.of(GatewayTypeEnum.DOCK, GatewayTypeEnum.DOCK2)),

    THERMAL_ISOTHERM_LOWER_LIMIT("thermal_isotherm_lower_limit", ThermalIsothermLowerLimitSet.class, CloudSDKVersionEnum.V0_0_1, Set.of(GatewayTypeEnum.DOCK, GatewayTypeEnum.DOCK2)),

    RTH_MODE("rth_mode", DockDroneRthMode.class, CloudSDKVersionEnum.V1_0_0, Set.of(GatewayTypeEnum.DOCK2)),

    USER_EXPERIENCE_IMPROVEMENT("user_experience_improvement", UserExperienceImprovementSet.class, CloudSDKVersionEnum.V1_0_0, Set.of(GatewayTypeEnum.DOCK, GatewayTypeEnum.DOCK2)),

    COMMANDER_MODE_LOST_ACTION("commander_mode_lost_action", DockDroneCommanderModeLostAction.class, CloudSDKVersionEnum.V1_0_0, Set.of(GatewayTypeEnum.DOCK, GatewayTypeEnum.DOCK2)),

    COMMANDER_FLIGHT_HEIGHT("commander_flight_height", DockDroneCommanderFlightHeight.class, CloudSDKVersionEnum.V1_0_0, Set.of(GatewayTypeEnum.DOCK, GatewayTypeEnum.DOCK2)),

    OFFLINE_MAP_ENABLE("offline_map_enable", DockDroneOfflineMapEnable.class, CloudSDKVersionEnum.V1_0_1, Set.of(GatewayTypeEnum.DOCK2)),

    SILENT_MODE("silent_mode", DockSilentMode.class, CloudSDKVersionEnum.V1_0_2, Set.of(GatewayTypeEnum.DOCK)),

    ;

    private final String property;

    private final Class<? extends BaseModel> clazz;

    private final CloudSDKVersionEnum since;

    private final Set<GatewayTypeEnum> supportedDevices;

    private boolean deprecated;

    PropertySetEnum(String property, Class<? extends BaseModel> clazz, CloudSDKVersionEnum since, Set<GatewayTypeEnum> supportedDevices) {
        this.property = property;
        this.clazz = clazz;
        this.since = since;
        this.supportedDevices = supportedDevices;
    }

    PropertySetEnum(String property, Class<? extends BaseModel> clazz, CloudSDKVersionEnum since, Set<GatewayTypeEnum> supportedDevices, boolean deprecated) {
        this.property = property;
        this.clazz = clazz;
        this.since = since;
        this.supportedDevices = supportedDevices;
        this.deprecated = deprecated;
    }

    public String getProperty() {
        return property;
    }

    public Class<? extends BaseModel> getClazz() {
        return clazz;
    }

    public CloudSDKVersionEnum getSince() {
        return since;
    }

    public Set<GatewayTypeEnum> getSupportedDevices() {
        return supportedDevices;
    }

    public boolean isDeprecated() {
        return deprecated;
    }

    public static PropertySetEnum find(String property) {
        return Arrays.stream(values()).filter(propertyEnum -> propertyEnum.property.equals(property)).findAny()
                .orElseThrow(() -> new CloudSDKException(PropertySetEnum.class, property));
    }
}
