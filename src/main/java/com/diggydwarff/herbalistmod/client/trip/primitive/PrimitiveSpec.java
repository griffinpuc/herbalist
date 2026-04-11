package com.diggydwarff.herbalistmod.client.trip.primitive;

import java.util.EnumSet;

public final class PrimitiveSpec {
    public final String id;
    public final Tags tag;
    public final boolean major;
    public final float weight; // base selection weight
    public final EnumSet<Tags> conflicts;

    public PrimitiveSpec(String id, Tags tag, boolean major, float weight, EnumSet<Tags> conflicts) {
        this.id = id;
        this.tag = tag;
        this.major = major;
        this.weight = weight;
        this.conflicts = conflicts == null ? EnumSet.noneOf(Tags.class) : conflicts;
    }
}