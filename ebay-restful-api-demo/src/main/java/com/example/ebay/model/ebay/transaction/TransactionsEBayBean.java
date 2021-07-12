package com.example.ebay.model.ebay.transaction;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author songbo
 * @Date 2021/7/12 11:06
 * @Version 1.0
 */
@NoArgsConstructor
@Data
public class TransactionsEBayBean {
    @SerializedName("transactionId")
    private String transactionId;
    @SerializedName("orderId")
    private String orderId;
    @SerializedName("salesRecordReference")
    private String salesRecordReference;
    @SerializedName("buyer")
    private BuyerEBayBean buyer;
    @SerializedName("transactionType")
    private String transactionType;
    @SerializedName("amount")
    private AmountEBayBean amount;
    @SerializedName("totalFeeBasisAmount")
    private AmountEBayBean totalFeeBasisAmount;
    @SerializedName("totalFeeAmount")
    private AmountEBayBean totalFeeAmount;
    @SerializedName("orderLineItems")
    private List<OrderLineItemsEBayBean> orderLineItems;
    @SerializedName("bookingEntry")
    private String bookingEntry;
    @SerializedName("transactionDate")
    private String transactionDate;
    @SerializedName("transactionStatus")
    private String transactionStatus;
    @SerializedName("paymentsEntity")
    private String paymentsEntity;
}
