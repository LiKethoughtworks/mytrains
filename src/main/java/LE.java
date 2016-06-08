public class LE implements Operator {
    private int base;

    public LE(int base) {
        this.base = base;
    }

    @Override
    public boolean op(int distance) {
        return base >= distance;
    }
}
