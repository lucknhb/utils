package com.nhb.pojo;

import java.io.Serializable;
import java.util.List;

public class PageBean<T> implements Serializable {
    private List<T> data;//当前页的数据
    private Integer totalPage;//总页数
    private Integer currentPage;//当前页
    private Integer totalCount;//总记录数
    private Integer pageSize;//每页显示的记录数

    public PageBean(List<T> data, Integer totalPage, Integer currentPage,
                Integer totalCount, Integer pageSize) {
        this.data = data;
        this.totalPage = totalPage;
        this.currentPage = currentPage;
        this.totalCount = totalCount;
        this.pageSize = pageSize;
    }

    public PageBean() {

    }

    public List<T> getData() {

        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public Integer getTotalPage() {
        return (int)Math.ceil(totalCount*1.0/pageSize);
    }


    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
