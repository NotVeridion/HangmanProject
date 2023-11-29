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
        String word;
        char[] word_copy = null;

        Stickman enemy = new Stickman(100, 18);
        User user = new User(70, 23);

        int startHP = user.getHP();

        boolean userLose = false;
        boolean enemyLose = false;

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

        Scanner input = new Scanner(System.in);
        // From here is the process of choosing the word for combat
        System.out.println();
        System.out.println(); // Print out the enemy hp and damage
        word = getNewWord(words); // Get a new word from the array list
        word_copy = new char[word.length()];

        // Mask the chosen word so the user sees their progress on the word over time
        for (int i = 0; i < word.length(); i++) {
            if (word_copy[i] != ' ') {
                word_copy[i] = '_';
            }
        }

        System.out.println("Battle commencing...");

        // Printing out the battlefield
        enemy.printEnemy();
        user.printUser();
        System.out.println("Word: " + String.valueOf(word_copy));

        boolean battleFinished = false;
        while (!battleFinished) {

            // Next, check for if the current word is already done
            boolean wordComplete = true;
            for (char c : word_copy) {
                if (c == '_') {
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
                        word_copy[i] = '_';
                    }
                }
                System.out.println("Word: " + String.valueOf(word_copy));
            }


            // Display actions
            System.out.println("----------------------------------------------");
            System.out.println("1: Fight");
            System.out.println("2: Heal");
            System.out.println("3: Do Nothing");
            System.out.println("4: Run");
            System.out.println();
            System.out.print("What would you like to do: ");

            char choice = input.next().charAt(0);

            switch (choice) {
                case '1':
                    // Display word for guessing
                    enemy.printEnemy();
                    user.printUser();
                    System.out.println();
                    System.out.println("Word: " + String.valueOf(word_copy));
                    System.out.println();
                    System.out.print("Choose a letter: ");
                    char letter = input.next().charAt(0);

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
                        enemy.printEnemy();
                        user.printUser();
                        System.out.println("Letter found, Enemy hit!");
                        System.out.println("Enemy has lost " + user.getDamage() + " hp!");
                        System.out.println();
                        System.out.println("Enemy loses concentration and misses!");
                        System.out.println();
                        System.out.println("Word: " + String.valueOf(word_copy));
                    } else {
                        System.out.println("Letter not found. Your Attack Missed!");
                        System.out.println();
                        enemy.attack(user);
                    }

                    break; // Turn has ended and the user can choose what to do next
                case '2':
                    user.heal();
                    if (user.getHP() > startHP) {
                        user.setHP(startHP);
                    }
                    enemy.printEnemy();
                    user.printUser();
                    System.out.println("User Healed.");
                    System.out.println("The enemy is Enraged...");
                    System.out.println("Enemy damage increased!");
                    System.out.println("Word: " + String.valueOf(word_copy));
                    enemy.setDamage(enemy.getDamage() + (int) (enemy.getDamage() * 0.2));
                    break;
                case '3':
                    enemy.printEnemy();
                    user.printUser();
                    enemy.attack(user);
                    break;
                case '4':
                    battleFinished = true;
                    System.out.println("You have successfully run away.");
                    break;
                default:
                    enemy.printEnemy();
                    user.printUser();
                    System.out.println("Please Enter a Proper Action");
            }

            if (user.getHP() <= 0) {
                userLose = true;
            }
            if (enemy.getHP() <= 0) {
                enemyLose = true;
            }

            // Finally check for win/lose conditions to close the while loop
            if (enemyLose) {
                System.out.println("----------------------------------------------");
                System.out.println("Enemy has been Defeated!");
                System.out.println(" __     __           ____  _____  U  ___ u  ____    __   __ ");
                System.out.println(" \\ \\   /\"/u ___   U /\"___||_ \" _|  \\/\"_ \\U |  _\"\\ u \\ \\ / / ");
                System.out.println("  \\ \\ / // |_\"_|  \\| | u    | |    | | | |\\| |_) |/  \\ V /  ");
                System.out.println("  /\\ V /_,-.| |    | |/__  /| |.-,_| |_| | |  _ <   U_|\"|_u ");
                System.out.println(" U  \\_/-(_U/| |\\u   \\____|u |_|U\\_)-\\___/  |_| \\_\\    |_|   ");
                System.out.println("   //  .-,_|___|_,-_// \\\\ _// \\\\_    \\\\    //   \\\\.-,//|(_  ");
                System.out.println("  (__)  \\_)-' '-(_(__)(__(__) (__)  (__)  (__)  (__\\_) (__) ");
                System.out.println();
                battleFinished = true;
            } else if (userLose) {
                System.out.println("----------------------------------------------");
                System.out.println("YOU HAVE DIED.");
                System.out.println(" _______   _______  _______  _______     ___   .___________.");
                System.out.println("|       \\ |   ____||   ____||   ____|   /   \\  |           |");
                System.out.println("|  .--.  ||  |__   |  |__   |  |__     /  ^  \\ `---|  |----`");
                System.out.println("|  |  |  ||   __|  |   __|  |   __|   /  /_\\  \\    |  |     ");
                System.out.println("|  '--'  ||  |____ |  |     |  |____ /  _____  \\   |  |     ");
                System.out.println("|_______/ |_______||__|     |_______/__/     \\__\\  |__|     ");
                System.out.println();
                battleFinished = true;
            }

        }

        input.close();
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
            System.out.println("Enemy attacked... and hits! User has lost " + damage + " hp");
        } else {
            System.out.println("Enemy attacked... but misses!");
        }
    }

    public void printEnemy() {
        System.out.println("----------------------------------------------");
        System.out.println("                        HP: " + hp + " | " + "Damage: " + damage);
        System.out.println("                              /\\ \\  / /\\");
        System.out.println("                             //\\\\ .. //\\\\");
        System.out.println("                             //\\((  ))/\\\\");
        System.out.println("                             /  < `' >  \\");
        System.out.println("                           ----------------");
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
        System.out.println("         /\\_/\\");
        System.out.println("        ( o.o )");
        System.out.println("         > ^ <");
        System.out.println("       ---------");
        System.out.println("HP: " + hp + " | " + "Damage: " + damage);
        System.out.println("----------------------------------------------");
    }

}
