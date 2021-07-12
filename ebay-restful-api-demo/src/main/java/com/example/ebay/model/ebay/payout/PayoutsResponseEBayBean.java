package com.example.ebay.model.ebay.payout;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author songbo
 * @Date 2021/7/12 10:05
 * @Version 1.0
 */
@NoArgsConstructor
@Data
public class PayoutsResponseEBayBean {
    @SerializedName("href")
    private String href;
    @SerializedName("limit")
    private Integer limit;
    @SerializedName("offset")
    private Integer offset;
    @SerializedName("payouts")
    private List<PayoutsEBayBean> payouts;
    @SerializedName("total")
    private Integer total;
}
