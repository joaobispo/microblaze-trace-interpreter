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
import java.util.LinkedList;
import java.util.List;
import org.ancora.MicroblazeInterpreter.HardwareBlocks.TraceMemory;
import org.ancora.MicroblazeInterpreter.Parser.InstructionParser;
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
      final String traceSuffix = ".txt";

      // Specify folder with trace files
      String tracesFolderpath = "./traces";
      File tracesFolder = disk.safeFolder(tracesFolderpath);

      // Get all traces files
      File[] candidateTraceFiles = tracesFolder.listFiles();
      List<File> traceFiles = new LinkedList<File>();
      for(File file : candidateTraceFiles) {
         // Check if it is a trace file
         if(file.getName().endsWith(traceSuffix)) {
            traceFiles.add(file);
         }
      }

      // Process each file
      processTraceFile(traceFiles.get(3));
      //for(File traceFile : traceFiles) {
      //   processTraceFile(traceFile);
      //}

   }

   public static void processTraceFile(File traceFile) {
      TraceMemory memory = new TraceMemory(traceFile);
      InstructionParser parser = new InstructionParser();

      String instruction = memory.nextInstruction();
      while(instruction != null) {
         //String address = InstructionParser.getMemoryAddress(instruction);
         //Long addressLong = ExtraCommons.hexa2long(address);
         //System.out.println(addressLong);

         parser.parseInstruction(instruction);

         instruction = memory.nextInstruction();
      }


   }

}
