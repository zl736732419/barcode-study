package com.zheng.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import javax.imageio.ImageIO;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.DefaultConfigurationBuilder;
import org.krysalis.barcode4j.BarcodeGenerator;
import org.krysalis.barcode4j.BarcodeUtil;
import org.krysalis.barcode4j.output.BarcodeCanvasSetupException;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.output.eps.EPSCanvasProvider;
import org.krysalis.barcode4j.output.svg.SVGCanvasProvider;
import org.w3c.dom.DocumentFragment;

import com.google.common.io.Resources;

/**
 * 条码工具生成器 最后生成png格式的图片文件 Created by zhenglian on 2016/9/29.
 */
public class BarCodeUtil {
	public static void create(String msg, String name) throws Exception {
		URI uri = Resources.getResource("barcode128.xml").toURI();
		File file = new File(uri);
		DefaultConfigurationBuilder builder = new DefaultConfigurationBuilder();
		Configuration configuration = builder.buildFromFile(file);

		BarcodeGenerator gen = BarcodeUtil.getInstance().createBarcodeGenerator(configuration);

		// svg(gen, msg);
		bitmaps(gen, msg, name);
		// eps(gen, msg);

	}

	private static void eps(BarcodeGenerator gen, String msg) throws Exception {
		OutputStream out = new java.io.FileOutputStream(new File("D:/images/output.eps"));
		EPSCanvasProvider provider = new EPSCanvasProvider(out, 0);
		gen.generateBarcode(provider, msg);
		provider.finish();
	}

	/**
	 * 生成图像
	 *
	 * @author zhenglian
	 * @data 2016年9月29日 下午4:22:31
	 * @param gen
	 * @param msg
	 * @throws Exception
	 */
	private static void bitmaps(BarcodeGenerator gen, String msg, String name) throws Exception {
		OutputStream out = new FileOutputStream(new File("D:/images/output.png"));
		BitmapCanvasProvider provider = new BitmapCanvasProvider(out, "image/x-png", 300, BufferedImage.TYPE_BYTE_GRAY,
				true, 0);
		gen.generateBarcode(provider, msg);
		provider.finish();

		drawString(provider, name);
	}

	private static void drawString(BitmapCanvasProvider provider, String name) throws IOException {
		BufferedImage barcodeImg = provider.getBufferedImage();
		int w = barcodeImg.getWidth();
		int barcodeImgH = barcodeImg.getHeight() + 50;
		int h = barcodeImgH;
		BufferedImage newImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		// 获取画布的画笔
		Graphics2D g2 = (Graphics2D) newImage.getGraphics();
		// 开始绘图
		g2.setBackground(Color.WHITE);
		g2.clearRect(0, 0, w, h);
		g2.drawImage(barcodeImg, 0, 0, null);
		g2.setPaint(Color.black);
		int fontSize = 20;
		Font f1 = new Font("宋体", Font.BOLD, fontSize);
		g2.setFont(f1);

		int x = (w - name.length() * fontSize) / 2;
		g2.drawString(name, x, barcodeImgH - 25);
		BufferedImage outImage = zoomInImage(newImage, 1);
		ImageIO.write(outImage, "png", new File("D:/images/me.png"));

	}

	/**
	 * 生成svg
	 *
	 * @author zhenglian
	 * @data 2016年9月29日 下午4:22:36
	 * @param gen
	 * @param msg
	 * @throws BarcodeCanvasSetupException
	 */
	private static void svg(BarcodeGenerator gen, String msg) throws Exception {
		// w3c
		SVGCanvasProvider provider = new SVGCanvasProvider(false, 0);
		gen.generateBarcode(provider, msg);
		DocumentFragment frag = provider.getDOMFragment();
		// jdom
		// JDOMSVGCanvasProvider provider = new JDOMSVGCanvasProvider(false, 0);
		// gen.generateBarcode(provider, msg);
		// org.jdom.Document doc = provider.getDocument();

		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer trans = factory.newTransformer();
		Source src = new DOMSource(frag);
		Result res = new StreamResult(new File("D:/images/test.svg"));
		trans.transform(src, res);

	}

	public static BufferedImage zoomOutImage(BufferedImage originalImage, Integer times) {
		int width = originalImage.getWidth() / times;
		int height = originalImage.getHeight() / times;
		BufferedImage newImage = new BufferedImage(width, height, originalImage.getType());
		Graphics g = newImage.getGraphics();
		g.drawImage(originalImage, 0, 0, width, height, null);
		g.dispose();
		return newImage;
	}

	public static BufferedImage zoomInImage(BufferedImage originalImage, Integer times) {
		int width = originalImage.getWidth() * times;
		int height = originalImage.getHeight() * times;
		BufferedImage newImage = new BufferedImage(width, height, originalImage.getType());
		Graphics g = newImage.getGraphics();
		g.drawImage(originalImage, 0, 0, width, height, null);
		g.dispose();
		return newImage;
	}

}
