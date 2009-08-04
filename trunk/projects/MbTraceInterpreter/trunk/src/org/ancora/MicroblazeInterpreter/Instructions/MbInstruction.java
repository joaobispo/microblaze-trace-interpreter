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

package org.ancora.MicroblazeInterpreter.Instructions;

/**
 * Interface for MicroBlaze instructions.
 *
 * @author Joao Bispo
 */
public interface MbInstruction {

   /**
    * Executes the MicroBlaze instruction, modifying the registers and the 
    * memory as necessary.
    */
   public void execute();

   /**
    * @return the latency of the instruction, in number of cycles
    */
   public int latency();

   /**
    * @return true if the instruction is a branch and updates the 
    * Program Counter, false otherwise.
    */
   public boolean isBranch();

}
