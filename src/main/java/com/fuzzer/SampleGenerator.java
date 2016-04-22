package com.fuzzer;

/**
 * Created by yauhen.bialiayeu on 22.04.2016.
 */
public class SampleGenerator
{
    public static void Generate(int count)
    {
        for (int i=1;i<=count;i++)
        {
            Mutation.MutateIdat("TS_"+i+"_IDAT_mutation");
            Mutation.MutateIdatCRC("TS_"+i+"_IDAT_CRC_mutation");
            Mutation.MutateIHDRWidth("TS_"+i+"_IHDR_Width_mutation");
            Mutation.MutateIHDRHeight("TS_"+i+"_IHDR_Height_mutation");
            Mutation.MutateIHDRDepth("TS_"+i+"_IHDR_Depth_mutation");
            Mutation.MutateIHDRColor("TS_"+i+"_IHDR_Color_mutation");
            Mutation.MutateIHDRCompression("TS_"+i+"_IHDR_Compression_mutation");
            Mutation.MutateIHDRFilter("TS_"+i+"_IHDR_Filter_mutation");
            Mutation.MutateIHDRInterface("TS_"+i+"_IHDR_Interface_mutation");
            Mutation.MutateIend("TS_"+i+"_IEND_mutation");
        }
    }
}
