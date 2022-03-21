package com.dji.sample.common.model;

import lombok.Data;

import java.util.List;

/**
 * The format of the data response when a paginated display is required.
 * @author sean
 * @version 0.3
 * @date 2021/12/22
 */
@Data
public class PaginationData<T> {

    /**
     * The collection in which the data list is stored.
     */
    private List<T> list;

    private Pagination pagination;

    public PaginationData(List<T> list, Pagination pagination) {
        this.list = list;
        this.pagination = pagination;
    }
}
