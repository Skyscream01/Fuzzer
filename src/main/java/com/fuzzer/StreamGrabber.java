package com.fuzzer;
import java.io.*;
class StreamGrabber extends Thread
{
    InputStream is;

    StreamGrabber(InputStream is)
    {
        this.is = is;

    }

    public void run()
    {
        try
        {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line=null;
            while ( (line = br.readLine()) != null)
               Logger.log.info(line);
        } catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
    }
}