package com.game.rpg;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * 播放开场动画的panel
 * 
 * @author Administrator
 * 
 */
public class OpeningPanel extends JPanel implements Runnable, KeyListener {
	private String str;// 开场动画播放完毕后显示的字符串
	private GameStart game;// 用于调用其中的方法
	private boolean stopped;// 开场动画是否被停止，按了enter
	private ResourseReader reader;// 读取资源的类

	public OpeningPanel(GameStart game) throws IOException {
		this.game = game;
		reader = new ResourseReader();
//		stopped = false;
		stopped = true;
		this.setBackground(Color.black);
		setSize(640, 480);
		str = "";
		addKeyListener(this);
	}

	/**
	 * 绘制动画放完的字符串，放完前字符串为空，不绘制
	 */
	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		Font font = new Font("宋体", Font.BOLD, 50);
		g.setFont(font);
		g.setColor(Color.white);
		FontMetrics fm = g.getFontMetrics();// 用于获取字符串的长宽，方便绘制在容器中间
		g.drawString(str, 320 - fm.stringWidth(str) / 2,
				275 - fm.getHeight() / 2);
	}

	/**
	 * 线程的实现
	 * 
	 * 每秒25帧的速度绘制图片形成动画
	 * 
	 * 动画播放完毕后显示字符串，即给str赋值
	 */
	@Override
	public void run() {
		Image img = null;
		for (int i = 0; i < 2196; i++) {
			try {
				img = reader.getImage("op/" + i + ".jpg");
			} catch (IOException e1) {
				e1.printStackTrace();
			}// 载入相应序号的图片

			Graphics g = this.getGraphics();
			g.drawImage(img, 0, 60, 640, 360, null);// 绘制图片
			try {
				Thread.sleep(40);// 等待40ms，即每秒绘制25帧
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (stopped) {// 按下enter后stopped为true，停止绘制
				break;
			}
		}
		while (!stopped) {// 放完动画且没按enter键，显示字符串
			str = "Press Enter";
			repaint();
			try {
				Thread.sleep(1000);// 等待1s
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			str = "";// 形成闪烁效果
			repaint();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("op is stopped");
	}

	/**
	 * enter键的监听
	 * 
	 * @param e
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			stopped = true;// 按下enter后开场结束，进入开始游戏选项
			game.showStartOption();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}
}
