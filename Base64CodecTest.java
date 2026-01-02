import java.nio.charset.StandardCharsets;

public final class Base64CodecTest {

    public static void main(String[] args) {
        testEncode();
        testDecode();
        testRoundTrip();
        testWhitespace();
        testInvalidInputs();

        System.out.println("\nAll tests PASSED.");
    }

    /* ============================================================
       ENCODER TESTS
       ============================================================ */

    private static void testEncode() {
        assertEquals("TWFu", Base64Codec.encode("Man".getBytes(StandardCharsets.UTF_8)), "Encode Man");
	assertEquals("TWE=", Base64Codec.encode("Ma".getBytes(StandardCharsets.UTF_8)), "Encode Ma");
	assertEquals("TQ==", Base64Codec.encode("M".getBytes(StandardCharsets.UTF_8)), "Encode M");


        byte[] binary = { (byte) 0xFF, (byte) 0xEE, (byte) 0xDD };
        assertEquals("/+7d", Base64Codec.encode(binary), "Encode binary");
    }

    /* ============================================================
       DECODER TESTS
       ============================================================ */

    private static void testDecode() {
        assertEquals("Man", new String(Base64Codec.decode("TWFu"), StandardCharsets.UTF_8), "Decode TWFu");
	assertEquals("Ma", new String(Base64Codec.decode("TWE="), StandardCharsets.UTF_8), "Decode TWE=");
	assertEquals("M", new String(Base64Codec.decode("TQ=="), StandardCharsets.UTF_8), "Decode TQ==");


        byte[] expected = { (byte) 0xFF, (byte) 0xEE, (byte) 0xDD };
        byte[] actual = Base64Codec.decode("/+7d");
        assertArrayEquals(expected, actual, "Decode binary");
    }

    /* ============================================================
       ROUND-TRIP TEST
       ============================================================ */

   private static void testRoundTrip() {
    String[] inputs = {
            "",
            "Hello",
            "Base64 Test",
            "1234567890",
            "!@#$%^&*()",
            "Unicode ✓ Test"
    };

    for (String s : inputs) {
        byte[] original = s.getBytes(StandardCharsets.UTF_8);
        String encoded = Base64Codec.encode(original);
        byte[] decoded = Base64Codec.decode(encoded);

        assertArrayEquals(original, decoded, "Round-trip: " + s);
    }
}

    /* ============================================================
       WHITESPACE HANDLING
       ============================================================ */

    private static void testWhitespace() {
        String encoded = "T W\nFu";
        assertEquals(
    		"Man",
    		new String(Base64Codec.decode(encoded), StandardCharsets.UTF_8),
    		"Decode with whitespace"
	);

    }

    /* ============================================================
       INVALID INPUT TESTS
       ============================================================ */

    private static void testInvalidInputs() {
        expectException(() -> Base64Codec.decode("ABC"), "Invalid length");
        expectException(() -> Base64Codec.decode("T@Fu"), "Invalid character");
        expectException(() -> Base64Codec.decode(null), "Null input");
    }

    /* ============================================================
       ASSERTION HELPERS
       ============================================================ */

    private static void assertEquals(String expected, String actual, String testName) {
        if (!expected.equals(actual)) {
            fail(testName, expected, actual);
        }
    }

    private static void assertArrayEquals(byte[] expected, byte[] actual, String testName) {
        if (expected.length != actual.length) {
            fail(testName, expected.length, actual.length);
        }
        for (int i = 0; i < expected.length; i++) {
            if (expected[i] != actual[i]) {
                fail(testName, expected[i], actual[i]);
            }
        }
    }

    private static void expectException(Runnable r, String testName) {
        try {
            r.run();
            System.err.println("FAILED: " + testName + " (exception expected)");
            System.exit(1);
        } catch (Exception e) {
            System.out.println("PASSED: " + testName);
        }
    }

    private static void fail(String testName, Object expected, Object actual) {
        System.err.println("FAILED: " + testName);
        System.err.println("Expected: " + expected);
        System.err.println("Actual  : " + actual);
        System.exit(1);
    }
}

