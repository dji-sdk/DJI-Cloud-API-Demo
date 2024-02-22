package com.dji.sample.manage.model.dto;

import com.dji.sdk.cloudapi.device.DeviceDomainEnum;
import com.dji.sdk.cloudapi.tsa.DeviceIconUrl;
import com.dji.sdk.cloudapi.tsa.DeviceTopology;
import com.dji.sdk.cloudapi.tsa.TopologyDeviceModel;

/**
 * @author sean
 * @version 0.2
 * @date 2021/12/8
 */
public class TopologyDeviceDTO extends DeviceTopology {

    private String model;

    private Boolean boundStatus;

    private String gatewaySn;

    private DeviceDomainEnum domain;

    public TopologyDeviceDTO() {
    }

    @Override
    public String toString() {
        return "TopologyDeviceDTO{" +
                "model='" + model + '\'' +
                ", boundStatus=" + boundStatus +
                ", gatewaySn='" + gatewaySn + '\'' +
                ", domain=" + domain +
                '}';
    }

    @Override
    public String getSn() {
        return super.getSn();
    }

    @Override
    public TopologyDeviceDTO setSn(String sn) {
        super.setSn(sn);
        return this;
    }

    @Override
    public String getDeviceCallsign() {
        return super.getDeviceCallsign();
    }

    @Override
    public TopologyDeviceDTO setDeviceCallsign(String deviceCallsign) {
        super.setDeviceCallsign(deviceCallsign);
        return this;
    }

    @Override
    public TopologyDeviceModel getDeviceModel() {
        return super.getDeviceModel();
    }

    @Override
    public TopologyDeviceDTO setDeviceModel(TopologyDeviceModel deviceModel) {
        super.setDeviceModel(deviceModel);
        return this;
    }

    @Override
    public Boolean getOnlineStatus() {
        return super.getOnlineStatus();
    }

    @Override
    public TopologyDeviceDTO setOnlineStatus(Boolean onlineStatus) {
        super.setOnlineStatus(onlineStatus);
        return this;
    }

    @Override
    public String getUserId() {
        return super.getUserId();
    }

    @Override
    public TopologyDeviceDTO setUserId(String userId) {
        super.setUserId(userId);
        return this;
    }

    @Override
    public String getUserCallsign() {
        return super.getUserCallsign();
    }

    @Override
    public TopologyDeviceDTO setUserCallsign(String userCallsign) {
        super.setUserCallsign(userCallsign);
        return this;
    }

    @Override
    public DeviceIconUrl getIconUrls() {
        return super.getIconUrls();
    }

    @Override
    public TopologyDeviceDTO setIconUrls(DeviceIconUrl iconUrls) {
        super.setIconUrls(iconUrls);
        return this;
    }

    public String getModel() {
        return model;
    }

    public TopologyDeviceDTO setModel(String model) {
        this.model = model;
        return this;
    }

    public Boolean getBoundStatus() {
        return boundStatus;
    }

    public TopologyDeviceDTO setBoundStatus(Boolean boundStatus) {
        this.boundStatus = boundStatus;
        return this;
    }

    public String getGatewaySn() {
        return gatewaySn;
    }

    public TopologyDeviceDTO setGatewaySn(String gatewaySn) {
        this.gatewaySn = gatewaySn;
        return this;
    }

    public DeviceDomainEnum getDomain() {
        return domain;
    }

    public TopologyDeviceDTO setDomain(DeviceDomainEnum domain) {
        this.domain = domain;
        return this;
    }
}
