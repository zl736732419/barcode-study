package com.zheng;

import com.google.zxing.qrcode.encoder.QRCode;
import com.zheng.util.BarCodeUtil;
import com.zheng.util.QrCodeUtil;

import java.io.File;
import java.io.IOException;

/**
 * Created by zhenglian on 2016/9/29.
 */
public class Application {
    public static void main(String[] args) throws Exception {
    	new Application().createBarCode();
//        new Application().createQR();
//        new Application().parseQR();
    }

    private void parseQR() throws IOException {
        File file = new File("D://images/zxing.png");
        QrCodeUtil.parseQR(file);
    }

    private void createQR() throws Exception {
        String msg = "123456789";
        String name = "小张";
        QrCodeUtil.createQR(msg, name);
    }

    private void createBarCode() throws Exception {
        String msg = "1234567890";
        String name = "老王";
        BarCodeUtil.create(msg, name);
    }





}
