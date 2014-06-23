/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.fs.physical.win32;

import gw.lang.UnstableAPI;

@UnstableAPI
public class Win32Util {

  // Various constants representing the standard Windows file system attributes, as pulled from MSDN

  // A file or directory that is an archive file or directory. Applications typically use this attribute to mark files for backup or removal.
  public static final int FILE_ATTRIBUTE_ARCHIVE = 0x20;

  // A file or directory that is compressed. For a file, all of the data in the file is compressed. For a directory, compression is the default for newly created files and subdirectories.
  public static final int FILE_ATTRIBUTE_COMPRESSED = 0x800;

  // This value is reserved for system use.
  public static final int FILE_ATTRIBUTE_DEVICE = 0x40;

  // The handle that identifies a directory.
  public static final int FILE_ATTRIBUTE_DIRECTORY = 0x10;

  // A file or directory that is encrypted. For a file, all data streams in the file are encrypted. For a directory, encryption is the default for newly created files and subdirectories.
  public static final int FILE_ATTRIBUTE_ENCRYPTED = 0x4000;

  // The file or directory is hidden. It is not included in an ordinary directory listing.
  public static final int FILE_ATTRIBUTE_HIDDEN = 0x2;

  // A file that does not have other attributes set. This attribute is valid only when used alone.
  public static final int FILE_ATTRIBUTE_NORMAL = 0x80;

  // The file or directory is not to be indexed by the content indexing service.
  public static final int FILE_ATTRIBUTE_NOT_CONTENT_INDEXED = 0x2000;

  // The data of a file is not available immediately. This attribute indicates that the file data is physically moved to offline storage. This attribute is used by Remote Storage, which is the hierarchical storage management software. Applications should not arbitrarily change this attribute.
  public static final int FILE_ATTRIBUTE_OFFLINE = 0x1000;

  // A file that is read-only. Applications can read the file, but cannot write to it or delete it. This attribute is not honored on directories. For more information, see "You cannot view or change the Read-only or the System attributes of folders in Windows Server 2003, in Windows XP, or in Windows Vista".
  public static final int FILE_ATTRIBUTE_READONLY = 0x1;

  // A file or directory that has an associated reparse point, or a file that is a symbolic link.
  public static final int FILE_ATTRIBUTE_REPARSE_POINT = 0x400;

  // A file that is a sparse file.
  public static final int FILE_ATTRIBUTE_SPARSE_FILE = 0x200;

  // A file or directory that the operating system uses a part of, or uses exclusively.
  public static final int FILE_ATTRIBUTE_SYSTEM = 0x4;

  // A file that is being used for temporary storage. File systems avoid writing data back to mass storage if sufficient cache memory is available, because typically, an application deletes a temporary file after the handle is closed. In that scenario, the system can entirely avoid writing the data. Otherwise, the data is written after the handle is closed.
  public static final int FILE_ATTRIBUTE_TEMPORARY = 0x100;

  // This value is reserved for system use.
  public static final int FILE_ATTRIBUTE_VIRTUAL = 0x10000;
}
