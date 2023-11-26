import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

// Planning on making it an rpg style hangman where the user and stickman have hp
// and dmg. Either the user beats the hangman and wins
// or runs out of hp and loses

// For now we work on the basic hangman game

public class App {

    public static char[] checkLetter(char letter, char[] word_copy, String word){
        for(int i = 0; i < word.length(); i++){
            if(word.charAt(i) == letter){
                word_copy[i] = letter;
            }
        }

        return word_copy;
    }
    public static void main(String[] args) throws Exception {

        //Random class for generating random int for word selection
        Random rand = new Random();
        Boolean end = false;
        String word;

        //Creating arraylist of words for random word selection
        ArrayList <String> words = new ArrayList <String> ();

        try {
            //Reads from words.txt file and puts everything into the arraylist
            FileReader inFile = new FileReader("words.txt");
            BufferedReader buffer = new BufferedReader(inFile);

            //Word chosen
            while((word = buffer.readLine()) != null){
                words.add(word);
            }

            buffer.close();
        } catch (Exception e) {
                e.printStackTrace();
        }

        
        while(end != true){
            //Randomly choose a word from the list
            int index = 0;

            //Checks if arraylist is empty
            if(words.size() > 0){
                index = rand.nextInt(words.size());
            }
            else{
                index = -1; //Will go to next else-statement and end the game
            }

            int total = 0;
            if(index >= 0){
                word = words.get(index);
                words.remove(index);            
            
                //Make a copy of the word for printing to the screen
                char[] word_copy = word.toCharArray();

                for(int i = 0; i < word_copy.length; i++){
                    if(word_copy[i] != ' '){
                        word_copy[i] = '*';
                    }
                }

                System.out.println(word);
                System.out.println(word_copy);

                Scanner input = new Scanner(System.in);

                while(total != word.length()){
                    System.out.println("Choose a letter: ");
                    char letter = input.next().charAt(0);
                    
                    word_copy = checkLetter(letter, word_copy, word);

                    System.out.println(word_copy);
                }

                input.close();

            }
            else{
                end = true;
            }


        }

    }
}

//Just in case we want to add other option tweaks.
/*
class Menu{
    public void printMenu(){
        System.out.println("What would you like to do?");
        System.out.println("1. Play");
    }
}
*/

class Stickman {
    int lives;
    //int damage;
    //int hp;

    public Stickman(int lives){
        this.lives = lives;
    }

    //you can use triple quotes to print exactly what you type in code instead of having to call println constantly
    String Enemy = """
            


                            >:(



            """;

    //Ignore this for now. I have an rpg idea for the game I'll work on. 
    public void printEnemy(){

        // System.out.println();
        // System.out.println();
        // System.out.println();

        // System.out.print("                     >:(                     ");

        // System.out.println();
        // System.out.println();
        // System.out.println();

        System.out.println(Enemy);
        
    }
}
