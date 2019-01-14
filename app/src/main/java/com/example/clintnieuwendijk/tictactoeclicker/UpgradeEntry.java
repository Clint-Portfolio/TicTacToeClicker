package com.example.clintnieuwendijk.tictactoeclicker;

import java.io.Serializable;

class UpgradeEntry implements Serializable {
    private int id, cost, tier;
    private String name, description;

    public UpgradeEntry(int id, int cost, int tier, String name, String description) {
        this.id = id;
        this.cost = cost;
        this.tier = tier;
        this.name = name;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getTier() {
        return tier;
    }

    public void setTier(int tier) {
        this.tier = tier;
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
}
