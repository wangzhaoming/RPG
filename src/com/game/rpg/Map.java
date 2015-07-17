package com.game.rpg;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.swing.JPanel;

/**
 * 游戏主要界面的实现
 * 
 * 包括地图的切换，人物的行走，对话等
 * 
 * @author Administrator
 * 
 */
public class Map extends JPanel implements KeyListener, Runnable {

	private int offsetX, offsetY;// 地图绘制的偏移量，相对于容器的坐标
	private int dirRow;// 记录人物前进的方向，1下，-1上,单位是行
	private int dirCol;// 方向，-1左，1右,列
	private boolean dirChanged;// 记录方向是否改变，即当前按下的方向与上一次的是否相同
	private final int SIZE = 32;// 常量，地图每一格长宽为32px
	private int[][][] map;// 存放地图的数组，从文件读入
	private Image img;// 绘制地图的素材
	private int mapWidth, mapHeight;// 游戏地图的大小，记录在地图文件的前两个字节
	private boolean isRun;// 人是否在走，即是否按下了方向键
	private boolean keyDown;// 方向键是否被按下，用于处理人物行走的延时，屏蔽系统对按键的repeat处理
	private Unit unit;// 绘制人物的类
	private String mapName;// 记录当前地图的名称
	private long time;// 存储当前系统时间，人物行走延时用
	private ResourseReader reader;// 读取资源的类
	private GameMenuPanel gameMenu;// 游戏内菜单
	private Monster monster;// 产生怪物的类
	private DialogPanel dialogPanel;// 用于显示对话的panel

	public Map() throws IOException {
		offsetX = offsetY = 0;// 缺省偏移量
		dirRow = 1;// 缺省方向，下
		dirCol = 0;
		dirChanged = false;
		mapName = "map00.txt";// 缺省载入的地图文件
		unit = new Unit();
		isRun = false;
		keyDown = false;
		reader = new ResourseReader();
		gameMenu = new GameMenuPanel(this);
		monster = new Monster(this);

		dialogPanel = new DialogPanel();

		setLayout(null);
		add(gameMenu);// 将游戏内菜单添加至map上，初始时不可见
		add(dialogPanel);// 添加对话框，初始时不可见
		img = reader.getImage("res/building.png");// 读入地图素材

		addKeyListener(this);
		setSize(640, 480);

		map = setMapArray(reader.getInputStream("res/" + mapName));// 读入地图文件

		for (int i = 0; i < mapHeight; i++) {// 循环查找出生点
			for (int j = 0; j < mapWidth; j++) {
				for (int k = 0; k < 3; k++) {
					if ((map[i][j][k] & 0x20) == 0x20) {
						// 判断该点是否为人物的出生点，即人物初始出现的坐标
						// 根据坐标设置偏移量，使出生点位于屏幕的中央，与人物重合
						// 每张地图最多只有一个出生点
						offsetX = -j * 32 + 320;
						offsetY = -i * 32 + 224;
					}
				}
			}
		}
	}

	/**
	 * 读取地图文件的方法,设置地图数组
	 * 
	 * @param in
	 *            存放地图文件的输入流
	 * 
	 * @return 存有地图文件的数组
	 * 
	 * @throws IOException
	 */
	public int[][][] setMapArray(InputStream in) throws IOException {
		mapHeight = in.read() + 1;
		mapWidth = in.read() + 1;
		int[][][] map = new int[mapHeight][mapWidth][3];

		for (int i = 0; i < mapHeight; i++) {
			for (int j = 0; j < mapWidth; j++) {
				for (int k = 0; k < 3; k++) {
					map[i][j][k] = in.read();
				}
			}
		}
		return map;
	}

	/**
	 * 取得地图数组
	 * 
	 * @return
	 */
	public int[][][] getMapArray() {
		return map;
	}

	/**
	 * 切换场景时载入新地图
	 * 
	 * 此时的出生坐标在配置文件中读取，即偏移量在读取配置文件时设置，getMapName方法
	 * 
	 * @throws IOException
	 */
	public void loadNewMap() throws IOException {
		try {
			setMapName();// 获取并设置地图名称
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		map = setMapArray(reader.getInputStream("res/" + mapName));// 获取地图
		monster.removeAllMonsters();// 切换地图时清空怪物列表
		for (int i = 0; i < mapHeight * mapWidth / 100; i++) {
			monster.create();// 切换地图时先重新生成一半的怪
		}
		repaint();
	}

	/**
	 * 读取配置文件，读取并设置切换的地图名称，同时设置偏移
	 * 
	 * 设置好的地图名称将在读取新地图时用到
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void setMapName() throws FileNotFoundException, IOException {
		Properties data = reader.getProperties("res/data"
				+ mapName.substring(3, 5) + ".txt");// 根据当前的地图名加载相应的配置文件
		String[] str = data.getProperty("jump_" + getPoint()).split(",");// 根据坐标查找到要跳转的地图名，出生位置数据，以逗号分隔，格式为“mapname,x,y”
		mapName = str[0];// 设置新地图名
		int x = Integer.valueOf(str[1]);// 出生点x坐标
		int y = Integer.valueOf(str[2]);// 出生点y坐标
		offsetX = -y * 32 + 320;// 根据坐标设置偏移
		offsetY = -x * 32 + 224;
	}

	/**
	 * 获取地图的刷怪信息
	 * 
	 * 根据地图名读取配置
	 * 
	 * @return true 则刷怪
	 * @throws IOException
	 */
	public boolean getCreateMonster() throws IOException {
		Properties data = reader.getProperties("res/data"
				+ mapName.substring(3, 5) + ".txt");
		String str = data.getProperty("createMonster");
		return str.equals("1") ? true : false;
	}

	/**
	 * 处理偏移量数据，绘制地图时用到
	 * 
	 * 若偏移x为负，地图原点相对于容器原点坐标为负，即容器原点相对于地图原点坐标为正，从地图的-x坐标开始绘制
	 * 
	 * x为正，地图原点相对于容器原点坐标为正，即地图原点在容器内，显然从地图原点0开始绘制
	 * 
	 * @param x
	 * @return x>=0,返回0,x<0,返回-x/32(地图坐标按行列计算，每行每列为32像素)
	 */
	public int func1(int x) {
		return x >= 0 ? 0 : -x / 32;
	}

	/**
	 * 获取人物面向的位置的标志位信息
	 * 
	 * 信息来自地图数组
	 * 
	 * @return 标志位的数据，int型，仅低8位有效
	 */
	public int getFlag() {
		return map[7 + dirRow - offsetY / 32][10 + dirCol - offsetX / 32][2];
	}

	/**
	 * 获取人物面向的位置，以行列表示
	 * 
	 * @return 字符串，如8行5列表示为“8_5”
	 */
	public String getPoint() {
		return String.valueOf(7 + dirRow - offsetY / 32) + "_"
				+ String.valueOf(10 + dirCol - offsetX / 32);
	}

	/**
	 * 显示对话数据
	 * 
	 * 读取自地图相对应的配置文件
	 * 
	 * @param g
	 * @return 对话的字符串，用于显示在控制台，调试用，无其他用途
	 * @throws IOException
	 */
	public String showDialog() throws IOException {
		Properties data = reader.getProperties("res/data"
				+ mapName.substring(3, 5) + ".txt");
		String str = data.getProperty("dialog_" + getPoint());// 根据坐标获取对话
		dialogPanel.open(str);// 将对话传入并显示
		return str;
	}

	/**
	 * 移动地图的方法
	 */
	public void moveMap() {
		long lag = System.currentTimeMillis() - time;// 计算按下键的时间与当前时间的时间差，超过某一值人物才会移动，否则只转向不移动
		if (isRun == true && (lag > 100 || dirChanged == false)) {
			// 人物移动的条件：方向键被按下&&(方向键按下的时间超过100毫秒||移动方向不变)
			// 由于重绘间隔是150ms，且方向改变时的第一次重绘lag必然小于100ms，此时只会改变方向，不会移动
			// 第二次重绘lag必然不小于150，就会移动，lag的值取10~149都可以，没区别

			if ((map[7 + dirRow - offsetY / 32][10 + dirCol - offsetX / 32][2] & 0x80) == 0x80) {
				offsetX -= dirCol * 32;
				offsetY -= dirRow * 32;
			}// 根据方向移动，读取标志位，该方向可通行时才移动，移动就是改变偏移量
		}
	}

	/**
	 * 绘制地图及人物，包括人物的跑动
	 */
	@Override
	protected void paintComponent(Graphics g) {
//		super.paintComponent(g);

		g.fillRect(0, 0, 640, 480);
		// 根据地图数据读取素材开始绘制
		for (int i = func1(offsetY); i < func1(offsetY) + 15 && i < mapHeight; i++) {
			for (int j = func1(offsetX); j < func1(offsetX) + 20
					&& j < mapWidth; j++) {
				g.drawImage(img, offsetX + j * SIZE, offsetY + i * SIZE,
						offsetX + j * SIZE + SIZE, offsetY + i * SIZE + SIZE,
						map[i][j][0] * SIZE, map[i][j][1] * SIZE, map[i][j][0]
								* SIZE + SIZE, map[i][j][1] * SIZE + SIZE, null);
			}
		}
		unit.move(g, dirRow, dirCol, isRun);// 根据方向绘制人物行走的动画，调用unit的方法
		monster.drawMonster(g);// 重新绘制怪物
	}

	/**
	 * 获取x方向偏移量
	 * 
	 * @return
	 */
	public int getOffsetX() {
		return offsetX;
	}

	/**
	 * 获取y方向偏移量
	 * 
	 * @return
	 */
	public int getOffsetY() {
		return offsetY;
	}

	/**
	 * 键盘监听
	 * 
	 * @param e
	 */
	public void keyPressed(KeyEvent e) {
		if (keyDown == false) {// 没有键被按下才执行，防止系统自动对按键repeat
			keyDown = true;// 记录键盘被按下
			time = System.currentTimeMillis();// 记录键按下的时间
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				if (dirCol != -1) {// 原方向不为1时方向改变
					dirCol = -1;// 设置方向
					dirRow = 0;
					dirChanged = true;// 设置方向改变
				} else {
					dirChanged = false;
				}
				isRun = true;
				dialogPanel.close();//关闭对话
			} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				if (dirCol != 1) {
					dirCol = 1;
					dirRow = 0;
					dirChanged = true;
				} else {
					dirChanged = false;
				}
				isRun = true;
				dialogPanel.close();
			} else if (e.getKeyCode() == KeyEvent.VK_UP) {
				if (dirRow != -1) {
					dirRow = -1;
					dirCol = 0;
					dirChanged = true;
				} else {
					dirChanged = false;
				}
				isRun = true;
				dialogPanel.close();
			} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				if (dirRow != 1) {
					dirRow = 1;
					dirCol = 0;
					dirChanged = true;
				} else {
					dirChanged = false;
				}
				isRun = true;
				dialogPanel.close();
			} else if (e.getKeyCode() == KeyEvent.VK_A) {// 设置a键被按下的事件
				System.out.println(getPoint());// 控制台输出面对的坐标
				if ((getFlag() & 0x8) == 0x8) {// 从标志位判断是否有对话
					try {
						System.out.println(showDialog());// 有对话时绘制对话，并从控制台输出
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				} else if ((getFlag() & 0x10) == 0x10) {// 从标志位判断此处是否是传送点
					try {
						loadNewMap();// 是传送点就载入新地图
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {// enter键被按下时的事件，唤醒菜单
				dialogPanel.close();
				gameMenu.setVisible(true);
				gameMenu.requestFocus();
			}
		}
	}

	/**
	 * 按键释放的监听
	 * 
	 * @param e
	 */
	public void keyReleased(KeyEvent e) {
		keyDown = false;
		if (e.getKeyCode() == KeyEvent.VK_LEFT
				|| e.getKeyCode() == KeyEvent.VK_RIGHT
				|| e.getKeyCode() == KeyEvent.VK_UP
				|| e.getKeyCode() == KeyEvent.VK_DOWN) {
			isRun = false;
		}
	}

	public void keyTyped(KeyEvent arg0) {

	}

	/**
	 * 获取地图的高
	 * 
	 * @return
	 */
	public int getMapHeight() {
		return mapHeight;
	}

	/**
	 * 获取地图的宽
	 * 
	 * @return
	 */
	public int getMapWidth() {
		return mapWidth;
	}

	/**
	 * 线程的实现
	 * 
	 * 循环重绘地图
	 */
	public void run() {
		Thread monsterThread = new Thread(monster);// 启动刷怪线程
		monsterThread.start();

		while (true) {
//			System.out.println(isRun);
			while (isRun == true) {// 方向键被按下时就重绘
				this.moveMap();// 移动地图
				this.repaint();
//				System.out.println(offsetX + "," + offsetY);
				try {
					Thread.sleep(150);// 重绘间隔为150毫秒
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
