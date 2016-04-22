package com.fuzzer;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yauhen.bialiayeu on 22.04.2016.
 */
public class Start
{
    public static void RunPaint()
    {
        List<String> pngFiles = new ArrayList<String>();
        String directory=System.getProperty("user.dir")+"\\src\\main\\resources\\Samples\\";
        File exampleDirectory = new File(directory);
        for (File file : exampleDirectory.listFiles())
        {
            if (file.getName().endsWith((".png")))
            {
                pngFiles.add(file.getName());
            }
        }
        try {
            for (int i=0;i<pngFiles.size();i++) {
                Process h1 =
                        Runtime.getRuntime().exec("C:\\WINDOWS\\system32\\mspaint.exe " + directory + pngFiles.get(i));
                BufferedReader stdError = new BufferedReader(new
                        InputStreamReader(h1.getErrorStream()));

                try {
                    Thread.sleep(1000);
                }
                catch (InterruptedException ex)
                {}
                if (!isAlive(h1)) {
                    Logger.log.error("Paint crashed on file: " + pngFiles.get(i));
                    Logger.log.info(InputToString(stdError));
                } else {
                    Logger.log.info("Paint successfully open " + pngFiles.get(i));
                    h1.destroy();
                }
            }
        }
        catch (IOException ex)
        {}

    }
    private static String InputToString(BufferedReader in) {
        String s = null;
        String result = null;
        try {
            while ((s = in.readLine()) != null) {
                result += s;
            }
        }
        catch (IOException e)
        {}
        return result;
    }
   private static boolean isAlive(Process process)
    {
        try
        {
            process.exitValue();
            return false;
        }
        catch(IllegalThreadStateException e)
        {
            return true;
        }
    }
}
