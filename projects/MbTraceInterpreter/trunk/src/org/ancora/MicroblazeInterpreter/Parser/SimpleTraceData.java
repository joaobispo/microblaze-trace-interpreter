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

package org.ancora.MicroblazeInterpreter.Parser;

/**
 *  Simple immutable implementation of interface TraceData.
 *
 * @author Joao Bispo
 */
public class SimpleTraceData implements TraceData{

    public SimpleTraceData(String opName, String[] registers, Short imm) {
        this.opName = opName;
        this.registers = registers;
        this.imm = imm;
    }


    public String getOpName() {
      return opName;
   }

   public String getRd() {
      return registers[RD_INDEX];
   }

   public String getRa() {
      return registers[RA_INDEX];
   }

   public String getRb() {
      return registers[RB_INDEX];
   }

   public Short getImm() {
      return imm;
   }

    @Override
   public String toString() {
      int capacity = 100;
      StringBuilder builder = new StringBuilder(capacity);

      // Print registers until a null is found
      for(int i=0; i<registers.length; i++) {
         if(registers[i] == null) {
            break;
         }

         // Check if its not the last argument.
         // Instead of "registers.length", it was previously MAX_INST_ARGS.
         if(i < MAX_REGISTERS-1) {
            // Check if this is the last argument
            if(registers[i+1] == null && imm == null) {
               builder.append(registers[i]);
            }
            else {
               builder.append(registers[i]+", ");
            }
         }
         else {
            builder.append(registers[i]);
         }

      }

      if(imm != null) {
          builder.append(imm);

      }

      return builder.toString();
   }



    // INSTANCE VARIABLES

   // Definitions
   private static final int RD_INDEX = 0;
   private static final int RA_INDEX = 1;
   private static final int RB_INDEX = 2;

    // State
   private final String opName;
   private final String[] registers;
   private final Short imm;

}
