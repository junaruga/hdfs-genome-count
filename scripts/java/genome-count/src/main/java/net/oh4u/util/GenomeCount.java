package net.oh4u.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by jun.aruga on 1/11/15.
 */
public class GenomeCount {
    private static final int STATUS_OK = 0;
    private static final int STATUS_ERROR = 1;
    private static final String GENOME_FILE = "/tmp/genome.txt";
    private Queue<Map> queues = null;

    public GenomeCount() {

    }

    public void run() throws IOException {

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(GENOME_FILE));
            String line = null;
            this.queues = new ArrayDeque();
            while ((line = reader.readLine()) != null) {
                Map mapObj = this.mapForLine(line);
                this.queues.add(mapObj);
            }

            Map<String, Integer> totalMap = new HashMap();
            while (this.queues.size() > 0) {
                Map<String, Object> queue = this.queues.poll();
                totalMap = this.reduce(totalMap, (Map) queue.get("value"));
            }

            this.printCountedMap(totalMap);
        } finally {
           if (reader != null) {
               try {
                   reader.close();
               }
               catch (IOException e) {
                   // do nothing.
               }
           }
        }
    }

    private Map<String, Map> mapForLine(String line) {
        if (line == null) {
            throw new IllegalArgumentException("line is required.");
        }
        Map countedMap = new HashMap();

        for (int pos = 0; pos < line.length(); pos++) {
            String c = line.substring(pos, pos + 1);
            if (!countedMap.containsKey(c)) {
                countedMap.put(c, new Integer(0));
            }

            int count = ((Integer) countedMap.get(c)).intValue();
            count++;
            countedMap.put(c, new Integer(count));
        }


        Map result = new HashMap();
        result.put("key", line);
        result.put("value", countedMap);

        return result;
    }

    private Map<String, Integer> reduce(Map<String, Integer> totalMap, Map<String, Integer> map) {
        if (totalMap == null || map == null) {
            throw new IllegalArgumentException("totalMap or map is required.");
        }

        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            String c = entry.getKey();
            int count = entry.getValue().intValue();
            if (!totalMap.containsKey(c)) {
                totalMap.put(c, new Integer(0));
            }
            totalMap.put(c, totalMap.get(c).intValue() + count);
        }

        return totalMap;
    }

    private void printCountedMap(Map<String, Integer> countedMap) {
        if (countedMap == null) {
            throw new IllegalArgumentException("countedMap is required.");
        }

        int total = 0;
        Object[] array = countedMap.keySet().toArray();
        Arrays.sort(array);
        for (int i = 0; i < array.length; i++) {
            String key = (String) array[i];

            int count = countedMap.get(key).intValue();
            String message = String.format("%s: %d", key, count);
            System.out.println(message);
            total += count;
        }
        String totalMessage = String.format("Total: %d", total);
        System.out.println(totalMessage);
    }

    public static void main(String[] args) throws Exception {
        int exit_status = STATUS_OK;
        try {
            GenomeCount gc = new GenomeCount();
            gc.run();
        } catch (Exception e) {
            //System.err.println("ERROR: " +  e.getMessage());
            //exit_status = STATUS_ERROR;
            throw e;
        }

        System.exit(exit_status);
    }
}
