package Client; /**
 * Created by User on 3/9/2017.
 */

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SimpleClient {
    private static Socket s = null;
    private static BufferedReader br = null;
    private static PrintWriter pr = null;
    private static String rollno = null;

    SimpleClient() {
        new TestJTextFieldFrameClient(this);
    }

    SimpleClient(String rollnum) {
        this.rollno = rollnum;
    }

    public static void main(String args[]) {
        new SimpleClient();

    }
}
