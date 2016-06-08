public class LT implements Operator {
    private int base;

    public LT(int base) {
        this.base = base;
    }

    @Override
    public boolean op(int distance) {
        return base > distance;
    }
}
