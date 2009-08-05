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

package org.ancora.MicroblazeInterpreter.HardwareBlocks.Registers;

import org.ancora.MicroblazeInterpreter.HardwareBlocks.*;
import java.util.HashMap;
import java.util.Map;
import org.ancora.jCommons.Console;
import org.ancora.jCommons.DefaultConsole;

/**
 * MicroBlaze's Special Purpose Registers, implemented by using a Map.
 *
 * @author Joao Bispo
 */
public class SprMap implements SpecialPurposeRegisters {

   public SprMap() {
      SpecialRegister[] registers = SpecialRegister.values();
      // Capacity estimate
      int numParameters = registers.length;
      int capacity = ((numParameters * 5) / 4) + 1;

       // Initialize table with configuration values
      registerFile = new HashMap<Integer, Integer>(capacity);

      for(SpecialRegister register : registers) {
         registerFile.put(register.getAddress(), 0);
      }
   }

   /**
    * Reads the contents of a special purpose register.
    *
    * @param address the address of the register
    * @return the contents of the register
    */
   public int read(int address) {
      return registerFile.get(address);
   }


   /**
    * Reads the contents of a special purpose register.
    *
    * @param register the register to read from
    * @return the contents of the register
    */
   public int read(SpecialRegister register) {
      return read(register.getAddress());
   }


   /**
    * Writes the contents of a special purpose register. If can only write to
    * registers that exist.
    *
    * @param register the register to write to
    * @param value the value to be written
    */
   public void write(SpecialRegister register, int value) {
      write(register.getAddress(), value);
   }

   /**
    * Writes the contents of a special purpose register. If can only write to
    * registers that exist. If register doesn't exist, the register file is not
    * modified (see class SpecialRegister).
    *
    * @param value the value to be written
    * @param address the address of the register
    */
   public void write(int address, int value) {
      boolean containsKey = registerFile.containsKey(address);

      if(!containsKey) {
         console.warn("write: Trying to write to address of Special Register " +
                 "that doesn't exist ("+address+")");
         return;
      }

      registerFile.put(address, value);
   }

   //INSTANCE VARIABLES
   // State
   private final Map<Integer,Integer> registerFile;

   // Utilities
   private final Console console = DefaultConsole.getConsole();
}
