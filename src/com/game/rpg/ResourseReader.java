package com.game.rpg;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.imageio.ImageIO;

/**
 * ���ڶ�ȡ�ļ�����
 * 
 * @author Administrator
 * 
 */
public class ResourseReader {

	/**
	 * ��ȡһ��ͼƬ�ļ�
	 * 
	 * @param path
	 * @return Image
	 * @throws IOException
	 */
	public Image getImage(String path) throws IOException {
		InputStream in = this.getClass().getResourceAsStream(path);// ͼƬ����������
		Image img = ImageIO.read(in);// ����ͼƬ
		in.close();
		return img;
	}

	/**
	 * ��ȡһ���ı��ļ�����������
	 * 
	 * @param path
	 * @return װ���ı��ļ���������
	 */
	public InputStream getInputStream(String path) {
		InputStream in = this.getClass().getResourceAsStream(path);// �ļ�����������
		return in;
	}

	/**
	 * ��ȡһ�������ļ�
	 * 
	 * @param path
	 * @return Properties �����ļ�����
	 * @throws IOException
	 */
	public Properties getProperties(String path) throws IOException {
		Properties p = new Properties();// �½������ļ�
		p.load(getInputStream(path));// ��ȡ
		return p;
	}

}
