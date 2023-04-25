package com.dji.sample.manage.service;

import com.dji.sample.component.mqtt.model.EventsOutputProgressReceiver;
import com.dji.sample.component.mqtt.model.EventsReceiver;
import com.dji.sample.manage.model.dto.DeviceDTO;
import com.dji.sample.manage.model.receiver.FirmwareProgressExtReceiver;

import java.util.Optional;
import java.util.Set;

/**
 * @author sean
 * @version 1.4
 * @date 2023/3/21
 */
public interface IDeviceRedisService {

    /**
     * Determine if the device is online.
     * @param sn
     * @return
     */
    Boolean checkDeviceOnline(String sn);

    /**
     * Query the basic information of the device in redis.
     * @param sn
     * @return
     */
    Optional<DeviceDTO> getDeviceOnline(String sn);

    /**
     * Save the basic information of the device in redis.
     * @param device
     */
    void setDeviceOnline(DeviceDTO device);

    /**
     * Delete the basic device information saved in redis.
     * @param sn
     * @return
     */
    Boolean delDeviceOnline(String sn);

    /**
     * Get the device's osd real-time data.
     * @param sn
     * @param clazz
     * @param <T>
     * @return
     */
    <T> Optional<T> getDeviceOsd(String sn, Class<T> clazz);

    /**
     * Save the firmware update progress of the device in redis.
     * @param sn
     * @param events
     */
    void setFirmwareUpgrading(String sn, EventsReceiver<EventsOutputProgressReceiver<FirmwareProgressExtReceiver>> events);

    /**
     * Query the firmware update progress of the device in redis.
     * @param sn
     * @return
     */
    Optional<EventsReceiver<EventsOutputProgressReceiver<FirmwareProgressExtReceiver>>> getFirmwareUpgradingProgress(String sn);

    /**
     * Delete the firmware update progress of the device in redis.
     * @param sn
     * @return
     */
    Boolean delFirmwareUpgrading(String sn);

    /**
     * Save the hms key of the device in redis.
     * @param sn
     * @param keys
     */
    void addEndHmsKeys(String sn, String... keys);

    /**
     * Query all hms keys of the device in redis.
     * @param sn
     * @return
     */
    Set<String> getAllHmsKeys(String sn);

    /**
     * Delete all hms keys of the device in redis.
     * @param sn
     * @return
     */
    Boolean delHmsKeysBySn(String sn);
}
