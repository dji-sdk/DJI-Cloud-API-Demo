package com.dji.sdk.cloudapi.wayline;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 1.7
 * @date 2023/10/25
 */
public class GetWaylineListOrderBy {

    @NotNull
    private OrderByColumnEnum column;

    private boolean desc;

    @JsonCreator
    public GetWaylineListOrderBy(String orderBy) {
        String[] arr = orderBy.split(" ");
        this.column = OrderByColumnEnum.find(arr[0]);
        this.desc = arr.length > 1 && arr[1].contains("desc");
    }

    @Override
    @JsonValue
    public String toString() {
        return column.getColumn() + (desc ? " desc" : " asc");
    }

    public OrderByColumnEnum getColumn() {
        return column;
    }

    public GetWaylineListOrderBy setColumn(OrderByColumnEnum column) {
        this.column = column;
        return this;
    }

    public boolean isDesc() {
        return desc;
    }

    public GetWaylineListOrderBy setDesc(boolean desc) {
        this.desc = desc;
        return this;
    }
}
