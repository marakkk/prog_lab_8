package common.models;

import java.io.Serializable;
import java.util.Objects;

public class Coordinates implements Serializable,Validator {
    private Float x;
    private float y;
    public Coordinates(Float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Float getX() {
        return x;
    }

    public void setX(Float x){
        this.x = x;
    }

    public float getY() {
        return y;
    }
    public void setY(Float y){
        this.y = y;
    }
    public float getRadius(){
        return (float) (Math.pow(getX(), 2) + Math.pow(getY(), 2));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Coordinates)) return false;
        Coordinates that = (Coordinates) o;
        return getX().equals(that.getX()) && getY()==(that.getY());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY());
    }

    @Override
    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }

    @Override
    public boolean validate() {
        if (this.x <= -206) return false;
        return !(this.y == 0 || this.y > 463);
    }
}
