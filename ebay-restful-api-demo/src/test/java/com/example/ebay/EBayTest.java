package com.example.ebay;

import com.ebay.api.client.auth.oauth2.CredentialUtil;
import com.ebay.api.client.auth.oauth2.OAuth2Api;
import com.ebay.api.client.auth.oauth2.model.AccessToken;
import com.ebay.api.client.auth.oauth2.model.Environment;
import com.ebay.api.client.auth.oauth2.model.OAuthResponse;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;
import org.springframework.util.ResourceUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * @Author songbo
 * @Date 2021/7/5 16:21
 * @Version 1.0
 */
@Slf4j
public class EBayTest {

    static {
        try {
            CredentialUtil.load(new FileInputStream(ResourceUtils.getFile("classpath:ebay-config.yaml")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * eBay授权页面地址
     *
     * @param state
     * @return
     */
    public String getAuthUrl(String state) {
        OAuth2Api oauth2Api = new OAuth2Api();
        List<String> scopeList = new ArrayList<>();
        scopeList.add("https://api.ebay.com/oauth/api_scope");
        scopeList.add("https://api.ebay.com/oauth/api_scope/sell.finances");
        scopeList.add("https://api.ebay.com/oauth/api_scope/sell.fulfillment");
        scopeList.add("https://api.ebay.com/oauth/api_scope/sell.fulfillment.readonly");
        return oauth2Api.generateUserAuthorizationUrl(Environment.PRODUCTION, scopeList, Optional.ofNullable(state));
    }

    /**
     * 程序内打开浏览器
     *
     * @param url
     * @return
     */
    public static boolean openBrowser(String url) {
        if (url == null) return false;
        String[] unixBrowser = new String[]{"google-chrome", "firefox"};
        boolean success = false;
        if (System.getProperty("os.name").toLowerCase().startsWith("win")) {
            try {
                Runtime.getRuntime().exec(
                        new String[]{"rundll32.exe", "url.dll,FileProtocolHandler", url});
                success = true;
            } catch (Exception e) {
            }
        } else {
            for (int i = 0; i < unixBrowser.length; ++i)
                try {
                    Runtime.getRuntime().exec(new String[]{unixBrowser[0], url});
                    success = true;
                    break;
                } catch (Exception e) {
                }
        }
        return success;
    }

    /**
     * 授权回调地址
     * 获取accessToken、refreshToken
     *
     * @return
     * @throws IOException
     */
    public String getToken(String code) throws IOException {
        Assert.notNull(code, "code不能为空");
        OAuth2Api oauth2Api = new OAuth2Api();
        OAuthResponse oAuthResponse = oauth2Api.exchangeCodeForAccessToken(Environment.PRODUCTION, code);
        AccessToken accessToken = oAuthResponse.getAccessToken().get();
        String token = accessToken.getToken();
        log.info("accessToken:" + token);
        Date accessTokenExpiresOn = accessToken.getExpiresOn();
        log.info("accessTokenExpiresOn:" + accessTokenExpiresOn);
        String refreshToken = oAuthResponse.getRefreshToken().get().getToken();
        log.info("refreshToken:" + refreshToken);
        Date refreshTokenExpiresOn = oAuthResponse.getRefreshToken().get().getExpiresOn();
        log.info("refreshTokenExpiresOn:" + refreshTokenExpiresOn);
        return token;
    }

    /**
     * 若access_token过期，刷新access_token
     *
     * @param refreshToken
     * @return
     * @throws IOException
     */
    public String refreshToken(String refreshToken) throws IOException {
        OAuth2Api oauth2Api = new OAuth2Api();
        List<String> scopeList = new ArrayList<>();
        scopeList.add("https://api.ebay.com/oauth/api_scope");
        scopeList.add("https://api.ebay.com/oauth/api_scope/sell.finances");
        scopeList.add("https://api.ebay.com/oauth/api_scope/sell.fulfillment");
        scopeList.add("https://api.ebay.com/oauth/api_scope/sell.fulfillment.readonly");
        OAuthResponse oAuthResponse = oauth2Api.getAccessToken(Environment.PRODUCTION, refreshToken, scopeList);
        AccessToken accessToken = oAuthResponse.getAccessToken().get();
        String token = accessToken.getToken();
        log.info("accessToken:" + token);
        Date accessTokenExpiresOn = accessToken.getExpiresOn();
        log.info("accessTokenExpiresOn:" + accessTokenExpiresOn);
        return token;
    }

    /**
     * 获取支付数据
     * API详情：https://developer.ebay.com/api-docs/sell/finances/resources/payout/methods/getPayout
     *
     * @param accessToken
     * @param offset
     * @param limit
     * @param sort
     * @param filter
     * @return
     * @throws IOException
     */
    public Response getPayOuts(String accessToken, int offset, int limit, String sort, String filter) throws IOException {
        Assert.notNull(accessToken, "accessToken不能为空");
        // 构建请求参数
        HashMap<String, String> params = new HashMap<>();
        params.put("offset", String.valueOf(offset));
        params.put("limit", String.valueOf(limit));
        if (StringUtils.isNotBlank(sort)) {
            params.put("sort", sort);
        }
        if (StringUtils.isNotBlank(filter)) {
            params.put("filter", filter);
        }
        // 构建请求头
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + accessToken);

        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://apiz.ebay.com/sell/finances/v1/payout").newBuilder();
        if (params != null) {
            for (String key : params.keySet()) {
                urlBuilder.setQueryParameter(key, params.get(key));
            }
        }
        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .headers(headers == null ? new Headers.Builder().build() : Headers.of(headers))
                .get()
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        return okHttpClient.newCall(request).execute();
    }

    /**
     * 获取交易数据
     * API详情：https://developer.ebay.com/api-docs/sell/finances/resources/transaction/methods/getTransactions
     *
     * @param accessToken
     * @param offset
     * @param limit
     * @param sort
     * @param filter
     * @return
     * @throws IOException
     */
    public Response getTransactions(String accessToken, int offset, int limit, String sort, String filter) throws IOException {
        Assert.notNull(accessToken, "accessToken不能为空");
        // 构建请求参数
        HashMap<String, String> params = new HashMap<>();
        params.put("offset", String.valueOf(offset));
        params.put("limit", String.valueOf(limit));
        if (StringUtils.isNotBlank(sort)) {
            params.put("sort", sort);
        }
        if (StringUtils.isNotBlank(filter)) {
            params.put("filter", filter);
        }
        // 构建请求头
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + accessToken);

        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://apiz.ebay.com/sell/finances/v1/transaction").newBuilder();
        if (params != null) {
            for (String key : params.keySet()) {
                urlBuilder.setQueryParameter(key, params.get(key));
            }
        }
        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .headers(headers == null ? new Headers.Builder().build() : Headers.of(headers))
                .get()
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        return okHttpClient.newCall(request).execute();
    }

    public static void main(String[] args) {
        try {

//            String code = "v%5E1.1%23i%5E1%......Ml8xI0VeMjYw";
//            EBayTest eBayTest = new EBayTest();
//            String accessToken = eBayTest.getToken(code);
//            System.out.println(accessToken);

//            String refreshToken = "v^1.1#i^1#p^3#f^......SNFXjI2MA==";
//            EBayTest eBayTest = new EBayTest();
//            String accessToken = eBayTest.refreshToken(refreshToken);
//            System.out.println(accessToken);

//            String accessToken = "v^1.1#i^1#r^0#I^3......VM3DJbvLyb8DIIMFQAA";
//            EBayTest eBayTest = new EBayTest();
//            Response response = eBayTest.getPayOuts(accessToken, 0, 20, "-payoutDate", "payoutStatus:{SUCCEEDED}");
//            String bodyString = response.body().string();
//            System.out.println(bodyString);
//            PayoutsResponseEBayBean payoutsResponse = JSONObject.parseObject(bodyString, PayoutsResponseEBayBean.class);
//            System.out.println(payoutsResponse);

//            String accessToken = "v^1.1#i^1#r^0#I^3#f......7XVM3DJbvLyb8DIIMFQAA";
//            EBayTest eBayTest = new EBayTest();
//            Response response = eBayTest.getTransactions(accessToken, 0, 20, "-transactionDate", "transactionType:{SALE}");
//            String bodyString = response.body().string();
//            System.out.println(bodyString);
//            TransactionsResponseEBayBean transactionsResponse = JSONObject.parseObject(bodyString, TransactionsResponseEBayBean.class);
//            System.out.println(transactionsResponse);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
