package com.game.rpg;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.imageio.ImageIO;

/**
 * 用于读取文件的类
 * 
 * @author Administrator
 * 
 */
public class ResourseReader {

	/**
	 * 读取一个图片文件
	 * 
	 * @param path
	 * @return Image
	 * @throws IOException
	 */
	public Image getImage(String path) throws IOException {
		InputStream in = this.getClass().getResourceAsStream(path);// 图片读入输入流
		Image img = ImageIO.read(in);// 读入图片
		in.close();
		return img;
	}

	/**
	 * 读取一个文本文件放入输入流
	 * 
	 * @param path
	 * @return 装有文本文件的输入流
	 */
	public InputStream getInputStream(String path) {
		InputStream in = this.getClass().getResourceAsStream(path);// 文件读入输入流
		return in;
	}

	/**
	 * 读取一个配置文件
	 * 
	 * @param path
	 * @return Properties 配置文件类型
	 * @throws IOException
	 */
	public Properties getProperties(String path) throws IOException {
		Properties p = new Properties();// 新建配置文件
		p.load(getInputStream(path));// 读取
		return p;
	}

}
