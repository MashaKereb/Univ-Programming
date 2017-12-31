package lucifer;


public class SBoxHandler {

    public byte Convert(int sBox, byte input) {

        byte output = 0;

        if (sBox == 0)
            if      (input == 0)  output = 12;
            else if (input == 1)  output = 15;
            else if (input == 2)  output = 7;
            else if (input == 3)  output = 10;
            else if (input == 4)  output = 14;
            else if (input == 5)  output = 13;
            else if (input == 6)  output = 11;
            else if (input == 7)  output = 0;
            else if (input == 8)  output = 2;
            else if (input == 9)  output = 6;
            else if (input == 10) output = 3;
            else if (input == 11) output = 1;
            else if (input == 12) output = 9;
            else if (input == 13) output = 4;
            else if (input == 14) output = 5;
            else if (input == 15) output = 8;
            else System.err.println("Invalid Argument");
        else if (sBox == 1)
            if (input == 0)       output = 7;
            else if (input == 1)  output = 2;
            else if (input == 2)  output = 14;
            else if (input == 3)  output = 9;
            else if (input == 4)  output = 3;
            else if (input == 5)  output = 11;
            else if (input == 6)  output = 0;
            else if (input == 7)  output = 4;
            else if (input == 8)  output = 12;
            else if (input == 9)  output = 13;
            else if (input == 10) output = 1;
            else if (input == 11) output = 10;
            else if (input == 12) output = 6;
            else if (input == 13) output = 15;
            else if (input == 14) output = 8;
            else if (input == 15) output = 5;
            else System.err.println("Invalid Argument");
        else
            System.err.println("Invalid Argument");

        return output;
    }
}


