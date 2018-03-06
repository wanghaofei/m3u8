package com.mytool.m3u8;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wanghaofei on 2017/12/28.
 */

public class HttpVideoInfoBean extends BaseBean {

    /*
    * @Fields serialVersionUID
    */
    private static final long serialVersionUID = 979151026353824105L;
    private String title;
    private String hls_url;
    private String column;
    private String cdn;
    private String _public;
    private String webUrl;
    private String length;
    public void setLength(String length) {
        this.length = length;
    }
    public String getLength() {
        return length;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String get_public() {
        return _public;
    }

    public void set_public(String _public) {
        this._public = _public;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHls_url() {
        return hls_url;
    }

    public void setHls_url(String hls_url) {
        this.hls_url = hls_url;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }
    public String getCdn() {
        return cdn;
    }

    public void setCdn(String cdn) {
        this.cdn = cdn;
    }


    @Override
    public String toString() {
        return "HttpVideoInfoBean{" +
                "title='" + title + '\'' +
                ", hls_url='" + hls_url + '\'' +
                ", column='" + column + '\'' +
                ", cdn='" + cdn + '\'' +
                ", _public='" + _public + '\'' +
                ", webUrl='" + webUrl + '\'' +
                ", length='" + length + '\'' +
                '}';
    }

    public static HttpVideoInfoBean convertFromJsonObject(String httpResult){
        Log.e("VideoListBean", "开始转换数据");
        HttpVideoInfoBean jsonResult = new HttpVideoInfoBean();
        try {
            JSONObject json = new JSONObject(httpResult);
            if ("".equals(json)) {
                return null;
            }

            // 取title数据
            if (json.has("title")
                    && json.get("title") != null
                    && !"".equals(json.getString("title"))) {
                jsonResult.setTitle(json.getString("title"));
            }
            // 取hls_url数据
            if (json.has("hls_url") && json.get("hls_url") != null
                    && !"".equals(json.getString("hls_url"))) {
                jsonResult.setHls_url(json.getString("hls_url"));
            }
            // 取public数据
            if (json.has("is_invalid_copyright") && json.get("is_invalid_copyright") != null
                    && !"".equals(json.getString("is_invalid_copyright"))) {
                jsonResult.set_public(json.getString("is_invalid_copyright"));
            }
            // 取column数据
            if (json.has("column") && json.get("column") != null
                    && !"".equals(json.getString("column"))) {
                jsonResult.setColumn(json.getString("column"));
            }
            JSONObject jsonData = json.getJSONObject("hls_cdn_info");
            // 取column数据
            if (jsonData.has("cdn_code") && jsonData.get("cdn_code") != null
                    && !"".equals(jsonData.getString("cdn_code"))) {
                jsonResult.setCdn(jsonData.getString("cdn_code"));
            }
            // 取length数据
            if (json.has("video") && json.get("video") != null
                    && !"".equals(json.getString("video"))) {
                JSONObject jsonDatal = json.getJSONObject("video");
                // 取column数据
                if (jsonDatal.has("totalLength") && jsonDatal.get("totalLength") != null
                        && !"".equals(jsonDatal.getString("totalLength"))) {
                    jsonResult.setLength(jsonDatal.getString("totalLength"));
                    Log.e("jsx=totalLength==", ""+jsonDatal.getString("totalLength"));
                }
            }

            return jsonResult;

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("three","接口数据转换失败");
            return null;
        }
    }
}
