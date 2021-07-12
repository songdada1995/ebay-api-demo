package com.example.ebay.model.ebay.payout;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author songbo
 * @Date 2021/7/12 10:05
 * @Version 1.0
 */
@NoArgsConstructor
@Data
public class PayoutsEBayBean {
    @SerializedName("payoutId")
    private String payoutId;
    @SerializedName("payoutStatus")
    private String payoutStatus;
    @SerializedName("payoutStatusDescription")
    private String payoutStatusDescription;
    @SerializedName("amount")
    private AmountEBayBean amount;
    @SerializedName("payoutDate")
    private String payoutDate;
    @SerializedName("lastAttemptedPayoutDate")
    private String lastAttemptedPayoutDate;
    @SerializedName("transactionCount")
    private Integer transactionCount;
    @SerializedName("payoutInstrument")
    private PayoutInstrumentEBayBean payoutInstrument;
    @SerializedName("payoutMemo")
    private String payoutMemo;
}
