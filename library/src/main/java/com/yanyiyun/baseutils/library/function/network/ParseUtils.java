package com.yanyiyun.baseutils.library.function.network;

import android.content.Context;

import com.yanyiyun.baseutils.library.tool.StringTool;
import com.yanyiyun.baseutils.library.tool.log;
import com.google.gson.TypeAdapter;

import org.json.JSONObject;

/**
 * Created on 2018/6/28.
 */
public class ParseUtils<T> {

    private TypeAdapter<T> typeAdapter;

    public ParseUtils(TypeAdapter typeAdapter) {
        this.typeAdapter = typeAdapter;
    }

    private Context mContext;

    public T parseResult(String resultJson) {
        log.e( "接口返回：" + resultJson);
        try {
            if (null != resultJson) {
                String _val = resultJson;


                JSONObject _json = new JSONObject(_val);
                ResultBean _baseBean = new ResultBean();
                JSONObject resultObject = _json.optJSONObject("result");
                _baseBean.code = resultObject.optInt("code");
                _baseBean.msg = resultObject.optString("msg");
                _baseBean.data = _json.optString("data");

                switch (_baseBean.code) {
                    // 执行成功
                    case 10000:{
                        if (!StringTool.isEmpty(_baseBean.data)) {
                            T resultBean = typeAdapter.fromJson(_baseBean.data);
                            if (resultBean instanceof ResultBean) {
                                ((ResultBean) resultBean).code = _baseBean.code;
                                ((ResultBean) resultBean).msg = _baseBean.msg;
                                ((ResultBean) resultBean).data = _baseBean.data;
                            }
                            if (null != resultBean) {
                                return resultBean;
                            }
                        } else {
                            T resultBean = (T) _baseBean;
                            return resultBean;
                        }
                        break;
                    }
                    case 400: // 执行失败
                    case 500: // 内部异常
                    case 404: // 无此接口
                        break;
                    case 1000: // 访问拒绝，用户未登录
                    case 1001: // 该当前账户已在其他地方登录,请重新登录
                        break;
                }
                Result result = new Result();
                result.setCode(_baseBean.code);
                result.setMsg(_baseBean.msg);
                return (T) new CodeBean(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
