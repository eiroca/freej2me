/**
 * Copyright (C) 2006-2019 eIrOcA (eNrIcO Croce & sImOnA Burzio) - GPL >= 3.0
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/
 */
package net.eiroca.j2me.host;

import net.eiroca.j2me.host.freej2me.FreeeJ2meHost;

public class J2meHost implements Runnable {

  private Class<?> midlet;

  public J2meHost(Class<?> midlet) {
    this.midlet = midlet;
  }

  @Override
  public void run() {
    new FreeeJ2meHost(midlet);
  }

}
