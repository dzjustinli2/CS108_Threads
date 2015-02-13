package assign4;

import java.util.concurrent.atomic.AtomicLong;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;

public class WebWorker extends Thread {
	WebFrame wf;
	int index;
	
	public WebWorker(WebFrame program, int linkIndex){
		wf = program;
		index = linkIndex;
	}
	public void run(){
		wf.incrementLabel();
		//This is the core web/download i/o code...
 		InputStream input = null;
		StringBuilder contents = null;
		try {
			URL url = new URL(wf.links.get(index));
			URLConnection connection = url.openConnection();
			// Set connect() to throw an IOException
			// if connection does not succeed in this many msecs.
			connection.setConnectTimeout(5000);
			connection.connect();
			input = connection.getInputStream();
			BufferedReader reader  = new BufferedReader(new InputStreamReader(input));
			char[] array = new char[1000];
			int len;
			contents = new StringBuilder(1000);
			while ((len = reader.read(array, 0, array.length)) > 0) {
				contents.append(array, 0, len);
				Thread.sleep(100);
			}
			// Successful download if we get here
			String line = contents.length() + "";
			wf.setStatusValue(line,index);
			wf.decrementLabel();
			wf.updateProgressBar();
		}
		// Otherwise control jumps to a catch...
		catch(MalformedURLException ignored) {
			String line = "err";
			wf.setStatusValue(line,index);
			wf.decrementLabel();
			wf.updateProgressBar();
		}
		catch(InterruptedException exception) {
			// deal with interruption
			String line = "interruted";
			wf.setStatusValue(line,index);
			wf.decrementLabel();
			wf.updateProgressBar();
		}
		catch(IOException ignored) {
			//String line = "timeout";
			String line = "err";
			wf.setStatusValue(line,index);
			wf.decrementLabel();
			wf.updateProgressBar();
		}
		// "finally" clause, to close the input stream
		// in any case
		finally {
			try{
				if (input != null) input.close();
			}
			catch(IOException ignored) {}
		}
	}
}
