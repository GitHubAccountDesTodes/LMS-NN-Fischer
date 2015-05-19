

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FileIOP {

	/**
	 * value of line
	 * @param path
	 * @param line
	 * @return 
	 * @throws IOException
	 */
	static public double readSingleValue(String path, int line)
			throws IOException {
		FileReader fr;
		fr = new FileReader(path);
		BufferedReader br = new BufferedReader(fr);
		for (int i = 0; i < line - 1; i++) {
			br.readLine();
		}
		String result = br.readLine();
		br.close();
		fr.close();
		return Double.parseDouble(result); // no test, if result is a number
	}

	/**
	 * learn data of table inclusive targets
	 * @param path
	 * @return 
	 * @throws IOException
	 */
	static public double[][] readTable(String path) throws IOException {
		FileReader fr;
		fr = new FileReader(path);
		BufferedReader br = new BufferedReader(fr);
		// read number of inputs
		br.readLine();
		// read number of hidden neurons
		br.readLine();
		//size of table unknown -> ArrayList
		ArrayList<double[]> result = new ArrayList<double[]>();
		while (br.ready()) {
			result.add(StringArrayToNumArray(br.readLine()));
		}
		br.close();
		fr.close();
		return result.toArray(new double[result.size()][]);
	}

	private static double[] StringArrayToNumArray(String line) {
		String[] str = line.split(",");
		double[] num = new double[str.length];
		for (int i = 0; i < str.length; i++) {
			num[i] = Double.parseDouble(str[i]);
		}
		return num;
	}
}
