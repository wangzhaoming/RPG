package com.game.rpg;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

/**
 * ����Ļ���
 * 
 * @author Administrator
 * 
 */
public class Unit {
	private Image img;// ���������ز�
	private final int SIZE = 32;
	private int i = 0;// ��¼���Ƶ��������������߹�����������
	private ResourseReader reader;// ��Դ��ȡ��

	public Unit() throws IOException {
		reader = new ResourseReader();
		img = reader.getImage("res/character.png");// �����ز�ͼƬ
	}

	public void move(Graphics g, int dirRow, int dirCol, boolean isRun) {// ��ͼ�������������ݷ���
		int n = 0;// ��¼�����Ƶ��������زĵĵڼ���
		if (dirRow == 1) {// ���ݷ���ѡ��
			n = 0;
		} else if (dirCol == -1) {
			n = 1;
		} else if (dirCol == 1) {
			n = 2;
		} else if (dirRow == -1) {
			n = 3;
		}
		if (isRun) {
			i++;// ������ܾͻ�����һ�ţ��γɶ���
			if (i == 3) {
				i = 0;// ����ͼ��ѭ��
			}
		}
		g.drawImage(img, 320, 240 - SIZE / 2, 320 + SIZE, 240 + SIZE / 2, i
				* SIZE, n * SIZE, i * SIZE + 32, n * SIZE + 32, null);
		
	}
}
