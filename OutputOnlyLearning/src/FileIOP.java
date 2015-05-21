import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FileIOP {

	private String path;

	public FileIOP(String filename) {
		String path = getClass().getResource(filename).getFile();
		if (path.substring(0, 1).equals("/"))
			path = path.substring(1);
		path = path.replaceAll("%20", " ");
		this.path = path;
	}

	/**
	 * value of line
	 *
	 * @param path
	 * @param line
	 * @return
	 * @throws IOException
	 */
	public int readSingleValue(int line)
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
		return Integer.parseInt(result); // no test, if result is a number
	}

	/**
	 * learn data of table inclusive targets
	 *
	 * @param path
	 * @return table[row][coloumn]
	 * @throws IOException
	 */
	public double[][] readTable() throws IOException {
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

	private double[] StringArrayToNumArray(String line) {
		String[] str = line.split(",");
		double[] num = new double[str.length];
		for (int i = 0; i < str.length; i++) {
			num[i] = Double.parseDouble(str[i]);
		}
		return num;
	}

}
