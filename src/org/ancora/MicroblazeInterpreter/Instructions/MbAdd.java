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

import org.ancora.MicroblazeInterpreter.Commons.BitOperations;
import org.ancora.MicroblazeInterpreter.HardwareBlocks.Processor.MicroBlazeProcessor;
import org.ancora.MicroblazeInterpreter.HardwareBlocks.Registers.RegisterFile;
import org.ancora.MicroblazeInterpreter.HardwareBlocks.Registers.SpecialPurposeRegisters;
import org.ancora.MicroblazeInterpreter.Parser.InstructionParser;
import org.ancora.MicroblazeInterpreter.Parser.TraceData;

/**
 *  Implements the MicroBlaze Arithmetic Add.
 * 
 * <p> Includes the instructions add, addc, addk and addkc.
 *
 * @author Joao Bispo
 */
public class MbAdd implements Instruction, Builder {

    /**
     * Constructor for using this object as a MbBuilder
     */
    public MbAdd() {
        cBit = false;
        kBit = false;
        regA = -1;
        regB = -1;
        regD = -1;
        spr = null;
        regs = null;
    }

    public Instruction build(TraceData data, MicroBlazeProcessor processor) {
        return new MbAdd(data, processor);
    }

    /**
     * Constructor for using this object as a MbInstruction
     *
     * @param data parsed trace data
     * @param processor a MicroBlaze processor
     */
    public MbAdd(TraceData data, MicroBlazeProcessor processor) {

        // Assign Hardware Blocks
        spr = processor.getSpecialRegisters();
        regs = processor.getRegisterFile();

        // Parse the data
        final String opName = data.getOpName();

        // Check bitC
        final boolean hasC = opName.contains(C_CHAR);
        if(hasC) {
            cBit = true;
        }
        else {
            cBit = false;
        }

        // Check bitK
        final boolean hasK = opName.contains(K_CHAR);
        if(hasK) {
            kBit = true;
        }
        else {
            kBit = false;
        }

        // Get rA
        regA = InstructionParser.parseRegister(data.getR2());

        // Get rB
        regB = InstructionParser.parseRegister(data.getR3());

        // Get rD
        regD = InstructionParser.parseRegister(data.getR1());

    }


    /**
     * Executes the instruction
     */
    public void execute() {

        // If cBit, get carry from MSR of special register file
        int carry = 0;
        if(cBit) {
            carry = spr.getCarryBit();
        }

        // Get rA and rB from register file
        int rA = regs.read(regA); // rA <- RF
        int rB = regs.read(regB); // rB <- RF

        // Do summation
        int rD = rA + rB + carry;

        // Store result in register file
        regs.write(regD, rD);// RD <- rD

        // If kBit, calculate carry out
        if(!kBit) {
            // SPR <- CarryOut
            int carryOut = BitOperations.getCarryOutAdd(rA, rB, carry);
            spr.writeCarryBit(carryOut);
        }
    }

   public int latency() {
      return LATENCY;
   }

   public boolean isBranch() {
      return IS_BRANCH;
   }

   /*
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(100);
        
        builder.append("cBit:"+cBit+"\n");
        builder.append("kBit:"+kBit+"\n");
        builder.append("regA:"+regA+"\n");
        builder.append("regB:"+regB+"\n");
        builder.append("regD:"+regD+"\n");
        
        return builder.toString();
    }
    */


    // INSTANCE VARIABLES

    // State
    private final boolean cBit;
    private final boolean kBit;
    private final int regA;
    private final int regB;
    private final int regD;

    // Hardware Blocks
    // Register file
    // Special Registers
    private final SpecialPurposeRegisters spr;
    private final RegisterFile regs;

    // Constants
    private final String C_CHAR = "c";
    private final String K_CHAR = "k";
    
    private final int LATENCY = 1;
    private final boolean IS_BRANCH = false;

}
