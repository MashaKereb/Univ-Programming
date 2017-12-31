package lucifer;

public class StringPadding {

	public static void main (String args[]){
		
		String s = "jsdhfkjsdhfkjsdhfkjsdhfkj";
		String []sa = padString(s);
		s = "";

        for (String aSa : sa) s += aSa;

		System.out.println(unpadString(s));
		
	}

     public static String[] padString(String input){
           
        String output = input + "1";
        int y = (output.length() / 16) + 1;
        String array[] = new String[y];
        int x = output.length() % 16;

        if (x!= 0)
            while (x < 16){
                output = output + "`";
                x++;
            }

        for (int i = 0; i < y; i++ )
        	array[i] = output.substring(i*16, (i*16) + 16);

        return array;
    }

     public static String unpadString(String input){
    	String output = input;
       
    	int length = output.length();
        int y = (length - 1);
       
        //Find the first '1' from the end and form substring
        while (y > -1)
            if (output.charAt(y) == '`'){
                y--;
            } else if (output.charAt(y) == '1'){
                output = output.substring(0, (y));
                break;
            }
       
        return output;
    }
}


