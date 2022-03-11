package com.example.Bookmodule.book.entity;

import lombok.Data;

import java.util.Date;

@Data
public class ViewerRating {
    private int numReviews;
    private double rating;
    private Date lastUpdated;
}
