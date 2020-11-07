package models;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * This class represents the model which is responsible of outputting
 * logs to an external file
 * @author Team 4
 *
 */
public class LogToFileModel {

	static Logger logger;
	private Handler fileHandler;
	private Formatter plainText;

	/**
	 * Constructor which setups the logger, the file handler and the formatter
	 * @throws IOException
	 */
	private LogToFileModel() throws IOException {
		logger = Logger.getLogger(LogToFileModel.class.getName());
		fileHandler = new FileHandler("logs.txt");
		plainText = new SimpleFormatter();
		fileHandler.setFormatter(plainText);
		logger.addHandler(fileHandler);
	}

	/**
	 * Getter to obtain a logger
	 * @return logger which is needed to log messages
	 */
	private static Logger getLogger() {
		if(logger == null){
			try {
				new LogToFileModel();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return logger;
	}

	/**
	 * This methods logs a message to an external file
	 * @param level indicates the level of the message (Severe, Warning, Info)
	 * @param msg the message that the user wants to log
	 */
	public static void log(Level level, String msg) {
		getLogger().log(level, msg);
	}
}
