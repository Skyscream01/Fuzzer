package com.fuzzer;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by yauhen.bialiayeu on 21.04.2016.
 */
public class Mutation
{
    // забираем файл образец
    private static String GetExample()
    {
        Logger.log.info("Getting png file example");
        List<String> pngFiles = new ArrayList<String>();
        String directory=System.getProperty("user.dir")+"\\src\\main\\resources\\Example\\";
        File exampleDirectory = new File(directory);
        for (File file : exampleDirectory.listFiles())
        {
            if (file.getName().endsWith((".png")))
            {
                pngFiles.add(file.getName());
            }
        }
        Logger.log.info("Example: "+pngFiles.get(0)+" found");
        return directory+pngFiles.get(0);
    }
    // получаем хекс валидного изображения
    private static String  GetImage()
    {
        String hex = new String();
        try {
            ImageInputStream im= ImageIO.createImageInputStream(new File(GetExample()));
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            Logger.log.info("Converting example file to hex");
            byte[] buffer = new byte[10240];
            for (int length = 0; (length = im.read(buffer)) > 0;) {
                output.write(buffer, 0, length);
            }
            hex = DatatypeConverter.printHexBinary(output.toByteArray());
            output.close();
            try {
                if (!hex.substring(0, 16).equals("89504E470D0A1A0A")) {
                    throw new Invalid("Invalid file type");
                }
            }
            catch (Invalid ex)
            {
                ex.printStackTrace();
                System.exit(1);
            }

        }
        catch (IOException e)
        {}
        return hex;
    }
    //конвертим хекс в бинарь и пишем в файл
    private static void SaveFile(String hex, String name)
    {
        byte[] binary = DatatypeConverter.parseHexBinary(hex);
        Logger.log.info("Converting hex to binary");
        try {
            FileOutputStream fos = new FileOutputStream(System.getProperty("user.dir")+"\\src\\main\\resources\\Samples\\"+name+".png");
            fos.write(binary);
            fos.flush();
            fos.close();
            Logger.log.info("Converting done. File saved as: "+name);
        }
        catch (FileNotFoundException e)
        {}
        catch (IOException e)
        {}
    }
    //рандомно создаём мутированный хекс по заданной длине
    private static String MutateHex(int lenght)
    {
        Logger.log.info("Random mutation");
        Random r = new Random();
        StringBuffer sb = new StringBuffer();
        while(sb.length() < lenght){
            sb.append(Integer.toHexString(r.nextInt()));
        }
        return sb.toString().substring(0, lenght);
    }
    //мутируем idat почти валидный файл на выходе
    public static void MutateIdat(String name)
    {
        Logger.log.info("Mutating IDAT of the file");
        String hex = GetImage();
        //у всех пнг всегда одинаковый айэнд
        String iend="0000000049454E44";
        //айэнд+еге длина
        String fullIend=iend+"AE426082";
        //разделяем первичные заголовки и idat
        String[] idat = hex.split("0000011E49444154");
        //первичные заголовки и ihdr + сама надпись idat
        String ihdr=idat[0]+"0000011E49444154";
        String[] idat2 =idat[1].split(iend);
        //айдат+СРС
        String idatCRC=idat2[0];
        //чистейшей воды айдат, читай само изображение
        String pureIdat=idatCRC.substring(0, idatCRC.length()-9);
        //контрольная сумма
        String crc=idatCRC.substring(idatCRC.length()-9);
        //мутируем айдат
        String fakeIdat = MutateHex(pureIdat.length()).toUpperCase();
        //склеиваем мутированный полный хекс
        String toReturn=ihdr+fakeIdat+crc+fullIend;
        Logger.MutateInfo("IDAT", pureIdat, fakeIdat);
        SaveFile(toReturn, name);
    }
    public static void MutateIdatCRC(String name)
    {
        Logger.log.info("Mutating IDAT CRC of the file");
        String hex = GetImage();
        //у всех пнг всегда одинаковый айэнд
        String iend="0000000049454E44";
        //айэнд+еге длина
        String fullIend=iend+"AE426082";
        //разделяем первичные заголовки и idat
        String[] idat = hex.split("0000011E49444154");
        //первичные заголовки и ihdr + сама надпись idat
        String ihdr=idat[0]+"0000011E49444154";
        String[] idat2 =idat[1].split(iend);
        //айдат+СРС
        String idatCRC=idat2[0];
        //чистейшей воды айдат, читай само изображение
        String pureIdat=idatCRC.substring(0, idatCRC.length()-9);
        //контрольная сумма
        String crc=idatCRC.substring(idatCRC.length()-9);
        //мутируем айдат
        String fakeCrc = MutateHex(crc.length()).toUpperCase();
        //склеиваем мутированный полный хекс
        String toReturn=ihdr+pureIdat+fakeCrc+fullIend;
        Logger.MutateInfo("IDAT CRC", crc, fakeCrc);
        SaveFile(toReturn, name);
    }

    public static void MutateIHDRWidth(String name)
    {
        Logger.log.info("Mutating IHDR width of the file");
        String hex = GetImage();
        String header = hex.substring(0,32);
        String ending = hex.substring(40);
        String original = hex.substring(32,40);
        String fakeWidth=MutateHex(8);
        //склеиваем мутированный полный хекс
        String toReturn=header+fakeWidth+ending;
        Logger.MutateInfo("IHDR Width", original, fakeWidth);
        SaveFile(toReturn, name);
    }
    public static void MutateIHDRHeight(String name)
    {
        Logger.log.info("Mutating IHDR height of the file");
        String hex = GetImage();
        String header = hex.substring(0,40);
        String ending = hex.substring(48);
        String original = hex.substring(40,48);
        String fakeHeight=MutateHex(8);
        //склеиваем мутированный полный хекс
        String toReturn=header+fakeHeight+ending;
        Logger.MutateInfo("IHDR height", original, fakeHeight);
        SaveFile(toReturn, name);
    }
    public static void MutateIHDRDepth(String name)
    {
        Logger.log.info("Mutating IHDR bit depth of the file");
        String hex = GetImage();
        String header = hex.substring(0,48);
        String ending = hex.substring(50);
        String original = hex.substring(48,50);
        String fakeBit=MutateHex(2);
        //склеиваем мутированный полный хекс
        String toReturn=header+fakeBit+ending;
        Logger.MutateInfo("IHDR bit depth", original, fakeBit);
        SaveFile(toReturn, name);
    }
    public static void MutateIHDRColor(String name)
    {
        Logger.log.info("Mutating IHDR color type of the file");
        String hex = GetImage();
        String header = hex.substring(0,50);
        String ending = hex.substring(52);
        String original = hex.substring(50,52);
        String fakeColor=MutateHex(2);
        //склеиваем мутированный полный хекс
        String toReturn=header+fakeColor+ending;
        Logger.MutateInfo("IHDR color", original, fakeColor);
        SaveFile(toReturn, name);
    }
    public static void MutateIHDRCompression(String name)
    {
        Logger.log.info("Mutating IHDR compression of the file");
        String hex = GetImage();
        String header = hex.substring(0,52);
        String ending = hex.substring(54);
        String original = hex.substring(52,54);
        String fakeCompression=MutateHex(2);
        //склеиваем мутированный полный хекс
        String toReturn=header+fakeCompression+ending;
        Logger.MutateInfo("IHDR compression", original, fakeCompression);
        SaveFile(toReturn, name);
    }

    public static void MutateIHDRFilter(String name)
    {
        Logger.log.info("Mutating IHDR filter method of the file");
        String hex = GetImage();
        String header = hex.substring(0,54);
        String ending = hex.substring(56);
        String original = hex.substring(54,56);
        String fakeFilter=MutateHex(2);
        //склеиваем мутированный полный хекс
        String toReturn=header+fakeFilter+ending;
        Logger.MutateInfo("IHDR filter method", original, fakeFilter);
        SaveFile(toReturn, name);
    }
    public static void MutateIHDRInterface(String name)
    {
        Logger.log.info("Mutating IHDR interface of the file");
        String hex = GetImage();
        String header = hex.substring(0,56);
        String ending = hex.substring(58);
        String original = hex.substring(56,58);
        String fakeInterface=MutateHex(2);
        //склеиваем мутированный полный хекс
        String toReturn=header+fakeInterface+ending;
        Logger.MutateInfo("IHDR Interface", original, fakeInterface);
        SaveFile(toReturn, name);
    }
    public static void MutateIend(String name)
    {
        Logger.log.info("Mutating IEND of the file");
        String hex = GetImage();
        //у всех пнг всегда одинаковый айэнд
        String iend="0000000049454E44";
        //разделяем первичные заголовки и idat
        String[] idat2 =hex.split(iend);
        //айдат+СРС
        String start=idat2[0];
        //мутируем CRC iend
        String fakeIend =iend+MutateHex(8);
        //склеиваем мутированный полный хекс
        String toReturn=start+fakeIend;
        Logger.MutateInfo("IEND", "0000000049454E44AE426082", fakeIend);
        SaveFile(toReturn, name);
    }

}
