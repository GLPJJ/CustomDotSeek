package simple.util.net.observable;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import rx.functions.Func1;
import simple.bean.CommonBeanT;
import simple.util.gson.GsonUtils;

/**
 * Created by glp on 2016/8/18.
 */

//提供对 通用返回数据的转换。qishi meiluanyong
@Deprecated
public class HttpResultFunc<T> implements Func1<String, CommonBeanT<T>> {
    @Override
    public CommonBeanT<T> call(String s) {
        CommonBeanT<T> ret = null;
        try {
            Type type = new TypeToken<CommonBeanT<T>>() {
            }.getType();
            ret = GsonUtils.getGson().fromJson(s, type);
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }
}
