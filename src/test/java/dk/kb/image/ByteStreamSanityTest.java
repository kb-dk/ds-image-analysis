package dk.kb.image;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.*;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ByteStreamSanityTest {
    private Logger log = LoggerFactory.getLogger(this.toString());

    @Test
    public void testOutputToInputByteStreams(){
        int[] testInts = new int[]{200,220,117,2};
        byte[] testBytes = new byte[]{-56, -36, 117, 2};

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        for (int i = 0; i < testInts.length; i++){
            out.write((byte) testInts[i]);
        }

        ByteArrayInputStream in = new ByteArrayInputStream(testBytes);
        byte[] inArray = in.readAllBytes();

        // Tests converting int to byte and byte to int
        for (int i = 0; i < inArray.length; i++) {
            assertEquals((byte) testInts[i], inArray[i]);
            assertEquals(testInts[i], Byte.toUnsignedInt(inArray[i]));
        }
        log.info("Input and output byte from streams are alike.");
    }
}
