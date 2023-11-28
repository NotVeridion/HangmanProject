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

    public static String getNewWord(ArrayList<String> words) {
        Random rand = new Random();
        int index;
        String word;

        index = rand.nextInt(words.size());
        word = words.get(index);
        words.remove(index);

        return word;
    }

    public static void main(String[] args) throws Exception {
        Boolean end = false;
        String word;
        char[] word_copy = null;

        Stickman enemy = new Stickman(100, 18);
        User user = new User(70, 23);

        int startHP = user.getHP();

        // Creating arraylist of words for random word selection
        ArrayList<String> words = new ArrayList<String>();

        try {
            // Reads from words.txt file and puts everything into the arraylist
            FileReader inFile = new FileReader("words.txt");
            BufferedReader buffer = new BufferedReader(inFile);

            // Word chosen
            while ((word = buffer.readLine()) != null) {
                words.add(word);
            }

            buffer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        while (!end) {
            Scanner input = new Scanner(System.in);
            // From here is the process of choosing the word for combat
            System.out.println();
            System.out.println(); // Print out the enemy hp and damage
            word = getNewWord(words); // Get a new word from the array list
            word_copy = new char[word.length()];

            // Mask the chosen word so the user sees their progress on the word over time
            for (int i = 0; i < word.length(); i++) {
                if (word_copy[i] != ' ') {
                    word_copy[i] = '*';
                }
            }

            System.out.println("Battle commencing...");
            boolean battleFinished = false;
            while (!battleFinished) {

                // First check for win/lose conditions
                if (enemy.getHP() <= 0) {
                    System.out.println("Enemy defeated!");
                    battleFinished = true;
                } else if (words.isEmpty()) {
                    System.out.println("No more words remaining. You lose");
                    battleFinished = true;
                } else if (user.getHP() <= 0) {
                    System.out.println("You have died. Game Over");
                    battleFinished = true;
                }

                // Printing out the battlefield
                System.out.println("----------------------------------------------");
                enemy.printEnemy();
                user.printUser();
                System.out.println();

                // Display actions
                System.out.println("1: Fight");
                System.out.println("2: Heal");
                System.out.println("3: Run");
                int choice = input.nextInt();

                switch (choice) {
                    case 1:
                        // Display word for guessing
                        System.out.println("Word: " + String.valueOf(word_copy));
                        System.out.println("Choose a letter: ");
                        char letter = input.next().charAt(0);

                        System.out.println("----------------------------------------------");
                        System.out.println("Result: ");
                        System.out.println();

                        // Check word for user's letter
                        boolean found = false;
                        for (int i = 0; i < word.length(); i++) {
                            if (word.charAt(i) == letter) {
                                word_copy[i] = letter;
                                found = true;
                            }
                        }

                        // Finally check the actions
                        if (found) {
                            user.attack(enemy);
                            System.out.println("Enemy hit! Enemy has lost " + user.getDamage() + " hp!");
                            System.out.println();
                            enemy.attack(user);
                        } else {
                            System.out.println("Letter not found. Missed!");
                            System.out.println();
                            enemy.attack(user);
                        }

                        break; // Turn has ended and the user can choose what to do next
                    case 2:
                        user.heal();
                        if (user.getHP() > startHP) {
                            user.setHP(startHP);
                        }
                        System.out.println("The enemy is powering up...");
                        enemy.setDamage(enemy.getDamage() + (int) (enemy.getDamage() * 0.2));
                }

                // For when the enemy is still alive but the word is done
                boolean wordComplete = true;  
                for (char c : word_copy) {  
                    if (c == '*') {  
                        wordComplete = false;  
                        break;  
                    }  
                }  

                if (wordComplete) {  
                    System.out.println("Word complete. Choosing new word.");  
                    System.out.println();  
                    word = getNewWord(words); // Get a new word from the array list  
                    word_copy = new char[word.length()];  

                    // Mask the chosen word so the user sees their progress on the word over time
                    for (int i = 0; i < word.length(); i++) {
                        if (word_copy[i] != ' ') {
                            word_copy[i] = '*';
                        }
                    }

                    System.out.println("For comparison");
                    System.out.println(word);
                    System.out.println(word_copy);
                }  

            }
            
            input.close();
        }
    }

}

class Stickman {
    private int hp;
    private int damage;

    public Stickman(int hp, int damage) {
        this.hp = hp;
        this.damage = damage;
    }

    public int getDamage() {
        return damage;
    }

    public int getHP() {
        return hp;
    }

    public void setDamage(int val) {
        this.damage = val;
    }

    public void setHP(int hp) {
        this.hp = hp;
    }

    public void attack(User user) {
        double val = Math.random();
        if (val >= 0.7) { // 70% chance to hit
            user.setHP(user.getHP() - damage);
            System.out.println("Stickman attacked... and hits! User has lost " + damage + " hp");
        } else {
            System.out.println("Stickman attacked... but misses!");
        }
    }

    public void printEnemy() {
        System.out.println("                        HP: " + hp + " | " + "Damage: " + damage);
        System.out.println();
        System.out.println("                                        >:(");
        System.out.println("                                       ------");
        System.out.println();
        System.out.println();
    }
}

class User {
    private int hp;
    private int damage;

    public User(int hp, int damage) {
        this.hp = hp;
        this.damage = damage;
    }

    public int getHP() {
        return hp;
    }

    public void setHP(int hp) {
        this.hp = hp;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void heal() {
        int amount = (int) (hp * 0.3);
        System.out.println("Healing. User has healed " + amount + " hp");
        setHP(hp + amount);
    }

    public void attack(Stickman enemy) {
        enemy.setHP(enemy.getHP() - damage);
    }

    public void printUser() {
        System.out.println();

        System.out.println("          :O");
        System.out.println("         ------");

        System.out.println();
        System.out.println();
        System.out.println("HP: " + hp + " | " + "Damage: " + damage);
    }

}
