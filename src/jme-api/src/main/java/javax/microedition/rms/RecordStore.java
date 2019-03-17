/**
 * This file is part of FreeJ2ME.
 *
 * FreeJ2ME is free software: you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * FreeJ2ME is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with FreeJ2ME. If not,
 * see http://www.gnu.org/licenses/
 *
 */
package javax.microedition.rms;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Vector;
import org.recompile.mobile.Mobile;

public class RecordStore {

  public static final int AUTHMODE_ANY = 1;
  public static final int AUTHMODE_PRIVATE = 0;

  private final String name;
  private final String appname;
  private static String rmsPath;
  private final String rmsFile;
  private File file;
  private int version = 0;
  private int nextid = 0;
  private final Vector<byte[]> records;
  private final Vector<RecordListener> listeners;
  private int lastModified = 0;

  private RecordStore(final String recordStoreName, final boolean createIfNecessary) throws RecordStoreException, RecordStoreNotFoundException {
    Mobile.debug("> RecordStore " + recordStoreName);
    records = new Vector<>();
    listeners = new Vector<>();
    records.add(new byte[] {}); // dummy record (record ids start at 1)
    int count;
    int offset;
    int reclen;
    name = recordStoreName;
    appname = Mobile.getPlatform().loader.getSuiteName();
    RecordStore.rmsPath = "rms/" + appname;
    rmsFile = "rms/" + appname + "/" + recordStoreName;
    try {
      Files.createDirectories(Paths.get(RecordStore.rmsPath));
    }
    catch (final Exception e) {
      Mobile.debug("> Problem Creating Record Store Path: " + RecordStore.rmsPath);
      Mobile.log(e.getMessage());
      throw (new RecordStoreException("Problem Creating Record Store Path " + RecordStore.rmsPath));
    }
    try {
      // Check Record Store File
      file = new File(rmsFile);
      if (!file.exists()) {
        if (createIfNecessary) {
          Mobile.debug("> Creating New Record Store " + appname + "/" + recordStoreName);
          file.createNewFile();
          version = 1;
          nextid = 1;
          count = 0;
          save();
          nextid = 1;
        }
        else {
          throw (new RecordStoreNotFoundException("Record Store Doesn't Exist: " + rmsFile));
        }
      }
    }
    catch (final Exception e) {
      Mobile.warn(e.getMessage());
      throw (new RecordStoreException("Problem Opening Record Store (createIfNecessary " + createIfNecessary + "): " + rmsFile));
    }

    try // Read Records
    {
      final Path path = Paths.get(file.getAbsolutePath());
      final byte[] data = Files.readAllBytes(path);

      if (data.length >= 4) {
        offset = 0;
        version = getUInt16(data, offset);
        offset += 2;
        nextid = getUInt16(data, offset);
        offset += 2;
        count = getUInt16(data, offset);
        offset += 2;
        for (int i = 0; i < count; i++) {
          reclen = getUInt16(data, offset);
          offset += 2;

          loadRecord(data, offset, reclen);
          offset += reclen;
        }
      }
    }
    catch (final Exception e) {
      Mobile.log("Problem Reading Record Store: " + rmsFile);
      Mobile.log(e.getMessage());
      throw (new RecordStoreException("Problem Reading Record Store: " + rmsFile));
    }
  }

  private void save() {
    final byte[] temp = new byte[2];
    try {
      final FileOutputStream fout = new FileOutputStream(rmsFile);
      // version //
      setUInt16(temp, 0, version);
      fout.write(temp);
      // next record id //
      setUInt16(temp, 0, nextid);
      fout.write(temp);
      // record count //
      setUInt16(temp, 0, records.size() - 1);
      fout.write(temp);
      // records //
      for (int i = 1; i < records.size(); i++) {
        setUInt16(temp, 0, records.get(i).length);
        fout.write(temp);
        fout.write(records.get(i));
      }
      fout.close();
    }
    catch (final Exception e) {
      Mobile.log("Problem Saving RecordStore");
      e.printStackTrace();
    }
  }

  private void loadRecord(final byte[] data, final int offset, final int numBytes) {
    byte[] rec = Arrays.copyOfRange(data, offset, offset + numBytes);
    if (rec == null) {
      rec = new byte[] {};
    }
    records.addElement(rec);
  }

  private int getUInt16(final byte[] data, final int offset) {
    int out = 0;

    out |= ((data[offset]) & 0xFF) << 8;
    out |= ((data[offset + 1]) & 0xFF);

    return out | 0x00000000;
  }

  private void setUInt16(final byte[] data, final int offset, final int val) {
    data[offset] = (byte)((val >> 8) & 0xFF);
    data[offset + 1] = (byte)((val) & 0xFF);
  }

  public int addRecord(final byte[] data, final int offset, final int numBytes) throws RecordStoreException {
    Mobile.debug("> Add Record " + nextid + " to " + name);
    try {
      final byte[] rec = Arrays.copyOfRange(data, offset, offset + numBytes);
      records.addElement(rec);

      lastModified = nextid;
      nextid++;
      version++;

      save();

      for (int i = 0; i < listeners.size(); i++) {
        listeners.get(i).recordAdded(this, lastModified);
      }
      return lastModified;
    }
    catch (final Exception e) {
      Mobile.debug("> Add Record Failed");
      throw (new RecordStoreException("Can't Add RMS Record"));
    }
  }

  public void addRecordListener(final RecordListener listener) {
    listeners.add(listener);
  }

  public void closeRecordStore() {
  }

  public void deleteRecord(final int recordId) {
    version++;
    Mobile.debug("> Delete Record");
    records.set(recordId, new byte[] {});
    save();
    for (int i = 0; i < listeners.size(); i++) {
      listeners.get(i).recordDeleted(this, recordId);
    }
  }

  public static void deleteRecordStore(final String recordStoreName) {
    try {
      final File fstore = new File("rms/" + Mobile.getPlatform().loader.getSuiteName() + "/" + recordStoreName);
      fstore.delete();
    }
    catch (final Exception e) {
      Mobile.error("Problem deleting RecordStore " + recordStoreName, e);
    }
    System.gc();
  }

  public RecordEnumeration enumerateRecords(final RecordFilter filter, final RecordComparator comparator, final boolean keepUpdated) {
    Mobile.log("RecordStore.enumerateRecords");
    return new enumeration(filter, comparator, keepUpdated);
  }

  public long getLastModified() {
    return lastModified;
  }

  public String getName() {
    return name;
  }

  public int getNextRecordID() {
    Mobile.debug("> getNextRecordID");
    return nextid;
  }

  public int getNumRecords() {
    Mobile.debug("> getNumRecords");
    int count = 0;
    for (int i = 1; i < records.size(); i++) // count deleted records
    {
      if (records.get(i).length == 0) {
        count++;
      }
    }
    count = records.size() - (1 + count);
    if (count < 0) {
      count = 0;
    }
    return count;
  }

  public byte[] getRecord(final int recordId) throws InvalidRecordIDException, RecordStoreException {
    Mobile.debug("> getRecord(" + recordId + ")");
    if (recordId >= records.size()) {
      Mobile.debug("getRecord Invalid RecordId " + recordId);
      throw (new InvalidRecordIDException("(A) Invalid Record ID: " + recordId));
    }
    try {
      final byte[] t = records.get(recordId);
      if (t.length == 0) { return null; }
      return t;
    }
    catch (final Exception e) {
      Mobile.error("(getRecord) Record Store Exception: " + recordId, e);
      throw (new RecordStoreException());
    }
  }

  public int getRecord(final int recordId, final byte[] buffer, final int offset) throws InvalidRecordIDException, RecordStoreException {
    Mobile.debug("> getRecord(id, buffer, offset)");
    final byte[] temp = getRecord(recordId);

    if (temp == null) { return 0; }

    int len = temp.length;

    while ((offset + len) > buffer.length) {
      len--;
    }

    for (int i = 0; i < len; i++) {
      buffer[offset + i] = temp[i];
    }
    return len;
  }

  public int getRecordSize(final int recordId) throws InvalidRecordIDException, RecordStoreException {
    Mobile.debug("> Get Record Size");
    return getRecord(recordId).length;
  }

  public int getSize() {
    return 32767;
  }

  public int getSizeAvailable() {
    return 65536;
  }

  public int getVersion() {
    return version;
  }

  public static String[] listRecordStores() {
    Mobile.debug("List Record Stores");
    if (RecordStore.rmsPath == null) {
      RecordStore.rmsPath = "rms/" + Mobile.getPlatform().loader.getName();
      try {
        Files.createDirectories(Paths.get(RecordStore.rmsPath));
      }
      catch (final Exception e) {
      }
    }
    try {
      final File folder = new File(RecordStore.rmsPath);
      final File[] files = folder.listFiles();
      final String[] out = new String[files.length];
      for (int i = 0; i < files.length; i++) {
        Mobile.debug((files[i].toString()).substring(RecordStore.rmsPath.length() + 1));
        out[i] = (files[i].toString()).substring(RecordStore.rmsPath.length() + 1);
      }
      return out;
    }
    catch (final Exception e) {
    }
    return null;
  }

  public static RecordStore openRecordStore(final String recordStoreName, final boolean createIfNecessary) throws RecordStoreException, RecordStoreNotFoundException {
    Mobile.debug("Open Record Store A " + createIfNecessary);
    return new RecordStore(recordStoreName, createIfNecessary);
  }

  public static RecordStore openRecordStore(final String recordStoreName, final boolean createIfNecessary, final int authmode, final boolean writable) throws RecordStoreException, RecordStoreNotFoundException {
    Mobile.debug("Open Record Store B " + createIfNecessary);
    return new RecordStore(recordStoreName, createIfNecessary);
  }

  public static RecordStore openRecordStore(final String recordStoreName, final String vendorName, final String suiteName) throws RecordStoreException, RecordStoreNotFoundException {
    Mobile.log("Open Record Store C");
    return new RecordStore(recordStoreName, false);
  }

  public void removeRecordListener(final RecordListener listener) {
    listeners.remove(listener);
  }

  public void setMode(final int authmode, final boolean writable) {
  }

  public void setRecord(final int recordId, final byte[] newData, final int offset, final int numBytes) throws RecordStoreException, InvalidRecordIDException {
    Mobile.debug("> Set Record " + recordId + " in " + name);
    if (recordId >= records.size()) { throw (new InvalidRecordIDException("(C) Invalid Record ID: " + recordId)); }
    try {
      final byte[] rec = Arrays.copyOfRange(newData, offset, offset + numBytes);
      records.set(recordId, rec);
    }
    catch (final Exception e) {
      Mobile.error("Problem in Set Record", e);
    }
    lastModified = recordId;
    save();
    for (int i = 0; i < listeners.size(); i++) {
      listeners.get(i).recordChanged(this, recordId);
    }
  }

  /* ************************************************************
  			RecordEnumeration implementation
      *********************************************************** */

  private class enumeration implements RecordEnumeration {

    private int index;
    private int[] elements;
    private int count;
    private boolean keepupdated;
    RecordFilter filter;
    RecordComparator comparator;

    public enumeration(final RecordFilter filter, final RecordComparator comparator, final boolean keepUpdated) {
      keepupdated = keepUpdated;
      index = 0;
      this.filter = filter;
      this.comparator = comparator;
      build();
    }

    private void build() {
      elements = new int[records.size() + 1];
      for (int i = 0; i < (records.size() + 1); i++) {
        elements[i] = 1;
      }
      count = 0;
      if (filter == null) {
        Mobile.debug("Not Filtered");
        for (int i = 1; i < records.size(); i++) {
          if (records.get(i).length > 0) // not deleted
          {
            elements[count] = i;
            count++;
          }
        }
      }
      else {
        Mobile.debug("Filtered");
        for (int i = 1; i < records.size(); i++) {
          if (filter.matches(records.get(i))) {
            if (records.get(i).length > 0) // not deleted
            {
              elements[count] = i;
              count++;
            }
          }
        }
      }

      int result = 0;
      int temp;
      if (comparator != null) {
        Mobile.debug("Comparator");
        for (int i = 0; i < (count - 1); i++) {
          for (int j = 0; j < (count - (1 + i)); j++) {
            result = comparator.compare(records.get(elements[j]), records.get(elements[j + 1]));
            if (result == RecordComparator.FOLLOWS) {
              temp = elements[j];
              elements[j] = elements[j + 1];
              elements[j + 1] = temp;
            }

          }
        }
      }
    }

    @Override
    public void destroy() {
    }

    @Override
    public boolean hasNextElement() {
      if (keepupdated) {
        rebuild();
      }
      if (index < count) { return true; }
      return false;
    }

    @Override
    public boolean hasPreviousElement() {
      if (keepupdated) {
        rebuild();
      }
      if (index > 0) { return true; }
      return false;
    }

    @Override
    public boolean isKeptUpdated() {
      return keepupdated;
    }

    @Override
    public void keepUpdated(final boolean keepUpdated) {
      keepupdated = keepUpdated;
    }

    @Override
    public byte[] nextRecord() throws InvalidRecordIDException {
      Mobile.debug("> Next Record");
      if (keepupdated) {
        rebuild();
      }
      if (index >= count) { throw (new InvalidRecordIDException()); }
      index++;
      return records.get(elements[index - 1]);
    }

    @Override
    public int nextRecordId() throws InvalidRecordIDException {
      Mobile.debug("> Next Record ID (idx:" + index + " cnt:" + count + ")");
      if (keepupdated) {
        rebuild();
      }
      if (index >= count) { throw (new InvalidRecordIDException()); }
      return elements[index];
    }

    @Override
    public int numRecords() {
      Mobile.debug("> numRecords()");
      if (keepupdated) {
        rebuild();
      }
      return count;
    }

    @Override
    public byte[] previousRecord() throws InvalidRecordIDException {
      Mobile.debug("> Previous Record");
      if (keepupdated) {
        rebuild();
      }
      index--;
      if (index >= 0) { return records.get(elements[index]); }
      if (index < 0) {
        index = count - 1;
        return records.get(elements[index]);
      }
      return null;
    }

    @Override
    public int previousRecordId() throws InvalidRecordIDException {
      Mobile.debug("> Previous Record ID");
      if (keepupdated) {
        rebuild();
      }
      if (index == 0) { throw (new InvalidRecordIDException()); }
      return elements[index - 1];
    }

    @Override
    public void rebuild() {
      build();
      if (index >= count) {
        index = count - 1;
      }
    }

    @Override
    public void reset() {
      if (keepupdated) {
        rebuild();
      }
      index = 0;
    }
  }

}
