package com.coppel.dto.by;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Key {
    @JsonProperty("orgId")
    private String orgId;
    @JsonProperty("sellingChannel")
    private String sellingChannel;
    @JsonProperty("transactionType")
    private String transactionType;
    @JsonProperty("productId")
    private String productId;
    @JsonProperty("uom")
    private String uom;
    @JsonProperty("gtin")
    private String gtin;
    @JsonProperty("segment")
    private String segment;
    @JsonProperty("fulfillmentType")
    private String fulfillmentType;

}
