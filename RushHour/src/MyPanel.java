import javax.swing.*;
//import javax.swing.text.html.HTMLDocument.Iterator;
import javax.xml.transform.Templates;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.Iterator;

public class MyPanel extends JPanel implements KeyListener {
	private boolean[] isFree = new boolean[36];
	private int currentCar = 1;// 1,2,3,4,5,6
	private int currentGame = 0;// firstly 0
	private int currentOperation = 0;
	private ArrayList<Game> allGames = new ArrayList<Game>();
	private ArrayList<Operation> currentGameOperations = new ArrayList<Operation>();
	public ArrayList<Car> initialCars=new ArrayList<Car>();//该局初始的carList
	public static int totalSteps=0;


	public void printCarList(ArrayList<Car> carList){
		for(int i=0;i<6;i++){
				System.out.println(carList.get(i).getBlocks()[0]+" "+carList.get(i).getBlocks()[1]
						+" "+carList.get(i).getBlocks()[2]);
		}
	}
	
	public void initialize() {
		currentGame=0;
		totalSteps=0;
		String fileName = "src/CarPosition.txt";
		setGames(fileName);
		for (int i = 0; i < 36; i++) {
			isFree[i] = true;
		}
		
//		for(int i=0;i<6;i++){
//			Car tempCar=new Car();
//			//tempCar=allGames.get(0).getCarList().get(i);
//			initialCars.add(tempCar);
//		}
		//initialCars=(ArrayList<Car>)allGames.get(currentGame).getCarList().clone();
		//Iterator<Car> iterator = allGames.get(currentGame).getCarList().iterator();   
//		for(int i=0;i<6;i++){   
//		    initialCars.add(allGames.get(currentGame).getCarList().get(i).clone());
//		    //initialCars.get(i).getBlocks()=(int[])allGames.get(currentGame).getCarList().get(i).getblocks().clone();
//		    System.arraycopy(allGames.get(currentGame).getCarList(), 0, initialCars.get(i).getBlocks(), 0, 3);
//		}  
		initialCars.clear();
		for(int i=0;i<allGames.get(currentGame).getCarList().size();i++){
			Car tempCar=new Car();
			tempCar.setBlocks(allGames.get(currentGame).getCarList().get(i).getBlocks());
			tempCar.setVertical(allGames.get(currentGame).getCarList().get(i).isVertical());
			tempCar.setVisible(allGames.get(currentGame).getCarList().get(i).isVisible());
			initialCars.add(tempCar);
		}
		/*
		Iterator<Car> iterator = allGames.get(currentGame).getCarList().iterator();   
		initialCars.clear();
		while(iterator.hasNext()){   
		    initialCars.add(iterator.next().clone());   
		}  
		System.out.println("carList of game0");
		printCarList(initialCars);
		*/
	} 
	public void gotoNextGame(){
		if(currentGame<(allGames.size()-1)){
			System.out.println("go to new game");
			currentGame ++;
			currentCar = 1;
			currentOperation = 0;
			totalSteps=0;
			currentGameOperations.clear();
//			for(int i=0;i<6;i++){//initialCars已被初始化
//				Car tempCar=new Car();
//				tempCar=allGames.get(currentGame).getCarList().get(i);
//				initialCars.set(i,tempCar);
//			}
			//initialCars=(ArrayList<Car>)allGames.get(currentGame).getCarList().clone();
			Iterator<Car> iterator = allGames.get(currentGame).getCarList().iterator();   
			initialCars.clear();
			
			while(iterator.hasNext()){   
			    initialCars.add(iterator.next().clone());   
			} 
			for(int i=0;i<36;i++){
				isFree[i]=true;
			}
			repaint();
			MyPanel.this.requestFocus();
		}
		else{
			System.out.println("all games win");
		}
	}

	public void setGames(String fileName) {
		int[][] position = new int[6][5];
		int gameNum = 0;// 当前局数
		int carNum = 0;// 当前哪辆小车
		int allGameNum = 0;
		allGames.clear();
		try {
			FileInputStream fileInputStream = new FileInputStream(fileName);
			InputStreamReader inputStreamReader = new InputStreamReader(
					fileInputStream);
			BufferedReader br = new BufferedReader(inputStreamReader);
			String lineString = br.readLine();// 从文件里面读出一行

			while (lineString != null) {
				String[] arrayString = lineString.split(" ");
				int temp = arrayString.length;// temp=5
				for (int i = 0; i < temp; i++) {
					position[carNum][i] = Integer.parseInt(arrayString[i]);// 每读一行就加到position[carNum][]里面去
				}
				if (carNum < 5) {
					carNum++;
				} else if (carNum == 5) {// 读完6行，即一轮
					Game newGame = new Game();
					carNum = 0;
					newGame.setCars(position);
					//if()
					int[] tempblock = new int[3];
					tempblock = newGame.getCarList().get(0).getBlocks();
					allGames.add(newGame);
					gameNum++;
				}
				lineString = br.readLine();// 继续从文件里面读
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		allGameNum = gameNum;// 总的局数
	}

	public static void main(String[] args) {
		
		MyPanel myPanel = new MyPanel();
		MyJFrame jFrame = new MyJFrame("Rush Hour",myPanel);
		jFrame.setBounds(0, 0, 850, 800);
		jFrame.setDefaultCloseOperation(jFrame.EXIT_ON_CLOSE);
		jFrame.setVisible(true);
		
		
		//menu
//		JMenuBar jMenuBar=new JMenuBar();
//		JMenu startJMenu=new JMenu("start");
//		JMenuItem jMenuItem1=new JMenuItem("new game");
//		jMenuItem1.addActionListener(myPanel.new SkipHandler());
//		startJMenu.add(jMenuItem1);
//		jMenuBar.add(startJMenu);
//		jFrame.setJMenuBar(jMenuBar);
		
		jFrame.add(myPanel);
		myPanel.setSize(700, 700);
		myPanel.setLocation(150, 100);
		myPanel.setLayout(null);

		JButton skipButton = new JButton("Skip");
		JButton undoButton = new JButton("Undo");
		JButton resetButton = new JButton("Reset");
		myPanel.add(skipButton);
		myPanel.add(undoButton);
		myPanel.add(resetButton);
		skipButton.setBounds(50, 500, 100, 20);
		undoButton.setBounds(50, 400, 100, 20);
		resetButton.setBounds(50, 300, 100, 20);
		skipButton.addActionListener(myPanel.new SkipHandler());
		undoButton.addActionListener(myPanel.new UndoHandler());
		resetButton.addActionListener(myPanel.new ResetHandler());
		           	
		
//		
//		//option
//		int result=JOptionPane.showConfirmDialog(null, 
//				"start a new game?" , "you win",  JOptionPane.CANCEL_OPTION);
//		System.out.println("resultttttttttttttt:"+result);
//

		myPanel.initialize();
	}
	
/****************事件监听器********************/
	class NewGame implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.out.println("initialize");
			initialize();
		}
	}
	class SkipHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.out.println("press skip");
			gotoNextGame();
		}
	}

	class UndoHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.out.println("press undo");
			if (currentGameOperations.size() > 0) {
				totalSteps--;
//				System.out.println("operation size:"+currentGameOperations.size());
//				System.out.println("operation size:"+currentOperation);
				Operation lastOperation = currentGameOperations
						.get(currentOperation - 1);
				currentGameOperations.remove(currentOperation - 1);
				currentOperation--;
				System.out.println("currentOperation(after minus):" + currentOperation);
				int lastCar = lastOperation.getSelectCar();
				char lastMove = lastOperation.getPressMove();
				char backMove = ' ';
				switch (lastMove) {
				case 'w':
					backMove = 's';
					break;
				case 'a':
					backMove = 'd';
					break;
				case 's':
					backMove = 'w';
					break;
				case 'd':
					backMove = 'a';
					//System.out.println("choose moveback left");
					break;
				default:
					break;
				}
				int i;
				int[] temp = new int[3];
				temp[2] = 36;
				int[] blocks = new int[3];
				blocks = allGames.get(currentGame).getCarList().get(lastCar - 1)
						.getBlocks();
				// System.out.println("lastcar num:"+lastCar);
				int len;
				if (blocks[2] > 35) {
					len = 2;
				} else{
					len = 3;
					}
				//System.out.println("car len:" + len);
				if (backMove == 'w') {// up
					if (true) {// must movable
						i = 0;
						while (i < len) {
							temp[i] = blocks[i] - 6;
							i++;
						}
						// change operations
					} else
						return;

				} else if (backMove == 'a') {// left
					System.out.println("moving back left");
//					System.out.println("block:");
//					System.out.println(blocks[0]);
//					System.out.println(blocks[1]);
//					System.out.println(blocks[2]);
					if (true) {
						i = 0;
						while (i < len) {
							temp[i] = blocks[i] - 1;
							i++;
						}
					} else
						return;

				} else if (backMove == 's') {// down
					if (true) {// 小车竖直方向
						i = 0;
						while (i < len) {
							temp[len - i - 1] = blocks[len - i - 1] + 6;
							i++;
						}
					} else
						return;

				} else if (backMove == 'd') {// right
					if (true) {
						System.out.println("moving right backkkkkkkkk,len:"+len);
						i = 0;
						while (i < len) {
							temp[i] = blocks[i] + 1;
							i++;
						}
//						System.out.println("Now temp[]:");
//						System.out.println(temp[0]);
//						System.out.println(temp[1]);
//						System.out.println(temp[2]);
					} else
						return;

				} else
					return;
				if (true) {
					// update isFree,cars
					for (i = 0; i < len; i++) {
						isFree[blocks[i]] = true;
					}
					for (i = 0; i < len; i++) {
						isFree[temp[i]] = false;
					}
					//System.out.println("isFree[14]:" + isFree[14]);
					blocks = temp;
					allGames.get(currentGame).getCarList().get(lastCar - 1)
							.setBlocks(blocks);//修改上辆小车的blocks，不是当前小车的！
				}
				// allGames.get(currentGame).setCarList(carsArrayList);
				repaint();
				System.out.println("undo:backmove over here");
			}else{
				System.out.println("cannot move back");
			}
			currentCar=1;
			System.out.println("undohandler over");
			MyPanel.this.requestFocus();
			return;
		}
	}

	class ResetHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			totalSteps=0;
			System.out.println("press reset");
			System.out.println("initial list1:");
			printCarList(initialCars);
//			ArrayList<Car> tempCarList=new ArrayList<Car>();
//			tempCarList=allGames.get(currentGame).
			allGames.get(currentGame).setCarList(initialCars);
			for(int i=0;i<36;i++){
				isFree[i]=true;
			}
			System.out.println("initial list2:");
			printCarList(initialCars);
			System.out.println("the list:");
			printCarList(allGames.get(currentGame).getCarList());
			currentOperation=0;
			currentGameOperations.clear();
			repaint();
			System.out.println("reset over");
			MyPanel.this.requestFocus();
		}
	}

	// lines
	public void paint(Graphics g) {
		super.paint(g);
		for (int i = 0; i < 7; i++) {
			int x1 = 200;
			int y1 = 10 + 100 * i;
			g.drawLine(x1, y1, x1 + 600, y1);
		}
		for (int i = 0; i < 7; i++) {
			int x1 = 200 + 100 * i;
			int y1 = 10;
			g.drawLine(x1, y1, x1, y1 + 600);
		}
		
		g.drawString("steps:"+totalSteps, 100,100);

		drawCars(allGames.get(currentGame).getCarList(), g);
	}
/*****************画小车*****************/
	public void drawCars(ArrayList<Car> cars, Graphics g) {// draw cars by
															// ArrayList<cCar>
		System.out.println("drawing cars:");
		printCarList(cars);
		for (int i = 0; i < 6; i++) {
			Icon icon = new ImageIcon("src/abc.png");// ?why
			Image image = Toolkit.getDefaultToolkit().getImage("src/abc.png");
			boolean vertical = cars.get(i).isVertical();
			boolean visible = cars.get(i).isVisible();
			int[] blocks = cars.get(i).getBlocks();
			int num;// 小车长度
			if (blocks[2] > 35) {
				num = 2;
			} else {
				num = 3;
			}
			int x0 = 200;
			int y0 = 10;
			int m = 100;
			if (visible) {
				if (vertical) {
					g.drawImage(image, (blocks[0] % 6) * m + x0,
							(blocks[0] / 6) * m + y0, m, m * num, null, null);
				} else {
					g.drawImage(image, (blocks[0] % 6) * m + x0,
							(blocks[0] / 6) * m + y0, m * num, m, null, null);
				}

				for (int j = 0; j < num; j++) {
					isFree[blocks[j]] = false;
				}

			} else {
			}// invisible

		}
		System.out.println("draw cars over");

	}
/********************键盘事件**********************/
	public MyPanel() { // 注册监听器
		addKeyListener(this);
	}

	public boolean isFocusTraversable() { // 允许面板获得焦点 //?why
		return true;
	}

	public void keyTyped(KeyEvent e) {}

	public void keyPressed(KeyEvent e) {}

	public void keyReleased(KeyEvent e) {
		System.out.println("keyyyyyyyyy");
		char KeyChar = e.getKeyChar();
		// 123456
		if (KeyChar >= '1' && KeyChar <= '6') {
			System.out.println("press 123456");
			switch (KeyChar) {
			case '1':
				currentCar = 1;
				break;
			case '2':
				currentCar = 2;
				break;
			case '3':
				currentCar = 3;
				break;
			case '4':
				currentCar = 4;
				break;
			case '5':
				currentCar = 5;
				break;
			case '6':
				currentCar = 6;
				break;
			default:
				break;
			}
		}

		// wasd
		else if (KeyChar == 'w' || KeyChar == 'a' || KeyChar == 's'
				|| KeyChar == 'd') {
			System.out.println("press wasd");
			// carsArrayList = allGames.get(currentGame).getCarList();
			boolean vertical = allGames.get(currentGame).getCarList()
					.get(currentCar - 1).isVertical();
			int[] blocks = allGames.get(currentGame).getCarList()
					.get(currentCar - 1).getBlocks();
			int len;
			if (blocks[2] > 35) {
				len = 2;
			} else {
				len = 3;
			}
			int i;
			int[] temp = { 0, 1, 36 };
			boolean movable = true;
			if (KeyChar == 'w') {// up
				if (vertical && blocks[0] / 6 > 0 && isFree[blocks[0] - 6]) {// 小车竖直方向
					i = 0;
					while (i < len) {
						temp[i] = blocks[i] - 6;
						i++;
					}
					// change operations
					Operation newOperation = new Operation(currentCar, KeyChar);
					currentGameOperations.add(newOperation);
					currentOperation++;
				} else
					return;

			} else if (KeyChar == 'a') {// left
				if (!vertical && blocks[0] % 6 > 0 && isFree[blocks[0] - 1]) {
					i = 0;
					while (i < len && movable) {
						temp[i] = blocks[i] - 1;
						i++;
					}
					// change operations
					Operation newOperation = new Operation(currentCar, KeyChar);
					currentGameOperations.add(newOperation);
					currentOperation++;
					System.out.println("currentOperation(after add):"+currentOperation);
				} else
					return;

			} else if (KeyChar == 's') {// down
				if (vertical && blocks[len - 1] / 6 < 5
						&& isFree[blocks[len - 1] + 6]) {// 小车竖直方向
					i = 0;
					while (i < len) {
						temp[len - i - 1] = blocks[len - i - 1] + 6;
						i++;
					}
					// change operations
					Operation newOperation = new Operation(currentCar, KeyChar);
					currentGameOperations.add(newOperation);
					currentOperation++;
				} else
					return;

			} else if (KeyChar == 'd') {// right//maybe win
				if(currentCar==1&&blocks[1]==17){//success
					System.out.println("success!!!!!!!!!");
					gotoNextGame();return;
				}
				System.out.println("press ddddddddddddddd");
				if (!vertical && blocks[len - 1] % 6 < 5
						&& isFree[blocks[len - 1] + 1]) {
					i = 0;
					while (i < len && movable) {
						temp[i] = blocks[i] + 1;
						i++;
					}
					// change operations
					Operation newOperation = new Operation(currentCar, KeyChar);
					currentGameOperations.add(newOperation);
					currentOperation++;
					System.out.println("operationnnnnnnnnnnnn"
							+ currentGameOperations.size());
				} else
					return;

			} else
				return;
			
			if (movable) {
				
				totalSteps++;
				// update isFree,cars
				for (i = 0; i < len; i++) {
					isFree[blocks[i]] = true;
				}
				for (i = 0; i < len; i++) {
					isFree[temp[i]] = false;
				}
				blocks = temp;
				allGames.get(currentGame).getCarList().get(currentCar - 1)
						.setBlocks(blocks);
			}
			// allGames.get(currentGame).setCarList(carsArrayList);
			repaint();
		}
	}
	
}

class MyJFrame extends JFrame {
	public MyJFrame(String string,MyPanel myPanel) {
		// TODO Auto-generated constructor stub
		super(string);
		
		JMenuBar jMenuBar=new JMenuBar();
		JMenu startJMenu=new JMenu("start");
		JMenuItem jMenuItem1=new JMenuItem("new game");
		jMenuItem1.addActionListener(myPanel.new SkipHandler());
		startJMenu.add(jMenuItem1);
		jMenuBar.add(startJMenu);
		this.setJMenuBar(jMenuBar);
		
	}
}
