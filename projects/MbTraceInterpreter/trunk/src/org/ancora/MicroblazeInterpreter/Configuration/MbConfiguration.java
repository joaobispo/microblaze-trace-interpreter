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

package org.ancora.MicroblazeInterpreter.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.ancora.jCommons.Console;
import org.ancora.jCommons.DefaultConsole;

/**
 * Due to the configurability of MicroBlaze, there are configuration parameters,
 * used by some instructions. This class contains that configuration.
 * 
 * @author Joao Bispo
 */
public class MbConfiguration {

   /**
    * Returns a MicroBlaze Configuration loaded with default values.
    */
   public MbConfiguration() {
      MpdParameter[] parameters = MpdParameter.values();
      // Capacity estimate
      int numParameters = parameters.length;
      int capacity = ((numParameters * 5) / 4) + 1;

      // Initialize table with configuration values
      configTable = new HashMap<MpdParameter, String>(capacity);

      for(MpdParameter parameter : parameters) {
         configTable.put(parameter, parameter.getDefault());
      }

      // Update cache
      updateCache();
   }


   /**
    * Override default values with the values in the properties object. The
    * keys of the properties must be the same as the name of the parameters as
    * in the enum MpdParameter.
    *
    * @param properties
    */
   public void loadProperties(Properties properties) {
      Set<String> keys = properties.stringPropertyNames();

      // Add keys to table
      for(String key : keys) {
         // Check if key is a valid parameter
         MpdParameter parameter = null;
         try{
            parameter = MpdParameter.valueOf(key);
         } catch(IllegalArgumentException ex) {
            console.warn("loadProperties: MicroBlaze configuration parameter " +
                    "doesn' exit ("+key+"). Check class "+MpdParameter.class+".");
         }

         if(parameter != null) {
            // Get value
            String value = properties.getProperty(key);
            // Update table
            configTable.put(parameter, value);
         }
      }

      // Update Cache
      updateCache();
   }

   /**
    * Updates all the values of cache.
    */
   private void updateCache() {
      C_USE_MMU = Integer.valueOf(configTable.get(MpdParameter.C_USE_MMU));
   }

   // Getters for cached parameters (commonly used).
   public int C_USE_MMU() {
      return C_USE_MMU;
   }

   // INSTANCE VARIABLES
   // Cache
   private int C_USE_MMU;

   // State
   private final Map<MpdParameter, String> configTable;

   // Utilities
   private final Console console = DefaultConsole.getConsole();
}
