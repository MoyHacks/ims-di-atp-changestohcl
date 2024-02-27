package com.coppel.dto.inventory;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@Getter
@Setter
public class InventoryResponse {
        @SerializedName("PartNumber")
        private String partNumber;
        private String status;
        public String toJson(){return new Gson().toJson(this);}
}
