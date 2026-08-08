package com.example.explomod.registries;

import com.google.errorprone.annotations.ForOverride;
import org.jetbrains.annotations.ApiStatus;

public class LevelScaling {
    protected saveMethod method;
    private final int i;
    private final float f;
    private final long l;
    private final double d;

    public LevelScaling(int j){
        method=saveMethod.INT;
        this.i=j;
        this.f=0;
        this.l=0;
        this.d=0;
    }

    public LevelScaling(int i, float f, long l, double d){
        method=saveMethod.INT;
        this.i=i;
        this.f=f;
        this.l=l;
        this.d=d;
    }

    public LevelScaling(float j){
        method=saveMethod.FLOAT;
        this.i=0;
        this.f=j;
        this.l=0;
        this.d=0;
    }

    public LevelScaling(long j){
        method=saveMethod.LONG;
        this.i=0;
        this.f=0;
        this.l=j;
        this.d=0;
    }
    public LevelScaling(double j){
        method=saveMethod.DOUBLE;
        this.i=0;
        this.f=0;
        this.l=0;
        this.d=j;
    }

    public LevelScaling(){
        method=saveMethod.OTHER;
        this.i=0;
        this.f=0;
        this.l=0;
        this.d=0;
    }

    public int getInt() {
        return i;
    }

    public double getDouble() {
        return d;
    }

    public float getFloat() {
        return f;
    }

    public long getLong() {
        return l;
    }


    public Number getValue(){
        if(this.method==saveMethod.DOUBLE){
            return d;
        } else if (method==saveMethod.FLOAT) {
            return f;
        } else if (method==saveMethod.INT) {
            return i;
        } else if (method==saveMethod.LONG) {
            return l;
        }else {
            return 0;
        }
    }

    public saveMethod getMethod() {
        return method;
    }

    public enum saveMethod{
        INT,
        FLOAT,
        DOUBLE,
        LONG,
        OTHER
    }

    public static class IntOnly extends LevelScaling{

        public IntOnly(int i){
            super(i);
        }

        @Override
        public int getInt() {
            return super.getInt();
        }

        @Override
        public saveMethod getMethod() {
            return saveMethod.INT;
        }

        @Override
        public Number getValue() {
            return getInt();
        }

        @Override
        public float getFloat() {
            return (float) getInt();
        }

        @Override
        public double getDouble() {
            return (double) getInt();
        }

        @Override
        public long getLong() {
            return (long) getInt();
        }
    }

    public static class DoubleOnly extends LevelScaling{

        public DoubleOnly(double d){
            super(d);
        }

        @Override
        public int getInt() {
            return (int) getDouble();
        }

        @Override
        public saveMethod getMethod() {
            return saveMethod.DOUBLE;
        }

        @Override
        public Number getValue() {
            return getDouble();
        }

        @Override
        public float getFloat() {
            return (float) getDouble();
        }

        @Override
        public double getDouble() {
            return (double) super.getDouble();
        }

        @Override
        public long getLong() {
            return (long) getDouble();
        }
    }

    public static class FloatOnly extends LevelScaling{

        public FloatOnly(float f){
            super(f);
        }

        @Override
        public int getInt() {
            return (int) super.getFloat();
        }

        @Override
        public saveMethod getMethod() {
            return saveMethod.FLOAT;
        }

        @Override
        public Number getValue() {
            return super.getFloat();
        }

        @Override
        public float getFloat() {
            return (float) super.getFloat();
        }

        @Override
        public double getDouble() {
            return (double) super.getFloat();
        }

        @Override
        public long getLong() {
            return (long) super.getFloat();
        }
    }
}
