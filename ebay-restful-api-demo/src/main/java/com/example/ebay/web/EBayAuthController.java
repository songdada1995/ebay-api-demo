package com.example.ebay.web;

import com.ebay.api.client.auth.oauth2.OAuth2Api;
import com.ebay.api.client.auth.oauth2.model.AccessToken;
import com.ebay.api.client.auth.oauth2.model.Environment;
import com.ebay.api.client.auth.oauth2.model.OAuthResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @Author songbo
 * @Date 2021/7/8 15:49
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/ebay")
public class EBayAuthController {

    @Autowired
    @Qualifier(value = "ebayEnvironment")
    private Environment ebayEnvironment;

    /**
     * 用户授权地址
     *
     * @param state 自定义参数，可空
     * @return
     */
    @GetMapping("/auth/url")
    public String getAuthUrl(@RequestParam(value = "state", required = false) String state) {
        OAuth2Api oauth2Api = new OAuth2Api();
        List<String> scopeList = new ArrayList<>();
        scopeList.add("https://api.ebay.com/oauth/api_scope");
        scopeList.add("https://api.ebay.com/oauth/api_scope/sell.finances");
        scopeList.add("https://api.ebay.com/oauth/api_scope/sell.fulfillment");
        scopeList.add("https://api.ebay.com/oauth/api_scope/sell.fulfillment.readonly");
        return oauth2Api.generateUserAuthorizationUrl(ebayEnvironment, scopeList, Optional.ofNullable(state));
    }

    /**
     * 授权回调地址
     * 获取accessToken、refreshToken
     *
     * @param code      授权code，用来获取token
     * @param expiresIn 过期时间
     * @param state     自定义参数，可空
     * @return
     * @throws IOException
     */
    @GetMapping("/auth/token")
    public OAuthResponse getToken(@RequestParam(value = "code") String code,
                                  @RequestParam(value = "expires_in") int expiresIn,
                                  @RequestParam(value = "state", required = false) String state) throws IOException {
        log.info("code:" + code);
        log.info("expiresIn:" + expiresIn);
        log.info("state:" + state);
        OAuth2Api oauth2Api = new OAuth2Api();
        OAuthResponse oAuthResponse = oauth2Api.exchangeCodeForAccessToken(ebayEnvironment, code);
        AccessToken accessToken = oAuthResponse.getAccessToken().get();
        String token = accessToken.getToken();
        log.info("accessToken:" + token);
        Date accessTokenExpiresOn = accessToken.getExpiresOn();
        log.info("accessTokenExpiresOn:" + accessTokenExpiresOn);
        String refreshToken = oAuthResponse.getRefreshToken().get().getToken();
        log.info("refreshToken:" + refreshToken);
        Date refreshTokenExpiresOn = oAuthResponse.getRefreshToken().get().getExpiresOn();
        log.info("refreshTokenExpiresOn:" + refreshTokenExpiresOn);
        return oAuthResponse;
    }

    /**
     * 若access_token过期，刷新access_token
     *
     * @param refreshToken
     * @return
     * @throws IOException
     */
    @GetMapping("/auth/refresh_token")
    public OAuthResponse refreshToken(@RequestParam(value = "refresh_token") String refreshToken) throws IOException {
        OAuth2Api oauth2Api = new OAuth2Api();
        List<String> scopeList = new ArrayList<>();
        scopeList.add("https://api.ebay.com/oauth/api_scope");
        scopeList.add("https://api.ebay.com/oauth/api_scope/sell.finances");
        scopeList.add("https://api.ebay.com/oauth/api_scope/sell.fulfillment");
        scopeList.add("https://api.ebay.com/oauth/api_scope/sell.fulfillment.readonly");
        OAuthResponse oAuthResponse = oauth2Api.getAccessToken(ebayEnvironment, refreshToken, scopeList);
        AccessToken accessToken = oAuthResponse.getAccessToken().get();
        String token = accessToken.getToken();
        log.info("accessToken:" + token);
        Date accessTokenExpiresOn = accessToken.getExpiresOn();
        log.info("accessTokenExpiresOn:" + accessTokenExpiresOn);
        return oAuthResponse;
    }

}
