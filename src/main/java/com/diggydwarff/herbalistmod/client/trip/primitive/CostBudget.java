package com.diggydwarff.herbalistmod.client.trip.primitive;

public final class CostBudget {
    public int particleBudgetPerTick = 30;
    public int entityBudget = 12;
    public float gpuBudget = 1.0f; // arbitrary scale
    public float cpuBudget = 1.0f;

    public CostBudget copy() {
        CostBudget b = new CostBudget();
        b.particleBudgetPerTick = this.particleBudgetPerTick;
        b.entityBudget = this.entityBudget;
        b.gpuBudget = this.gpuBudget;
        b.cpuBudget = this.cpuBudget;
        return b;
    }
}