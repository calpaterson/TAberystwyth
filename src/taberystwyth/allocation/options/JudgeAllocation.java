package taberystwyth.allocation.options;

final class Balanced implements TabAlgorithm{
    @Override
    public String getName() {
        return "Balanced";
    }
    @Override
    public String getDescription() {
        return "The judges are assigned to rooms in a round-robin, balanced way";
    }
}