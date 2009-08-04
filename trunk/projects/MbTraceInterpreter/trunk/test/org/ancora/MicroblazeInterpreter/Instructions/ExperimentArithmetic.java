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

import org.ancora.MicroblazeInterpreter.Parser.SimpleTraceData;
import org.ancora.MicroblazeInterpreter.Parser.TraceData;

/**
 *
 * @author Administrador
 */
public class ExperimentArithmetic {

/**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //longRegisters();
        //testAdd();
        testExtra();
    }

    public static void longRegisters() {
        long lRegister = 4;
        short imm = -2;

        // Promote short to long
        long lImm = imm;

        // Add the two filtering the upper half
        long mask = 0x00000000FFFFFFFFL;
        long filterRegister = mask & lRegister;
        long filterImm =mask & lImm;
        long result = (mask & lRegister) +
                      (mask & lImm);
        System.out.println("Register:"+Long.toBinaryString(lRegister));
        System.out.println("filterRegister:"+Long.toBinaryString(filterRegister));
        System.out.println("Imm:"+Long.toBinaryString(lImm));
        System.out.println("filterImm:"+Long.toBinaryString(filterImm));
        System.out.println("mask:"+Long.toBinaryString(mask));

        System.out.println("Result:"+Long.toBinaryString(result));

        long carryMask = 0x0000000100000000L;
        long carry = result & carryMask;
        System.out.println("CryMsk:"+Long.toBinaryString(carryMask));
        System.out.println("Carry:"+(carry >> 32));

        // Write Carry bit in position 29
        int msr = 0;
        System.out.println("MSR:"+Integer.toBinaryString(msr));
        int newCarry = (int)(carry >> 32);
        int CARRY_SHIFT = 2;
        msr = msr | (newCarry << CARRY_SHIFT);
        System.out.println("MSR:"+Integer.toBinaryString(msr));

    }

    public static void shortExt() {
                int register = 4;
        short imm = -2;

        // Extension
        System.out.println("imm:"+Integer.toBinaryString(imm));
        int simm = imm;
        System.out.println("imm:"+Integer.toBinaryString(simm));



        //long l = i;

        //System.out.println("Boxing:"+l);
        //l = 0xFFFF0000 & l;
        //System.out.println("Filter:"+l);

    }

    private static void testAdd() {
        String opName = "addck";
        String[] regs = {"r3", "r1", "r2"};
        Integer imm = null;

        TraceData data = new SimpleTraceData(opName, regs, imm);

        MbAdd add = new MbAdd(data);

        System.out.println(add.toString());
    }

    /**
     * Tests for class Extra
     */
    private static void testExtra() {

        int rA = 1;
        int rB = -1;
        int carry = 0;

        int carryOut = Extra.getCarryOut(rA, rB, carry);

        System.out.println("CarryOut:"+carryOut);
    }

}
