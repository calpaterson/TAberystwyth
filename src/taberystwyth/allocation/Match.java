/*
 * This file is part of TAberystwyth, a debating competition organiser
 * Copyright (C) 2010, Roberto Sarrionandia and Cal Paterson
 * 
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
