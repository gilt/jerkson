package com.codahale.jerkson

import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.`type`.{TypeFactory, ArrayType}
import scala.collection.JavaConverters._
import java.util.concurrent.ConcurrentHashMap

private[jerkson] object Types {
  private val cachedTypes = new ConcurrentHashMap[Manifest[_], JavaType]().asScala

  def build(factory: TypeFactory, manifest: Manifest[_]): JavaType =
    cachedTypes.getOrElseUpdate(manifest, constructType(factory, manifest))

  private def constructType(factory: TypeFactory, manifest: Manifest[_]): JavaType = {
    if (manifest.erasure.isArray) {
      ArrayType.construct(factory.constructType(manifest.erasure.getComponentType), null)
    } else if (manifest.typeArguments.size > 0) {
      factory.constructParametricType(
        manifest.erasure,
        manifest.typeArguments.map {m => build(factory, m)}.toArray: _*)
    } else {
      factory.constructType(manifest.erasure)
    }
  }
}
