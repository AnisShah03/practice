package test;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.Scanner;

public class TestDecrypt {

    private static final String APP_SECRET_KEY = "LdCZ2bCs5GtRCWB18Aj0t4nLWHponTq4Xiqq7yysstA=";
    private static final int KEY_SIZE = 256;
    private static final int ITERATIONS = 1000;
    private static final int T_LEN = 128;

    private static SecretKeySpec getSecretKey() {
        byte[] keyBytes = Arrays.copyOf(Base64.getDecoder().decode(APP_SECRET_KEY), 32);
        return new SecretKeySpec(keyBytes, "AES");
    }

    public static void main(String[] args) throws Exception {

        System.out.println("\n\n\n------------- Welcome to Encryption Decryption Program -------------");
        System.out.println("                                                 \n" +
                           ",--------.        ,--.                           \n" +
                           "'--.  .--',---. ,-'  '-.,--.--.,--,--.,--.  ,--. \n" +
                           "   |  |  | .-. :'-.  .-'|  .--' ,-.  | \\  `'  /  \n" +
                           "   |  |  \\   --.  |  |  |  |  \\ '-'  | /  /.  \\  \n" +
                           "   `--'   `----'  `--'  `--'   `--`--''--'  '--' \n" +
                           "                                                 \n");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("1 : Backend Decrypt");
            System.out.println("2 : Backend Encrypt");
            System.out.println("3 : UI Decrypt");
            System.out.println("4 : Exit");
            System.out.println("\nSelect From Above: \n");
            int num = scanner.nextInt();
            switch (num) {
                case 1: {
                    System.out.println("Enter value : ");
                    String encryptedGCM = scanner.next();
                    String decryptedGCM = decryptForBackendGCM(encryptedGCM);
                    System.out.println("Decrypted value: " + decryptedGCM + "\n");
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    clipboard.setContents(new StringSelection(decryptedGCM), null);
                    System.out.println("############################\n\n");
                    break;
                }
                case 2: {
                    System.out.println("Enter value : ");
                    String text = scanner.next();
                    String ecryptedGCM = encrypt(text);
                    System.out.println("Ecrypted value: " + ecryptedGCM + "\n");
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    clipboard.setContents(new StringSelection(ecryptedGCM), null);
                    System.out.println("############################\n\n");
                    break;
                }
                case 3: {
                    System.out.println("Enter value : ");
                    String textUI = scanner.next();
                    String decryptedUIGCM = decryptForFrontend(textUI);
                    System.out.println("Decrypted value: " + decryptedUIGCM + "\n");
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    clipboard.setContents(new StringSelection(decryptedUIGCM), null);
                    System.out.println("############################\n\n");
                    break;
                }
                case 4:
                default:
                    System.out.println("##############- SHUTDOWN -##############");
                    System.exit(0);
                    break;
            }
        }
    }

    public static String decryptForBackendGCM(String encryptedText) throws Exception {
        if (encryptedText == null || encryptedText.isEmpty()) {
            return "";
        }

        // 1️⃣ Decode Base64 input
        byte[] cipherWithIv = Base64.getDecoder().decode(encryptedText);
        if (cipherWithIv.length < 16) {
            throw new IllegalArgumentException("Cipher text too short, invalid format.");
        }

        // 2️⃣ Extract first 16 bytes as IV (because copyIVAndCipher stored IV first)
        byte[] iv = Arrays.copyOf(cipherWithIv, 16);
        byte[] ciphertext = Arrays.copyOfRange(cipherWithIv, 16, cipherWithIv.length);

        // 3️⃣ Init cipher for GCM decryption using same SecretKey
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec gcmSpec = new GCMParameterSpec(T_LEN, iv);
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), gcmSpec);

        // 4️⃣ Decrypt
        byte[] decryptedBytes = cipher.doFinal(ciphertext);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    public static String decryptForFrontend(String encryptedText) throws Exception {
        if (encryptedText == null || encryptedText.isEmpty()) {
            return "";
        }

        String[] parts = encryptedText.split(":");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid encrypted format. Expected -> salt:iv:content");
        }

        String salt = parts[0];
        String ivString = parts[1];
        String encryptedContent = parts[2];

        byte[] iv = Base64.getDecoder().decode(ivString);
        byte[] secretKeyRaw = Base64.getDecoder().decode(APP_SECRET_KEY);
        byte[] keyBytes = deriveKey(secretKeyRaw, salt);

        SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));

        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedContent));
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    private static byte[] deriveKey(byte[] appSecretKey, String salt) throws Exception {
        byte[] decodedSalt = Base64.getDecoder().decode(salt);
        String password = Base64.getEncoder().encodeToString(appSecretKey);

        KeySpec spec = new PBEKeySpec(password.toCharArray(), decodedSalt, ITERATIONS, KEY_SIZE);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        return factory.generateSecret(spec).getEncoded();
    }

    public static String encrypt(String password) {
        try {
            if (password == null || password.isEmpty()) {
                return password;
            }
            byte[] iv = new byte[]{(byte) 0x8E, 0x12, 0x39, (byte) 0x9C, 0x07,
                    0x72, 0x6F, 0x5A, (byte) 0x8E, 0x12, 0x39, (byte) 0x9C, 0x07,
                    0x72, 0x6F, 0x5A};

            byte[] passwordInBytes = password.getBytes();
            Cipher encryptionCipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(T_LEN, iv);
            encryptionCipher.init(Cipher.ENCRYPT_MODE, getSecretKey(), gcmParameterSpec);

            byte[] encryptedByte = encryptionCipher.doFinal(passwordInBytes);
            byte[] encryptedWithIV = copyIVAndCipher(encryptedByte, iv);

            return Base64.getEncoder().encodeToString(encryptedWithIV);
        } catch (Exception e) {
            System.out.println("Something went wrong in encryption : " + e.getMessage());
            return null;
        }
    }

    public static byte[] copyIVAndCipher(byte[] encryptedText, byte[] iv) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        os.write(iv);
        os.write(encryptedText);
        return os.toByteArray();
    }
}
