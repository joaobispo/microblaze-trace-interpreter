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
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.ancora.MicroblazeInterpreter.HardwareBlocks.TraceMemory;
import org.ancora.MicroblazeInterpreter.Parser.InstructionParser;
import org.ancora.MicroblazeInterpreter.Parser.TraceData;
import org.ancora.jCommons.Disk;

/**
 * Mini-Program.
 *
 * <p>Extracts the operations found in a set of trace files. Traces are being
 * identified as files which end with ".txt".
 *
 * @author Joao Bispo
 */
public class ExtractOperations {

   public static void main(String[] args) {
      Disk disk = Disk.getDisk();

      String[] cleanArgs = processArgs(args);

      String tracesFolderpath = cleanArgs[INDEX_TRACE_FOLDER];
      final String traceSuffix = cleanArgs[INDEX_TRACE_EXTENSION];

      // Specify folder with trace files
      System.out.println("Opening folder \""+tracesFolderpath+"\"...");
      File tracesFolder = disk.safeFolder(tracesFolderpath);

      // Get all traces files
      System.out.print("Looking for files which end in \""+traceSuffix+"\"...");
      File[] candidateTraceFiles = tracesFolder.listFiles();
      List<File> traceFiles = new LinkedList<File>();
      for(File file : candidateTraceFiles) {
         // Check if it is a trace file
         if(file.getName().endsWith(traceSuffix)) {
            traceFiles.add(file);
         }
      }
       System.out.println(" found "+traceFiles.size()+" files.");

      // Process each file
      Set<String> operations = new HashSet<String>();

      //processTraceFile(traceFiles.get(3), operations);

      
      for(File traceFile : traceFiles) {
         processTraceFile(traceFile, operations);
      }


       System.out.println("Operations ("+operations.size()+"):");
       for(String op : operations) {
           System.out.println(op);
       }
   }

   /**
    * Reads a trace from a file and puts the name of the operations it finds
    * in a set.
    *
    * @param traceFile file with a MicroBlaze trace
    * @param operations set where the results will be put
    */
   public static void processTraceFile(File traceFile, Set<String> operations) {
      TraceMemory memory = new TraceMemory(traceFile);
      //InstructionParser parser = new InstructionParser();

      String instruction = memory.nextInstruction();
      while(instruction != null) {
         TraceData data = InstructionParser.parseInstruction(instruction);

         // Store operation
         operations.add(data.getOpName());
         
         instruction = memory.nextInstruction();
      }


   }

   /**
    * Process the command line arguments
    *
    * @param args
    * @return
    */
    private static String[] processArgs(String[] args) {
        if(args.length != 2) {
            System.out.println("Usage: [Trace_Folder] [Traces_Extension]");
            System.out.println("Example: ./traces .txt");
            System.exit(1);
        }

        return args;
    }


    // INSTANCE VARIABLES
    private static final int INDEX_TRACE_FOLDER = 0;
    private static final int INDEX_TRACE_EXTENSION = 1;
}
