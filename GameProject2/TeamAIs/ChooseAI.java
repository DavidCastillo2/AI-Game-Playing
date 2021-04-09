package TeamAIs;

import ProjectTwoEngine.Player;

public class ChooseAI {

    public static Player buildAI(String ai, String topOrBot) {
        return getBot(ai, topOrBot);
    }

    private static Player getBot(String ai, String topOrBot) {
        return switch (ai.toLowerCase()) {
            case "basic" -> new BasicBoi2000(topOrBot);
            case "aggresive" -> new Aggresive(topOrBot);
            case "con" -> new Conservative(topOrBot);
            default -> null;
        };
    }
}
