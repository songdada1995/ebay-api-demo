package com.example.ebay.model.ebay.transaction;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author songbo
 * @Date 2021/7/12 11:06
 * @Version 1.0
 */
@NoArgsConstructor
@Data
public class BuyerEBayBean {
    @SerializedName("username")
    private String username;
}
