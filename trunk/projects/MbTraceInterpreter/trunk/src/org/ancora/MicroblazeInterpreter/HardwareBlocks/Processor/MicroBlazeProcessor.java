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

package org.ancora.MicroblazeInterpreter.HardwareBlocks.Processor;

import org.ancora.MicroblazeInterpreter.HardwareBlocks.InstructionMemory.InstructionMemory;
import org.ancora.MicroblazeInterpreter.HardwareBlocks.Registers.LockRegister;
import org.ancora.MicroblazeInterpreter.HardwareBlocks.Registers.RegisterFile;
import org.ancora.MicroblazeInterpreter.HardwareBlocks.Registers.SpecialPurposeRegisters;

/**
 * Represents a MicroBlaze Processor which runs trace files.
 *
 * @author Joao Bispo
 */
public interface MicroBlazeProcessor {

   
    /**
     * Runs the trace.
     */
   public void run();

   /**
    * Access to the Instruction Memory.
    *
    * @return
    */
   public InstructionMemory getInstructionMemory();

   /**
    * Access to the Special Purpose Registers.
    * 
    * @return
    */
   public SpecialPurposeRegisters getSpecialRegisters();

   /**
    * Access to the General Purpose Register File.
    *
    * @return
    */
   public RegisterFile getRegisterFile();

   /**
    * Access to the Lock Register
    *
    * @return
    */
   public LockRegister getLockRegister();

   /**
    * Access to the Clock
    * 
    * @return
    */
   public Clock getClock();
}
