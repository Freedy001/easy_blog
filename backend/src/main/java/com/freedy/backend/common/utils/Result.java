/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.freedy.backend.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.freedy.backend.enumerate.ResultCode;
import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回数据
 *
 * @author Mark sunlightcs@gmail.com
 */
public class Result extends HashMap<String, Object> {
	private static final long serialVersionUID = 1L;

	public Result setData(Object date){
		put("data",date);
		return this;
	}

	public <T>T getData(String key,TypeReference<T> tTypeReference){
		Object data = get(key);
		String s = JSON.toJSONString(data);
		return JSON.parseObject(s,tTypeReference);
	}

	public <T>T getData(TypeReference<T> tTypeReference){
		Object data = get("data");
		String s = JSON.toJSONString(data);
		return JSON.parseObject(s,tTypeReference);
	}

	public Result() {
		put("code", ResultCode.SUCCESS.getCode());
		put("msg", ResultCode.SUCCESS.getMessage());
	}
	
	public static Result error() {
		return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "未知异常，请联系管理员");
	}
	
	public static Result error(String msg) {
		return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, msg);
	}

	public static Result error(int code, String msg) {
		Result result = new Result();
		result.put("code", code);
		result.put("msg", msg);
		return result;
	}

	public static Result error(String code, String msg) {
		Result result = new Result();
		result.put("code", code);
		result.put("msg", msg);
		return result;
	}

	public static Result ok() {
		return new Result();
	}

	public static Result ok(String msg) {
		Result result = new Result();
		result.put("msg", msg);
		return result;
	}
	
	public static Result ok(int code, String msg) {
		Result result = new Result();
		result.put("code", code);
		result.put("msg", msg);
		return result;
	}

	@Override
	public Result put(String key, Object value) {
		super.put(key, value);
		return this;
	}

	public Integer getCode() {
		return (Integer)this.get("code");
	}
}
