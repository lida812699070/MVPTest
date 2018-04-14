package com.careagle.mvptest.api;

import com.careagle.mvptest.entity.Result;
import com.careagle.mvptest.global.G;

import java.util.Map;
import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;

/**
 * Created by lida on 2018/4/4.
 */

public interface UserApi {
    /**
     * 登录
     */
    @FormUrlEncoded
    @POST("http://" + G.DEVELOPE_VERSION + "/commercial/member/login")
    Observable<Result> goLogin(@HeaderMap Map<String, String> headers, @FieldMap Map<String, String> map);

}
