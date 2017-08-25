package com.example.poster.easysellingsellers.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by POSTER on 07.08.2017.
 */

public class Product {
    private String id;
    private String name;
    private String description;
    private String photoUri;
    private Double price;
    private String weight;
    private String color;
    private boolean availability;
    private int count;

    public Product() {
    }

    public Product(String id, String name, String description, String photoUri, Double price, String weight, String color, boolean availability, int count) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.photoUri = photoUri;
        this.price = price;
        this.weight = weight;
        this.color = color;
        this.availability = availability;
        this.count = count;
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name", name);
        result.put("description", description);
        result.put("photoUri", photoUri);
        result.put("price", price);
        result.put("weight", weight);
        result.put("color", color);
        result.put("availability", availability);
        result.put("count", count);
        return result;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhotoUries() {
        return photoUri;
    }

    public void setPhotoUries(String photoUri) {
        this.photoUri = photoUri;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
