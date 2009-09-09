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

import org.ancora.MicroblazeInterpreter.HardwareBlocks.InstructionMemory.TraceMemory;
import java.io.File;
import org.ancora.jCommons.Disk;

/**
 *
 * @author Ancora Group <ancora.codigo@gmail.com>
 */
public class TraceMemoryProfile {

   /**
    * @param args the command line arguments
    */
   public static void main(String[] args) {
      String testFilename = "";

      //testFilename = "traces/fdct_trace_without_optimization.txt";
      //testFilename = "traces/autocorrelation_trace_without_optimization.txt";
      //testFilename = "traces/adpcm-decoder_trace_without_optimization.txt";
      //testFilename = "traces/adpcm-coder_trace_without_optimization.txt";
      testFilename = "traces/New Text Document.txt";

      Disk disk = Disk.getDisk();
      File testFile = disk.safeFile(testFilename);
      TraceMemory instance = new TraceMemory(testFile);


      String inst = instance.nextInstruction();
      int numInst = 0;
      while (inst != null) {
         inst = instance.nextInstruction();
         numInst++;
      }

      System.out.println("Num Insts:"+numInst);
   }

}
