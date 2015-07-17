package com.game.rpg;

import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 绘制怪物的类
 * 
 * @author Administrator
 * 
 */
public class Monster implements Runnable {
	private Image img;// 素材
	private ResourseReader reader;
	private Map map;// 用于调用map的方法
	private int[][][] mapArray;// 地图数组
	private java.util.List<Integer[]> monsterList;// 存放产生出来的怪
	private final int SIZE = 32;// 常量，地图每一格长宽为32px

	public Monster(Map map) throws IOException {
		this.map = map;// 传入map
		reader = new ResourseReader();
		img = reader.getImage("res/monster.png");// 载入素材
		monsterList = new ArrayList<Integer[]>();
	}

	/**
	 * 每次调用产生一个怪物
	 * 
	 * @throws IOException
	 */
	public void create() throws IOException {
		int y = map.getMapWidth();// 地图列数
		int x = map.getMapHeight();// 行数

		if (map.getCreateMonster() && monsterList.size() < x * y / 50) {// 该地图允许刷怪且数量未到达上限时才产生怪,怪的密度为1/50
			mapArray = map.getMapArray();// 获取地图数组

			int monsterX;// 即将刷怪的坐标
			int monsterY;
			int monsterDir;// 刷怪的方向 0下1左2右3上
			do {
				monsterX = (int) (Math.random() * x);// 随机产生刷怪的坐标
				monsterY = (int) (Math.random() * y);
				monsterDir = (int) (Math.random() * 4);// 随机产生刷怪的方向
			} while (!isValidPlace(monsterX, monsterY));// 坐标不合法就重新产生坐标

			monsterList.add(new Integer[] { monsterX, monsterY, monsterDir });// 将产生的怪的坐标存入list
		}
	}

	/**
	 * 判断（x，y）处是否是有效的刷怪点
	 * 
	 * @param x
	 * @param y
	 * @return 能产生返回true
	 */
	public boolean isValidPlace(int x, int y) {
		for (Integer[] obj : monsterList) {// 检查该点是否有怪
			if (obj[0] == x && obj[1] == y)
				return false;
		}
		if ((mapArray[x][y][2] & 0x80) == 0)// 检查该点是否可以通行
			return false;
		return true;
	}

	/**
	 * 开始绘制怪物，绘制在map(extends JPanel)上
	 * 
	 * @param g
	 */
	public void drawMonster(Graphics g) {
		int offx = map.getOffsetX();// 获取绘制的偏移
		int offy = map.getOffsetY();
		int x, y;// 存放怪物的坐标
		int dir;// 怪的方向
		for (Integer[] obj : monsterList) {// 遍历所有已产生的怪
			x = obj[0];// 取出坐标
			y = obj[1];
			dir = obj[2];// 取出方向
			g.drawImage(img, offx + y * SIZE, offy + x * SIZE, offx + y * SIZE
					+ SIZE, offy + x * SIZE + SIZE, 0 * SIZE, dir * SIZE, 0
					* SIZE + SIZE, dir * SIZE + SIZE, null);
		}
	}

	/**
	 * 移动怪物
	 * 
	 * @param obj
	 */
	public void moveMonsters() {
		for (Integer[] obj : monsterList) {// 遍历所有已产生的怪
			if (Math.random() < 0.5) {// 怪物有0.5的概率会移动
				int dir = (int) (Math.random() * 4);// 产生一个新的移动方向
				if (obj[2] == dir) {// 如果移动方向与原方向相等
					switch (dir) {// 根据方向调整怪物的坐标
					case 0:
						if (isValidPlace(obj[0] + 1, obj[1])) {
							obj[0] += 1;
						}
						break;
					case 1:
						if (isValidPlace(obj[0], obj[1] - 1)) {
							obj[1] -= 1;
						}
						break;
					case 2:
						if (isValidPlace(obj[0], obj[1] + 1)) {
							obj[1] += 1;
						}
						break;
					case 3:
						if (isValidPlace(obj[0] - 1, obj[1])) {
							obj[0] -= 1;
						}
						break;
					default:
						break;
					}
				} else {// 与原方向不等的话，只改变方向
					obj[2] = dir;
				}
			}
		}
	}

	/**
	 * 清空存放怪物的list
	 * 
	 * 切换地图时由map调用
	 */
	public void removeAllMonsters() {
		monsterList.clear();
	}

	/**
	 * 刷怪线程的实现
	 */
	@Override
	public void run() {
		while (true) {
			try {
				create();// 产生怪
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			for (int i = 0; i < 5; i++) {
				map.repaint();// 绘制地图，包括怪物
				moveMonsters();// 绘制后让所有怪移动一次
				try {
					Thread.sleep(500);// 绘制间隔
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
