# Java 11 with Gosu on Manifold 

Manifold is an Open Source project hosted on [GitHub](https://github.com/manifold-systems/manifold) maintained by Scott 
McKinney. Manifold is both an API to build metaprogramming features that integrate directly with Java and a set of 
components built atop the API such as type-safe JSON Schema / YAML support, templating, and extension methods. 

The core API is designed to expose any kind of structured data as a first-class type in Java with no extra build 
step involved.  Essentially it enables the Java compiler to accept files other than `.java` files as source code.  This 
is how, for example, a JSON Schema file can be directly accessed in Java source as a Java class.  This is also what 
makes Gosu a natural fit as a Manifold plugin.
 
### Layered Architecture

Manifold's architecture consists of several layers:

* Core (type identity management)
  * Extensions (string interpolation, extension methods, structural typing)
    * Templates
    * JSON Schema
    * YAML / OpenAPI
    * Collections/Text/Web Ext
  * JavaScript
  * Gosu
  * etc. 


Gosu requires only the _Core_ layer, which is a relatively compact dependency.  The core layer interfaces with the Java compiler
and runtime to enable an API consumer to register a custom _type manifold_ as a service.  A type manifold implements 
an API to expose resources such as JSON files to Java as a set of types.  As such the Gosu type manifold exposes all the 
`*.gs*` files as Java classes. Since we currently aren't interested in interop _from_ Java we only require Manifold 
_runtime_ services i.e., dynamic loading of Gosu types as Java classes.  That said we get comprehensive Java interop 
for free with Manifold if we choose to use it down the road.

### Why Use Manifold?

#### It's finished
  * I finished this work several months ago -- Gosu fully supports Java 11 _right now_
   
#### Integrates with ths Java Platform Module System (JPMS)
  * Enables Gosu to dynamically load classes into Java's new module-oriented type system.
  * Gosu's current architecture only supports URL-based dynamic loading. It is missing critical components to work in a JPMS environment (bypass ModuleReaders, JRT-based, URI-oriented file i/o)
	
#### Parse Java source and support Java interop from source
  * Gosu currently supports 100% Java interop _from_ Gosu since Ferrite (9), which entails parsing Java source. 
  * Manifold's Java parser is designed to work with Java 11 / JPMS, Gosu's Java parser is not.
	* Mostly this is about low-level integration with javac's source/class file manager and handling the intricacies of 
	symbol management regarding module access provided by Manifold.
	
#### Dynamically access otherwise inaccessible packages in `jdk.compiler`, `jdk.javadoc`, `java.base`, etc.
  * Although Gosu will use Java 11 in single-module mode, there are still some 50+ modules involved with just the JRE. The JPMS enforces the boundaries defined in these modules.
  * Gosu and our platform use various packages that are now illegal references, some of these can be forced to work via compiler and runtime flags, some not.
	* Manifold provides a consistent, type-safe API to dynamically access packages across module boundaries without affecting build or runtime configurations.
	
#### Improved design, simplicity 
  * Gosu as a Manifold plugin simplifies Gosu's design.
  * It shifts much of the "Java hacking" burden out of Gosu as abstractions in Manifold    
	
#### Manifold itself
  * Manifold provides a host of practical features I'd like to use in Gosu's design and implementation these include:
    * String interpolation
    * Extension methods
    * Structural interfaces
    * Extension libraries for collections, I/O, and text
    * Self type support
    * Type-safe reflection
    * Access to other type plugins: JSON Schema, YAML, Images, Templates, etc.
  * Gosu is in a much better position to be replaced with Java as a hedge against our current plans
  
#### It's finished
  * I finished this work several months ago -- Gosu fully supports Java 11 _right now_  
  
	
	