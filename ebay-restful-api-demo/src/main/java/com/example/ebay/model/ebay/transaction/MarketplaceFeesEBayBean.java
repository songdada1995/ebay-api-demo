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
public class MarketplaceFeesEBayBean {
    @SerializedName("feeType")
    private String feeType;
    @SerializedName("amount")
    private AmountEBayBean amount;
    @SerializedName("feeMemo")
    private String feeMemo;
}
