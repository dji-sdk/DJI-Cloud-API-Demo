package com.dji.sample.common.model;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

/**
 * Used for paging display in the wayline. These field names cannot be changed.
 * Because they need to be the same as the pilot.
 * @author sean
 * @version 0.3
 * @date 2021/12/22
 */
@Data
public class Pagination {

    /**
     * The current page number.
     */
    private long page;

    /**
     * The amount of data displayed per page.
     */
    private long pageSize;

    /**
     * The total amount of all data.
     */
    private long total;

    public Pagination(Page page) {
        this.page = page.getCurrent();
        this.pageSize = page.getSize();
        this.total = page.getTotal();
    }
}
