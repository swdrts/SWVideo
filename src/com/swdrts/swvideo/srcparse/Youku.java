package com.swdrts.swvideo.srcparse;

import android.text.TextUtils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.swdrts.swvideo.utils.SLog;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Youku {

    public static void getVideoRealSrc(String url, final VideoSrcCallback callback){
        if (TextUtils.isEmpty(url)) {
            callback.onFailed("url is empty");
            return;
        }
        String vid = getVid(url);
        String requestUrl = "http://v.youku.com/player/getPlayList/VideoIDS/" + vid + "/Pf/4/ctype/12/ev/1";
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(requestUrl, new TextHttpResponseHandler() {
            
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                // TODO Auto-generated method stub
                SLog.debug("onSuccess--statusCode--"+statusCode+"--responseString--"+responseString);
                String playUrl = getPlayUrl(responseString, false, 2);
                if (!TextUtils.isEmpty(playUrl)) {
                    callback.onFinish(playUrl);
                } else {
                    callback.onFailed("playurl is empty");
                }
            }
            
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString,
                    Throwable throwable) {
                // TODO Auto-generated method stub
                SLog.error("onFailure--statusCode--"+statusCode+"--responseString--"+responseString);
                callback.onFailed(responseString);
            }
        });
    }
    /**
     * 能否播放m3u8视频，由于目前youku m3u8视频源只能播放40s，故暂时默认为false
     * @param json 通过http://v.youku.com/player/getPlayList/VideoIDS/XODEyMzA2NjQ0/Pf/4/ctype/12/ev/1获取的json串
     * @param canPlayM3U8 是否能播放m3u8
     * @param type 0，标清；1，高清；2，超清  只有当canPlayM3U8为true时才有效
     * @return
     */
    private static String getPlayUrl(String json, boolean canPlayM3U8, int type){
        
        
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject data = jsonObject.getJSONArray("data").getJSONObject(0);
            String mk_a3 = "b4et";
            String mk_a4 = "boa4";
            String userCache_a1 = "4";
            String userCache_a2 = "1";
            String param = E(F(mk_a3 + "o0b" + userCache_a1, new int[]{19, 1, 4, 7, 30, 14, 28, 8, 24, 17, 6, 35, 34, 16, 9, 10, 13, 22, 32, 29, 31, 21, 18, 3, 2, 23, 25, 27, 11, 20, 5, 15, 12, 0, 33, 26}).toString(), na(data.getString("ep")));
            String[] c = param.split("_");
            String sid = c[0];
            String token = c[1];
            if (data.getDouble("seconds") <= 40 *60) { //目前只有小于40分钟的视频支持m3u8
                canPlayM3U8 = true;
            }
            if (canPlayM3U8) {
                String id = data.getString("videoid");
                String ep = URLEncoder.encode(D(E(F(mk_a4 + "poz" + userCache_a2, new int[]{19, 1, 4, 7, 30, 14, 28, 8, 24, 17, 6, 35, 34, 16, 9, 10, 13, 22, 32, 29, 31, 21, 18, 3, 2, 23, 25, 27, 11, 20, 5, 15, 12, 0, 33, 26}).toString(), sid + "_" + id + "_" + token)), "UTF-8");
                String oip = data.getString("ip");
                String src = "";
                switch (type) {
                case 0://标清
                    src = "http://pl.youku.com/playlist/m3u8?vid=" + id + "&type=flv&ctype=12&keyframe=1&ep=" + ep + "&sid=" + sid + "&token=" + token + "&ev=1&oip=" + oip;
                    break;
                case 1://高清
                    src = "http://pl.youku.com/playlist/m3u8?vid=" + id + "&type=mp4&ctype=12&keyframe=1&ep=" + ep + "&sid=" + sid + "&token=" + token + "&ev=1&oip=" + oip;
                    break;
                case 2://超清
                    src = "http://pl.youku.com/playlist/m3u8?vid=" + id + "&type=hd2&ctype=12&keyframe=1&ep=" + ep + "&sid=" + sid + "&token=" + token + "&ev=1&oip=" + oip;
                    break;

                default:
                    break;
                }
                return src;
            } else {
                Map<String, String[]> t = T(data);
                return t.get("3gphd")[0];
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 获取优酷视频资源vid
     * @param url
     * @return
     */
    private static String getVid(String url)
    {
        String strRegex = "(?<=id_)(\\w+)";
        Pattern pattern = Pattern.compile(strRegex);
        Matcher match = pattern.matcher(url);
        if (match.find()) {
            return match.group();
        } else {
            return null;
        }
    }
    
    private static Map<String, String[]> T(JSONObject a){
        try {
            Map<String, String[]> videoSrcMap = new HashMap<String, String[]>();
            JSONArray videoTypes = a.getJSONObject("segs").names();
            for (int i = 0; i < videoTypes.length(); i++) {
                String type = videoTypes.getString(i);
                JSONArray segsJsonArray = a.getJSONObject("segs").getJSONArray(type);
                String[] videoSegs = new String[segsJsonArray.length()];
                for (int j = 0; j < segsJsonArray.length(); j++) {
                    JSONObject segObject = segsJsonArray.getJSONObject(j);
                    String fileId = getFileID(a.getJSONObject("streamfileids").getString(type), a.getDouble("seed"));
                    String mk_a3 = "b4et";
                    String mk_a4 = "boa4";
                    String userCache_a1 = "4";
                    String userCache_a2 = "1";
                    String na = na(a.getString("ep"));
                    int[] c = new int[] {19, 1, 4, 7, 30, 14, 28, 8, 24, 17, 6, 35, 34, 16, 9, 10, 13, 22, 32, 29, 31, 21, 18, 3, 2, 23, 25, 27, 11, 20, 5, 15, 12, 0, 33, 26};
                    String F = F(mk_a3 + "o0b" + userCache_a1, c);
                    String E = E(F, na);
                    String[] sidToken = E.split("_");
                    String sid = sidToken[0];
                    String token = sidToken[1];
                    videoSegs[j] = getVideoSrc(mk_a4, userCache_a2, sid, token, segObject.getInt("no"), a, type, fileId, "", "");
                }
                videoSrcMap.put(type, videoSegs);
            }
            
            return videoSrcMap;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        
        return null;
    }
    
    private static String getVideoSrc(String mk_a4, String userCache_a2, String sid,
            String token, int num, JSONObject c, String d, String f, String i,
            String g) {
        try {
            if (TextUtils.isEmpty(c.getString("videoid"))
                    || TextUtils.isEmpty(d)) {
                return "";
            }
            String h = new JSONObject(
                    "{flv: 0,flvhd: 0,mp4: 1,hd2: 2,\"3gphd\": 1,\"3gp\": 0}")
                    .getString(d);
            String q = new JSONObject(
                    "{flv: \"flv\",mp4: \"mp4\",hd2: \"flv\",\"3gphd\": \"mp4\",\"3gp\": \"flv\"}")
                    .getString(d);
            String k = Integer.toHexString(num);
            if (1 == k.length()) {
                k = "0" + k;
            }
            String l = c.getJSONObject("segs").getJSONArray(d)
                    .getJSONObject(num).getString("seconds");
            String a = c.getJSONObject("segs").getJSONArray(d)
                    .getJSONObject(num).getString("k");
            if (TextUtils.isEmpty(a))
                a = c.getString("key2") + c.getString("key1");
            d = "";
            if (c.has("show") && null != c.getJSONObject("show")) {
                if (c.getJSONObject("show").getInt("show_paid") == 0) {
                    d = "&ymovie=1";
                } else {
                    d = "&ypremium=1";
                }
            }
            String url = "/player/getFlvPath/sid/" + sid + "_" + k + "/st/" + q
                    + "/fileid/" + f + "?K=" + a + "&hd=" + h + "&myp=0&ts="
                    + l + "&ypp=0" + d;
            f = URLEncoder.encode(
                    D(E(F(
                            mk_a4 + "poz" + userCache_a2,
                            new int[] { 19, 1, 4, 7, 30, 14, 28, 8, 24, 17, 6,
                                    35, 34, 16, 9, 10, 13, 22, 32, 29, 31, 21,
                                    18, 3, 2, 23, 25, 27, 11, 20, 5, 15, 12, 0,
                                    33, 26 }).toString(), sid + "_" + f + "_"
                            + token)), "UTF-8");
            url = url + ("&ep=" + f) + "&ctype=12&ev=1" + ("&token=" + token);
            url += "&oip=" + c.getString("ip");
            return "http://k.youku.com"
                    + (url + ((!TextUtils.isEmpty(i) ? "/password/" + i : "") + (!TextUtils
                            .isEmpty(g) ? g : "")));
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return "";
        }

    }
    
    private static String D(String a){
        if (TextUtils.isEmpty(a)) return "";
        StringBuffer c = new StringBuffer();
        int b, f, e, g, h;
        f = a.length();
        b = 0;
        for (; b < f;) {
            e = a.charAt(b++) & 255;
            if (b == f) {
                c.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".charAt(e >> 2));
                c.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".charAt((e & 3) << 4));
                c.append("==");
                break;
            }
            g = a.charAt(b++);
            if (b == f) {
                c.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".charAt(e >> 2));
                c.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".charAt((e & 3) << 4 | (g & 240) >> 4));
                c.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".charAt((g & 15) << 2));
                c.append("=");
                break;
            }
            h = a.charAt(b++);
            c.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".charAt(e >> 2));
            c.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".charAt((e & 3) << 4 | (g & 240) >> 4));
            c.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".charAt((g & 15) << 2 | (h & 192) >> 6));
            c.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".charAt(h & 63));
        }
        return c.toString();
    }
    
    private static String E(String a, String c) {
        int[] b = new int[256];
        int f = 0, i = 0, h = 0;
        StringBuffer e = new StringBuffer();
        for (h = 0; 256 > h; h++)
            b[h] = h;
        for (h = 0; 256 > h; h++) {
            f = (f + b[h] + a.charAt((h % a.length()))) % 256;
            i = b[h];
            b[h] = b[f];
            b[f] = i;
        }

        for (int q = f = h = 0; q < c.length(); q++) {
            h = (h + 1) % 256;
            f = (f + b[h]) % 256;
            i = b[h];
            b[h] = b[f];
            b[f] = i;
            e.append((char) (c.charAt(q) ^ b[(b[h] + b[f]) % 256]));
        }

        return e.toString();

    }
    
    private static String F(String a, int[] c) {
        StringBuffer b = new StringBuffer();
        int i = 0;
        for (int f = 0; f < a.length(); f++) {
            i = 'a' <= a.charAt(f) && 'z' >= a.charAt(f) ? ((int) a.charAt(f)) - 97
                    : a.charAt(f) - '0' + 26;
            for (int e = 0; 36 > e; e++) {
                if (c[e] == i) {
                    i = e;
                    break;
                }
            }
            b.append(25 < i ? (char) (i + '0' - 26) : (char) (i + 97));
        }
        return b.toString();
    }
    
    private static String na(String ep) {
        if (TextUtils.isEmpty(ep)) {
            return null;
        }

        int c, b;
        StringBuffer e = new StringBuffer();

        int[] code = new int[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1,
                -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11,
                12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1,
                -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37,
                38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1,
                -1, -1, -1 };
        int i = ep.length();
        for (int f = 0; f < i;) {
            do {
                c = code[((int) ep.charAt(f++)) & 255];
            } while (f < i && -1 == c);
            if (-1 == c)
                break;

            do {
                b = code[((int) ep.charAt(f++)) & 255];
            } while (f < i && -1 == b);
            if (-1 == b)
                break;
            e.append((char) (c << 2 | (b & 48) >> 4));
            do {
                c = ((int) ep.charAt(f++)) & 255;
                if (61 == c)
                    return e.toString();
                c = code[c];
            } while (f < i && -1 == c);
            if (-1 == c)
                break;
            e.append((char) ((b & 15) << 4 | (c & 60) >> 2));
            do {
                b = (int) ep.charAt(f++) & 255;
                if (61 == b)
                    return e.toString();
                b = code[b];
            } while (f < i && -1 == b);
            if (-1 == b)
                break;
            e.append((char) ((c & 3) << 6 | b));
        }
        return e.toString();
    }
    
    private static String getFileID(String fileid, double seed){
        String mixed = getFileIDMixString(seed);
        String[] ids = fileid.split("\\*");
        StringBuilder realId = new StringBuilder();
        int idx;
        for (int i = 0; i < ids.length; i++)
        {
            idx = Integer.parseInt(ids[i]);
            realId.append(mixed.charAt(idx));
        }
        return realId.toString();
    }

    private static String getFileIDMixString(double seed) {
        StringBuilder mixed = new StringBuilder();
        StringBuilder source = new StringBuilder(
                "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ/\\:._-1234567890");
        int index, len = source.length();
        for (int i = 0; i < len; ++i) {
            seed = (seed * 211 + 30031) % 65536;
            index = (int) Math.floor(seed / 65536 * source.length());
            mixed.append(source.charAt(index));
            source.deleteCharAt(index);
        }
        return mixed.toString();
    }
}
