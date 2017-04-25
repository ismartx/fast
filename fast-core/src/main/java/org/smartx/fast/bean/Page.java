package org.smartx.fast.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * API分页
 *
 * @author kext
 * @since 1.0
 */
public class Page implements Serializable {

    private static final int MAX_PAGE_SIZE = 100;

    private static final long serialVersionUID = 7036052298524233604L;

    private int page;

    private int size;

    private int totalPage;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    @JSONField(serialize = false)
    public Integer getOffset() {
        return (page - 1) * size;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        if (size > MAX_PAGE_SIZE) {
            this.size = MAX_PAGE_SIZE;
        } else {
            this.size = size;
        }
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public Page() {
    }

    public Page(int size, int page, Integer totalCount) {

        if (size < 0) {
            throw new IllegalArgumentException("Size can not be less than zero");
        }
        if (page < 1) {
            throw new IllegalArgumentException("Page can not be less than one");
        }
        this.size = size;
        this.page = page;
        if (totalCount <= 0) {
            this.totalPage = 1;
        } else {
            this.totalPage = (totalCount - 1) / this.size + 1;
        }
    }

}
