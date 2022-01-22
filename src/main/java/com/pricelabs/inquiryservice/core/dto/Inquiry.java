package com.pricelabs.inquiryservice.core.dto;

public class Inquiry {

    private int pageSize;
    private String address;

    public Inquiry(int pageSize, String address) {
        this.pageSize = pageSize;
        this.address = address;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Inquiry{" +
                "pageSize=" + pageSize +
                ", address='" + address + '\'' +
                '}';
    }
}
