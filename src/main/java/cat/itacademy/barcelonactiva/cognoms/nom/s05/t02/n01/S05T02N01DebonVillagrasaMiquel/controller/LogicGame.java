package cat.itacademy.barcelonactiva.cognoms.nom.s05.t02.n01.S05T02N01DebonVillagrasaMiquel.controller;

public class LogicGame {
    public static int PLAY(){
        int range = 7;
        int min = 1;
        return (int)(Math.random() * range) + min;
    }
}
