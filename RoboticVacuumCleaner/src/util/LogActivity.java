package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class LogActivity {


	public synchronized void loggingCleaning(String str) {

		// The name of the file to open.
		String fileName = "cleaninglog.txt";

		try {
			// Assume default encoding.
			FileWriter fileWriter = new FileWriter(fileName, true);

			// Always wrap FileWriter in BufferedWriter.
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

			// Note that write() does not automatically
			// append a newline character.
			
			bufferedWriter.write(str+"\r\n");

			// Always close files.
			bufferedWriter.close();
		} catch (IOException ex) {
			System.out.println("Error writing to file '" + fileName + "'");
			// Or we could just do this:
			// ex.printStackTrace();
		}

	}


}
