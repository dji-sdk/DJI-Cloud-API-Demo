package com.dji.sdk.cloudapi.wayline;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/6
 */
public class ExecutableConditions {

    /**
     * Storage capacity
     * The minimum storage capacity of DJI dock or aircraft that can execute a task. Unit: MB.
     * If the storage capacity doesn't satisfy the `storage_capacity`, task execution will fail.
     */
    @NotNull
    @Min(0)
    private Integer storageCapacity;

    public ExecutableConditions() {}

    @Override
    public String toString() {
        return "ExecutableConditions{" +
                "storageCapacity=" + storageCapacity +
                '}';
    }

    public Integer getStorageCapacity() {
        return storageCapacity;
    }

    public ExecutableConditions setStorageCapacity(Integer storageCapacity) {
        this.storageCapacity = storageCapacity;
        return this;
    }
}