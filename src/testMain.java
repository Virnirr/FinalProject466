public class testMain {
    public static void main (String[] args) {
        String[] available_cards = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Queen", "King", "Jack", "Ace"};
        java.util.Random random = new java.util.Random();
        int random_computer_card = random.nextInt(available_cards.length);
        System.out.println(available_cards[random_computer_card]);
    }
}
