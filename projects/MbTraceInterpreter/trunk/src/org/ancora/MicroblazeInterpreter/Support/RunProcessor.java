/*
 *  Copyright 2009 Ancora Research Group.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.ancora.MicroblazeInterpreter.Support;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ancora.MicroblazeInterpreter.Commons.BitOperations;
import org.ancora.MicroblazeInterpreter.Configuration.MbConfiguration;
import org.ancora.MicroblazeInterpreter.HardwareBlocks.DataMemory.CachedSegments;
import org.ancora.MicroblazeInterpreter.HardwareBlocks.DataMemory.DataMemory;
import org.ancora.MicroblazeInterpreter.HardwareBlocks.DataMemory.DataMemoryPlus;
import org.ancora.MicroblazeInterpreter.HardwareBlocks.DataMemory.MemorySegment;
import org.ancora.MicroblazeInterpreter.HardwareBlocks.InstructionMemory.InstructionMemory;
import org.ancora.MicroblazeInterpreter.HardwareBlocks.Processor.MicroBlazeProcessor;
import org.ancora.MicroblazeInterpreter.HardwareBlocks.InstructionMemory.TraceMemory;
import org.ancora.MicroblazeInterpreter.HardwareBlocks.Processor.Clock;
import org.ancora.MicroblazeInterpreter.HardwareBlocks.Processor.CycleClock;
import org.ancora.MicroblazeInterpreter.HardwareBlocks.Processor.MbProcessor;
import org.ancora.MicroblazeInterpreter.HardwareBlocks.Registers.LockRegister;
import org.ancora.MicroblazeInterpreter.HardwareBlocks.Registers.RegisterFile;
import org.ancora.MicroblazeInterpreter.HardwareBlocks.Registers.RegisterFileArray;
import org.ancora.MicroblazeInterpreter.HardwareBlocks.Registers.SpecialPurposeRegisters;
import org.ancora.MicroblazeInterpreter.HardwareBlocks.Registers.SpecialRegister;
import org.ancora.MicroblazeInterpreter.HardwareBlocks.Registers.SprMap;
import org.ancora.jCommons.Console;
import org.ancora.jCommons.DefaultConsole;
import org.ancora.jCommons.Disk;
import org.ancora.jCommons.FileConsole;

/**
 * TODO: Discover how to read ç and ã into a properties.
 *
 */
/**
 * Runs a trace in the MicroBlaze Processor Interpreter
 *
 * @author Joao Bispo
 */
public class RunProcessor {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
      Disk disk = Disk.getDisk();

      // Check if config file exists
      // Read Properties file
      File propertiesFile = new File(configFile);
      if(!propertiesFile.isFile()) {
          System.out.println("Could not locate the config file ("+configFile+")");
          System.out.println("Exiting...");
          System.exit(1);
      }
      config = file2Properties(propertiesFile);

      // Process Command line arguments
      String[] cleanArgs = processArgs(args);

      String traceFilepath = cleanArgs[INDEX_TRACE_FILE];

      // Check if trace file exists
      File traceFile = new File(traceFilepath);
      if(!traceFile.isFile()) {
          System.out.println("Could not locate the trace file ("+traceFilepath+")");
          System.out.println("Exiting...");
          System.exit(1);
      }

      // Get trace file
      System.out.println("Opening trace \""+traceFilepath+"\"...");
      //File traceFile = disk.safeFile(traceFilepath);

      // Initialize Console
      boolean writeOutputFile = Boolean.valueOf(config.getProperty(ConfigParam.writeFile.name()));
      if(writeOutputFile) {
          String outFilename = config.getProperty(ConfigParam.outputFile.name());
          File outFile = disk.safeFile(outFilename);
          console = new FileConsole(outFile);
      } else {
          console = DefaultConsole.getConsole();
      }

      // Load the processor
      MicroBlazeProcessor mb = loadMicroBlaze(traceFile);
      // Execute it
      mb.run();

      // Inspect it after execution
      showSpr(mb);
      console.print("--------------------------------");
      showRegs(mb);
      console.print("--------------------------------");
      showClock(mb);

       console.close();
    }

    /**
     * Process the command line arguments.
     *
     * <p>If there is no arguments, read the configuration from config.properties.
     * Else, use default values and read the
     * 
     * @param args
     * @return
     */
   private static String[] processArgs(String[] args) {
       if (args.length == 0) {
           args = new String[1];
           args[0] = config.getProperty(ConfigParam.inputTrace.name());
           System.out.println("Args:"+args[0]);
       } else if (args.length > 1) {
           System.out.println("Usage: [Trace_File]");
           System.out.println("Example: ./traces/fdct_trace_without_optimization.txt");
           System.out.println("If no arguments are given, the trace file defined in " +
                   configFile + " is used.");
           System.exit(1);
       }

        return args;
   }

   /**
    * Loads the processor.
    * 
    * @param traceFile
    * @return
    */
   private static MicroBlazeProcessor loadMicroBlaze(File traceFile) {
      // Instruction Memory
      InstructionMemory memory = new TraceMemory(traceFile);
      SpecialPurposeRegisters specialRegisters = new SprMap();
      RegisterFile registerFile = new RegisterFileArray();
      LockRegister lockRegister = new LockRegister();
      Clock clock = new CycleClock(lockRegister, specialRegisters);
      DataMemory dataMemory = new CachedSegments();
      DataMemoryPlus dataMemoryPlus = new DataMemoryPlus(dataMemory);
      MbConfiguration configuration = new MbConfiguration();
      // Prepare Memory

      // Write register 2
      //registerFile.write(2, 100);
      // Write register 3
      //registerFile.write(3, 200);
      // Write Carry Bit
      //specialRegisters.writeCarryBit(1);

      //DataMemory mem = new CachedDataMemory(4);
      //DataMemory mem = new CachedSegments();
      //mem.storeWord(0, 100);
      //mem.storeWord(1, 200);
      // System.out.println(mem.read(0));
      //mem.storeWord(1024*1024, 1000);
      //System.out.println("Memory:"+Arrays.toString(mem.writtenWordAddresses()));

      // Initialize Processor
      MicroBlazeProcessor mb = new MbProcessor(
              memory,
              specialRegisters,
              registerFile,
              lockRegister,
              clock,
              dataMemoryPlus,
              configuration);

      return mb;
   }

    /**
     * Show the contents of Special Purpose Registers
     *
     * @param mb
     */
    private static void showSpr(MicroBlazeProcessor mb) {
        SpecialPurposeRegisters spr = mb.getSpecialRegisters();
        //System.out.println("Special Register Values:");
        console.print("Special Register Values:");
        for (SpecialRegister reg : SpecialRegister.values()) {
            int value = spr.read(reg);
            String stringValue;
            switch(reg) {
                case rmsr:
                    stringValue = Integer.toBinaryString(value);
                    stringValue = BitOperations.padBinaryString(stringValue, 32);
                    //System.out.println(reg.name() + ":" + stringValue);
                    console.print(reg.name() + ":" + stringValue);
                    break;
                case resr:
                    stringValue = Integer.toBinaryString(value);
                    stringValue = BitOperations.padBinaryString(stringValue, 32);
                    //System.out.println(reg.name() + ":" + stringValue);
                    console.print(reg.name() + ":" + stringValue);
                    break;
                case rpc:
                    stringValue = Integer.toHexString(value);
                    stringValue = BitOperations.padHexString(stringValue, 8);
                    //System.out.println(reg.name() + ":" + stringValue);
                    console.print(reg.name() + ":" + stringValue);
                    break;
                default:
                    //System.out.println(reg.name() + ":" + value);
                    console.print(reg.name() + ":" + value);
                    break;

            }
            
        }
    }

    /**
     * Show the contents of Genereal Purpose Register File.
     *
     * @param mb
     */
    private static void showRegs(MicroBlazeProcessor mb) {
        RegisterFile regs = mb.getRegisterFile();
        //System.out.println("General Purpose Registers Values:");
        console.print("General Purpose Registers Values:");
        for(int i=0; i<regs.numRegisters(); i++) {
            console.print("[" + i + "]:" + regs.read(i));
            //System.out.println("[" + i + "]:" + regs.read(i));
        }
    }

    /**
     *
     * Show information about processor execution.
     * @param mb
     */
    private static void showClock(MicroBlazeProcessor mb) {
        Clock clock = mb.getClock();
        //System.out.println("Number o clock cycles:"+clock.getLatency());
        console.print("Number o clock cycles:"+clock.getLatency());
    }

   // INSTANCE VARIABLES
   private static final int INDEX_TRACE_FILE = 0;

    private static void testDataMemory() {
        
        int segmentSize;
        int value;
        
        // 1024 words
        segmentSize= 10;

        // Second halfword = 1
        value = 65536;

        MemorySegment segment = new MemorySegment(segmentSize);
        DataMemoryPlus seg = new DataMemoryPlus(segment);

        seg.storeWord(0, 1);
        System.out.println(seg.loadWord(0));

        //seg.storeHalfword(2, 1);
        //System.out.println(seg.loadWord(0));
        //System.out.println(seg.loadHalfword(2));

        seg.storeByte(3, 1);
        System.out.println(seg.loadWord(0));
        System.out.println(seg.loadByte(3));

        seg.storeByte(1000, 2);

        System.out.println("Addresses:"+Arrays.toString(seg.writtenWordAddresses()));
    }

    private static void testCachedSegments() {
        CachedSegments segs = new CachedSegments();
        //MemorySegment segs = new MemorySegment(10);

        segs.storeWord(0, 1);
        segs.storeWord(4, 1);
        segs.storeWord(8, 1);
        segs.storeWord(1000, 1);
        //segs.storeWord(1023, 1);
        segs.storeWord(1024, 1);
        segs.storeWord(4095, 2);
        segs.storeWord(4096, 3);
        //System.out.println(segs.getSegment(0));
        //System.out.println(segs.getSegment(4095));
        //System.out.println(segs.getSegment(4095));
        //System.out.println(segs.getSegment(4095));
        //System.out.println(segs.getSegment(4095));
        //System.out.println(segs.getSegment(4096));

        //System.out.println(segs.stats());
        System.out.println(Arrays.toString(segs.writtenWordAddresses()));
    }
    
    /**
     * Loads a configuration file into a Java Properties object.
     * Returns the object
     *
     * @param filename
     * @return
     */
    public static Properties file2Properties(File file) {
        Properties props = new Properties();
        FileInputStream stream = null;
        InputStreamReader streamReader = null;


        try {
            stream = new FileInputStream(file);
            streamReader = new InputStreamReader(stream, charSet);
            props.load(streamReader);
        } catch (IOException ex) {
            Logger.getLogger(RunProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }

        return props;
    }


    enum ConfigParam {
        inputTrace,
        writeFile,
        outputFile;
    }

    // INSTANCE VARIABLES
    // State
        private static Properties config;
        private static Console console;
    // Constants
    private static final String configFile = "config.properties";
    private static final String charSet = "UTF-8";




}
