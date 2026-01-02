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
 *

public final class Base64Codec {
  /**
   * Base64 encoding table.
   * Index (0–63) maps directly to Base64 characters.
   */
  private static final char[] ENCODE_TABLE =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();

  
}
