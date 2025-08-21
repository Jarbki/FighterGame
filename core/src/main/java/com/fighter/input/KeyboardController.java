package com.fighter.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.fighter.Player;

public class KeyboardController {


   public InputState poll(){
       float x = 0f, y = 0f;

       // movement input detection
       if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT))  x -= 1f;
       if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) x += 1f;

       boolean run = (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) && (x != 0f));
       boolean jump = Gdx.input.isKeyPressed(Input.Keys.SPACE);
       boolean attackOne = Gdx.input.isKeyPressed(Input.Keys.NUM_1);
       boolean attackTwo = Gdx.input.isKeyPressed(Input.Keys.NUM_2);
       boolean attackThree = Gdx.input.isKeyPressed(Input.Keys.NUM_3);
       boolean shield = Gdx.input.isKeyPressed(Input.Keys.TAB);

       return new InputState(x, y, run, jump, attackOne, attackTwo, attackThree, shield);

   }



}
