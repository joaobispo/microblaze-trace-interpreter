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

package org.ancora.MicroblazeInterpreter.HardwareBlocks.DataMemory;

import java.util.HashMap;
import java.util.Map;
import org.ancora.MicroblazeInterpreter.Commons.BitOperations;

/**
 * Implementation of DataMemory
 *
 * @author Joao Bispo
 */
public class CachedDataMemory implements DataMemory{

    /**
     * 
     * 
     * @param memorySizeEstimate estimate for the memory size, in MB. Value is
     * rouded up to the next power of two. If more memory is used, the internal 
     * size is adjusted.
     */
    public CachedDataMemory(int memorySizeEstimate) {
        // Determine the next power of two of the memorySizeEstimate
        int nextPower2 = (int) Math.ceil(Math.log(memorySizeEstimate)/Math.log(BASE_2));
        int initialBucketsPower = nextPower2 + MEGABYTE_POWER -
                BUCKET_SIZE_POWER - WORD_POWER;

        // Calculate number of initial buckets
        numInitialBuckets = (int) Math.pow(BASE_2, initialBucketsPower);
        // Initialize memory
        buckets = new HashMap<Integer, Bucket>(numInitialBuckets);

        // Initialize cache with first memory position
        Bucket bucket = new DataBucket(BUCKET_SIZE_POWER);
        bucket.write(0, 0);

        // Store bucket
        buckets.put(0, bucket);
        cachedBucketAddress = 0;
        cachedBucket = bucket;
    }



    public int read(int address) {
        //int wordIndex = address >>> WORD_POWER;
        int bucketAddress = address >>> WORD_POWER >>> BUCKET_SIZE_POWER;

        // Check if bucket is in cache
        if(cachedBucketAddress == bucketAddress) {
            return cachedBucket.read(address);
        }

        // Check if bucket exists
        Bucket bucket = buckets.get(bucketAddress);
        if(bucket != null) {
            // Store current cached bucket
            buckets.put(cachedBucketAddress, cachedBucket);
            // Caches new bucket
            cachedBucket = bucket;
            cachedBucketAddress = bucketAddress;
            return bucket.read(address);
        }

        // If bucket doesn't exist, memory was never written. Create bucket.
        bucket = new DataBucket(BUCKET_SIZE_POWER);
        // Store bucket
        buckets.put(bucketAddress, bucket);
        // Try to read again
        return read(address);
    }

    public void write(int address, int word) {
        int bucketAddress = address >>> WORD_POWER >>> BUCKET_SIZE_POWER;

        // Check if bucket is in cache
        if(cachedBucketAddress == bucketAddress) {
            cachedBucket.write(address, word);
            return;
        }

        // Check if bucket exists
        Bucket bucket = buckets.get(bucketAddress);
        if(bucket != null) {
            // Store current cached bucket
            buckets.put(cachedBucketAddress, cachedBucket);
            // Caches new bucket
            cachedBucket = bucket;
            cachedBucketAddress = bucketAddress;
            bucket.write(address, word);
        }

        // If bucket doesn't exist, memory was never written.
        // Create bucket and write to it.
        bucket = new DataBucket(BUCKET_SIZE_POWER);
        bucket.write(address, word);

        // Store bucket
        buckets.put(bucketAddress, bucket);
    }

    public int[] getWrittenAddresses() {
        // Store cached value
        buckets.put(cachedBucketAddress, cachedBucket);

        // Get the number of buckets
        //int numBuckets = buckets.size();

        // Get the number of written addresses and the addresses
        int numWrittenAddress = 0;
        Map<Integer,int[]> writtenAddressesMap = new HashMap<Integer,int[]>();

        for(Integer bucketAddress : buckets.keySet()) {
            int[] writtenAddress = buckets.get(bucketAddress).writtenPositions();
            numWrittenAddress += writtenAddress.length;
            writtenAddressesMap.put(bucketAddress, writtenAddress);
        }
        
        // Create array with complete addresses
        int[] writtenAddresses = new int[numWrittenAddress];
        // Fill the array
        int index = 0;
        for(Integer bucketAddress : writtenAddressesMap.keySet()) {
            // Get the addresses from the bucket memory
            int[] addresses = writtenAddressesMap.get(bucketAddress);
            
            for(int bitIndex = 0; bitIndex < addresses.length; bitIndex++) {
                // Calculate the correct address
                int lowerBits = addresses[bitIndex];
                int higherBitsPosition = BUCKET_SIZE_POWER+WORD_POWER;
                int higherBitsSize = INT_SIZE - higherBitsPosition;
                int address = BitOperations.writeBits(higherBitsPosition, 
                        higherBitsSize, bucketAddress, lowerBits);
                writtenAddresses[index] = address;
                index++;
            }
            
        }
        
        return writtenAddresses;

    }

    /**
     * Stores a bucket.
     * 
     * @param bucket
     */
    /*
    private void storeBucket(int bucketAddress, Bucket bucket) {
            // Store current cached bucket
            buckets.put(cachedBucketAddress, cachedBucket);
            // Caches new bucket
            cachedBucket = bucket;
            cachedBucketAddress = bucketAddress;
    }
     */

    // INSTANCE VARIABLES
    // State
    private final Map<Integer, Bucket> buckets;
    private final int numInitialBuckets;

    // Cache
    private Bucket cachedBucket;
    private int cachedBucketAddress;

    // Constants
    private final int WORD_POWER = 2;
    private final int MEGABYTE_POWER = 20;
    private final int BUCKET_SIZE_POWER = 10;
    private final int BASE_2 = 2;
    private final int INT_SIZE = 32;


}
