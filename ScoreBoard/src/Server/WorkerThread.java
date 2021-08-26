package Server;

/**
 * Created by User on 3/13/2017.
 */

import java.io.*;
import java.net.Socket;
import java.util.Arrays;


class WorkerThread implements Runnable
{
    private Socket socket;
    private InputStream is;
    private OutputStream os;
    //private Server server;
    private MainThread mainThread;

    private int id = 0;

    public WorkerThread(Socket s, int id,MainThread mainThread)
    {
        this.socket = s;
        //this.server=server;
        this.mainThread=mainThread;
        try
        {
            this.is = this.socket.getInputStream();
            this.os = this.socket.getOutputStream();
        }
        catch(Exception e)
        {
            System.err.println("Sorry. Cannot manage client [" + id + "] properly.");
        }

        this.id = id;
    }

    public void run()
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(this.is));
        PrintWriter pr = new PrintWriter(this.os);
        String str=null;
        String rollNo=null;
        String IPadd=null;
        try {
            str=br.readLine();
            System.out.println(str);
            String [] IDandIP;
            IDandIP=str.split(",");
            str=IDandIP[0];
            rollNo=str;
            IPadd=IDandIP[1];
            System.out.println("So now str="+str+"  roll="+rollNo+"  IP="+IPadd);
            String matchlistString="";
            for(int i=0;i<mainThread.matches.size();i++)
            {
                //if(i!=0)matchlistString +=",";
                matchlistString += ","+mainThread.matches.get(i).MatchName;
            }
            if(mainThread.server.isValid(str)){
                mainThread.server.setIPActive(str,IPadd);
                mainThread.server.area.append("Student ID:"+rollNo+"  IP: "+IPadd+" is connected");
                //pr.println("Your id is: " + this.id + "and student_id is:" + str+" matches:" + mainThread.matches.size()+matchlistString);
                pr.println("Student_id: " + str+" NO. of matches:" + mainThread.matches.size()+matchlistString+", you can subscribe matches maximum ="+mainThread.server.numberOfMatches);
                System.out.println("number of matches: " + mainThread.matches.size()+matchlistString);
                pr.flush();
            }
            //else if(mainThread.server.onlyIDValid(str)){}
            else{
                pr.println("already active");
                pr.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
       //---------------------------to receive subscription list------------------------------
       try {
            str=br.readLine();
            System.out.println(str);
            String [] subscribed;
            subscribed=str.split(",");
            if(subscribed.length>mainThread.server.numberOfMatches)
            {
                pr.println("you have subscribed more matches");
                System.out.println("you have subscribed more matches");
                pr.flush();
            }
            else{
                int index=mainThread.server.getIndex(rollNo);
                //pr.println("your subscribed matches: ");
                System.out.println("---------------------"+index);
                String aString=null;
                mainThread.server.client[index].subscribedMatches=subscribed;
                for(int i=0;i<subscribed.length;i++){
                    System.out.println("------*******-----"+mainThread.server.client[index].subscribedMatches[i]);
                }

                pr.println("you have subscribed "+aString+" matches");
                pr.flush();
                sendFile(index,mainThread.server,pr);
            }
          
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    void sendFile(int index,Server server,PrintWriter pr){
        int subMatch=server.client[index].subscribedMatches.length;
        System.out.println("###########################Subscribed matches:"+subMatch);
        int indices[]=new int[subMatch];
        for(int j=0;j<subMatch;j++){
        for(int i=0;i<server.matchArrayList.size();i++){
            System.out.println("-----here is a match: "+server.matchArrayList.get(i).MatchName+"  and client requested for "+server.client[index].subscribedMatches[j]);
            if((server.matchArrayList.get(i).MatchName).matches(server.client[index].subscribedMatches[j])){
                indices[j]=i;
                System.out.println("*********** request matched with index number:"+i+"  which is:"+server.matchArrayList.get(i).MatchName);
            }

        }
        }
        for(int j=0;j<subMatch;j++){
            System.out.println("######################## INdices:"+indices[j]);
        }
        Arrays.sort(indices);
        String prevFile[]=new String[indices.length];
        for(int i=0;i<prevFile.length;i++){
            prevFile[i]="";
        }
        for(int j=0;j<subMatch;j++){
            System.out.println("######################## INdices:"+indices[j]);
        }
        int in=0;
        boolean b=true;
        while(b){
           String forFile1="";
            String forFile2="";
            String forFile3="";
            String totalforFile="";
            for(int i=0;i<subMatch;i++){
                forFile1+="\n\n"+server.matchArrayList.get(indices[i]).MatchName+":\n";
               // for(int j=0;j<6;j++){
                   // forFile1+=server.matchArrayList.get(indices[i]).innings1[in][j]+";";}
                forFile2= server.matchArrayList.get(indices[i]).TforFile;
                forFile3=forFile2.replace(prevFile[i],"");
                prevFile[i]=forFile2;
                System.out.println("forFile2-----"+forFile2);
                System.out.println("forFile3-----"+forFile3);
                System.out.println("prev-----"+prevFile[i]);
                totalforFile+=forFile3;
            }
            try{
            File file = new File("file.txt");
                BufferedWriter bw = null;
		FileWriter fw = null;

		try {

			String content = "This is the content to write into file\n";

			fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
			bw.write(totalforFile);

			System.out.println(forFile1+"---------------------Done");

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}
		}
                        FileInputStream fis = new FileInputStream(file);
                            BufferedInputStream bis = new BufferedInputStream(fis);
                            OutputStream os = socket.getOutputStream();
                            byte[] contents;
                            long fileLength = file.length();
                BufferedReader br=new BufferedReader(new InputStreamReader(this.is));
                            pr.println(String.valueOf(fileLength));		//These two lines are used
                            pr.flush();									//to send the file size in bytes.

                            long current = 0;

                            long start = System.nanoTime();
                            while(current!=fileLength){
                                int size = 256;
                                if(fileLength - current >= size)
                                    current += size;
                                else{
                                    size = (int)(fileLength - current);
                                    current = fileLength;
                                }
                                contents = new byte[size];
                                bis.read(contents, 0, size);
                                os.write(contents);
                                System.out.println("Sending file ... "+(current*100)/fileLength+"% complete!");
                                try {
                                    String sg=br.readLine();
                                    System.out.println("---------got---"+sg);
                                    if(Integer.parseInt(sg)!=current){
                                        System.out.println("chunk not sent");
                                        //------------handle chunk-----------------------
                                        //current=current-256;
                                    }
                                }catch (Exception e){

                                }
                            }
                            os.flush();
                            System.out.println("File sent successfully!");
                        }
                        catch(Exception e)
                        {
                            System.err.println("Could not transfer file.");
                        }
                        pr.println("Downloaded.");
                        pr.flush();
                        in++;
                        int checkBool=0;
                        for(int i=0;i<subMatch;i++){
                            if(server.matchArrayList.get(indices[i]).isRunning==false){
                                checkBool++;
                                System.out.println("****     match"+server.matchArrayList.get(indices[i]).MatchName+" is over and result is"+server.matchArrayList.get(indices[i]).getStatus());
                            }
                        }
                        System.out.println("stopped matches "+checkBool);
                        if(checkBool==subMatch){b=false;
                            System.out.println("stopped matches "+checkBool+" and b is "+b);}
                        sleepThread sleep=new sleepThread();
                        Thread t=new Thread(sleep);
                        t.start();
            try {
                t.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        }
    }
//}
class sleepThread implements Runnable{
    public void run(){
        try{

            Thread.sleep(10000);
        }catch(InterruptedException ex){
            System.out.println("Interupting sleep thread");
        }
    }

        }

