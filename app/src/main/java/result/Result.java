package result;
import java.io.IOException;
import java.io.Serializable;

public class Result implements Serializable{

    private int food = -1;
    private int confidence = -1;
    private byte[] image;

    public Result(int food, int conf, byte[] image){

        this.food = food;
        this.confidence = conf;
        this.image = image;

    }
    public int getFood(){
        return this.food;
    }

    public int getConfidence(){
        return this.confidence;
    }
    public byte[] getImage() throws IOException{

        return image;
    }

}