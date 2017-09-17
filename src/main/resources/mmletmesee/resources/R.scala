package mmletmesee.resources

import java.net.URL

class R

object R {
  def apply[T](path: String, relativeToDirOfClass: Class[T] = classOf[R]): URL = {
    relativeToDirOfClass getResource(path) ensuring(_ != null, 
        "Invalid path or resorce not found: " + path + " relative to: " +
        relativeToDirOfClass.getProtectionDomain().getCodeSource().getLocation().getPath())
  }
}