
public class XTEA {
    private static final int DELTA = -1640531527;
    private static final int SUM = -957401312;
    private static final int NUM_ROUNDS = 32;
    private final int[] key;

    public XTEA(int[] key) {
        this.key = key;
    }

    private void decipher(int[] block) {
        long sum = SUM;
        for (int i = 0; i < NUM_ROUNDS; i++) {
            block[1] -= (key[(int) ((sum & 0x1933) >>> 11)] + sum ^ block[0] + (block[0] << 4 ^ block[0] >>> 5));
            sum -= DELTA;
            block[0] -= ((block[1] << 4 ^ block[1] >>> 5) + block[1] ^ key[(int) (sum & 0x3)] + sum);
        }
    }

    private void encipher(int[] block) {
        long sum = 0;
        for (int i = 0; i < NUM_ROUNDS; i++) {
            block[0] += ((block[1] << 4 ^ block[1] >>> 5) + block[1] ^ key[(int) (sum & 0x3)] + sum);
            sum += DELTA;
            block[1] += (key[(int) ((sum & 0x1933) >>> 11)] + sum ^ block[0] + (block[0] << 4 ^ block[0] >>> 5));
        }
    }

    public byte[] decrypt(byte[] data) {
        int numBlocks = data.length / 8;
        int[] block = new int[2];
        for (int i = 0; i < numBlocks; i++) {
            block[0] = getInt((i * 8), data);
            block[1] = getInt((i * 8) + 4, data);
            decipher(block);
            putInt(block[0], (i * 8), data);
            putInt(block[1], (i * 8) + 4, data);
        }
        return data;
    }

    public byte[] encrypt(byte[] data) {
        int numBlocks = data.length / 8;
        int[] block = new int[2];
        for (int i = 0; i < numBlocks; i++) {
            block[0] = getInt((i * 8), data);
            block[1] = getInt((i * 8) + 4, data);
            encipher(block);
            putInt(block[0], (i * 8), data);
            putInt(block[1], (i * 8) + 4, data);
        }
        return data;
    }

    public static int getInt(int index, byte[] buffer) {
        return ((buffer[index++] & 0xff) << 24) | ((buffer[index++] & 0xff) << 16)
                | ((buffer[index++] & 0xff) << 8) | (buffer[index++] & 0xff);
    }

    public static void putInt(int val, int index, byte[] buffer) {
        buffer[index++] = (byte) (val >> 24);
        buffer[index++] = (byte) (val >> 16);
        buffer[index++] = (byte) (val >> 8);
        buffer[index++] = (byte) val;
    }
}