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

package org.ancora.MicroblazeInterpreter.HardwareBlocks;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import javax.crypto.SealedObject;
import org.ancora.MicroblazeInterpreter.Instructions.InstructionBuilder;
import org.ancora.MicroblazeInterpreter.Parser.InstructionParser;
import org.ancora.MicroblazeInterpreter.Parser.TraceData;
import org.ancora.MicroblazeInterpreter.Support.NumberCounter;

/**
 * Represents a MicroBlaze Processor. Runs trace files.
 *
 * @author Joao Bispo
 */
public class MicroBlazeProcessor {

   public MicroBlazeProcessor(TraceMemory instructionMemory) {
      this.instructionMemory = instructionMemory;
   }

   public void run() {
      // Fetch instruction
      String instruction = instructionMemory.nextInstruction();

      NumberCounter counter = new NumberCounter();

      while(instruction != null) {
         // Check instruction address
         String stringAddress = InstructionParser.getMemoryAddress(instruction);
         int decodedAddress = Integer.valueOf(stringAddress, 16);
         counter.addInt(decodedAddress);

         // Check if instruction is in cache

         // If instruction is not in cache, create it an add to cache
            
            TraceData data = InstructionParser.parseInstruction(instruction);
            String opName = data.getOpName();
            InstructionBuilder inst = InstructionBuilder.valueOf(opName);


         // Executes Instructions

         // Advances time: clock, program counter, etc...

         // Fetch next instruction
         instruction = instructionMemory.nextInstruction();
      }

      Map<Integer, Integer> map = counter.getTable();
      System.out.println("Number of addresses:"+map.size());
      
      // Get keyset and sort it
      Set<Integer> keys = map.keySet();
      Integer[] keysArray = keys.toArray(new Integer[keys.size()]);
      Arrays.sort(keysArray);

      for(Integer key : keysArray) {
         System.out.println("Address:"+key+";Count:"+map.get(key));
      }

   }


   // INSTANCE VARIABLES
   // State
   private final TraceMemory instructionMemory;
}
