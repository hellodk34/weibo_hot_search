package com.hellodk.wb_hotsearch.Service;

import cn.hutool.core.map.MapUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.hellodk.wb_hotsearch.Bean.Post;
import com.hellodk.wb_hotsearch.Bean.Result;
import com.hellodk.wb_hotsearch.Mapper.PostMapper;
import com.hellodk.wb_hotsearch.Util.NumbersDict;
import com.hellodk.wb_hotsearch.Util.YourlsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: hellodk
 * @description Main Service
 * @date: 10/18/2021 4:30 PM
 */

//@Component
@Service
public class MainService {

    @Autowired
    private PostMapper postMapper;

    private static JSONArray HOT_SEARCH_LIST;

    private static JSONArray NJ_HOT_SEARCH_LIST;

    private static String urlString = "https://api.telegram.org/bot%s/sendMessage";

    // replace with your personal bot chat id, the given one is fake
    private static final Integer PERSONAL_CHAT_ID = 123456;

    // replace with your channel chat id, the given one is fake
    private static final Long CHANNEL_CHAT_ID = -12345678L;

    // replace with your token, the given one is fake
    private static final String TG_TOKEN = "1234:ABCDEFG";

    static {
        String token = TG_TOKEN;
        urlString = String.format(urlString, token);
    }

    private static String SEARCH = "search_";

    private static String RANKING = "ranking_";

    private static final String apiUrl = "https://m.weibo.cn/api/container/getIndex?containerid=106003type%3D25%26t%3D3%26disable_hot%3D1%26filter_type%3Drealtimehot";

    private static final String njApiUrl = "https://m.weibo.cn/api/container/getIndex?gsid=_2A25MfHFWDeRxGeRI4lEW8SfIzjyIHXVtKIOerDV6PUJbgdCOLVHakWpNUs6fGkBZimYWa_d-o_oczkp5q06cv6K1&wm=3333_2001&launchid=10000365--x&b=0&from=10B5393010&c=iphone&v_p=89&skin=default&v_f=1&s=333c1b91&lang=zh_CN&sflag=1&ua=iPhone13,2__weibo__11.5.3__iphone__os15.1&ft=0&aid=01A_qwb9ZGtg44Tw1vQBVrWG9TETEc45zv5nniZ8WQl7kLf2s.&card25_new_style=1&client_key=1f010e1d88debcfa7ba76846859b9d4f&card159164_emoji_enable=0&extparam=seat%253D1%2526filter_type%253Drealtimehot%2526dgr%253D0%2526refresh_type%253D0%2526lat%253D32.11228073890235%2526pos%253D0_0%2526lon%253D118.8067305368011%2526mi_cid%253D100103%2526cate%253D10103%2526position%253D%25257B%252522objectid%252522%25253A%2525228008632010000000000%252522%25252C%252522name%252522%25253A%252522%25255Cu5357%25255Cu4eac%252522%25257D%2526c_type%253D30%2526display_time%253D1635352723%2526pre_seqid%253D2012387860&refresh_type=0&uicode=10000011&show_layer=1&count=20&fid=106003type%3D25%26t%3D3%26disable_hot%3D1%26filter_type%3Dregion&lon=118.8067305368011&image_type=heif&page_interrupt_enable=1&moduleID=pagecard&new_topic_header_recommend=1&need_head_cards=0&need_new_pop=1&containerid=106003type%3D25%26t%3D3%26disable_hot%3D1%26filter_type%3Dregion&luicode=10000010&open_searchall_164card=1&orifid=231619&open_searchall_174card=1&oriuicode=10000010&location_accuracy=1&no_location_permission=0&lat=32.11228073890235&page=1&new_comment_171card=1&lfid=231619&st_bottom_bar_new_style_enable=1&new_topic_header=1&ul_sid=C0DBB307-67AB-4E70-87CB-03251CB8076F&ul_hid=3D4953F5-3E59-4F61-9F80-4C3C2488898D&ul_ctime=1635352746022";


    //    /**
//     * @param * @param :
//     * @return com.hellodk.wb_hotsearch.Bean.Result
//     * @author hellodk
//     * @description 往数据库新增数据
//     * 每小时 09分 30秒执行，24小时均会执行
//     * @date 10/19/2021 1:48 PM
//     */
//    @Scheduled(cron = "30 9 * * * ?")
//    @Async(value = "asyncPoolTaskExecutor")
    public Result addData() {
        generateJSONArray();
        Result httpResult = new Result();
        httpResult.setSuccess(false);
        httpResult.setMsg("");
        httpResult.setDetail(null);

        Post post = new Post();
        post.setPostTime(getNowTimeStr());
        post.setContent(buildDatabaseContent(HOT_SEARCH_LIST));
        post.setCreatedTime(new Date());
        post.setPostTitle(getNowTime().concat("的微博热🔥️搜"));
        try {
            int num = postMapper.insert(post);
            if (num > 0) {
                httpResult.setSuccess(true);
                httpResult.setMsg("insert success");
                httpResult.setDetail(post);
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
        return httpResult;
    }


    public static void generateJSONArray() {
        HttpResponse response = HttpRequest.get(apiUrl)
                .timeout(30000)
                .execute();
        String result = response.body();
        JSONObject jsonObject = JSONObject.parseObject(result);
        Integer ok = jsonObject.getInteger("ok");
        if (ok != 1) {
            return;
        }
        // 实时热搜 json array
        HOT_SEARCH_LIST = jsonObject.getJSONObject("data").getJSONArray("cards").getJSONObject(0).getJSONArray("card_group");
        // 最原始的 for 循环往往效率最高
        for (int i = 0; i < HOT_SEARCH_LIST.size(); i++) {
            JSONObject item = HOT_SEARCH_LIST.getJSONObject(i);
            String scheme = item.getString("scheme");
            String newScheme = scheme.split("&")[0];
            item.put("scheme", YourlsUtil.getYourlsUrl(newScheme));
        }
    }

    @Scheduled(cron = "0 0 8,12,19 * * ?")
    @Async(value = "asyncPoolTaskExecutor")
    public void pushNjHotSearchToTelegram() {
        generateNjJSONArray();
        Post post = new Post();
        post.setPostTime(getNowTimeStr());
        post.setContent(NJ_HOT_SEARCH_LIST.toJSONString());
        post.setCreatedTime(new Date());
        post.setCity("nanjing");
        post.setPostTitle(getNowTime().concat("的南京同城热🔥️搜"));
        try {
            int num = postMapper.insert(post);
            if (num > 0) {
                String pushContent = buildTelegramNjPushContent(NJ_HOT_SEARCH_LIST);
                Map<String, Object> map = new HashMap<>();
                map.put("chat_id", PERSONAL_CHAT_ID);
                map.put("text", pushContent);
                sendPostReq(buildMap(map));
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void generateNjJSONArray() {
        HttpResponse response = HttpRequest.get(njApiUrl)
                .timeout(30000)
                .execute();
        String result = response.body();
        JSONObject jsonObject = JSONObject.parseObject(result);
        Integer ok = jsonObject.getInteger("ok");
        if (ok != 1) {
            return;
        }
        // 南京同城本地热搜 json array
        NJ_HOT_SEARCH_LIST = jsonObject.getJSONObject("data").getJSONArray("cards").getJSONObject(1).getJSONArray("card_group");

        // 最原始的 for 循环往往效率最高
        for (int i = 0; i < NJ_HOT_SEARCH_LIST.size(); i++) {
            JSONObject item = NJ_HOT_SEARCH_LIST.getJSONObject(i);
            String scheme = item.getString("scheme");
            String newScheme = scheme.split("&")[0];
            item.put("scheme", YourlsUtil.getYourlsUrl(newScheme));
        }
    }

    /**
     * @param * @param :
     * @return void
     * @author hellodk
     * @description 推送到 Telegram 6点到23点 每小时 10分推送
     * @date 10/19/2021 1:49 PM
     */
    @Scheduled(cron = "0 10 6-23/1 * * ?")
    @Async(value = "asyncPoolTaskExecutor")
    public void pushToTelegram() {
        generateJSONArray();

        Post post = new Post();
        post.setPostTime(getNowTimeStr());
        post.setContent(buildDatabaseContent(HOT_SEARCH_LIST));
        post.setCreatedTime(new Date());
        post.setPostTitle(getNowTime().concat("的微博热🔥️搜"));
        try {
            int num = postMapper.insert(post);
            if (num > 0) {
                String pushContent = buildTelegramPushContent(HOT_SEARCH_LIST);
                Map<String, Object> map = new HashMap<>();
                map.put("chat_id", CHANNEL_CHAT_ID);
                map.put("text", pushContent);
                Map<String, Object> resultMap = buildMap(map);
                sendPostReq(resultMap);
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    public Map buildMap(Map map) {
        /**
         * parse_mode 取值有
         * 1. Markdown
         * 2. HTML
         * 3. MarkdownV2
         *
         * 经过测试 MarkdownV2 没有生效。Markdown 可用，但是支持的标签很有限
         */
        map.put("parse_mode", "Markdown");
        map.put("disable_web_page_preview", true);
        return map;
    }

    public Result getContent(Map map) {
        Result result = new Result();
        result.setSuccess(false);
        result.setMsg("");
        result.setDetail(null);
        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
        String postTime = MapUtil.getStr(map, "time");
        String city = MapUtil.getStr(map, "city");
        queryWrapper.eq("post_time", postTime);
        if (city == null) {
            queryWrapper.isNull("city");
        }
        else if (StringUtils.isNotBlank(city)) {
            queryWrapper.eq("city", city);
        }
        queryWrapper.orderByDesc("created_time");
        List<Post> list = postMapper.selectList(queryWrapper);
        if (list != null && list.size() > 0) {
            result.setMsg("query success");
            result.setSuccess(true);
            // created_time 按照升序排序 取第一个元素 始终获取“最新推送”
            result.setDetail(list.get(0));
        }
        else {
            result.setMsg("no data");
        }
        return result;
    }

    private static String buildDatabaseContent(JSONArray hotSearchList) {
        JSONArray resultArray = new JSONArray();
        for (int i = 0; i < hotSearchList.size(); i++) {
            JSONObject item = hotSearchList.getJSONObject(i);
            String pic = item.getString("pic");
            if (pic.contains(SEARCH)) {
                int end = pic.lastIndexOf(".");
                int start = pic.lastIndexOf(SEARCH);
                String number = pic.substring(start + 7, end);
                try {
                    Integer.valueOf(number);
                }
                catch (NumberFormatException e) {
                    continue;
                }
                resultArray.add(item);
            }
        }
        return resultArray.toJSONString();
    }

    private String buildTelegramPushContent(JSONArray hotSearchList) {
        StringBuilder sb = new StringBuilder("*" + getNowTime());
        sb.append("的微博热🔥️搜* [查看更多](")
                .append("https://weibo.hellodk.com/get?time=")
                .append(getNowTimeStr())
                .append(")")
                .append("\r\n\r\n");
        for (int i = 0; i < hotSearchList.size(); i++) {
            JSONObject item = hotSearchList.getJSONObject(i);
            String pic = item.getString("pic");

            if (pic.contains(SEARCH)) {
                int end = pic.lastIndexOf(".");
                int start = pic.lastIndexOf(SEARCH);
                String number = pic.substring(start + 7, end);
                Integer num;
                try {
                    num = Integer.valueOf(number);
                }
                catch (NumberFormatException e) {
                    continue;
                }
                String str;
                if (num < 10) {
                    str = "0".concat(String.valueOf(num));
                }
                else {
                    str = String.valueOf(num);
                }
                char[] chars = str.toCharArray();
                for (int j = 0; j < chars.length; j++) {
                    Integer single = Integer.parseInt(String.valueOf(chars[j]));
                    sb.append(NumbersDict.getSpecificEmoji(single));
                }
            }
            sb.append(" [")
                    .append(item.getString("desc"))
                    .append("](")
                    .append(item.getString("scheme"))
                    .append(") | ")
                    .append(item.getString("desc_extr"))
                    .append("\r\n");
        }
        return sb.toString();
    }


    private String buildTelegramNjPushContent(JSONArray njHotSearchList) {
        StringBuilder sb = new StringBuilder("*" + getNowTime());
        sb.append("的南京同城热🔥️搜* [查看更多](")
                .append("https://weibo.hellodk.com/get?city=nanjing&time=")
                .append(getNowTimeStr())
                .append(")")
                .append("\r\n\r\n");
        for (int i = 0; i < njHotSearchList.size(); i++) {
            JSONObject item = njHotSearchList.getJSONObject(i);
            String pic = item.getString("pic");
            if (pic.contains(RANKING)) {
                int end = pic.lastIndexOf(".");
                int start = pic.lastIndexOf(RANKING);
                String number = pic.substring(start + 8, end);
                Integer num;
                try {
                    num = Integer.valueOf(number);
                }
                catch (NumberFormatException e) {
                    continue;
                }
                String str;
                if (num < 10) {
                    str = "0".concat(String.valueOf(num));
                }
                else {
                    str = String.valueOf(num);
                }
                char[] chars = str.toCharArray();
                for (int j = 0; j < chars.length; j++) {
                    Integer single = Integer.parseInt(String.valueOf(chars[j]));
                    sb.append(NumbersDict.getSpecificEmoji(single));
                }
            }
            sb.append(" [")
                    .append(item.getString("title_sub"))
                    .append("](")
                    .append(item.getString("scheme"))
                    .append(") | ")
                    .append(item.getString("desc"))
                    .append("\r\n");
        }
        return sb.toString();
    }

    private static Boolean sendPostReq(Map map) {
        String response = HttpRequest.post(urlString)
                .form(map)
                .timeout(60000) // 设置请求 60s 超时
                .execute()
                .body();
        JSONObject responseJSON = JSONObject.parseObject(response);
        if (responseJSON.getBoolean("ok").equals(false)) {
            StringBuilder newPost = new StringBuilder();
            newPost.append("Pushing failed. It maybe caused by too much nodes matched or too much content in some topic/post, which exceeds the telegram API request limit.\r\n\r\n")
                    .append("See API document: https://core.telegram.org/bots/api \r\n\r\n")
                    .append("Now push the version without content field.");
            map.put("text", newPost.toString());
            sendPostReq(map);
            return false;
        }
        else {
            return true;
        }
    }

    private static String getNowTime() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy 年 MM 月 dd 日 HH 时");
        return sdf.format(date);
    }

    private static String getNowTimeStr() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH");
        return sdf.format(date);
    }
}
