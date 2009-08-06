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
import java.util.Arrays;
import org.ancora.MicroblazeInterpreter.Commons.BitOperations;
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
import org.ancora.jCommons.Disk;

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

      String[] cleanArgs = processArgs(args);

      String traceFilepath = cleanArgs[INDEX_TRACE_FILE];

      // Get trace file
      System.out.println("Opening trace \""+traceFilepath+"\"...");
      File traceFile = disk.safeFile(traceFilepath);

      //testDataMemory();
      //testCachedSegments();
      
      // Load the processor
      MicroBlazeProcessor mb = loadMicroBlaze(traceFile);
      // Execute it
      mb.run();

      // Inspect it after execution
      showSpr(mb);
      showRegs(mb);
      showClock(mb);

       
    }

    /**
     * Process the command line arguments
     * 
     * @param args
     * @return
     */
   private static String[] processArgs(String[] args) {
        if(args.length != 1) {
            System.out.println("Usage: [Trace_File]");
            System.out.println("Example: ./traces/fdct_trace_without_optimization.txt");
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
              dataMemoryPlus);

      return mb;
   }

    /**
     * Show the contents of Special Purpose Registers
     *
     * @param mb
     */
    private static void showSpr(MicroBlazeProcessor mb) {
        SpecialPurposeRegisters spr = mb.getSpecialRegisters();
        System.out.println("Special Register Values:");
        for (SpecialRegister reg : SpecialRegister.values()) {
            int value = spr.read(reg);
            String stringValue;
            switch(reg) {
                case rmsr:
                    stringValue = Integer.toBinaryString(value);
                    stringValue = BitOperations.padBinaryString(stringValue, 32);
                    System.out.println(reg.name() + ":" + stringValue);
                    break;
                case resr:
                    stringValue = Integer.toBinaryString(value);
                    stringValue = BitOperations.padBinaryString(stringValue, 32);
                    System.out.println(reg.name() + ":" + stringValue);
                    break;
                case rpc:
                    stringValue = Integer.toHexString(value);
                    stringValue = BitOperations.padHexString(stringValue, 8);
                    System.out.println(reg.name() + ":" + stringValue);
                    break;
                default:
                    System.out.println(reg.name() + ":" + value);
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
        System.out.println("General Purpose Registers Values:");
        for(int i=0; i<regs.numRegisters(); i++) {
            System.out.println("[" + i + "]:" + regs.read(i));
        }
    }

    /**
     *
     * Show information about processor execution.
     * @param mb
     */
    private static void showClock(MicroBlazeProcessor mb) {
        Clock clock = mb.getClock();
        System.out.println("Number o clock cycles:"+clock.getLatency());
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







}
