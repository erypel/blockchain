package DictionariesAndHashMaps;
import java.io.*;
import java.util.*;
import java.util.regex.*;
import static java.util.stream.Collectors.joining;


public class FrequencyQuery {

	// Complete the freqQuery function below.
	// to speed up you can add a HM of frequency occurences
	static List<Integer> freqQuery(List<int[]> queries) {
		Iterator<int[]> itr = queries.iterator();
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		List<Integer> freqList = new ArrayList<Integer>();
		while (itr.hasNext()) {
			int[] next = itr.next();
			int op = (Integer) next[0];
			int val = (Integer) next[1];
			if (op == 1) // insert
			{
				if (map.containsKey(val)) {
					int freq = map.get(val) + 1;
					map.put(val, freq);
				} else {
					map.put(val, 1);
				}
			} else if (op == 2) // delete
			{
				if (map.containsKey(val)) {
					int freq = map.get(val);
					map.put(val, freq > 0 ? freq-- : 0);
				}
			} else if (op == 3) // does count exist?
			{
				if (map.containsValue(val)) {
					freqList.add(1);
				} else {
					freqList.add(0);
				}
			}
		}
		return freqList;
	}

	public static void main(String[] args) throws IOException {
		try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in))) {
			int q = Integer.parseInt(bufferedReader.readLine().trim());
			List<int[]> queries = new ArrayList<>(q);
			Pattern p = Pattern.compile("^(\\d+)\\s+(\\d+)\\s*$");
			for (int i = 0; i < q; i++) {
				int[] query = new int[2];
				Matcher m = p.matcher(bufferedReader.readLine());
				if (m.matches()) {
					query[0] = Integer.parseInt(m.group(1));
					query[1] = Integer.parseInt(m.group(2));
					queries.add(query);
				}
			}
			List<Integer> ans = freqQuery(queries);
			try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")))) {
				bufferedWriter.write(ans.stream().map(Object::toString).collect(joining("\n")) + "\n");
			}
		}
	}
}
