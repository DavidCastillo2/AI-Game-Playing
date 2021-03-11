package HelperClasses;

public class Values {
    int utility, alpha, beta;

    public Values(int utility, int alpha, int beta){
        this.utility=utility;
        this.alpha = alpha;
        this.beta = beta;
    }
    public int getAlpha(){
        return alpha;
    }
    public int getBeta(){
        return beta;
    }

    public int getUtility(){
        return utility;
    }

    public void setAlpha(int alpha){
        this.alpha= alpha;
    }

    public void setBeta(int beta){
        this.beta= beta;
    }

}
