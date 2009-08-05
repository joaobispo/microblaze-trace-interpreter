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
import org.ancora.MicroblazeInterpreter.HardwareBlocks.Registers.LockRegister;
import org.ancora.MicroblazeInterpreter.HardwareBlocks.Registers.MsrBit;
import org.ancora.MicroblazeInterpreter.HardwareBlocks.Registers.RegisterFile;
import org.ancora.MicroblazeInterpreter.HardwareBlocks.Registers.SpecialPurposeRegisters;
import org.ancora.MicroblazeInterpreter.HardwareBlocks.Registers.SpecialRegister;
import org.ancora.MicroblazeInterpreter.Parser.InstructionParser;
import org.ancora.MicroblazeInterpreter.Parser.TraceData;
import org.ancora.jCommons.Console;
import org.ancora.jCommons.DefaultConsole;

/**
 *  Implements the MicroBlaze Arithmetic Add.
 * 
 * <p> Includes the instructions add, addc, addk and addkc.
 *
 * @author Joao Bispo
 */
public class MbAddi implements MbInstruction, MbBuilder {

    /**
     * Constructor for using this object as a MbBuilder
     */
    public MbAddi() {
        cBit = false;
        kBit = false;
        regA = -1;
        imm = -1;
        regD = -1;
        execute = false;
        spr = null;
        regs = null;
        lockReg = null;
    }

    public MbInstruction build(TraceData data, MicroBlazeProcessor processor) {
        return new MbAddi(data, processor);
    }

    /**
     * Constructor for using this object as a MbInstruction
     *
     * @param data
     */
    public MbAddi(TraceData data, MicroBlazeProcessor processor) {
        // Signal this object as "executable"
        execute = true;

        // Assign Hardware Blocks
        spr = processor.getSpecialRegisters();
        regs = processor.getRegisterFile();
        lockReg = processor.getLockRegister();

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
        regA = InstructionParser.parseRegister(data.getRa());

        // Get rB
        imm = data.getImm();

        // Get rD
        regD = InstructionParser.parseRegister(data.getRd());

    }


    /**
     * Executes the instruction
     */
    public void execute() {
        if(!execute) {
            console.warn("execute: this object is a builder, not an instruction" +
                    " ("+this.getClass()+")");
            return;
        }



        // If cBit, get carry from MSR of special register file
        int carry = 0;
        if(cBit) {
            int msr = spr.read(SpecialRegister.rmsr);
            carry = BitOperations.getBit(MsrBit.C.getPosition(), msr);
        }

        // Get rA from register file
        int rA = regs.read(regA); // rA <- RF


        // Check if the previous instruction was an Imm
        int immediate;
        
        if(lockReg.isLocked()) {
            int upperHalf = lockReg.getImmediate();
            immediate = BitOperations.writeBits(16, 16, upperHalf, this.imm);
        }
        else {
            immediate = imm;
        }


        // Do summation
        int rD = rA + immediate + carry;

        // Store result in register file
        regs.write(regD, rD);// RD <- rD

        // If kBit, calculate carry out
        if(!kBit) {
            // SPR <- CarryOut
            int carryOut = BitOperations.getCarryOut(rA, immediate, carry);
            int msr = spr.read(SpecialRegister.rmsr);
            msr = BitOperations.writeBit(MsrBit.C.getPosition(), carryOut, msr);
            msr = BitOperations.writeBit(MsrBit.CC.getPosition(), carryOut, msr);
            spr.write(SpecialRegister.rmsr, msr);
            
        }
    }

   public int latency() {
      return LATENCY;
   }

   public boolean isBranch() {
      return IS_BRANCH;
   }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(100);
        
        builder.append("cBit:"+cBit+"\n");
        builder.append("kBit:"+kBit+"\n");
        builder.append("regA:"+regA+"\n");
        builder.append("regB:"+imm+"\n");
        builder.append("regD:"+regD+"\n");
        
        return builder.toString();
    }


    // INSTANCE VARIABLES

    // State
    private final boolean cBit;
    private final boolean kBit;
    private final int regA;
    private final int imm;
    private final int regD;
    private final boolean execute;

    // Hardware Blocks
    // Register file
    // Special Registers
    private final SpecialPurposeRegisters spr;
    private final RegisterFile regs;
    private final LockRegister lockReg;

    // Constants
    private final String C_CHAR = "c";
    private final String K_CHAR = "k";
    
    private final int LATENCY = 1;
    private final boolean IS_BRANCH = false;

    // Utilities
    private final Console console = DefaultConsole.getConsole();

}
