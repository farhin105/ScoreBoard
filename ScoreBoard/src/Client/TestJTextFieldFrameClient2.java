package Client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TestJTextFieldFrameClient2 extends JFrame implements ActionListener
{

	JButton jb;

	public TestJTextFieldFrameClient2()
	{
		super("Client Frame");
		jb=new JButton("Start");
/*
		Container c=getContentPane();
		c.setLayout(new FlowLayout());

		Server.TestServer ts=new Server.TestServer();
		Server.TestJTextFieldFrame t=new Server.TestJTextFieldFrame();
		JButton buttonArray[]=new JButton[t.get_MatchList().length];
		JButton button1Array[]=new JButton[ts.get_matchArrayList().size()];
		JLabel labelArray[]=new JLabel[t.get_MatchList().length];

		System.out.println(t.get_MatchList().length);
		System.out.println(ts.get_matchArrayList().size());
		for(int i=0;i<t.get_MatchList().length && t.get_MatchList()!=null;i++)
		{
			//labelArray[i]=new JLabel(t.get_MatchList()[i].name);
			buttonArray[i]=new JButton("get");

			//c.add(labelArray[i]);
			c.add(buttonArray[i]);
		}
		for(int i=0;i<ts.get_matchArrayList().size();i++)
		{
			//labelArray[i]=new JLabel(t.get_MatchList()[i].name);
			button1Array[i]=new JButton("2ndget");

			//c.add(labelArray[i]);
			c.add(buttonArray[i]);
		}
		c.add(jb);
		jb.addActionListener(this);

*/

		setSize(250,250);		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation(300,300);		
		setVisible(true);
		
	}
	
	public void actionPerformed(ActionEvent ae)
	{
		if(ae.getSource()==jb)
		{
			//JOptionPane.showMessageDialog(null, tf.getText() + "@" + pf.getText());
			//this.setVisible(false);
			//LabelFrame2 l2 = new LabelFrame2();
			//l2.setVisible(true);
			//while(true) {


			//}
		}
	}
	/*public static void main(String args[])
	{
		//new Server.TestJTextFieldFrame();
	}*/
}