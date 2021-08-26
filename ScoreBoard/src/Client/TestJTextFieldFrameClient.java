package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class TestJTextFieldFrameClient extends JFrame implements ActionListener
{
	private static Socket s = null;
	private static BufferedReader br = null;
	private static PrintWriter pr = null;
	private static SimpleClient simpleClient;
	public static String SID;
	int maxSub;

	JLabel label1;
	JTextField id;
	JLabel label2;
	JTextField ip;
	JLabel label3;
	JTextField port;
	JButton jbc;
	Container c=getContentPane();
	JTextField clientField;
	JTextArea serverarea;
	JLabel clientLabel;
	JButton sendButton;

	public TestJTextFieldFrameClient(SimpleClient simpleClient)
	{
		super("Client Frame");
		label1=new JLabel("Student ID");
		id=new JTextField(20);
		label2=new JLabel("IP");
		ip=new JTextField(20);
		label3=new JLabel("PORT");
		port=new JTextField(20);
                serverarea = new JTextArea(10, 20);
			clientLabel = new JLabel("client");
			clientField = new JTextField(20);
			sendButton = new JButton("Send");
			sendButton.addActionListener(this);

		jbc=new JButton("OK");

		this.simpleClient=simpleClient;
		c.setLayout(new FlowLayout());
		c.add(label1);
		c.add(id);
		c.add(label2);
		c.add(ip);
		c.add(label3);
		c.add(port);
			
		c.add(jbc);
		jbc.addActionListener(this);

		setSize(250,250);		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation(300,300);		
		setVisible(true);
		
	}

	public void actionPerformed(ActionEvent ae)
	{
		if(ae.getSource()==jbc) {
			String strSend = null, strRecv = null;
			SID = id.getText();
			String IP = ip.getText();
			String PORT = port.getText();

			try {
				s = new Socket(IP, Integer.parseInt(PORT));

				br = new BufferedReader(new InputStreamReader(s.getInputStream()));
				pr = new PrintWriter(s.getOutputStream());
				//----------------------sending the ID----------------------
				try {
					strSend=SID+","+IP;
					//strSend = id.getText();
					pr.println(strSend);
					pr.flush();
					System.out.println(strSend);

				} catch (Exception e) {
					System.out.println("Exception occured");
				}

			} catch (Exception e) {
				System.err.println("Problem in connecting with the server. Exiting main.");
				System.exit(1);
			}

			strSend = null;
			//-----------------------------the page after login--------------------
			
			c.removeAll();
			c.setLayout(new FlowLayout());
			c.add(serverarea);
			//c.add(clientLabel);
			c.add(clientField);
			c.add(sendButton);

			System.out.println("here");
			//
			setSize(300, 300);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setLocation(300, 300);
			setVisible(true);
			c.revalidate();
			c.repaint();
			c.setVisible(true);
			c.setLocation(200, 200);

			ClientToReadThread clientRead=new ClientToReadThread(s,br,pr,this);
			Thread t = new Thread(clientRead);
			t.start();
		}
		//-----------------send Button-----------------------------------------------------
		if(ae.getSource()==sendButton) {
			String strRecv = null, strSend1 = clientField.getText(),strSend=null;
			strSend=strSend1.trim().replaceAll("\\s+", "");
			String chkLimit[];
			chkLimit = strSend.split(",");
			for(int i=0;i<chkLimit.length;i++){
				chkLimit[i]=chkLimit[i].trim();
			}
			if (chkLimit.length > maxSub) {
					serverarea.append("You have subscribed maches more than limit");
			}
			else {
			//--------------------------------------------------------------------------------------
			try {
				System.out.println("before");
				//strSend = clientField.getText();
				strSend1 = clientField.getText();strSend=null;
				strSend=strSend1.trim().replaceAll("\\s+", "");
				System.out.println("after");
			} catch (Exception e) {
				//continue;
			}
			//-------------------ok lets see---------------------------
			pr.println(strSend);
			System.out.println("client requesting for :" + strSend);
			pr.flush();

			strRecv = null;
			try {
				strRecv = br.readLine();
				if (strRecv != null) {
					System.out.println("Server says: " + strRecv);
					serverarea.append("Server says: " + strRecv);
				} else {
					System.err.println("Error in reading from the socket. Exiting main.");
					cleanUp();
					System.exit(0);
				}
			} catch (Exception e) {
				System.err.println("Error in reading from the socket. Exiting main.");
				cleanUp();
				System.exit(0);
			}
			//fname
			int count = 0;
			while (true) {
				try {
					strRecv = br.readLine();                    //These two lines are used to determine
					int filesize = Integer.parseInt(strRecv);        //the size of the receiving file
					byte[] contents = new byte[256];

					String FName = SID + Integer.toString(count) + ".txt";
					System.out.println("-----------file name" + FName + "  for count:" + count + " and this is:" + strRecv);
					count++;
					FileOutputStream fos = new FileOutputStream(FName);
					//FileOutputStream fos = new FileOutputStream("file1.txt");
					BufferedOutputStream bos = new BufferedOutputStream(fos);
					InputStream is = s.getInputStream();

					int bytesRead = 0;
					int total = 0;            //how many bytes read

					while (total != filesize && total < filesize)    //loop is continued until received byte=totalfilesize
					{
						bytesRead = is.read(contents);
						total += bytesRead;
						bos.write(contents, 0, bytesRead);
						System.out.println("in while(total!=filesize) loop,total=" + total + " byteRead=" + bytesRead + " fileSize" + filesize);
						try {
							strSend=Integer.toString(total);
						} catch (Exception e) {
							//continue;
						}
						System.out.println("sending---------"+strSend);
						pr.println(strSend);
						pr.flush();
					}
					System.out.println("1.my count is now" + count);
					bos.flush();
					System.out.println("2.my count is now" + count);
				} catch (Exception e) {
					System.err.println("Could not transfer file.");
				}
				System.out.println("3.my count is now" + count);
				//sleepThread sleep=new sleepThread();
				//Thread t=new Thread(sleep);
				//t.start();
				System.out.println("4.my count is now" + count);
			}
		}	//cleanUp();
		}
	}

	private static void cleanUp()
	{
		try
		{
			br.close();
			pr.close();
			s.close();
		}
		catch(Exception e)
		{

		}
	}

}
class ClientToReadThread implements Runnable{
    private static Socket s = null;
	private static BufferedReader br = null;
	private static PrintWriter pr = null;
	//private static SimpleClient simpleClient;
	public static String SID;
        TestJTextFieldFrameClient tc;
        
    ClientToReadThread(Socket s,BufferedReader br, PrintWriter pr,TestJTextFieldFrameClient tc){
        this.s=s;
        this.br=br;
        this.pr=pr;
        this.tc=tc;
    }
    public void run(){
        String strRecv=null;
        try {
				strRecv = br.readLine();
				if (strRecv != null) {
					String [] chk;
					chk=strRecv.split(",");
					String []mSub;
					mSub=strRecv.split("=");
					tc.maxSub=Integer.parseInt(mSub[1]);
					for(int i=0;i<chk.length;i++)
					{
						System.out.println("\n"+ chk[i]);
						tc.serverarea.append(chk[i] + "\n");
					}

				} else {
					System.err.println("Error in reading from the socket. Exiting main.");
					cleanUp();
					System.exit(0);
				}
			} catch (Exception e) {
				System.err.println("Error in reading from the socket. Exiting main.");
				cleanUp();
				System.exit(0);
			}
    }
    
    private static void cleanUp()
	{
		try
		{
			br.close();
			pr.close();
			s.close();
		}
		catch(Exception e)
		{

		}
	}
}

class sleepThread implements Runnable{
	public void run(){
		try{

			Thread.sleep(10000);
		}catch(InterruptedException ex){
			System.out.println("Interupting sleep thread");
		}
	}

}

