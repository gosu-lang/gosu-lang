# Replace Gosu Enhancements with Java Extensions

Gosu enhancements enable both customers and internal developers to logically add functionality to entities and other
types they otherwise can't modify. Guidewire application APIs are heavily dependant on this feature.  But enhancements
are exclusive to Gosu and not readily accessible from Java. As a consequence our APIs are incomplete and difficult to
use from Java. Here I propose Java Extensions from the Manifold project as a replacement for Gosu enhancements. This
functionality is nearly identical to enhancements, but is fully and equally accessible to *both* Gosu and Java, which
means we can also begin leveraging extensions in our own internal Java code.

## The Problem

When our customer-facing Java API switched from the "external" API to a non-delegating Java API something was lost in
the translation: Enhancements.    
Slack thread: https://app.slack.com/client/T031BLS5Z/DED2MCVEF/thread/CEJU0QN2D-1591825108.071300

(show a simple example using e.g., Claim)

## The Solution

(show the same enhancement as a Java extension )

## Work Involved

- Compile Gosu with Javac 

## Risks

- What if Manifold 