public class StopCondition implements Condition{


    private Operator operator;

    public StopCondition(Operator operator){
        this.operator = operator;
    }

    @Override
    public boolean isMatch(int distance, int stops) {
        return operator.op(stops);
    }

}
