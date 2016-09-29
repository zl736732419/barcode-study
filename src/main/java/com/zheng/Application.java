package com.zheng;

import com.zheng.util.BarCodeUtil;

/**
 * Created by zhenglian on 2016/9/29.
 */
public class Application {
    public static void main(String[] args) throws Exception {
    	String msg = "123456789";
    	BarCodeUtil.create(msg, "小张");
    }

}
