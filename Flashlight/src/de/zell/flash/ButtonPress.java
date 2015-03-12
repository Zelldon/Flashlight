/*
 * Copyright (C) 2015 Christopher Zell <zelldon91@googlemail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.zell.flash;

import java.util.Date;

/**
 * Represents the ButtonPress class which was used to count how
 * often a button was pressed.
 * 
 * Contains also the date of the first press.
 * 
 * @author Christopher Zell <zelldon91@googlemail.com>
 */
public class ButtonPress {
  
  /**
   * The count of the pressed buttons.
   */
  private Integer count;
  
  /**
   * The time of the first pressed button.
   */
  private final Date firstButtonPressedTime;

  /**
   * The default ctor which initializes the count with 1 and the 
   * date with the current time.
   */
  public ButtonPress() {
    count = 1;
    firstButtonPressedTime = new Date();
  }

  /**
   * Returns the button pressed count.
   * 
   * @return the count
   */
  public Integer getCount() {
    return count;
  }

  /**
   * Returns the time of the first pressed button
   * 
   * @return the time
   */
  public Date getFirstButtonPressedTime() {
    return firstButtonPressedTime;
  }
  
  /**
   * Increments the count of pressed buttons.
   */
  public void incrementPress() {
    count++;
  }

  @Override
  public String toString() {
    return "ButtonPress{" + "count=" + count + ", firstButtonPressedTime=" + firstButtonPressedTime + '}';
  }
  
}
