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

package org.ancora.MicroblazeInterpreter.HardwareBlocks;

/**
 * Represents the special purpose registers of the MicroBlaze processor.
 *
 * @author Joao Bispo
 */
public enum SpecialRegister {

   rpc(0x0000),
   rmsr(0x001);
   //rear(),
   //resr(),
   //rfsr(),
   //rbtr(),
   //redr(),
   //rpid(),
   //rzpr(),
   //rtlblo(),
   //rtlbhi(),
   //rtlbx(),
   //rpvr0(),
   //rpvr1(),
   //rpvr2(),
   //rpvr3(),
   //rpvr4(),
   //rpvr5(),
   //rpvr6(),
   //rpvr7(),
   //rpvr8(),
   //rpvr9(),
   //rpvr10(),
   //rpvr11();

   private SpecialRegister(int address) {
      this.address = address;
   }

   /**
    * @return the address of this register
    */
   public int getAddress() {
      return address;
   }



   // INSTANCE VARIABLES
   private final int address;
}
