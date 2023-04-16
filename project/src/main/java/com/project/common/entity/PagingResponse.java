package com.project.common.entity;

import lombok.Data;
import org.springframework.data.domain.Page;

@Data
public class PagingResponse {

    private int currentPage;
    private int totalPages;
    private int pageSize;
    private long totalElements;
    private boolean hasNext;
    private boolean hasPrevious;

    public PagingResponse(Page page) {
        this.currentPage = page.getNumber() + 1;
        this.totalPages = page.getTotalPages();
        this.pageSize = page.getSize();
        this.totalElements = page.getTotalElements();
        this.hasNext = page.hasNext();
        this.hasPrevious = page.hasPrevious();
    }

}
