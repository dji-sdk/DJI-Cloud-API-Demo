package com.dji.sdk.mqtt;

/**
 * The name of all channels.
 *
 * @author sean.zhou
 * @date 2021/11/10
 * @version 0.1
 */
public class ChannelName {

    public static final String INBOUND = "inbound";
    public static final String DEFAULT = "default";
    public static final String OUTBOUND = "outbound";

    // status
    public static final String INBOUND_STATUS = "inboundStatus";

    public static final String OUTBOUND_STATUS = "outboundStatus";

    public static final String INBOUND_STATUS_ONLINE = "inboundStatusOnline";

    public static final String INBOUND_STATUS_OFFLINE = "inboundStatusOffline";


    // state
    public static final String INBOUND_STATE = "inboundState";

    public static final String INBOUND_STATE_RC_CONTROL_SOURCE = "inboundStateRcControlSource";

    public static final String INBOUND_STATE_DOCK_DRONE_CONTROL_SOURCE = "inboundStateDockControlSource";

    public static final String INBOUND_STATE_RC_LIVESTREAM_ABILITY_UPDATE = "inboundStateRcLiveCapacity";

    public static final String INBOUND_STATE_DOCK_LIVESTREAM_ABILITY_UPDATE = "inboundStateDockLiveCapacity";

    public static final String INBOUND_STATE_RC_LIVE_STATUS = "inboundStateRcLiveStatus";

    public static final String INBOUND_STATE_DOCK_LIVE_STATUS = "inboundStateDockLiveStatus";

    public static final String INBOUND_STATE_RC_AND_DRONE_FIRMWARE_VERSION = "inboundStateRcAndDroneFirmwareVersion";

    public static final String INBOUND_STATE_DOCK_FIRMWARE_VERSION = "inboundStateDockFirmwareVersion";

    public static final String INBOUND_STATE_RC_PAYLOAD_FIRMWARE = "inboundStateRcPayloadFirmware";

    public static final String INBOUND_STATE_DOCK_DRONE_WPMZ_VERSION = "inboundStateDockDroneWpmzVersion";

    public static final String INBOUND_STATE_DOCK_DRONE_THERMAL_SUPPORTED_PALETTE_STYLE = "inboundStateDockDronePayload";

    public static final String INBOUND_STATE_DOCK_DRONE_RTH_MODE = "inboundStateDockDroneRthMode";

    public static final String INBOUND_STATE_DOCK_DRONE_CURRENT_RTH_MODE = "inboundStateDockDroneCurrentRthMode";

    public static final String INBOUND_STATE_DOCK_DRONE_COMMANDER_MODE_LOST_ACTION = "inboundStateDockDroneCommanderModeLostAction";

    public static final String INBOUND_STATE_DOCK_DRONE_CURRENT_COMMANDER_FLIGHT_MODE = "inboundStateDockDroneCurrentCommanderFlightMode";

    public static final String INBOUND_STATE_DOCK_DRONE_COMMANDER_FLIGHT_HEIGHT = "inboundStateDockDroneCommanderFlightHeight";

    public static final String INBOUND_STATE_DOCK_DRONE_MODE_CODE_REASON = "inboundStateDockDroneModeCodeReason";

    public static final String INBOUND_STATE_DOCK_DRONE_OFFLINE_MAP_ENABLE = "inboundStateDockDroneOfflineMapEnable";

    public static final String INBOUND_STATE_DOCK_AND_DRONE_DONGLE_INFOS = "inboundStateDockAndDroneDongleInfos";

    public static final String INBOUND_STATE_DOCK_SILENT_MODE = "inboundStateDockSilentMode";


    public static final String OUTBOUND_STATE = "outboundState";


    // services_reply
    public static final String INBOUND_SERVICES_REPLY = "inboundServicesReply";


    // osd
    public static final String INBOUND_OSD = "inboundOsd";

    public static final String INBOUND_OSD_RC = "inboundOsdRc";

    public static final String INBOUND_OSD_DOCK = "inboundOsdDock";

    public static final String INBOUND_OSD_RC_DRONE = "inboundOsdRcDrone";

    public static final String INBOUND_OSD_DOCK_DRONE = "inboundOsdDockDrone";


    // requests
    public static final String INBOUND_REQUESTS = "inboundRequests";

    public static final String INBOUND_REQUESTS_STORAGE_CONFIG_GET = "inboundRequestsStorageConfigGet";

    public static final String INBOUND_REQUESTS_AIRPORT_BIND_STATUS = "inboundRequestsAirportBindStatus";

    public static final String INBOUND_REQUESTS_AIRPORT_ORGANIZATION_GET = "inboundRequestsAirportOrganizationGet";

    public static final String INBOUND_REQUESTS_AIRPORT_ORGANIZATION_BIND = "inboundRequestsAirportOrganizationBind";

    public static final String INBOUND_REQUESTS_CONFIG = "inboundRequestsConfig";

    public static final String INBOUND_REQUESTS_FLIGHTTASK_RESOURCE_GET = "inboundRequestsFlightTaskResourceGet";

    public static final String INBOUND_REQUESTS_FLIGHT_AREAS_GET = "inboundRequestsFlightAreasGet";

    public static final String INBOUND_REQUESTS_OFFLINE_MAP_GET = "inboundRequestsOfflineMapGet";


    public static final String OUTBOUND_REQUESTS = "outboundRequests";


    // events
    public static final String INBOUND_EVENTS = "inboundEvents";

    public static final String OUTBOUND_EVENTS = "outboundEvents";

    public static final String INBOUND_EVENTS_DEVICE_EXIT_HOMING_NOTIFY = "inboundEventsDeviceExitHomingNotify";

    public static final String INBOUND_EVENTS_FLIGHTTASK_PROGRESS = "inboundEventsFlighttaskProgress";

    public static final String INBOUND_EVENTS_FLIGHTTASK_READY = "inboundEventsFlighttaskReady";

    public static final String INBOUND_EVENTS_FILE_UPLOAD_CALLBACK = "inboundEventsFileUploadCallback";

    public static final String INBOUND_EVENTS_HMS = "inboundEventsHms";

    public static final String INBOUND_EVENTS_CONTROL_PROGRESS = "inboundEventsControlProgress";

    public static final String INBOUND_EVENTS_OTA_PROGRESS = "inboundEventsOtaProgress";

    public static final String INBOUND_EVENTS_FILEUPLOAD_PROGRESS = "inboundEventsFileUploadProgress";

    public static final String INBOUND_EVENTS_FLY_TO_POINT_PROGRESS = "inboundEventsFlyToPointProgress";

    public static final String INBOUND_EVENTS_TAKEOFF_TO_POINT_PROGRESS = "inboundEventsTakeoffToPointProgress";

    public static final String INBOUND_EVENTS_DRC_STATUS_NOTIFY = "inboundEventsDrcStatusNotify";

    public static final String INBOUND_EVENTS_JOYSTICK_INVALID_NOTIFY = "inboundEventsJoystickInvalidNotify";

    public static final String INBOUND_EVENTS_HIGHEST_PRIORITY_UPLOAD_FLIGHT_TASK_MEDIA = "inboundEventsHighestPriorityUploadFlightTaskMedia";

    public static final String INBOUND_EVENTS_RETURN_HOME_INFO = "inboundEventsReturnHomeInfo";

    public static final String INBOUND_EVENTS_CUSTOM_DATA_TRANSMISSION_FROM_ESDK = "inboundEventsCustomDataTransmissionFromEsdk";

    public static final String INBOUND_EVENTS_CUSTOM_DATA_TRANSMISSION_FROM_PSDK = "inboundEventsCustomDataTransmissionFromPsdk";

    public static final String INBOUND_EVENTS_AIRSENSE_WARNING = "inboundEventsAirsenseWarning";

    public static final String INBOUND_EVENTS_FLIGHT_AREAS_SYNC_PROGRESS = "inboundEventsFlightAreasSyncProgress";

    public static final String INBOUND_EVENTS_FLIGHT_AREAS_DRONE_LOCATION = "inboundEventsFlightAreasDroneLocation";

    public static final String INBOUND_EVENTS_OFFLINE_MAP_SYNC_PROGRESS = "inboundEventsOfflineMapSyncProgress";

    public static final String INBOUND_EVENTS_POI_STATUS_NOTIFY = "inboundEventsPoiStatusNotify";

    public static final String INBOUND_EVENTS_CAMERA_PHOTO_TAKE_PROGRESS = "inboundEventsCameraPhotoTakeProgress";


    // property
    public static final String INBOUND_PROPERTY_SET_REPLY = "inboundPropertySetReply";


    // drc/up
    public static final String INBOUND_DRC_UP = "inboundDrcUp";

    public static final String INBOUND_DRC_UP_DRONE_CONTROL = "inboundDrcUpDroneControl";

    public static final String INBOUND_DRC_UP_DRONE_EMERGENCY_STOP = "inboundDrcUpDroneEmergencyStop";

    public static final String INBOUND_DRC_UP_HEART_BEAT = "inboundDrcUpHeartBeat";

    public static final String INBOUND_DRC_UP_HSI_INFO_PUSH = "inboundDrcUpHsiInfoPush";

    public static final String INBOUND_DRC_UP_DELAY_INFO_PUSH = "inboundDrcUpDelayInfoPush";

    public static final String INBOUND_DRC_UP_OSD_INFO_PUSH = "inboundDrcUpOsdInfoPush";

}
