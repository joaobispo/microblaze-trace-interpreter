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

package org.ancora.MicroblazeInterpreter.Commons;

import org.ancora.jCommons.Console;
import org.ancora.jCommons.DefaultConsole;

/**
 * Extra methods not found in jCommons.
 *
 * @author Joao Bispo
 */
public class Arithmetic {

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
   public static Long hexa2long(String hexadecimal) {

      Long decodedAddress;
      try{
         decodedAddress = Long.valueOf(hexadecimal, 16);
      } catch(NumberFormatException ex) {
         console.warn("hexa2long: Could not decode hexadecimal " +
                 "string ("+hexadecimal+")");
         decodedAddress = null;
      }

      return decodedAddress;
   }


   // INSTANCE VARIABLES
   private static Console console = DefaultConsole.getConsole();
}
