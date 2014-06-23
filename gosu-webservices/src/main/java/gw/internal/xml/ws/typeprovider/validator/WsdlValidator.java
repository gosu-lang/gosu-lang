/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.ws.typeprovider.validator;

import gw.internal.xml.xsd.typeprovider.schema.WsdlBinding;
import gw.internal.xml.xsd.typeprovider.schema.WsdlBindingOperation;
import gw.internal.xml.xsd.typeprovider.schema.WsdlDefinitions;
import gw.internal.xml.xsd.typeprovider.schema.WsdlPart;
import gw.internal.xml.xsd.typeprovider.schema.WsdlPort;
import gw.internal.xml.xsd.typeprovider.schema.WsdlPortTypeFault;
import gw.internal.xml.xsd.typeprovider.schema.WsdlPortTypeOperation;
import gw.internal.xml.xsd.typeprovider.schema.WsdlService;
import gw.xml.ws.WsdlValidationException;

public class WsdlValidator {

  public static void validateWsdl( WsdlDefinitions definitions ) {
    try {
      for ( WsdlService wsdlService : definitions.getServices().values() ) {
        for ( WsdlPort wsdlPort : wsdlService.getPorts() ) {
          if ( wsdlPort.getSoapAddresses().isEmpty() ) {
            continue; // not a soap service
          }
          WsdlBinding wsdlBinding = wsdlPort.getBinding();
          for ( WsdlBindingOperation wsdlBindingOperation : wsdlBinding.getBindingOperations() ) {
            WsdlPortTypeOperation wsdlPortTypeOperation = wsdlBinding.getPortType().getOperationByName( wsdlBindingOperation.getName() );
            if ( wsdlPortTypeOperation.getInput() != null ) {
              for ( WsdlPart wsdlPart : wsdlPortTypeOperation.getInput().getMessage().getParts() ) {
                validatePart( wsdlPart );
              }
            }
            if ( wsdlPortTypeOperation.getOutput() != null ) {
              for ( WsdlPart wsdlPart : wsdlPortTypeOperation.getOutput().getMessage().getParts() ) {
                validatePart( wsdlPart );
              }
            }
            for ( WsdlPortTypeFault wsdlPortTypeFault : wsdlPortTypeOperation.getFaults() ) {
              for ( WsdlPart wsdlPart : wsdlPortTypeFault.getMessage().getParts() ) {
                validatePart( wsdlPart );
              }
            }
          }
        }
      }
    }
    catch ( Exception ex ) {
      throw new WsdlValidationException( "Unable to validate WSDL " + definitions.getSchemaIndex().getXSDSourcePath(), ex );
    }
  }

  private static void validatePart( WsdlPart wsdlPart ) {
    if ( wsdlPart.getType() != null ) {
      throw new WsdlValidationException( "Part '" + wsdlPart.getName() + "' contains an RPC-style type reference. Type references are not supported. Wsdl2Gosu can be used to translate an RPC-style WSDL into a Document-style WSDL.");
    }
    if ( wsdlPart.getElement() == null ) {
      throw new WsdlValidationException( "Part '" + wsdlPart.getName() + "' does not contain an element reference." );
    }
  }

}
