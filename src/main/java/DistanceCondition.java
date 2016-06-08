public class DistanceCondition implements Condition{


    private Operator operator;

    public DistanceCondition(Operator operator){
        this.operator = operator;
    }

    @Override
    public boolean isMatch(int distance, int stops) {
        return operator.op(distance);
    }

}
