package com.pricelabs.inquiryservice.core.dto;

import java.util.ArrayList;

public class InquiryResponse {

    private String listingId;

    private String headline;

    private ArrayList<Integer> rentNights;

    public InquiryResponse(String listing_id, String headline, ArrayList<Integer> rentNights) {
        this.listingId = listing_id;
        this.headline = headline;
        this.rentNights = rentNights;
    }

    public InquiryResponse() {

    }

    public String getListing_id() {
        return listingId;
    }

    public void setListing_id(String listing_id) {
        this.listingId = listing_id;
    }

    public String getheadline() {
        return headline;
    }

    public void setheadline(String headline) {
        this.headline = headline;
    }

    public ArrayList<Integer> getrentNights() {
        return rentNights;
    }

    public void setrentNights(ArrayList<Integer> rentNights) {
        this.rentNights = rentNights;
    }

    @Override
    public String toString() {
        return "InquiryResponse{" +
                "listing_id='" + listingId + '\'' +
                ", headline='" + headline + '\'' +
                ", rentNights=" + rentNights +
                '}';
    }
}
