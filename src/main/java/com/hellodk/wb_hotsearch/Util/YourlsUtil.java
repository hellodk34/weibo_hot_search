package com.hellodk.wb_hotsearch.Util;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.XmlUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.HashMap;
import java.util.Map;

/**
 * yourls util
 */
public class YourlsUtil {

    // yourls 的 api 基础 url
    private static final String YOURLS_API_BASE = "https://yourls.example.com/yourls-api.php";

    // 签名字符串，即 token
    private static final String SIGNATURE = "YOUR_SIGNATURE";

    // 动作名称， shorturl 表示将传入的一个长链接进行短化并返回短链接
    private static final String ACTION = "shorturl";

    public static String getYourlsUrl(String targetUrl) {
        String api = YOURLS_API_BASE;
        Map form = new HashMap<>();
        form.put("signature", SIGNATURE);
        form.put("action", ACTION);
        form.put("url", targetUrl);
        HttpResponse resp = HttpRequest.post(api)
                .form(form)
                .timeout(40000)
                .execute();
        boolean ok = resp.isOk();
        String body = resp.body();
        Document document = XmlUtil.parseXml(body);
        Element root = document.getDocumentElement();
        Document owner = root.getOwnerDocument();
        Node status = owner.getElementsByTagName("status").item(0);
        // 一般情况下正常请求就是成功短化了链接
        if (ok) {
            if (ObjectUtil.isNotEmpty(owner)) {
                // 长链接在库中不存在，首次添加成功 status 返回了 success
                if (ObjectUtil.isNotEmpty(status) && "success".equals(status.getFirstChild().getNodeValue())) {
                    Node shorturl = owner.getElementsByTagName("shorturl").item(0);
                    String shortenedUrl = shorturl.getFirstChild().getNodeValue();
                    return shortenedUrl;
                }
            }
        }
        else {
            if (ObjectUtil.isNotEmpty(owner)) {
                if (ObjectUtil.isNotEmpty(status) && "fail".equals(status.getFirstChild().getNodeValue())) {
                    Node shorturl = owner.getElementsByTagName("shorturl").item(0);
                    String originalUrl = shorturl.getFirstChild().getNodeValue();
                    return originalUrl;
                }
            }
        }
        // 如果请求失败了，有可能是域名识别错误或者 SSL 证书错误，比如遇到了 UnknownHostException 直接返回 https://yourls.example.com/
        return "https://yourls.example.com/";
    }
}
