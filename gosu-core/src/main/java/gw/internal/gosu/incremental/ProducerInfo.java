/*
 * Copyright 2026 Guidewire Software, Inc.
 */

package gw.internal.gosu.incremental;

import java.util.HashSet;
import java.util.Set;

/**
 * One type's record in the dependency graph, in memory and in the dependency file alike: the hex
 * SHA-1 of its ABI ({@link IncrementalCompilationManager#NO_ABI_HASH} for a type gosuc never
 * compiled, i.e. a local Java type, and for a producer that was merely referenced during this
 * build) and the consumers that must recompile when it changes.
 */
class ProducerInfo
{
  String abiHash;
  final Set<String> consumers;

  ProducerInfo( String abiHash, Set<String> consumers )
  {
    this.abiHash = abiHash;
    this.consumers = consumers;
  }

  ProducerInfo()
  {
    this( IncrementalCompilationManager.NO_ABI_HASH, new HashSet<>() );
  }
}
