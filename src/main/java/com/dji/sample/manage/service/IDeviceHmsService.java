package com.dji.sample.manage.service;

import com.dji.sample.common.model.PaginationData;
import com.dji.sample.component.mqtt.model.CommonTopicReceiver;
import com.dji.sample.manage.model.dto.DeviceHmsDTO;
import com.dji.sample.manage.model.param.DeviceHmsQueryParam;
import org.springframework.messaging.MessageHeaders;

/**
 * @author sean
 * @version 1.1
 * @date 2022/7/6
 */
public interface IDeviceHmsService {

    void handleHms(CommonTopicReceiver receiver, MessageHeaders headers);

    PaginationData<DeviceHmsDTO> getDeviceHmsByParam(DeviceHmsQueryParam param);

    void updateUnreadHms(String deviceSn);
}
