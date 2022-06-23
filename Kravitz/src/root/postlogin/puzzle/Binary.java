package root.postlogin.puzzle;

import root.postlogin.puzzle.memory.MemoryTable;

import java.util.Stack;

public class Binary {
    private int index, a, b, c, d, e, f, g, h;

    public int getIndex() {
        return index;
    }

    public int getA() {
        return a;
    }

    public int getB() {
        return b;
    }

    public int getC() {
        return c;
    }

    public int getD() {
        return d;
    }

    public int getE() {
        return e;
    }

    public int getF() {
        return f;
    }

    public int getG() {
        return g;
    }

    public int getH() {
        return h;
    }

    public Binary(int index, int a, int b, int c, int d, int e, int f, int g, int h) {

        this.index = index;
        binaryStack.push(this);
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
        this.f = f;
        this.g = g;
        this.h = h;
    }

    public Binary() { }

    public static int getNum(Binary binary) {
        return binary.h + binary.g * 2 + binary.f * 4 + binary.e * 8 + binary.d * 16 + binary.c * 32 + binary.b * 64 + binary.a * 128;
    }


    public static void changeBinary(int index, int num) {

        final String binaryString = Integer.toBinaryString(num);
        String finalBinaryString = "";

        int length = binaryString.length();
        for (int i = length; i < 8; i++) { // Padding
            finalBinaryString = finalBinaryString + "0";
        }

        finalBinaryString = finalBinaryString + binaryString;

        char[] characterStrings = finalBinaryString.toCharArray();

        Binary newBinary = new Binary();
        newBinary.index=index;
        // -48 turns it from char to num
        newBinary.a = characterStrings[0] - 48;
        newBinary.b = characterStrings[1] - 48;
        newBinary.c = characterStrings[2] - 48;
        newBinary.d = characterStrings[3] - 48;
        newBinary.e = characterStrings[4] - 48;
        newBinary.f = characterStrings[5] - 48;
        newBinary.g = characterStrings[6] - 48;
        newBinary.h = characterStrings[7] - 48;

        binaryStack.set(0, newBinary);
    }

    public static void updateMemoryTable(int indexAtMemoryTable, int indexOfBinary) {
        MemoryTable.getTableView().getItems().set(indexAtMemoryTable, binaryStack.get(indexOfBinary));
    }


    public static int getValueOfFourthBinary() {
        Binary fourthBinary = binaryStack.get(3);
        return fourthBinary.h + fourthBinary.g * 2 + fourthBinary.f * 4 + fourthBinary.e * 8 + fourthBinary.d * 16 + fourthBinary.c * 32 + fourthBinary.b * 64 + fourthBinary.a * 128;
    }


    private final static Stack<Binary> binaryStack = new Stack<>();

    public static Stack<Binary> getBinaryStack() {
        return binaryStack;
    }

    public static void pushAX(int AX) {

        Stack<Binary> clonedBinaryStack = (Stack<Binary>) binaryStack.clone();
        for (int i=0; i<10; i++) {
            binaryStack.get(i).index++;
        }

        for (int i=1; i<10; i++) {
            binaryStack.set(i, clonedBinaryStack.get(i-1));
        }

        changeBinary(0, AX);

        for (int i=0; i<10; i++) {
            updateMemoryTable(i, i);
        }




    }
}
