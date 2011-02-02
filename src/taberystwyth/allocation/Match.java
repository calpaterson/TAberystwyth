package taberystwyth.allocation;

import java.util.ArrayList;

public class Match {
    private String firstProp;
    private String secondProp;
    private String firstOp;
    private String secondOp;
    private String chair;
    private ArrayList<String> wings = new ArrayList<String>();
    private String location;
    private int rank;
    public Match(int rank) {
        this.rank = rank;
    }
    public synchronized String getFirstProp() {
        return firstProp;
    }
    public synchronized void setFirstProp(String firstProp) {
        this.firstProp = firstProp;
    }
    public synchronized String getSecondProp() {
        return secondProp;
    }
    public synchronized void setSecondProp(String secondProp) {
        this.secondProp = secondProp;
    }
    public synchronized String getFirstOp() {
        return firstOp;
    }
    public synchronized void setFirstOp(String firstOp) {
        this.firstOp = firstOp;
    }
    public synchronized String getSecondOp() {
        return secondOp;
    }
    public synchronized void setSecondOp(String secondOp) {
        this.secondOp = secondOp;
    }
    public synchronized String getChair() {
        return chair;
    }
    public synchronized void setChair(String chair) {
        this.chair = chair;
    }
    public synchronized ArrayList<String> getWings() {
        return wings;
    }
    public synchronized String getLocation() {
        return location;
    }
    public synchronized void setLocation(String location) {
        this.location = location;
    }
    public synchronized int getRank() {
        return rank;
    }
    public synchronized void setRank(int rank) {
        this.rank = rank;
    }
    public synchronized boolean hasChair(){
        return chair != null;
    }
    public void addWing(String name) {
        wings.add(name);        
    }
}
