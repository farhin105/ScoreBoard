package Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


class Clients{
    String IP;
    String roll_no;
    boolean active;
    String [] subscribedMatches;
    
    boolean getActive(){
        return active;
    }
    
}

public class Server extends JFrame implements ActionListener
{
    JLabel label1;
    JTextField tf;
    JLabel label2;
    JTextField pf;
    JButton jb;

    ArrayList<Match>matchArrayList;
    Clients [] client;
    static int workerThreadCount=0;
    static int id=1;
    Container c=getContentPane();
    JTextArea area;
    JButton jb1;

    JLabel label3;
    JTextField team1;
    JLabel label4;
    JTextField team2;
    JButton jb2;
    int test=0;
    int numberOfMatches;
    Container c_add= getContentPane();

    public Server()
    {
        super("Server");
        label1=new JLabel("Maximum Number of Matches");
        tf=new JTextField(20);
        label2=new JLabel("Student ID ");
        pf=new JTextField(20);

        jb=new JButton("Set");
        area=new JTextArea(5,20);

        //Container c=getContentPane();
        c.setLayout(new FlowLayout());
        c_add.setLayout(new FlowLayout());
        c.add(label1);
        c.add(tf);
        c.add(label2);
        c.add(pf);

        c.add(jb);
        jb.addActionListener(this);

        //test=0;

        setSize(250,250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocation(300, 300);
        setVisible(true);

        matchArrayList=new ArrayList<Match>();

    }
    boolean onlyIDValid(String roll){
        for(int i=0;i<client.length;i++){
            if(client[i].roll_no.equals(roll)){
                if(client[i].getActive()==true){
                    return true;
                }
                else return false;
            }
        }
        return false;
    }
    boolean isValid(String roll){
        for(int i=0;i<client.length;i++){
            if(client[i].roll_no.equals(roll)){
                if(client[i].getActive()==false){
                    return true;
                }
                else return false;
            }
        }
        return false;
    }
    void setIPActive(String roll,String IP){
        for(int i=0;i<client.length;i++){
            if(client[i].roll_no.equals(roll)){
                client[i].active=true;
                client[i].IP=IP;
            }
        }
    }
    int getIndex(String roll){
        for(int i=0;i<client.length;i++){
            if(client[i].roll_no.equals(roll)){
                return i;
            }
        }
        return -1;
    }
    public void actionPerformed(ActionEvent ae)
    {
        if(ae.getSource()==jb)
        {
            String str1="";

            String number = tf.getText();
            numberOfMatches=Integer.parseInt(number);
            String S_ids = pf.getText();
            Pattern pattern1 = Pattern.compile("[0-9]+[,0-9]*");
            Pattern pattern2 = Pattern.compile("[0-9]+\\-[0-9]+");
            Matcher matcher1 = pattern1.matcher(S_ids);
            Matcher matcher2 = pattern2.matcher(S_ids);
            String [] str;
            if (matcher1.matches()==true){
                str=S_ids.split(",");
                client=new Clients[str.length];
                for(int i=0;i<str.length;i++){
                    client[i]=new Clients();
                    client[i].active=false;
                    client[i].roll_no=str[i];
                    //client[i].subscribedMatches= new String[numberOfMatches];
                }
            }
            else if(matcher2.matches()==true)
            {
                str=S_ids.split("-");
                int j1=Integer.parseInt(str[0]);
                int j2=Integer.parseInt(str[1]);
                client=new Clients[j2-j1+1];
                for(int i=0;i<j2-j1+1;i++){
                    client[i]=new Clients();
                    client[i].active=false;

                    client[i].roll_no=Integer.toString(j1+i);
                }

            }

            c.removeAll();
            //area=new JTextArea("Server");
            c.add(area);
            //jb1=new JButton("Add Match");
            //c.add(jb1);
            label3=new JLabel("Team 1");
            team1=new JTextField(20);
            label4=new JLabel("Team 2 ");
            team2=new JTextField(20);

            jb2=new JButton("Set");

            c.add(label3);
            c.add(team1);
            c.add(label4);
            c.add(team2);
            c.add(jb2);
            jb2.addActionListener(this);
            //
            c.revalidate();
            c.repaint();
            c.setVisible(true);
            c.setLocation(150, 200);

            MainThread mt=new MainThread(this,matchArrayList);
            Thread t=new Thread(mt);
            t.start();


            //}
        }
        if(ae.getSource()==jb1){
            // new TestJTextFieldFrame(this);
            System.out.println("in add match button");
            label3=new JLabel("Team 1");
            team1=new JTextField(20);
            label4=new JLabel("Team 2 ");
            team2=new JTextField(20);

            jb2=new JButton("Set");
            c_add.add(label3);
            c_add.add(team1);
            c_add.add(label4);
            c_add.add(team2);

            c_add.add(jb2);
            jb2.addActionListener(this);

            c_add.revalidate();
            c_add.repaint();

            c_add.setSize(250, 250);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            c_add.setLocation(100, 100);
            c_add.setVisible(true);

        }


        if(ae.getSource()==jb2)
        {
            System.out.println("here");
            String tm1 = team1.getText();
            String tm2 = team2.getText();
            JOptionPane.showMessageDialog(null,"Start match:"+ tm1 + "Vs" + tm2);

            String ww=Integer.toString(test);
            test++;
            Match m=new Match(ww,tm1,tm2);

            matchArrayList.add(m);
            System.out.println("adding-------"+matchArrayList.size()+"no. match");
        }
    }

    public  static void main(String[]args){
        new Server();
    }
}


class MainThread implements  Runnable{
    Server server;
    Socket s;
    ArrayList<Match> matches=new ArrayList<Match>();
    MainThread(Server server,ArrayList<Match>matches){
        this.server=server;
        //this.socket=socket;
        this.matches=matches;
    }
    public void run(){
        try
        {
            ServerSocket ss = new ServerSocket(5567);
            server.area.append("Server has been started successfully.\n");

            while(true)
            {
                server.area.append(" No. of worker threads = " + server.workerThreadCount+"\n");
                s = ss.accept();		//TCP Connection
                WorkerThread wt = new WorkerThread(s, server.id,this);
                Thread t = new Thread(wt);
                t.start();
                server.workerThreadCount++;
                //server.area.append("Client [" + server.id + "] is now connected. No. of worker threads = " + server.workerThreadCount+"\n");
                //server.area.append("Student ID"+server.client[server.id-1].roll_no+"  IP:"+server.client[server.id-1].IP+"  is now connected.No. of worker threads = " + server.workerThreadCount+"\n");
                server.id++;


            }
        }
        catch(Exception e)
        {
            System.err.println("Problem in ServerSocket operation. Exiting main.");
        }
    }

}
