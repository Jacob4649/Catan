package catan.renderer.window.menu;

import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import java.security.Key;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import catan.Catan;
import catan.engine.board.BoardNotInitializedException;
import catan.engine.board.objects.InvalidLocationException;
import catan.engine.board.tile.TileNotInitializedException;
import catan.engine.board.tile.VertexNotInitializedException;
import catan.engine.player.PlayerCountOutOfBoundsException;
import catan.engine.player.PlayerIndexOutOfBoundsException;
import javax.swing.JTextField;
import java.awt.Color;

/**
 * Class representing the application's main menu
 * 
 * @author Jacob
 *
 */
public class MainMenu {

	private static final String PASSWORD_HASH = EncryptionUtils.base64Hash(EncryptionUtils.getRandomSalt(), "Catan123");
	
	private JFrame m_frame;
	private MapsMenu m_maps;
	private JTextField password;

	/**
	 * Create a new {@link MainMenu}
	 */
	public MainMenu() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		m_frame = new JFrame();
		m_frame.setBounds(100, 100, 450, 300);
		m_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		m_frame.setResizable(false);
		m_frame.getContentPane().setLayout(null);

		JLabel menuLabel = new JLabel("Jacob's Catan");
		menuLabel.setFont(new Font("Tahoma", Font.PLAIN, 32));
		menuLabel.setHorizontalAlignment(SwingConstants.CENTER);
		menuLabel.setBounds(10, 11, 414, 51);
		m_frame.getContentPane().add(menuLabel);

		JButton newGame = new JButton("New Game");
		newGame.setEnabled(false);
		newGame.setBounds(10, 73, 100, 23);

		newGame.addActionListener((performedAction) -> {
			setVisible(false);
			new GameSetupMenu(this);
		});

		m_frame.getContentPane().add(newGame);

		JButton quickGame = new JButton("Quickstart Game");
		quickGame.setEnabled(false);
		quickGame.setBounds(120, 73, 194, 23);

		quickGame.addActionListener((performedAction) -> {
			m_frame.setVisible(false);
			new Thread(() -> {
				try {
					Catan.startCatan(this);
				} catch (PlayerCountOutOfBoundsException | PlayerIndexOutOfBoundsException
						| BoardNotInitializedException | TileNotInitializedException | VertexNotInitializedException
						| InvalidLocationException e) {
					e.printStackTrace();
					System.exit(0);
				}
			}).start();
		});

		m_frame.getContentPane().add(quickGame);

		JButton maps = new JButton("Maps");
		maps.setEnabled(false);
		maps.setBounds(324, 73, 100, 23);

		maps.addActionListener((performedAction) -> {
			m_maps = new MapsMenu();
		});

		m_frame.getContentPane().add(maps);

		JButton rules = new JButton("Rules");
		rules.setBounds(10, 227, 100, 23);

		rules.addActionListener((performedAction) -> {
			new Rules();
		});

		m_frame.getContentPane().add(rules);

		JButton quit = new JButton("Quit");
		quit.setBounds(324, 227, 100, 23);

		quit.addActionListener((performedAction) -> {
			System.exit(0);
		});

		m_frame.getContentPane().add(quit);

		JLabel passwordLabel = new JLabel("Password:");
		passwordLabel.setHorizontalAlignment(SwingConstants.CENTER);
		passwordLabel.setBounds(10, 158, 100, 14);
		m_frame.getContentPane().add(passwordLabel);

		password = new JTextField();
		password.setBounds(120, 155, 194, 20);
		m_frame.getContentPane().add(password);
		password.setColumns(10);

		JButton unlock = new JButton("Unlock");
		unlock.setBounds(324, 154, 100, 23);
		m_frame.getContentPane().add(unlock);

		JLabel incorrect = new JLabel("Incorrect Password");
		incorrect.setForeground(Color.RED);
		incorrect.setHorizontalAlignment(SwingConstants.CENTER);
		incorrect.setBounds(120, 186, 194, 14);
		incorrect.setVisible(false);
		m_frame.getContentPane().add(incorrect);

		unlock.addActionListener((performedAction) -> {
			if (EncryptionUtils.compareToHash(password.getText().trim(), PASSWORD_HASH)) {
				unlock.setEnabled(false);
				password.setEnabled(false);
				incorrect.setVisible(false);
				maps.setEnabled(true);
				quickGame.setEnabled(true);
				newGame.setEnabled(true);
			} else {
				incorrect.setVisible(true);
			}
			password.setText("");
		});
		
		m_frame.setVisible(true);
	}

	/**
	 * Sets the visibility of this window
	 * 
	 * @param visible
	 *            true if visible
	 */
	public void setVisible(boolean visible) {
		m_frame.setVisible(visible);
	}

	/**
	 * Collection of useful encryption and hashing methods
	 * 
	 * @author Jacob
	 *
	 */
	private static class EncryptionUtils {
		private static final int PASSWORD_SALT_LENGTH = 16;
		private static final int PASSWORD_HASH_LENGTH = 16;
		private static final int PASSWORD_HASH_BIT_LENGTH = PASSWORD_HASH_LENGTH * 8;

		private static final int HASH_REPITITIONS = 16;

		/**
		 * Gets a random, cryptographically secure salt
		 * 
		 * @return A byte array containing the salt
		 */
		public static byte[] getRandomSalt() {
			SecureRandom random = new SecureRandom();
			byte[] salt = new byte[PASSWORD_SALT_LENGTH];
			random.nextBytes(salt);
			return salt;
		}

		/**
		 * Hashes the specified password with the specified salt using the SHA-1
		 * algorithm
		 * 
		 * @param salt
		 *            the salt to use when hashing
		 * @param password
		 *            the password to hash
		 * @return an array containing the salt in the first few bytes and the
		 *         hash following this
		 */
		public static byte[] hash(byte[] salt, String password) {
			byte[] hash = new byte[PASSWORD_HASH_LENGTH + salt.length];
			try {
				SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
				PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, HASH_REPITITIONS,
						PASSWORD_HASH_BIT_LENGTH);
				Key key = factory.generateSecret(keySpec);
				byte[] keyBytes = key.getEncoded();
				for (int i = 0; i < salt.length; i++) {
					hash[i] = salt[i];
				}
				for (int i = 0; i < keyBytes.length; i++) {
					hash[salt.length + i] = keyBytes[i];
				}
			} catch (Exception e) {

			}
			return hash;
		}

		/**
		 * Hashes and salts the specified password and compares to specified
		 * hash
		 * 
		 * @param password
		 *            the password to compare
		 * @param hashString
		 *            a base-64 {@link String} containing the hash and salt
		 * @return true if equal
		 */
		public static boolean compareToHash(String password, String hashString) {
			// Decodes hash into byte array
			byte[] hashAndSalt = Base64.getDecoder().decode(hashString);
			// Extracts salt from old hash
			byte[] salt = Arrays.copyOfRange(hashAndSalt, 0, PASSWORD_SALT_LENGTH);
			// Hashes new password
			byte[] newHashAndSalt = hash(salt, password);
			// Checks if hashes are equal
			return Arrays.equals(hashAndSalt, newHashAndSalt);
		}

		/**
		 * Hashes the specified password with the specified salt using the SHA-1
		 * algorithm
		 * 
		 * @param salt
		 *            the salt to use when hashing
		 * @param password
		 *            the password to hash
		 * @return the hashed and salted password encoded in a base-64
		 *         {@link String}
		 */
		public static String base64Hash(byte[] salt, String password) {
			return Base64.getEncoder().encodeToString(hash(salt, password));
		}
	}

}
