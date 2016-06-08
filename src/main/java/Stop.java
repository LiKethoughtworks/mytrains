public class Stop {
    public String name;

    public Stop(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || (this.getClass() != other.getClass())) {
            return false;
        }
        Stop otherStop = (Stop) other;
        return otherStop.name.equals(this.name);
    }
}
