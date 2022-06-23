package root.shop.login.encryption;

import java.io.*;
import java.nio.ByteBuffer;

public abstract class CharacterDecoder {

    /** Return the number of bytes per atom of decoding */
    abstract protected int bytesPerAtom();

    /** Return the maximum number of bytes that can be encoded per line */
    abstract protected int bytesPerLine();

    /** decode the beginning of the buffer, by default this is a NOP. */
    protected void decodeBufferPrefix(PushbackInputStream aStream, OutputStream bStream) throws IOException { }

    /** decode the buffer suffix, again by default it is a NOP. */
    protected void decodeBufferSuffix(PushbackInputStream aStream, OutputStream bStream) throws IOException { }

    /**
     * This method should return, if it knows, the number of bytes
     * that will be decoded. Many formats such as uuencoding provide
     * this information. By default we return the maximum bytes that
     * could have been encoded on the line.
     */
    protected int decodeLinePrefix(PushbackInputStream aStream, OutputStream bStream) throws IOException {
        return (bytesPerLine());
    }

    /**
     * This method post processes the line, if there are error detection
     * or correction codes in a line, they are generally processed by
     * this method. The simplest version of this method looks for the
     * (newline) character.
     */
    protected void decodeLineSuffix(PushbackInputStream aStream, OutputStream bStream) throws IOException { }

    /**
     * This method does an actual decode. It takes the decoded bytes and
     * writes them to the OutputStream. The integer <i>l</i> tells the
     * method how many bytes are required. This is always <= bytesPerAtom().
     */
    protected void decodeAtom(PushbackInputStream aStream, OutputStream bStream, int l) throws Exception {
        throw new Exception();
    }

    /**
     * This method works around the bizarre semantics of BufferedInputStream's
     * read method.
     */
    protected int readFully(InputStream in, byte buffer[], int offset, int len)
            throws java.io.IOException {
        for (int i = 0; i < len; i++) {
            int q = in.read();
            if (q == -1)
                return ((i == 0) ? -1 : i);
            buffer[i+offset] = (byte)q;
        }
        return len;
    }

    public void decodeBuffer(InputStream aStream, OutputStream bStream) throws Exception {
        int     i;
        int     totalBytes = 0;

        PushbackInputStream ps = new PushbackInputStream (aStream);
        decodeBufferPrefix(ps, bStream);
        while (true) {
            int length;

            try {
                length = decodeLinePrefix(ps, bStream);
                for (i = 0; (i+bytesPerAtom()) < length; i += bytesPerAtom()) {
                    decodeAtom(ps, bStream, bytesPerAtom());
                    totalBytes += bytesPerAtom();
                }
                if ((i + bytesPerAtom()) == length) {
                    decodeAtom(ps, bStream, bytesPerAtom());
                    totalBytes += bytesPerAtom();
                } else {
                    decodeAtom(ps, bStream, length - i);
                    totalBytes += (length - i);
                }
                decodeLineSuffix(ps, bStream);
            } catch (Exception e) {
                break;
            }
        }
        decodeBufferSuffix(ps, bStream);
    }

    public byte decodeBuffer(String inputString)[] throws Exception {
        byte    inputBuffer[] = new byte[inputString.length()];
        ByteArrayInputStream inStream;
        ByteArrayOutputStream outStream;

        inputString.getBytes(0, inputString.length(), inputBuffer, 0);
        inStream = new ByteArrayInputStream(inputBuffer);
        outStream = new ByteArrayOutputStream();
        decodeBuffer(inStream, outStream);
        return (outStream.toByteArray());
    }

    /**
     * Decode the contents of the inputstream into a buffer.
     */
    public byte decodeBuffer(InputStream in)[] throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        decodeBuffer(in, outStream);
        return (outStream.toByteArray());
    }

    /**
     * Decode the contents of the String into a ByteBuffer.
     */
    public ByteBuffer decodeBufferToByteBuffer(String inputString)
            throws Exception {
        return ByteBuffer.wrap(decodeBuffer(inputString));
    }

    /**
     * Decode the contents of the inputStream into a ByteBuffer.
     */
    public ByteBuffer decodeBufferToByteBuffer(InputStream in)
            throws Exception {
        return ByteBuffer.wrap(decodeBuffer(in));
    }
}
