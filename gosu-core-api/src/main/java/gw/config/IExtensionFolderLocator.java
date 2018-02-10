/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.config;

import java.io.File;
import manifold.api.service.IService;

public interface IExtensionFolderLocator extends IService
{
  File getExtensionFolderPath();
}
