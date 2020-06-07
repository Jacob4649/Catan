package catan.engine.board;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;

import catan.engine.board.tile.Tile;
import catan.engine.board.tile.TileNotInitializedException;
import catan.engine.board.tile.TileType;

public class IO {

	public static final String BOARD_DIRECTORY = "/Layouts/";
	public static final String BOARD_NAME = "Layout";
	public static final String BOARD_EXTENSION = ".clayout"; // . Catan Layout

	/**
	 * 
	 * @return an array of {@link File}s storing {@link Board} layouts
	 */
	public static File[] getSavedBoards() {
		ArrayList<File> output = new ArrayList<File>(Arrays.asList(new File(BOARD_DIRECTORY).listFiles()));
		int i = 0;
		while (i < output.size()) {
			if (output.get(i).getName().substring(output.get(i).getName().length() - BOARD_EXTENSION.length())
					.equalsIgnoreCase(BOARD_EXTENSION)) {
				i++;
			} else {
				output.remove(i);
			}
		}
		File[] boards = new File[output.size()];
		boards = output.toArray(boards);
		return boards;
	}

	/**
	 * Reads a {@link Board} from a {@link File}
	 * 
	 * @param file
	 *            the {@link File} to read from
	 * @return the {@link Board} read
	 * @throws IOException
	 *             if something goes wrong while reading
	 */
	public static Board readBoard(File file) throws IOException {
		byte[] read = Files.readAllBytes(file.toPath());
		int rowSize = read[0];
		Tile[][] map = new Tile[read.length / rowSize][rowSize];
		for (int i = 0; i < read.length / rowSize; i++) {
			for (int j = 0; j < rowSize; j++) {
				int frequency = 0xF0 & read[i * rowSize + j + 1];
				frequency >>>= 4;
				TileType type = TileType.getWithValue(0xF & read[i * rowSize + j + 1]);
				map[i][j] = new Tile(frequency, type);
			}
		}
		return new Board(map);
	}

	/**
	 * Reads a {@link Board} from a {@link File}
	 * 
	 * @param boardName
	 *            the name of the {@link File} to read from
	 * @return the {@link Board} read
	 * @throws IOException
	 *             if something goes wrong while reading
	 */
	public static Board readBoard(String boardName) throws IOException {
		return readBoard(new File(boardName));
	}

	/**
	 * Writes a {@link Board} to the specified {@link File}
	 * 
	 * @param board
	 *            the {@link Board} to save
	 * @param file
	 *            the {@link File} to write to
	 * @return true if successful
	 */
	public static boolean writeBoard(Board board, File file)
			throws BoardNotInitializedException, TileNotInitializedException {
		try {
			file.getParentFile().mkdirs();
			file.createNewFile();
		} catch (IOException e) {
			return false;
		}
		try (FileOutputStream stream = new FileOutputStream(file)) {
			stream.write(new Integer(board.getDimensions()[1]).byteValue());
			for (int i = 0; i < board.getDimensions()[0]; i++) {
				for (int j = 0; j < board.getDimensions()[1]; j++) {
					byte toWrite = 0x0;
					byte tileType = new Integer(board.getTileAt(i, j).getTileType().getValue()).byteValue();
					toWrite = new Integer(board.getTileAt(i, j).getFrequency()).byteValue();
					toWrite <<= 4;
					toWrite |= tileType;
					stream.write(toWrite);
				}
			}
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	/**
	 * Writes a {@link Board} to a {@link File} with the specified name
	 * 
	 * @param board
	 *            the {@link Board} to save
	 * @param fileName
	 *            the name of the {@link File} to create and write to
	 * @return true if successful
	 */
	public static boolean writeBoard(Board board, String fileName)
			throws BoardNotInitializedException, TileNotInitializedException {
		return writeBoard(board, new File(fileName));
	}

	/**
	 * Writes a {@link Board} to a {@link File} with the default board file name
	 * 
	 * @param board
	 *            the {@link Board} to save
	 * @return true if successful
	 */
	public static boolean writeBoard(Board board) throws BoardNotInitializedException, TileNotInitializedException {
		int i = 0;
		while (new File(getFileName(i)).exists()) {
			i++;
		}
		return writeBoard(board, getFileName(i));
	}

	/**
	 * 
	 * @return a fileName for the ith layout {@link File}
	 */
	private static String getFileName(int i) {
		return System.getProperty("user.dir") + "/" + BOARD_DIRECTORY + BOARD_NAME + (i != 0 ? "_" + i : "") + BOARD_EXTENSION;
	}

}
