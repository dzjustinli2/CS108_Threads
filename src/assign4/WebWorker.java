package assign4;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WebWorker extends Thread {
	WebFrame wf;
	int index;
	
	public WebWorker(WebFrame program, int linkIndex){
		wf = program;
		index = linkIndex;
	}
	private void graphics(String line){
		wf.setStatusValue(line,index);
		wf.decrementLabel();
		wf.updateProgressBar();
	}
	public void run(){
		wf.incrementLabel();
		//This is the core web/download i/o code...
 		InputStream input = null;
		StringBuilder contents = null;
		BufferedReader reader = null;
		try {
			URL url = new URL((String)wf.dbTable.getValueAt(index,0));
			URLConnection connection = url.openConnection();
			if(isInterrupted()){
				throw new InterruptedException();
			}
			// Set connect() to throw an IOException
			// if connection does not succeed in this many msecs.
			connection.setConnectTimeout(5000);
			connection.connect();
			if(isInterrupted()){
				throw new InterruptedException();
			}
			input = connection.getInputStream();
			reader  = new BufferedReader(new InputStreamReader(input));
			char[] array = new char[1000];
			int len;
			contents = new StringBuilder(1000);
			double start = System.currentTimeMillis();
			while ((len = reader.read(array, 0, array.length)) > 0) {
				if(isInterrupted()){
					throw new InterruptedException();
				}
				contents.append(array, 0, len);
				Thread.sleep(100);
			}
			double end = System.currentTimeMillis();
			// Successful download if we get here
			double wallToWall = end - start;
			Date timeDiff = new Date();
			SimpleDateFormat timeFormat = new SimpleDateFormat("H:mm:ss");
			String line = timeFormat.format(timeDiff) + "   " + (int)wallToWall + "ms   " + contents.length() + " bytes";
			graphics(line);
		}
		// Otherwise control jumps to a catch...
		catch(MalformedURLException ignored) {
			graphics("err");
		}
		catch(InterruptedException exception) {
			graphics("interrupted");
		}
		catch(IOException ignored) {
			graphics("err");
		}
		// "finally" clause, to close the input stream
		// in any case
		finally {
			try{
				if(input != null) input.close();
				if(reader != null) reader.close();
			}
			catch(IOException ignored) {}
		}
	}
}
