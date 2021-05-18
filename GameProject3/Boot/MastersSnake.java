package Boot;

import HelperClasses.Reinforcement.QLearningImproved;
import ProjectThreeEngine.DirType;
import ProjectThreeEngine.GameState;
import ProjectThreeEngine.Player;

public class MastersSnake implements Player {
    int play_num;
    QLearningImproved brain;
    @Override
    public void begin(GameState init_state, int play_num) {
        this.play_num = play_num;
        this.brain = QLearningImproved.getSelf(play_num);
        this.brain.initialize(init_state, play_num, play_num==0?1:0);
    }

    @Override
    public DirType getMove(GameState state) {
        return this.brain.getMove(state);
    }

    @Override
    public String getPlayName() {
        return "I have a Master's degree";
    }
}
