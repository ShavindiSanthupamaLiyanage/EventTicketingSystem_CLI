public class Logo {

    public static void printBoxedMessage(String message) {
        int messageLength = message.length();
        int boxWidth = messageLength + 4; // Add padding for the border

        // Create the top and bottom border
        String border = "*".repeat(boxWidth);

        // Print the box
        System.out.println(border);
        System.out.println("* " + message + " *");
        System.out.println(border);
    }

}
