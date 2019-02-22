package Utilies;

import java.io.Serializable;

public enum  Direction implements Serializable {
    Stand(4),
    Right(1),
    Left(2),
    Up(3),
    Down(4);

    private int value;

    Direction(int value){
        this.value = value;
    }

    public int getValue(){
        return value;
    }

    public static Direction ParseDirection(String s){
        switch (s) {
            case "Down":
                return Direction.Down;
            case "Right":
                return Direction.Right;
            case "Left":
                return Direction.Left;
            case "Up":
                return Direction.Up;
            case "Stand":
                return Direction.Stand;
            case "4":
                return Direction.Down;
            case "3":
                return Direction.Up;
            case "2":
                return Direction.Left;
            case "1":
                return Direction.Right;
            default:
                return Direction.Stand;
        }
    }

}
