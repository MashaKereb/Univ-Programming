package lucifer;

class Permutator {

    private static final int[] P_ORDER =
                   {10, 21, 52, 56, 27, 1, 47, 38,
                    18, 29, 60, 0, 35, 9, 55, 46,
                    26, 37, 4, 8, 43, 17, 63, 54,
                    34, 45, 12, 16, 51, 25, 7, 62,
                    42, 53, 20, 24, 59, 33, 15, 6,
                    50, 61, 28, 32, 3, 41, 23, 14,
                    58, 5, 36, 40, 11, 49, 31, 22,
                    2, 13, 44, 48, 19, 57, 39, 30};

    int[] permute(int[] input) {

        int[] output = new int[64];
        for (int i = 0; i < 64; i++)
            output[i] = input[P_ORDER[i]];

        return output;
    }
}

