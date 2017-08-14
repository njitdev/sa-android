/*
    sa-android
    Copyright (C) 2017 sa-android authors

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.njitdev.sa_android.models.school;

public class ClassSchedule {
    private int week;
    private int day_of_week;
    private String classes_in_day;
    private String title;
    private String instructor;
    private String location;
    private String type;

    public int getWeek() {
        return week;
    }

    public int getDay_of_week() {
        return day_of_week;
    }

    public String getClasses_in_day() {
        return classes_in_day;
    }

    public String getTitle() {
        return title;
    }

    public String getInstructor() {
        return instructor;
    }

    public String getLocation() {
        return location;
    }

    public String getType() {
        return type;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public void setDay_of_week(int day_of_week) {
        this.day_of_week = day_of_week;
    }

    public void setClasses_in_day(String classes_in_day) {
        this.classes_in_day = classes_in_day;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setType(String type) {
        this.type = type;
    }
}
