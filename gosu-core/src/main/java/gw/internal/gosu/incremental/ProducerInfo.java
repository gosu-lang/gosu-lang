/*
 * Copyright 2026 Guidewire Software, Inc.
 */

package gw.internal.gosu.incremental;

import java.util.HashSet;
import java.util.Set;

/**
 * One type's record in the dependency graph, in memory and in the dependency file alike: its ABI
 * hash and the consumers that must recompile when it changes. The hash is the hex SHA-1 of the
 * type's ABI; {@link IncrementalCompilationManager#CANNOT_COMPUTE_ABI_HASH} for a compiled class
 * whose surface names a structure, which is not hashed; or
 * {@link IncrementalCompilationManager#NO_ABI_HASH} for a type gosuc never compiled, i.e. a local
 * Java type, and, in this build's records, for a producer that was merely referenced.
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
