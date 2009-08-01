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

import org.ancora.jCommons.Console;
import org.ancora.jCommons.DefaultConsole;


/**
 * From a String with a MibroBlaze trace instruction, it can do two things:
 *
 * <p>1)Extract the memory address of the instruction;
 * <br>2)Parse its information, storing the values in his instance variables.
 * 
 * @author Joao Bispo
 */
public class InstructionParser implements TraceData {

   public InstructionParser() {
      opName = null;
      rD = null;
      rA = null;
      rB = null;
      imm = null;
   }

   /**
    * Parse its information, storing the values in his instance variables.
    *
    * <p>This method assumes that the instruction begins that the 13th
    * character (10 chars for instruction address + 2 whitespaces).
    *
    * @param traceInstruction a trace instruction
    */
   public void parseInstruction(String traceInstruction) {
      cleanVariables();

      System.out.println(traceInstruction.substring(12));
   }

   /**
    * Extracts the memory address from the given instruction. 
    *
    * <p>The method assumes that the address are at the beginning of the String
    * and that they start with "0x" and have 8 hexadecimal digits.
    *
    * @param traceInstruction a trace instruction
    * @return a string with an hexadecimal representation of the memory
    */
   public static String getMemoryAddress(String traceInstruction) {

      // Old Assumptions
      //    * <p>The method assumes that the trace instruction:
      //    * <br>Starts with "0x", with the address immediately following;
      //    * <br>Has a whitespace immediately after the address;

      // If no address is      * found,      returns           null.
      //, or null * if an address could not be extracted

      /*
      final String prefix = "0x";

      String workString = traceInstruction.substring(prefix.length());
      // Find the first whitespace
      int whitespaceIndex = 0;
      for(int i=0; i<workString.length(); i++) {
         if(workString.charAt(i))
      }
       */
      final int beginIndex = "0x".length();
      final int endIndex = "0x".length() + 8;

      return traceInstruction.substring(beginIndex, endIndex);
   }

   /**
    * Extracts the memory address from the given instruction. If the address is
    * not valid, returns -1.
    * 
    * <p>A long is used because Java as no support for unsigned types. 
    * Therefore, a long is used to store an unsigned integer.
    * 
    * 
    * @param traceInstruction a trace instruction
    * @return a long with the value of the memory address, or -1 if an address
    * is not valid
    */
   /*
   public static long getMemoryAddressDecoded(String traceInstruction) {

      String address = getMemoryAddress(traceInstruction);
      long decodedAddress;
      try{
         decodedAddress = Long.valueOf(address, 16);
      } catch(NumberFormatException ex) {
         console.warn("getMemoryAddressDecoded: Could not decode hexadecimal " +
                 "string ("+address+")");
         decodedAddress = -1;
      }

      return decodedAddress;
   }
    */

   /**
    * Cleans the variables
    */
   private void cleanVariables() {
      opName = null;
      rD = null;
      rA = null;
      rB = null;
      imm = null;
   }

   public String getOpName() {
      return opName;
   }

   public String getRd() {
      return rD;
   }

   public String getRa() {
      return rA;
   }

   public String getRb() {
      return rB;
   }

   public Short getImm() {
      return imm;
   }


   //INSTANCE VARIABLES

   // State
   String opName;
   String rD;
   String rA;
   String rB;
   Short imm;


   //private static Console console = DefaultConsole.getConsole();

}
