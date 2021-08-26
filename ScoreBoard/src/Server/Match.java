package Server;

import java.util.Random;

/**
 * Created by User on 3/10/2017.
 */


class NewThread implements Runnable {
    String name; // name of thread
    Thread t;

    int totalRun1;
    int totalRun2;
    int w1;
    int w2;
    Match match;


    NewThread(String threadName,int totalRun1,int totalRun2,int w1, int w2, Match match) {
        name = threadName;

        this.totalRun1=totalRun1;
        this.totalRun2=totalRun2;
        this.w1=w1;
        this.w2=w2;
        this.match=match;
        t = new Thread(this, name);
        t.start(); // Start the thread
    }

    public void run() {
        try {
            match.forFile+=match.MatchName+":"+match.team1+": innings1\n";
            int k=0;
            for(int i = 0; i < 20 && w1<9 ; i++) {
                System.out.print("\ninnings1  over:"+(i+1) + ": ");
                match.forFile+=match.MatchName+":"+match.team1+":\ninnings 1: over :"+Integer.toString((i+1))+":  ";
                int runofOver=0,wicketOfOver=0;
                for(int j=0;j<6&& w1<9;j++)
                {
                    Random rand = new Random();
                    int run = rand.nextInt(8);
                    String runString=Integer.toString(run);
                    if(run==7){run=0;w1++;runString="out";wicketOfOver++;}
                    if(run==5){run=0;}
                    match.innings1[i][j]=runString;
                   // match.innings[k][j]=runString;
                    k++;
                    match.forFile+=runString+"  , ";
                    totalRun1=totalRun1+run;
                    runofOver+=run;
                    System.out.print(name + ":" + (i + 1) + " :" + runString + "(" + run + ")" + " , ");
                    //Thread.sleep(3000);
                }
                Thread.sleep(5000);
                match.forFile+=" ->total run of innings1,over:"+Integer.toString((i+1))+Integer.toString(runofOver)+", wickets:"+Integer.toString(wicketOfOver);
                match.TforFile=match.forFile;
                System.out.println();
            }
            for(int i = 0; i < 20 && w2<9; i++) {
                System.out.print("innings2  over:"+(i+1) + ": ");
                match.forFile+=match.MatchName+":"+match.team1+":\ninnings 2: over :"+Integer.toString((i+1))+":  ";
                int runofOver=0,wicketOfOver=0;
                for(int j=0;j<6 && w2<9;j++)
                {
                    Random rand = new Random();
                    int run = rand.nextInt(8);
                    String runString=Integer.toString(run);
                    if(run==7){run=0;w2++;runString="out";wicketOfOver++;}
                    if(run==5){run=0;}
                    match.innings2[i][j]=runString;
                   // match.innings[k][j]=runString;
                    k++;
                    match.forFile+=runString+"  , ";
                    totalRun2=totalRun2+run;
                    runofOver+=run;
                    System.out.print(name + ":" + (i + 1) + " :" + runString + "(" + run + ")" + " , ");
                    //Thread.sleep(3000);
                }Thread.sleep(5000);
                match.forFile+=" ->total run of innings2,over:"+Integer.toString((i+1))+Integer.toString(runofOver)+", wickets:"+Integer.toString(wicketOfOver);
                match.TforFile=match.forFile;
                System.out.println();
            }

            if(totalRun1>totalRun2){
                String s=Integer.toString(totalRun1-totalRun2);
                match.setStatus(match.team1+" won by "+s+" run");
                match.TforFile+=match.team1+" won by "+s+" run";
               // match.innings[k][1]=match.team1+" won by "+s+" run";
            }
            else if(totalRun2>totalRun1){
                String s=Integer.toString((10-w2));
                match.setStatus(match.team2+" won by "+s+" wicket");
                match.TforFile+=match.team2+" won by "+s+" wicket";
                //match.innings[k][1]=match.team2+" won by "+s+" wicket";
            }
            else {
                match.setStatus("draw");
                match.TforFile+="draw";

                //match.innings[k][1]="draw"
            }
            match.isRunning=false;

        } catch (InterruptedException e) {
            System.out.println(name + "Interrupted");
        }
        System.out.println(name + " exiting.");
    }
}

public class Match {
    String name;
    //int innings1[][];
    //int innings2[][];
    String innings1[][];
    String innings2[][];
    String innings[][];
    String team1;
    String team2;
    String MatchName;
    String Status;
    boolean isRunning;
    int totalRunTeam1;
    int totalRunTeam2;
    int wicket1;
    int wicket2;
    String forFile;
    String TforFile;

    Match(String name,String team1,String team2)
    {
        this.name=name;
        innings1 = new String[20][6];
        innings2 = new String[20][6];
        innings = new String[21][6];
        this.team1=team1;
        this.team2=team2;
        this.MatchName=team1+"Vs"+team2;
        setStatus("running");
        this.totalRunTeam1=0;
        this.totalRunTeam2=0;
        this.wicket1=0;
        this.wicket2=0;
        this.forFile="";
        this.TforFile="";
        this.isRunning=true;
        new NewThread(MatchName,totalRunTeam1,totalRunTeam2,wicket1,wicket2,this);
        System.out.println(team1+"="+totalRunTeam1+" , "+team2+"="+totalRunTeam2+"------------"+getStatus()+"--------");

    }

    void setStatus(String status){
        this.Status=status;
    }
    String getStatus(){
        return this.Status;
    }
}
