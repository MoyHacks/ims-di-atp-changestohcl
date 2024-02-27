package com.coppel.dto.by;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AvailabilityDetails {
    private String segment;
    private double atp;
    private double supply;
    private double demand;
    private double safetyStock;
    private String atpStatus;
    private List<Object> availabilityByLocations;
    private List<Object> atpDetailsById;
    private List<Object> futureAtpDetailsById;

}
