package org.epicycloide_back.epicycloide_back.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import org.epicycloide_back.epicycloide_back.validation.GreaterThan;
import org.springframework.validation.annotation.Validated;
import org.epicycloide_back.epicycloide_back.util.FractionConverter;

import java.util.ArrayList;
import java.util.HashMap;

@Entity
public class Epicycloid {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;

    private String name;

    @GreaterThan(limit = 0.0)
    private Double radius;

    @Column(nullable = true)
    @GreaterThan(limit = 0.0)
    private Double frequency;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "rolling_id")
    @Valid
    private Epicycloid rolling;

    @OneToOne(mappedBy = "rolling", cascade = {CascadeType.ALL})
    @JsonIgnore
    @Valid
    private Epicycloid fixed;


    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Double getRadius() {
        return radius;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
    }

    public Epicycloid getRolling() {
        return rolling;
    }

    public void setRolling(Epicycloid rolling) {
        this.rolling = rolling;
    }

    public Epicycloid getFixed() {
        return fixed;
    }

    public void setFixed(Epicycloid fixed) {
        this.fixed = fixed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getFrequency() {
        return frequency;
    }

    public void setFrequency(Double frequency) {
        this.frequency = frequency;
    }

    public ArrayList<Point> getCoordinates(int pointsNumber) {

        ArrayList<Point> coordinates = new ArrayList<Point>();

        for (double i = 0; i < pointsNumber; i++) {

            Epicycloid rolling = this;
            Epicycloid fixed = null;
            double baseFrequency = rolling.getFrequency();

            double x = 0;
            double y = 0;
//            double t = 0;
            double frequencySum = 0;

            double t = (double) 2 * Math.PI * i / pointsNumber;

            while (rolling != null) {


                frequencySum += rolling.getFrequency();

                if (fixed == null) {

//                    t = (double) 2 * Math.PI * i / pointsNumber;
                    x += rolling.getRadius() * Math.cos(t);
                    y += rolling.getRadius() * Math.sin(t);

                } else {

//                    FractionConverter.decimalToFraction(rolling.getRadius() / rolling.getFixed().getRadius())[1] *
//                    t = (double) 2 * Math.PI * i / pointsNumber;
                    x += rolling.getRadius() * Math.cos(frequencySum / baseFrequency * t);
                    y += rolling.getRadius() * Math.sin(frequencySum / baseFrequency * t);

                }

                rolling = rolling.getRolling();
                fixed = rolling;
            }

            Point point = new Point(x, y, t);
            coordinates.add(point);

        }

        return coordinates;

    }

    @Override
    public String toString() {
        return "Epicycloid [id=" + id + ", name=" + name + ", radius=" + radius + ", frequency=" + frequency + ", rolling=" + rolling;
    }
}
