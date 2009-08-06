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
import javax.imageio.stream.MemoryCacheImageInputStream;
import org.ancora.MicroblazeInterpreter.Commons.BitOperations;

/**
 * Memory with cache for a data memory made of MemorySegments.
 *
 * @author Joao
 */
public class CachedSegments implements DataMemory {

    /**
     * Creates a CachedSegments prepared to alocate the number of
     * estimatedNumberSegments. Segments are created on demand.
     * 
     * @param estimateNumberSegments
     */
    /*
    public CachedSegments(int estimatedNumberSegments) {
    HashMap
    }
     */
    /**
     * 
     */
    public CachedSegments() {
        segments = new HashMap<Integer, MemorySegment>();

        // Stats
        accesses = 0;
        misses = 0;
        noSegment = 0;

        // Cache
        cachedAddress = -1;

    }

    public int loadWord(int wordAddress) {
        return getSegment(wordAddress).loadWord(wordAddress);
    }

    public void storeWord(int wordAddress, int value) {
        getSegment(wordAddress).storeWord(wordAddress, value);
    }

    public boolean isWordWritten(int wordAddress) {
        return getSegment(wordAddress).isWordWritten(wordAddress);
    }

    /**
     * Returns the segments corresponding to the given word-aligned address.
     * The segment is created, if it doesn't already exist.
     * 
     * @param segmentAddress
     * @return
     */
    public MemorySegment getSegment(int wordAddress) {
        final int segmentAddress = segmentAddress(wordAddress);
        System.out.println("Address:"+wordAddress);
//        System.out.println("SegmentAddress:"+segmentAddress);

        MemorySegment segment = getSegmentFromCache(segmentAddress);
        //return segments.get(segmentAddress);

        // Check if segment exists
        if (segment != null) {
            return segment;
        }

        // Create segment and store it in table.
        segment = new MemorySegment(SEGMENT_SIZE_POWER);
        segments.put(segmentAddress, segment);
        return getSegmentFromCache(segmentAddress);

    }

    /**
     * Converts a word-aligned address into a segment address.
     * 
     * @param wordAddress
     * @return
     */
    private int segmentAddress(int wordAddress) {
        return wordAddress >>> SEGMENT_SIZE_POWER >>> MemorySegment.WORD_POWER;
    }

    /**
     * Returns a segment.
     *
     * @param segmentAddress
     * @return
     */
    private MemorySegment getSegmentFromCache(int segmentAddress) {
        // Count access
        accesses++;

        // Verify if segment is in cache
        if (cachedAddress == segmentAddress) {
            return cachedSegment;
        }

        // It is not in cache. Verify if segment exists
        MemorySegment segment = segments.get(segmentAddress);

        if (segment == null) {
            noSegment++;
            return segment;
        }

        // Cache the new segment. There's no need to "store" the previously
        // cached segment because writes in cached value is a reference.
        cachedAddress = segmentAddress;
        cachedSegment = segment;
        misses++;
        return segment;
    }

    public String stats() {
        StringBuilder builder = new StringBuilder(100);

        float hitrate = (float) (accesses - misses - noSegment) / (float) (accesses);
        hitrate *= 100;

        builder.append("Total Acesses:" + accesses + "\n");
        builder.append("Hit-rate:" + hitrate + "\n");
        builder.append("Number of created segments:" + segments.size() + "\n");

        return builder.toString();
    }

    /**
     * @return an array with the word-aligned addresses which have been written.
     */
    public int[] writtenWordAddresses() {
        // Collect the written words addresses
        Map<Integer, int[]> words = getWrittenWords();
        // Calculate total number of words
        int totalWords = countNumberOfWrittenWords(words);

        // Create array
        int[] fullWords = new int[totalWords];
        // Assign each word to the array
        int index = 0;
        for (Integer key : words.keySet()) {
            int[] segWords = words.get(key);
            for (int segIndex = 0; segIndex < segWords.length; segIndex++) {
                fullWords[index] = recoverWordAddress(key, segWords[segIndex]);
                index++;
            }
        }


        return fullWords;
    }

    /**
     * Recoves the word-aligned byte address from the segment address and the
     * word index within the segment.
     * @param segmentAddress
     * @param wordIndex
     * @return
     */
    private int recoverWordAddress(int segmentAddress, int IndexAddress) {
        System.out.println("Segment Address:"+segmentAddress);
        System.out.println("Word Index:"+IndexAddress);
        // Calculate the correct address
        final int lowerBits = IndexAddress;
        final int higherBitsPosition = SEGMENT_SIZE_POWER + MemorySegment.WORD_POWER;
        final int higherBitsSize = INT_SIZE_BITS - higherBitsPosition;
        return BitOperations.writeBits(higherBitsPosition,
                higherBitsSize, segmentAddress, lowerBits);

    }

    /**
     * Get all the written words, mapped by segment address.
     *
     * @return
     */
    private Map<Integer, int[]> getWrittenWords() {
        Map<Integer, int[]> words = new HashMap<Integer, int[]>();

        for (Integer key : segments.keySet()) {
            MemorySegment seg = segments.get(key);
            words.put(key, seg.writtenWordAddresses());
        }

        return words;
    }

    /**
     * Counts the number of words written in this memory.
     *
     * @return
     */
    private int countNumberOfWrittenWords(Map<Integer, int[]> words) {
        int numberOfWords = 0;

        for (int[] segWords : words.values()) {
            numberOfWords += segWords.length;
        }

        return numberOfWords;
    }
    //INSTANCE VARIABLES
    // State
    private final Map<Integer, MemorySegment> segments;
    // Stats
    private int accesses;
    private int misses;
    private int noSegment;
    // Cache
    private int cachedAddress;
    private MemorySegment cachedSegment;
    //Constants
    /**
     * Exponent of a base two power. Size of the segment, in words.
     */
    public final int SEGMENT_SIZE_POWER = 10;
    private final int INT_SIZE_BITS = 32;
}
