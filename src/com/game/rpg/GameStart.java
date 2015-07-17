package com.game.rpg;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.*;

/**
 * ���������壬�������
 * 
 * @author Administrator
 * 
 */
public class GameStart extends JFrame implements ActionListener {
	private Map map;// ��Ϸ��ͼ������Ϸ����
	private OpeningPanel op;// ��Ϸ�Ŀ�������
	private GameStartMenu gsm;// �������������Ϸѡ��
	private MenuItem mi;// �˵����ڿ�ʼ��Ϸ�Ĳ˵�

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
	 * ѡ������Ϸ�����Map����ʼ��Ϸ�߳�
	 * @throws IOException 
	 */
	public void startGame() throws IOException {
		this.add(map);
		map.requestFocus();
		Thread t = new Thread(map);
		t.start();
		map.repaint();
		gsm.setEnabled(false);//���ò��Ƴ�����Ϸ�˵���GameStartMenu��
		this.remove(gsm);
		this.validate();
	}

	/**
	 * ��ʾ�������������Ϸѡ�����Ϸ������ϴε���Ϸ
	 */
	public void showStartOption() {
		add(gsm);
		gsm.repaint();
		gsm.requestFocus();
		op.setEnabled(false);//�Ƴ����������õ�JPanel��OpeningPanel��
		this.remove(op);
		this.validate();
	}

	/**
	 * �˵��ļ�������start��Ὺʼ���ſ�������
	 */
	public void actionPerformed(ActionEvent arg0) {
		mi.setEnabled(false);//����start�˵���ť
		this.add(op);//���OpeningPanel
		op.requestFocus();
		op.repaint();
		Thread opThread = new Thread(op);//��ʼ�߳�
		opThread.start();
	}
}
