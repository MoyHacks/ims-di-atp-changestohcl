package com.coppel.dto.by;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AvailabilityByFulfillmentType {
    private String fulfillmentType;
    private List<AvailabilityDetails> availabilityDetails;

}
