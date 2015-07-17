package com.game.rpg;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

/**
 * 人物的绘制
 * 
 * @author Administrator
 * 
 */
public class Unit {
	private Image img;// 用于载入素材
	private final int SIZE = 32;
	private int i = 0;// 记录绘制的张数，人物行走共有三个动作
	private ResourseReader reader;// 资源读取类

	public Unit() throws IOException {
		reader = new ResourseReader();
		img = reader.getImage("res/character.png");// 载入素材图片
	}

	public void move(Graphics g, int dirRow, int dirCol, boolean isRun) {// 地图中央绘制人物，根据方向
		int n = 0;// 记录将绘制的人物在素材的第几行
		if (dirRow == 1) {// 根据方向选择
			n = 0;
		} else if (dirCol == -1) {
			n = 1;
		} else if (dirCol == 1) {
			n = 2;
		} else if (dirRow == -1) {
			n = 3;
		}
		if (isRun) {
			i++;// 如果在跑就绘制下一张，形成动画
			if (i == 3) {
				i = 0;// 三张图的循环
			}
		}
		g.drawImage(img, 320, 240 - SIZE / 2, 320 + SIZE, 240 + SIZE / 2, i
				* SIZE, n * SIZE, i * SIZE + 32, n * SIZE + 32, null);
		
	}
}
