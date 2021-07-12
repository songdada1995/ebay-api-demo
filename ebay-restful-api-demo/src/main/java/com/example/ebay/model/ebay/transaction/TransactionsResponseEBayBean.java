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
public class TransactionsResponseEBayBean {
    @SerializedName("href")
    private String href;
    @SerializedName("next")
    private String next;
    @SerializedName("limit")
    private Integer limit;
    @SerializedName("offset")
    private Integer offset;
    @SerializedName("transactions")
    private List<TransactionsEBayBean> transactions;
    @SerializedName("total")
    private Integer total;
}
