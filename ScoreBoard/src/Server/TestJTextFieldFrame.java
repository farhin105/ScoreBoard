package Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class TestJTextFieldFrame extends JFrame implements ActionListener
{
	JLabel label1;
	JTextField tf;
	JLabel label2;
	JTextField pf;
	JButton jb2;
	int test;
	Server server;

	public TestJTextFieldFrame(Server server)
	{
		super("Simple Frame");
		label1=new JLabel("Team 1");
		tf=new JTextField(20);
		label2=new JLabel("Team 2");
		pf=new JTextField(20);

		jb2=new JButton("Start");
		this.server=server;
		
		Container c=getContentPane();
		c.setLayout(new FlowLayout());
		c.add(label1);	
		c.add(tf); 
		c.add(label2);		
		c.add(pf); 
			
		c.add(jb2);
		jb2.addActionListener(this);

		test=0;

		setSize(250,250);		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation(300,300);		
		setVisible(true);
		
	}


	public void actionPerformed(ActionEvent ae)
	{
		if(ae.getSource()==jb2)
		{
			//JOptionPane.showMessageDialog(null, tf.getText() + "@" + pf.getText());
			//this.setVisible(false);
			//LabelFrame2 l2 = new LabelFrame2();
			//l2.setVisible(true);
			//while(true) {
				String tm1 = tf.getText();
				String tm2 = pf.getText();

				String ww=Integer.toString(test);
				Match m=new Match(ww,tm1,tm2);

				server.matchArrayList.add(m);
				System.out.println("adding-------"+server.matchArrayList.size()+"no. match");

			    test++;
			//}
		}
	}
}