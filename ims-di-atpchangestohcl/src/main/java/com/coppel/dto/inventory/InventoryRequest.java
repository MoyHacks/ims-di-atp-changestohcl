package com.coppel.dto.inventory;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@Getter
@Setter
public class InventoryRequest {
    private List<Item> items;

    public String toJson(){return new Gson().toJson(this);}
}
