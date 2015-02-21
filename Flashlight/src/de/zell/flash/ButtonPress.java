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
 *
 * @author Christopher Zell <zelldon91@googlemail.com>
 */
public class ButtonPress {
  
  private Integer count;
  private final Date firstButtonPressedTime;

  public ButtonPress() {
    count = 1;
    firstButtonPressedTime = new Date();
  }

  public Integer getCount() {
    return count;
  }

  public Date getFirstButtonPressedTime() {
    return firstButtonPressedTime;
  }
  
  public void incrementPress() {
    count++;
  }

  @Override
  public String toString() {
    return "ButtonPress{" + "count=" + count + ", firstButtonPressedTime=" + firstButtonPressedTime + '}';
  }
  
}
