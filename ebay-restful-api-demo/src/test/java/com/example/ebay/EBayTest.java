package com.example.ebay;

import com.ebay.api.client.auth.oauth2.CredentialUtil;
import com.ebay.api.client.auth.oauth2.OAuth2Api;
import com.ebay.api.client.auth.oauth2.model.AccessToken;
import com.ebay.api.client.auth.oauth2.model.Environment;
import com.ebay.api.client.auth.oauth2.model.OAuthResponse;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.springframework.util.ResourceUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
        log.info("code:" + code);
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
     * @throws IOException
     */
    public void refreshToken(String refreshToken) throws IOException {
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
    }

    /**
     * 调用eBay api
     *
     * @param accessToken
     * @return
     * @throws IOException
     */
    public Response getPayOuts(String accessToken) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://apiz.ebay.com/sell/finances/v1/payout")
                .get()
                .addHeader("Authorization", "Bearer " + accessToken)
                .header("limit", "10")
                .header("offset", "0")
                .header("sort", "-payoutDate")
                .header("filter", "payoutStatus:{SUCCEEDED}")
                .build();
        Response response = okHttpClient.newCall(request).execute();
        return response;
    }


    public static void main(String[] args) {
        try {
            String accessToken = "v^1.1#i^1#I^3#f^0...............wVAAA=";
            EBayTest eBayTest = new EBayTest();
            Response response = eBayTest.getPayOuts(accessToken);
            ResponseBody body = response.body();
            System.out.println(body.string());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
