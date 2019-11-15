package model.bean;

public class Foto {

    private byte[] binary;

    public Foto(byte[] source) {
        this.binary = source;
    }

    public byte[] getBinary() {
        return binary;
    }

    public void setBinary(byte[] binary) {
        this.binary = binary;
    }
}
