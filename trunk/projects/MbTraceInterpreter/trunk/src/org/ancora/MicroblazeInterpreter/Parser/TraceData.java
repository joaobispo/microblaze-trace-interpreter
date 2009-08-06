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
 * Interface for a Data Holder. Contains the data of a single trace instruction.
 *
 * @author Joao Bispo
 */
public interface TraceData {


   /**
    * @return the name of the instruction
    */
   public String getOpName();
   /**
    * @return the name of the destination register, or null if not defined
    */
   public String getR1();
   /**
    * @return the name of the first source register, or null if not defined
    */
   public String getR2();
   /**
    * @return the name of the second source register, or null if not defined
    */
   public String getR3();
   /**
    * @return the immediate value, or null if not defined
    */
   public Integer getImm();

   /**
    * The maximum number of registers a MicroBlaze trace instruction can have.
    */
   public static int MAX_REGISTERS = 3;
}
