/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.stubs;

import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.intellij.util.containers.ContainerUtil;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GosuStubUtils {
  @NotNull
  public static List<Set<String>> deserializeCollectionsArray(@NotNull StubInputStream dataStream) throws IOException {
    //named parameters
    final byte namedParametersSetNumber = dataStream.readByte();
    final List<Set<String>> collArray = new ArrayList<>();

    for (int i = 0; i < namedParametersSetNumber; i++) {
      final byte curNamedParameterSetSize = dataStream.readByte();
      final String[] namedParameterSetArray = new String[curNamedParameterSetSize];

      for (int j = 0; j < curNamedParameterSetSize; j++) {
        namedParameterSetArray[j] = dataStream.readUTF();
      }
      Set<String> curSet = new HashSet<>();
      ContainerUtil.addAll(curSet, namedParameterSetArray);
      collArray.add(curSet);
    }
    return collArray;
  }

  public static void serializeCollectionsArray(@NotNull StubOutputStream dataStream, @NotNull Set<String>[] collArray) throws IOException {
    dataStream.writeByte(collArray.length);
    for (Set<String> namedParameterSet : collArray) {
      dataStream.writeByte(namedParameterSet.size());
      for (String namepParameter : namedParameterSet) {
        dataStream.writeUTF(namepParameter);
      }
    }
  }

  public static void writeStringArray(@NotNull StubOutputStream dataStream, @NotNull String[] array) throws IOException {
    dataStream.writeByte(array.length);
    for (String s : array) {
      dataStream.writeName(s);
    }
  }

  @NotNull
  public static String[] readStringArray(@NotNull StubInputStream dataStream) throws IOException {
    final byte b = dataStream.readByte();
    final String[] annNames = new String[b];
    for (int i = 0; i < b; i++) {
      annNames[i] = dataStream.readName().toString();
    }
    return annNames;
  }
}
