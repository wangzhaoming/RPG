package com.game.rpg;

import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;

/**
 * ���ƹ������
 * 
 * @author Administrator
 * 
 */
public class Monster implements Runnable {
	private Image img;// �ز�
	private ResourseReader reader;
	private Map map;// ���ڵ���map�ķ���
	private int[][][] mapArray;// ��ͼ����
	private java.util.List<Integer[]> monsterList;// ��Ų��������Ĺ�
	private final int SIZE = 32;// ��������ͼÿһ�񳤿�Ϊ32px

	public Monster(Map map) throws IOException {
		this.map = map;// ����map
		reader = new ResourseReader();
		img = reader.getImage("res/monster.png");// �����ز�
		monsterList = new ArrayList<Integer[]>();
	}

	/**
	 * ÿ�ε��ò���һ������
	 * 
	 * @throws IOException
	 */
	public void create() throws IOException {
		int y = map.getMapWidth();// ��ͼ����
		int x = map.getMapHeight();// ����

		if (map.getCreateMonster() && monsterList.size() < x * y / 50) {// �õ�ͼ����ˢ��������δ��������ʱ�Ų�����,�ֵ��ܶ�Ϊ1/50
			mapArray = map.getMapArray();// ��ȡ��ͼ����

			int monsterX;// ����ˢ�ֵ�����
			int monsterY;
			int monsterDir;// ˢ�ֵķ��� 0��1��2��3��
			do {
				monsterX = (int) (Math.random() * x);// �������ˢ�ֵ�����
				monsterY = (int) (Math.random() * y);
				monsterDir = (int) (Math.random() * 4);// �������ˢ�ֵķ���
			} while (!isValidPlace(monsterX, monsterY));// ���겻�Ϸ������²�������

			monsterList.add(new Integer[] { monsterX, monsterY, monsterDir });// �������Ĺֵ��������list
		}
	}

	/**
	 * �жϣ�x��y�����Ƿ�����Ч��ˢ�ֵ�
	 * 
	 * @param x
	 * @param y
	 * @return �ܲ�������true
	 */
	public boolean isValidPlace(int x, int y) {
		for (Integer[] obj : monsterList) {// ���õ��Ƿ��й�
			if (obj[0] == x && obj[1] == y)
				return false;
		}
		if ((mapArray[x][y][2] & 0x80) == 0)// ���õ��Ƿ����ͨ��
			return false;
		return true;
	}

	/**
	 * ��ʼ���ƹ��������map(extends JPanel)��
	 * 
	 * @param g
	 */
	public void drawMonster(Graphics g) {
		int offx = map.getOffsetX();// ��ȡ���Ƶ�ƫ��
		int offy = map.getOffsetY();
		int x, y;// ��Ź��������
		int dir;// �ֵķ���
		for (Integer[] obj : monsterList) {// ���������Ѳ����Ĺ�
			x = obj[0];// ȡ������
			y = obj[1];
			dir = obj[2];// ȡ������
			g.drawImage(img, offx + y * SIZE, offy + x * SIZE, offx + y * SIZE
					+ SIZE, offy + x * SIZE + SIZE, 0 * SIZE, dir * SIZE, 0
					* SIZE + SIZE, dir * SIZE + SIZE, null);
		}
	}

	/**
	 * �ƶ�����
	 * 
	 * @param obj
	 */
	public void moveMonsters() {
		for (Integer[] obj : monsterList) {// ���������Ѳ����Ĺ�
			if (Math.random() < 0.5) {// ������0.5�ĸ��ʻ��ƶ�
				int dir = (int) (Math.random() * 4);// ����һ���µ��ƶ�����
				if (obj[2] == dir) {// ����ƶ�������ԭ�������
					switch (dir) {// ���ݷ���������������
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
				} else {// ��ԭ���򲻵ȵĻ���ֻ�ı䷽��
					obj[2] = dir;
				}
			}
		}
	}

	/**
	 * ��մ�Ź����list
	 * 
	 * �л���ͼʱ��map����
	 */
	public void removeAllMonsters() {
		monsterList.clear();
	}

	/**
	 * ˢ���̵߳�ʵ��
	 */
	@Override
	public void run() {
		while (true) {
			try {
				create();// ������
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			for (int i = 0; i < 5; i++) {
				map.repaint();// ���Ƶ�ͼ����������
				moveMonsters();// ���ƺ������й��ƶ�һ��
				try {
					Thread.sleep(500);// ���Ƽ��
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
