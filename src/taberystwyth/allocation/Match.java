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
import java.util.List;

/**
 * The Match class represents what is currently referred to as a "room" in the
 * schema.
 */
public class Match {
    
    /** The first prop. */
    private String firstProp;
    
    /** The second prop. */
    private String secondProp;
    
    /** The first op. */
    private String firstOp;
    
    /** The second op. */
    private String secondOp;
    
    /** The chair. */
    private String chair;
    
    /** The wings. */
    private List<String> wings = new ArrayList<String>();
    
    /** The location. */
    private String location;
    
    /** The rank. */
    private int rank;
    
    /**
     * Instantiates a new match.
     * 
     * @param rank
     *            the rank
     */
    public Match(int rank) {
        this.rank = rank;
    }
    
    /**
     * Gets the first prop.
     * 
     * @return the first prop
     */
    public synchronized String getFirstProp() {
        return firstProp;
    }
    
    /**
     * Sets the first prop.
     * 
     * @param firstProp
     *            the new first prop
     */
    public synchronized void setFirstProp(String firstProp) {
        this.firstProp = firstProp;
    }
    
    /**
     * Gets the second prop.
     * 
     * @return the second prop
     */
    public synchronized String getSecondProp() {
        return secondProp;
    }
    
    /**
     * Sets the second prop.
     * 
     * @param secondProp
     *            the new second prop
     */
    public synchronized void setSecondProp(String secondProp) {
        this.secondProp = secondProp;
    }
    
    /**
     * Gets the first op.
     * 
     * @return the first op
     */
    public synchronized String getFirstOp() {
        return firstOp;
    }
    
    /**
     * Sets the first op.
     * 
     * @param firstOp
     *            the new first op
     */
    public synchronized void setFirstOp(String firstOp) {
        this.firstOp = firstOp;
    }
    
    /**
     * Gets the second op.
     * 
     * @return the second op
     */
    public synchronized String getSecondOp() {
        return secondOp;
    }
    
    /**
     * Sets the second op.
     * 
     * @param secondOp
     *            the new second op
     */
    public synchronized void setSecondOp(String secondOp) {
        this.secondOp = secondOp;
    }
    
    /**
     * Gets the chair.
     * 
     * @return the chair
     */
    public synchronized String getChair() {
        return chair;
    }
    
    /**
     * Sets the chair.
     * 
     * @param chair
     *            the new chair
     */
    public synchronized void setChair(String chair) {
        this.chair = chair;
    }
    
    /**
     * Gets the wings.
     * 
     * @return the wings
     */
    public synchronized List<String> getWings() {
        return wings;
    }
    
    /**
     * Gets the location.
     * 
     * @return the location
     */
    public synchronized String getLocation() {
        return location;
    }
    
    /**
     * Sets the location.
     * 
     * @param location
     *            the new location
     */
    public synchronized void setLocation(String location) {
        this.location = location;
    }
    
    /**
     * Gets the rank.
     * 
     * @return the rank
     */
    public synchronized int getRank() {
        return rank;
    }
    
    /**
     * Sets the rank.
     * 
     * @param rank
     *            the new rank
     */
    public synchronized void setRank(int rank) {
        this.rank = rank;
    }
    
    /**
     * Checks whether a chair has been set.
     * 
     * @return true, if successful
     */
    public synchronized boolean hasChair() {
        return chair != null;
    }
    
    /**
     * Adds a wing.
     * 
     * @param name
     *            the name
     */
    public void addWing(String name) {
        wings.add(name);
    }
}
