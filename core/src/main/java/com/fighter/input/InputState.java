package com.fighter.input;

public record InputState(
    float axisX,
    float axisY,
    boolean run,
    boolean jump,
    boolean attackOne,
    boolean attackTwo,
    boolean attackThree,
    boolean shield
) {}




