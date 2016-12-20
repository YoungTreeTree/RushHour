import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MyJFrame extends JFrame {
	public MyJFrame(String string) {
		// TODO Auto-generated constructor stub
		super(string);
		JMenuBar jMenuBar=new JMenuBar();
		JMenu startJMenu=new JMenu("start");
		JMenuItem jMenuItem1=new JMenuItem("new game");
		startJMenu.add(jMenuItem1);
		jMenuBar.add(startJMenu);
		this.setJMenuBar(jMenuBar);
	}
}
