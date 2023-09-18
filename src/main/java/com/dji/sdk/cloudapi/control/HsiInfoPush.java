package com.dji.sdk.cloudapi.control;

import java.util.List;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/29
 */
public class HsiInfoPush {

    private Integer upDistance;

    private Integer downDistance;

    private List<Integer> aroundDistance;

    private Boolean upEnable;

    private Boolean upWork;

    private Boolean downEnable;

    private Boolean downWork;

    private Boolean leftEnable;

    private Boolean leftWork;

    private Boolean rightEnable;

    private Boolean rightWork;

    private Boolean frontEnable;

    private Boolean frontWork;

    private Boolean backEnable;

    private Boolean backWork;

    private Boolean verticalEnable;

    private Boolean verticalWork;

    private Boolean horizontalEnable;

    private Boolean horizontalWork;

    public HsiInfoPush() {
    }

    @Override
    public String toString() {
        return "HsiInfoPush{" +
                "upDistance=" + upDistance +
                ", downDistance=" + downDistance +
                ", aroundDistance=" + aroundDistance +
                ", upEnable=" + upEnable +
                ", upWork=" + upWork +
                ", downEnable=" + downEnable +
                ", downWork=" + downWork +
                ", leftEnable=" + leftEnable +
                ", leftWork=" + leftWork +
                ", rightEnable=" + rightEnable +
                ", rightWork=" + rightWork +
                ", frontEnable=" + frontEnable +
                ", frontWork=" + frontWork +
                ", backEnable=" + backEnable +
                ", backWork=" + backWork +
                ", verticalEnable=" + verticalEnable +
                ", verticalWork=" + verticalWork +
                ", horizontalEnable=" + horizontalEnable +
                ", horizontalWork=" + horizontalWork +
                '}';
    }

    public Integer getUpDistance() {
        return upDistance;
    }

    public HsiInfoPush setUpDistance(Integer upDistance) {
        this.upDistance = upDistance;
        return this;
    }

    public Integer getDownDistance() {
        return downDistance;
    }

    public HsiInfoPush setDownDistance(Integer downDistance) {
        this.downDistance = downDistance;
        return this;
    }

    public List<Integer> getAroundDistance() {
        return aroundDistance;
    }

    public HsiInfoPush setAroundDistance(List<Integer> aroundDistance) {
        this.aroundDistance = aroundDistance;
        return this;
    }

    public Boolean getUpEnable() {
        return upEnable;
    }

    public HsiInfoPush setUpEnable(Boolean upEnable) {
        this.upEnable = upEnable;
        return this;
    }

    public Boolean getUpWork() {
        return upWork;
    }

    public HsiInfoPush setUpWork(Boolean upWork) {
        this.upWork = upWork;
        return this;
    }

    public Boolean getDownEnable() {
        return downEnable;
    }

    public HsiInfoPush setDownEnable(Boolean downEnable) {
        this.downEnable = downEnable;
        return this;
    }

    public Boolean getDownWork() {
        return downWork;
    }

    public HsiInfoPush setDownWork(Boolean downWork) {
        this.downWork = downWork;
        return this;
    }

    public Boolean getLeftEnable() {
        return leftEnable;
    }

    public HsiInfoPush setLeftEnable(Boolean leftEnable) {
        this.leftEnable = leftEnable;
        return this;
    }

    public Boolean getLeftWork() {
        return leftWork;
    }

    public HsiInfoPush setLeftWork(Boolean leftWork) {
        this.leftWork = leftWork;
        return this;
    }

    public Boolean getRightEnable() {
        return rightEnable;
    }

    public HsiInfoPush setRightEnable(Boolean rightEnable) {
        this.rightEnable = rightEnable;
        return this;
    }

    public Boolean getRightWork() {
        return rightWork;
    }

    public HsiInfoPush setRightWork(Boolean rightWork) {
        this.rightWork = rightWork;
        return this;
    }

    public Boolean getFrontEnable() {
        return frontEnable;
    }

    public HsiInfoPush setFrontEnable(Boolean frontEnable) {
        this.frontEnable = frontEnable;
        return this;
    }

    public Boolean getFrontWork() {
        return frontWork;
    }

    public HsiInfoPush setFrontWork(Boolean frontWork) {
        this.frontWork = frontWork;
        return this;
    }

    public Boolean getBackEnable() {
        return backEnable;
    }

    public HsiInfoPush setBackEnable(Boolean backEnable) {
        this.backEnable = backEnable;
        return this;
    }

    public Boolean getBackWork() {
        return backWork;
    }

    public HsiInfoPush setBackWork(Boolean backWork) {
        this.backWork = backWork;
        return this;
    }

    public Boolean getVerticalEnable() {
        return verticalEnable;
    }

    public HsiInfoPush setVerticalEnable(Boolean verticalEnable) {
        this.verticalEnable = verticalEnable;
        return this;
    }

    public Boolean getVerticalWork() {
        return verticalWork;
    }

    public HsiInfoPush setVerticalWork(Boolean verticalWork) {
        this.verticalWork = verticalWork;
        return this;
    }

    public Boolean getHorizontalEnable() {
        return horizontalEnable;
    }

    public HsiInfoPush setHorizontalEnable(Boolean horizontalEnable) {
        this.horizontalEnable = horizontalEnable;
        return this;
    }

    public Boolean getHorizontalWork() {
        return horizontalWork;
    }

    public HsiInfoPush setHorizontalWork(Boolean horizontalWork) {
        this.horizontalWork = horizontalWork;
        return this;
    }
}
