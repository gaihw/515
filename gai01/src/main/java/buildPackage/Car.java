package buildPackage;

import java.awt.*;

/**
 * Builder模式
 *
 * @author gaihw
 * @date 2022/10/5 00:01
 */
public class Car {
    Color color;

    public Color getColor() {
        return color;
    }

    public double getPrice() {
        return price;
    }

    public String getBrand() {
        return brand;
    }

    public String getDisplacement() {
        return displacement;
    }

    double price;

    String brand;

    String displacement;

    private Car(Builder builder) {
        this.color = builder.color;
        this.price = builder.price;
        this.brand = builder.brand;
        this.displacement = builder.displacement;
    }

    public static class Builder {
        Color color;
        double price;
        String brand;
        String displacement;

        public Builder color(Color color){
            this.color=color;
            return this;
        }

        public Builder price(double price){
            this.price=price;
            return this;
        }

        public Builder brand(String brand){
            this.brand=brand;
            return this;
        }

        public Builder displacement(String displacement){
            this.displacement=displacement;
            return this;
        }

        public Car build() {
            return new Car(this);
        }
    }

}
