package com.game.rpg;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.*;

/**
 * 程序主窗体，程序入口
 * 
 * @author Administrator
 * 
 */
public class GameStart extends JFrame implements ActionListener {
	private Map map;// 游戏地图，即游戏界面
	private OpeningPanel op;// 游戏的开场动画
	private GameStartMenu gsm;// 开场动画后的游戏选项
	private MenuItem mi;// 菜单栏内开始游戏的菜单

	public GameStart() throws IOException {
		setLayout(null);
		setSize(646, 528);

		MenuBar mb = new MenuBar();
		Menu m = new Menu("Game");
		mi = new MenuItem("Start");
		m.add(mi);
		mb.add(m);
		mi.addActionListener(this);
		this.setMenuBar(mb);
		this.setResizable(false);
		setDefaultCloseOperation(3);
		setVisible(true);

		map = new Map();
		op = new OpeningPanel(this);
		gsm = new GameStartMenu(this);
	}

	public static void main(String[] args) throws IOException {
		GameStart demo = new GameStart();
	}

	/**
	 * 选择新游戏后添加Map，开始游戏线程
	 * @throws IOException 
	 */
	public void startGame() throws IOException {
		this.add(map);
		map.requestFocus();
		Thread t = new Thread(map);
		t.start();
		map.repaint();
		gsm.setEnabled(false);//禁用并移除新游戏菜单（GameStartMenu）
		this.remove(gsm);
		this.validate();
	}

	/**
	 * 显示开场动画后的游戏选项，新游戏或继续上次的游戏
	 */
	public void showStartOption() {
		add(gsm);
		gsm.repaint();
		gsm.requestFocus();
		op.setEnabled(false);//移除开场动画用的JPanel（OpeningPanel）
		this.remove(op);
		this.validate();
	}

	/**
	 * 菜单的监听，按start后会开始播放开场动画
	 */
	public void actionPerformed(ActionEvent arg0) {
		mi.setEnabled(false);//禁用start菜单按钮
		this.add(op);//添加OpeningPanel
		op.requestFocus();
		op.repaint();
		Thread opThread = new Thread(op);//开始线程
		opThread.start();
	}
}
