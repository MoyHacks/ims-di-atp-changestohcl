package com.coppel.dto.by;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AvailabilityByProduct {
    private String productId;
    private String uom;
    private String gtin;
    private Object launchDate;
    private Object launchDateTime;
    private Object associationType;
    private List<AvailabilityByFulfillmentType> availabilityByFulfillmentTypes;

}
