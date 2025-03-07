package com.sanshugpt.module.mp;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sanshugpt.module.common.constant.Constant;
import com.sanshugpt.module.common.exception.RenException;
import com.sanshugpt.module.common.redis.RedisUtils;
import com.sanshugpt.module.mp.entity.MpAccount;
import com.sanshugpt.module.mp.pojo.WeixinAuthInfoPojo;
import com.sanshugpt.service.MpAccountService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @Author liang.sha
 * @Date 2024/11/21 9:06
 * @Description: 微信服务
 * @Version 1.0
 */
@Service
public class WeixinService {

    @Resource
    RedisUtils redisUtils;


    @Autowired
    MpAccountService mpAccountService;

    public void sendThankYouMessage(String appid,String openid,String message) {
        String accessToken = getAccessToken(appid);

        // 构造发送消息的JSON
        String messageJson = JSONUtil.createObj()
                .put("touser", openid)
                .put("msgtype", "text")
                .put("text", JSONUtil.createObj().put("content", message))
                .toString();

        // 发送HTTP请求给微信接口
        String url = String.format("https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=%s", accessToken);
        HttpRequest.post(url)
                .header("Content-Type", "application/json")
                .body(messageJson)
                .execute();
    }



    public String getLoginQrCode(String appid, String hash) {
        try {
            // 获取access_token
            String accessToken = getAccessToken(appid);
            // 生成带参数的二维码ticket
            String ticket = createQRCodeTicket(accessToken, hash, 60); //
            // 获取二维码URL
            return "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=" + ticket;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 创建带参数的二维码ticket
     *
     * @param accessToken   接口调用凭证
     * @param sceneStr      自定义参数
     * @param expireSeconds 有效期（秒）
     */
    private static String createQRCodeTicket(String accessToken, String sceneStr, int expireSeconds)
            throws IOException {
        String url = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=" + accessToken;

        // 构造请求参数（临时二维码）
        JSONObject params = new JSONObject();
        params.put("expire_seconds", expireSeconds);
        params.put("action_name", "QR_STR_SCENE");
        JSONObject scene = new JSONObject();
        scene.put("scene_str", sceneStr);
        JSONObject actionInfo = new JSONObject();
        actionInfo.put("scene", scene);
        params.put("action_info", actionInfo);

        String response = HttpRequest.post(url)
                .header("Content-Type", "application/json")
                .body(params.toString())
                .execute().body();
        JSONObject jsonObject = JSONObject.parseObject(response);
        return jsonObject.getString("ticket");
    }



    private MpAccount getWXConfig(String appid) {
        String key = Constant.KEY_WEIXIN_CONFIG_CACHE + "_" + appid + "_" + Constant.DEFAULT_TENANT_CODE;
        if (redisUtils.get(key) != null)
            return (MpAccount) redisUtils.get(key);
        MpAccount configEntity =  mpAccountService.getOne(new LambdaQueryWrapper<MpAccount>().eq(MpAccount::getAppId, appid));
        if (configEntity == null) {
            throw new RuntimeException("未配置微信公众号");
        }
        redisUtils.set(key, configEntity);
        return configEntity;
    }

    private static final String REDIS_KEY = "wechat:access_token";

    /**
     * 获取全局 access_token
     */
    private String getAccessToken(String appid) {
        try {
            // 从缓存中获取 access_token
            String access_token = (String) redisUtils.get(REDIS_KEY);
            if (access_token == null) {
                access_token = refreshAccessToken(appid);
                redisUtils.set(REDIS_KEY, access_token,7000);
            }

            return access_token;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("获取 access_token 失败", e);
        }
    }

    /**
     * 刷新 access_token 并缓存
     */
    /**
     * 微信接口响应结果
     * {
     * "access_token":"ACCESS_TOKEN",
     * "expires_in":7200,
     * "refresh_token":"REFRESH_TOKEN",
     * "openid":"OPENID",
     * "scope":"SCOPE",
     * "unionid": "o6_bmasdasdsad6_2sgVt7hMZOPfL"
     * }
     */
    private String refreshAccessToken(String appid) {
        try {
            String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appid + "&secret=" + getWXConfig(appid).getAppSecret();
            String res = HttpUtil.get(url);
            System.out.println("刷新 access_token 响应: " + res);

            JSONObject jsonObject = JSONObject.parseObject(res);
            String access_token = jsonObject.getString("access_token");
            if (StrUtil.isEmpty(access_token)) {
                throw new RuntimeException("获取 access_token 失败，返回数据：" + res);
            }

            // 缓存 access_token，有效期 7000 秒
            redisUtils.set(REDIS_KEY, access_token, 7000);
            return access_token;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("刷新 access_token 失败", e);
        }
    }


    /**
     * 获取用户信息
     */
    public WeixinAuthInfoPojo code2userInfo(String code, String appid) {

        try {
            // 获取用户特定 access_token
            String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appid + "&secret=" + getWXConfig(appid).getAppSecret() + "&code=" + code + "&grant_type=authorization_code";
            System.out.println("请求用户特定 access_token 的 URL: " + url);
            String res = HttpUtil.get(url);
            System.out.println("服务器返回: " + res);

            JSONObject jsonObject = JSONObject.parseObject(res);
            if (jsonObject.containsKey("errcode")) {
                throw new RenException("微信接口调用失败:" + jsonObject.get("errmsg"));
            }
            String openid = jsonObject.getString("openid");
            String userSpecificAccessToken = jsonObject.getString("access_token");

            if (StrUtil.isEmpty(openid) || StrUtil.isEmpty(userSpecificAccessToken)) {
                throw new RuntimeException("获取 openid 或用户 access_token 失败，返回数据：" + res);
            }

            // 使用用户 access_token 获取用户信息
            url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + userSpecificAccessToken + "&openid=" + openid;
            System.out.println("请求用户信息的 URL: " + url);
            res = HttpUtil.get(url);
            System.out.println("服务器返回: " + res);

            return JSONObject.parseObject(res, WeixinAuthInfoPojo.class);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 连接微信，获取签名等信息
     */
    public Map<String, Object> connectWechat(String appid) {
        try {
            String access_token = getAccessToken(appid);

            System.out.println("使用的 access_token: " + access_token);

            // 获取 ticket
            String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + access_token + "&type=2";
            String res = HttpUtil.get(url);
            System.out.println("获取 ticket 的响应: " + res);

            String ticket = JSONObject.parseObject(res).getString("ticket");
            if (StrUtil.isEmpty(ticket)) {
                throw new RuntimeException("获取 ticket 失败，返回数据：" + res);
            }

            // 生成随机字符串
            StringBuilder str = new StringBuilder();
            for (int i = 0; i < 8; i++) {
                str.append(new Random().nextInt(10));
            }
            String noncestr = str.toString();
            String timeStamp = Long.toString(System.currentTimeMillis()).substring(0, 10);

            // 构造签名
            String string1 = String.format("appid=%s&noncestr=%s&sdk_ticket=%s&timestamp=%s", appid, noncestr, ticket, timeStamp);
            String signature = SecureUtil.sha1(string1);

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("appId", appid);
            responseData.put("scope", "snsapi_userinfo"); // scope 示例
            responseData.put("noncestr", noncestr);
            responseData.put("timestamp", timeStamp);
            responseData.put("signature", signature);

            return responseData;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("连接微信失败", e);
        }
    }
}
