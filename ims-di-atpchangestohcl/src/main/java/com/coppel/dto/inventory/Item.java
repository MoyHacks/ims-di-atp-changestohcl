package com.coppel.dto.inventory;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@Getter
@Setter
public class Item {

    private String partNumber;
    private Double availableQuantity;
    private int storeLocationId;


    public String toJson(){return new Gson().toJson(this);}
}





