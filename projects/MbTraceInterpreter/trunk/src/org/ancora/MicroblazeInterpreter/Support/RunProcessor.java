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
import org.ancora.MicroblazeInterpreter.HardwareBlocks.MicroBlazeProcessor;
import org.ancora.MicroblazeInterpreter.HardwareBlocks.TraceMemory;
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

      // Load the processor
      MicroBlazeProcessor mb = loadMicroBlaze(traceFile);
      // Execute it
      mb.run();
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
      TraceMemory memory = new TraceMemory(traceFile);

      MicroBlazeProcessor mb = new MicroBlazeProcessor(memory);

      return mb;
   }
   // INSTANCE VARIABLES
   private static final int INDEX_TRACE_FILE = 0;

}
