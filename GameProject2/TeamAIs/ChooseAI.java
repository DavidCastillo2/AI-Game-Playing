package TeamAIs;

import ProjectTwoEngine.Player;

public class ChooseAI {

    public static Player buildAI(String ai) {
        return getBot(ai);
    }

    private static Player getBot(String ai) {
        return switch (ai.toLowerCase()) {
            case "basic" -> new BasicBoi2000();
            default -> null;
        };
    }
}
