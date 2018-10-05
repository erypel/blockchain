package DictionariesAndHashMaps;
import java.io.*;
import java.util.*;
import java.util.stream.*;
import static java.util.stream.Collectors.toList;

public class CountTriplets {

    // Complete the countTriplets function below.
    static long countTriplets(List<Long> arr, long r) {
        long count = 0;
        Iterator<Long> itr = arr.iterator();
        HashMap<Long, Long> h1 = new HashMap<Long, Long>();
        HashMap<Long, Long> h2 = new HashMap<Long, Long>();
        while(itr.hasNext())
        {
            long next = itr.next();
            if(next % r == 0){
                long pre = next / r;
                Long cnt2 = h2.get(pre);
                if (cnt2 != null) count += cnt2;
                
                Long cnt1 = h1.get(pre);
                if (cnt1 != null) h2.put(next, h2.getOrDefault(next, 0L) + cnt1);
            }
            h1.put(next, h1.getOrDefault(next, 0L) + 1);
        }
        return count;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        String[] nr = bufferedReader.readLine().replaceAll("\\s+$", "").split(" ");

        int n = Integer.parseInt(nr[0]);

        long r = Long.parseLong(nr[1]);

        List<Long> arr = Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
            .map(Long::parseLong)
            .collect(toList());

        long ans = countTriplets(arr, r);

        bufferedWriter.write(String.valueOf(ans));
        bufferedWriter.newLine();

        bufferedReader.close();
        bufferedWriter.close();
    }
}

