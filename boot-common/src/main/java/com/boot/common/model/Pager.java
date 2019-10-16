package com.boot.common.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页工具类
 *
 * @param <T>
 */
public class Pager<T> {
    private int pageNo;
    private int pageSize;
    private int totalCount;
    private int pageCount;
    private List<T> data;
    private double time;

    public Pager() {
    }

    public Pager(int pageNo, int pageSize) {
        this.pageNo = pageNo > 0 ? pageNo : 1;
        this.pageSize = pageSize > 0 ? pageSize : 10;
    }

    public Pager(int pageNo, int pageSize, int totalCount) {
        this.pageNo = pageNo > 0 ? pageNo : 1;
        this.pageSize = pageSize > 0 ? pageSize : 10;
        this.totalCount = Math.max(0, totalCount);
        this.pageNo = Math.min(getPageCount(), this.pageNo);
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
        this.pageNo = Math.min(this.getPageCount(), this.pageNo);
    }

    public int getPageCount() {
        if (this.pageSize > 0) {
            this.pageCount = (this.totalCount + this.pageSize - 1) / this.pageSize;
        }

        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public <D> Pager<D> copy(Transform<T, D> transform) {

        Pager<D> desc = new Pager<>(this.getPageNo(), this.getPageSize(), this.getTotalCount());

        if (this.getData() != null && this.getData().size() > 0) {
            List<D> data = new ArrayList<>(this.getData().size());
            this.getData().forEach(s -> data.add(transform.convert(s)));
            desc.setData(data);
        }

        return desc;
    }

    @FunctionalInterface
    public static interface Transform<T, D> {
        D convert(T source);
    }
}
