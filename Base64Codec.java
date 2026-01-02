/**
 * Production-ready Base64 Encoder and Decoder.
 *
 * This class provides RFC 4648 compliant Base64 encoding and decoding.
 * It is designed for correctness, performance, and clarity.
 *
 * Features:
 * - No external dependencies
 * - Fast array-based lookups
 * - Proper padding handling
 * - Ignores whitespace during decoding
 * - Thread-safe (stateless)
 */

public final class Base64Codec {
  /**
   * Base64 encoding table.
   * Index (0–63) maps directly to Base64 characters.
   */
  private static final char[] ENCODE_TABLE =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();

  /**
   * Base64 decoding table.
   *
   * Index: ASCII value (0–255)
   * Value:
   *   0–63 → valid Base64 character
   *   -1   → invalid character
   *   -2   → padding '='
   */
  private static final int[] DECODE_TABLE = new int[256];

  /**
   * Static initializer to build decoding lookup table.
   */
  static {
    // Initialize all entries as invalid
    for (int i = 0; i < 256; i++) {
      DECODE_TABLE[i] = -1;
    }

    // Map Base64 characters to values
    for (int i = 0; i < ENCODE_TABLE.length; i++) {
      DECODE_TABLE[ENCODE_TABLE[i]] = i;
    }

    // Padding character
    DECODE_TABLE['='] = -2;
  }


  /**
   * Private constructor to prevent instantiation.
   */
  private Base64Codec() {
    
  }
  

  /* ============================================================
     ENCODER
     ============================================================ 
  */

  /**
   * Encodes a byte array into a Base64 string.
   *
   * @param input raw binary data
   * @return Base64 encoded string
   */
  public static String encode(byte[] input) {
    if (input == null || input.length == 0) {
       return "";
    }

     // Each 3 bytes become 4 Base64 characters
    int outputLength = ((input.length + 2) / 3) * 4;
    StringBuilder output = new StringBuilder(outputLength);

    int i = 0;

     // Process input in 3-byte blocks (24 bits)
    while (i + 2 < input.length) {
      int chunk =
               ((input[i] & 0xFF) << 16) |
               ((input[i + 1] & 0xFF) << 8) |
               (input[i + 2] & 0xFF);

      output.append(ENCODE_TABLE[(chunk >> 18) & 0x3F]);
      output.append(ENCODE_TABLE[(chunk >> 12) & 0x3F]);
      output.append(ENCODE_TABLE[(chunk >> 6) & 0x3F]);
      output.append(ENCODE_TABLE[chunk & 0x3F]);

      i += 3;
    } 

     // Handle remaining bytes (padding)
    int remaining = input.length - i;

    if (remaining == 1) {
      int chunk = (input[i] & 0xFF) << 16;

      output.append(ENCODE_TABLE[(chunk >> 18) & 0x3F]);
      output.append(ENCODE_TABLE[(chunk >> 12) & 0x3F]);
      output.append('=');
      output.append('=');

    } else if (remaining == 2) {
        int chunk =
                  ((input[i] & 0xFF) << 16) |
                  ((input[i + 1] & 0xFF) << 8);

        output.append(ENCODE_TABLE[(chunk >> 18) & 0x3F]);
        output.append(ENCODE_TABLE[(chunk >> 12) & 0x3F]);
        output.append(ENCODE_TABLE[(chunk >> 6) & 0x3F]);
        output.append('=');
    }

    return output.toString(); 
  }

  /* ============================================================
     DECODER
     ============================================================ 
  */

  /**
   * Decodes a Base64 encoded string back into raw bytes.
   *
   * @param input Base64 encoded string
   * @return decoded byte array
   * @throws IllegalArgumentException if input is invalid
   */
  public static byte[] decode(String input) {
    if (input == null) {
      throw new IllegalArgumentException("Input cannot be null");
    }

    // Remove whitespace (RFC compliant)
    input = input.replaceAll("\\s", "");

    int length = input.length();
    if (length == 0 || length % 4 != 0) {
      throw new IllegalArgumentException("Invalid Base64 length");
    }

    // Count padding characters
    int padding = 0;
    if (length >= 2 && input.charAt(length - 1) == '=') {
      padding++;
      if (input.charAt(length - 2) == '=') {
        padding++;
      }
    }

    int outputLength = (length / 4) * 3 - padding;
    byte[] output = new byte[outputLength];

    int outIndex = 0;

    // Process in 4-character blocks
    for (int i = 0; i < length; i += 4) {
      int c1 = decodeChar(input.charAt(i));
      int c2 = decodeChar(input.charAt(i + 1));
      int c3 = decodeChar(input.charAt(i + 2));
      int c4 = decodeChar(input.charAt(i + 3));

      int chunk =
                (c1 << 18) |
                (c2 << 12) |
                ((c3 & 0x3F) << 6) |
                (c4 & 0x3F);

      output[outIndex++] = (byte) ((chunk >> 16) & 0xFF);

      if (c3 != -2) {
        output[outIndex++] = (byte) ((chunk >> 8) & 0xFF);
      }

      if (c4 != -2) {
        output[outIndex++] = (byte) (chunk & 0xFF);
      }
    }

    return output;
  }
  
  /**
     * Converts a Base64 character into its numeric value.
     *
     * @param ch Base64 character
     * @return Base64 index (0–63) or -2 for padding
     */
    private static int decodeChar(char ch) {
        if (ch > 255 || DECODE_TABLE[ch] == -1) {
            throw new IllegalArgumentException(
                    "Invalid Base64 character: '" + ch + "'"
            );
        }
        return DECODE_TABLE[ch];
    }
  


  /**
   * Entry into main function.
   *
   * @args takes none.
   */
  public static void main(String[] args) {
    
  }   
}
