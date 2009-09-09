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

package org.ancora.MicroblazeInterpreter.Support;

import java.util.HashMap;
import java.util.Map;

/**
 * Counts the occurence of numbers.
 *
 * @author Joao Bispo
 */
public class NumberCounter {

   public NumberCounter() {
      count = new HashMap<Integer, Integer>();
   }

   /**
    * Adds a number to the table. If the number is already there as a key, the
    * value associated increments by one.
    *
    * @param number
    */
   public void addInt(int number) {
      boolean containsKey = count.containsKey(number);

      if(containsKey) {
         int value = count.get(number);
         value++;
         count.put(number, value);
      }
      else {
         count.put(number, 1);
      }
   }

   public Map<Integer,Integer> getTable() {
      return count;
   }

   // INSTANCE VARIABLES
   private final Map<Integer,Integer> count;
}
