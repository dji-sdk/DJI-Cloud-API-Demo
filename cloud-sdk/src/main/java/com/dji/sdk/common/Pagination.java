package com.dji.sdk.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Used for paging display. These field names cannot be changed.
 * Because they need to be the same as the pilot.
 * @author sean
 * @version 0.3
 * @date 2021/12/22
 */
@Schema(description = "Used for paging display")
public class Pagination {

    /**
     * The current page number.
     */
    @Schema(description = "The current page number.", example = "1")
    private long page;

    /**
     * The amount of data displayed per page.
     */
    @Schema(description = "The amount of data displayed per page.", example = "10")
    @JsonProperty("page_size")
    private long pageSize;

    /**
     * The total amount of all data.
     */
    @Schema(description = "The total amount of all data.", example = "10")
    private long total;

    public Pagination() {
    }

    public Pagination(long page, long pageSize, long total) {
        this.page = page;
        this.pageSize = pageSize;
        this.total = total;
    }

    @Override
    public String toString() {
        return "Pagination{" +
                "page=" + page +
                ", pageSize=" + pageSize +
                ", total=" + total +
                '}';
    }

    public long getPage() {
        return page;
    }

    public Pagination setPage(long page) {
        this.page = page;
        return this;
    }

    public long getPageSize() {
        return pageSize;
    }

    public Pagination setPageSize(long pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public long getTotal() {
        return total;
    }

    public Pagination setTotal(long total) {
        this.total = total;
        return this;
    }
}
