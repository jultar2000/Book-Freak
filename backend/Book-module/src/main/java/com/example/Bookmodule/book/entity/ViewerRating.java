package com.example.Bookmodule.book.entity;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class ViewerRating {
    private int numReviews;
    private double rating;
    private Date lastUpdated;
}