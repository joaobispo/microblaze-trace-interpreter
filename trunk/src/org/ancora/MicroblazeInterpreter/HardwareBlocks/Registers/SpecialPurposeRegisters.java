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

/**
 * MicroBlaze's Special Purpose Registers
 *
 * @author Joao
 */
public interface SpecialPurposeRegisters {

   /**
    * Reads the contents of a special purpose register.
    *
    * @param address the address of the register
    * @return the contents of the register
    */
   public int read(int address);


   /**
    * Reads the contents of a special purpose register.
    *
    * @param register the register to read from
    * @return the contents of the register
    */
   public int read(SpecialRegister register);


   /**
    * Writes the contents of a special purpose register. If can only write to
    * registers that exist.
    *
    * @param register the register to write to
    * @param value the value to be written
    */
   public void write(SpecialRegister register, int value);

   /**
    * Writes the contents of a special purpose register. If can only write to
    * registers that exist. If register doesn't exist, the register file is not
    * modified (see class SpecialRegister).
    *
    * @param value the value to be written
    * @param address the address of the register
    */
   public void write(int address, int value);

   /**
    * @return The carry bit from the Machine Status Register (MSR)
    */
   public int getCarryBit();

   /**
    * @param carryBit the new value of the bit of the
    * Status Register (MSR)
    */
   public void writeCarryBit(int carryBit);

   /**
    * @param dzo the new value of the bit of the
    * Status Register (MSR)
    */
   public void writeDzoBit(int dzo);

   /**
    * @param dec the new value of the bit in the
    * Error Status Register (ESR)
    */
   public void writeDecBit(int dec);

   /**
    * @param ec the new value of the 5 bits in the
    * Error Status Register (ESR)
    */
   public void writeEcBits(int ec);

     /**
    * @return The contents of the Program Counter (PC)
    */
   public int getPc();

   /**
    * @param carryBit the new value of Program Counter (PC)
    */
   public void writePc(int pc);

   /**
    * Increments the value of the Program Counter (PC) by 4.
    */
    public void incrementPc();
}
