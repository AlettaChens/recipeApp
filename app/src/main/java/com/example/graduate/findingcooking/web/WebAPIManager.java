package com.example.graduate.findingcooking.web;

import android.content.Context;

import com.example.graduate.findingcooking.utils.NetWorkUtil;

import java.io.File;
import java.util.concurrent.TimeUnit;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.graduate.findingcooking.web.Constant.BASEURL;
import static com.example.graduate.findingcooking.web.Constant.WEBDIR;

public class WebAPIManager{
	private static Retrofit retrofit;
	private static RxHttpRequestHandler rxHttpRequestHandler;

	public static RxHttpRequestHandler getInstance(Context context) {

		if (retrofit == null) {
			synchronized (WebAPIManager.class) {
				if (retrofit == null) {
					retrofit = new Retrofit.Builder().baseUrl(BASEURL).client(InitOkHttp(context,WEBDIR)).addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory
							(RxJava2CallAdapterFactory.create()).build();
				rxHttpRequestHandler=retrofit.create(RxHttpRequestHandler.class);
				}
			}
		}
		return rxHttpRequestHandler;
	}

	private static OkHttpClient InitOkHttp(Context context,String webLogDir) {
		OkHttpClient.Builder builder = new OkHttpClient.Builder();
		File cacheFile = new File(webLogDir);
		Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);
		Interceptor cacheInterceptor = chain -> {
			Request request = chain.request();
			if (!NetWorkUtil.isNetworkConnected(context)) {
				request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
			}
			Response response = chain.proceed(request);
			Response.Builder newBuilder = response.newBuilder();
			if (NetWorkUtil.isNetworkConnected(context)) {
				int maxAge = 60;
				newBuilder.header("Cache-Control", "public, max-age=" + maxAge);
			} else {
				int maxStale = 60 * 60 * 24 * 28;
				newBuilder.header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale);
			}
			return newBuilder.build();
		};
		builder.cache(cache).addInterceptor(cacheInterceptor);
		builder.connectTimeout(15, TimeUnit.SECONDS);
		builder.readTimeout(20, TimeUnit.SECONDS);
		builder.writeTimeout(20, TimeUnit.SECONDS);
		builder.retryOnConnectionFailure(true);
		return builder.build();
	}
}
