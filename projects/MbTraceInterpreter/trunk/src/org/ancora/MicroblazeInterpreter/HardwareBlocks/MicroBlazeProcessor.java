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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.ancora.MicroblazeInterpreter.Instructions.InstructionBuilder;
import org.ancora.MicroblazeInterpreter.Parser.InstructionParser;
import org.ancora.MicroblazeInterpreter.Parser.TraceData;
import org.ancora.MicroblazeInterpreter.Support.NumberCounter;
import org.ancora.jCommons.Console;
import org.ancora.jCommons.DefaultConsole;

/**
 * Represents a MicroBlaze Processor. Runs trace files.
 *
 * @author Joao Bispo
 */
public class MicroBlazeProcessor {

   public MicroBlazeProcessor(TraceMemory instructionMemory) {
      this.instructionMemory = instructionMemory;

      notImplemented = new HashSet<String>();
   }

   public void run() {
      // Fetch instruction
      String instruction = instructionMemory.nextInstruction();
      // Initialize address counter
      NumberCounter counter = new NumberCounter();

      // Run Trace File
      while(instruction != null) {
          // Process instruction
          processInstruction(instruction, counter);
        
         // Fetch next instruction
         instruction = instructionMemory.nextInstruction();
      }

      // Show how many not implemented operations where found
      int notImplementedNumber = notImplemented.size();
      if(notImplementedNumber > 0) {
          console.warn(notImplementedNumber+" instructions not implemented.");
      }

      // Show contents of counter
      showCounter(counter);
     

   }

    /**
     * Processes the given instruction.
     *
     * @param instruction
     */
    private void processInstruction(String instruction, NumberCounter counter) {
        // Check instruction address
        String stringAddress = InstructionParser.getMemoryAddress(instruction);
        int decodedAddress = Integer.valueOf(stringAddress, 16);
        counter.addInt(decodedAddress);

        // Check if instruction is in cache

        // If instruction is not in cache, create it an add to cache

        TraceData data = InstructionParser.parseInstruction(instruction);
        String opName = data.getOpName();
        InstructionBuilder instBuilder = getInstruction(opName);
        // Check if instruction could be built
        if(instBuilder == null) {
            return;
        }


        // Executes Instructions

        // Advances time: clock, program counter, etc...
    }

   /**
    * Returns the InstructionBuilder that corresponds to the given name.
    *
    * @param opName
    * @return
    */
    private InstructionBuilder getInstruction(String opName) {
        InstructionBuilder instBuilder = null;

        try {
            instBuilder = InstructionBuilder.valueOf(opName);
        } catch (IllegalArgumentException ex) {
            if (!notImplemented.contains(opName)) {
                console.warn("getInstruction: Asked for a MicroBlaze Instruction " +
                        "which is not implemented (" + opName + ")");
                notImplemented.add(opName);
            }
        }
        return instBuilder;
    }

       /**
    * Show contents of address counter.
    *
    * @param counter
    */
    private void showCounter(NumberCounter counter) {
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

   // Debug
   private final Set<String> notImplemented;

   // Utilities
   private final Console console = DefaultConsole.getConsole();






}
