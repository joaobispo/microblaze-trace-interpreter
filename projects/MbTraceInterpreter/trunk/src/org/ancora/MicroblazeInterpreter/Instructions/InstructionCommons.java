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

import org.ancora.jCommons.Console;
import org.ancora.jCommons.DefaultConsole;


/**
 * Contains static methods used in MicroBlaze instructions.
 *
 * @author Joao Bispo
 */
public class InstructionCommons {

    /**
     * Calculates the carryOut of the sum of rA with rB and carry.
     *
     * @param rA
     * @param rB
     * @param carry the carry from the previous operation. Should be 0 or 1.
     * @return 1 if there is carry out, or 0 if not.
     */
    static int getCarryOut(int rA, int rB, int carry) {
        if(carry != 0 && carry != 1) {
            console.warn("getCarryOut: Carry is different than 0 or 1 ("+
                    carry+")");
        }

        System.out.println("rA:"+Integer.toBinaryString(rA));
        System.out.println("rB:"+Integer.toBinaryString(rB));

        // Extend operands to long and mask them
        long lRa = rA & MASK_32_BITS;
        long lRb = rB & MASK_32_BITS;
        // Carry must be 0 or 1, it shouldn't need to be masked.
        long lCarry = carry;


        System.out.println("lRa:"+Long.toBinaryString(lRa));
        System.out.println("lRb:"+Long.toBinaryString(lRb));

        // Do the summation
        long result = lRa + lRb + lCarry;

        System.out.println("Result:"+Long.toBinaryString(result));

        // Get the carry bit
        int carryOut = (int) ((result & MASK_BIT_33) >> 32);
        return carryOut;
    }

    private static final long MASK_32_BITS = 0xFFFFFFFFL;
    private static final long MASK_BIT_33 = 0x100000000L;
    private static final Console console = DefaultConsole.getConsole();

}
