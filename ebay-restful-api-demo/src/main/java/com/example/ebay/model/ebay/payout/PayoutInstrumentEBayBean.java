package com.example.ebay.model.ebay.payout;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author songbo
 * @Date 2021/7/12 10:06
 * @Version 1.0
 */
@NoArgsConstructor
@Data
public class PayoutInstrumentEBayBean {
    @SerializedName("instrumentType")
    private String instrumentType;
    @SerializedName("nickname")
    private String nickname;
    @SerializedName("accountLastFourDigits")
    private String accountLastFourDigits;
}
