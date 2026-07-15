package com.example.explomod.utill;

public class MoreMath{

    public static float round(int cutOff, float roundNum){
        int e = (int) exponent(10, cutOff);
        double root = roundNum*e;
        double cut = Math.round(root);
        double result = cut/e;
        return (float) result;
    }

    public static double exponent(double num, int exponent){
        double multiplier = num;
        for (int i = 0; i < exponent-1; i++) {
            multiplier*=num;
        }
        if(exponent==0){
            return 0;
        }else{
            return multiplier;
        }
    }
}
