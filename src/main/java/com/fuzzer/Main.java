package com.fuzzer;

/**
 * Created by yauhen.bialiayeu on 21.04.2016.
 */
public class Main {
    public static void main(String[] args)
    {
        Logger.Init();
        SampleGenerator.Generate(10);
        Start.RunPaint();
    }
}
